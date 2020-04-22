package graphvisualizer;

import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Used to visualize tree structures implementing {@link VisualizableNode} interface for their Nodes.
 * <p>
 * How to use:<br>
 * <code>new {@link #TreeVisualizer(int)}.{@link #draw(VisualizableNode)};</code><br>
 * There are also other {@link #TreeVisualizer(int, boolean, YOffsetMode, int, Color, Color) constructors} available to do more advanced things.
 *
 * @see <a href="http://graphstream-project.org/" Uses Graphstream gs-core and gs-ui for visualization of the tree graph</a>
 */
public class TreeVisualizer {

    public static final int DEFAULT_TEXT_SIZE = 14;
    public static final boolean DEFAULT_ENABLE_TREE_LAYOUT = true;
    public static final YOffsetMode DEFAULT_ENABLE_Y_OFFSET = YOffsetMode.AUTO;
    public static final Color DEFAULT_NODE_COLOR = Color.white;
    public static final Color DEFAULT_MARK_COLOR = Color.white;

    private static final double X_SCALE = 50;
    private static final double Y = 300;

    private int k;
    private boolean useTreeLayout;
    private YOffsetMode enableYOffsetMode;

    private GraphicGraph graph;
    private Viewer viewer;
    private ViewPanel viewPanel;
    private CssGenerator generalStyle;
    private CssGenerator markedStyle;

    // flag stating if a node containing multiple keys was found while drawing. Is set to false upon reset.
    private boolean multipleKeys;
    private int nodeAmount;
    private boolean firstVisualization = true;

    /**
     * Calls {@link TreeVisualizer#TreeVisualizer(int, boolean, YOffsetMode, int, Color, Color)} with provided k and the {@link Config default values}
     *
     * @param k max deg+
     * @see Config default values
     */
    public TreeVisualizer(int k) {
        this(k, DEFAULT_ENABLE_TREE_LAYOUT, DEFAULT_ENABLE_Y_OFFSET, DEFAULT_TEXT_SIZE, DEFAULT_NODE_COLOR, DEFAULT_MARK_COLOR);
    }

    /**
     * Calls {@link TreeVisualizer#TreeVisualizer(int, boolean, YOffsetMode, int, Color, Color)} with using arguments provided in the {@link Config} parameter
     *
     * @param config {@link Config} initialize TreeVisualizer with provided Attributes.
     * @see Config default values
     */
    public TreeVisualizer(Config config) {
        this(config.k, config.enableTreeLayout, config.enableYOffsetMode, config.textSize, config.color, config.mark);
    }


    /**
     * Creates a new TreeVisualizer with provided settings. All settings can be changes afterwards using their setters.
     *
     * @param k                 max deg+
     * @param enableTreeLayout  if the Tree View shall be applied when the tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     *                          Else use GraphStream <a href="http://graphstream-project.org/doc/Tutorials/Graph-Visualisation/1.0/#automatic-layout">automatic layout</a>.
     * @param enableYOffsetMode {@link YOffsetMode YOffsetMode} if an alternating y offset should be applied to neighbouring children when the tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param textSize          size of value letters when tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param color             color of the nodes when tree is {@link TreeVisualizer#draw(VisualizableNode) displayed}.
     * @param mark              color of the nodes when tree is {@link TreeVisualizer#draw(VisualizableNode) displayed} and the node is marked (left click on node).
     * @see Config default values
     */
    public TreeVisualizer(int k, boolean enableTreeLayout, YOffsetMode enableYOffsetMode, int textSize, Color color, Color mark) {
        this.enableYOffsetMode = enableYOffsetMode;
        // SELECT RENDERER
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        // INITIALIZE ATTRIBUTES
        this.k = k;
        this.useTreeLayout = enableTreeLayout;
        graph = new GraphicGraph("Tree");

        // BUILD CSS
        generalStyle = new CssGenerator("node");
        generalStyle.set("fill-mode", "plain");
        generalStyle.set("fill-color", CssGenerator.rgbString(color));
        generalStyle.set("shadow-mode", "gradient-radial");
        generalStyle.set("shadow-color", "#999, white");
        generalStyle.set("shadow-width", "0px");
        generalStyle.set("shadow-offset", "3px, -3px");
        generalStyle.set("stroke-mode", "plain");
        generalStyle.set("stroke-color", "black");
        generalStyle.set("stroke-width", "1");
        generalStyle.set("text-font", Font.MONOSPACED);
        generalStyle.set("text-color", CssGenerator.rgbString(Color.black));
        generalStyle.set("text-size", String.valueOf(textSize));

        markedStyle = new CssGenerator("node", ".", "marked");
        markedStyle.set("text-style", "bold");
        markedStyle.set("fill-color", CssGenerator.rgbString(mark));
        markedStyle.set("stroke-width", "2");
        markedStyle.set("z-index", "1000");
    }

    /**
     * One time call on first draw call
     */
    private void viewSetup() {
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        // SETUP MOUSE LISTENERS
        ViewPanel view_panel_frame = viewer.addDefaultView(true);
        viewPanel = viewer.getDefaultView();
        // remove GraphStream default MouseListener
        view_panel_frame.removeMouseListener(view_panel_frame.getMouseListeners()[0]);
        // Assign our own Mouse Listener which is making some changes to the GraphStream default MouseListener
        TreeMouseManager mouseManager = new TreeMouseManager();
        mouseManager.init(graph, viewPanel);
        //add a mouse wheel listener to the ViewPanel for zooming the graph
        view_panel_frame.addMouseWheelListener(e -> TreeVisualizer.zoomGraphMouseWheelMoved(e, viewPanel));
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

    public boolean isTreeLayout() {
        return useTreeLayout;
    }

    public void enableTreeLayout(YOffsetMode enableYOffsetMode) {
        this.enableYOffsetMode = enableYOffsetMode;
    }

    public YOffsetMode isEnableYOffsetMode() {
        return enableYOffsetMode;
    }

    public void setEnableYOffsetMode(YOffsetMode enableYOffsetMode) {
        this.enableYOffsetMode = enableYOffsetMode;
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

    public void scale(float textScale) {
        setTextSize((int) (getTextSize() * textScale));
    }

    /**
     * reset the graph and perform a default setup
     */
    private void reset() {
        graph.clear();
        multipleKeys = false;
        graphSetup();
        viewer.getDefaultView().setVisible(false);

    }

    /**
     * Default graph setup
     */
    private void graphSetup() {
        if(firstVisualization||viewer.getDefaultView()==null) {
            viewSetup();
            firstVisualization = false;
        }
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
        if(root == null) {
            CssGenerator noNodeCss = new CssGenerator("node");
            noNodeCss.set("stroke-mode", "none");
            noNodeCss.set("fill-color", CssGenerator.rgbString(Color.white));
            noNodeCss.set("shadow-mode", "none");
            graph.setAttribute("ui.stylesheet", graph.getAttribute("ui.stylesheet") + noNodeCss.toString());
            Node emptyMessageNode = graph.addNode("0");
            emptyMessageNode.addAttribute("ui.label", "EMPTY");
            emptyMessageNode.addAttribute("ui.class", "marked");
        } else {
            int height = getHeightCountNodesAndCheckForMultipleKeys(root);

            // Zoom in because the view my lag if to many nodes are visible at the same time
            if (nodeAmount > 300)
                if (nodeAmount > 500)
                    if (nodeAmount > 700)
                        viewPanel.getCamera().setViewPercent(0.3);
                    else
                        viewPanel.getCamera().setViewPercent(0.35);
                else
                    viewPanel.getCamera().setViewPercent(0.45);
            if (multipleKeys) {
                // if any node has more then 1 value set shape of all nodes to "box"
                CssGenerator generalCss = new CssGenerator("node");
                generalCss.set("shape", "rounded-box");
                graph.setAttribute("ui.stylesheet", graph.getAttribute("ui.stylesheet") + generalCss.toString());
            }
            // draw root, using it's hashcode as id
            Node graphRoot = graph.addNode(String.valueOf(root.hashCode()));
            // Stringify key so it can be displayed
            String[] rootKeyStrings = getKeys(root);
            configureNode(graphRoot, rootKeyStrings, root.getColor());
            if (useTreeLayout)
                graphRoot.setAttribute("xyz", 0.0, 0.0, 0.0);
            // traverse tree recursive drawing all nodes
            addNodesRecursive(root, 1, height);
        }
        viewer.getDefaultView().setVisible(true);
    }

    private String[] getKeys(VisualizableNode node) {
        return Arrays.stream(node.getKeys())
                .map(Object::toString).toArray(String[]::new);
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
        VisualizableNode[] children = filterNullChildren(parentNode.getChildren());
        //draws all children and edges to them. Recursively traverse children subtrees
        for (int i = 0; i < children.length; i++) {
            VisualizableNode child = children[i];
            // draw child, using it's hashcode as id
            Node childGraphNode = graph.addNode(String.valueOf(child.hashCode()));
            if (useTreeLayout) {
                // calculate custom node position
                double x = calculateNodeXPosition((double) parentXYZ[0], i, children.length, currentDepth, maxDepth);
                double y = calculateNodeYPosition((double) parentXYZ[1], i, multipleKeys, currentDepth, maxDepth);
                childGraphNode.setAttribute("xyz", x, y, 0);
            }
            drawEdge(graphNode, childGraphNode);
            // Stringify key so it can be displayed
            String[] childKeyStrings = getKeys(child);
            configureNode(childGraphNode, childKeyStrings, child.getColor());
            // traverse child subtree
            addNodesRecursive(child, currentDepth + 1, maxDepth);
        }
    }

    /**
     * calculates the x position for a child at a specific index
     *
     * @param parentX          parent node x position
     * @param childIndex       index of the child in parents children list
     * @param AmountOfChildren total amount of children of the parent
     * @param currentDepth     current depth in the complete tree
     * @param maxDepth         total depth the complete tree will reach
     * @return child node x position
     */
    private double calculateNodeXPosition(double parentX, int childIndex, int AmountOfChildren,
                                          int currentDepth, int maxDepth) {
        if (AmountOfChildren == 1)
            return parentX;
        double distanceDecay = Math.pow(k, currentDepth);
        // calculation of "distance" was decided by trial and error in terms of how k and maxDepth affected it.
        double distance = X_SCALE * k * k * maxDepth * maxDepth / distanceDecay;
        double xStart = parentX - distance / 2;
        double distanceBetweenChildren = distance / (AmountOfChildren - 1);
        return xStart + distanceBetweenChildren * childIndex;
    }

    /**
     * @param parentY      parent node Y position
     * @param childIndex   index of the child in parents children list
     * @param multipleKeys if the current tree contains nodes with multiple values
     * @param currentDepth current depth in the complete tree
     * @param maxDepth     total depth the complete tree will reach
     * @return child node y position
     */
    private double calculateNodeYPosition(double parentY, int childIndex, boolean multipleKeys, int currentDepth, int maxDepth) {
        double y = parentY + Y;
        if (enableYOffsetMode == YOffsetMode.ON)
            y += childIndex * 1.25 * (getTextSize() + getHeightPadding()) * ((currentDepth % 2) * 2 + -1);
        else if (enableYOffsetMode == YOffsetMode.AUTO)
            y += autoYOffset(k, multipleKeys, maxDepth) ? childIndex * 1.25 * (getTextSize() + getHeightPadding()) * ((currentDepth % 2) * 2 + -1) : 0;
        return y;
    }

    /**
     * Decides if an YOffset should be applied to the tree <br>
     * <br>
     * In general:<br>
     * if the tree reaches certain depth by high enough k the y offset is applied.<br>
     * if the tree contains multiple keys the y offset is applied
     *
     * @param k                        max deg+
     * @param treeContainsMultipleKeys if the currently drawn tree contains multiple keys
     * @param maxDepth                 the maximum depth of the currently drawn tree
     * @return if a YOffset should be applied
     */
    private boolean autoYOffset(int k, boolean treeContainsMultipleKeys, int maxDepth) {
        return treeContainsMultipleKeys && maxDepth > 2 || k * maxDepth > 16;
    }

    private int getHeightCountNodesAndCheckForMultipleKeys(VisualizableNode visualizableNode) {
        nodeAmount++;
        return Arrays.stream(filterNullChildren(visualizableNode.getChildren()))
                .mapToInt((node) -> {
                            if (node.getKeys().length > 1)
                                multipleKeys = true;
                            return getHeightCountNodesAndCheckForMultipleKeys(node);
                        }
                )
                .max().orElse(0) + 1;
    }

    private VisualizableNode[] filterNullChildren(VisualizableNode[] children) {
        return Arrays.stream(children).filter(Objects::nonNull).toArray(VisualizableNode[]::new);
    }

    /**
     * Configure the provided node
     *
     * @param node  node to be configured
     * @param keys  in string form to be assigned as graph node label
     * @param color color of the node if one was provided by {@link VisualizableNode#getColor()}
     */
    private void configureNode(Node node, String[] keys, Color color) {
        String delimiter = " | ";
        String unitedKey = String.join(delimiter, keys);
        node.addAttribute("ui.label", unitedKey);
        node.addAttribute("ui.class", "unmarked");
        CssGenerator nodeCss = new CssGenerator("node", "#", node.getId());
        if (color != null) {
            nodeCss.set("fill-color", CssGenerator.rgbString(color));
        }
        // Calculate node size
        float delimiterLength = delimiter.length();
        // for some reason stroke size is actually closer to 2 then 1 despite setting it to 1 in the generalStyle css.
        float strokeWidth = 2f;
        float widthPaddingWithBorderSize = getTextSize() * delimiterLength / 2f + 2 * strokeWidth;
        float nodeWidth = (Arrays.stream(keys).mapToInt(String::length).sum() + delimiterLength * (keys.length - 1)) * getTextSize() * 0.6f;
        float nodeHeight = multipleKeys ? getTextSize() + getHeightPadding() : nodeWidth + widthPaddingWithBorderSize;
        nodeCss.set("size", (nodeWidth + widthPaddingWithBorderSize) + "px, " + nodeHeight + "px");
        graph.setAttribute("ui.stylesheet", graph.getAttribute("ui.stylesheet") + nodeCss.toString());

    }

    private float getHeightPadding() {
        return getTextSize() / 3f;
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
        protected LinkedList<Node> markedElements;


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
            // deselect node
            if (curElement == null && (!(e.isShiftDown() || e.isControlDown() || SwingUtilities.isRightMouseButton(e)))) {
                markedElements.forEach(node -> {
                            node.setAttribute("ui.class", "unmarked");
                            node.getEdgeSet().forEach(edge -> edge.setAttribute("ui.style", "size: 1px;"));
                        }
                );
                markedElements.clear();
            }
            curElement = view.findNodeOrSpriteAt(e.getX(), e.getY());
            //select node if not currently dragging and not pressed the right mouse button
            if (curElement != null) {
                super.mouseButtonPressOnElement(curElement, e);
                if (!SwingUtilities.isRightMouseButton(e)) {
                    if (curElement.getAttribute("ui.class").equals("unmarked")) {
                        Node node = graph.getNode(curElement.getId());

                        curElement.setAttribute("ui.class", "marked");
                        node.getEdgeSet().forEach(edge -> edge.setAttribute("ui.style", "size: 3px;"));
                        markedElements.add(node);
                    }
                }
            } else mouseButtonPress(e);

        }

    }


    /**
     * <p>default values</p><br/>
     *
     * <p>enableTreeLayout = {@value #DEFAULT_ENABLE_TREE_LAYOUT }</p>
     * <p>enableYOffset = {@link #DEFAULT_ENABLE_Y_OFFSET }</p>
     * <p>textSize = {@value #DEFAULT_TEXT_SIZE }</p>
     * <p>color = {@link #DEFAULT_NODE_COLOR }</p>
     * <p>mark = {@link #DEFAULT_MARK_COLOR }</p>
     */
    public static final class Config {
        public int k;
        public boolean enableTreeLayout = DEFAULT_ENABLE_TREE_LAYOUT;
        public YOffsetMode enableYOffsetMode = DEFAULT_ENABLE_Y_OFFSET;
        public int textSize = DEFAULT_TEXT_SIZE;
        public Color color = DEFAULT_NODE_COLOR;
        public Color mark = DEFAULT_MARK_COLOR;

        /**
         * Config Object for easy initialization of TreeVisualizer Objects
         *
         * @param k max deg+
         * @see Config default values
         */
        public Config(int k) {
            this.k = k;
        }
    }

    /**
     * A y offset helps to read deep trees and trees with many keys
     * {@link #DEFAULT_ENABLE_Y_OFFSET default value}
     *
     * @see TreeVisualizer#autoYOffset on AUTO
     */
    public enum YOffsetMode {
        ON,
        OFF,
        AUTO
    }
}
