package io.github.dbstarll.dubai.user.entity.ext;

import io.github.dbstarll.dubai.model.service.validate.Validate;
import org.bson.conversions.Bson;

import java.util.Map;

public interface CredentialDetails extends Map<String, Object> {
    String FIELD_CREDENTIALS = "credentials";

    void validate(Map<String, Object> original, Validate validate);

    Bson distinctFilter();
}
