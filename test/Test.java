import graphvisualizer.TreeVisualizer;

import java.awt.*;

public class Test {
    private static final int K = 2;

    public static void main(String[] args) {
        TreeVisualizer visualizer = new TreeVisualizer(K);
        KNode root = new KNode(K, 10, 1000, 2000);
        visualizer.draw(root);
    }
}
