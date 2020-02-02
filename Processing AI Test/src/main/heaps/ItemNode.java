package main.heaps;

import main.pathfinding.Node;

public class ItemNode {

    public Node node;
    float value;
    int index;

    public ItemNode(Node node){
        node.findGHF();
        this.value = node.totalCost;
        this.node = node;
    }
}
