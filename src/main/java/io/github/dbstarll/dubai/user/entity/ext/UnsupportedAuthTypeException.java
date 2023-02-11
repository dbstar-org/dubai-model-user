package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.user.entity.enums.AuthType;

public class UnsupportedAuthTypeException extends Exception {
    /**
     * 构建一个UnsupportedAuthTypeException实例.
     *
     * @param authType 不支持的认证类型
     */
    public UnsupportedAuthTypeException(final AuthType authType) {
        super("尚不支持: " + authType);
    }
}
