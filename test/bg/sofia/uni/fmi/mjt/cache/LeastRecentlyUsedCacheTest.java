package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.CollectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class LeastRecentlyUsedCacheTest {

    private static final int STORAGE_TEST_CAPACITY = 15;

    private StorageStub<Integer, String> storageTest = new StorageStub<>();

    @InjectMocks
    private LeastRecentlyUsedCache<Integer, String> leastRecentTest = new LeastRecentlyUsedCache<Integer, String>(storageTest, STORAGE_TEST_CAPACITY);

    @Test
    void testReturnSizeZeroElement() {
        assertEquals(0, leastRecentTest.size(), "Put no element in LeastRecentlyUsedCache, expected size 0, but the result was different.");
    }

    @Test
    void testReturnSizeOneElement() {
        leastRecentTest.addToCache(1, "first");
        assertEquals(1, leastRecentTest.size(), "Put one element in LeastRecentlyUsedCache, expected size 1, but the result was different.");
    }

    @Test
    void testReturnSizeTenElements() {
        final int testNum = 10;
        for (int i = 0; i < testNum; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
        }
        assertEquals(10, leastRecentTest.size(), "Put ten elements in LeastRecentlyUsedCache, expected size ot be 10, but the result was different.");
    }

    @Test
    void testPut100Elements() {
        final int moreThanCapacity = STORAGE_TEST_CAPACITY + 100;

        for (int i = 0; i < moreThanCapacity; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
        }
    }

    @Test
    void testGetExistingElement() throws ItemNotFound {
        leastRecentTest.addToCache(1, "testPut");
        assertEquals("testPut", leastRecentTest.get(1));
    }

    @Test
    void testGetNonexistentElement() {
        try {
            leastRecentTest.get(2);
            fail("This should have failed, but did not");
        }
        catch (ItemNotFound exc) {
            //expected
        }
    }

    @Test
    void testGetNullElement() throws ItemNotFound{
        try {
            leastRecentTest.get(null);
        }
        catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    void testValuesLeastRecentlyUsed25InputValuesWith15Limit() {
        Map valueSet = new LinkedHashMap();
        final int testNum = 25;
        for (int i = 0; i < testNum; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
            if (i >= testNum - STORAGE_TEST_CAPACITY) {
                valueSet.put(i, ((Integer) i).toString());
            }
        }

        boolean valuesAreEqual = true;
        for (var i : leastRecentTest.values()) {
            if (!valueSet.values().contains(i)) {
                valuesAreEqual = false;
            }
        }
        //Why can't I assertEquals valueSet.values() and leastRecentTest.values()?
        assertTrue(valuesAreEqual);
    }

    @Test
    void testGetFromStorage() throws ItemNotFound {
        final int testNum = 25;
        for (int i = 0; i < testNum; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
        }
        assertEquals("1", leastRecentTest.get(1));
    }

    @Test
    void testClearAfterPutting10Elements() {
        final int testNum = 10;
        for (int i = 0; i < testNum; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
        }
        leastRecentTest.clear();
        assertEquals(0, leastRecentTest.size(), "Cleared, should be 0 elements, but is not");
    }

    @Test
    void testGetFromCache() {
        leastRecentTest.addToCache(1, "first");
        assertEquals("first", leastRecentTest.getFromCache(1));
    }

    @Test
    void testContainsKey() {
        leastRecentTest.addToCache(1, "first");
        assertTrue(leastRecentTest.containsKey(1));
    }

    @Test
    void testPutInMultipleOfTheSameElementAndGetHitRate(){
        leastRecentTest.addToCache(1, "1");
        leastRecentTest.addToCache(1, "1");
        assertEquals(0.5, leastRecentTest.getHitRate());
    }

    @Test
    void testContainsKeyAfterItShouldHaveBeenDeletedFromCache() {
        final int moreThanCapacity = STORAGE_TEST_CAPACITY + 100;
        for (int i = 0; i < moreThanCapacity; i++) {
            leastRecentTest.addToCache(i, ((Integer) i).toString());
        }
        assertFalse(leastRecentTest.containsKey(1));
    }

    @Test
    void testGetHitRate0() {
        leastRecentTest.addToCache(1, "first");
        assertEquals(0,leastRecentTest.getHitRate());
    }

    @Test
    void testGetHitRateFiftyPercent() throws ItemNotFound {
        leastRecentTest.addToCache(1, "first");
        leastRecentTest.get(1);
        assertEquals(0.5,leastRecentTest.getHitRate());
    }

}