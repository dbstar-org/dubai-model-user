package io.github.dbstarll.dubai.user.service.attach;

import io.github.dbstarll.dubai.model.service.AutowireException;
import io.github.dbstarll.dubai.model.service.Implemental;
import io.github.dbstarll.dubai.model.service.ImplementalAutowirer;
import io.github.dbstarll.dubai.model.service.ServiceTestCase;
import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.ext.CredentialDetails;
import io.github.dbstarll.dubai.user.entity.ext.Credentials;
import io.github.dbstarll.dubai.user.entity.ext.UsernamePasswordCredentials;
import io.github.dbstarll.dubai.user.service.CredentialService;
import io.github.dbstarll.dubai.user.service.impl.CredentialServiceImplemental;
import io.github.dbstarll.dubai.user.utils.PasswordProperties;
import io.github.dbstarll.dubai.user.utils.PasswordValidator;
import io.github.dbstarll.dubai.user.utils.UsernameProperties;
import io.github.dbstarll.dubai.user.utils.UsernameValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CredentialServiceAttachTest extends ServiceTestCase {
    private static final Class<CredentialService> serviceClass = CredentialService.class;

    private UsernameProperties usernameProperties;
    private UsernameValidator usernameValidator;
    private PasswordProperties passwordProperties;
    private PasswordValidator passwordValidator;

    @BeforeAll
    static void beforeAll() {
        globalCollectionFactory();
    }

    @BeforeEach
    void setUp() {
        usernameProperties = new UsernameProperties();
        usernameValidator = new UsernameValidator(usernameProperties);
        passwordProperties = new PasswordProperties();
        passwordValidator = new PasswordValidator(passwordProperties);
    }

    @AfterEach
    void tearDown() {
        passwordValidator = null;
        passwordProperties = null;
        usernameValidator = null;
        usernameProperties = null;
    }

    private void useService(final Consumer<CredentialService> consumer) {
        useService(serviceClass, new ImplementalAutowirer() {
            @Override
            public <I extends Implemental> void autowire(I i) throws AutowireException {
                if (i instanceof CredentialServiceImplemental) {
                    ((CredentialServiceImplemental) i).setUsernameValidator(usernameValidator);
                    ((CredentialServiceImplemental) i).setPasswordValidator(passwordValidator);
                }
            }
        }, consumer);
    }

    @Test
    void credentialsValidationNotSet() {
        useService(s -> {
            final Credential credential = Credentials.miniProgram("appId", "openId");
            credential.setPrincipalId(new ObjectId());
            credential.setCredentials(null);
            final Validate validate = new DefaultValidate();
            assertNull(s.save(credential, validate));
            assertTrue(validate.hasErrors());
            assertFalse(validate.hasActionErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("凭据未设置"), validate.getFieldErrors()
                    .get(CredentialDetails.FIELD_CREDENTIALS));
        });
    }

    @Test
    void credentialsValidationEmpty() {
        useService(s -> {
            final Credential credential = Credentials.miniProgram("appId", "openId");
            credential.setPrincipalId(new ObjectId());
            credential.getCredentials().clear();
            final Validate validate = new DefaultValidate();
            assertNull(s.save(credential, validate));
            assertTrue(validate.hasErrors());
            assertFalse(validate.hasActionErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("凭据未设置"), validate.getFieldErrors()
                    .get(CredentialDetails.FIELD_CREDENTIALS));
        });
    }

    @Test
    void duplicateValidation1() {
        final ObjectId principalId = new ObjectId();
        useService(s -> {
            final Credential credential = Credentials.usernamePassword("username", "PassWord0");
            credential.setPrincipalId(principalId);
            final Validate validate = new DefaultValidate();
            assertSame(credential, s.save(credential, validate));
            assertFalse(validate.hasErrors());

            final Credential credential2 = Credentials.usernamePassword("username2", "PassWord2");
            credential2.setPrincipalId(principalId);
            assertNull(s.save(credential2, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasActionErrors());
            assertFalse(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("同一认证类型下的主体必须唯一"), validate.getActionErrors());
        });
    }

    @Test
    void duplicateValidation2() {
        useService(s -> {
            final Credential credential = Credentials.usernamePassword("username", "PassWord0");
            credential.setPrincipalId(new ObjectId());
            final Validate validate = new DefaultValidate();
            assertSame(credential, s.save(credential, validate));
            assertFalse(validate.hasErrors());

            final Credential credential2 = Credentials.usernamePassword("username", "PassWord0");
            credential2.setPrincipalId(new ObjectId());
            assertNull(s.save(credential2, validate));
            assertTrue(validate.hasErrors());
            assertFalse(validate.hasActionErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("凭据[UsernamePassword]不唯一"), validate.getFieldErrors()
                    .get(CredentialDetails.FIELD_CREDENTIALS));
        });
    }

    @Test
    void passwordHistoryAppender() {
        useService(s -> {
            final Validate validate = new DefaultValidate();
            final Credential credential = Credentials.usernamePassword("username", "PassWord0");
            {
                credential.setPrincipalId(new ObjectId());
                assertSame(credential, s.save(credential, validate));
                assertFalse(validate.hasErrors());
                final UsernamePasswordCredentials details = assertInstanceOf(UsernamePasswordCredentials.class,
                        Credentials.credentials(credential));
                assertNotNull(details.getHistories());
                assertEquals(1, details.getHistories().size());
                assertEquals("PassWord0", details.getHistories().get(0).getPassword());
            }

            // change username and password
            final Credential loaded = s.findById(credential.getId());
            {
                assertEquals(credential, loaded);
                final UsernamePasswordCredentials details = assertInstanceOf(UsernamePasswordCredentials.class,
                        Credentials.credentials(loaded));
                details.setUsername("username1");
                details.setPassword("PassWord1");
                assertSame(loaded, s.save(loaded, validate));
                assertFalse(validate.hasErrors());
                assertNotNull(details.getHistories());
                assertEquals(2, details.getHistories().size());
                assertEquals("PassWord1", details.getHistories().get(0).getPassword());
                assertEquals("PassWord0", details.getHistories().get(1).getPassword());
            }

            // same username and password
            final Credential loaded2 = s.findById(credential.getId());
            {
                assertEquals(loaded, loaded2);
                final UsernamePasswordCredentials details = assertInstanceOf(UsernamePasswordCredentials.class,
                        Credentials.credentials(loaded2));
                loaded2.setDisabled(true);
                assertSame(loaded2, s.save(loaded2, validate));
                assertFalse(validate.hasErrors());
                assertNotNull(details.getHistories());
                assertEquals(2, details.getHistories().size());
                assertEquals("PassWord1", details.getHistories().get(0).getPassword());
                assertEquals("PassWord0", details.getHistories().get(1).getPassword());
            }
        });
    }
}