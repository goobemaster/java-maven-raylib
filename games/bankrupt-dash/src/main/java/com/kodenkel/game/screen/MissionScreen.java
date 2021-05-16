package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.Timer;
import java.util.TimerTask;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.ResourceLoader;

public class MissionScreen extends BaseScreen {
    private static final Color TEXT_COLOR = ColorFromHSV(new Vector3(0.0f, 6.1f, 0.37f));
    private static final int TEXT_SIZE = 36;
    private Texture2D background;
    private Texture2D player;
    private String text;
    private Timer delayTimer;

    public MissionScreen(ResourceLoader resource) {
        super(resource);

        this.background = resource.getTexture("title_screen.png");
        this.player = resource.getTexture("player_1.png");
    }

    public boolean respondsTo(GameState state, GameData data) {
        return state.equals(GameState.NEXT_MISSION);
    }

    public void tick(GameState state, GameData data) {
        DrawTextureEx(this.background, new Vector2(0, 0), 0.0f, 2.0f, WHITE);
        DrawRectangle(0, 0, 320 * 2, 55 * 2, BLACK);
        DrawRectangle(0, 165 * 2, 320 * 2, 75 * 2, BLACK);
        DrawTextureEx(this.player, new Vector2(296, 30), 0.0f, 2.0f, WHITE);

        this.text = "Mission #" + String.valueOf(data.mission);
        DrawText(this.text, 320 - MeasureText(this.text, TEXT_SIZE) / 2, 180 * 2, TEXT_SIZE, TEXT_COLOR);
    }

    public void onEnter(GameData data) {
        this.delayTimer = new Timer("StartDelay");
        this.delayTimer.schedule(new TimerTask() {
            public void run() {
                Application.changeState(GameState.TELEPORT_IN);
            }
        }, 2000L);
    }

    public void onLeave(GameData data) {
        // Nothing to do.
    }
}
