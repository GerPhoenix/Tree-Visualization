import graphvisualizer.TreeVisualizer;
import graphvisualizer.TreeVisualizer.TreeLayout;

import java.awt.*;

public class Test {
    private static final int K = 5;
    private static final TreeLayout LAYOUT = TreeLayout.TREE;
    private static final int AMOUNT_OF_KEYS = 5;
    private static final int AMOUNT_OF_NODES = 150;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 200;
    private static final boolean AUTOMATIC_NODE_SCALE = true;
    private static final int NODE_SIZE = 50;

    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config();
        config.layout = LAYOUT;
        config.nodeSize = NODE_SIZE;
        config.automaticNodeScalingMode = AUTOMATIC_NODE_SCALE;
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, AMOUNT_OF_KEYS, AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        root.setColor(Color.green);
        visualizer.draw(root);
    }
}
