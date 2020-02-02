package main.actions;

import processing.core.PApplet;

import static main.Processing_AI_Test.*;
import static main.actions.UpdateNodes.updateNodes;

public class UpdatePath {

    private UpdatePath() {}

    public static void updatePath(PApplet p){
        for (main.pathfinding.Node[] nodes : nodeGrid) {
            for (main.pathfinding.Node node : nodes) {
                node.reset();
            }
        }
        if (seekers.size() > 0){
            for (int i = seekers.size()-1; i >= 0; i--){
                boolean d = false;
                for (int j = path.reqQ.size()-1; j >= 0; j--){
                    if (seekers.get(i) == path.reqQ.get(j).seeker) {
                        d = true;
                        break;
                    }
                }
                if (!d){
                    seekers.get(i).requestPath(i);
                }
            }
        }
        else{
            start.setStart((int) (start.position.x/nSize),(int) (start.position.y/nSize));
            start.findGHF();
            updateNodes(p,start,null);
            path.done = false;
            path.find(-1);
        }
    }
}
