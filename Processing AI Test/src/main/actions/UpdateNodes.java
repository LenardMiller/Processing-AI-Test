package main.actions;

import main.pathfinding.Node;
import main.pathfinding.PathRequest;
import processing.core.PApplet;

import static main.Processing_AI_Test.nSize;
import static main.Processing_AI_Test.nodeGrid;

public class UpdateNodes {

    private UpdateNodes() {}

    public static void updateNodes(PApplet p, Node current, PathRequest request) { //request can be null; will default to defaultSize
        int x = (int) (current.position.x / nSize);
        int y = (int)(((current.position.y)/nSize));
        if (x == 0 && y == 0) {
            nodeGrid[x + 1][y].setOpen(current,request);
            nodeGrid[x + 1][y + 1].setOpen(current,request);
            nodeGrid[x][y + 1].setOpen(current,request);
        } else if (x == 0 && (y > 0) && y < (p.height / (nSize)) - 1) {
            nodeGrid[x][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y].setOpen(current,request);
            nodeGrid[x + 1][y + 1].setOpen(current,request);
            nodeGrid[x][y + 1].setOpen(current,request);
        } else if (x == ((p.width - 200) / (nSize)) - 1 && y == 0) {
            nodeGrid[x][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y].setOpen(current,request);
        } else if ((x > 0 && x < ((p.width - 200) / (nSize)) - 1) && y == 0) {
            nodeGrid[x + 1][y].setOpen(current,request);
            nodeGrid[x + 1][y + 1].setOpen(current,request);
            nodeGrid[x][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y].setOpen(current,request);
        } else if (x == ((p.width - 200) / (nSize)) - 1 && y == (p.height / (nSize)) - 1) {
            nodeGrid[x - 1][y].setOpen(current,request);
            nodeGrid[x - 1][y - 1].setOpen(current,request);
            nodeGrid[x][y - 1].setOpen(current,request);
        } else if (x == ((p.width - 200) / (nSize)) - 1 && y < (p.height / (nSize)) - 1) {
            nodeGrid[x][y - 1].setOpen(current,request);
            nodeGrid[x - 1][y - 1].setOpen(current,request);
            nodeGrid[x - 1][y].setOpen(current,request);
            nodeGrid[x - 1][y + 1].setOpen(current,request);
            nodeGrid[x][y + 1].setOpen(current,request);
        } else if (x == 0 && y == (p.height / (nSize)) - 1) {
            nodeGrid[x][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y].setOpen(current,request);
        } else if (x < ((p.width - 200) / (nSize)) - 1 && y == (p.height / (nSize)) - 1) {
            nodeGrid[x + 1][y].setOpen(current,request);
            nodeGrid[x + 1][y - 1].setOpen(current,request);
            nodeGrid[x][y - 1].setOpen(current,request);
            nodeGrid[x - 1][y - 1].setOpen(current,request);
            nodeGrid[x - 1][y].setOpen(current,request);
        } else {
            nodeGrid[x - 1][y - 1].setOpen(current,request);
            nodeGrid[x][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y - 1].setOpen(current,request);
            nodeGrid[x + 1][y].setOpen(current,request);
            nodeGrid[x + 1][y + 1].setOpen(current,request);
            nodeGrid[x][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y + 1].setOpen(current,request);
            nodeGrid[x - 1][y].setOpen(current,request);
        }
    }
}
