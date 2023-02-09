package io.github.dbstarll.dubai.user.service.impl;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.collection.Collection;
import io.github.dbstarll.dubai.model.entity.Entity;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation;
import io.github.dbstarll.dubai.model.service.validation.GeneralValidation.Position;
import io.github.dbstarll.dubai.model.service.validation.Validation;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.enums.SourceType;
import io.github.dbstarll.dubai.user.entity.ext.CredentialDetails;
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

    @Override
    public Bson filterBySource(final SourceType source) {
        return Filters.eq("source", source);
    }

    /**
     * 来源校验.
     *
     * @return sourceValidation
     */
    @GeneralValidation
    public Validation<Credential> sourceValidation() {
        return new AbstractEntityValidation() {
            @Override
            public void validate(final Credential entity, final Credential original, final Validate validate) {
                if (entity.getSource() == null) {
                    validate.addFieldError("source", "来源未设置");
                } else if (original != null && !entity.getSource().equals(original.getSource())) {
                    validate.addFieldError("source", "来源不可更改");
                }
            }
        };
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
                } else if (entity.getCredentials() instanceof CredentialDetails) {
                    ((CredentialDetails) entity.getCredentials()).
                            validate(original == null ? null : original.getCredentials(), validate);
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
                                service.filterBySource(entity.getSource()))) > 0) {
                            validate.addActionError("同一来源下的主体必须唯一");
                        }
                    }

                    if (entity.getCredentials() instanceof CredentialDetails) {
                        final List<Bson> filters = new ArrayList<>();
                        filters.add(((CredentialDetails) entity.getCredentials()).distinctFilter());
                        if (original != null) {
                            filters.add(Filters.ne(Entity.FIELD_NAME_ID, original.getId()));
                        }
                        if (service.count(Filters.and(filters)) > 0) {
                            validate.addFieldError(CredentialDetails.FIELD_CREDENTIALS, "凭据不唯一");
                        }
                    }
                }
            }
        };
    }
}
