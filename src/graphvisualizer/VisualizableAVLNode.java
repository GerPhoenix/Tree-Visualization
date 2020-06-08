package graphvisualizer;

/**
 * Implements the {@link VisualizableNode} Interface which has to be implemented by tree nodes if you want to use the {@link TreeVisualizer}. <br><br>
 *
 * @implNote This interface should be used for AVL Tree nodes. Nodes will be drawn with their key left and their balance right.
 * @see TreeVisualizer
 */
public interface VisualizableAVLNode extends VisualizableOneKeyNode {
    @Override
    default Object[] getKeys() {
        return new Object[]{getKey(), getBalance()};
    }

    int getBalance();
}
