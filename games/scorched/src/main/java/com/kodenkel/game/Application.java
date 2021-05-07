package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.concurrent.ThreadLocalRandom;
import static com.kodenkel.game.Tank.TankColor;

public class Application {

    public enum GAME_STATE { SETUP, TURN, MISSILE, END }

    private static final int LOCKED_FPS = 60;
    public static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int TANK_POS_MIN_INDENT = WINDOW_WIDTH / 14;
    private static final int TANK_POS_MAX_INDENT = TANK_POS_MIN_INDENT + WINDOW_WIDTH / 9;
    private static final Color TEXT_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f));

    public static void main(String args[]) throws Exception {
        InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Scorched - kodenkel.com");
        SetTargetFPS(LOCKED_FPS);

        Ground ground = new Ground(WINDOW_WIDTH, WINDOW_HEIGHT);
        Tank player1 = new Tank(ThreadLocalRandom.current().nextInt(TANK_POS_MIN_INDENT, TANK_POS_MAX_INDENT), ground, 45.0f, TankColor.GREEN, 100);
        Tank player2 = new Tank(ThreadLocalRandom.current().nextInt(WINDOW_WIDTH - TANK_POS_MAX_INDENT, WINDOW_WIDTH - TANK_POS_MIN_INDENT), ground, -45.0f, TankColor.BROWN, 100);
        int gamePlayer = 1;
        Missile missile = null;
        GAME_STATE gameState = GAME_STATE.TURN;

        while(!WindowShouldClose()){
            if (gameState.equals(GAME_STATE.SETUP)) {
                ground = new Ground(WINDOW_WIDTH, WINDOW_HEIGHT);
                player1 = new Tank(ThreadLocalRandom.current().nextInt(TANK_POS_MIN_INDENT, TANK_POS_MAX_INDENT), ground, 45.0f, TankColor.GREEN, 100);
                player2 = new Tank(ThreadLocalRandom.current().nextInt(WINDOW_WIDTH - TANK_POS_MAX_INDENT, WINDOW_WIDTH - TANK_POS_MIN_INDENT), ground, -45.0f, TankColor.BROWN, 100);
                gamePlayer = 1;
                missile = null;
                gameState = GAME_STATE.TURN;
            }

            BeginDrawing();

            Horizon.tick(WINDOW_WIDTH, WINDOW_HEIGHT);
            ground.tick();
            if (player1.tick(gameState, gamePlayer == 1, ground)) {
                gameState = GAME_STATE.MISSILE;
                missile = new Missile(player1.getMissileOriginX(), player1.getMissileOriginY(), player1.getAngle(), player1.getPower());
            }
            if (player2.tick(gameState, gamePlayer == 2, ground)) {
                gameState = GAME_STATE.MISSILE;
                missile = new Missile(player2.getMissileOriginX(), player2.getMissileOriginY(), player2.getAngle(), player2.getPower());
            }

            if (gameState.equals(GAME_STATE.MISSILE)) {
                if (missile.tick(player1, player2, ground, LOCKED_FPS, WINDOW_WIDTH)) {
                    missile = null;
                    gameState = GAME_STATE.TURN;
                    gamePlayer = gamePlayer == 1 ? 2 : 1;
                }
            }

            if (player1.isDestroyed() || player2.isDestroyed()) {
                gameState = GAME_STATE.END;
                DrawText("The " + (player1.isDestroyed() ? player2.getColor().toString() : player1.getColor().toString()) + " tank is victorious!", 10, 10, 40, TEXT_COLOR);
                DrawText("Press [ENTER] to restart", 10, 55, 20, TEXT_COLOR);

                if (IsKeyDown(KEY_ENTER)) {
                    gameState = GAME_STATE.SETUP;
                }
            }

            EndDrawing();
        }
        Raylib.CloseWindow();
    }
}
