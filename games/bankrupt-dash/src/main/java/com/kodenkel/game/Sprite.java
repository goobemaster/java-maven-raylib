package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.time.ZonedDateTime;

public abstract class Sprite {
    private ResourceLoader resource;
    private String textureFileName;
    private int totalFrames;

    private int posX;
    private int posY;
    private float rotation;
    private float scale;
    private Color tint;
    private int currentFrame;
    private boolean hidden;

    private int animFrameFrom;
    private int animFrameTo;
    private long animSpeed;
    private long animLastUpdate;
    private int animRepeated;

    public Sprite(String textureFileName, int totalFrames, ResourceLoader resource) {
        this.textureFileName = textureFileName;
        this.totalFrames = totalFrames;
        this.resource = resource;

        this.posX = 0;
        this.posY = 0;
        this.rotation = 0.0f;
        this.scale = 1.0f;
        this.tint = WHITE;
        this.currentFrame = 1;
        this.hidden = false;

        this.animFrameFrom = 1;
        this.animFrameTo = 1;
        this.animSpeed = 0;
        this.animLastUpdate = 0;
        this.animRepeated = 0;
    }

    public void tick() {
        if (this.hidden) return;

        DrawTextureEx(
            this.resource.getTexture(String.format(this.textureFileName, String.valueOf(this.currentFrame))),
            new Vector2(this.posX, this.posY),
            this.rotation,
            this.scale,
            this.tint
        );

        // Animation
        if (this.animSpeed > 0 && this.animFrameFrom >= 1 && this.animFrameTo <= this.totalFrames) {
            long nowMillis = ZonedDateTime.now().toInstant().toEpochMilli();
            if (this.animSpeed + this.animLastUpdate < nowMillis) {
                if (this.currentFrame + 1 <= this.animFrameTo) {
                    this.currentFrame += 1;
                } else {
                    this.currentFrame = this.animFrameFrom;
                    this.animRepeated += 1;
                }
                this.animLastUpdate = nowMillis;
            }
        }
    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setPosX(int posX) { this.posX = posX; }

    public void setPosY(int posY) { this.posY = posY; }

    public void setRotation(float rotation) {
        if (rotation < 0) rotation = 360 + rotation;
        if (rotation > 360) rotation = rotation - 360;
        this.rotation = rotation;
    }

    public void setScale(float scale) {
        if (scale < 0) scale = 0;
        this.scale = scale;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

    public void loopFrames(int frameFrom, int frameTo, int speedMillis) {
        if (frameFrom < 1) frameFrom = 1;
        if (frameTo > this.totalFrames) frameTo = this.totalFrames;
        if (speedMillis < 1) speedMillis = 1;

        this.animFrameFrom = frameFrom;
        this.animFrameTo = frameTo;
        this.animSpeed = (long) speedMillis;
        this.animLastUpdate = ZonedDateTime.now().toInstant().toEpochMilli();
        this.currentFrame = frameFrom;
        this.animRepeated = 0;
    }

    public boolean isLooping() {
        return this.animSpeed != 0;
    }

    public void setFrame(int frame) {
        if (frame < 1) frame = 1;
        if (frame > this.totalFrames) frame = this.totalFrames;
        this.currentFrame = frame;
        this.animRepeated = 0;
        this.animSpeed = 0;
    }

    public boolean animRepeated(int times) {
        return this.animRepeated >= times;
    }

    public boolean animRepeatedExactly(int times) {
        return this.animRepeated == times;
    }

    public void hide() {
        this.hidden = true;
    }

    public void show() {
        this.hidden = false;
    }

    public boolean isHidden() {
        return this.hidden == true;
    }
}
