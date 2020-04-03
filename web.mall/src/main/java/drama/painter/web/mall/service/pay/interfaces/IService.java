package drama.painter.web.mall.service.pay.interfaces;

import drama.painter.web.mall.service.pay.enums.EncryptEnum;
import drama.painter.web.mall.service.pay.enums.SerializeEnum;

import java.util.Map;

/**
 * @author murphy
 */
public interface IService {
	/**
	 * 商户ID
	 *
	 * @return
	 */
	Integer id();

	/**
	 * 求请参数
	 *
	 * @return
	 */
	Map<String, Object> param();

	/**
	 * 签名方法
	 *
	 * @return
	 */
	EncryptEnum encrypter();

	/**
	 * 序列化方法
	 *
	 * @return
	 */
	SerializeEnum serializer();

	/**
	 * 验证签名方法
	 *
	 * @return
	 */
	EncryptEnum verifier();

	/**
	 * 是否encoding
	 *
	 * @return
	 */
	Boolean encoding();

	/**
	 * 提取支付链接（兑换无非提取，返回null）
	 *
	 * @param text 第三方POST结果
	 * @return
	 */
	String extract(String text);

	/**
	 * 解析回调的支付或兑换的报文
	 * @return
	 */
	ICallback callback();

	/**
	 * 回调后的回应信息
	 *
	 * @return
	 */
	String reply();
}
