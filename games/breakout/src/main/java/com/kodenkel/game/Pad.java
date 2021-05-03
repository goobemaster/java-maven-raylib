package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Pad {
    private static final Color PAD_COLOR = ColorFromHSV(new Vector3(20.0f, 10.0f, 5.1f));

    private final int screenWidth;
    private final int width, widthHalf;
    private final int height, heightHalf;
    private int posX;
    private int posY;
    private int topPos;

    public Pad(int posY, int width, int height, int screenWidth) {
        this.posY = posY;
        this.width = width;
        this.widthHalf = width / 2;
        this.height = height;
        this.heightHalf = height / 2;
        this.screenWidth = screenWidth;
        this.topPos = posY - this.heightHalf;
        this.reset();
    }

    public void tick() {
        DrawRectangle(this.posX - widthHalf, this.posY - heightHalf, this.width, this.height, PAD_COLOR);

        int mouseX = GetMouseX();
        if (mouseX > this.widthHalf && mouseX < this.screenWidth - this.widthHalf) {
            this.posX = mouseX;
        } else {
            SetMousePosition(mouseX, this.posY);
        }
    }

    public void reset() {
        this.posX = this.screenWidth / 2;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getWidthHalf() {
        return this.widthHalf;
    }

    public int getTopPos() {
        return this.topPos;
    }
}