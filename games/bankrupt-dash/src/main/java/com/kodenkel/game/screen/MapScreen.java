package com.kodenkel.game.screen;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import javax.sound.sampled.Clip;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.kodenkel.game.Application;
import com.kodenkel.game.GameData;
import com.kodenkel.game.GameState;
import com.kodenkel.game.GameMap;
import com.kodenkel.game.ResourceLoader;
import com.kodenkel.game.sprite.Player;
import com.kodenkel.game.sprite.Teleporter;
import com.kodenkel.game.sprite.Alien;

public class MapScreen extends BaseScreen {
    private static final Color STATUS_TEXT_COLOR = WHITE;
    private static final int STATUS_TEXT_SIZE = 26;

    private ResourceLoader resource;
    private Texture2D tileBorderH;
    private Texture2D tileBorderV;
    private Texture2D tileBoulder;
    private Texture2D tileBrick;
    private Texture2D tileDiamond;
    private Texture2D tileEarth;
    private Texture2D statusTime;
    private Texture2D statusScore;
    private Texture2D statusTeleporter;
    private Texture2D statusCheck;

    private GameMap map;
    private int viewPortX = 0;
    private int viewPortY = 0;

    private Player player;
    private int playerTileX, playerTileY;
    private int playerMoveDirection;
    private long playerMoveTimer;
    private int diamondsTotal;
    private int diamondsCollected;
    private long timeCounter;

    private Teleporter teleporter;
    private int teleTileX, teleTileY;
    private Clip teleInSound;
    private Clip teleOutSound;

    private ArrayList<Alien> aliens;

    public MapScreen(ResourceLoader resource) {
        super(resource);

        this.resource = resource;
        this.tileBorderH = resource.getTexture("tile_border_h.png");
        this.tileBorderV = resource.getTexture("tile_border_v.png");
        this.tileBoulder = resource.getTexture("tile_boulder.png");
        this.tileBrick = resource.getTexture("tile_brick.png");
        this.tileDiamond = resource.getTexture("tile_diamond.png");
        this.tileEarth = resource.getTexture("tile_earth.png");

        this.statusTime = resource.getTexture("status_time.png");
        this.statusScore = resource.getTexture("status_score.png");
        this.statusCheck = resource.getTexture("status_check.png");
        this.statusTeleporter = resource.getTexture("portal_2.png");
    }

    public boolean respondsTo(GameState state, GameData data) {
        return state.equals(GameState.TELEPORT_IN) ||
            state.equals(GameState.TELEPORT_OUT) ||
            state.equals(GameState.MAP);
    }

