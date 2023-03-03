package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.user.entity.Credential;

import static io.github.dbstarll.dubai.user.entity.enums.AuthType.API_KEY;
import static io.github.dbstarll.dubai.user.entity.enums.AuthType.MINI_PROGRAM;
import static io.github.dbstarll.dubai.user.entity.enums.AuthType.USERNAME_PASSWORD;

public final class Credentials {
    private Credentials() {
        // 禁止实例化
    }

    /**
     * 从credential解析CredentialDetails，目前只支持MiniProgram、UsernamePassword和ApiKey.
     *
     * @param credential credential
     * @return CredentialDetails, null if credential is null
     * @throws UnsupportedAuthTypeException 目前尚不支持的AuthType时抛出
     */
    public static CredentialDetails credentials(final Credential credential) throws UnsupportedAuthTypeException {
        if (credential == null) {
            return null;
        } else {
            switch (credential.getSource()) {
                case MINI_PROGRAM:
                    return new MiniProgramCredentials(credential.getCredentials());
                case USERNAME_PASSWORD:
                    return new UsernamePasswordCredentials(credential.getCredentials());
                case API_KEY:
                    return new ApiKeyCredentials(credential.getCredentials());
                default:
                    throw new UnsupportedAuthTypeException(credential.getSource());
            }
        }
    }

    /**
     * 基于微信小程序的凭证.
     *
     * @param appId  appId
     * @param openId openId
     * @return MiniProgramCredentials
     */
    public static Credential miniProgram(final String appId, final String openId) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(MINI_PROGRAM);
        return new MiniProgramCredentials(appId, openId).apply(credential);
    }

    /**
     * 基于用户名密码的凭证.
     *
     * @param username username
     * @param password password
     * @return UsernamePasswordCredentials
     */
    public static Credential usernamePassword(final String username, final String password) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(USERNAME_PASSWORD);
        return new UsernamePasswordCredentials(username, password).apply(credential);
    }

    /**
     * 基于ApiKey的凭证.
     *
     * @param appId  appId
     * @param key    key
     * @param secret secret
     * @return ApiKeyCredentials
     */
    public static Credential apiKey(final String appId, final String key, final String secret) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(API_KEY);
        return new ApiKeyCredentials(appId, key, secret).apply(credential);
    }
}
