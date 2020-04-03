package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum EncryptEnum implements BaseEnum {
	/**
	 * MD5加密(指定文本)
	 */
	MD5_TEXT(1, "MD5加密(指定文本)"),
	/**
	 * RSA加密(指定文本)
	 */
	RSA_TEXT(4, "RSA加密(指定文本)"),

	/**
	 * MD5加密(来自request.getParameterMap()里的QUERY参数加密)
	 */
	MD5_QUERY(1,"MD5加密(来自request.getParameterMap()里的QUERY参数加密)"),

	/**
	 * RSA加密(大写)
	 */
	RSA_QUERY(4,"RSA加密(来自request.getParameterMap()里的QUERY参数加密)"),

	/**
	 * MD5加密(来自POST加密，对POST完整加密，加密后的值和QUERY上的sign值匹配)
	 */
	MD5_POST_TEXT(1, "MD5加密(来自POST加密，对POST完整加密，加密后的值和QUERY上的sign值匹配)"),

	/**
	 * MD5加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)
	 */
	MD5_POST_PARAMETER(1, "MD5加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)"),

	/**
	 * RSA加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)
	 */
	RSA_POST_TEXT(4, "RSA加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)"),

	/**
	 * RSA加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)
	 */
	RSA_POST_PARAMETER(4,"RSA加密(来自POST加密，对POST先转成map，然后去掉sign字段加密后和sign值匹配)");

	private int id;
	private String name;

	EncryptEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getValue() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
}

