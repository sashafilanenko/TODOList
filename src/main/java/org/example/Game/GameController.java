package org.example.Game;

public class GameController {
    private Character character;

    public GameController() {
        this.character = new Character();
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

    public int getCharacterStreak() {
        return character.getStreak();
    }

    public String getCharacterAvatar() {
        return character.getCurrentAvatar();
    }

    public void addXPToCharacter(int xp) {
        character.addXP(xp);
    }

    public void incrementCharacterStreak() {
        character.incrementStreak();
    }

    public void resetCharacterStreak() {
        character.resetStreak();
    }

    public Character getCharacter() {
        return character;
    }
}