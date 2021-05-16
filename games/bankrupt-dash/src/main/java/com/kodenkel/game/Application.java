package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.ArrayList;

import com.kodenkel.game.screen.*;

public class Application {
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final int LOCKED_FPS = 60;
    private static final String JAR_PATH = System.getProperty("user.dir") + "/src/main/resources/";
    public static final ResourceLoader RESOURCE_LOADER = new ResourceLoader(JAR_PATH);

    private static GameState STATE;
    private static GameData DATA;
    private static ArrayList<BaseScreen> SCREENS;

    public static void main(String args[]) throws Exception {
        InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Bankrupt Dash - kodenkel.com");
        SetTargetFPS(LOCKED_FPS);

        STATE = GameState.BOOT;
        DATA = new GameData();
        SCREENS = new ArrayList<BaseScreen>();
        SCREENS.add(new TitleScreen(RESOURCE_LOADER));
        SCREENS.add(new RulesScreen(RESOURCE_LOADER));
        SCREENS.add(new MissionScreen(RESOURCE_LOADER));
        SCREENS.add(new MapScreen(RESOURCE_LOADER));
        SCREENS.add(new ScoreScreen(RESOURCE_LOADER));

        while (!WindowShouldClose()) {
            BeginDrawing();

            for (BaseScreen manager : SCREENS) {
                if (manager.respondsTo(STATE, DATA)) manager.tick(STATE, DATA);
            }
            SCREENS.get(0).tickCommon(STATE, DATA);

            EndDrawing();
        }

        RESOURCE_LOADER.free();
        Raylib.CloseWindow();
    }

    public static void changeState(GameState newGameState) {
        for (BaseScreen manager : SCREENS) {
            if (manager.respondsTo(STATE, DATA) && !manager.respondsTo(newGameState, DATA)) {
                manager.onLeave(DATA);
            }
            if (!manager.respondsTo(STATE, DATA) && manager.respondsTo(newGameState, DATA)) {
                manager.onEnter(DATA);
            }
        }
        STATE = newGameState;
    }

    public static boolean isGameState(GameState state) {
        return STATE.equals(state);
    }
}
