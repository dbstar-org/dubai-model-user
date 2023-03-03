package io.github.dbstarll.dubai.user.service.attach;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.model.service.ServiceTestCase;
import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.TestAuthTypeEntity;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.service.TestAuthTypeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthTypeAttachTest extends ServiceTestCase {
    private static final Class<TestAuthTypeEntity> entityClass = TestAuthTypeEntity.class;
    private static final Class<TestAuthTypeService> serviceClass = TestAuthTypeService.class;

    @BeforeAll
    static void beforeAll() {
        globalCollectionFactory();
    }

    @Test
    void filterByAuthType() {
        useService(serviceClass, s -> assertEquals("Filter{fieldName='source', value=MiniProgram}",
                s.filterByAuthType(AuthType.MINI_PROGRAM).toString()));
    }

    @Test
    void countByAuthType() {
        useService(serviceClass, s -> {
            assertEquals(0, s.countByAuthType(AuthType.MINI_PROGRAM));
            final TestAuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MINI_PROGRAM);
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.countByAuthType(AuthType.MINI_PROGRAM));
        });
    }

    @Test
    void findByAuthType() {
        useService(serviceClass, s -> {
            assertNull(s.findByAuthType(AuthType.MINI_PROGRAM).first());
            final TestAuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MINI_PROGRAM);
            assertSame(entity, s.save(entity, null));
            assertEquals(entity, s.findByAuthType(AuthType.MINI_PROGRAM).first());
        });
    }

    @Test
    void deleteByAuthType() {
        useService(serviceClass, s -> {
            assertEquals(0, s.deleteByAuthType(AuthType.MINI_PROGRAM).getDeletedCount());
            final TestAuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MINI_PROGRAM);
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.deleteByAuthType(AuthType.MINI_PROGRAM).getDeletedCount());
            assertNull(s.findById(entity.getId()));
        });
    }

    @Test
    void authTypeValidationNotSet() {
        useService(serviceClass, s -> {
            final TestAuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证类型未设置"), validate.getFieldErrors()
                    .get(AuthTypable.FIELD_NAME_AUTH_TYPE));
        });
    }

    @Test
    void authTypeValidationChange() {
        useService(serviceClass, s -> {
            final TestAuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MINI_PROGRAM);
            assertSame(entity, s.save(entity, null));

            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertFalse(validate.hasErrors());

            entity.setSource(AuthType.USERNAME_PASSWORD);
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证类型不可更改"), validate.getFieldErrors()
                    .get(AuthTypable.FIELD_NAME_AUTH_TYPE));
        });
    }
}