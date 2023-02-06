package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.user.entity.Credential;

import java.util.List;

import static io.github.dbstarll.dubai.user.entity.enums.SourceType.ApiKey;
import static io.github.dbstarll.dubai.user.entity.enums.SourceType.MiniProgram;
import static io.github.dbstarll.dubai.user.entity.enums.SourceType.UsernamePassword;

public final class Credentials {
    private Credentials() {
        // 禁止实例化
    }

    /**
     * 从credential解析CredentialDetails.
     *
     * @param credential credential
     * @return CredentialDetails
     */
    public static CredentialDetails credentials(final Credential credential) {
        switch (credential.getSource()) {
            case MiniProgram:
                return new MiniProgramCredentials(credential.getCredentials());
            case UsernamePassword:
                return new UsernamePasswordCredentials(credential.getCredentials());
            case ApiKey:
                return new ApiKeyCredentials(credential.getCredentials());
            default:
                return null;
        }
    }

    /**
     * 基于微信小程序的凭证.
     *
     * @param appId  appId
     * @param openid openid
     * @return MiniProgramCredentials
     */
    public static Credential miniProgram(final String appId, final String openid) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(MiniProgram);
        credential.setCredentials(new MiniProgramCredentials(appId, openid));
        return credential;
    }

    /**
     * 基于用户名密码的凭证.
     *
     * @param username  username
     * @param password  password
     * @param histories histories
     * @return UsernamePasswordCredentials
     */
    public static Credential usernamePassword(final String username, final String password,
                                              final List<PasswordHistory> histories) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(UsernamePassword);
        credential.setCredentials(new UsernamePasswordCredentials(username, password, histories));
        return credential;
    }

    /**
     * 基于ApiKey的凭证.
     *
     * @param appId  appId
     * @param key    key
     * @param secret secret
     * @return ApiKeyCredentials
     */
    public static Credential apiKey(String appId, String key, String secret) {
        final Credential credential = EntityFactory.newInstance(Credential.class);
        credential.setSource(ApiKey);
        credential.setCredentials(new ApiKeyCredentials(appId, key, secret));
        return credential;
    }
}
