package com.zavier.dubbotest.common;

import lombok.Data;

/**
 * The type Result.
 *
 * @param <T> the type parameter
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
@Data
public class Result<T> {
    /**
     * The Code.
     */
    private String code;
    /**
     * The Success.
     */
    private boolean success;
    /**
     * The Message.
     */
    private String message;
    /**
     * The Data.
     */
    private T data;

    /**
     * Wrap successful result result.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the result
     */
    public static <T> Result<T> wrapSuccessfulResult(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * Wrap error result result.
     *
     * @param <T>     the type parameter
     * @param message the message
     * @return the result
     */
    public static <T> Result<T> wrapErrorResult(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
