package io.github.dbstarll.dubai.user.utils;

import java.io.Serializable;

public class UsernameProperties implements Serializable {
    private static final long serialVersionUID = 1723263105832030610L;

    /**
     * 用户名最小长度.
     */
    private int minLen = 2;

    /**
     * 用户名最大长度.
     */
    private int maxLen = 16;

    public int getMinLen() {
        return minLen;
    }

    public void setMinLen(final int minLen) {
        this.minLen = minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(final int maxLen) {
        this.maxLen = maxLen;
    }
}
