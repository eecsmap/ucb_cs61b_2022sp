package map;

public interface SimpleMap<K extends Comparable<K>, V> {

    /**
     * Inserts the key-value mapping represented by key and value to the map.
     * If the map already contains the key, then the corresponding value
     * will be updated.
     */
    void put(K key, V value);

    /**
     * Returns the value associated with the given key or null if the key does
     * not exist.
     *
     */
    V get(K key);

    /**
     * Removes all key-value pair mappings from the given map.
     */
    void clear();
}
