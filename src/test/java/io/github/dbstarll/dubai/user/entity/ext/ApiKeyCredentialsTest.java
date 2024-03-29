package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiKeyCredentialsTest {
    @Test
    void get() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        assertSame(AuthType.API_KEY, credential.getSource());
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        assertEquals("appId", credentials.getAppId());
        assertEquals("key", credentials.getKey());
        assertEquals("secret", credentials.getSecret());

        assertEquals("ApiKeyCredentials[map=3]", credentials.toString());
    }

    @Test
    void setSecret() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        assertEquals("secret", credentials.getSecret());

        credentials.setSecret("newSecret");
        assertEquals("newSecret", credentials.getSecret());
    }

    @Test
    void validate() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();
        credentials.validate(null, validate);
        assertFalse(validate.hasErrors());

        credential.getCredentials().clear();
        credentials.validate(null, validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Arrays.asList("appId未设置", "key未设置", "secret未设置"), validate.getFieldErrors().get(CredentialDetails.FIELD_CREDENTIALS));
    }

    @Test
    void validateChange() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();

        // just change secret
        final Credential secret = Credentials.apiKey("appId", "key", "secret1");
        final ApiKeyCredentials secretDetails = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(secret));
        secretDetails.validate(credentials, validate);
        assertFalse(validate.hasErrors());

        // change appId and key
        final Credential other = Credentials.apiKey("appId1", "key1", "secret2");
        final ApiKeyCredentials otherDetails = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(other));
        otherDetails.validate(credentials, validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Arrays.asList("凭据[appId]不能修改", "凭据[key]不能修改"), validate.getFieldErrors().get(CredentialDetails.FIELD_CREDENTIALS));
    }

    @Test
    void validateUnknownOriginal() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();

        final Credential other = Credentials.apiKey("appId1", "key1", "secret1");
        final ApiKeyCredentials otherDetails = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(other));

        otherDetails.validate(credentials, validate);
        assertTrue(validate.hasErrors());
        assertTrue(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
        assertEquals(Collections.singletonList("original not instanceof ApiKeyCredentials"), validate.getActionErrors());
    }

    @Test
    void distinctFilter() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        assertEquals("And Filter{filters=[Filter{fieldName='source', value=ApiKey}, Filter{fieldName='credentials.appId', value=appId}, Filter{fieldName='credentials.key', value=key}]}", credentials.distinctFilter().toString());
    }

    @Test
    void testHashCode() {
        final Credential c1 = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials d1 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c1));
        final Credential c2 = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials d2 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c2));
        final Credential c3 = Credentials.apiKey("appId", "key", "secret1");
        final ApiKeyCredentials d3 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c3));
        assertEquals(d1.hashCode(), d2.hashCode());
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(d1.hashCode(), d3.hashCode());
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testEquals() {
        final Credential c1 = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials d1 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c1));
        final Credential c2 = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials d2 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c2));
        final Credential c3 = Credentials.apiKey("appId", "key", "secret1");
        final ApiKeyCredentials d3 = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(c3));
        assertEquals(d1, d2);
        assertEquals(c1, c2);
        assertNotEquals(d1, d3);
        assertNotEquals(c1, c3);

        assertEquals(d1, d1);
        assertTrue(Stream.of(null, "null").noneMatch(d1::equals));
    }
}