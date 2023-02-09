package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import org.apache.commons.lang3.StringUtils;

public class PasswordValidator {
    private final PasswordProperties passwordProperties;

    public PasswordValidator(final PasswordProperties passwordProperties) {
        this.passwordProperties = passwordProperties;
    }

    public void validate(final String password, final Validate validate) {
        validate(password, validate, UsernamePasswordCredentials.FIELD_PASSWORD);
    }

    /**
     * 密码强度校验.
     *
     * @param password 待校验的密码
     * @param validate validate
     * @param field    field
     */
    public void validate(final String password, final Validate validate, final String field) {
        if (StringUtils.isBlank(password)) {
            validate.addFieldError(field, "密码未设置");
        } else if (StringUtils.containsWhitespace(password)) {
            validate.addFieldError(field, "密码中不允许使用空字符");
        } else if (passwordProperties.getMinLen() > 0 && password.length() < passwordProperties.getMinLen()) {
            validate.addFieldError(field, "密码长度不足" + passwordProperties.getMinLen() + "个字符");
        } else if (passwordProperties.getMaxLen() > 0 && password.length() > passwordProperties.getMaxLen()) {
            validate.addFieldError(field, "密码长度超过" + passwordProperties.getMaxLen() + "个字符");
        } else {
            int upper = 0;
            int lower = 0;
            int digit = 0;
            int special = 0;
            for (char ch : password.toCharArray()) {
                if (ch >= 'A' && ch <= 'Z') {
                    upper++;
                } else if (ch >= 'a' && ch <= 'z') {
                    lower++;
                } else if (ch >= '0' && ch <= '9') {
                    digit++;
                } else {
                    special++;
                }
            }

            int complexity = 0;
            if (checkCharacter(validate, field, passwordProperties.getUpper(), upper, "大写字母")) {
                complexity++;
            }
            if (checkCharacter(validate, field, passwordProperties.getLower(), lower, "小写字母")) {
                complexity++;
            }
            if (checkCharacter(validate, field, passwordProperties.getDigit(), digit, "数字")) {
                complexity++;
            }
            if (checkCharacter(validate, field, passwordProperties.getSpecial(), special, "特殊字符")) {
                complexity++;
            }
            if (complexity < passwordProperties.getComplexity()) {
                validate.addFieldError(field,
                        "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的" + passwordProperties.getComplexity() + "种");
            }
        }

        // TODO 以下内容需要校验
        // private int remember = 3;
        // private int minDays = 0;
        // private int maxDays = 90;
        // private int warnDays = 7;
    }

    private static boolean checkCharacter(final Validate validate, final String field, final int expect,
                                          final int actual, final String title) {
        if (expect < 0 && actual > 0) {
            validate.addFieldError(field, "密码中不允许使用" + title);
        } else if (actual < expect) {
            validate.addFieldError(field, "密码中至少要包含" + expect + "个" + title);
        }
        return actual > 0;
    }
}
