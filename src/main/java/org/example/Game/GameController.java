package org.example.Game;

import org.example.storage.CharacterStorage;

public class GameController {
    private Character character;
    private DailyStreakService dailyStreakService;

    public GameController() {
        this.character = new Character();
        CharacterStorage.load(character);           // загружаем при старте
        this.dailyStreakService = new DailyStreakService();
    }

    public int getCharacterLevel() {
        return character.getLevel();
    }

    public int getCharacterXP() {
        return character.getCurrentXP();
    }

    public int getCharacterNextLevelXP() {
        return character.getNextLevelXP();
    }

    public String getCharacterAvatar() {
        return character.getCurrentAvatar();
    }

    public void addXPToCharacter(int xp) {
        character.addXP(xp);
        CharacterStorage.save(character);           // ? сохраняем сразу
    }

    public Character getCharacter() {
        return character;
    }

    public int getCharacterStreak() {
        return character.getStreak();
    }

    public void incrementCharacterStreak() {
        character.incrementStreak();
    }

    public void resetCharacterStreak() {
        character.resetStreak();
    }

    public int getDailyStreak() {
        return dailyStreakService.getStreak();
    }

    public String getDailyStreakLabel() {
        return dailyStreakService.getStreakLabel();
    }

    public double getDailyStreakMultiplier() {
        return dailyStreakService.getStreakMultiplier();
    }

    public void onTaskCompleted() {
        dailyStreakService.markActiveToday();
    }

    public int calculateAndAddXP(int baseXP, double pomodoroMultiplier) {
        double dailyMultiplier = dailyStreakService.getStreakMultiplier();
        int total = (int)(baseXP * pomodoroMultiplier * dailyMultiplier);
        character.addXP(total);
        CharacterStorage.save(character);           // ? и тут
        return total;
    }
}