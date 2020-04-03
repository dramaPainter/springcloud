package drama.painter.web.mall.service.impl;

import drama.painter.web.mall.model.dto.order.RechargeDTO;
import drama.painter.web.mall.model.enums.CashTypeEnum;
import drama.painter.web.mall.model.enums.PartnerTypeEnum;
import drama.painter.web.mall.model.enums.RechargeMethodEnum;
import drama.painter.web.mall.mapper.RechargeMapper;
import drama.painter.web.mall.model.dto.order.PartnerDTO;
import drama.painter.web.mall.model.dto.order.PartnerRangeDTO;
import drama.painter.web.mall.model.vo.order.PartnerVO;
import drama.painter.web.mall.model.vo.order.RechargeVO;
import drama.painter.web.mall.model.vo.search.RechargeSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import drama.painter.core.web.misc.Page;
import drama.painter.web.mall.service.inf.IRecharge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class RechargeImpl implements IRecharge {
	@Autowired
	RechargeMapper rechargeMapper;

	static final List<PartnerVO> PARTNERS = new ArrayList();

	@Override
	public List<RechargeVO> getRecharges(RechargeSearch search, Page page) {
		List<RechargeDTO> recharge = rechargeMapper.getRecharge(search, page);
		List<RechargeVO> list = recharge.stream().map(x -> {
			String partner = getPartners().stream().filter(p -> p.getId().equals(x.getPartner())).findAny().get().getName();
			return new RechargeVO(x.getOrderid(), x.getUserid(), partner, x.getFixdate(), x.getEchodate(), x.getStatus(), x.getCash(), x.getMethod(), x.getIp(), x.getThirdid());
		}).collect(Collectors.toList());
		recharge.clear();
		return list;
	}

	@Override
	public List<PartnerVO> getPartners() {
		if (PARTNERS.isEmpty()) {
			synchronized (this) {
				if (PARTNERS.isEmpty()) {
					List<PartnerDTO> partners = rechargeMapper.getPartners();
					List<PartnerRangeDTO> ranges = rechargeMapper.getPartnerRange();
					RechargeMethodEnum[] enums = RechargeMethodEnum.values();
					for (PartnerDTO dto : partners) {
						byte method = dto.getMethod().byteValue();
						byte property = dto.getProperty().byteValue();
						PartnerVO vo = new PartnerVO(dto.getId(), dto.getName(), dto.getPeak());
						vo.setAlipay((method & ALIPAY) == ALIPAY);
						vo.setWxpay((method & WXPAY) == WXPAY);
						vo.setQqpay((method & QQPAY) == QQPAY);
						vo.setUnionpay((method & UNIONPAY) == UNIONPAY);
						vo.setApplepay((method & APPLEPAY) == APPLEPAY);
						vo.setDisplay((property & DISPLAY) == DISPLAY);
						vo.setRecharge((property & RECHARGE) == RECHARGE);
						vo.setForeign((property & FOREIGN) == FOREIGN);
						vo.setVip((property & VIP) == VIP);

						EnumMap<RechargeMethodEnum, PartnerVO.PartnerRangeVO> map = new EnumMap(RechargeMethodEnum.class);
						for (RechargeMethodEnum methodEnum : enums) {
							if (methodEnum.getValue() < ALIPAY) {
								continue;
							}

							Integer min = ranges.stream().filter(y -> y.getId().equals(dto.getId()) && y.getMethod().equals(methodEnum) && y.getType().equals(CashTypeEnum.MIN)).findAny().orElse(new PartnerRangeDTO(0)).getAmount();
							Integer max = ranges.stream().filter(y -> y.getId().equals(dto.getId()) && y.getMethod().equals(methodEnum) && y.getType().equals(CashTypeEnum.MAX)).findAny().orElse(new PartnerRangeDTO(0)).getAmount();
							List<Integer> fixed = ranges.stream().filter(y -> y.getId().equals(dto.getId()) && y.getMethod().equals(methodEnum) && y.getType().equals(CashTypeEnum.FIXED)).map(PartnerRangeDTO::getAmount).collect(Collectors.toList());
							Boolean enabled = (dto.getMethod() | methodEnum.getValue()) == methodEnum.getValue();
							map.put(methodEnum, new PartnerVO.PartnerRangeVO(dto.getId(), methodEnum, enabled, min, max, fixed));
						}

						vo.setRange(map);
						PARTNERS.add(vo);
					}
					ranges.clear();
					partners.clear();
				}
			}
		}
		return PARTNERS;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void enableRecharge(PartnerVO.PartnerRangeVO rangeVO) {
		PartnerDTO p = seekPartner(rangeVO.getId());
		int property = p.getMethod();
		int val = rangeVO.getMethod().getValue();
		property = rangeVO.getEnabled() ? (property | val) : ((property & val) == val ? property ^ val : property);
		p.setMethod((byte) property);
		rechargeMapper.updatePartner(p);
		PARTNERS.clear();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updatePartner(PartnerVO partnerVO) {
		PartnerDTO dto = seekPartner(partnerVO.getId());
		int property = dto.getProperty();
		property = partnerVO.getDisplay() ? (property | DISPLAY) : ((property & DISPLAY) == DISPLAY ? property ^ DISPLAY : property);
		property = partnerVO.getVip() ? (property | VIP) : ((property & VIP) == VIP ? property ^ VIP : property);
		dto.setProperty((byte) property);
		dto.setPeak(partnerVO.getPeak());

		List<PartnerRangeDTO> list = new ArrayList();
		partnerVO.getRange().values().forEach(x -> {
			list.add(new PartnerRangeDTO(x.getId(), x.getMethod(), CashTypeEnum.MIN, x.getMin()));
			list.add(new PartnerRangeDTO(x.getId(), x.getMethod(), CashTypeEnum.MAX, x.getMax()));
			x.getFixed().forEach(y -> list.add(new PartnerRangeDTO(x.getId(), x.getMethod(), CashTypeEnum.FIXED, y)));
		});

		rechargeMapper.updatePartner(dto);
		rechargeMapper.removePartnerRange(dto.getId());
		rechargeMapper.updatePartnerRange(list);
		PARTNERS.clear();
		list.clear();
	}

	PartnerDTO seekPartner(Short id) {
		Optional<PartnerVO> any = PARTNERS.stream().filter(x -> x.getId().equals(id)).findAny();
		if (!any.isPresent()) {
			throw new RuntimeException("渠道不存在，请刷新页面后重新操作。");
		}

		PartnerVO vo = any.get();
		PartnerDTO dto = new PartnerDTO();
		dto.setId(id);
		dto.setName(vo.getName());
		dto.setPeak(vo.getPeak());
		dto.setMethod((byte) (vo.getAlipay() ? ALIPAY : 0));
		dto.setMethod((byte) (dto.getMethod() + (vo.getWxpay() ? WXPAY : 0)));
		dto.setMethod((byte) (dto.getMethod() + (vo.getUnionpay() ? UNIONPAY : 0)));
		dto.setMethod((byte) (dto.getMethod() + (vo.getQqpay() ? QQPAY : 0)));
		dto.setMethod((byte) (dto.getMethod() + (vo.getApplepay() ? APPLEPAY : 0)));
		dto.setProperty((byte) (vo.getDisplay() ? DISPLAY : 0));
		dto.setProperty((byte) (dto.getProperty() + (vo.getForeign() ? FOREIGN : 0)));
		dto.setProperty((byte) (dto.getProperty() + (vo.getRecharge() ? RECHARGE : 0)));
		dto.setProperty((byte) (dto.getProperty() + (vo.getVip() ? VIP : 0)));
		return dto;
	}

	static final int ALIPAY = RechargeMethodEnum.ALIPAY.getValue();
	static final int WXPAY = RechargeMethodEnum.WXPAY.getValue();
	static final int QQPAY = RechargeMethodEnum.QQPAY.getValue();
	static final int UNIONPAY = RechargeMethodEnum.UNIONPAY.getValue();
	static final int APPLEPAY = RechargeMethodEnum.APPLEPAY.getValue();
	static final int RECHARGE = PartnerTypeEnum.RECHARGE.getValue();
	static final int FOREIGN = PartnerTypeEnum.FOREIGN.getValue();
	static final int VIP = PartnerTypeEnum.VIP.getValue();
	static final int DISPLAY = PartnerTypeEnum.DISPLAY.getValue();
}
