package graphvisualizer;

import java.awt.*;

/**
 * Required implementation for tree nodes if you want to use the {@link TreeVisualizer}.
 * see {@link TreeVisualizer}
 */
public interface VisualizableNode {

    /**
     * If the nodes can only have one value consider implementing the {@link VisualizableOneKeyNode} interface instead
     *
     * @return the keys of this node.
     */
    Object[] getKeys();

    /**
     * @return children of this node
     */
    VisualizableNode[] getChildren();

    /**
     * Overwrite in subclass to easily manage which color a specific node should have if it is drawn with {@link TreeVisualizer}
     *
     * @return background color for the node when drawn with the {@link TreeVisualizer}. If null is returned the default color specified by the TreeVisualizer is used.
     */
    default Color getColor() {
        return null;
    }
}
