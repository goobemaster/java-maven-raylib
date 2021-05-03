package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ball {
    public static final int RADIUS = 8;
    public static final int RADIUS_HALF = RADIUS / 2;
    private static final Color COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 81.0f));
    private static final int SPEED_MIN_DEFAULT = 3;
    private static final int SPEED_MAX_DEFAULT = 12;
    private static final int SPEED_DEFAULT = 3;

    private final int windowWidth;
    private final int windowHeight;
    private int posX;
    private int posY;
    private int speedX;
    private int speedY;
    private int speedMin;
    private int speedMax;

    private class HitPoint {
        public int absolute;
        public int relative;

        public HitPoint(int absolute, int relative) {
            this.absolute = absolute;
            this.relative = relative;
        }
    }

    public Ball(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.reset();
    }

    public void tick(Pad player) {
        this.posX += this.speedX;
        this.posY += this.speedY;

        if (this.posX - RADIUS < 0) {
            this.speedX = -this.speedX;
            this.posX = RADIUS;
        } else if (this.posX + RADIUS > windowWidth) {
            this.speedX = -this.speedX;
            this.posX = windowWidth - RADIUS;
        }

        if (this.posY - RADIUS < 0) {
            this.speedY = -this.speedY;
            this.posY = RADIUS;
        } else if (this.posY + RADIUS > windowHeight) {
            this.speedY = -this.speedY;
            this.posY = windowHeight - RADIUS;
        }

        HitPoint hitPointPlayer = this.hitPoint(player);
        if (hitPointPlayer.absolute > 0) {
            this.bounceBack(hitPointPlayer.relative);
        }

        DrawCircle(this.posX, this.posY, RADIUS, COLOR);
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void reset() {
        this.posX = windowWidth / 2;
        this.posY = windowHeight / 2;
        this.speedX = ThreadLocalRandom.current().nextInt(0, 2) == 0 ? SPEED_DEFAULT : -SPEED_DEFAULT;
        //this.speedY = ThreadLocalRandom.current().nextInt(SPEED_MIN_DEFAULT, SPEED_MAX_DEFAULT);
        this.speedY = SPEED_MIN_DEFAULT;
        this.speedMin = SPEED_MIN_DEFAULT;
        this.speedMax = SPEED_MAX_DEFAULT;
    }

    public boolean hitScreenBottom() {
        return this.posY >= this.windowHeight - RADIUS;
    }

    private HitPoint hitPoint(Pad player) {
        HitPoint hitPoint = new HitPoint(0, 0);

        int padWidthHalf = player.getWidthHalf();
        if (this.posX < player.getPosX() - padWidthHalf - RADIUS_HALF ||
            this.posX > player.getPosX() + padWidthHalf + RADIUS_HALF) {
            return hitPoint;
        }

        if (this.posY - RADIUS_HALF > player.getTopPos() - RADIUS - RADIUS_HALF) {
            hitPoint.absolute = this.posX;
        }

        hitPoint.relative = this.posX < player.getPosX() ? -(player.getPosX() - this.posY) : this.posX - player.getPosX();

        return hitPoint;
    }

    public void adjustSpeedY(int mod) {
        if (this.speedMin != SPEED_MIN_DEFAULT + mod) this.speedMin = SPEED_MIN_DEFAULT + mod;
        if (this.speedMax != SPEED_MAX_DEFAULT + mod) this.speedMax = SPEED_MAX_DEFAULT + mod;
    }

    public void bounceBack(int relative) {
        this.speedY = this.speedY < 0 ? Math.abs(this.speedY) : -this.speedY;
        if (this.speedY < 0) {
            if (this.speedY > -this.speedMin) this.speedY = -ThreadLocalRandom.current().nextInt(this.speedMin, this.speedMax);
        } else {
            if (this.speedY < this.speedMin) this.speedY = ThreadLocalRandom.current().nextInt(this.speedMin, this.speedMax);
        }

        if (relative == 0) return;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
            int altXMin = 0;
            int altXMax = 2;
            if (this.speedX < 0) {
                if (relative < 0) {
                    this.speedX += ThreadLocalRandom.current().nextInt(altXMin, altXMax);
                } else {
                    this.speedX += -ThreadLocalRandom.current().nextInt(altXMin, altXMax);
                }
            } else {
                if (relative < 0) {
                    this.speedX += -ThreadLocalRandom.current().nextInt(altXMin, altXMax);
                } else {
                    this.speedX += ThreadLocalRandom.current().nextInt(altXMin, altXMax);
                }
            }
        }
    }
}