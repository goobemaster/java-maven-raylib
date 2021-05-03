package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.ArrayList;
import java.util.Timer; 
import java.util.TimerTask; 

import com.kodenkel.game.Brick.BrickType;

public class Application {
    public static final boolean DEBUG = false;
    private static final int LOCKED_FPS = 60;
    private static final int COOLDOWN_PERIOD = LOCKED_FPS * 5;
    private static final Color BACKGROUND_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 0.0f));
    private static final Color TEXT_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f));
    public static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BRICK_MARGIN_TOP = 60;
    private static int SPEED_MOD = 0;

    public static void main(String args[]) {
        InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Breakout - kodenkel.com");
        SetTargetFPS(LOCKED_FPS);

        Pad player = new Pad(WINDOW_HEIGHT - 50, WINDOW_WIDTH / 16, 16, WINDOW_WIDTH);
        Ball ball = new Ball(WINDOW_WIDTH, WINDOW_HEIGHT);
        int tries = 1;
        int score = 0;
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 10; x++) {
                if (y >= 0 && y <= 1) {
                    bricks.add(new Brick((Brick.WIDTH + Brick.MARGIN_HORIZONTAL) * x + 10, (Brick.HEIGHT + Brick.MARGIN_VERTICAL) * y + BRICK_MARGIN_TOP, BrickType.RED));
                } else if (y > 1 && y <= 3) {
                    bricks.add(new Brick((Brick.WIDTH + Brick.MARGIN_HORIZONTAL) * x + 10, (Brick.HEIGHT + Brick.MARGIN_VERTICAL) * y + BRICK_MARGIN_TOP, BrickType.ORANGE));
                } else if (y > 3 && y <= 5) {
                    bricks.add(new Brick((Brick.WIDTH + Brick.MARGIN_HORIZONTAL) * x + 10, (Brick.HEIGHT + Brick.MARGIN_VERTICAL) * y + BRICK_MARGIN_TOP, BrickType.GREEN));
                } else {
                    bricks.add(new Brick((Brick.WIDTH + Brick.MARGIN_HORIZONTAL) * x + 10, (Brick.HEIGHT + Brick.MARGIN_VERTICAL) * y + BRICK_MARGIN_TOP, BrickType.YELLOW));
                }
            }
        }
        long bricksLeft;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                Application.SPEED_MOD += 1;
            }
        }, 3000, 8000);

        while(!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(BACKGROUND_COLOR);

            bricksLeft = bricks.stream()
                .filter((brick) -> brick.isVisible())
                .count();

            if (bricksLeft == 0) {
                DrawText("Congrats, you cleared the board!" , WINDOW_WIDTH / 2 - (13 * 15), WINDOW_HEIGHT / 2, 20, TEXT_COLOR);
            }

            for (int i = 0; i < bricks.size(); i++) {
                score += bricks.get(i).tick(ball);
            }

            player.tick();

            ball.tick(player);
            ball.adjustSpeedY(SPEED_MOD);

            if (bricksLeft > 0 && ball.hitScreenBottom()) {
                tries += 1;
                ball.reset();
                SPEED_MOD = 0;
            }

            // Tries & Score Display
            DrawText(String.valueOf(tries), 50, 20, 20, TEXT_COLOR);
            DrawText(String.valueOf(score), WINDOW_WIDTH - 200, 20, 20, TEXT_COLOR);

            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
