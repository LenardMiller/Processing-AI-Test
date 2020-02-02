package main.pathfinding;

import main.Processing_AI_Test;
import main.Seeker.TurnPoint;
import main.heaps.HeapFloat;
import main.heaps.HeapNode;
import main.heaps.ItemFloat;
import main.heaps.ItemNode;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static main.Processing_AI_Test.*;
import static main.actions.UpdateClearance.updateClearance;
import static main.actions.UpdateNodes.updateNodes;
import static main.actions.UpdatePath.updatePath;

public class Node {

    private PApplet p;

    private Node parent;
    public int clearance;
    public ArrayList<Float> clearanceMp;
    public ItemNode openItem;
    public PVector position;
    public float movementPenalty;
    private float startCost;
    private float endCost;
    public float totalCost;
    public int[] rgb = {255,255,255};
    private int tint;
    private boolean isStart;
    private boolean isEnd;
    public boolean isOpen;
    public boolean isClosed;
    public boolean isNotTraversable;
    private PathRequest request;

    public Node(PApplet p, PVector position){
        this.p = p;

        this.position = position;
    }

    public void main(){
        display();
        hover();
    }

    private void hover(){
        if (p.mouseX < position.x+nSize && p.mouseX > position.x && p.mouseY < position.y+nSize && p.mouseY > position.y){
            p.textFont(font);
            tint = 50;
            p.fill(rgb[0],rgb[1],rgb[2]);
            if(isNotTraversable){p.fill(125);}
            p.text("X: " + (int) (position.x/nSize) + " Y: " + (int) (position.y/nSize), 1100, 50);
            if (!(clearanceMp.size()-1 > defaultSize && clearanceMp.get(defaultSize) > 0)) p.text("MP: " + round(movementPenalty), 1100, 90);
            else p.text("MP: " + (round(movementPenalty)+round(clearanceMp.get(defaultSize))), 1100, 90);
            if (isStart || isEnd || isOpen || isClosed){
                p.text("start: " + round(startCost), 1100, 130);
                p.text("end: " + round(endCost), 1100, 170);
                p.text("total: " + round(totalCost), 1100, 210);
                if (parent != null){
                    p.text("P: X: " + (int) (parent.position.x/nSize) + " Y: " + (int) (parent.position.y/nSize), 1100, 250);
                }
                else p.text("No Parent", 1100, 250);
            }
            if (isStart) p.text("Start", 1100, 950);
            if (isEnd) p.text("End", 1100, 950);
            if (isOpen) p.text("Open", 1100, 910);
            if (isClosed) p.text("Closed", 1100, 910);
            if (isNotTraversable){
                p.text("Not", 1100, 910);
                p.text("Traversable", 1100, 950);
            }
            if (p.mousePressed && p.mouseButton == p.RIGHT){
                isOpen = false;
                isClosed = false;
                parent = null;
                switch (place) {
                    case "s":
                        isNotTraversable = false;
                        setNotEnd((int) (position.x / nSize), (int) (position.y / nSize));
                        setStart((int) (position.x / nSize), (int) (position.y / nSize));
                        break;
                    case "e":
                        isNotTraversable = false;
                        setEnd((int) (position.x / nSize), (int) (position.y / nSize));
                        break;
                    case "nt":
                        isNotTraversable = true;
                        setNotEnd((int) (position.x / nSize), (int) (position.y / nSize));
                        rgb[0] = 50;
                        rgb[1] = 50;
                        rgb[2] = 50;
                        break;
                    case "t":
                        isNotTraversable = false;
                        setNotEnd((int) (position.x / nSize), (int) (position.y / nSize));
                        rgb[0] = 255;
                        rgb[1] = 255;
                        rgb[2] = 255;
                        break;
                    case "mp":
                        movementPenalty++;
                        break;
                    case "mmp":
                        movementPenalty = 205;
                        break;
                    case "nmp":
                        movementPenalty = 0;
                        break;
                    case "mmmp":
                        movementPenalty = 2005;
                        break;
                }
                if (auto) {
                    updatePath(p);
                    updateClearance();
                }
            }
            if (p.mousePressed && p.mouseButton == p.LEFT && isOpen && !auto) setClose();
        }
        else tint = 0;
    }

