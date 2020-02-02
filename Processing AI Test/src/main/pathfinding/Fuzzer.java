package main.pathfinding;

import static main.Processing_AI_Test.nSize;
import static main.Processing_AI_Test.nodeGrid;
import static processing.core.PApplet.ceil;

public class Fuzzer{

    private int ks;
    private float mp;
    private float[] mps;

    public Fuzzer(int ks){
        this.ks = ks;
        mp = 0;
        fuzz();
    }

    private void fuzz(){
        leftRight();
        topBottom();
    }

    private void leftRight(){
        for (int y = 0; y < 1000/nSize; y++){
            mps = new float[1000/nSize];
            for (int x = 0; x < mps.length; x++){
                mps[x] = nodeGrid[x][y].movementPenalty;
            }
            mp = mps[0];
            for (int i = 1; i <= (int)(ks/2); i++){
                mp += mps[0];
                mp += mps[i];
            }
            nodeGrid[0][y].movementPenalty = mp/ks;
            for (int x = 1; x < 1000/nSize; x++){
                if (x - (int)(ks/2) <= 0){
                    mp -= mps[0];
                }
                else{
                    mp -= mps[x-ceil((float)(ks)/2)];
                }
                if (x + (int)(ks/2) >= 1000/nSize){
                    mp += mps[mps.length-1];
                }
                else{
                    mp += mps[x+(int)(ks/2)];
                }
                nodeGrid[x][y].movementPenalty = mp/ks;
            }
        }
    }
    private void topBottom(){
        for (int x = 0; x < 1000/nSize; x++){
            mps = new float[1000/nSize];
            for (int y = 0; y < mps.length; y++){
                mps[y] = nodeGrid[x][y].movementPenalty;
            }
            mp = mps[0];
            for (int i = 1; i <= (int)(ks/2); i++){
                mp += mps[0];
                mp += mps[i];
            }
            nodeGrid[x][0].movementPenalty = mp/ks;
            for (int y = 1; y < 1000/nSize; y++){
                if (y - (int)(ks/2) <= 0){
                    mp -= mps[0];
                }
                else{
                    mp -= mps[y-ceil((float)(ks)/2)];
                }
                if (y + (int)(ks/2) >= 1000/nSize){
                    mp += mps[mps.length-1];
                }
                else{
                    mp += mps[y+(int)(ks/2)];
                }
                nodeGrid[x][y].movementPenalty = mp/ks;
            }
        }
    }
}