    public void tick(GameState state, GameData data) {
        DrawRectangle(0, 0, 320 * 2, 240 * 2, BLACK);

        // MAP TILES
        String tile;
        int shiftX = this.viewPortX * 48;
        int shiftY = this.viewPortY * 48;
        for (int y = this.viewPortY; y < this.viewPortY + 10; y++) {
            for (int x = this.viewPortX; x < this.viewPortX + 14; x++) {
                if (y < 0 || x < 0 || y >= this.map.height() || x >= this.map.width()) continue;
                tile = this.map.getTile(x, y);
                if (tile.length() == 0) {
                    DrawTextureEx(this.tileEarth, new Vector2(x * 48 - shiftX, y * 48 - shiftY), 0.0f, 2.0f, WHITE);
                    continue;
                }

                switch (tile.charAt(0)) {
                    case '#':
                        DrawTextureEx(this.tileBorderV, new Vector2(x * 48 - shiftX, y * 48 - shiftY), 0.0f, 2.0f, WHITE);
                        break;
                    case '%':
                        DrawTextureEx(this.tileBrick, new Vector2(x * 48 - shiftX, y * 48 - shiftY), 0.0f, 2.0f, WHITE);
                        break;
                    case '@':
                        DrawTextureEx(this.tileDiamond, new Vector2(x * 48 - shiftX, y * 48 - shiftY), 0.0f, 2.0f, WHITE);
                        break;  
                    case 'o':
                        DrawTextureEx(this.tileBoulder, new Vector2(x * 48 - shiftX, y * 48 - shiftY), 0.0f, 2.0f, WHITE);
                        break;                                              
                }
            }
        }

        // STATES
        Vector2 startPos = this.map.getStartPosition();

        if (state.equals(GameState.TELEPORT_IN)) {
            this.teleporter.setPosition(this.teleTileX * 48 - shiftX, this.teleTileY * 48 - shiftY);
            this.teleporter.tick();
            if (this.teleporter.animRepeated(3)) {
                Application.changeState(GameState.MAP);
                this.player.show();
                this.teleporter.hide();
            }
        }

        if (state.equals(GameState.TELEPORT_OUT)) {
            this.teleOutSound.loop(0);
            if (ZonedDateTime.now().toInstant().toEpochMilli() > this.timeCounter + 3200) {
                if (data.mission == 5) { // Last Mission > END
                    Application.changeState(GameState.SCORE);
                } else {
                    data.nextMission();
                    Application.changeState(GameState.NEXT_MISSION);
                }
            }
        }

        if (state.equals(GameState.MAP)) {
            this.player.setPosition(this.playerTileX * 48 - shiftX, this.playerTileY * 48 - shiftY);
            this.player.tick();

            // PLAYER MOVEMENT
            if (IsKeyDown(KEY_LEFT)) {
                this.movePlayerTo(this.playerTileX - 1, this.playerTileY, 0, data);
            }
            if (IsKeyDown(KEY_RIGHT)) {
                this.movePlayerTo(this.playerTileX + 1, this.playerTileY, 1, data);
            }
            if (IsKeyDown(KEY_UP)) {
                this.movePlayerTo(this.playerTileX, this.playerTileY - 1, -1, data);
            }
            if (IsKeyDown(KEY_DOWN)) {
                this.movePlayerTo(this.playerTileX, this.playerTileY + 1, -1, data);
            }
            this.map.freeFall(this.playerTileX, this.playerTileY, this.playerMoveDirection);
            if (this.player.animRepeated(1) || this.playerMoveDirection == -1) {
                this.player.setFrame(1);
            }

            // PLAYER STUCK (auto-detect or T key)
            if ((!this.canLeaveMission() && IsKeyPressed(KEY_T)) || this.map.isPlayerStuck(this.playerTileX, this.playerTileY)) {
                // Destroying boulders until teleporter is not blocked,
                // but taking into account free falling as well...
                while (this.map.isBoulder((int) startPos.x(), (int) startPos.y())) {
                    this.map.setTile((int) startPos.x(), (int) startPos.y(), "X");
                    this.map.freeFall((int) startPos.x(), (int) startPos.y(), -1);
                }
                this.player.hide();
                this.playerTileX = (int) startPos.x();
                this.playerTileY = (int) startPos.y();
                this.centerViewPortOnPlayer();
                this.teleporter.show();
                this.teleporter.loopFrames(1, 4, 150);
                this.teleInSound.loop(0);
                Application.changeState(GameState.TELEPORT_IN);
            }

            // TIME LEFT
            long now = ZonedDateTime.now().toInstant().toEpochMilli();
            if (now >= this.timeCounter + 1000 && data.timeLeft > 0) {
                data.timeLeft -= 1;
                this.timeCounter = ZonedDateTime.now().toInstant().toEpochMilli();
            }
            // TIME HAS RUN OUT - END
            if (data.timeLeft == 0) {
                Application.changeState(GameState.SCORE);
            }

            // ALIENS
            if (this.aliens.size() > 0) {
                for (int i = 0; i < this.aliens.size(); i++) {
                    this.aliens.get(i).setPosition(this.aliens.get(i).tileX * 48 - shiftX, this.aliens.get(i).tileY * 48 - shiftY);
                    this.aliens.get(i).tick(this.map);
                    // ALIEN CAUGHT THE PLAYER - END
                    if (this.playerTileX == this.aliens.get(i).tileX && this.playerTileY == this.aliens.get(i).tileY) {
                        Application.changeState(GameState.SCORE);
                    }
                }
            }
        }

        // STATUS BAR
        DrawRectangle(0, 432, 640, 48, BLACK);
        DrawTextureEx(this.tileDiamond, new Vector2(0, 432), 0.0f, 2.0f, WHITE);
        DrawText(String.valueOf(this.diamondsCollected) + " / " + String.valueOf(this.diamondsTotal), 50, 444, STATUS_TEXT_SIZE, STATUS_TEXT_COLOR);

        DrawTextureEx(this.statusScore, new Vector2(192, 432), 0.0f, 2.0f, WHITE);
        DrawText(String.valueOf(data.score), 244, 444, STATUS_TEXT_SIZE, STATUS_TEXT_COLOR);

        // 90% diamonds collected = show teleport out
        if (this.canLeaveMission()) {
            DrawTextureEx(this.statusCheck, new Vector2(146, 432), 0.0f, 2.0f, WHITE);
            if (this.teleporter.isHidden()) {
                this.teleporter.show();
                this.teleporter.loopFrames(1, 4, 150);
            }
            this.teleporter.tick();
            this.teleTileX = (int) startPos.x();
            this.teleTileY = (int) startPos.y();
            this.teleporter.setPosition(this.teleTileX * 48 - shiftX, this.teleTileY * 48 - shiftY);
            if (this.playerTileX == this.teleTileX && this.playerTileY == this.teleTileY && !state.equals(GameState.TELEPORT_OUT)) {
                Application.changeState(GameState.TELEPORT_OUT);
                this.timeCounter = ZonedDateTime.now().toInstant().toEpochMilli();
            }
            // Teleport entrance must be clear...
            if (this.map.isBlocking(this.teleTileX, this.teleTileY)) {
                this.map.setTile(this.teleTileX, this.teleTileY, "X");
            }
        }

        DrawTextureEx(this.statusTime, new Vector2(412, 432), 0.0f, 2.0f, WHITE);
        DrawText(String.valueOf(data.timeLeftAsString()), 462, 444, STATUS_TEXT_SIZE, STATUS_TEXT_COLOR);

        DrawTextureEx(this.statusTeleporter, new Vector2(532, 432), 0.0f, 2.0f, WHITE);
        DrawText(String.valueOf(data.mission), 590, 444, STATUS_TEXT_SIZE, STATUS_TEXT_COLOR);
    }

