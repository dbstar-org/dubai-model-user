package io.github.dbstarll.dubai.user.entity.ext;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notBlank;

public final class UsernamePasswordCredentials extends AbstractCredentials {
    private static final long serialVersionUID = 5632135628982288707L;

    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_HISTORIES = "histories";
    public static final int MAX_HISTORIES = 5;

    UsernamePasswordCredentials(String username, String password, List<PasswordHistory> histories) {
        setUsername(username);
        setPassword(password);
        if (histories != null) {
            put(FIELD_HISTORIES, noNullElements(histories, "histories contains null element at index: %d"));
        }
    }

    UsernamePasswordCredentials(Map<String, Object> map) {
        super(map);
    }

    public String getUsername() {
        return (String) get(FIELD_USERNAME);
    }

    public void setUsername(String username) {
        put(FIELD_USERNAME, notBlank(username, FIELD_USERNAME + " is blank"));
    }

    public String getPassword() {
        return (String) get(FIELD_PASSWORD);
    }

    public void setPassword(String password) {
        put(FIELD_PASSWORD, notBlank(password, FIELD_PASSWORD + " is blank"));
    }

    /**
     * 获得密码修改历史.
     *
     * @return list of PasswordHistory
     */
    public List<PasswordHistory> getHistories() {
        if (containsKey(FIELD_HISTORIES)) {
            final List<PasswordHistory> histories = new LinkedList<>();
            for (Object history : (List<?>) get(FIELD_HISTORIES)) {
                histories.add(parsePasswordHistory(history));
            }
            return histories;
        }
        return null;
    }

    @Override
    public void validate(Map<String, Object> original, Validate validate) {
        if (StringUtils.isBlank(getUsername())) {
            validate.addFieldError(FIELD_CREDENTIALS, "用户名未设置");
        }
        if (StringUtils.isBlank(getPassword())) {
            validate.addFieldError(FIELD_CREDENTIALS, "密码未设置");
        }

        if (!validate.hasErrors()) {
            @SuppressWarnings("unchecked") List<Object> histories = (List<Object>) (original != null ? original : this)
                    .get(FIELD_HISTORIES);
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

    private PasswordHistory parsePasswordHistory(Object history) {
        if (PasswordHistory.class.isInstance(history)) {
            return (PasswordHistory) history;
        } else if (Document.class.isInstance(history)) {
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

    public static Bson distinctFilter(String username) {
        return Filters.and(Filters.eq("source", SourceType.UsernamePassword),
                Filters.eq(FIELD_CREDENTIALS + '.' + FIELD_USERNAME, username));
    }
}
