package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class CredentialsTest {
    @Test
    void usernamePassword() {
        final Credential credential = Credentials.usernamePassword("username", "password");
        assertSame(AuthType.UsernamePassword, credential.getSource());
        final UsernamePasswordCredentials credentials = assertInstanceOf(UsernamePasswordCredentials.class,
                Credentials.credentials(credential));
        assertEquals("username", credentials.getUsername());
        assertEquals("password", credentials.getPassword());
        assertNull(credentials.getHistories());
    }

    @Test
    void miniProgram() {
        final Credential credential = Credentials.miniProgram("appId", "openId");
        assertSame(AuthType.MiniProgram, credential.getSource());
        final MiniProgramCredentials credentials = assertInstanceOf(MiniProgramCredentials.class,
                Credentials.credentials(credential));
        assertEquals("appId", credentials.getAppId());
        assertEquals("openId", credentials.getOpenId());
    }

    @Test
    void apiKey() {
        final Credential credential = Credentials.apiKey("appId", "key", "secret");
        assertSame(AuthType.ApiKey, credential.getSource());
        final ApiKeyCredentials credentials = assertInstanceOf(ApiKeyCredentials.class,
                Credentials.credentials(credential));
        assertEquals("appId", credentials.getAppId());
        assertEquals("key", credentials.getKey());
        assertEquals("secret", credentials.getSecret());
    }

    @Test
    void credentials() {
        assertNull(Credentials.credentials(null));

        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(AuthType.X509);
        final Exception e = assertThrowsExactly(UnsupportedAuthTypeException.class,
                () -> Credentials.credentials(credential));
        assertEquals("尚不支持: X509", e.getMessage());
    }
}