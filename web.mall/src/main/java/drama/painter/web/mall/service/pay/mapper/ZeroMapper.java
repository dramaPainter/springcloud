package drama.painter.web.mall.service.pay.mapper;

import drama.painter.web.mall.service.pay.enums.ExchangeStatusEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import drama.painter.web.mall.service.pay.model.Exchange;

/**
 * @author murphy
 */
@Repository
public interface ZeroMapper {
	/**
	 * 根据订单号查询兑换单信息
	 *
	 * @param orderid 订单号
	 * @return
	 */
	@Select("SELECT * FROM zero.exchange WHERE orderid = #{orderid}")
	Exchange getExchange(@Param("orderid") long orderid);

	/**
	 * 兑换单号、状态、回调内容更新
	 * @param orderid 订单号
	 * @param echodate 回调时间
	 * @param status 状态
	 */
	@Update("UPDATE zero.exchange SET status = #{status}, echodate = #{echodate} WHERE orderid = #{orderid}")
	void applyExchange(@Param("orderid") long orderid, @Param("echodate") String echodate, @Param("status") ExchangeStatusEnum status);

	/**
	 * 兑换单号、状态、回调内容更新
	 *
	 * @param orderid  订单号
	 * @param callback 回调内容
	 */
	@Update("REPLACE INTO zero.callback(orderid, callback) VALUES(#{orderid}, #{callback})")
	void applyCallback(@Param("orderid") long orderid, @Param("callback") String callback);

	/**
	 * 更新订单受理的第三方平台
	 * @param orderid 订单号
	 * @param partner 兑换渠道
	 */
	@Update("UPDATE zero.exchange SET partner = #{partner} WHERE orderid = #{orderid}")
	void applyServiceProvider(@Param("orderid") long orderid, @Param("partner") int partner);

	/**
	 * 撤消已标记打款的本人收益
	 *
	 * @param withdrawid 提款ID
	 */
	@Update("UPDATE stat.stat_channel SET withdrawid = 0 WHERE withdrawid = #{withdrawid}")
	void rollbackChannelOrder(@Param("withdrawid") int withdrawid);

	/**
	 * 撤消已标记打款的下级收益
	 *
	 * @param withdrawid 提款ID
	 */
	@Update("UPDATE stat.stat_channel_sub SET withdrawid = 0 WHERE withdrawid = #{withdrawid}")
	void rollbackChannelSubOrder(@Param("withdrawid") int withdrawid);

	/**
	 * 更新玩家或代理兑换成功后的总金额
	 *
	 * @param userid 玩家或代理ID
	 * @param price  金额
	 * @param cashup 范围: 1、玩家 2、代理
	 */
	@Update({"<script>",
		"<choose><when test='cashup == 1'>",
		"   UPDATE  account.userdata ",
		"   SET     exchange = exchange + #{price}, exchangeCount = exchangeCount + (CASE WHEN #{price} > 0 THEN 1 ELSE -1 END) ",
		"   WHERE   userid = #{userid}</when>",
		"<otherwise>",
		"   UPDATE  treasure.channel ",
		"   SET     exchangeAmount = exchangeAmount + #{price} ",
		"   WHERE   id = #{userid} ",
		"</otherwise></choose></script>"})
	void updateExchangeAmount(@Param("userid") int userid, @Param("price") int price, @Param("cashup") int cashup);
}
