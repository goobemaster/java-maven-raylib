package com.kodenkel.game;

public interface GameScreen {

    public boolean respondsTo(GameState state, GameData data);

    public void tick(GameState state, GameData data);

    public void onLeave(GameData data);
    
    public void onEnter(GameData data);
}