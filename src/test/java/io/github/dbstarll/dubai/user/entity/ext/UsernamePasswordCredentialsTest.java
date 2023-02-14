package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernamePasswordCredentialsTest {
    @Test
    void get() {
        final Credential credential = Credentials.usernamePassword("username", "password");
        assertEquals(AuthType.UsernamePassword, credential.getSource());
        final UsernamePasswordCredentials credentials = assertInstanceOf(UsernamePasswordCredentials.class, Credentials.credentials(credential));
        assertEquals("username", credentials.getUsername());
        assertEquals("password", credentials.getPassword());
        assertNull(credentials.getHistories());

        assertEquals("UsernamePasswordCredentials[map=2]", credentials.toString());
    }

    @Test
    void validate() {
        final Credential credential = Credentials.usernamePassword("username", "password");
        final UsernamePasswordCredentials credentials = assertInstanceOf(UsernamePasswordCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();
        credentials.validate(null, validate);
        assertFalse(validate.hasErrors());

        credential.getCredentials().clear();
        credentials.validate(null, validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Arrays.asList("用户名未设置", "密码未设置"), validate.getFieldErrors().get(CredentialDetails.FIELD_CREDENTIALS));
    }

    @Test
    void distinctFilter() {
        final Credential credential = Credentials.usernamePassword("username", "password");
        final UsernamePasswordCredentials credentials = assertInstanceOf(UsernamePasswordCredentials.class, Credentials.credentials(credential));
        assertEquals("And Filter{filters=[Filter{fieldName='source', value=UsernamePassword}, Filter{fieldName='credentials.username', value=username}]}", credentials.distinctFilter().toString());
    }
}