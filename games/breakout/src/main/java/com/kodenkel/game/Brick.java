package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.concurrent.ThreadLocalRandom;

import com.kodenkel.game.Ball;

public class Brick {
    public static final int WIDTH = 60;
    public static final int WIDTH_HALF = WIDTH / 2;
    public static final int HEIGHT = 10;
    public static final int HEIGHT_HALF = HEIGHT / 2;
    public static final int MARGIN_HORIZONTAL = 20;
    public static final int MARGIN_VERTICAL = 10;

    public enum BrickType {
        RED(ColorFromHSV(new Vector3(0.4f, 1.0f, 0.62f)), 12),
        ORANGE(ColorFromHSV(new Vector3(39.0f, 1.0f, 0.78f)), 6),
        GREEN(ColorFromHSV(new Vector3(138.0f, 1.0f, 0.5f)), 3),
        YELLOW(ColorFromHSV(new Vector3(60.0f, 0.8f, 0.78f)), 1);

        private final Color color;
        private final int score;

        private BrickType(Color color, int score) {
            this.color = color;
            this.score = score;
        }

        public Color getColor() {
            return this.color;
        }

        public int getScore() {
            return this.score;
        }
    }

    private BrickType type;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private boolean visible = true;

    public Brick(int x1, int y1, BrickType type) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1 + WIDTH;
        this.y2 = y1 + HEIGHT;
    }

    public int tick(Ball ball) {
        if (!this.visible) return 0;

        if (this.withinBounds(ball)) {
            this.visible = false;
            return this.type.getScore();
        }

        DrawRectangle(this.x1, this.y1, WIDTH, HEIGHT, this.type.getColor());
        return 0;
    }

    public boolean withinBounds(Ball ball) {
        int ballX1 = ball.getPosX() - Ball.RADIUS;
        int ballX2 = ball.getPosX() + Ball.RADIUS;
        int ballY1 = ball.getPosY() - Ball.RADIUS;
        int ballY2 = ball.getPosY() + Ball.RADIUS;

        if ((ballX1 > this.x2) || (ballX2 < this.x1) || (ballY1 > this.y2) || (ballY2 < this.y1)) {
            return false;
        }
        ball.bounceBack(ThreadLocalRandom.current().nextInt(0, 2) == 0 ? -1 : 1);
        return true;
    }

    public boolean isVisible() {
        return this.visible;
    }
}