package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class UsernameValidator {
    private final UsernameProperties usernameProperties;
    private final Pattern pattern;

    /**
     * 构造用户名校验器.
     *
     * @param usernameProperties 用户名校验的配置属性
     */
    public UsernameValidator(final UsernameProperties usernameProperties) {
        this.usernameProperties = usernameProperties;
        this.pattern = Pattern.compile(usernameProperties.getPattern());
    }

    /**
     * 校验用户名是否合规.
     *
     * @param username 待校验的用户名
     * @param validate validate
     */
    public void validate(final String username, final Validate validate) {
        validate(username, validate, UsernamePasswordCredentials.FIELD_USERNAME);
    }

    /**
     * 校验用户名是否合规.
     *
     * @param username 待校验的用户名
     * @param validate validate
     * @param field    field
     */
    public void validate(final String username, final Validate validate, final String field) {
        if (StringUtils.isBlank(username)) {
            validate.addFieldError(field, "用户名未设置");
        } else if (usernameProperties.getMinLen() > 0 && username.length() < usernameProperties.getMinLen()) {
            validate.addFieldError(field, "用户名长度不足" + usernameProperties.getMinLen() + "个字符");
        } else if (usernameProperties.getMaxLen() > 0 && username.length() > usernameProperties.getMaxLen()) {
            validate.addFieldError(field, "用户名长度超过" + usernameProperties.getMaxLen() + "个字符");
        } else if (!pattern.matcher(username).matches()) {
            validate.addFieldError(field, "用户名不符合正则：" + pattern.pattern());
        }
    }
}
