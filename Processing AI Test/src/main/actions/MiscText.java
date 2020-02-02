package main.actions;

import main.Processing_AI_Test;
import processing.core.PApplet;

import static main.Processing_AI_Test.*;

public class MiscText {

    private MiscText() {}

    public static void toggleText(PApplet p) {
        p.fill(255);
        p.textFont(font);
        p.text("Size: ", 1100, 350);
        p.text(defaultSize, 1100, 390);
        if (place.equals("nt")){
            p.text("Placing:", 1100, 460);
            p.fill(75);
            p.text("Not", 1100, 500);
            p.text("Traversable", 1100, 540);
        }
        if (place.equals("t")){
            p.text("Placing:", 1100, 460);
            p.fill(255);
            p.text("Traversable", 1100, 500);
        }
        if (place.equals("s")){
            p.text("Moving:", 1100, 460);
            p.fill(0,0,255);
            p.text("Start", 1100, 500);
        }
        if (place.equals("e")){
            p.text("Placing:", 1100, 460);
            p.fill(255,0,0);
            p.text("End", 1100, 500);
        }
        if (place.equals("mp")){
            p.text("Adding:", 1100, 460);
            p.fill(175);
            p.text("+MP", 1100, 500);
        }
        if (place.equals("mmp")){
            p.text("Setting:", 1100, 460);
            p.fill(125);
            p.text("MP = 205", 1100, 500);
        }
        if (place.equals("nmp")){
            p.text("Setting:", 1100, 460);
            p.fill(255);
            p.text("MP = 0", 1100, 500);
        }
        if (place.equals("mmmp")){
            p.text("Setting:", 1100, 460);
            p.fill(75);
            p.text("MP = 2005", 1100, 500);
        }
        p.fill(255);
        p.text((int) (p.frameRate) + " FPS", 1100, 700);
        p.text(Processing_AI_Test.seekers.size() + " Seekers", 1100, 740);
    }
}
