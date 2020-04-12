package drama.painter.web.rbac.model.dto.oa;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class PageDTO {
	/**
	 * 页面ID
	 */
	private int id;

	/**
	 * 页面名称
	 */
	private String name;

	/**
	 * 页面地址
	 */
	private String url;

	/**
	 * 父节点
	 */
	private int pId;

	/**
	 * 项目顶级ID
	 */
	private int tId;

	/**
	 * 排名
	 */
	private int rank;

	/**
	 * 类型 0.子项 1.菜单
	 */
	private Byte type;

	/**
	 * 是否菜单栏显示
	 */
	private Byte listed;

	/**
	 * 状态 1.启用 2.隐藏 3.禁用
	 */
	private Byte status;
}
