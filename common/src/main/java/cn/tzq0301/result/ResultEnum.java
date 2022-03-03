package cn.tzq0301.result;

/**
 * {@link Result} 的返回码与返回信息的枚举类
 *
 * @author tzq0301
 * @version 1.0
 */
public enum ResultEnum implements ResultEnumerable {
    SUCCESS(0, "Success"), // 请求成功
    ERROR(0, "Error"), // 请求失败
    ;

    private final Integer code;

    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }


}