    private void display(){
        PVector[] tintCost = new PVector[7];
        tintCost[0] = new PVector(255,0,0); //highest cost, far from start
        tintCost[1] = new PVector(255,128,0);
        tintCost[2] = new PVector(255,255,0);
        tintCost[3] = new PVector(0,255,0);
        tintCost[4] = new PVector(0,0,255);
        tintCost[5] = new PVector(255,0,255);
        tintCost[6] = new PVector(0,0,0); //lowest cost, close to start
        float costIncrement = (maxCost-minCost)/6;
        float effectiveCost = totalCost-minCost;
        int costLevel;
        if (effectiveCost < costIncrement) costLevel = 0;
        else if (effectiveCost < costIncrement*2) costLevel = 1;
        else if (effectiveCost < costIncrement*3) costLevel = 2;
        else if (effectiveCost < costIncrement*4) costLevel = 3;
        else if (effectiveCost < costIncrement*5) costLevel = 4;
        else costLevel = 5;

        PVector finalTint;
        float costPercent = (effectiveCost-costIncrement*costLevel)/costIncrement;
        float x = (tintCost[costLevel].x*(1-costPercent))+(tintCost[costLevel+1].x*(costPercent));
        float y = (tintCost[costLevel].y*(1-costPercent))+(tintCost[costLevel+1].y*(costPercent));
        float z = (tintCost[costLevel].z*(1-costPercent))+(tintCost[costLevel+1].z*(costPercent));
        finalTint = new PVector(x,y,z);

        p.stroke(0);
        if (movementPenalty < 205) tint += movementPenalty;
        else tint += 205;
        p.fill(rgb[0]-tint,rgb[1]-tint,rgb[2]-tint);
        if (!displayLines && !displayClearance) p.noStroke();
        if (!(isStart || isOpen || isClosed || isNotTraversable || isEnd) && displayClearance) {
            if (clearance < defaultSize) p.fill(50,0,0);
        }
        if (isEnd) p.fill(255,0,0);
        p.rect(position.x,position.y,nSize,nSize);
        if (totalCost != 0 && colorful) { //a splash of color!
            p.fill(finalTint.x, finalTint.y, finalTint.z, 255);
            p.rect(position.x, position.y, nSize, nSize);
        }
        if (parent != null && displayLines && seekers.size() == 0){
            PVector d = PVector.sub(position,parent.position);
            d.div(2);
            p.line(position.x+nSize/(float)2,position.y+nSize/(float)2,-d.x+position.x+nSize/(float)2,-d.y+position.y+nSize/(float)2);
        }
        if (displayClearance && !isNotTraversable) {
            if (!(clearanceMp.size()-1 > defaultSize && clearanceMp.get(defaultSize-1) > 0)) p.fill(0);
            else p.fill(225,0,0);
            p.textAlign(CENTER);
            p.textFont(fontSmall);
            p.text(clearance, position.x+nSize/2f, position.y+nSize/2f);
        }
    }

    private void setNotEnd(int x, int y){
        if (isEnd){
            isEnd = false;
            int index = numEnd+1;
            PVector pv = new PVector(x*nSize,y*nSize);
            for (int i = 0; i < numEnd; i++){
                if (end[i].position.x == pv.x && end[i].position.y == pv.y){
                    end[i] = end[i+1];
                    index = i;
                }
                if (i > index && i < numEnd-1) end[i] = end[i + 1];
                if (i == numEnd-1) end[i] = null;
            }
            numEnd--;
            updatePath(p);
        }
    }

    public void setStart(int x, int y){
        if (Processing_AI_Test.start != null){
            Processing_AI_Test.start.isStart = false;
            Processing_AI_Test.start.rgb[0] = 255;
            Processing_AI_Test.start.rgb[1] = 255;
            Processing_AI_Test.start.rgb[2] = 255;
        }
        if (seekers.size() == 0){
            rgb[0] = 50;
            rgb[1] = 50;
            rgb[2] = 255;
        }
        Processing_AI_Test.start = nodeGrid[x][y];
        isStart = true;
    }

