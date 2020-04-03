package drama.painter.web.mall.service.pay.interfaces;

/**
 * @author murphy
 */
public interface IMessage {
	String SUCCESS = "success";
	String READ_TIMED_OUT = "Read timed out";
	String EXCHANGE_CONFIG_KEY = "SP%d-%d";
	String EXCHANGE_ENCRYPT_KEY = "%s%d%s";
	String CHINA_REGION = "(+86)";
	String AT = "@";
	String REMARK = "\"remark\":";
	String CALLBACK_UNAVAILABLE_TIP = "没有可用的兑换平台。";
	String CALLBACK_UNDONE_TIP = "回调未能执行成功。";
	String REWIND_TIP = "您兑换的金币因为个人信息不完整或者其它原因造成无法兑换的，会将原有金币全部退还。请注意查收，如有问题请联系客服。";
	String RANGE_TEMPLATE = "兑换金额不在范围内：%d < (%.0f) < %d";
	String REFUSE_TIP = "您本次兑换金币已被拒绝，如有问题请联系客服。";
}