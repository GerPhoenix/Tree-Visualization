package graphvisualizer;

/**
 * Implements the {@link VisualizableNode} Interface which has to be implemented by tree nodes if you want to use the {@link TreeVisualizer}. <br><br>
 *
 * @implNote This interface helps writing nodes with only one key by adding a default implementation of {@link VisualizableOneKeyNode#getKeys() getKeys} relying on the {@link VisualizableOneKeyNode#getKey() getKey} function
 * @see TreeVisualizer
 */
public interface VisualizableOneKeyNode extends VisualizableNode {
    /**
     * @return the key of this node.
     */
    Object getKey();

    @Override
    default Object[] getKeys() {
        return new Object[]{getKey()};
    }
}
