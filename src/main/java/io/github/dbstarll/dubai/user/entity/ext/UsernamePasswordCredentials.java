package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notBlank;

public final class UsernamePasswordCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 5632135628982288707L;

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
        put(FIELD_USERNAME, notBlank(username, FIELD_USERNAME + " is blank"));
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
        put(FIELD_PASSWORD, notBlank(password, FIELD_PASSWORD + " is blank"));
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

    @Override
    public void validate(final CredentialDetails original, final Validate validate) {
        if (StringUtils.isBlank(getUsername())) {
            validate.addFieldError(FIELD_CREDENTIALS, "用户名未设置");
        }
        if (StringUtils.isBlank(getPassword())) {
            validate.addFieldError(FIELD_CREDENTIALS, "密码未设置");
        }
        if (original instanceof UsernamePasswordCredentials) {
            final UsernamePasswordCredentials o = (UsernamePasswordCredentials) original;
            //TODO 换个地方追加PasswordHistory
            if (!validate.hasErrors()) {
                List<PasswordHistory> histories = o.getHistories();
                if (histories == null) {
                    histories = new LinkedList<>();
                }
                if (histories.isEmpty() || !getPassword().equals(histories.get(0).getPassword())) {
                    histories.add(0, new PasswordHistory(getPassword(), new Date()));
                }
                while (histories.size() > MAX_HISTORIES) {
                    histories.remove(histories.size() - 1);
                }
                put(FIELD_HISTORIES, histories);
            }
        } else if (original != null) {
            throw new UnsupportedOperationException("original not instanceof UsernamePasswordCredentials");
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
