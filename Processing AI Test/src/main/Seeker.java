package main;

import main.pathfinding.PathRequest;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.Processing_AI_Test.*;
import static main.actions.UpdatePath.updatePath;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.atan;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.PI;
import static processing.core.PVector.angleBetween;

public class Seeker {

    private PApplet p;

    public ArrayList<TurnPoint> points;
    public PVector position;
    public int size;
    private float speed;
    private float angle;

    Seeker(PApplet p, PVector position, float speed){
        this.p = p;

        points = new ArrayList<>();
        this.position = position;
        this.speed = speed;
        size = defaultSize;
        angle = 0;
    }

    void sMain(int i){
        display();
        move(i);
    }

    private void display(){
        if (Processing_AI_Test.displayLines){
            for (int i = points.size()-1; i > 0; i--){
                points.get(i).display();
            }
        }
        p.fill(0,0,255);
        p.stroke(0);
        p.ellipse(position.x,position.y,nSize*size,nSize*size);
    }

    private void move(int i){
        PVector moveDist = PVector.fromAngle(angle);
        moveDist.setMag(speed);
        position.add(moveDist);
        if (position.x >= 1000 || position.x <= 0 || position.y >= 1000 || position.y <= 0) die(i);
        if (points.size() != 0){
            PVector p = points.get(points.size()-1).position;
            boolean intersecting;
            intersecting = (position.x+(nSize/2f) >= p.x && position.x+(nSize/2f) <= p.x+nSize+size) && (position.y+(nSize/2f) >= p.y && position.y+(nSize/2f) <= p.y+nSize+size);
            if (intersecting) swapPoints(true);
        }
        if (points.size() == 0) die(i);
    }

    public void requestPath(int i){
        Processing_AI_Test.path.reqQ.add(new PathRequest(i,seekers.get(i)));
    }

    public void swapPoints(boolean remove) {
        if (remove) points.remove(points.size() - 1);
        if (points.size() != 0){
            PVector pointPosition = points.get(points.size()-1).position;
            pointPosition = new PVector(pointPosition.x,pointPosition.y);
            angle = findAngleBetween(pointPosition,position);
        }
    }

    private void die(int i){
        for (int j = 0; j < Processing_AI_Test.path.reqQ.size(); j++){
            if (Processing_AI_Test.path.reqQ.get(j).id > i){
                Processing_AI_Test.path.reqQ.get(j).id--;
            }
            if (Processing_AI_Test.path.reqQ.get(j).id == i){
                Processing_AI_Test.path.reqQ.remove(j);
            }
        }
        if (Processing_AI_Test.path.index == i){
            Processing_AI_Test.path.index = -1;
        }
        seekers.remove(i);
        if (seekers.size() == 0) updatePath(p);
    }

    private static float findAngleBetween(PVector p1, PVector p2){
        //https://forum.processing.org/one/topic/pvector-anglebetween.html
        float a = atan2(p1.y-p2.y, p1.x-p2.x);
        if (a<0) { a+=TWO_PI; }
        return a;
    }

    public void cleanTurnPoints() {
        ArrayList<TurnPoint> pointsD = new ArrayList<>(points);
        for (int i = 0; i < pointsD.size()-2; i++) {
            TurnPoint pointA = pointsD.get(i);
            TurnPoint pointB = pointsD.get(i+1);
            TurnPoint pointC = pointsD.get(i+2);
            float angleAB = findAngleBetween(pointA.position, pointB.position);
            float angleBC = findAngleBetween(pointB.position, pointC.position);
            if (angleAB == angleBC) {
                pointsD.remove(pointB);
                i--;
            }
            if (i+1 == pointsD.size()+2) break;
        }
        points = new ArrayList<>();
        points.addAll(pointsD);
    }

    public static class TurnPoint {

        private PApplet p;

        PVector position;

        public TurnPoint(PApplet p, PVector position){
            this.p = p;
            this.position = position;
        }

        void display(){
            p.fill(0);
            p.ellipse(position.x,position.y,nSize,nSize);
        }
    }

}
