package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum SerializeEnum implements BaseEnum {
	/**
	 * JSON序列化
	 */
	JSON(1, "JSON序列化"),
	/**
	 * URL序列化
	 */
	URL(4, "URL序列化");

	private int id;
	private String name;

	SerializeEnum(int id, String name) {
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

