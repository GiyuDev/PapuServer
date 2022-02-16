package com.bitnet.paulo.papuafk.player;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class AFKPlayer {

    private String name;
    private boolean afk;

    @Nullable
    private Location loc;

    private int playTime;

    public AFKPlayer() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isAfk() {
        return afk;
    }
    public void setAfk(boolean afk) {
        this.afk = afk;
    }
    public Location getLoc() {
        return loc;
    }
    public void setLoc(Location loc) {
        this.loc = loc;
    }
    public int getPlayTime() {
        return playTime;
    }
    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
