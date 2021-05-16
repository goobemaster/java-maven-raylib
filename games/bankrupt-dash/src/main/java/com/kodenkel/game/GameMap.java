package com.kodenkel.game;

import static com.raylib.Jaylib.*;

import java.util.ArrayList;
import javax.sound.sampled.Clip;

public final class GameMap {
    private ResourceLoader resource;
    private ArrayList<String[]> tiles;
    private Vector2 startPosition = null;
    private ArrayList<Vector2> alienStartPositions;
    private int diamonds;
    private Clip rockFallSound;

    public GameMap(int mission, ResourceLoader resource) {
        this.resource = resource;
        this.tiles = resource.getMap("map" + String.valueOf(mission) + ".csv");
        this.rockFallSound = resource.getSound("BD_3.wav");
        this.alienStartPositions = new ArrayList<Vector2>();

        String mark;
        for (int y = 0; y < this.height(); y++) {
            for (int x = 0; x < this.width(); x++) {
                mark = this.getTile(x, y);
                if (mark.length() == 0) continue;
                if (mark.charAt(0) == 'S') {
                    this.startPosition = new Vector2(x, y);
                    this.setTile(x, y, "X");
                }
                if (mark.charAt(0) == '@') this.diamonds += 1;
                if (mark.charAt(0) == 'A') {
                    this.setTile(x, y, "X");
                    this.alienStartPositions.add(new Vector2(x, y));
                }
            }
        }
    }

    public String getTile(int x, int y) {
        return this.tiles.get(y)[x];
    }

    public boolean isBoulder(int x, int y) {
        String mark = this.getTile(x, y);
        return mark.length() != 0 && mark.charAt(0) == 'o';
    }

    public boolean isBlocking(int x, int y) {
        String mark = this.getTile(x, y);
        return mark.length() != 0 && (mark.charAt(0) == 'o' || mark.charAt(0) == '#' || mark.charAt(0) == '%');
    }

    private boolean isDiamond(int x, int y) {
        String mark = this.getTile(x, y);
        return mark.length() != 0 && mark.charAt(0) == '@';
    }

    public boolean isClear(int x, int y) {
        String mark = this.getTile(x, y);
        return mark.length() != 0 && mark.charAt(0) == 'X';
    }

    public void setTile(int x, int y, String mark) {
        if (x < 0 || y < 0 || x > this.width() || y > this.height()) return;
        this.tiles.get(y)[x] = mark;
    }

    public int height() {
        return this.tiles.size();
    }

    public int width() {
        return this.tiles.get(0).length;
    }

    public Vector2 getStartPosition() {
        return this.startPosition;
    }

    public int diamondCount() {
        return this.diamonds;
    }

    public void freeFall(int playerTileX, int playerTileY, int playerMoveDirection) {
        boolean updateCommited = false;

        for (int y = 0; y < this.height(); y++) {
            for (int x = 0; x < this.width(); x++) {
                if (this.isDiamond(x, y)) {
                    if (this.isClear(x, y + 1) && !(playerTileX == x && playerTileY == y + 1)) {
                        this.setTile(x, y, "X");
                        this.setTile(x, y + 1, "@");
                        updateCommited = true;
                        this.playRockFallSound();
                    }
                }

                if (this.isBoulder(x, y) && !updateCommited) {
                    // Priority matters!
                    if (this.isClear(x, y + 1) && !(playerTileX == x && playerTileY == y + 1)) {
                        this.setTile(x, y, "X");
                        this.setTile(x, y + 1, "o");
                        updateCommited = true;
                        this.playRockFallSound();
                    }
                    if (this.isClear(x + 1, y + 1) && this.isClear(x + 1, y) && !(playerTileX == x + 1 && playerTileY == y + 1) && !(playerTileX == x + 1 && playerTileY == y)
                        && playerMoveDirection != -1 && !updateCommited) {
                        this.setTile(x, y, "X");
                        this.setTile(x + 1, y + 1, "o");
                        updateCommited = true;
                        this.playRockFallSound();
                    }
                    if (this.isClear(x - 1, y + 1) && this.isClear(x - 1, y) && !(playerTileX == x - 1 && playerTileY == y + 1) && !(playerTileX == x - 1 && playerTileY == y)
                        && playerMoveDirection != 1 && !updateCommited) {
                        this.setTile(x, y, "X");
                        this.setTile(x - 1, y + 1, "o");
                        updateCommited = true;
                        this.playRockFallSound();
                    }
                }
                if (updateCommited) break;
            }
            if (updateCommited) break;
        }

        //if (updateCommited) freeFall(playerTileX, playerTileY, playerMoveDirection);
    }

    private void playRockFallSound() {
        // This is still weird...
        if (!this.rockFallSound.isRunning() && !this.rockFallSound.isActive()) {
            this.rockFallSound.start();
        } else {
            this.rockFallSound.flush();
            this.rockFallSound = this.resource.getSound("BD_3.wav");
            this.rockFallSound.start();
        }
    }

    public boolean isPlayerStuck(int playerTileX, int playerTileY) {
        // We only check for situations fully enclosed on all sides...
        return this.isBlocking(playerTileX -1, playerTileY) &&
            this.isBlocking(playerTileX + 1, playerTileY) &&
            this.isBlocking(playerTileX, playerTileY - 1) &&
            this.isBlocking(playerTileX, playerTileY + 1);
    }

    public ArrayList<Vector2> getAlienStartPositions() {
        return this.alienStartPositions;
    }
}
