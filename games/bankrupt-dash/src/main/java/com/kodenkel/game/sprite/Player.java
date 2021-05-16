package com.kodenkel.game.sprite;

import com.kodenkel.game.ResourceLoader;
import com.kodenkel.game.Sprite;

public class Player extends Sprite {
    public Player(String textureFileName, int totalFrames, ResourceLoader resource) {
        super(textureFileName, totalFrames, resource);

        this.setScale(2.0f);
    }
}
