package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class PasswordValidator {
    private enum CharacterType {
        UPPER("大写字母"), LOWER("小写字母"), DIGIT("数字"), OTHER("特殊字符");

        private final String title;

        CharacterType(final String title) {
            this.title = title;
        }

        int expect(final PasswordProperties p) {
            switch (this) {
                case UPPER:
                    return p.getUpper();
                case LOWER:
                    return p.getLower();
                case DIGIT:
                    return p.getDigit();
                default:
                    return p.getSpecial();
            }
        }

        static CharacterType typeOf(final int codePoint) {
            switch (Character.getType(codePoint)) {
                case Character.UPPERCASE_LETTER:
                    return UPPER;
                case Character.LOWERCASE_LETTER:
                    return LOWER;
                case Character.DECIMAL_DIGIT_NUMBER:
                    return DIGIT;
                default:
                    return OTHER;
            }
        }
    }

    private final PasswordProperties properties;

    /**
     * 构造密码校验器.
     *
     * @param properties 密码校验的配置属性
     */
    public PasswordValidator(final PasswordProperties properties) {
        this.properties = properties;
    }

    /**
     * 校验密码是否合规.
     *
     * @param password 待校验的密码
     * @param validate validate
     */
    public void validate(final String password, final Validate validate) {
        validate(password, validate, UsernamePasswordCredentials.FIELD_PASSWORD);
    }

    /**
     * 校验密码是否合规.
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
        } else if (properties.getMinLen() > 0 && password.length() < properties.getMinLen()) {
            validate.addFieldError(field, "密码长度不足" + properties.getMinLen() + "个字符");
        } else if (properties.getMaxLen() > 0 && password.length() > properties.getMaxLen()) {
            validate.addFieldError(field, "密码长度超过" + properties.getMaxLen() + "个字符");
        } else {
            final Map<CharacterType, Integer> map = password.codePoints().mapToObj(CharacterType::typeOf)
                    .collect(MapUtils.counter());
            map.forEach((key, value) -> check(validate, field, key, value));
            if (map.size() < properties.getComplexity()) {
                validate.addFieldError(field,
                        "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的" + properties.getComplexity() + "种");
            }
        }
    }

    // TODO 以下内容需要校验
    // private int remember = 3;
    // private int minDays = 0;
    // private int maxDays = 90;
    // private int warnDays = 7;

    private void check(final Validate validate, final String field, final CharacterType type, final int actual) {
        final int expect = type.expect(properties);
        if (expect < 0) {
            validate.addFieldError(field, "密码中不允许使用" + type.title);
        } else if (actual < expect) {
            validate.addFieldError(field, "密码中至少要包含" + expect + "个" + type.title);
        }
    }
}
