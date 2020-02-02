package main.pathfinding;

import main.Processing_AI_Test;
import main.Seeker;
import processing.core.PApplet;

import java.util.ArrayList;

import static main.Processing_AI_Test.nSize;
import static main.Processing_AI_Test.nodeGrid;
import static main.actions.UpdateNodes.updateNodes;
import static processing.core.PApplet.round;

public class PathRequest {

    public int id;
    public Seeker seeker;
    int size;

    public PathRequest(int id, Seeker seeker){
        this.id = id;
        this.seeker = seeker;
        this.size = seeker.size;
    }

    public void getPath(PApplet p){
        for (Node[] nodes : nodeGrid) {
            for (Node node : nodes) {
                node.reset();
            }
        }

        seeker.points = new ArrayList<>();
        nodeGrid[round((seeker.position.x-(seeker.size/2f))/nSize)][round((seeker.position.y-(seeker.size/2f))/nSize)].setStart(round((seeker.position.x-(seeker.size/2f))/nSize), round((seeker.position.y-(seeker.size/2f))/nSize));
        Processing_AI_Test.start.findGHF();
        updateNodes(p,Processing_AI_Test.start,this);
        Processing_AI_Test.path.done = false;
        Processing_AI_Test.path.find(id); //points are added here
        seeker.swapPoints(false);
        seeker.cleanTurnPoints(); //and subtracted here
    }
}
