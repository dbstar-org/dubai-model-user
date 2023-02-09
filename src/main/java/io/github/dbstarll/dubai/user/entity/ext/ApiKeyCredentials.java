package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.io.Serializable;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notBlank;

public final class ApiKeyCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 828294776886147992L;

    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_KEY = "key";
    public static final String FIELD_SECRET = "secret";

    ApiKeyCredentials(final String appId, final String key, final String secret) {
        put(FIELD_APP_ID, notBlank(appId, FIELD_APP_ID + " is blank"));
        put(FIELD_KEY, notBlank(key, FIELD_KEY + " is blank"));
        put(FIELD_SECRET, notBlank(secret, FIELD_SECRET + " is blank"));
    }

    ApiKeyCredentials(final Map<String, Serializable> map) {
        super(map);
    }

    /**
     * 获取appId.
     *
     * @return appId
     */
    public String getAppId() {
        return (String) get(FIELD_APP_ID);
    }

    /**
     * 获取appKey.
     *
     * @return appKey
     */
    public String getKey() {
        return (String) get(FIELD_KEY);
    }

    /**
     * 获取appSecret.
     *
     * @return appSecret
     */
    public String getSecret() {
        return (String) get(FIELD_SECRET);
    }

    @Override
    public void validate(final Map<String, Serializable> original, final Validate validate) {
        if (StringUtils.isBlank(getAppId())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_APP_ID + "未设置");
        }
        if (StringUtils.isBlank(getKey())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_KEY + "未设置");
        }
        if (StringUtils.isBlank(getSecret())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_SECRET + "未设置");
        }
        if (original != null && !this.equals(original)) {
            validate.addFieldError(FIELD_CREDENTIALS, "凭据不能修改");
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
        return Filters.and(Filters.eq("source", SourceType.ApiKey),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_APP_ID, appId),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_KEY, key));
    }
}
