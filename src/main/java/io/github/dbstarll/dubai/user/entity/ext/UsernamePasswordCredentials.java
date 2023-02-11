package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notBlank;

public final class UsernamePasswordCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 5632135628982288707L;

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_HISTORIES = "histories";
    public static final int MAX_HISTORIES = 5;

    UsernamePasswordCredentials(final String username, final String password, final List<PasswordHistory> histories) {
        put(FIELD_USERNAME, notBlank(username, FIELD_USERNAME + " is blank"));
        setPassword(password);
        if (histories != null) {
            put(FIELD_HISTORIES, noNullElements(histories, "histories contains null element at index: %d"));
        }
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
        return list == null ? null : list.stream().map(this::parsePasswordHistory).collect(Collectors.toList());
    }

    @Override
    public void validate(final Map<String, Object> original, final Validate validate) {
        if (StringUtils.isBlank(getUsername())) {
            validate.addFieldError(FIELD_CREDENTIALS, "用户名未设置");
        }
        if (StringUtils.isBlank(getPassword())) {
            validate.addFieldError(FIELD_CREDENTIALS, "密码未设置");
        }

        if (!validate.hasErrors()) {
            List<Object> histories;
            if (original != null) {
                //noinspection unchecked
                histories = (List<Object>) original.get(FIELD_HISTORIES);
            } else {
                histories = this.get(FIELD_HISTORIES);
            }
            if (histories == null) {
                histories = new LinkedList<>();
            }
            if (histories.isEmpty() || !getPassword().equals(parsePasswordHistory(histories.get(0)).getPassword())) {
                histories.add(0, new PasswordHistory(getPassword(), new Date()));
            }
            while (histories.size() > MAX_HISTORIES) {
                histories.remove(histories.size() - 1);
            }
            put(FIELD_HISTORIES, histories);
        }
    }

    private PasswordHistory parsePasswordHistory(final Object history) {
        if (history instanceof PasswordHistory) {
            return (PasswordHistory) history;
        } else if (history instanceof Document) {
            final Document doc = (Document) history;
            return new PasswordHistory(doc.getString(FIELD_PASSWORD), doc.getDate(FIELD_DATE));
        } else {
            throw new UnsupportedOperationException(history.toString());
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
