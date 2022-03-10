package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeastFrequentlyUsedCacheTest {

    private static final int CAPACITY_CACHE = 10;

    private StorageStub<Integer, String> storageTest = new StorageStub<>();

    private LeastFrequentlyUsedCache<Integer, String> leastFreqMock = new LeastFrequentlyUsedCache<>(storageTest, CAPACITY_CACHE);

    @Test
    void testValuesLeastFrequentlyUsed() throws ItemNotFound {
        final int inputCount = 20;
        Map<Integer, String> valueMap = new LinkedHashMap<>();
        for (int i = 0; i < inputCount; i++) {
            leastFreqMock.addToCache(i, ((Integer)i).toString());
            if (i % 2 == 0) {
                leastFreqMock.get(i);
                valueMap.put(i, ((Integer)(i)).toString());
            }
            if (i == inputCount-1){
                valueMap.remove(0);
                valueMap.put(i, ((Integer)i).toString());
            }
        }

        boolean mapsAreEqual = true;

        for(var i : leastFreqMock.values()) {
            if (!valueMap.containsValue(i)) {
                mapsAreEqual = false;
                break;
            }
        }

        assertTrue(mapsAreEqual);
    }

    @Test
    void testPutSameElements() {
        leastFreqMock.addToCache(1,"first");
        leastFreqMock.addToCache(1,"first");
        assertEquals(1, leastFreqMock.size());
    }

    @Test
    void testClear() {
        leastFreqMock.addToCache(1,"first");
        assertEquals(1, leastFreqMock.size());
        leastFreqMock.clear();
        assertEquals(0, leastFreqMock.size());
    }

    @Test
    void testGetFromCache() {
        assertNull(leastFreqMock.getFromCache(null));
    }

    @Test
    void testEvictionPractice() {
        leastFreqMock.addToCache(1, "one");
        leastFreqMock.addToCache(2, "one");
        leastFreqMock.addToCache(3, "one");
        leastFreqMock.addToCache(4, "one");
        leastFreqMock.addToCache(5, "one");

        leastFreqMock.addToCache(6, "one");
        leastFreqMock.addToCache(7, "one");
        leastFreqMock.addToCache(8, "one");
        leastFreqMock.addToCache(9, "one");
        leastFreqMock.addToCache(10, "one");

        leastFreqMock.addToCache(1, "one");
        leastFreqMock.addToCache(2, "one");
        leastFreqMock.addToCache(3, "one");
        leastFreqMock.addToCache(4, "one");
        leastFreqMock.addToCache(5, "one");

        leastFreqMock.addToCache(6, "one");
        leastFreqMock.addToCache(7, "one");
        leastFreqMock.addToCache(8, "one");
        leastFreqMock.addToCache(9, "one");
        leastFreqMock.addToCache(10, "one");

        leastFreqMock.addToCache(11, "2");
        leastFreqMock.addToCache(12, "2");
    }

    @Test
    void containsKey() {
    }

    @Test
    void evictFromCache() {
    }
}