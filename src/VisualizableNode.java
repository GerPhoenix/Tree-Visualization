import java.awt.*;

/**
 * Required implementation for tree nodes if using {@link TreeVisualizer}
 */
public interface VisualizableNode<E> {
    /**
     * @return the key of this node
     */
    E getKey();

    /**
     * @return children of this node
     */
    VisualizableNode<E>[] getChildren();

    /**
     * @return height of the tree or sub-tree
     */
    int height();

    /**
     * Overwrite in subclass to easily manage which color a specific node should have if it is drawn with {@link TreeVisualizer}
     *
     * @return background color for the node when drawn with the {@link TreeVisualizer}. Overrides {@link TreeVisualizer#DEFAULT_NODE_COLOR DEFAULT_NODE_COLOR}
     */
    default Color getColor() {
        return null;
    }
}
