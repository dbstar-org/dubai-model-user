package io.github.dbstarll.dubai.user.utils;

import java.io.Serializable;

public class PasswordProperties implements Serializable {
    private static final long serialVersionUID = -4216256648207389964L;

    /**
     * 禁止使用最近用过的密码个数，0表示不禁止.
     */
    private int remember = 3;

    /**
     * 最小密码长度.
     */
    private int minLen = 8;

    /**
     * 最大密码长度.
     */
    private int maxLen = 16;

    /**
     * 必须至少包含大写字母的数量，-1表示不允许使用.
     */
    private int upper = 0;

    /**
     * 必须至少包含小写字母的数量，-1表示不允许使用.
     */
    private int lower = 0;

    /**
     * 必须至少包含数字的数量，-1表示不允许使用.
     */
    private int digit = 0;

    /**
     * 必须至少包含特殊字符的数量，-1表示不允许使用.
     */
    private int special = 0;

    /**
     * 设置密码复杂度，必须包含大写字母/小写字母/数字/特殊字符这4中类型中的几种.
     */
    private int complexity = 3;

    /**
     * 设置最短多少天可以再次修改密码.
     */
    private int minDays = 0;
    /**
     * 设置最长多少天必须修改密码.
     */
    private int maxDays = 90;

    /**
     * 设置提前多少天提醒用户密码快到期了.
     */
    private int warnDays = 7;

    public int getRemember() {
        return remember;
    }

    public void setRemember(int remember) {
        this.remember = remember;
    }

    public int getMinLen() {
        return minLen;
    }

    public void setMinLen(int minLen) {
        this.minLen = minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getMinDays() {
        return minDays;
    }

    public void setMinDays(int minDays) {
        this.minDays = minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    public int getWarnDays() {
        return warnDays;
    }

    public void setWarnDays(int warnDays) {
        this.warnDays = warnDays;
    }
}
