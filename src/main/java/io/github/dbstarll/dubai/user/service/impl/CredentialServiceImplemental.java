package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation.Position;
import io.github.dbstarll.dubai.model.service.validation.Validation;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.ext.CredentialDetails;
import io.github.dbstarll.dubai.user.entity.ext.Credentials;
import io.github.dbstarll.dubai.user.entity.ext.UnsupportedAuthTypeException;
import io.github.dbstarll.dubai.user.service.CredentialService;
import io.github.dbstarll.dubai.user.service.attach.CredentialServiceAttach;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public final class CredentialServiceImplemental extends UserImplementals<Credential, CredentialService>
        implements CredentialServiceAttach {
    /**
     * 构造CredentialServiceImplemental.
     *
     * @param service    服务类
     * @param collection 集合类
     */
    public CredentialServiceImplemental(final CredentialService service, final Collection<Credential> collection) {
        super(service, collection);
    }

    /**
     * 凭据校验.
     *
     * @return credentialsValidation
     */
    @GeneralValidation
    public Validation<Credential> credentialsValidation() {
        return new AbstractEntityValidation() {
            @Override
            public void validate(final Credential entity, final Credential original, final Validate validate) {
                if (entity.getCredentials() == null || entity.getCredentials().isEmpty()) {
                    validate.addFieldError(CredentialDetails.FIELD_CREDENTIALS, "凭据未设置");
                } else {
                    final CredentialDetails details;
                    try {
                        details = Credentials.credentials(entity);
                    } catch (UnsupportedAuthTypeException e) {
                        validate.addActionError(e.getMessage());
                        return;
                    }
                    details.validate(original == null ? null : original.getCredentials(), validate);
                }
            }
        };
    }

    /**
     * 唯一性校验.
     *
     * @return duplicateValidation
     */
    @GeneralValidation(position = Position.POST)
    public Validation<Credential> duplicateValidation() {
        return new AbstractEntityValidation() {
            @Override
            public void validate(final Credential entity, final Credential original, final Validate validate) {
                if (!validate.hasErrors()) {
                    if (original == null) {
                        if (service.count(Filters.and(service.filterByPrincipalId(entity.getPrincipalId()),
                                service.filterByAuthType(entity.getSource()))) > 0) {
                            validate.addActionError("同一认证类型下的主体必须唯一");
                        }
                    }
                    final CredentialDetails details;
                    try {
                        details = Credentials.credentials(entity);
                    } catch (UnsupportedAuthTypeException e) {
                        validate.addActionError(e.getMessage());
                        return;
                    }
                    final List<Bson> filters = new ArrayList<>();
                    filters.add(details.distinctFilter());
                    if (original != null) {
                        filters.add(Filters.ne(Entity.FIELD_NAME_ID, original.getId()));
                    }
                    if (service.count(Filters.and(filters)) > 0) {
                        validate.addFieldError(CredentialDetails.FIELD_CREDENTIALS, "凭据不唯一");
                    }
                }
            }
        };
    }
}
