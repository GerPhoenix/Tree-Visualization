import graphvisualizer.TreeVisualizer;
import graphvisualizer.TreeVisualizer.TreeLayout;

import java.awt.*;

public class Test {
    private static final int K = 2;
    private static final TreeLayout LAYOUT = TreeLayout.TREE_INVERTED;
    private static final int NODE_SIZE = 50;
    private static final int AMOUNT_OF_KEYS = 1;
    private static final int AMOUNT_OF_NODES = 15;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 200;
    
    public static void main(String[] args) {
    	/**
    	 * Wenn überall die default Werte verwendet werden sollen, dann Konstruktor ohne Config verwenden und lediglich Wert für k übergeben
    	 * 
    	 */
    	
        //TreeVisualizer visualizer = new TreeVisualizer(K);
        //KNode root = new KNode(K, AMOUNT_OF_KEYS,AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        //root.setColor(Color.green);
        //visualizer.draw(root);
    	
    	
    	
    	/**
    	 * Wenn ein definiertes Layout verwendet werden soll, dann eine eigene Config verwenden und über deren Konstruktor übergeben
    	 * 
    	 */
    	
    	//TreeVisualizer.Config config = new TreeVisualizer.Config(K, LAYOUT);
        //TreeVisualizer visualizer = new TreeVisualizer(config);
        //KNode root = new KNode(K, AMOUNT_OF_KEYS,AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        //root.setColor(Color.green);
        //visualizer.draw(root);
    	
    	
    	
    	/**
    	 * Wenn eine definierte Knotengröße verwendet werden soll, dann eine eigene Config verwenden und über deren Konstruktor übergeben
    	 * 
    	 */
    	
    	TreeVisualizer.Config config = new TreeVisualizer.Config(K, NODE_SIZE);
        TreeVisualizer visualizer = new TreeVisualizer(config);
        KNode root = new KNode(K, AMOUNT_OF_KEYS,AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        root.setColor(Color.green);
        visualizer.draw(root);
        
        
        
    	/**
    	 * Wenn eine definierte Knotengröße und ein definiertes Layout verwendet werden soll, dann eine eigene Config verwenden und über deren Konstruktor übergeben
    	 * 
    	 */
    	
    	//TreeVisualizer.Config config = new TreeVisualizer.Config(K, LAYOUT, NODE_SIZE);
        //TreeVisualizer visualizer = new TreeVisualizer(config);
        //KNode root = new KNode(K, AMOUNT_OF_KEYS,AMOUNT_OF_NODES, MIN_VALUE, MAX_VALUE);
        //root.setColor(Color.green);
        //visualizer.draw(root);
    }
}
