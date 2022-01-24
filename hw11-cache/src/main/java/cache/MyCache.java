package cache;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listenersAction(key, value, "put");
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        listenersAction(key, null, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        listenersAction(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void listenersAction(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }
}
