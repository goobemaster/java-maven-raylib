package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

public class Horizon {
    private static final Color COLOR_FROM_UPPER = ColorFromHSV(new Vector3(204.0f, 91.0f, 0.5f));
    private static final Color COLOR_TO_UPPER = ColorFromHSV(new Vector3(204.0f, 66.0f, 0.69f));
    private static final Color COLOR_FROM_LOWER = COLOR_TO_UPPER;
    private static final Color COLOR_TO_LOWER = ColorFromHSV(new Vector3(340.0f, 0.0f, 0.9f));

    public static final void tick(int screenWidth, int screenHeight) {
        int heightHalf = screenHeight / 2;
        DrawRectangleGradientV(0, 0, screenWidth, heightHalf, COLOR_FROM_UPPER, COLOR_TO_UPPER);
        DrawRectangleGradientV(0, heightHalf, screenWidth, heightHalf, COLOR_FROM_LOWER, COLOR_TO_LOWER);
    }
}