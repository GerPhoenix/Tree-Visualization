import graphvisualizer.VisualizableNode;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * unsorted k tree. Is not following the degree rule. This is just meant to be used to quickly generate trees of random size.
 */
public class KNode implements VisualizableNode {
    List<KNode> children = new LinkedList<>();
    Integer key;
    Color color;

    @Override
    public Integer[] getKeys() {
        return new Integer[]{key};
    }

    @Override
    public VisualizableNode[] getChildren() {
        KNode[] array = new KNode[children.size()];
        return children.toArray(array);
    }

    /**
     * Creates an unsorted tree. Is not following the degree rule. This is just meant to be used to quickly generate Trees of random size.
     *
     * @param k                    dge+
     * @param amountOfRandomValues amount of random values that should be inserted in the tree
     * @param minValue             Minimum value of the random values
     * @param maxValue             Maximum values of the ranom values
     * @param values               Fixed values that should be inserted in the tree
     */
    KNode(int k, int amountOfRandomValues, int minValue, int maxValue, Integer... values) {
        if (minValue > maxValue)
            throw new IllegalArgumentException(minValue + " > " + maxValue);
        List<Integer> valueList = Arrays.stream(values).collect(Collectors.toList());
        Random random = new Random();
        for (int i = 0; i < amountOfRandomValues; i++) {
            valueList.add(random.nextInt(minValue + maxValue) - minValue);
        }
        values = valueList.toArray(new Integer[0]);
        this.key = values[0];
        values = Arrays.copyOfRange(values, 1, values.length);
        int rangeLength = values.length / k;
        int rest = values.length % k;
        for (int i = 0; i < k; i++) {
            if (i < values.length) {
                if (i < rest) {
                    Integer[] newValues = Arrays.copyOfRange(values, rangeLength * i + i, rangeLength * (i + 1) + i + 1);
                    if (newValues.length != 0)
                        children.add(new KNode(k, 0, 0, 0, newValues));
                } else {
                    Integer[] newValues = Arrays.copyOfRange(values, rangeLength * i + rest, rangeLength * (i + 1) + rest);
                    if (newValues.length != 0)
                        children.add(new KNode(k, 0, 0, 0, newValues));
                }
            }
        }
        this.color = null;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
