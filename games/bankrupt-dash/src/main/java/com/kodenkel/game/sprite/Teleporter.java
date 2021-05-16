package com.kodenkel.game.sprite;

import com.kodenkel.game.ResourceLoader;
import com.kodenkel.game.Sprite;

public class Teleporter extends Sprite {
    public Teleporter(String textureFileName, int totalFrames, ResourceLoader resource) {
        super(textureFileName, totalFrames, resource);

        this.setScale(2.0f);
    }
}
