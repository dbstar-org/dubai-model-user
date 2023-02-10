package io.github.dbstarll.dubai.user.utils;

import java.io.Serializable;

public class PasswordProperties implements Serializable {
    private static final long serialVersionUID = -4216256648207389964L;

    public static final int DEFAULT_REMEMBER = 3;
    public static final int DEFAULT_MIN_LEN = 8;
    public static final int DEFAULT_MAX_LEN = 16;
    public static final int DEFAULT_UPPER = 0;
    public static final int DEFAULT_LOWER = 0;
    public static final int DEFAULT_DIGIT = 0;
    public static final int DEFAULT_SPECIAL = 0;
    public static final int DEFAULT_COMPLEXITY = 3;
    public static final int DEFAULT_MIN_DAYS = 0;
    public static final int DEFAULT_MAX_DAYS = 90;
    public static final int DEFAULT_WARN_DAYS = 7;

    /**
     * 禁止使用最近用过的密码个数，<=0表示不禁止.
     */
    private int remember = DEFAULT_REMEMBER;

    /**
     * 最小密码长度，若<=0则不限制.
     */
    private int minLen = DEFAULT_MIN_LEN;

    /**
     * 最大密码长度，若<=0则不限制.
     */
    private int maxLen = DEFAULT_MAX_LEN;

    /**
     * 必须至少包含大写字母的数量，-1表示不允许使用.
     */
    private int upper = DEFAULT_UPPER;

    /**
     * 必须至少包含小写字母的数量，-1表示不允许使用.
     */
    private int lower = DEFAULT_LOWER;

    /**
     * 必须至少包含数字的数量，-1表示不允许使用.
     */
    private int digit = DEFAULT_DIGIT;

    /**
     * 必须至少包含特殊字符的数量，-1表示不允许使用.
     */
    private int special = DEFAULT_SPECIAL;

    /**
     * 密码复杂度，必须包含大写字母/小写字母/数字/特殊字符这4中类型中的几种.
     */
    private int complexity = DEFAULT_COMPLEXITY;

    /**
     * 最短多少天可以再次修改密码.
     */
    private int minDays = DEFAULT_MIN_DAYS;

    /**
     * 最长多少天必须修改密码.
     */
    private int maxDays = DEFAULT_MAX_DAYS;

    /**
     * 提前多少天提醒用户密码快到期了.
     */
    private int warnDays = DEFAULT_WARN_DAYS;

    /**
     * 禁止使用最近用过的密码个数，<=0表示不禁止.
     *
     * @return 禁止使用最近用过的密码个数
     */
    public int getRemember() {
        return remember;
    }

    /**
     * 设置禁止使用最近用过的密码个数，<=0表示不禁止.
     *
     * @param remember 禁止使用最近用过的密码个数
     */
    public void setRemember(final int remember) {
        this.remember = remember;
    }

    /**
     * 获取最小密码长度，若<=0则不限制.
     *
     * @return 最小密码长度
     */
    public int getMinLen() {
        return minLen;
    }

    /**
     * 设置最小密码长度，若<=0则不限制.
     *
     * @param minLen 最小密码长度
     */
    public void setMinLen(final int minLen) {
        this.minLen = minLen;
    }

    /**
     * 获取最大密码长度，若<=0则不限制.
     *
     * @return 最大密码长度
     */
    public int getMaxLen() {
        return maxLen;
    }

    /**
     * 设置最大密码长度，若<=0则不限制.
     *
     * @param maxLen 最大密码长度
     */
    public void setMaxLen(final int maxLen) {
        this.maxLen = maxLen;
    }

    /**
     * 获取必须至少包含大写字母的数量，-1表示不允许使用.
     *
     * @return 必须至少包含大写字母的数量
     */
    public int getUpper() {
        return upper;
    }

    /**
     * 设置必须至少包含大写字母的数量，-1表示不允许使用.
     *
     * @param upper 必须至少包含大写字母的数量
     */
    public void setUpper(final int upper) {
        this.upper = upper;
    }

    /**
     * 获取必须至少包含小写字母的数量，-1表示不允许使用.
     *
     * @return 必须至少包含小写字母的数量
     */
    public int getLower() {
        return lower;
    }

    /**
     * 设置必须至少包含小写字母的数量，-1表示不允许使用.
     *
     * @param lower 必须至少包含小写字母的数量
     */
    public void setLower(final int lower) {
        this.lower = lower;
    }

    /**
     * 获取必须至少包含数字的数量，-1表示不允许使用.
     *
     * @return 必须至少包含数字的数量
     */
    public int getDigit() {
        return digit;
    }

    /**
     * 设置必须至少包含数字的数量，-1表示不允许使用.
     *
     * @param digit 必须至少包含数字的数量
     */
    public void setDigit(final int digit) {
        this.digit = digit;
    }

    /**
     * 获取必须至少包含特殊字符的数量，-1表示不允许使用.
     *
     * @return 必须至少包含特殊字符的数量
     */
    public int getSpecial() {
        return special;
    }

    /**
     * 设置必须至少包含特殊字符的数量，-1表示不允许使用.
     *
     * @param special 必须至少包含特殊字符的数量
     */
    public void setSpecial(final int special) {
        this.special = special;
    }

    /**
     * 获取密码复杂度，必须包含大写字母/小写字母/数字/特殊字符这4中类型中的几种.
     *
     * @return 密码复杂度
     */
    public int getComplexity() {
        return complexity;
    }

    /**
     * 设置密码复杂度，必须包含大写字母/小写字母/数字/特殊字符这4中类型中的几种.
     *
     * @param complexity 密码复杂度
     */
    public void setComplexity(final int complexity) {
        this.complexity = complexity;
    }

    /**
     * 获取最短多少天可以再次修改密码.
     *
     * @return 最短多少天可以再次修改密码
     */
    public int getMinDays() {
        return minDays;
    }

    /**
     * 设置最短多少天可以再次修改密码.
     *
     * @param minDays 天数
     */
    public void setMinDays(final int minDays) {
        this.minDays = minDays;
    }

    /**
     * 获取最长多少天必须修改密码.
     *
     * @return 最长多少天必须修改密码
     */
    public int getMaxDays() {
        return maxDays;
    }

    /**
     * 设置最长多少天必须修改密码.
     *
     * @param maxDays 天数
     */
    public void setMaxDays(final int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * 获取提前多少天提醒用户密码快到期了.
     *
     * @return 提前多少天提醒用户密码快到期了.
     */
    public int getWarnDays() {
        return warnDays;
    }

    /**
     * 设置提前多少天提醒用户密码快到期了.
     *
     * @param warnDays 天数
     */
    public void setWarnDays(final int warnDays) {
        this.warnDays = warnDays;
    }
}
