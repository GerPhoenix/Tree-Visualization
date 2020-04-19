import graphvisualizer.TreeVisualizer;

import java.awt.*;

public class Test {
    private static final int K = 2;
    private static final int AMOUNT_OF_KEYS = 1;
    private static final int AMOUNT_OF_NODES = 200;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 200;
    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config(K);
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, AMOUNT_OF_KEYS,AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        root.setColor(Color.green);
        visualizer.draw(root);
    }
}
