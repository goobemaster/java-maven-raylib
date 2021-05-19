package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameScreen;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.ResourceLoader;

public abstract class BaseScreen implements GameScreen {
    ResourceLoader resource;
    private boolean volumeControlAvailable;

    public BaseScreen(ResourceLoader resource) {
        this.resource = resource;
    }

    public boolean respondsTo(GameState state, GameData data) { return true; }
    public void tick(GameState state, GameData data) {}
    public void onLeave(GameData data) {}
    public void onEnter(GameData data) {}

    public final void tickCommon(GameState state, GameData data) {
        // Redirect to Title on boot
        if (state.equals(GameState.BOOT)) Application.changeState(GameState.TITLE);

        // Sound
        if (!this.volumeControlAvailable) return;
        if (IsKeyPressed(KEY_F1)) data.toggleSound();
        FloatControl volume;
        // TODO: test it on Win and Mac. May break (ALSA requires different value).
        Float setVolume = data.sound ? 50000.0f : 0.0f;
        for (Clip clip : this.resource.getAllSound().values()) {
            try {
                volume = (FloatControl) clip.getControl(Type.VOLUME);
                volume.setValue(setVolume);
                this.volumeControlAvailable = true;
            } catch (Exception e) {
                e.printStackTrace();
                this.volumeControlAvailable = false;
            }
        }
    }
}