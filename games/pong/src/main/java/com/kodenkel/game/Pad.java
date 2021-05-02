package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import com.kodenkel.game.Application.Player;
import com.kodenkel.game.Application.Side;
import com.kodenkel.game.Ball;

public class Pad {
    public static final int MARGIN = 10;
    public static final int PAD_WIDTH = 20;
    private static final int PAD_HEIGHT = 80;
    public static final int PAD_HEIGHT_HALF = PAD_HEIGHT / 2;
    private static final Color PAD_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f));

    private final Player player;
    private final Side side;
    private final int screenHeight;
    private final int x1;
    private final int x2;
    private int score = 0;
    private int posY;
    private int defaultSpeed;

    public Pad(Player player, Side side, int screenWidth, int screenHeight) {
        this.player = player;
        this.side = side;
        this.screenHeight = screenHeight;
        this.posY = screenHeight / 2;
        this.x1 = side.equals(Side.LEFT) ? MARGIN : screenWidth - MARGIN - PAD_WIDTH;
        this.x2 = side.equals(Side.LEFT) ? MARGIN + PAD_WIDTH : screenWidth - MARGIN;
        this.defaultSpeed = screenHeight / 300;
        if (this.defaultSpeed < 1) this.defaultSpeed = 1;
    }

    public void tick(Ball ball) {
        DrawRectangle(this.x1, this.posY - PAD_HEIGHT_HALF, PAD_WIDTH, PAD_HEIGHT, PAD_COLOR);

        if (this.player.equals(Player.COMPUTER)) {
            this.tickComputer(ball);
            return;
        }

        if (IsKeyDown(KEY_UP)) {
            this.updateY(this.posY - this.defaultSpeed);
        } else if (IsKeyDown(KEY_DOWN)) {
            this.updateY(this.posY + this.defaultSpeed);
        }
    }

    private void tickComputer(Ball ball) {
        this.updateY(ball.getPosY() < this.posY ? this.posY - this.defaultSpeed : this.posY + this.defaultSpeed);
    }

    public String getScore() {
        return String.valueOf(this.score);
    }

    public void incrementScore() {
        this.score += 1;
    }

    public String getName() {
        return this.player.toString();
    }

    private void updateY(int newYPos) {
        if (newYPos < MARGIN + PAD_HEIGHT_HALF || newYPos > screenHeight - PAD_HEIGHT_HALF - MARGIN) return;
        this.posY = newYPos;
    }

    public Side getSide() {
        return this.side;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getX1() {
        return this.x1;
    }

    public int getX2() {
        return this.x2;
    }
}