package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.Map;

import static org.apache.commons.lang3.Validate.notBlank;

public final class ApiKeyCredentials extends AbstractCredentials {
    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_KEY = "key";
    public static final String FIELD_SECRET = "secret";

    ApiKeyCredentials(final String appId, final String key, final String secret) {
        put(FIELD_APP_ID, notBlank(appId, DEFAULT_NOT_EMPTY_EX_MESSAGE, FIELD_APP_ID));
        put(FIELD_KEY, notBlank(key, DEFAULT_NOT_EMPTY_EX_MESSAGE, FIELD_KEY));
        setSecret(secret);
    }

    ApiKeyCredentials(final Map<String, Object> map) {
        super(map);
    }

    /**
     * 获取appId.
     *
     * @return appId
     */
    public String getAppId() {
        return get(FIELD_APP_ID);
    }

    /**
     * 获取appKey.
     *
     * @return appKey
     */
    public String getKey() {
        return get(FIELD_KEY);
    }

    /**
     * 获取appSecret.
     *
     * @return appSecret
     */
    public String getSecret() {
        return get(FIELD_SECRET);
    }

    /**
     * 设置新的Secret.
     *
     * @param secret 新的Secret
     */
    public void setSecret(final String secret) {
        put(FIELD_SECRET, notBlank(secret, DEFAULT_NOT_EMPTY_EX_MESSAGE, FIELD_SECRET));
    }

    @Override
    public void validate(final CredentialDetails original, final Validate validate) {
        if (StringUtils.isBlank(getAppId())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_APP_ID + "未设置");
        }
        if (StringUtils.isBlank(getKey())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_KEY + "未设置");
        }
        if (StringUtils.isBlank(getSecret())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_SECRET + "未设置");
        }
        if (original instanceof ApiKeyCredentials) {
            final ApiKeyCredentials o = (ApiKeyCredentials) original;
            if (!getAppId().equals(o.getAppId())) {
                validate.addFieldError(FIELD_CREDENTIALS, "凭据[" + FIELD_APP_ID + "]不能修改");
            }
            if (!getKey().equals(o.getKey())) {
                validate.addFieldError(FIELD_CREDENTIALS, "凭据[" + FIELD_KEY + "]不能修改");
            }
        } else if (original != null) {
            validate.addActionError("original not instanceof ApiKeyCredentials");
        }
    }

    @Override
    public Bson distinctFilter() {
        return distinctFilter(getAppId(), getKey());
    }

    /**
     * 唯一filter.
     *
     * @param appId appId
     * @param key   key
     * @return 唯一filter
     */
    public static Bson distinctFilter(final String appId, final String key) {
        return Filters.and(Filters.eq(AuthTypable.FIELD_NAME_AUTH_TYPE, AuthType.ApiKey),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_APP_ID, appId),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_KEY, key));
    }
}
