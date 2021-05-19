package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import static com.kodenkel.game.Application.GAME_STATE;

public class Tank {
    private static final int HEIGHT = 20;
    private static final int HEIGHT_HALF = HEIGHT / 2;
    private static final int WIDTH = 35;
    private static final int WIDTH_HALF = WIDTH / 2;
    private static final Color TURRET_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 0.0f));
    private static final Color INFO_COLOR = ColorFromHSV(new Vector3(0.0f, 0.0f, 100.0f));
    private static final Color HEALTH_BAR_GREEN = ColorFromHSV(new Vector3(100.0f, 10.0f, 10.0f));
    private static final Color HEALTH_BAR_YELLOW = ColorFromHSV(new Vector3(64.0f, 10.0f, 10.0f));
    private static final Color HEALTH_BAR_RED = ColorFromHSV(new Vector3(0.0f, 10.0f, 10.0f));
    private int posX;
    private int posY;
    private float turretAngle;
    private float power;
    private Texture2D tankBodyTexture;
    private TankColor color;
    private int health;

    public enum TankColor { GREEN, BROWN }

    public Tank(int posX, Ground ground, float turretAngle, TankColor tankColor, int health) {
        this.health = health;
        this.color = tankColor;
        this.posX = posX;
        this.posY = ground.getAbsoluteElevationAt(posX) - HEIGHT - 1;
        this.turretAngle = turretAngle;
        this.power = 25.0f;
        Image tankBodyImage = LoadImage("src/main/resources/scorched/tank_" + tankColor.toString().toLowerCase() + ".png");
        tankBodyTexture = LoadTextureFromImage(tankBodyImage);
        UnloadImage(tankBodyImage);
    }

    public boolean tick(GAME_STATE state, boolean turn, Ground ground) {
        int elevationAtTank = ground.getAbsoluteElevationAt(this.posX);
        if (this.posY < elevationAtTank) {
            while (this.posY < elevationAtTank) {
                this.posY += 1;
            }
        }

        if (state.equals(GAME_STATE.TURN) && turn) {
            if (IsKeyDown(KEY_LEFT)) {
                this.turretAngleDec();
            } else if (IsKeyDown(KEY_RIGHT)) {
                this.turretAngleInc();
            }
            if (IsKeyDown(KEY_UP)) {
                this.powerInc();
            } else if (IsKeyDown(KEY_DOWN)) {
                this.powerDec();
            }
            if (IsKeyDown(KEY_SPACE)) {
                return true;
            }
            DrawText(this.color.toString() + " Tank", 10, 10, 20, INFO_COLOR);
            DrawText("Turret: " + String.valueOf((int) this.turretAngle) + " °", 10, 30, 20, INFO_COLOR);
            DrawText(" Power: " + String.valueOf(this.power) + " %", 150, 30, 20, INFO_COLOR);
            DrawText("[SPACE] to launch", 10, 50, 15, INFO_COLOR);
        }

        DrawRectangle(this.posX - WIDTH_HALF, this.posY - HEIGHT_HALF - 30, WIDTH, 8, TURRET_COLOR);
        int healthBarWidth = (int) ((WIDTH / 100.0f * this.health) - 2);
        if (this.health > 75) {
            DrawRectangle(this.posX - WIDTH_HALF + 1, this.posY - HEIGHT_HALF - 29, healthBarWidth, 6, HEALTH_BAR_GREEN);
        } else if (this.health > 35 && this.health <= 75) {
            DrawRectangle(this.posX - WIDTH_HALF + 1, this.posY - HEIGHT_HALF - 29, healthBarWidth, 6, HEALTH_BAR_YELLOW);
        } else {
            DrawRectangle(this.posX - WIDTH_HALF + 1, this.posY - HEIGHT_HALF - 29, healthBarWidth, 6, HEALTH_BAR_RED);
        }

        DrawTexture(this.tankBodyTexture, this.posX - WIDTH_HALF, this.posY - HEIGHT, WHITE);
        DrawRectanglePro(new Rectangle().x(this.posX).y(this.posY - HEIGHT_HALF).width(4).height(20), new Vector2(2, 20), (float) this.turretAngle, TURRET_COLOR);
        return false;
    }

    private void turretAngleDec() {
        this.turretAngle = this.turretAngle - 0.1f;
        if (this.turretAngle < -90.0f) this.turretAngle = -90.0f;
    }

    private void turretAngleInc() {
        this.turretAngle += 0.1f;
        if (this.turretAngle > 90.0f) this.turretAngle = 90.0f;
    }

    private void powerDec() {
        this.power = this.power - 0.5f;
        if (this.power < 10.0f) this.power = 10.0f;
    }

    private void powerInc() {
        this.power += 0.5f;
        if (this.power > 100.0f) this.power = 100.0f;
    }

    public int getMissileOriginX() {
        return this.posX;
    }

    public int getMissileOriginY() {
        return this.posY - HEIGHT_HALF;
    }

    public float getAngle() {
        return this.turretAngle;
    }

    public float getPower() {
        return this.power;
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public TankColor getColor() {
        return this.color;
    }

    public int getX() {
        return this.posX;
    }

    public int getY() {
        return this.posY;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
    }
}