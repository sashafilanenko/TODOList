package org.example.storage;

import org.example.Game.Character;

public class CharacterStorage {

    private static final String FILE = "character.json";

    public static void save(Character c) {
        String json = String.format(
                """
                {
                  "level": %d,
                  "currentXP": %d,
                  "nextLevelXP": %d,
                  "streak": %d
                }""",
                c.getLevel(), c.getCurrentXP(), c.getNextLevelXP(), c.getStreak()
        );
        try { JsonStorage.write(FILE, json); }
        catch (Exception e) { System.err.println("[CharStorage] " + e.getMessage()); }
    }

    public static void load(Character c) {
        if (!JsonStorage.exists(FILE)) return;
        try {
            String json = JsonStorage.read(FILE);
            // Character нужны сеттеры для level / currentXP / nextLevelXP
            c.setLevel(     parseIntField(json, "level"));
            c.setCurrentXP( parseIntField(json, "currentXP"));
            c.setNextLevelXP(parseIntField(json, "nextLevelXP"));
            c.setStreak(    parseIntField(json, "streak"));
        } catch (Exception e) {
            System.err.println("[CharStorage] Ошибка загрузки: " + e.getMessage());
        }
    }

    private static int parseIntField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) return 0;
        int s = idx + key.length(), e = s;
        while (e < json.length() && (java.lang.Character.isDigit(json.charAt(e)) || json.charAt(e) == '-')) e++;
        try { return Integer.parseInt(json.substring(s, e).trim()); }
        catch (NumberFormatException ex) { return 0; }
    }
}