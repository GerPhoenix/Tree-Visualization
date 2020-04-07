# Tree-Visualization
## Description
Visualization tool for tree structures

## Requirements
[gs-core & gs-ui](http://graphstream-project.org/download/)

## How to
Nodes of trees you want to visualize have to implement the [VisualizableNode](https://github.com/GerPhoenix/Tree-Visualization/blob/master/src/VisualizableNode.java) Interface.
Then you can use a [TreeVisualizer](https://github.com/GerPhoenix/Tree-Visualization/blob/master/src/TreeVisualizer.java) Object to visualize your trees using the `TreeVisualizer.visualize(INode node)` function.
