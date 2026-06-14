import edu.princeton.cs.algs4.RedBlackBST;

public class RedBlackBSTInventoryIndex implements InventoryIndex {

    private RedBlackBST<Integer, InventoryItem> st;

    public RedBlackBSTInventoryIndex() {
        this.st = new RedBlackBST<>();
    }

    @Override
    public void put(Integer key, InventoryItem value) {
        st.put(key, value);
    }

    @Override
    public InventoryItem get(Integer key) {
        return st.get(key);
    }

    @Override
    public void delete(Integer key) {
        st.delete(key);
    }

    @Override
    public boolean contains(Integer key) {
        return st.contains(key);
    }

    @Override
    public Iterable<Integer> keys() {
        return st.keys();
    }

    @Override
    public int size() {
        return st.size();
    }

    @Override
    public int height() {
        return st.height();
    }
}