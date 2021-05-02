package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;
import java.util.concurrent.ThreadLocalRandom;

import com.kodenkel.game.Application.Side;

public class Ball {
    private static final int RADIUS = 16;
    private static final int RADIUS_HALF = RADIUS / 2;
    private static final Color COLOR = ColorFromHSV(new Vector3(50.0f, 90.0f, 0.9f));
    private static final int SPEED_MIN = 2;
    private static final int SPEED_MAX = 8;
    private static final int SPEED_DEFAULT = (SPEED_MAX - SPEED_MIN) / 2;

    private final int windowWidth;
    private final int windowHeight;
    private int posX;
    private int posY;
    private int speedX;
    private int speedY;

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

    public void tick(Pad player1, Pad player2) {
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

        HitPoint hitPointPlayer1 = this.hitPoint(player1);
        HitPoint hitPointPlayer2 = this.hitPoint(player2);
        if (hitPointPlayer1.absolute > 0 || hitPointPlayer2.absolute > 0) {
            this.speedX = this.speedX < 0 ? Math.abs(this.speedX) : -this.speedX;
        }

        if (Application.DEBUG) {
            DrawText(String.valueOf(hitPointPlayer1.absolute) + " , " + String.valueOf(hitPointPlayer1.relative), 100, 100, 20, ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f)));
            DrawText(String.valueOf(hitPointPlayer2.absolute) + " , " + String.valueOf(hitPointPlayer2.relative), 100, 200, 20, ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f)));
            DrawText(String.valueOf(this.speedX), 100, 300, 20, ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f)));
        }

        DrawCircle(this.posX, this.posY, RADIUS, COLOR);
    }

    public int getPosY() {
        return this.posY;
    }

    public void reset() {
        this.posX = windowWidth / 2;
        this.posY = windowHeight / 2;
        this.speedX = ThreadLocalRandom.current().nextInt(0, 2) == 0 ? SPEED_DEFAULT : -SPEED_DEFAULT;
        this.speedY = ThreadLocalRandom.current().nextInt(0, SPEED_DEFAULT);
    }

    public boolean inMarginOf(Pad player) {
        if (player.getSide().equals(Side.LEFT)) {
            return this.posX < Pad.MARGIN + Pad.PAD_WIDTH - RADIUS_HALF;
        }
        return this.posX > Application.WINDOW_WIDTH - Pad.MARGIN - Pad.PAD_WIDTH + RADIUS_HALF;
    }

    private HitPoint hitPoint(Pad player) {
        HitPoint hitPoint = new HitPoint(0, 0);

        if (this.posY < player.getPosY() - Pad.PAD_HEIGHT_HALF - RADIUS_HALF ||
            this.posY > player.getPosY() + Pad.PAD_HEIGHT_HALF + RADIUS_HALF) {
            return hitPoint;
        }

        if (player.getSide().equals(Side.LEFT)) {
            if (this.posX - RADIUS_HALF < player.getX2() + (Pad.PAD_WIDTH / 2)) {
                hitPoint.absolute = this.posY;
            }
        } else {
            if (this.posX + RADIUS_HALF > player.getX1() - (Pad.PAD_WIDTH / 2)) {
                hitPoint.absolute = this.posY;
            }
        }
        hitPoint.relative = this.posY < player.getPosY() ? -(player.getPosY() - this.posY) : this.posY - player.getPosY();

        return hitPoint;
    }
}