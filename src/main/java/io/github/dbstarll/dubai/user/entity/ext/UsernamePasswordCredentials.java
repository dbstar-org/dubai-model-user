package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notBlank;

public final class UsernamePasswordCredentials extends AbstractCredentials {
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_HISTORIES = "histories";
    public static final int MAX_HISTORIES = 5;

    UsernamePasswordCredentials(final String username, final String password) {
        setUsername(username);
        setPassword(password);
    }

    UsernamePasswordCredentials(final Map<String, Object> map) {
        super(map);
    }

    /**
     * 获得用户名.
     *
     * @return 用户名
     */
    public String getUsername() {
        return get(FIELD_USERNAME);
    }

    /**
     * 设置新用户名.
     *
     * @param username 新用户名
     */
    public void setUsername(final String username) {
        put(FIELD_USERNAME, notBlank(username, DEFAULT_NOT_EMPTY_EX_MESSAGE, FIELD_USERNAME));
    }

    /**
     * 获得密码.
     *
     * @return 密码
     */
    public String getPassword() {
        return get(FIELD_PASSWORD);
    }

    /**
     * 设置新密码.
     *
     * @param password 新密码
     */
    public void setPassword(final String password) {
        put(FIELD_PASSWORD, notBlank(password, DEFAULT_NOT_EMPTY_EX_MESSAGE, FIELD_PASSWORD));
    }

    /**
     * 获得密码修改历史.
     *
     * @return list of PasswordHistory
     */
    public List<PasswordHistory> getHistories() {
        final List<Object> list = get(FIELD_HISTORIES);
        return list == null ? null : list.stream().map(PasswordHistory::parse).collect(Collectors.toList());
    }

    /**
     * 设置密码修改历史.
     *
     * @param histories 密码修改历史
     */
    public void setHistories(final List<PasswordHistory> histories) {
        put(FIELD_HISTORIES, noNullElements(histories, FIELD_HISTORIES + " contains null element at index: %d"));
    }

    @Override
    public void validate(final CredentialDetails original, final Validate validate) {
        if (StringUtils.isBlank(getUsername())) {
            validate.addFieldError(FIELD_CREDENTIALS, "用户名未设置");
        }
        if (StringUtils.isBlank(getPassword())) {
            validate.addFieldError(FIELD_CREDENTIALS, "密码未设置");
        }
    }

    @Override
    public Bson distinctFilter() {
        return distinctFilter(getUsername());
    }

    /**
     * 唯一filter.
     *
     * @param username 用户名
     * @return 唯一filter
     */
    public static Bson distinctFilter(final String username) {
        return Filters.and(Filters.eq(AuthTypable.FIELD_NAME_AUTH_TYPE, AuthType.UsernamePassword),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_USERNAME, username));
    }
}
