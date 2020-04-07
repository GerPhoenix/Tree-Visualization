import java.awt.*;

public class Test {
    private static final int K = 2;

    public static void main(String[] args) {
        TreeVisualizer.Config config = new TreeVisualizer.Config(K);
        config.useTreeLayout=true;
        TreeVisualizer<Integer> visualizer = new TreeVisualizer<>(config);
        KNode root = new KNode(K, 20, 0, 1000);
        root.setColor(Color.green);
        visualizer.visualize(root);

    }
}
