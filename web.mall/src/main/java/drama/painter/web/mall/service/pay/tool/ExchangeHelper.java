package drama.painter.web.mall.service.pay.tool;

import drama.painter.web.mall.service.pay.enums.ExchangeStatusEnum;
import drama.painter.web.mall.service.pay.enums.ExchangeUserTypeEnum;
import drama.painter.web.mall.service.pay.enums.MethodEnum;
import drama.painter.web.mall.service.pay.mapper.PlatformMapper;
import drama.painter.web.mall.service.pay.mapper.ZeroMapper;
import drama.painter.web.mall.service.pay.model.Exchange;
import drama.painter.web.mall.service.pay.model.Mail;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 * 处理兑换成功后的业务操作
 */
public class ExchangeHelper {

	private static Map<ExchangeStatusEnum, IProcessor> executor = new HashMap<ExchangeStatusEnum, IProcessor>(9) {{
		put(ExchangeStatusEnum.WAIT, new WaitProcessor());
		put(ExchangeStatusEnum.MANUAL, new ManualProcessor());
		put(ExchangeStatusEnum.REWIND, new RewindProcessor());
		put(ExchangeStatusEnum.READY, new ReadyProcessor());
		put(ExchangeStatusEnum.FAIL, new FailProcessor());
		put(ExchangeStatusEnum.SUCCESS, new SuccessProcessor());
		put(ExchangeStatusEnum.REFUSE, new RefuseProcessor());
		put(ExchangeStatusEnum.REFUND, new RefundProcessor());
		put(ExchangeStatusEnum.REFUNDING, new RefundingProcessor());
	}};

	public static void execute(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
		executor.get(order.getStatus()).process(order, zeroMapper, platformMapper, notify);
	}

	private interface IProcessor {
		/**
		 * 订单处理
		 *
		 * @param order            订单对象
		 * @param zeroMapper treasure数据库对象
		 * @param platformMapper platform数据库对象
		 * @param notify         通知对象
		 */
		void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify);
	}

	/**
	 * 等待兑换
	 */
	private static class WaitProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			// 无需操作
		}
	}

	/**
	 * 人工处理
	 */
	private static class ManualProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			// 无需操作
		}
	}

	/**
	 * 驳回兑换
	 */
	private static class RewindProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			if (order.getType() == ExchangeUserTypeEnum.USER) {
				platformMapper.sendMail(new Mail(order.getUserid(), 0, (long) order.getPrice(), "兑换金币退还通知", order.getCallback()));
				notify.notifyMail(order.getUserid());
			} else {
				zeroMapper.rollbackChannelOrder((int)(order.getOrderid() % 99999999));
				zeroMapper.rollbackChannelSubOrder((int) (order.getOrderid() % 99999999));
			}
		}
	}

	/**
	 * 兑换失败
	 */
	private static class FailProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			executor.get(ExchangeStatusEnum.REWIND).process(order, zeroMapper, platformMapper, notify);
		}
	}

	/**
	 * 兑换成功
	 */
	private static class SuccessProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			zeroMapper.updateExchangeAmount(order.getUserid(), order.getCash(), order.getType().getValue());
			if (order.getType() == ExchangeUserTypeEnum.USER) {
				String method = order.getMethod() == MethodEnum.ALIPAY ? "支付宝" : "银行";
				String msg = String.format("您于%s提交的%.2f元兑换订单，已经成功兑换。请查看您的%s账单，如有疑问请联系客服。", order.getFixdate(), order.getPrice() * 0.01f, method);
				platformMapper.sendMail(new Mail(order.getUserid(), 0, 0, "兑换金币成功通知", msg));
				notify.notifyMail(order.getUserid());
			}
		}
	}

	/**
	 * 拒绝兑换
	 */
	private static class RefuseProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			if (order.getType() == ExchangeUserTypeEnum.USER) {
				String msg = String.format("您的兑换已被拒绝，原因是：%s　如有问题请联系客服。", order.getCallback());
				platformMapper.sendMail(new Mail(order.getUserid(), 0, 0, "拒绝兑换金币通知", msg));
				notify.notifyMail(order.getUserid());
			}
		}
	}

	/**
	 * 准备兑换
	 */
	private static class ReadyProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			// 无需操作
		}
	}

	/**
	 * 发起退款
	 */
	private static class RefundingProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			// 无需操作
		}
	}

	/**
	 * 退款成功
	 */
	private static class RefundProcessor implements IProcessor {
		@Override
		public void process(Exchange order, ZeroMapper zeroMapper, PlatformMapper platformMapper, Notify notify) {
			zeroMapper.updateExchangeAmount(order.getUserid(), 0 - order.getCash(), order.getType().getValue());
		}
	}
}

