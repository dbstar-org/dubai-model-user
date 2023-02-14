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
import io.github.dbstarll.dubai.user.entity.ext.PasswordHistory;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import io.github.dbstarll.dubai.user.service.CredentialService;
import io.github.dbstarll.dubai.user.service.attach.CredentialServiceAttach;
import io.github.dbstarll.dubai.user.utils.PasswordValidator;
import io.github.dbstarll.dubai.user.utils.UsernameValidator;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections.MapUtils.isEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public final class CredentialServiceImplemental extends UserImplementals<Credential, CredentialService>
        implements CredentialServiceAttach {
    private UsernameValidator usernameValidator;
    private PasswordValidator passwordValidator;

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
     * 注入用户名校验器.
     *
     * @param usernameValidator 用户名校验器
     */
    public void setUsernameValidator(final UsernameValidator usernameValidator) {
        this.usernameValidator = usernameValidator;
    }

    /**
     * 注入密码校验器.
     *
     * @param passwordValidator 密码校验器
     */
    public void setPasswordValidator(final PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void afterPropertiesSet() {
        notNull(usernameValidator, "usernameValidator not set");
        notNull(passwordValidator, "passwordValidator not set");
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
                if (isEmpty(entity.getCredentials())) {
                    validate.addFieldError(CredentialDetails.FIELD_CREDENTIALS, "凭据未设置");
                } else {
                    final CredentialDetails details = Credentials.credentials(entity);
                    details.validate(Credentials.credentials(original), validate);

                    if (details instanceof UsernamePasswordCredentials) {
                        validateUsernamePassword(
                                (UsernamePasswordCredentials) details,
                                (UsernamePasswordCredentials) Credentials.credentials(original),
                                validate);
                    }
                }
            }

            private void validateUsernamePassword(final UsernamePasswordCredentials credentials,
                                                  final UsernamePasswordCredentials original,
                                                  final Validate validate) {
                if (original == null) {
                    usernameValidator.validate(credentials.getUsername(), validate);
                    passwordValidator.validate(credentials.getPassword(), validate);
                } else {
                    if (!original.getUsername().equals(credentials.getUsername())) {
                        usernameValidator.validate(credentials.getUsername(), validate);
                    }
                    if (!original.getPassword().equals(credentials.getPassword())) {
                        passwordValidator.validate(credentials.getPassword(), validate);
                        passwordValidator.validateHistory(credentials.getPassword(), original.getHistories(), validate);
                    }
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
                    if (original == null && service.count(Filters.and(
                            service.filterByPrincipalId(entity.getPrincipalId()),
                            service.filterByAuthType(entity.getSource()))) > 0) {
                        validate.addActionError("同一认证类型下的主体必须唯一");
                    }
                    final List<Bson> filters = new ArrayList<>();
                    filters.add(Credentials.credentials(entity).distinctFilter());
                    if (original != null) {
                        filters.add(Filters.ne(Entity.FIELD_NAME_ID, original.getId()));
                    }
                    if (service.count(Filters.and(filters)) > 0) {
                        validate.addFieldError(CredentialDetails.FIELD_CREDENTIALS,
                                "凭据[" + entity.getSource() + "]不唯一");
                    }
                }
            }
        };
    }

    /**
     * 用于向UsernamePasswordCredentials中追加PasswordHistory.
     *
     * @return passwordHistoryAppender
     */
    @GeneralValidation(position = Position.LAST)
    public Validation<Credential> passwordHistoryAppender() {
        return new AbstractEntityValidation() {
            @Override
            public void validate(final Credential entity, final Credential original, final Validate validate) {
                if (!validate.hasErrors()) {
                    final CredentialDetails details = Credentials.credentials(entity);
                    if (details instanceof UsernamePasswordCredentials) {
                        appendPasswordHistory(
                                (UsernamePasswordCredentials) details,
                                (UsernamePasswordCredentials) Credentials.credentials(original));
                    }
                }
            }

            private void appendPasswordHistory(final UsernamePasswordCredentials credentials,
                                               final UsernamePasswordCredentials original) {
                final List<PasswordHistory> histories;
                if (original != null && StringUtils.equals(original.getPassword(), credentials.getPassword())) {
                    // password exist and not change
                    histories = Optional.ofNullable(original.getHistories()).orElseGet(ArrayList::new);
                } else {
                    // create new PasswordHistory
                    histories = new ArrayList<>();
                    histories.add(new PasswordHistory(credentials.getPassword(), new Date()));
                    if (original != null) {
                        histories.addAll(Optional.ofNullable(original.getHistories()).orElseGet(ArrayList::new));
                    }
                }
                credentials.setHistories(histories.stream().sorted().limit(passwordValidator.getMaxPasswordHistory())
                        .collect(Collectors.toList()));
            }
        };
    }
}
