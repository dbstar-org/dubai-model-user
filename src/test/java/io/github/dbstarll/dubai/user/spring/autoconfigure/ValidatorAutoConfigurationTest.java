package io.github.dbstarll.dubai.user.spring.autoconfigure;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import io.github.dbstarll.dubai.user.utils.PasswordValidator;
import io.github.dbstarll.dubai.user.utils.UsernameValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.NONE,
        classes = ValidatorAutoConfiguration.class,
        properties = {
                "dubai.model.user.validator.username.min-len=5",
                "dubai.model.user.validator.password.complexity=4"
        })
class ValidatorAutoConfigurationTest {
    @Autowired(required = false)
    private UsernameValidator usernameValidator;

    @Autowired(required = false)
    private PasswordValidator passwordValidator;

    @Test
    void usernameValidator() {
        assertNotNull(usernameValidator);
        final Validate validate = new DefaultValidate();
        usernameValidator.validate("abcd", validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Collections.singletonList("用户名长度不足5个字符"), validate.getFieldErrors()
                .get(UsernamePasswordCredentials.FIELD_USERNAME));
    }

    @Test
    void passwordValidator() {
        assertNotNull(passwordValidator);
        final Validate validate = new DefaultValidate();
        passwordValidator.validate("abcdEfg6", validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Collections.singletonList("密码太简单，必须包含大写字母/小写字母/数字/特殊字符中的4种"),
                validate.getFieldErrors().get(UsernamePasswordCredentials.FIELD_PASSWORD));
    }
}