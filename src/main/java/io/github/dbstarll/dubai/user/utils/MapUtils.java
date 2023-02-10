package io.github.dbstarll.dubai.user.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

public final class MapUtils {
    private MapUtils() {
        // 工具类禁止实例化
    }

    /**
     * 按照map中的key来累加，增量为1.
     *
     * @param map map
     * @param key 累加的key
     * @param <T> key的类型
     * @return 累加后的值
     */
    public static <T> int accumulator(final Map<T, Integer> map, final T key) {
        return add(map, key, 1);
    }

    /**
     * 将additional中的所有项目，按照对应的key累加到map中.
     *
     * @param map        map
     * @param additional additional map
     * @param <T>        key的类型
     * @return 累加后的map
     */
    public static <T> Map<T, Integer> combiner(final Map<T, Integer> map, final Map<T, Integer> additional) {
        additional.forEach((key, count) -> add(map, key, count));
        return map;
    }

    /**
     * 按照map中的key来累加.
     *
     * @param map       map
     * @param key       累加的key
     * @param increment 本次累加的增量
     * @param <T>       key的类型
     * @return 累加后的值
     */
    public static <T> int add(final Map<T, Integer> map, final T key, final int increment) {
        return map.merge(key, increment, Integer::sum);
    }

    /**
     * 构建一个统计流中各项指标出现次数的Collector.
     *
     * @param <T> 指标的类型
     * @return Collector
     */
    public static <T> Collector<T, Map<T, Integer>, Map<T, Integer>> counter() {
        return Collector.of(HashMap::new, MapUtils::accumulator, MapUtils::combiner);
    }
}
