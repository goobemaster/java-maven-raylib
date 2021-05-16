package com.kodenkel.game.sprite;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

import com.kodenkel.game.ResourceLoader;
import com.kodenkel.game.Sprite;
import com.kodenkel.game.GameMap;

public class Alien extends Sprite {
    public int tileX, tileY;
    public long lastMove;
    public long speed;
    public int moveDirection;

    public Alien(String textureFileName, int totalFrames, ResourceLoader resource) {
        super(textureFileName, totalFrames, resource);

        this.setScale(2.0f);
        this.tileX = 0;
        this.tileY = 0;
        this.lastMove = ZonedDateTime.now().toInstant().toEpochMilli();
        this.speed = (long) ThreadLocalRandom.current().nextInt(400, 900);
        this.moveDirection = -1;
    }

    public void tick(GameMap map) {
        if (ZonedDateTime.now().toInstant().toEpochMilli() > this.lastMove + this.speed) {
            ArrayList<Vector2> eligiblePosition = new ArrayList<Vector2>();
            
            for (int y = this.tileY - 1; y < this.tileY + 2; y++) {
                for (int x = this.tileX - 1; x < this.tileX + 2; x++) {
                    if (y < 0 || y > map.height() || x < 0 || x > map.width() || !map.isClear(x, y)) continue;
                    eligiblePosition.add(new Vector2(x, y));
                }
            }

            if (eligiblePosition.size() > 0) {
                Vector2 moveToTile = eligiblePosition.get(ThreadLocalRandom.current().nextInt(0, eligiblePosition.size()));
                int currentX = this.tileX;
                int currentY = this.tileY;
                if (this.animRepeated(1)) this.setFrame(0);
                if ((int) moveToTile.x() < currentX) {
                    if (!this.isLooping()) this.loopFrames(2, 4, 200);
                } else if ((int) moveToTile.x() > currentX) {
                    if (!this.isLooping()) this.loopFrames(5, 7, 200);
                } else {
                    this.setFrame(0);
                }
                this.tileX = (int) moveToTile.x();
                this.tileY = (int) moveToTile.y();
            }

            this.lastMove = ZonedDateTime.now().toInstant().toEpochMilli();
            this.speed = (long) ThreadLocalRandom.current().nextInt(400, 900);
        }
        this.tick();
    }
}
