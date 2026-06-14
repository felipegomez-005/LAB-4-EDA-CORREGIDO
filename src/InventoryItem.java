public class InventoryItem {
    private int id;
    private String name;
    private String category;
    private String location;
    private int stockTotal;
    private int stockAvailable;
    private int stockOnLoan;

    public InventoryItem(int id, String name, String category, String location,
                         int stockTotal, int stockAvailable, int stockOnLoan) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.stockTotal = stockTotal;
        this.stockAvailable = stockAvailable;
        this.stockOnLoan = stockOnLoan;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public int getStockTotal() { return stockTotal; }
    public int getStockAvailable() { return stockAvailable; }
    public int getStockOnLoan() { return stockOnLoan; }

    public void addStock(int itemId, int quantity) {
        if (this.id == itemId && quantity > 0) {
            this.stockTotal += quantity;
            this.stockAvailable += quantity;
        }
    }

    public boolean lend(int itemId, int quantity) {
        if (this.id == itemId && quantity > 0 && this.stockAvailable >= quantity) {
            this.stockAvailable -= quantity;
            this.stockOnLoan += quantity;
            return true;
        }
        return false;
    }

    public boolean receive(int itemId, int quantity) {
        if (this.id == itemId && quantity > 0 && this.stockOnLoan >= quantity) {
            this.stockAvailable += quantity;
            this.stockOnLoan -= quantity;
            return true;
        }
        return false;
    }
}