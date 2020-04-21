# Tree-Visualization
## Description
Visualization tool for tree structures

## Required libaries
[gs-core & gs-ui](http://graphstream-project.org/download/)

The required files are already in the [lib](https://github.com/GerPhoenix/Tree-Visualization/tree/master/lib) directory

## How to
Nodes of trees you want to draw have to implement the [VisualizableNode](https://github.com/GerPhoenix/Tree-Visualization/blob/master/src/graphvisualizer/VisualizableNode.java) Interface.
Then you can use a [TreeVisualizer](https://github.com/GerPhoenix/Tree-Visualization/blob/master/src/graphvisualizer/TreeVisualizer.java) Object to draw your trees using the `TreeVisualizer.draw(VisualizableNode root)` function.
