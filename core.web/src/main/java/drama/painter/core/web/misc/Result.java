package drama.painter.core.web.misc;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Result<T> {
	public static final int CODE_SUCCESS = 0;
	public static final int CODE_FAIL = -1;
	public static final String OPERATION_SUCCEED = "操作成功。";
	public static final String OPERATION_FAILED = "操作失败，请稍候再重试。";
	public static Result SUCCESS = new Result(CODE_SUCCESS, OPERATION_SUCCEED);
	public static Result FAIL = new Result(CODE_FAIL, OPERATION_FAILED);

	int code;
	String message;
	T data;

	public Result() {
		this(-1, "状态进入初始化。");
	}

	public Result(int code, String message) {
		this(code, message, null);
	}

	public Result(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}
}