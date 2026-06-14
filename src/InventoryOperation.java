public class InventoryOperation {
    private final OperationType type;
    private final int key;
    private final int quantity;
    private final InventoryItem item;

    public InventoryOperation(OperationType type, int key, int quantity, InventoryItem item) {
        this.type = type;
        this.key = key;
        this.quantity = quantity;
        this.item = item;
    }

    public OperationType getType() { return type; }
    public int getKey() { return key; }
    public int getQuantity() { return quantity; }
    public InventoryItem getItem() { return item; }
}