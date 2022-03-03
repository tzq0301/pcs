package cn.tzq0301.result;

/**
 * @author tzq0301
 * @version 1.0
 */
public interface ResultEnumerable {
    Integer getCode();

    String getMessage();

    @Override
    String toString();
}
