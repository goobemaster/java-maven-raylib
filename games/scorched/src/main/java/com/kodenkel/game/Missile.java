package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Missile {
    private static final float GRAVITY = 90.8f;
    private static final int RADIUS = 3;
    public static final int SCORCH_RADIUS = 50;
    private static final Color COLOR = ColorFromHSV(new Vector3(255.0f, 255.0f, 255.0f));

    private double posX, posY;
    private double originX, originY;
    private double velX, velY;
    private double timeFps;
    private double timeSec;

    public Missile(int originX, int originY, float angle, float power) {
        angle = this.modAngle(angle, 90.0f);

        this.velX = power * Math.cos(Math.PI / 180 * angle);
        this.velY = power * Math.sin(Math.PI / 180 * angle);

        this.originX = originX;
        this.originY = originY;
        this.posX = originX;
        this.posY = originY;

        this.timeFps = 0;
        this.timeSec = 0;
    }

    public boolean tick(Tank player1, Tank player2, Ground ground, int fps, int screenWidth) {
        this.timeFps += 1;
        this.timeSec = this.timeFps / (double) fps;

        double deltaX = this.velX * this.timeSec / 2.0f;
        double deltaY = this.velY * this.timeSec - (GRAVITY * this.timeSec * this.timeSec) / 2.0f;
        this.posX -= deltaX;
        this.posY -= deltaY;

        if ((int) this.posX < RADIUS || (int) this.posX > screenWidth - RADIUS) return true;
        if (ground.getAbsoluteElevationAt((int) this.posX) <= (int) this.posY) {
            // Calculating damage first
            int distancePlayer = (int) Math.sqrt((player1.getX() - this.posX) * (player1.getX() - this.posX) + (player1.getY() - this.posY) * (player1.getY() - this.posY));
            if (distancePlayer <= SCORCH_RADIUS * 2) {
                int damage = distancePlayer - SCORCH_RADIUS * 2;
                player1.takeDamage(-damage);
            }

            distancePlayer = (int) Math.sqrt((player2.getX() - this.posX) * (player2.getX() - this.posX) + (player2.getY() - this.posY) * (player2.getY() - this.posY));
            if (distancePlayer <= SCORCH_RADIUS * 2) {
                int damage = distancePlayer - SCORCH_RADIUS * 2;
                player2.takeDamage(-damage);
            }

            // Then scorch the ground
            ground.scorch((int) this.posX, (int) this.posY, SCORCH_RADIUS);
            return true;
        }

        DrawCircle((int) this.posX, (int) this.posY, RADIUS, COLOR);
        return false;
    }

    private float modAngle(float angle, float mod) {
        return this.absAngle(angle + mod);
    }

    private float absAngle(float angle) {
        if (angle < 0.0f) return 360.0f + angle;
        if (angle > 360.0f) return 360.0f - angle;
        return angle;
    }
}
