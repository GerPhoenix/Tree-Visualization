import graphvisualizer.TreeVisualizer;

import java.awt.*;

public class Test {
    private static final int K = 30;

    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config(K);
        config.useTreeLayout=true;
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, 200, 0, 1000);
        root.setColor(Color.green);
        visualizer.draw(root);

    }
}
