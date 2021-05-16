package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import javax.sound.sampled.Clip;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.ResourceLoader;

public class TitleScreen extends BaseScreen {
    private static final Color LINE_COLOR = ColorFromHSV(new Vector3(0.0f, 6.1f, 0.37f));
    private Texture2D background;
    private Clip musicLoop;
    private int selected;

    public TitleScreen(ResourceLoader resource) {
        super(resource);

        this.background = resource.getTexture("title_screen.png");
        this.musicLoop = resource.getSound("BankruptDashTitleMusic.wav");
    }

    public boolean respondsTo(GameState state, GameData data) {
        return state.equals(GameState.TITLE) || state.equals(GameState.RULES);
    }

    public void tick(GameState state, GameData data) {
        if (!state.equals(GameState.TITLE)) return;

        DrawTextureEx(this.background, new Vector2(0, 0), 0.0f, 2.0f, WHITE);

        if (this.selected == 0) {
            DrawRectangle(65 * 2, 200 * 2, 71 * 2, 6, LINE_COLOR);
            if (IsKeyPressed(KEY_RIGHT)) selected = 1;
            if (IsKeyPressed(KEY_ENTER)) {
                data.score = 0;
                data.mission = 1;
                Application.changeState(GameState.NEXT_MISSION);
            }
        } else {
            DrawRectangle(208 * 2, 200 * 2, 77 * 2, 6, LINE_COLOR);
            if (IsKeyPressed(KEY_LEFT)) selected = 0;
            if (IsKeyPressed(KEY_ENTER)) {
                Application.changeState(GameState.SCORE);
            }
        }

        if (IsKeyPressed(KEY_F2)) {
            Application.changeState(GameState.RULES);
        }
    }

    public void onEnter(GameData data) {
        this.musicLoop.loop(Clip.LOOP_CONTINUOUSLY);
        this.selected = 0;
        data.mission = 1;
        data.score = 0;
    }

    public void onLeave(GameData data) {
        if (!Application.isGameState(GameState.RULES)) this.musicLoop.stop();
    }
}