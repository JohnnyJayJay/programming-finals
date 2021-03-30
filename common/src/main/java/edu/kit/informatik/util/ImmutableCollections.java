package edu.kit.informatik.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class to work with immutable collections.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ImmutableCollections {

    private ImmutableCollections() {

    }

    /**
     * Takes two {@link List}s of any kind and returns a new, immutable List that contains all elements
     * of the first and all elements of the second list, in that order.
     *
     * @param a   The first List.
     * @param b   The second List.
     * @param <T> The element type of the two lists.
     * @return A new list representing {@code a} concatenated with {@code b}.
     */
    public static <T> List<T> concat(List<T> a, List<T> b) {
        List<T> newList = new ArrayList<>(a.size() + b.size());
        newList.addAll(a);
        newList.addAll(b);
        return List.copyOf(newList);
    }

    /**
     * Takes two {@link Set}s of any kind and returns a new, immutable Set that contains all elements
     * of the first set and all elements of the second set. I.e., the returned set is the union of
     * the two provided sets.
     *
     * @param a   The first set.
     * @param b   The second set.
     * @param <T> The element type of the two sets.
     * @return A new set representing the union of {@code a } and {@code b}.
     */
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> newSet = new HashSet<>(a.size() + b.size());
        newSet.addAll(a);
        newSet.addAll(b);
        return Set.copyOf(newSet);
    }

    /**
     * Takes two {@link Map}s of any kind and returns a new, immutable Map that contains the
     * entries of the first and second map. If both maps contain the same key, the entry in
     * the second map will be favoured over the one in the first map.
     *
     * @param a   The first map.
     * @param b   The second map.
     * @param <K> the key type of the two maps.
     * @param <V> the value type of the two maps.
     * @return A new map representing {@code a} and {@code b}, merged together.
     */
    public static <K, V> Map<K, V> merge(Map<K, V> a, Map<K, V> b) {
        Map<K, V> newMap = new HashMap<>(a.size() + b.size());
        newMap.putAll(a);
        newMap.putAll(b);
        return Map.copyOf(newMap);
    }

}