    public void setEnd(int x, int y){
        if (!isEnd){
            end[numEnd] = nodeGrid[x][y];
            numEnd++;
            isEnd = true;
        }
        rgb[0] = 255;
        rgb[1] = 50;
        rgb[2] = 50;
    }

    public void setOpen(Node parentNew, PathRequest request) {
        this.request = request;
        int size = defaultSize;
        if (request != null) size = request.size;
        if (!(isStart || isOpen || isClosed || isNotTraversable || clearance < size)){
            if(!isEnd){
                rgb[0] = 50;
                rgb[1] = 255;
                rgb[2] = 50;
            }
            isOpen = true;
            findGHF();
        }
        if (parentNew.isClosed || parentNew.isStart){
            if ((isOpen || isClosed) && parent == null){
                parent = parentNew;
                findGHF();
                openNodes.addItem(new ItemNode(nodeGrid[(int )(position.x/nSize)][(int) (position.y/nSize)]));
            }
            if ((isOpen || isClosed) && parentNew.startCost < parent.startCost){ //these have to be split in two because parent might be null
                parent = parentNew;
                findGHF();
                openNodes.addItem(new ItemNode(nodeGrid[(int) (position.x/nSize)][(int) (position.y/nSize)]));
            }
        }
    }

    void setClose(){
        isClosed = true;
        isOpen = false;
        if (isEnd){
            path.done = true;
            if (path.index != -1 && seekers.size() != 0 && path.index < seekers.size()) { //points added HERE
                seekers.get(path.index).points.add(new TurnPoint(p,position));
                seekers.get(path.index).points.add(new TurnPoint(p,parent.position));
            }
            parent.setDone();
        }
        else{
            rgb[0] = 255;
            rgb[1] = 155;
            rgb[2] = 50;
            updateNodes(p,nodeGrid[(int) (position.x/nSize)][(int) (position.y/nSize)],request);
        }
        findGHF();
    }

    private void setDone(){
        if (!isStart){
            rgb[0] = 50;
            rgb[1] = 155;
            rgb[2] = 255;
            if (path.index != -1 && seekers.size() != 0){
                seekers.get(path.index).points.add(new TurnPoint(p,position));
            }
            parent.setDone();
        }
    }

    public void findGHF(){
        if (isEnd){
            endCost = 0;
        }
        else{
            if (numEnd > 0){
                HeapFloat endH = new HeapFloat(numEnd);
                for (int i = 0; i < numEnd; i++){
                    end[i].findGHF();
                    PVector d = PVector.sub(position,end[i].position);
                    endCost = sqrt(sq(d.x)+sq(d.y));
                    endH.addItem(new ItemFloat(endCost));
                }
                endCost = endH.removeFirstItem().value;
            }
        }
        if (isStart){
            startCost = 0;
        }
        else{
            PVector offset;
            if (parent != null){
                offset = PVector.sub(position,parent.position);
                startCost = sqrt(sq(offset.x)+sq(offset.y));
                int size = defaultSize;
                if (request != null) size = request.size;
                float mpn = movementPenalty;
                if (clearanceMp.size() >= size) mpn += clearanceMp.get(size-1); //mpc
                if (mpn > 0){
                    startCost += mpn;
                }
                startCost += parent.startCost;
            }
        }
        totalCost = startCost + endCost;
    }

    public void reset(){
        isOpen = false;
        isClosed = false;
        if(!isStart){
            totalCost = 0;
            endCost = 0;
            startCost = 0;
        }
        openNodes = new HeapNode((int) (sq((float) p.height/nSize)));
        if (!(isStart || isEnd || isNotTraversable)){
            rgb[0] = 255;
            rgb[1] = 255;
            rgb[2] = 255;
        }
        parent = null;
    }
}
