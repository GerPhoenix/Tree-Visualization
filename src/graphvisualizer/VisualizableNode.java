package graphvisualizer;

import java.awt.*;

/**
 * Required implementation for tree nodes if you want to use the {@link TreeVisualizer}.
 * see {@link TreeVisualizer}
 */
public interface VisualizableNode {

    /**
     * @return the keys of this node.
     * If this node only has one key, use <code>return new Object[] {key}</code>
     */
    Object[] getKeys();

    /**
     * @return children of this node
     */
    VisualizableNode[] getChildren();

    /**
     * Overwrite in subclass to easily manage which color a specific node should have if it is drawn with {@link TreeVisualizer}
     *
     * @return background color for the node when drawn with the {@link TreeVisualizer}. If null is returned the default color is used.
     */
    default Color getColor() {
        return null;
    }
}
