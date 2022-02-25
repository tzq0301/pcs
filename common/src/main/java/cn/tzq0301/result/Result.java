package cn.tzq0301.result;

import java.io.Serializable;

import static cn.tzq0301.result.ResultEnum.*;

/**
 * @author tzq0301
 * @version 1.0
 */
public class Result<T> implements Serializable {

    private static final Long serialVersionUID = 9192910608408209894L;

    private final T data;

    private final Integer code;

    private final String message;

    private Result(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success() {
        return new Result<>(null, SUCCESS.getCode(), SUCCESS.getMessage());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, SUCCESS.getCode(), SUCCESS.getMessage());
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(data, ERROR.getCode(), ERROR.getMessage());
    }

    public T getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
