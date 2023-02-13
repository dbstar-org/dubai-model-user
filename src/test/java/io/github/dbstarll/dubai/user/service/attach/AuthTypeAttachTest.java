package io.github.dbstarll.dubai.user.service.attach;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.dubai.model.service.ServiceTestCase;
import io.github.dbstarll.dubai.model.service.validate.DefaultValidate;
import io.github.dbstarll.dubai.model.service.validate.Validate;
import io.github.dbstarll.dubai.user.entity.AuthTypeEntity;
import io.github.dbstarll.dubai.user.entity.enums.AuthType;
import io.github.dbstarll.dubai.user.entity.join.AuthTypable;
import io.github.dbstarll.dubai.user.service.AuthTypeService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthTypeAttachTest extends ServiceTestCase {
    private static final Class<AuthTypeEntity> entityClass = AuthTypeEntity.class;
    private static final Class<AuthTypeService> serviceClass = AuthTypeService.class;

    @BeforeAll
    static void beforeAll() {
        globalCollectionFactory();
    }

    @AfterAll
    static void afterAll() {
        cleanupGlobal();
    }

    @AfterEach
    void tearDown() {
        cleanupTest();
    }

    @Test
    void filterByAuthType() {
        useService(serviceClass, s -> assertEquals("Filter{fieldName='source', value=MiniProgram}",
                s.filterByAuthType(AuthType.MiniProgram).toString()));
    }

    @Test
    void countByAuthType() {
        useService(serviceClass, s -> {
            assertEquals(0, s.countByAuthType(AuthType.MiniProgram));
            final AuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MiniProgram);
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.countByAuthType(AuthType.MiniProgram));
        });
    }

    @Test
    void findByAuthType() {
        useService(serviceClass, s -> {
            assertNull(s.findByAuthType(AuthType.MiniProgram).first());
            final AuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MiniProgram);
            assertSame(entity, s.save(entity, null));
            assertEquals(entity, s.findByAuthType(AuthType.MiniProgram).first());
        });
    }

    @Test
    void deleteByAuthType() {
        useService(serviceClass, s -> {
            assertEquals(0, s.deleteByAuthType(AuthType.MiniProgram).getDeletedCount());
            final AuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MiniProgram);
            assertSame(entity, s.save(entity, null));
            assertEquals(1, s.deleteByAuthType(AuthType.MiniProgram).getDeletedCount());
            assertNull(s.findById(entity.getId()));
        });
    }

    @Test
    void authTypeValidationNotSet() {
        useService(serviceClass, s -> {
            final AuthTypeEntity entity = EntityFactory.newInstance(entityClass);
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
            final AuthTypeEntity entity = EntityFactory.newInstance(entityClass);
            entity.setSource(AuthType.MiniProgram);
            assertSame(entity, s.save(entity, null));

            final Validate validate = new DefaultValidate();
            assertNull(s.save(entity, validate));
            assertFalse(validate.hasErrors());

            entity.setSource(AuthType.UsernamePassword);
            assertNull(s.save(entity, validate));
            assertTrue(validate.hasErrors());
            assertTrue(validate.hasFieldErrors());
            assertEquals(Collections.singletonList("认证类型不可更改"), validate.getFieldErrors()
                    .get(AuthTypable.FIELD_NAME_AUTH_TYPE));
        });
    }
}