package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Application {
    public static final boolean DEBUG = false;
    private static final int LOCKED_FPS = 60;
    private static final Color BACKGROUND_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 0.0f));
    public static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color SCORE_COLOR = ColorFromHSV(new Vector3(1.0f, 1.0f, 1.0f));
    private static final Color NAME_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f));

    public enum Player { HUMAN, COMPUTER }
    public enum Side { LEFT, RIGHT }

    public static void main(String args[]) {
        InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Pong - kodenkel.com");
        SetTargetFPS(LOCKED_FPS);

        Pad playerHuman = new Pad(Player.HUMAN, Side.LEFT, WINDOW_WIDTH, WINDOW_HEIGHT);
        Pad playerComputer = new Pad(Player.COMPUTER, Side.RIGHT, WINDOW_WIDTH, WINDOW_HEIGHT);
        Ball ball = new Ball(WINDOW_WIDTH, WINDOW_HEIGHT);
        int scoreLeftX = WINDOW_WIDTH / 4;
        int nameLeftX = scoreLeftX - 20;
        int scoreRightX = (int) (WINDOW_WIDTH * 0.75);
        int nameRightX = scoreRightX - 40;
        int scoreY = 30;
        int nameY = 10;

        while(!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(BACKGROUND_COLOR);

            // Pads & Ball
            playerHuman.tick(ball);
            playerComputer.tick(ball);
            ball.tick(playerHuman, playerComputer);

            // Goalkeeping
            if (ball.inMarginOf(playerHuman)) {
                playerComputer.incrementScore();
                ball.reset();
            } else if (ball.inMarginOf(playerComputer)) {
                playerHuman.incrementScore();
                ball.reset();
            }

            // Score Display
            DrawText(playerHuman.getScore(), scoreLeftX, scoreY, 20, SCORE_COLOR);
            DrawText(playerHuman.getName(), nameLeftX, nameY, 20, NAME_COLOR);
            DrawText(playerComputer.getScore(), scoreRightX, scoreY, 20, SCORE_COLOR);
            DrawText(playerComputer.getName(), nameRightX, nameY, 20, NAME_COLOR);

            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
