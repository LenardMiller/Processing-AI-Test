package main;

import main.heaps.HeapNode;
import main.pathfinding.AStar;
import main.pathfinding.Fuzzer;
import main.pathfinding.Node;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;

import static main.actions.MiscText.toggleText;
import static main.actions.UpdateClearance.updateClearance;
import static main.actions.UpdateNodes.updateNodes;
import static main.actions.UpdatePath.updatePath;

public class Processing_AI_Test extends PApplet {

    public static PFont font;
    public static PFont fontSmall;

    public static Node[][] nodeGrid;
    public static HeapNode openNodes;
    public static Node start;
    public static Node[] end;
    public static AStar path;
    public static ArrayList<Seeker> seekers;
    public static String place;
    public static Fuzzer fuzzer;

    public static int defaultSize = 1;
    public static int nSize;
    public static int numEnd;
    public static float maxCost;
    public static float minCost;

    public static boolean auto = true;
    public static boolean displayLines = false;
    public static boolean displayClearance = false;
    public static boolean colorful = false;

    public static void main(String[] args) {
        PApplet.main("main.Processing_AI_Test",args);
    }

    public void settings() {
        size(1200,1000);
    }

    public void setup() {
        ellipseMode(CORNER);
        nSize = 25; //works best if divisor of 1000
        nodeGrid = new Node[(width-200)/nSize][height/nSize];
        for (int x = 0; x < (width-200)/nSize; x++){
            for (int y = 0; y < height/nSize; y++){
                nodeGrid[x][y] = new Node(this,new PVector(nSize*x,nSize*y));
            }
        }

        path = new AStar();
        seekers = new ArrayList<>();
        openNodes = new HeapNode((int) sq((float) 1000/nSize));
        end = new Node[(int) (sq((float) 1000/nSize))];

        nodeGrid[(1000/nSize)-2][(1000/nSize)/2].setEnd((1000/nSize)-2,(1000/nSize)/2);
        nodeGrid[1][(1000/nSize)/2].setStart(1,(1000/nSize)/2);
        start.findGHF();
        for (int i = 0; i < numEnd; i++) end[i].findGHF();
        updateClearance();
        updateNodes(this,start,null);
        updatePath(this);

        place = "nt";
        font = createFont("STHeitiSC-Light", 32);
        fontSmall = createFont("STHeitiSC-Light", 12);
        textAlign(CENTER);
        textFont(font);
    }

    public void draw() {
        noStroke();
        fill(20,25,45);
        rect(1000,0,200,height);
        fill(255);
        rect(0,0,width-200,height);
        toggleText(this);

        if (path.reqQ.size() > 0){
            path.reqQ.get(0).getPath(this);
            path.reqQ.remove(0);
        }

        maxCost = maxCost();
        minCost = minCost(maxCost);
        for (Node[] nodes : nodeGrid) {
            for (Node node : nodes) {
                node.main();
            }
        }

        for (int i = seekers.size()-1; i >= 0; i--) seekers.get(i).sMain(i);
    }

    private float maxCost() {
        float maxCost = 0;
        for (Node[] nodes : nodeGrid) {
            for (Node node : nodes) {
                if (node.totalCost > maxCost && (node.isOpen || node.isClosed)) maxCost = node.totalCost;
            }
        }
        return maxCost;
    }

    private float minCost(float maxCost) {
        float minCost = maxCost;
        for (Node[] nodes : nodeGrid) {
            for (Node node : nodes) {
                if (node.totalCost < minCost && (node.isOpen || node.isClosed)) minCost = node.totalCost;
            }
        }
        return minCost;
    }

    public void keyReleased(){
        if (key == 'h'){
            println("--Key Bindings--");
            println("h: display this list");
            println("1-8: change tile placement type, type is displayed on right");
            println("l: display fit lines & tile borders");
            println("r: display clearance & tile borders");
            println("a: toggle autosolve");
            println("[SPACE]: reload paths");
            println("v/b: set all nodes to movement penalty 205/0");
            println("q: add new seeker at cursor position");
            println("w: add new obstacle of random size at cursor position");
            println("s: kill all seekers");
            println("d: destroy all obstacles");
            println("-: reduce size");
            println("+: increase size");
            println("t: make things colorful!");
            println("z/x/c: fuzz movement penalty at sizes 3/5/7");
        }
        if (key == '1') place = "nt";
        if (key == '2') place = "t";
        if (key == '3') place = "s";
        if (key == '4') place = "e";
        if (key == '5') place = "mp";
        if (key == '6') place = "mmp";
        if (key == '7') place = "nmp";
        if (key == '8') place = "mmmp";
        if (key == 'l') displayLines = !displayLines;
        if (key == 't') colorful = !colorful;
        if (key == 'r') {
            displayClearance = !displayClearance;
            updateClearance();
        }
        if (key == 'z') {
            fuzzer = new Fuzzer(3);
            updatePath(this);
        }
        if (key == 'x') {
            fuzzer = new Fuzzer(5);
            updatePath(this);
        }
        if (key == 'c') {
            fuzzer = new Fuzzer(7);
            updatePath(this);
        }
        if (key == 'a'){
            if (auto){
                for (Node[] nodes : nodeGrid) {
                    for (Node node : nodes) {
                        node.reset();
                    }
                }
                auto = false;
                updateNodes(this,start,null);
            }
            else{
                updatePath(this);
                updateClearance();
                auto = true;
            }
        }
        if (key == ' ') {
            updatePath(this);
            updateClearance();
        }
        if (key == 'v'){
            for (Node[] nodes : nodeGrid) {
                for (Node node : nodes) {
                    node.movementPenalty = 205;
                }
            }
            updatePath(this);
            updateClearance();
        }
        if (key == 'b'){
            for (Node[] nodes : nodeGrid) {
                for (Node node : nodes) {
                    node.movementPenalty = 0;
                }
            }
            updatePath(this);
            updateClearance();
        }
        if (key == 'q' && mouseX < 1000) {
            seekers.add(new Seeker(this,new PVector(roundBy(mouseX,nSize),roundBy(mouseY,nSize)),1));
            for (Node[] nodes : nodeGrid) {
                for (Node node : nodes) {
                    node.reset();
                }
            }
            for (int i = 0; i < 3; i++){
                start.rgb[i] = 255;
            }
            seekers.get(seekers.size()-1).requestPath(seekers.size()-1);
        }
        if (key == 'Q' && mouseX < 1000) {
            defaultSize = (int)random(1,8);
            seekers.add(new Seeker(this,new PVector(roundBy(mouseX,nSize),roundBy(mouseY,nSize)),1));
            for (Node[] nodes : nodeGrid) {
                for (Node node : nodes) {
                    node.reset();
                }
            }
            for (int i = 0; i < 3; i++){
                start.rgb[i] = 255;
            }
            seekers.get(seekers.size()-1).requestPath(seekers.size()-1);
        }
        if (key == 's'){
            seekers = new ArrayList<>();
            updatePath(this);
            updateClearance();
        }
        if (key == '-' && defaultSize > 1) {
            defaultSize--;
            updateClearance();
            updatePath(this);
        }
        if (key == '=') {
            defaultSize++;
            updateClearance();
            updatePath(this);
        }
    }

    public static int roundBy(float input, int rounder) {
        return ((int)(input/rounder)) * rounder;
    }
}


