package io.github.dbstarll.dubai.user.entity.ext;

import java.io.Serializable;
import java.util.Date;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public final class PasswordHistory implements Serializable {
  private static final long serialVersionUID = -8179404016625483162L;

  private final String password;
  private final Date date;

  public PasswordHistory(String password, Date date) {
    this.password = notBlank(password, "password is blank");
    this.date = notNull(date, "date is null");
  }

  public String getPassword() {
    return password;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + date.hashCode();
    result = prime * result + password.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PasswordHistory other = (PasswordHistory) obj;
    return date.equals(other.date) && password.equals(other.password);
  }
}
