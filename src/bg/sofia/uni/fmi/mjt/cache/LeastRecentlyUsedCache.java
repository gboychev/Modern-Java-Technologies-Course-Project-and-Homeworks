package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.storage.Storage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeastRecentlyUsedCache<K, V> extends CacheBase<K, V> {
    private final LinkedHashMap<K, V> cache;
    private static final float HASH_LOAD_FACTOR = 0.75f;

    public LeastRecentlyUsedCache(Storage<K, V> storage, int capacity) {
        super(storage, capacity);
        this.cache = new LinkedHashMap<>(capacity, HASH_LOAD_FACTOR, true);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void clear() {
        super.resetHitRate();
        cache.clear();
    }

    @Override
    public Collection<V> values() {
        Map<K, V> copy = Map.copyOf(cache);
        return copy.values();
    }

    protected V getFromCache(K k) {
        if (!cache.containsKey(k)) {
            return null;
        }
        return cache.get(k);
    }

    public V put(K k, V v) {
        cache.put(k, v);
        return cache.get(k);
    }

    protected boolean containsKey(K k) {
        return cache.containsKey(k);
    }

    protected void evictFromCache() {
        for (var i : cache.entrySet()) {
            cache.remove(i.getKey());
            break;
        }
    }
}