    public void onEnter(GameData data) {
        DrawRectangle(0, 0, 320 * 2, 240 * 2, BLACK);

        this.viewPortX = 0;
        this.viewPortY = 0;

        this.map = new GameMap(data.mission, this.resource);
        this.teleInSound = this.resource.getSound("BD_1.wav");
        this.teleOutSound = this.resource.getSound("BD_2.wav");

        this.player = new Player("player_%s.png", 7, this.resource);
        Vector2 startPos = this.map.getStartPosition();
        this.playerTileX = (int) startPos.x();
        this.playerTileY = (int) startPos.y();
        this.playerMoveDirection = -1;
        this.playerMoveTimer = ZonedDateTime.now().toInstant().toEpochMilli();
        this.diamondsTotal = this.map.diamondCount();
        this.diamondsCollected = 0;
        data.timeLeft = this.diamondsTotal + (this.map.width() * this.map.height() / 4) - (data.mission * 2);
        this.timeCounter = ZonedDateTime.now().toInstant().toEpochMilli();

        this.teleporter = new Teleporter("portal_%s.png", 4, this.resource);
        this.teleTileX = (int) startPos.x();
        this.teleTileY = (int) startPos.y();
        this.teleporter.loopFrames(1, 4, 150);
        this.teleInSound.loop(0);
        this.centerViewPortOnTeleport();

        this.aliens = new ArrayList<Alien>();
        ArrayList<Vector2> alienStartPositions = this.map.getAlienStartPositions();
        for (int i = 0; i < alienStartPositions.size(); i++) {
            this.aliens.add(new Alien("alien_%s.png", 7, this.resource));
            this.aliens.get(i).tileX = (int) alienStartPositions.get(i).x();
            this.aliens.get(i).tileY = (int) alienStartPositions.get(i).y();
        }
    }

    public void onLeave(GameData data) {
        // Nothing to do.
    }

    private void movePlayerTo(int moveToX, int moveToY, int moveDirection, GameData data) {
        // Limiting rate...
        if (this.playerMoveTimer + 100 > ZonedDateTime.now().toInstant().toEpochMilli()) {
            return;
        } else {
            this.playerMoveTimer = ZonedDateTime.now().toInstant().toEpochMilli();
        }

        String tile = this.map.getTile(moveToX, moveToY);
        this.playerMoveDirection = moveDirection;
        boolean movePermitted = false;

        if (tile.length() == 0 || tile == "X") {
            // Earth
            this.map.setTile(this.playerTileX, this.playerTileY, "X");
            this.map.setTile(moveToX, moveToY, "X");
            movePermitted = true;
        } else {
            switch (tile.charAt(0)) {
                case '@': // Diamond
                    this.map.setTile(moveToX, moveToY, "X");
                    this.diamondsCollected += 1;
                    data.score += 1;
                    movePermitted = true;
                    Clip diamondClip = this.resource.getSound("BD_4.wav");
                    diamondClip.loop(0);
                    break;  
                case 'o': // Boulder
                    if (moveDirection == 0) {
                        // Left Movement
                    } else if (moveDirection == 1) {
                        // Right Movement
                    }
                    break;
                case 'X': // Clear
                    movePermitted = true;
                    break;
            }
        }

        if (movePermitted) {
            this.playerTileX = moveToX;
            this.playerTileY = moveToY;
            this.centerViewPortOnPlayer();
        }

        if (moveDirection == 0 && !this.player.isLooping()) {
            this.player.loopFrames(2, 4, 200);
        }
        if (moveDirection == 1 && !this.player.isLooping()) {
            this.player.loopFrames(5, 7, 200);
        }
    }

    private void centerViewPortOnPlayer() {
        for (int y = this.playerTileY; y >= 0 && y > this.playerTileY - 5; y--) {
            for (int x = this.playerTileX; x >= 0 && x > this.playerTileX - 7; x--) {
                this.viewPortX = x;
                this.viewPortY = y;
                if (this.viewPortX >= this.map.width() - 13) this.viewPortX = this.map.width() - 13;
                if (this.viewPortY >= this.map.height() - 9) this.viewPortY = this.map.height() - 9;
            }
        }
    }

    private void centerViewPortOnTeleport() {
        for (int y = this.teleTileY; y >= 0 && y > this.teleTileY - 5; y--) {
            for (int x = this.teleTileX; x >= 0 && x > this.teleTileX - 7; x--) {
                this.viewPortX = x;
                this.viewPortY = y;
                if (this.viewPortX >= this.map.width() - 13) this.viewPortX = this.map.width() - 13;
                if (this.viewPortY >= this.map.height() - 9) this.viewPortY = this.map.height() - 9;
            }
        }
    }

    private boolean canLeaveMission() {
        return (float) this.diamondsCollected / (float) this.map.diamondCount() * 100.0f >= 90.0f;
    }
}