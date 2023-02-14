package io.github.dbstarll.dubai.user.service.attach;

import com.mongodb.client.model.Filters;
import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.model.service.AutowireException;
import io.github.dbstarll.dubai.model.service.Implemental;
import io.github.dbstarll.dubai.model.service.ImplementalAutowirer;
import io.github.dbstarll.dubai.model.service.ServiceTestCase;
import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.Credential;
import io.github.dbstarll.dubai.user.entity.TestCredentialEntity;
import io.github.dbstarll.dubai.user.entity.ext.Credentials;
import io.github.dbstarll.dubai.user.entity.join.CredentialBase;
import io.github.dbstarll.dubai.user.service.CredentialService;
import io.github.dbstarll.dubai.user.service.TestCredentialService;
import io.github.dbstarll.dubai.user.service.impl.CredentialAttachImplemental;
import io.github.dbstarll.dubai.user.service.impl.CredentialServiceImplemental;
import io.github.dbstarll.dubai.user.utils.PasswordProperties;
import io.github.dbstarll.dubai.user.utils.PasswordValidator;
import io.github.dbstarll.dubai.user.utils.UsernameProperties;
import io.github.dbstarll.dubai.user.utils.UsernameValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialAttachTest extends ServiceTestCase {
    private static final Class<TestCredentialEntity> entityClass = TestCredentialEntity.class;
    private static final Class<TestCredentialService> serviceClass = TestCredentialService.class;

    private UsernameProperties usernameProperties;
    private UsernameValidator usernameValidator;
    private PasswordProperties passwordProperties;
    private PasswordValidator passwordValidator;

    @BeforeAll
    static void beforeAll() {
        globalCollectionFactory();
    }

    @AfterAll
    static void afterAll() {
        cleanupGlobal();
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
        cleanupTest();
        passwordValidator = null;
        passwordProperties = null;
        usernameValidator = null;
        usernameProperties = null;
    }


    private void useServiceAutowirer(final BiConsumer<Credential, TestCredentialService> consumer) {
        useService(CredentialService.class, new ImplementalAutowirer() {
            @Override
            public <I extends Implemental> void autowire(I i) throws AutowireException {
                if (i instanceof CredentialServiceImplemental) {
                    ((CredentialServiceImplemental) i).setUsernameValidator(usernameValidator);
                    ((CredentialServiceImplemental) i).setPasswordValidator(passwordValidator);
                }
            }
        }, cs -> {
            final Credential credential = Credentials.miniProgram("appId", "openId");
            credential.setPrincipalId(new ObjectId());
            assertSame(credential, cs.save(credential, null));

            useService(serviceClass, new ImplementalAutowirer() {
                @Override
                public <I extends Implemental> void autowire(I i) throws AutowireException {
                    if (i instanceof CredentialAttachImplemental) {
                        ((CredentialAttachImplemental<?, ?>) i).setCredentialService(cs);
                    }
                }
            }, s -> consumer.accept(credential, s));
        });
    }

    @Test
    void filterByCredentialId() {
        useServiceAutowirer((c, s) -> assertEquals(Filters.eq(CredentialBase.FIELD_NAME_CREDENTIAL_ID, c.getId()),
                s.filterByCredentialId(c.getId())));
    }

    @Test
    void countByCredentialId() {
        useServiceAutowirer((c, s) -> {
            assertEquals(0, s.countByCredentialId(c.getId()));
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(c.getId());
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.countByCredentialId(c.getId()));
        });
    }

    @Test
    void findByCredentialId() {
        useServiceAutowirer((c, s) -> {
            assertNull(s.findByCredentialId(new ObjectId()).first());
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(c.getId());
            assertSame(entity, s.save(entity, null));
            assertEquals(entity, s.findByCredentialId(c.getId()).first());
        });
    }

    @Test
    void deleteByCredentialId() {
        useServiceAutowirer((c, s) -> {
            assertEquals(0, s.deleteByCredentialId(c.getId()).getDeletedCount());
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(c.getId());
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.deleteByCredentialId(c.getId()).getDeletedCount());
            assertNull(s.findById(entity.getId()));
        });
    }

    @Test
    void findWithCredential() {
        useServiceAutowirer((c, s) -> {
            assertNull(s.findWithCredential(null).first());

            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(c.getId());
            assertSame(entity, s.save(entity, null));

            final Entry<TestCredentialEntity, Credential> match = s.findWithCredential(Filters.eq(entity.getId())).first();
            assertNotNull(match);
            assertEquals(entity, match.getKey());
            assertEquals(c, match.getValue());
        });
    }

    @Test
    void credentialIdValidationNotSet() {
        useServiceAutowirer((c, s) -> {
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证凭据未设置"), validate.getFieldErrors()
                    .get(CredentialBase.FIELD_NAME_CREDENTIAL_ID));
        });
    }

    @Test
    void credentialIdValidationChange() {
        useServiceAutowirer((c, s) -> {
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(c.getId());
            assertSame(entity, s.save(entity, null));

            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertFalse(validate.hasErrors());

            entity.setCredentialId(new ObjectId());
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证凭据不可更改"), validate.getFieldErrors()
                    .get(CredentialBase.FIELD_NAME_CREDENTIAL_ID));
        });
    }

    @Test
    void credentialIdValidationNotExist() {
        useServiceAutowirer((c, s) -> {
            final TestCredentialEntity entity = EntityFactory.newInstance(entityClass);
            entity.setCredentialId(new ObjectId());
            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证凭据不存在"), validate.getFieldErrors()
                    .get(CredentialBase.FIELD_NAME_CREDENTIAL_ID));
        });
    }
}