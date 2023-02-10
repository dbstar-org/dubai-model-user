package io.github.dbstarll.dubai.user.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class MapUtilsTest {
    private Map<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
        map = null;
    }

    @Test
    void add() {
        assertNull(map.get("abc"));
        assertEquals(1, MapUtils.add(map, "abc", 1));
        assertEquals(1, map.get("abc"));
        assertEquals(11, MapUtils.add(map, "abc", 10));
        assertEquals(11, map.get("abc"));
    }

    @Test
    void accumulator() {
        assertNull(map.get("abc"));
        assertEquals(1, MapUtils.accumulator(map, "abc"));
        assertEquals(1, map.get("abc"));
        assertEquals(2, MapUtils.accumulator(map, "abc"));
        assertEquals(2, map.get("abc"));
    }

    @Test
    void combiner() {
        assertNull(map.get("abc"));
        assertSame(map, MapUtils.combiner(map, new HashMap<>()));
        assertNull(map.get("abc"));
        assertSame(map, MapUtils.combiner(map, Collections.singletonMap("abc", 10)));
        assertEquals(10, map.get("abc"));
        assertSame(map, MapUtils.combiner(map, Collections.singletonMap("abc", 10)));
        assertEquals(20, map.get("abc"));
    }

    @Test
    void counter() {
        final Map<Character, Integer> counter = "accumulator".chars().mapToObj(c -> (char) c)
                .collect(MapUtils.counter());
        assertEquals(8, counter.size());
        assertEquals("[a=2, c=2, l=1, m=1, o=1, r=1, t=1, u=2]", counter.entrySet().stream()
                .sorted(Entry.comparingByKey()).collect(Collectors.toList()).toString());
    }
}