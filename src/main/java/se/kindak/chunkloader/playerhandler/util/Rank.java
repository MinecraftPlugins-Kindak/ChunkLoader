package se.kindak.chunkloader.playerhandler.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Rank {
    private int level;
    private String name;
    private int minutes;
    private int cooldown;
    private int price;
    private int radius;

    public Rank(FileConfiguration config, String path, int level) {
        this.name = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
        this.minutes = config.getInt(path + ".Minutes");
        this.cooldown = config.getInt(path + ".Cooldown");
        this.price = config.getInt(path + ".Price");
        this.radius = config.getInt(path + ".Radius");
        this.level = level;
    }

    public Rank(String name, int level, int minutes, int price, int cooldown, int radius) {
        this.name = name;
        this.minutes = minutes;
        this.price = price;
        this.cooldown = cooldown;
        this.level = level;
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
