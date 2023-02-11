package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import org.bson.conversions.Bson;

import java.util.Map;

public interface CredentialDetails {
    String FIELD_CREDENTIALS = "credentials";

    /**
     * 对凭据的合法性进行校验.
     *
     * @param original 未修改前的历史凭据详情
     * @param validate 校验器
     */
    void validate(Map<String, Object> original, Validate validate);

    /**
     * 返回检查凭据唯一性的过滤条件.
     *
     * @return 检查凭据唯一性的过滤条件
     */
    Bson distinctFilter();
}
