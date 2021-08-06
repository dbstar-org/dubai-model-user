package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import org.apache.commons.lang3.StringUtils;

public class UsernameValidator {
    private final UsernameProperties usernameProperties;

    public UsernameValidator(UsernameProperties usernameProperties) {
        this.usernameProperties = usernameProperties;
    }

    public void validate(final String username, final Validate validate) {
        validate(username, validate, UsernamePasswordCredentials.FIELD_USERNAME);
    }

    /**
     * 用户名校验.
     *
     * @param username 待校验的用户名
     * @param validate validate
     * @param field    field
     */
    public void validate(final String username, final Validate validate, final String field) {
        if (StringUtils.isBlank(username)) {
            validate.addFieldError(field, "用户名未设置");
        } else if (StringUtils.containsWhitespace(username)) {
            validate.addFieldError(field, "用户名中不允许使用空字符");
        } else if (StringUtils.containsAny(username, "~!@#$%^&*()")) {
            validate.addFieldError(field, "用户名中不允许使用特殊字符：~!@#$%^&*()");
        } else if (usernameProperties.getMinLen() > 0 && username.length() < usernameProperties.getMinLen()) {
            validate.addFieldError(field, "用户名长度不足" + usernameProperties.getMinLen() + "个字符");
        } else if (usernameProperties.getMaxLen() > 0 && username.length() > usernameProperties.getMaxLen()) {
            validate.addFieldError(field, "用户名长度超过" + usernameProperties.getMaxLen() + "个字符");
        }
    }
}
