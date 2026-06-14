import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataGenerator {

    private static final String[] CATEGORIES = {
            "Sensor", "Motor", "Microcontrolador", "Cable",
            "Bateria", "Herramienta", "Modulo", "Kit"
    };

    private static final String[] LOCATIONS = {
            "Estante_A", "Estante_B", "Caja_1",
            "Caja_2", "Laboratorio", "Bodega"
    };

    public static InventoryItem generateItem(int id) {
        String name = "Componente " + id;
        int catIndex = StdRandom.uniform(CATEGORIES.length);
        String category = CATEGORIES[catIndex];
        int locIndex = StdRandom.uniform(LOCATIONS.length);
        String location = LOCATIONS[locIndex];
        int stockTotal = StdRandom.uniform(1, 21);
        int stockAvailable = stockTotal;
        int stockOnLoan = 0;

        return new InventoryItem(id, name, category, location, stockTotal, stockAvailable, stockOnLoan);
    }

    public static ArrayList<InventoryOperation> generateOperations(int m, int keyUniverse, long seed) {
        StdRandom.setSeed(seed);
        ArrayList<InventoryOperation> operations = new ArrayList<>();
        Set<Integer> activeItems = new HashSet<>();

        for (int i = 0; i < m; i++) {
            double p = StdRandom.uniform();
            OperationType type;
            int quantity = 0;

            if (p < 0.35) {
                type = OperationType.PURCHASE;
            } else if (p < 0.65) {
                type = OperationType.QUERY;
            } else if (p < 0.80) {
                type = OperationType.LEND;
            } else if (p < 0.90) {
                type = OperationType.RECEIVE;
            } else {
                type = OperationType.DISPOSE;
            }

            int key = StdRandom.uniform(1, keyUniverse + 1);

            if (type == OperationType.PURCHASE || type == OperationType.LEND || type == OperationType.RECEIVE) {
                quantity = StdRandom.uniform(1, 6);
            }

            InventoryItem item = null;

            if (type == OperationType.PURCHASE) {
                if (!activeItems.contains(key)) {
                    item = generateItem(key);
                    activeItems.add(key);
                }
            } else if (type == OperationType.DISPOSE) {
                activeItems.remove(key);
            }

            operations.add(new InventoryOperation(type, key, quantity, item));
        }

        return operations;
    }
}