package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.Map;

import static org.apache.commons.lang3.Validate.notBlank;

public final class MiniProgramCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 5632135628982288707L;

    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_OPEN_ID = "openid";

    MiniProgramCredentials(final String appId, final String openid) {
        put(FIELD_APP_ID, notBlank(appId, FIELD_APP_ID + " is blank"));
        put(FIELD_OPEN_ID, notBlank(openid, FIELD_OPEN_ID + " is blank"));
    }

    MiniProgramCredentials(final Map<String, Object> map) {
        super(map);
    }

    /**
     * 获得appId.
     *
     * @return appId
     */
    public String getAppId() {
        return get(FIELD_APP_ID);
    }

    /**
     * 获得openId.
     *
     * @return openId
     */
    public String getOpenid() {
        return get(FIELD_OPEN_ID);
    }

    @Override
    public void validate(final CredentialDetails original, final Validate validate) {
        if (StringUtils.isBlank(getAppId())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_APP_ID + "未设置");
        }
        if (StringUtils.isBlank(getOpenid())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_OPEN_ID + "未设置");
        }
        if (original instanceof MiniProgramCredentials) {
            final MiniProgramCredentials o = (MiniProgramCredentials) original;
            if (!getAppId().equals(o.getAppId())) {
                validate.addFieldError(FIELD_CREDENTIALS, "凭据[" + FIELD_APP_ID + "]不能修改");
            }
            if (!getOpenid().equals(o.getOpenid())) {
                validate.addFieldError(FIELD_CREDENTIALS, "凭据[" + FIELD_OPEN_ID + "]不能修改");
            }
        } else if (original != null) {
            throw new UnsupportedOperationException("original not instanceof MiniProgramCredentials");
        }
    }

    @Override
    public Bson distinctFilter() {
        return distinctFilter(getAppId(), getOpenid());
    }

    /**
     * 唯一filter.
     *
     * @param appId  appId
     * @param openid openid
     * @return 唯一filter
     */
    public static Bson distinctFilter(final String appId, final String openid) {
        return Filters.and(Filters.eq(AuthTypable.FIELD_NAME_AUTH_TYPE, AuthType.MiniProgram),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_APP_ID, appId),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_OPEN_ID, openid));
    }
}
