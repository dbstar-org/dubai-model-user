package io.github.dbstarll.dubai.user.entity.enums;

import io.github.dbstarll.utils.lang.enums.EnumValue;

/**
 * 认证类型.
 */
@EnumValue(method = "toString")
public enum AuthType {
    USERNAME_PASSWORD("UsernamePassword"), SMS("SMS"), MINI_PROGRAM("MiniProgram"), API_KEY("ApiKey"), X509("X509");

    private final String title;

    AuthType(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
