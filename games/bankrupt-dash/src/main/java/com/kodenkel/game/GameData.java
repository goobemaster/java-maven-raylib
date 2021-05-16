package com.kodenkel.game;

import java.util.concurrent.TimeUnit;

public class GameData {
    public boolean sound = true;
    public int mission = 1;
    public int timeLeft = 0;
    public int score = 0;

    public GameData() {}

    public void toggleSound() {
        if (this.sound == true) {
            this.sound = false;
        } else {
            this.sound = true;
        }
    }

    public String timeLeftAsString() {
        long secondsLeft = (long) this.timeLeft;
        long minutes = TimeUnit.SECONDS.toMinutes(secondsLeft);
        secondsLeft -= minutes * 60;
        return String.valueOf(minutes) + ":" + (secondsLeft < 10 ? "0" : "") + String.valueOf(secondsLeft);
    }

    public void nextMission() {
        this.mission += 1;
    }
}