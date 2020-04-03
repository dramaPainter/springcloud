package drama.painter.web.mall.service.pay.interfaces;

/**
 * @author murphy
 */
public interface ICallback {
	/**
	 * 回调时间
	 * @return
	 */
	String getCallDate();

	/**
	 * 订单号
	 * @return
	 */
	Long getCallOrderId();

	/**
	 * 第三方订单号
	 * @return
	 */
	String getCallFlowId();

	/**
	 * 回调金额
	 * @return
	 */
	int getCallCash();

	/**
	 * 是否回调成功
	 * @return
	 */
	boolean succeed();

	/**
	 * 回调消息
	 * @return
	 */
	String getCallMessage();
}
