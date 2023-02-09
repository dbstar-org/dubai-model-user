package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.io.Serializable;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notBlank;

public final class MiniProgramCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 5632135628982288707L;

    public static final String FIELD_APP_ID = "appId";
    public static final String FIELD_OPENID = "openid";

    MiniProgramCredentials(final String appId, final String openid) {
        put(FIELD_APP_ID, notBlank(appId, FIELD_APP_ID + " is blank"));
        put(FIELD_OPENID, notBlank(openid, FIELD_OPENID + " is blank"));
    }

    MiniProgramCredentials(final Map<String, Serializable> map) {
        super(map);
    }

    /**
     * 获得appId.
     *
     * @return appId
     */
    public String getAppId() {
        return (String) get(FIELD_APP_ID);
    }

    /**
     * 获得openId.
     *
     * @return openId
     */
    public String getOpenid() {
        return (String) get(FIELD_OPENID);
    }

    @Override
    public void validate(final Map<String, Serializable> original, final Validate validate) {
        if (StringUtils.isBlank(getAppId())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_APP_ID + "未设置");
        }
        if (StringUtils.isBlank(getOpenid())) {
            validate.addFieldError(FIELD_CREDENTIALS, FIELD_OPENID + "未设置");
        }
        if (original != null && !this.equals(original)) {
            validate.addFieldError(FIELD_CREDENTIALS, "凭据不能修改");
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
        return Filters.and(Filters.eq("source", SourceType.MiniProgram),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_APP_ID, appId),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_OPENID, openid));
    }
}
