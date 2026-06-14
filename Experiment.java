import edu.princeton.cs.algs4.StopwatchCPU;
import edu.princeton.cs.algs4.StdRandom;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Experiment {

    private static class Metrics {
        int purchase_total = 0, query_total = 0, lend_total = 0, receive_total = 0, dispose_total = 0;
        int query_successful = 0, query_failed = 0;
        int lend_successful = 0, lend_failed = 0;
        int receive_successful = 0, receive_failed = 0;
        int final_size = 0, final_height = 0;
        double elapsed_seconds = 0.0;
    }

    public static void main(String[] args) {
        int[] tValues = {12, 13, 14, 15, 16, 17, 18, 19};

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        for (int t : tValues) {
            int m = (int) Math.pow(2, t);
            runExperimentForSize(m);
        }
    }

    private static void runExperimentForSize(int m) {
        String fileName = "data/inventory_experiment_" + m + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            writer.println("instancia,estructura,m,purchase_total,query_total,lend_total,receive_total," +
                    "dispose_total,query_successful,query_failed,lend_successful,lend_failed," +
                    "receive_successful,receive_failed,final_size,final_height,elapsed_seconds");

            int keyUniverse = 4 * m;

            for (int i = 1; i <= 30; i++) {
                long seed = m + i;

                ArrayList<InventoryOperation> opsBST = DataGenerator.generateOperations(m, keyUniverse, seed);
                ArrayList<InventoryOperation> opsRB = DataGenerator.generateOperations(m, keyUniverse, seed);

                BSTInventoryIndex bstIndex = new BSTInventoryIndex();
                RedBlackBSTInventoryIndex rbIndex = new RedBlackBSTInventoryIndex();

                Metrics bstMetrics = runOperations(bstIndex, opsBST);
                writeMetrics(writer, i, "BST", m, bstMetrics);

                Metrics rbMetrics = runOperations(rbIndex, opsRB);
                writeMetrics(writer, i, "RedBlackBST", m, rbMetrics);

                if (!validate(bstIndex, rbIndex, m, keyUniverse)) {
                    System.err.println("Error de validacion en tamano " + m + ", instancia " + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Metrics runOperations(InventoryIndex index, ArrayList<InventoryOperation> operations) {
        Metrics metrics = new Metrics();
        StopwatchCPU timer = new StopwatchCPU();

        for (InventoryOperation op : operations) {
            OperationType type = op.getType();
            int key = op.getKey();
            int qty = op.getQuantity();

            if (type == OperationType.PURCHASE) {
                metrics.purchase_total++;
                InventoryItem item = index.get(key);
                if (item == null) {
                    index.put(key, op.getItem());
                } else {
                    item.addStock(key, qty);
                    index.put(key, item);
                }
            }
            else if (type == OperationType.QUERY) {
                metrics.query_total++;
                InventoryItem item = index.get(key);
                if (item != null) {
                    metrics.query_successful++;
                } else {
                    metrics.query_failed++;
                }
            }
            else if (type == OperationType.LEND) {
                metrics.lend_total++;
                InventoryItem item = index.get(key);
                if (item != null && item.lend(key, qty)) {
                    metrics.lend_successful++;
                } else {
                    metrics.lend_failed++;
                }
            }
            else if (type == OperationType.RECEIVE) {
                metrics.receive_total++;
                InventoryItem item = index.get(key);
                if (item != null && item.receive(key, qty)) {
                    metrics.receive_successful++;
                } else {
                    metrics.receive_failed++;
                }
            }
            else if (type == OperationType.DISPOSE) {
                metrics.dispose_total++;
                index.delete(key);
            }
        }

        metrics.elapsed_seconds = timer.elapsedTime();
        metrics.final_size = index.size();
        metrics.final_height = index.height();
        return metrics;
    }

    private static void writeMetrics(PrintWriter writer, int instance, String structure, int m, Metrics met) {
        writer.printf("%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%.6f\n",
                instance, structure, m,
                met.purchase_total, met.query_total, met.lend_total, met.receive_total, met.dispose_total,
                met.query_successful, met.query_failed, met.lend_successful, met.lend_failed,
                met.receive_successful, met.receive_failed, met.final_size, met.final_height, met.elapsed_seconds);
    }

    private static boolean validate(InventoryIndex bst, InventoryIndex rb, int m, int keyUniverse) {
        if (bst.size() != rb.size()) return false;

        for (int i = 0; i < 100; i++) {
            int randomKey = StdRandom.uniform(1, keyUniverse + 1);
            InventoryItem itemBst = bst.get(randomKey);
            InventoryItem itemRb = rb.get(randomKey);

            if ((itemBst == null && itemRb != null) || (itemBst != null && itemRb == null)) return false;
            if (itemBst != null && itemRb != null && itemBst.getId() != itemRb.getId()) return false;
        }

        for (Integer key : bst.keys()) {
            InventoryItem item = bst.get(key);
            if (item.getStockAvailable() < 0 || item.getStockOnLoan() < 0 || item.getStockTotal() < 0) return false;
            if ((item.getStockAvailable() + item.getStockOnLoan()) != item.getStockTotal()) return false;
        }

        return true;
    }
}