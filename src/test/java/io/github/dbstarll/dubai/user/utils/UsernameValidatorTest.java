package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernameValidatorTest {
    private UsernameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UsernameValidator(new UsernameProperties());
    }

    @AfterEach
    void tearDown() {
        validator = null;
    }

    @Test
    void validateFailed() {
        new HashMap<String, String>() {{
            put(null, "用户名未设置");
            put("", "用户名未设置");
            put("a", "用户名长度不足2个字符");
            put("a-very-long-username-to-use", "用户名长度超过16个字符");
            put("\tabc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("abc\t", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("ab\tc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("ab#c", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put(".abc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("_abc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("-abc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
            put("0abc", "用户名不符合正则：" + UsernameProperties.DEFAULT_PATTERN);
        }}.forEach((k, v) -> {
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertTrue(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertTrue(validate.hasFieldErrors(), message);
            assertEquals(1, validate.getFieldErrors().size(), message);
            final List<String> errors = validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_USERNAME);
            assertNotNull(errors, message);
            assertEquals(Collections.singletonList(v), errors, message);
        });
    }

    @Test
    void validate() {
        Arrays.asList(
                "name",
                "name1",
                "first_last",
                "first-last",
                "first.last"
        ).forEach(k -> {
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertFalse(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertFalse(validate.hasFieldErrors(), message);
        });
    }

    @Test
    void validateMinLenNotLimit() {
        final UsernameProperties properties = new UsernameProperties();
        properties.setMinLen(0);
        validator = new UsernameValidator(properties);

        final Validate validate = new DefaultValidate();
        validator.validate("a", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }

    @Test
    void validateMaxLenNotLimit() {
        final UsernameProperties properties = new UsernameProperties();
        properties.setMaxLen(0);
        validator = new UsernameValidator(properties);

        final Validate validate = new DefaultValidate();
        validator.validate("a-very-long-username-to-use", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }

    @Test
    void validateCustomizedPattern() {
        final UsernameProperties properties = new UsernameProperties();
        properties.setPattern("^[a-z]{5}$");
        validator = new UsernameValidator(properties);

        new HashMap<String, String>() {{
            put("abcd", "用户名不符合正则：" + properties.getPattern());
            put("abcdE", "用户名不符合正则：" + properties.getPattern());
            put("abcdef", "用户名不符合正则：" + properties.getPattern());
        }}.forEach((k, v) -> {
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertTrue(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertTrue(validate.hasFieldErrors(), message);
            assertEquals(1, validate.getFieldErrors().size(), message);
            final List<String> errors = validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_USERNAME);
            assertNotNull(errors, message);
            assertEquals(Collections.singletonList(v), errors, message);
        });

        final Validate validate = new DefaultValidate();
        validator.validate("fivec", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }
}