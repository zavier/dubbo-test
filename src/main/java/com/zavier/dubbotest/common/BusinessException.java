package com.zavier.dubbotest.common;

/**
 * The type Business exception.
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
public class BusinessException extends RuntimeException {

    /**
     * Instantiates a new Business exception.
     *
     * @param message the message
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param cause the cause
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
