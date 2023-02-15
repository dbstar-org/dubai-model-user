package io.github.dbstarll.dubai.user.entity.ext;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;

import java.io.Serializable;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public final class PasswordHistory implements Comparable<PasswordHistory>, Serializable {
    private static final long serialVersionUID = -8179404016625483162L;

    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_DATE = "date";

    private final String password;
    private final Date date;

    /**
     * 历史设置过的密码信息.
     *
     * @param password 密码
     * @param date     设置时间
     */
    public PasswordHistory(final String password, final Date date) {
        this.password = notBlank(password, "%s is blank", FIELD_PASSWORD);
        this.date = notNull(date, "%s is null", FIELD_DATE);
    }

    private PasswordHistory(final Document document) {
        this(notNull(document, "document is null").getString(FIELD_PASSWORD), document.getDate(FIELD_DATE));
    }

    /**
     * 获得密码.
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 获得设置时间.
     *
     * @return 设置时间
     */
    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(final PasswordHistory o) {
        return o.getDate().compareTo(getDate());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        }

        final PasswordHistory that;
        if (o instanceof Document) {
            that = new PasswordHistory((Document) o);
        } else if (o instanceof PasswordHistory) {
            that = (PasswordHistory) o;
        } else {
            return false;
        }

        return new EqualsBuilder()
                .append(getPassword(), that.getPassword())
                .append(getDate(), that.getDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getPassword())
                .append(getDate())
                .toHashCode();
    }

    static PasswordHistory parse(final Object history) {
        if (history instanceof PasswordHistory) {
            return (PasswordHistory) history;
        } else if (history instanceof Document) {
            return new PasswordHistory((Document) history);
        } else {
            throw new UnsupportedOperationException(notNull(history).getClass().getName());
        }
    }
}
