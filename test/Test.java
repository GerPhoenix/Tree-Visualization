import graphvisualizer.TreeVisualizer;

import java.awt.*;

public class Test {
    private static final int K = 2;

    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config(K);
        config.useTreeLayout = true;
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, 10, 1000, 1001);
        root.setColor(Color.green);
        visualizer.draw(root);
    }
}
