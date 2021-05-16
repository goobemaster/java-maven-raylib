package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.util.ArrayList;
import java.time.ZonedDateTime;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.ResourceLoader;

public class ScoreScreen extends BaseScreen {
    private static final Color TEXT_COLOR = ColorFromHSV(new Vector3(0.0f, 6.1f, 0.37f));
    private static final int TEXT_SIZE = 22;

    private ResourceLoader resource;
    private Texture2D background;
    private int newHighScorePosition;
    private ArrayList<String[]> scores;
    private long displayedAt;

    public ScoreScreen(ResourceLoader resource) {
        super(resource);

        this.resource = resource;
        this.background = resource.getTexture("score_screen.png");
    }

    public boolean respondsTo(GameState state, GameData data) {
        return state.equals(GameState.SCORE);
    }

    public void tick(GameState state, GameData data) {
        DrawTextureEx(this.background, new Vector2(0, 0), 0.0f, 2.0f, WHITE);

        String[] row;
        int textYPos;
        for (int i = 0; i < scores.size(); i++) {
            row = this.scores.get(i);
            textYPos = (i * 24 + 83) * 2;
            DrawText(row[0], 18 * 2, textYPos, TEXT_SIZE, i == this.newHighScorePosition ? WHITE : TEXT_COLOR);
            DrawText(row[1], 155 * 2, textYPos, TEXT_SIZE, i == this.newHighScorePosition ? WHITE : TEXT_COLOR);
            DrawText(row[2], 284 * 2, textYPos, TEXT_SIZE, i == this.newHighScorePosition ? WHITE : TEXT_COLOR);
        }

        if (ZonedDateTime.now().toInstant().toEpochMilli() > this.displayedAt + 2000 && IsKeyPressed(KEY_ENTER)) {
            Application.changeState(GameState.TITLE);
        }
    }

    public void onEnter(GameData data) {
        this.newHighScorePosition = -1;
        this.scores = this.resource.getHighScores();

        for (int i = 0; i < this.scores.size(); i++) {
            if (i < this.scores.size() && Integer.valueOf(this.scores.get(i)[1]) < data.score) {
                this.newHighScorePosition = i;
                this.scores.add(i, new String[]{System.getProperty("user.name"), String.valueOf(data.score), String.valueOf(data.mission)});
                break;
            }
        }
        if (this.newHighScorePosition != -1) {
            this.scores.remove(5);
            this.scores.trimToSize();
        }

        this.resource.saveHighScores(this.scores);
        this.displayedAt = ZonedDateTime.now().toInstant().toEpochMilli();
    }

    public void onLeave(GameData data) {
        // Nothing to do.
    }
}
