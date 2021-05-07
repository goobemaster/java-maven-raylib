package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Ground {
    private static final int ELEVATION_BREAKPOINTS_MIN = 10;
    private static final int ELEVATION_BREAKPOINTS_MAX = 25;
    private static int ELEVATION_MIN, ELEVATION_MAX;

    private final int windowWidth, windowHeight;
    private ArrayList<Integer> elevation = new ArrayList<Integer>();

    public Ground(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        ELEVATION_MIN = 50;
        ELEVATION_MAX = windowHeight - 100;

        // Generating breakpoints for elevation map transitions
        int numBreakPoints = ThreadLocalRandom.current().nextInt(ELEVATION_BREAKPOINTS_MIN, ELEVATION_BREAKPOINTS_MAX);
        int breakPointDistanceMin = windowWidth / ELEVATION_BREAKPOINTS_MAX / 4;
        ArrayList<Integer> breakPoints = new ArrayList<Integer>();
        breakPoints.add(0);
        int breakPointX, lastBreakPointX;
        while (breakPoints.size() != numBreakPoints) {
            breakPointX = ThreadLocalRandom.current().nextInt(ELEVATION_MIN, ELEVATION_MAX);
            breakPoints.add(breakPointX);
            Collections.sort(breakPoints);
            lastBreakPointX = 0;
            for (int i = 0; i < breakPoints.size(); i++) {
                if (i > 0 && breakPoints.get(i) - lastBreakPointX < breakPointDistanceMin) {
                    breakPoints.remove(i);
                    break;
                }
                lastBreakPointX = breakPoints.get(i);
            }
        }
        Collections.sort(breakPoints);
        breakPoints.set(numBreakPoints - 1, this.windowWidth);

        // Generating elevation map
        int terrainType;
        int currentElevation = ELEVATION_MAX / 2; //ThreadLocalRandom.current().nextInt(ELEVATION_MIN, ELEVATION_MAX);
        for (int i = 0; i < numBreakPoints - 1; i++) {
            terrainType = ThreadLocalRandom.current().nextInt(0, 3);
            for (int x = breakPoints.get(i); x < breakPoints.get(i + 1); x++) {
                switch (terrainType) {
                    case 1: // Upward
                        currentElevation += ThreadLocalRandom.current().nextInt(0, 3);
                        break;
                    case 2: // Downward
                        currentElevation -= ThreadLocalRandom.current().nextInt(0, 3);
                        break;
                    case 3: // Random
                        currentElevation += ThreadLocalRandom.current().nextInt(0, 1) == 0 ? ThreadLocalRandom.current().nextInt(0, 2) : -ThreadLocalRandom.current().nextInt(0, 2);
                        break;
                    default: // Flat, nothing to do
                }
                if (currentElevation > ELEVATION_MAX) currentElevation = ELEVATION_MAX;
                if (currentElevation < ELEVATION_MIN) currentElevation = ELEVATION_MIN;
                this.elevation.add(currentElevation);
            }
        }
        this.elevation.add(currentElevation);
    }

    public void tick() {
        Color color = ColorFromHSV(new Vector3(0.0f, 0.0f, 0.0f));
        for (int x = 1; x < this.windowWidth + 1; x++) {
            DrawLine(x, this.windowHeight - this.elevation.get(x), x, this.windowHeight, color);
        }
    }

    public int getAbsoluteElevationAt(int posX) {
        return this.windowHeight - this.elevation.get(posX);
    }

    /**
     * Entirely not realistic, but fun :)
     */
    public void scorch(int posX, int posY, int radius) {
        int x1, y1;
        int x2, y2;
        int distance, distance2;

        for (int x = posX - radius; x < posX + radius; x++) {
            if (x < 0 || x > this.windowWidth) continue;

            x1 = x;
            y1 = posY;
            x2 = posX;
            y2 = posY;

            distance = (int) (Math.sqrt((Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2))) - radius);
            this.modElevation(x, distance, radius);
        }
    }

    private void modElevation(int x, int mod, int radius) {
        int y = this.elevation.get(x) + mod;
        if (y > this.elevation.get(x) + radius) y = this.elevation.get(x) + radius;
        this.setElevation(x, y);
    }

    private void setElevation(int x, int y) {
        if (y < 0) y = 0;
        if (y > this.windowHeight) y = this.windowHeight;
        this.elevation.set(x, y);
    }
}