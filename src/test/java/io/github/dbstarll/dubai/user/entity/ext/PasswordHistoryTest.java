package io.github.dbstarll.dubai.user.entity.ext;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class PasswordHistoryTest {
    @Test
    void getPassword() {
        final PasswordHistory history = new PasswordHistory("password", new Date());
        assertEquals("password", history.getPassword());
    }

    @Test
    void getDate() {
        final Date date = new Date();
        final PasswordHistory history = new PasswordHistory("password", date);
        assertEquals(date, history.getDate());
    }

    @Test
    void compareTo() {
        final PasswordHistory history = new PasswordHistory("password", new Date());
        assertEquals(0, history.compareTo(new PasswordHistory(history.getPassword(), history.getDate())));
    }

    @Test
    void testEquals() {
        final PasswordHistory history = new PasswordHistory("password", new Date());
        assertEquals(history, history);
        assertNotEquals(Collections.singletonList(history), Collections.singletonList(null));
        assertNotEquals(Collections.singletonList(history), Collections.singletonList("null"));
        assertEquals(history, new PasswordHistory("password", history.getDate()));
    }

    @Test
    void testHashCode() {
        final PasswordHistory history = new PasswordHistory("password", new Date());
        assertNotEquals(0, history.hashCode());
    }

    @Test
    void parse() {
        final PasswordHistory history = new PasswordHistory("password", new Date());
        assertSame(history, PasswordHistory.parse(history));
        final Document document = new Document();
        document.put(PasswordHistory.FIELD_PASSWORD, "password");
        document.put(PasswordHistory.FIELD_DATE, history.getDate());
        assertEquals(history, PasswordHistory.parse(document));

        final Exception e = assertThrowsExactly(UnsupportedOperationException.class, () -> PasswordHistory.parse("string"));
        assertEquals("java.lang.String", e.getMessage());
    }
}