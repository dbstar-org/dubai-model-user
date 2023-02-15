package io.github.dbstarll.dubai.user.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

import static org.apache.commons.lang3.Validate.notBlank;

@ConfigurationProperties(prefix = "dubai.model.user.validator.username")
public class UsernameProperties implements Serializable {
    private static final long serialVersionUID = 1723263105832030610L;

    public static final String DEFAULT_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9_\\-\\.]*$";
    public static final int DEFAULT_MIN_LEN = 2;
    public static final int DEFAULT_MAX_LEN = 16;

    /**
     * 用户名匹配正则表达式模版.
     */
    private String pattern = DEFAULT_PATTERN;

    /**
     * 用户名最小字符长度，若<=0则不限制.
     */
    private int minLen = DEFAULT_MIN_LEN;

    /**
     * 用户名最大字符长度，若<=0则不限制.
     */
    private int maxLen = DEFAULT_MAX_LEN;

    /**
     * 获取用户名匹配正则表达式模版.
     *
     * @return 用户名匹配正则表达式模版
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 设置用户名匹配正则表达式模版.
     *
     * @param pattern 用户名匹配正则表达式模版
     */
    public void setPattern(final String pattern) {
        this.pattern = notBlank(pattern);
    }

    /**
     * 获取用户名最小字符长度限制.
     *
     * @return 用户名最小字符长度限制
     */
    public int getMinLen() {
        return minLen;
    }

    /**
     * 设置用户名最小长度限制，若<=0则不限制.
     *
     * @param minLen 用户名最小长度限制
     */
    public void setMinLen(final int minLen) {
        this.minLen = minLen;
    }

    /**
     * 获取用户名最大字符长度限制.
     *
     * @return 用户名最大字符长度限制
     */
    public int getMaxLen() {
        return maxLen;
    }

    /**
     * 设置用户名最大长度限制，若<=0则不限制.
     *
     * @param maxLen 用户名最大长度限制
     */
    public void setMaxLen(final int maxLen) {
        this.maxLen = maxLen;
    }
}
