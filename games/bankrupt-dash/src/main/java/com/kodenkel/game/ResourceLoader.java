package com.kodenkel.game;

import com.raylib.Raylib;
import static com.raylib.Jaylib.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;

public class ResourceLoader {
    private HashMap<String, Texture2D> textureCache;
    private HashMap<String, Clip> soundCache;
    private HashMap<String, String> mapCache;
    private String jarPath;

    public ResourceLoader(String jarPath) {
        this.jarPath = jarPath;
        this.textureCache = new HashMap<String, Texture2D>();
        this.soundCache = new HashMap<String, Clip>();
        this.mapCache = new HashMap<String, String>();
    }

    public Texture2D getTexture(String fileName) {
        if (this.textureCache.containsKey(fileName)) return this.textureCache.get(fileName);

        Texture2D texture = LoadTexture(this.jarPath + fileName);
        this.textureCache.put(fileName, texture);

        return texture;
    }

    public Clip getSound(String fileName) {
        if (this.soundCache.containsKey(fileName)) {
            Clip sound = this.soundCache.get(fileName);
            sound.stop();
            sound.flush();
            sound.setMicrosecondPosition(0);
            return sound;
        }

        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(this.jarPath + fileName));
            DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            this.soundCache.put(fileName, clip);

            return clip;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String[]> getMap(String fileName) {
        ArrayList<String[]> map = new ArrayList<String[]>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.jarPath + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                map.add(line.split("\\,", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public HashMap<String, Clip> getAllSound() {
        return this.soundCache;
    }

    public void free() {
        for (Texture2D texture : this.textureCache.values()) {
            UnloadTexture(texture);
        }

        for (Clip sound : this.soundCache.values()) {
            sound.stop();
            sound.flush();
        }
    }

    public ArrayList<String[]> getHighScores() {
        ArrayList<String[]> scores = new ArrayList<String[]>();

        try (BufferedReader br = new BufferedReader(new FileReader(this.jarPath + "high.scr"))) {
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line.split("\\,", -1));
            }
        } catch (IOException e) {
            e.printStackTrace();
            // File corrupt or doesn't exist: create a fresh high score table
            scores = new ArrayList<String[]>();
            scores.add(new String[] {"Foo", "6", "1"});
            scores.add(new String[] {"Bar", "5", "1"});
            scores.add(new String[] {"Baz", "4", "1"});
            scores.add(new String[] {"Qux", "3", "1"});
            scores.add(new String[] {"Quux", "2", "1"});
            scores.add(new String[] {"Quuz", "1", "1"});
        }

        return scores;
    }

    public void saveHighScores(ArrayList<String[]> scores) {
        String fileContents = "";
        String[] row;
        for (int i = 0; i < scores.size(); i++) {
            row = scores.get(i);
            fileContents += row[0] + "," + row[1] + "," + row[2] + System.lineSeparator();
        }

        try {
            FileWriter writer = new FileWriter(new File(this.jarPath + "high.scr"), false);
            writer.write(fileContents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
}