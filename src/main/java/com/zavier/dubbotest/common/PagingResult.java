package com.zavier.dubbotest.common;

import java.io.Serializable;
import java.util.List;

/**
 * The type Paging result.
 *
 * @param <T> the type parameter
 * @date 2020-01-15 12:42
 * @author hanruida
 */
public class PagingResult<T> implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 8911072786251958689L;
    /**
     * The List.
     */
    private List<T> list;
    /**
     * The Total.
     */
    private int total;
    /**
     * The Success.
     */
    private boolean success;
    /**
     * The Code.
     */
    private String code;
    /**
     * The Message.
     */
    private String message;

    /**
     * Instantiates a new Paging result.
     */
    public PagingResult() {
    }

    /**
     * Wrap successful result paging result.
     *
     * @param <T>   the type parameter
     * @param data  the data
     * @param total the total
     * @return the paging result
     */
    public static <T> PagingResult<T> wrapSuccessfulResult(List<T> data, int total) {
        PagingResult<T> result = new PagingResult();
        result.list = data;
        result.total = total;
        result.success = true;
        return result;
    }

    /**
     * Wrap error result paging result.
     *
     * @param <T>     the type parameter
     * @param message the message
     * @return the paging result
     */
    public static <T> PagingResult<T> wrapErrorResult(String message) {
        PagingResult<T> result = new PagingResult();
        result.success = false;
        result.message = message;
        return result;
    }

    /**
     * Wrap error result paging result.
     *
     * @param <T>     the type parameter
     * @param code    the code
     * @param message the message
     * @return the paging result
     */
    public static <T> PagingResult<T> wrapErrorResult(String code, String message) {
        PagingResult<T> result = new PagingResult();
        result.success = false;
        result.code = code;
        result.message = message;
        return result;
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public List<T> getList() {
        return this.list;
    }

    /**
     * Sets list.
     *
     * @param list the list
     * @return the list
     */
    public PagingResult<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public int getTotal() {
        return this.total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     * @return the total
     */
    public PagingResult<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    /**
     * Is success boolean.
     *
     * @return the boolean
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * Sets success.
     *
     * @param success the success
     * @return the success
     */
    public PagingResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     * @return the code
     */
    public PagingResult<T> setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public PagingResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }
}
