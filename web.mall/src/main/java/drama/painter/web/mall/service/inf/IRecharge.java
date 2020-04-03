package drama.painter.web.mall.service.inf;

import drama.painter.core.web.misc.Page;
import drama.painter.web.mall.model.vo.order.PartnerVO;
import drama.painter.web.mall.model.vo.order.RechargeVO;
import drama.painter.web.mall.model.vo.search.RechargeSearch;

import java.util.List;

/**
 * @author murphy
 */
public interface IRecharge {
	/**
	 * 所有支付通道列表
	 *
	 * @return
	 */
	List<PartnerVO> getPartners();

	/**
	 * 查询充值列表
	 *
	 * @param page   分页对象
	 * @param search 搜索条件
	 * @return
	 */
	List<RechargeVO> getRecharges(RechargeSearch search, Page page);

	/**
	 * 启用/禁用支付通道
	 *
	 * @param p 范围
	 * @return
	 */
	void enableRecharge(PartnerVO.PartnerRangeVO p);

	/**
	 * 更新通道信息
	 *
	 * @param p 通道信息
	 */
	void updatePartner(PartnerVO p);
}
