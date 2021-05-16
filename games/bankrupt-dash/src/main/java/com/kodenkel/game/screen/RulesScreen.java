package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.ResourceLoader;

public class RulesScreen extends BaseScreen {
    private Texture2D background;

    public RulesScreen(ResourceLoader resource) {
        super(resource);

        this.background = resource.getTexture("rules_screen.png");
    }

    public boolean respondsTo(GameState state, GameData data) {
        return state.equals(GameState.RULES);
    }

    public void tick(GameState state, GameData data) {
        DrawTextureEx(this.background, new Vector2(0, 0), 0.0f, 2.0f, WHITE);

        if (IsKeyPressed(KEY_ENTER)) {
            Application.changeState(GameState.TITLE);
        }
    }

    public void onEnter(GameData data) {
        // Nothing to do.
    }

    public void onLeave(GameData data) {
        // Nothing to do.
    }
}
