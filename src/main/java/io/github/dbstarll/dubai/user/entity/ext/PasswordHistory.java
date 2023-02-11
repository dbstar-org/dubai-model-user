package io.github.dbstarll.dubai.user.entity.ext;

import java.io.Serializable;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public final class PasswordHistory implements Comparable<PasswordHistory>, Serializable {
    private static final long serialVersionUID = -8179404016625483162L;

    private final String password;
    private final Date date;

    /**
     * 历史设置过的密码信息.
     *
     * @param password 密码
     * @param date     设置时间
     */
    public PasswordHistory(final String password, final Date date) {
        this.password = notBlank(password, "password is blank");
        this.date = notNull(date, "date is null");
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
}
