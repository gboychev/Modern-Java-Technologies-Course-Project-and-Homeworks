package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.storage.Storage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StorageStub<K,V> implements Storage<K,V> {

    Map<K,V> storage;

    public StorageStub(){
        storage = new HashMap<K,V>();
    }

    @Override
    public V store(K key, V value) {
        return storage.put(key, value);
    }

    @Override
    public V retrieve(K key) {
        return storage.get(key);
    }

    @Override
    public V remove(K key) {
        return storage.remove(key);
    }
}
