package org.example.Game;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private int level;
    private int currentXP;
    private int nextLevelXP;
    private int streak;
    private List<String> avatarPaths;

    private String avatar;

    public Character() {
        this.level = 1;
        this.currentXP = 0;
        this.nextLevelXP = 50;
        this.streak = 0;
        this.avatarPaths = new ArrayList<>();
        initializeAvatars();
        this.avatar = avatarPaths.get(0);
    }

    private void initializeAvatars() {
        avatarPaths.add("/lvl1.png");
        avatarPaths.add("/lvl2.png");
        avatarPaths.add("/lvl3.png");
        avatarPaths.add("/lvl4.png");
        avatarPaths.add("/lvl5.png");
        avatarPaths.add("/lvl6.png");
        avatarPaths.add("/lvl7.png");
        avatarPaths.add("/lvl8.png");
        avatarPaths.add("/lvl9.png");
        avatarPaths.add("/lvl10.png");
        avatarPaths.add("/lvl11.png");
        avatarPaths.add("/lvl12.png");
        avatarPaths.add("/lvl13.png");
        avatarPaths.add("/lvl14.png");
        avatarPaths.add("/lvl15.png");
        avatarPaths.add("/lvl16.png");
        avatarPaths.add("/lvl17.png");
        avatarPaths.add("/lvl18.png");
        avatarPaths.add("/lvl19.png");
    }

    public void addXP(int xp) {
        currentXP += xp;

        while (currentXP >= nextLevelXP) {
            levelUp();
        }
    }

    private void levelUp() {
        currentXP -= nextLevelXP;
        level++;
        nextLevelXP = calculateNextLevelXP();
        updateAvatar();
    }

    private int calculateNextLevelXP() {
        if (level == 1) {
            return 50;
        }
        return (int) Math.round(50 * Math.pow(1.1, level - 1));
    }

    private void updateAvatar() {
        int index = Math.min(level - 1, avatarPaths.size() - 1);
        this.avatar = avatarPaths.get(index);
    }

    public String getCurrentAvatar() {
        return avatar;
    }

    public void addAvatarPath(String path) {
        avatarPaths.add(path);
    }

    public void incrementStreak() {
        streak++;
    }

    public void resetStreak() {
        streak = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getNextLevelXP() {
        return nextLevelXP;
    }

    public int getStreak() {
        return streak;
    }

    public List<String> getAvatarPaths() {
        return new ArrayList<>(avatarPaths);
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    @Override
    public String toString() {
        return String.format("Character[Level: %d, XP: %d/%d, Streak: %d, Avatar: %s]",
                level, currentXP, nextLevelXP, streak, avatar);
    }
}