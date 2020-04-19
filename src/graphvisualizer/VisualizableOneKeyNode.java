package graphvisualizer;

/**
 * Implements the VisualizableNode Interface which has to be implemented by tree nodes if you want to use the {@link TreeVisualizer}.
 * <br>
 * <br>
 * This interface helps writing nodes with only one key by adding a default implementation of {@link VisualizableOneKeyNode#getKeys() getKeys} relying on a add {@link VisualizableOneKeyNode#getKey() getKey} function
 * see {@link VisualizableNode}
 */
public interface VisualizableOneKeyNode extends VisualizableNode {
    Object getKey();

    @Override
    default Object[] getKeys() {
        return new Object[]{getKey()};
    }
}
