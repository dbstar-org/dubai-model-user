package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MiniProgramCredentialsTest {
    @Test
    void get() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        assertSame(AuthType.MINI_PROGRAM, credential.getSource());
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(credential));
        assertEquals("appId", credentials.getAppId());
        assertEquals("openId", credentials.getOpenId());

        assertEquals("MiniProgramCredentials[map=2]", credentials.toString());
    }

    @Test
    void validate() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();
        credentials.validate(null, validate);
        assertFalse(validate.hasErrors());

        credential.getCredentials().clear();
        credentials.validate(null, validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Arrays.asList("appId未设置", "openId未设置"), validate.getFieldErrors().get(CredentialDetails.FIELD_CREDENTIALS));
    }

    @Test
    void validateChange() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();

        // no change
        final Credential secret = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials secretDetails = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(secret));
        secretDetails.validate(credentials, validate);
        assertFalse(validate.hasErrors());

        // change appId and openId
        final Credential other = Credentials.miniProgram("appId2", "openId2");
        final MiniProgramCredentials otherDetails = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(other));
        otherDetails.validate(credentials, validate);
        assertTrue(validate.hasErrors());
        assertFalse(validate.hasActionErrors());
        assertTrue(validate.hasFieldErrors());
        assertEquals(Arrays.asList("凭据[appId]不能修改", "凭据[openId]不能修改"), validate.getFieldErrors().get(CredentialDetails.FIELD_CREDENTIALS));
    }

    @Test
    void validateUnknownOriginal() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class, Credentials.credentials(credential));
        final Validate validate = new DefaultValidate();

        final Credential other = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials otherDetails = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(other));

        otherDetails.validate(credentials, validate);
        assertTrue(validate.hasErrors());
        assertTrue(validate.hasActionErrors());
        assertFalse(validate.hasFieldErrors());
        assertEquals(Collections.singletonList("original not instanceof MiniProgramCredentials"), validate.getActionErrors());
    }

    @Test
    void distinctFilter() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class, Credentials.credentials(credential));
        assertEquals("And Filter{filters=[Filter{fieldName='source', value=MiniProgram}, Filter{fieldName='credentials.appId', value=appId}, Filter{fieldName='credentials.openId', value=openId}]}", credentials.distinctFilter().toString());
    }
}