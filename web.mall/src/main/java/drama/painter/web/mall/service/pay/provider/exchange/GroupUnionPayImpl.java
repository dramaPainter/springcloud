package drama.painter.web.mall.service.pay.provider.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import drama.painter.core.web.tool.Json;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.mall.service.pay.anomaly.CallbackException;
import drama.painter.web.mall.service.pay.enums.EncryptEnum;
import drama.painter.web.mall.service.pay.enums.SerializeEnum;
import drama.painter.web.mall.service.pay.interfaces.ICallback;
import drama.painter.web.mall.service.pay.interfaces.IService;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author murphy
 * 集团银行卡兑换
 */
public class GroupUnionPayImpl implements IService {
	private static final Map<String, Object> PARAM = new TreeMap();
	private static final ICallback CALLBACK = new GroupUnionPayCallback();

	static {
		PARAM.put("merchant_id", "<appid>");
		PARAM.put("order_id", "<orderid>");
		PARAM.put("notify_url", "<callbackUrl>");
		PARAM.put("bill_price", "<cash%sy>");
		PARAM.put("extra", "");

		Map<String, Object> info = new TreeMap<>();
		info.put("user_id", "wh_<userid%s>");
		info.put("device_ip", "<ip>");
		info.put("device_id", Randoms.getRandom(9));
		info.put("name", "<realname>");
		info.put("bank_card", "<account>");
		info.put("bank_code", "<bankcode>");
		info.put("bank_province", "<province>");
		info.put("bank_city", "<city>");
		info.put("bank_open", "<branch>");
		info.put("tel", "13" + Randoms.getRandomNumber(9));
		info.put("device_type", "android");
		PARAM.put("info", info);
	}

	@Override
	public Integer id() {
		return 11;
	}

	@Override
	public Map<String, Object> param() {
		return PARAM;
	}

	@Override
	public EncryptEnum verifier() {
		return EncryptEnum.MD5_POST_TEXT;
	}

	@Override
	public EncryptEnum encrypter() {
		return EncryptEnum.MD5_TEXT;
	}

	@Override
	public SerializeEnum serializer() {
		return SerializeEnum.JSON;
	}

	@Override
	public Boolean encoding() {
		return false;
	}

	@Override
	public String extract(String text) throws CallbackException {
		boolean succeed = text.contains("\"status\":10000");
		if (succeed) {
			return null;
		} else {
			HashMap map = Json.parseObject(text, HashMap.class);
			String msg = map.get("message").toString();
			map.clear();
			throw new CallbackException(msg);
		}
	}

	@Override
	public ICallback callback() {
		return CALLBACK;
	}

	@Override
	public String reply() {
		return "success";
	}

	@Data
	public static class GroupUnionPayCallback implements ICallback {
		private String flashid;
		private int status;
		@JsonProperty("payed_money")
		private float payedMoney;
		@JsonProperty("merchant_order_id")
		private long merchatOrderId;
		@JsonProperty("payed_time")
		private String payedTime;
		private String message;

		@Override
		public String getCallDate() {
			return payedTime;
		}

		@Override
		public Long getCallOrderId() {
			return merchatOrderId;
		}

		@Override
		public String getCallFlowId() {
			return flashid;
		}

		@Override
		public int getCallCash() {
			return (int) (payedMoney * 100);
		}

		@Override
		public boolean succeed() {
			return status == 10000;
		}

		@Override
		public String getCallMessage() {
			return message;
		}
	}
}



