package graphvisualizer;

import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.layout.LayoutRunner;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Used to visualize tree structures implementing {@link VisualizableNode} interface for their Nodes.
 * <p>
 * How to use:<br>
 * <code>new {@link #TreeVisualizer(int)}.{@link #draw(VisualizableNode)};</code><br>
 * There are also other {@link #TreeVisualizer(int, boolean, int, int, Color, Color) constructors} available to do more advanced things.
 *
 * @see <a href="http://graphstream-project.org/" Uses Graphstream gs-core and gs-ui for visualization of the tree graph</a>
 */
public class TreeVisualizer {

    public final static int DEFAULT_TEXT_SIZE = 12;
    public final static int DEFAULT_NODE_SIZE = 35;
    public final static boolean DEFAULT_USE_TREE_LAYOUT = true;
    public final static Color DEFAULT_NODE_COLOR = Color.white;
    public final static Color DEFAULT_MARK_COLOR = Color.white;

    private final static double X_SCALE = 50;
    private final static double Y_SCALE = 120;


    private GraphicGraph graph;
    private Viewer viewer;
    private CssGenerator generalStyle;

    private CssGenerator markedStyle;

    private int k;
    private boolean useTreeLayout;

    /**
     * Calls {@link TreeVisualizer#TreeVisualizer(int, boolean, int, int, Color, Color)} with provided k and the {@link Config default values}
     *
     * @param k deg+
     * @see Config default values
     */
    public TreeVisualizer(int k) {
        this(k, DEFAULT_USE_TREE_LAYOUT, DEFAULT_TEXT_SIZE, DEFAULT_NODE_SIZE, DEFAULT_NODE_COLOR, DEFAULT_MARK_COLOR);
    }

    /**
     * Calls {@link TreeVisualizer#TreeVisualizer(int, boolean, int, int, Color, Color)} with using arguments provided in the {@link Config} parameter
     *
     * @param config {@link Config} initialize TreeVisualizer with provided Attributes.
     * @see Config default values
     */
    public TreeVisualizer(Config config) {
        this(config.k, config.useTreeLayout, config.textSize, config.nodeSize, config.color, config.mark);
    }


    /**
     * Creates a new TreeVisualizer with provided settings. All settings can be changes afterwards using their setters.
     *
     * @param k             deg+
     * @param useTreeLayout if the Tree View shall be applied if the tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     *                      Else use GraphStream <a href="http://graphstream-project.org/doc/Tutorials/Graph-Visualisation/1.0/#automatic-layout">automatic layout</a>.
     * @param textSize      size of value letters if tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param nodeSize      size of the node if tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param color         color of the nodes if tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param mark          color of the nodes if tree is {@link TreeVisualizer#draw(VisualizableNode) displayed} and the node is marked (left click on node).
     * @see Config default values
     */
    public TreeVisualizer(int k, boolean useTreeLayout, int textSize, int nodeSize, Color color, Color mark) {
        // INITIALIZE ATTRIBUTES
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        this.k = k;
        this.useTreeLayout = useTreeLayout;
        graph = new GraphicGraph("Tree");
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        // BUILD CSS
        generalStyle = new CssGenerator("node");
        generalStyle.set("size", nodeSize + "px, " + nodeSize + "px");
        generalStyle.set("fill-mode", "plain");
        generalStyle.set("stroke-mode", "plain");
        generalStyle.set("stroke-color", "black");
        generalStyle.set("stroke-width", "1");
        generalStyle.set("text-size", String.valueOf(textSize));
        generalStyle.set("fill-color", CssGenerator.rgbString(color));

        markedStyle = new CssGenerator("node", ".", "marked");
        markedStyle.set("fill-color", CssGenerator.rgbString(mark));
        markedStyle.set("text-style", "bold");
        markedStyle.set("stroke-width", "2");
        markedStyle.set("z-index", "1000");

        // SETUP GRAPH
        graphSetup();

        // SETUP MOUSE LISTENERS
        ViewPanel view_panel = viewer.addDefaultView(true);
        // remove GraphStream default MouseListener
        view_panel.removeMouseListener(view_panel.getMouseListeners()[0]);
        // Assign our own Mouse Listener which is making some changes to the GraphStream default MouseListener
        TreeMouseManager mouseManager = new TreeMouseManager();
        mouseManager.init(graph, viewer.getDefaultView());
        view_panel.addMouseMotionListener(mouseManager);
        view_panel.addMouseListener(mouseManager);
        //add a mouse wheel listener to the ViewPanel for zooming the graph
        view_panel.addMouseWheelListener(e -> TreeVisualizer.zoomGraphMouseWheelMoved(e, viewer.getDefaultView()));
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getTextSize() {
        return Integer.parseInt(generalStyle.get("text-size"));
    }

    public void setTextSize(int textSize) {
        generalStyle.set("text-size", String.valueOf(textSize));
    }

    public int getNodeSize() {
        String size = generalStyle.get("size");
        return Integer.parseInt(size.split("p")[0]);
    }

    public void setNodeSize(int nodeSize) {
        generalStyle.set("size", nodeSize + "px, " + nodeSize + "px");
    }

    public boolean isAutoLayout() {
        return useTreeLayout;
    }

    public void enableTreeLayout(boolean bol) {
        this.useTreeLayout = bol;
    }

    public String getNodeColor() {
        return generalStyle.get("fill-color");
    }

    public void setNodeColor(Color color) {
        generalStyle.set("fill-color", CssGenerator.rgbString(color));
    }


    public String getMarkColor() {
        return markedStyle.get("fill-color");
    }

    public void setMarkColor(Color color) {
        markedStyle.set("fill-color", CssGenerator.rgbString(color));
    }

    /**
     * @param factor scale for text size and node size
     * @see #scale(float, float)
     */
    public void scale(float factor) {
        scale(factor, factor);
    }

    /**
     * Scales text size and node size
     *
     * @param textScale text scale
     * @param nodeScale node scale
     * @see #DEFAULT_TEXT_SIZE
     * @see #DEFAULT_NODE_SIZE
     */
    public void scale(float textScale, float nodeScale) {
        setTextSize((int) (getTextSize() * textScale));
        setNodeSize((int) (getNodeSize() * nodeScale));

    }


    /**
     * reset the graph and perform a default setup
     */
    private void reset() {
        graph.clear();
        graphSetup();
    }

    private void graphSetup() {
        // disable Logger so warnings that seems to have no effect won't be shown#
        Logger.getLogger(GraphicGraph.class.getSimpleName()).setLevel(Level.OFF);
        Logger.getLogger(LayoutRunner.class.getSimpleName()).setLevel(Level.OFF);
        if (useTreeLayout)
            viewer.disableAutoLayout();
        else
            viewer.enableAutoLayout();
        graph.setAttribute("ui.stylesheet", generalStyle.toString() + markedStyle.toString());
    }

    /**
     * draws a new tree growing from the provided root Node
     *
     * @param root of a tree or subtree
     */
    public void draw(VisualizableNode root) {
        // clear graph
        reset();
        // draw root, using it's hashcode as id
        Node graphRoot = graph.addNode(String.valueOf(root.hashCode()));
        // Stringify key so it can be displayed
        String rootKeyString = getKey(root);
        configureNode(graphRoot, rootKeyString, root.getColor());
        if (useTreeLayout)
            graphRoot.setAttribute("xyz", 0.0, 0.0, 0.0);
        // traverse tree recursive drawing all nodes
        addNodesRecursive(root, 1, height(root));
    }

    private String getKey(VisualizableNode node) {
        return Arrays.stream(node.getKeys())
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
    }

    /**
     * draws children of the provided parent node and edges between the parentNode and the children.
     * Traverse the tree recursive drawing all nodes.
     *
     * @param parentNode   the current node whose children should be visualized
     * @param currentDepth current depth in the complete tree
     * @param maxDepth     maximum depth of the complete tree
     */
    private void addNodesRecursive(VisualizableNode parentNode, int currentDepth, int maxDepth) {
        Node graphNode = graph.getNode(String.valueOf(parentNode.hashCode()));
        Object[] parentXYZ = graphNode.getAttribute("xyz");
        VisualizableNode[] children = parentNode.getChildren();
        //draws all children and edges to them. Recursively traverse children subtrees
        for (int i = 0; i < children.length; i++) {
            VisualizableNode child = children[i];
            // draw child, using it's hashcode as id
            Node childGraphNode = graph.addNode(String.valueOf(child.hashCode()));
            if (useTreeLayout) {
                // calculate custom node position
                double x = calculateNodeXPosition((double) parentXYZ[0], i, children.length, currentDepth, maxDepth);
                double y = (double) parentXYZ[1] + maxDepth * Y_SCALE;
                childGraphNode.setAttribute("xyz", x, y, 0);
            }
            drawEdge(graphNode, childGraphNode);
            // Stringify key so it can be displayed
            String childKeyString = getKey(child);
            configureNode(childGraphNode, childKeyString, child.getColor());
            // traverse child subtree
            addNodesRecursive(child, currentDepth + 1, maxDepth);
        }


    }

    /**
     * calculates the x position for a child at a specific index
     * Note: calculation for the distance was decided by trial and error for the most part
     *
     * @param parentX          parent node x position
     * @param AmountOfChildren total amount of children of the parent
     * @param childIndex       index of the child in parents children list
     * @param currentDepth     current depth in the complete tree
     * @param maxDepth         total depth the complete tree will reach
     * @return child node x position
     */
    private double calculateNodeXPosition(double parentX, double childIndex, int AmountOfChildren,
                                          int currentDepth, int maxDepth) {
        if (AmountOfChildren == 1)
            return parentX;
        double distanceDecay = Math.pow(k, currentDepth);
        double distance = X_SCALE * k * k * maxDepth * maxDepth / distanceDecay;
        double xStart = parentX - distance / 2;
        double distanceBetweenChildren = distance / (AmountOfChildren - 1);
        return xStart + distanceBetweenChildren * childIndex;
    }

    private static int height(VisualizableNode visualizableNode) {
        return Arrays.stream(visualizableNode.getChildren())
                .mapToInt(TreeVisualizer::height)
                .max().orElse(0) + 1;
    }

    /**
     * Configure the provided node
     *
     * @param node  node to be configured
     * @param key   in string form to be assigned as graph node label
     * @param color color of the node if one was provided by {@link VisualizableNode#getColor()}
     */
    private void configureNode(Node node, String key, Color color) {
        node.addAttribute("ui.label", key);
        node.addAttribute("ui.class", "unmarked");
        if (color != null) {
            CssGenerator nodeCss = new CssGenerator("node", "#", node.getId());
            nodeCss.set("fill-color", CssGenerator.rgbString(color));
            graph.setAttribute("ui.stylesheet", graph.getAttribute("ui.stylesheet") + nodeCss.toString());
        }

    }

    /**
     * draws and edge between parent and children
     *
     * @param parent the parent node
     * @param child  the child node
     */
    private void drawEdge(Node parent, Node child) {
        String parentId = parent.getId();
        String childId = child.getId();
        graph.addEdge(parentId + "to" + childId, parentId, childId);
    }

    /**
     * Handles MouseWheelEvents. Is used to zoom in and out of the graph
     *
     * @param e          the mouse event
     * @param view_panel the ViewPanel of the graph
     */
    private static void zoomGraphMouseWheelMoved(MouseWheelEvent e, ViewPanel view_panel) {
        if (InputEvent.ALT_DOWN_MASK != 0) {
            if (e.getWheelRotation() > 0) {
                double new_view_percent = view_panel.getCamera().getViewPercent() + 0.05;
                view_panel.getCamera().setViewPercent(new_view_percent);
            } else if (e.getWheelRotation() < 0) {
                double current_view_percent = view_panel.getCamera().getViewPercent();
                if (current_view_percent > 0.05) {
                    view_panel.getCamera().setViewPercent(current_view_percent - 0.05);
                }
            }
        }
    }

    /**
     * Mouse Listener changing some features of the default GraphStream MouseListener
     * removes drag selection that is not working anyways.
     * Instead introduce screen drag, colored selection and multiple selection.
     */
    private static class TreeMouseManager extends DefaultMouseManager {

        private MouseEvent last;
        protected LinkedList<GraphicElement> markedElements;


        TreeMouseManager() {
            super();
            markedElements = new LinkedList<>();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (curElement != null) {
                elementMoving(curElement, e);
            } else {
                view.beginSelectionAt(0, 0);
                view.endSelectionAt(0, 0);
                Camera camera = view.getCamera();
                if (last != null) {
                    Point3 p1 = camera.getViewCenter();
                    Point3 p2 = camera.transformGuToPx(p1.x, p1.y, 0);
                    int xdelta = e.getX() - last.getX();//determine direction
                    int ydelta = e.getY() - last.getY();//determine direction
                    p2.x -= xdelta;
                    p2.y -= ydelta;
                    Point3 p3 = camera.transformPxToGu(p2.x, p2.y);
                    camera.setViewCenter(p3.x, p3.y, 0);

                }
                last = e;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            last = null;

            if (curElement == null && (!(e.isShiftDown() || e.isControlDown()))) {
                markedElements.forEach((elem) -> elem.setAttribute("ui.class", "unmarked"));
                markedElements.clear();
            }
            curElement = view.findNodeOrSpriteAt(e.getX(), e.getY());
            if (curElement != null) {
                mouseButtonPressOnElement(curElement, e);
                if (!SwingUtilities.isRightMouseButton(e)) {
                    if (curElement.getAttribute("ui.class").equals("unmarked")) {
                        curElement.setAttribute("ui.class", "marked");
                        markedElements.add(curElement);
                    }
                }
            } else mouseButtonPress(e);

        }

    }


    /**
     * <p>default values</p><br/>
     *
     * <p>useTreeLayout = {@value #DEFAULT_USE_TREE_LAYOUT }</p>
     * <p>textSize = {@value #DEFAULT_TEXT_SIZE }</p>
     * <p>nodeSize = {@value #DEFAULT_NODE_SIZE }</p>
     * <p>color = {@link #DEFAULT_NODE_COLOR }</p>
     * <p>mark = {@link #DEFAULT_MARK_COLOR }</p>
     */
    public static final class Config {
        public int k;
        public boolean useTreeLayout = DEFAULT_USE_TREE_LAYOUT;
        public int textSize = DEFAULT_TEXT_SIZE;
        public int nodeSize = DEFAULT_NODE_SIZE;
        public Color color = DEFAULT_NODE_COLOR;
        public Color mark = DEFAULT_MARK_COLOR;

        /**
         * Config Object for easy initialization of TreeVisualizer Objects
         *
         * @param k deg+
         * @see Config default values
         */
        public Config(int k) {
            this.k = k;
        }

        /**
         * @param scale scale for text size and node size
         * @see Config#Config(int)
         */
        public Config(int k, float scale) {
            this(k);
            textSize = (int) (DEFAULT_TEXT_SIZE * scale);
            nodeSize = (int) (DEFAULT_NODE_SIZE * scale);
        }
    }
}
