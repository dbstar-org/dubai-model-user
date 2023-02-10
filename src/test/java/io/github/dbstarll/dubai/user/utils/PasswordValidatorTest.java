package io.github.dbstarll.dubai.user.utils;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {
    private PasswordProperties properties;
    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        properties = new PasswordProperties();
        validator = new PasswordValidator(properties);
    }

    @AfterEach
    void tearDown() {
        validator = null;
        properties = null;
    }

    @Test
    void validateFailed() {
        new HashMap<String, String>() {{
            put(null, "密码未设置");
            put("", "密码未设置");
            put("a", "密码长度不足8个字符");
            put("a-very-long-username-to-use", "密码长度超过16个字符");
            put("\tabcdefgh", "密码中不允许使用空字符");
            put("abcdefgh\t", "密码中不允许使用空字符");
            put("abcdefg\th", "密码中不允许使用空字符");
            put("abcdefgh", "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的3种");
            put("aBcdefgh", "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的3种");
            put("a0cdefgh", "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的3种");
            put("a$cdefgh", "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的3种");
            put("~`!@#$%^&*()_-+=", "密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的3种");
        }}.forEach((k, v) -> {
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertTrue(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertTrue(validate.hasFieldErrors(), message);
            assertEquals(1, validate.getFieldErrors().size(), message);
            final List<String> errors = validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_PASSWORD);
            assertNotNull(errors, message);
            assertEquals(Collections.singletonList(v), errors, message);
        });
    }

    @Test
    void validate() {
        Arrays.asList(
                "1qaz@WSX",
                "1qaz@wsx",
                "1qaz2WSX",
                "1QAZ@WSX",
                "!qaz@WSX"
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
        properties.setMinLen(0);
        final Validate validate = new DefaultValidate();
        validator.validate("aB0#", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }

    @Test
    void validateMaxLenNotLimit() {
        properties.setMaxLen(0);
        final Validate validate = new DefaultValidate();
        validator.validate("~!@#$%^&*()_+`-=0aC[]{}", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }

    @Test
    void validateNotAllow() {
        new HashMap<String, Entry<String, Consumer<Integer>>>() {{
            put("abcdefGh0&1", EntryWrapper.wrap("密码中不允许使用大写字母", properties::setUpper));
            put("abcdefGh0&2", EntryWrapper.wrap("密码中不允许使用小写字母", properties::setLower));
            put("abcdefGh0&3", EntryWrapper.wrap("密码中不允许使用数字", properties::setDigit));
            put("abcdefGh0&4", EntryWrapper.wrap("密码中不允许使用特殊字符", properties::setSpecial));
        }}.forEach((k, e) -> {
            e.getValue().accept(-1);
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertTrue(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertTrue(validate.hasFieldErrors(), message);
            assertEquals(1, validate.getFieldErrors().size(), message);
            final List<String> errors = validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_PASSWORD);
            assertNotNull(errors, message);
            assertEquals(Collections.singletonList(e.getKey()), errors, message);
            e.getValue().accept(0);
        });
    }

    @Test
    void validateMust() {
        new HashMap<String, Entry<String, Consumer<Integer>>>() {{
            put("aaBB%%00", EntryWrapper.wrap("密码中至少要包含3个大写字母", properties::setUpper));
            put("aaBB%%11", EntryWrapper.wrap("密码中至少要包含3个小写字母", properties::setLower));
            put("aaBB%%22", EntryWrapper.wrap("密码中至少要包含3个数字", properties::setDigit));
            put("aaBB%%33", EntryWrapper.wrap("密码中至少要包含3个特殊字符", properties::setSpecial));
        }}.forEach((k, e) -> {
            e.getValue().accept(3);
            final String message = "failed with: [" + k + "]";
            final Validate validate = new DefaultValidate();
            validator.validate(k, validate);
            assertTrue(validate.hasErrors(), message);
            assertFalse(validate.hasActionErrors(), message);
            assertTrue(validate.hasFieldErrors(), message);
            assertEquals(1, validate.getFieldErrors().size(), message);
            final List<String> errors = validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_PASSWORD);
            assertNotNull(errors, message);
            assertEquals(Collections.singletonList(e.getKey()), errors, message);
            e.getValue().accept(0);
        });
    }

    @Test
    void validateComplexity() {
        properties.setComplexity(1);
        final Validate validate = new DefaultValidate();
        validator.validate("aaaaaaaaaa", validate);
        assertFalse(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
    }
}