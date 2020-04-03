package drama.painter.web.mall.mapper;

import drama.painter.core.web.dal.TargetDataSource;
import drama.painter.web.mall.model.dto.order.PartnerDTO;
import drama.painter.web.mall.model.dto.order.PartnerRangeDTO;
import drama.painter.web.mall.model.dto.order.RechargeDTO;
import drama.painter.web.mall.model.vo.search.RechargeSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import drama.painter.core.web.misc.Page;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface RechargeMapper {
	/**
	 * 合作商列表
	 *
	 * @return
	 */
	@TargetDataSource("master")
	@Select("SELECT * FROM zero.partner WHERE property & 1 = 1")
	List<PartnerDTO> getPartners();

	/**
	 * 合作商金额范围列表
	 *
	 * @return
	 */
	@Select("SELECT * FROM zero.partner_range")
	List<PartnerRangeDTO> getPartnerRange();

	/**
	 * 更新合作商信息
	 *
	 * @param p
	 * @return
	 */
	@Update("UPDATE zero.partner SET name = #{p.name}, encrypter = #{p.encrypter}, property = #{p.property}, peak = #{p.peak} WHERE id = #{p.id}")
	int updatePartner(@Param("p") PartnerDTO p);

	/**
	 * 更新合作商信息
	 *
	 * @param list
	 * @return
	 */
	@Update({"<script>",
		"INSERT INTO zero.partner_range(id, encrypter, type, amount) VALUES",
		"<foreach collection='list' index='index' item='item' open='' separator=',' close=''>",
		"   (#{item.id}, #{item.encrypter}, #{item.type}, #{item.amount})",
		"</foreach>",
		"</script>"})
	int updatePartnerRange(@Param("list") List<PartnerRangeDTO> list);

	/**
	 * 删除合作商充值范围信息
	 *
	 * @param id 合作商id
	 * @return
	 */
	@Update("DELETE FROM zero.partner_range WHERE id = #{id}")
	int removePartnerRange(@Param("id") Short id);

	/**
	 * 提交订单
	 *
	 * @param r 订单对象
	 * @return
	 */
	@Update({"INSERT INTO zero.recharge (orderid, userid, partner, fixdate, echodate, status, cash, encrypter, ip, thirdid) ",
		"VALUES (#{r.orderid}, #{r.userid}, #{r.partner}, #{r.fixdate}, #{r.echodate}, #{r.status}, #{r.cash}, #{r.encrypter}, #{r.ip}, #{r.thirdid})"})
	int updateRecharge(@Param("r") RechargeDTO r);

	/**
	 * 查询订单
	 *
	 * @param s    查询条件
	 * @param page 分页对象
	 * @return
	 */
	@Select({"<script>",
		"SELECT * FROM zero.recharge WHERE fixdate BETWEEN #{s.from} AND #{s.to}",
		"<if test='s.status.value > 0'>AND status = #{s.status}</if>",
		"<if test='s.partner > 0'>AND partner = #{s.partner}</if>",
		"<if test='s.userid > 0'>AND userid = #{s.userid}</if>",
		"<if test='s.orderid != null'>AND orderid = #{s.orderid}</if>",
		"<if test='s.thirdid != null'>AND thirdid = #{s.thirdid}</if>",
		"</script>"})
	List<RechargeDTO> getRecharge(@Param("s") RechargeSearch s, @Param("page") Page page);
}
