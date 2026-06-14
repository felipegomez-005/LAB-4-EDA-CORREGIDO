import java.util.Iterable;

public interface InventoryIndex {
    void put(Integer key, InventoryItem value);
    InventoryItem get(Integer key);
    void delete(Integer key);
    boolean contains(Integer key);
    Iterable<Integer> keys();
    int size();
    int height();
}