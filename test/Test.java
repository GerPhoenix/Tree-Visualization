import graphvisualizer.TreeVisualizer;

import java.awt.*;

public class Test {
    private static final int K = 5;

    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config(K);
        config.useTreeLayout=true;
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, 20, 0, 1000);
        root.setColor(Color.green);
        visualizer.draw(root);

    }
}
