package org.example.Game;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DailyStreakService {

    private static final String FILE_PATH = "streak.json";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private int streak;
    private LocalDate lastActiveDate;
    private boolean activeTodayAlready;

    public DailyStreakService() {
        load();
        checkAndUpdateOnStartup();
    }

    public void markActiveToday() {
        LocalDate today = LocalDate.now();

        if (activeTodayAlready) {
            return;
        }

        activeTodayAlready = true;
        lastActiveDate = today;
        streak++;

        save();
        System.out.println("[DailyStreak] Активность засчитана! Стрик: " + streak + " дней ?");
    }

    public int getStreak() {
        return streak;
    }

    public double getStreakMultiplier() {
        if (streak >= 30) return 2.5;
        if (streak >= 14) return 2.0;
        if (streak >= 7)  return 1.6;
        if (streak >= 3)  return 1.3;
        return 1.0;
    }

    public String getStreakLabel() {
        if (streak >= 30) return "???? " + streak + " дней";
        if (streak >= 14) return "??? "  + streak + " дней";
        if (streak >= 7)  return "?? "    + streak + " дней";
        if (streak >= 3)  return "? "      + streak + " дней";
        if (streak >= 1)  return "? "      + streak + " дней";
        return "Нет стрика";
    }

    public boolean isActiveTodayAlready() {
        return activeTodayAlready;
    }

    private void checkAndUpdateOnStartup() {
        if (lastActiveDate == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastActiveDate, today);

        if (daysBetween == 0) {
            System.out.println("[DailyStreak] Сегодня уже была активность. Стрик: " + streak);
        } else if (daysBetween == 1) {
            activeTodayAlready = false;
            System.out.println("[DailyStreak] Новый день. Стрик пока: " + streak + ". Выполни задачу чтобы сохранить!");
        } else if (daysBetween == 2) {
            activeTodayAlready = false;
            streak = Math.max(0, streak - 1);
            System.out.println("[DailyStreak] Пропущен день! Стрик уменьшен до: " + streak);
            save();
        } else {
            activeTodayAlready = false;
            streak = 0;
            System.out.println("[DailyStreak] Пропущено " + daysBetween + " дней. Стрик сброшен.");
            save();
        }
    }

    private void save() {
        String json = "{\n"
                + "  \"streak\": " + streak + ",\n"
                + "  \"lastActiveDate\": \"" + (lastActiveDate != null ? lastActiveDate.format(DATE_FMT) : "") + "\",\n"
                + "  \"activeTodayAlready\": " + activeTodayAlready + "\n"
                + "}";

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.print(json);
        } catch (IOException e) {
            System.err.println("[DailyStreak] Ошибка сохранения: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            streak = 0;
            lastActiveDate = null;
            activeTodayAlready = false;
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            String json = sb.toString();

            streak = parseIntField(json, "streak");
            activeTodayAlready = parseBoolField(json, "activeTodayAlready");

            String dateStr = parseStringField(json, "lastActiveDate");
            lastActiveDate = (dateStr != null && !dateStr.isEmpty())
                    ? LocalDate.parse(dateStr, DATE_FMT)
                    : null;

            System.out.println("[DailyStreak] Загружено: стрик=" + streak
                    + ", последний день=" + lastActiveDate
                    + ", сегодня активен=" + activeTodayAlready);

        } catch (Exception e) {
            System.err.println("[DailyStreak] Ошибка загрузки, сбрасываем: " + e.getMessage());
            streak = 0;
            lastActiveDate = null;
            activeTodayAlready = false;
        }
    }

    private int parseIntField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) return 0;
        int start = idx + key.length();
        int end = start;
        while (end < json.length() && (java.lang.Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean parseBoolField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) return false;
        String rest = json.substring(idx + key.length()).trim();
        return rest.startsWith("true");
    }

    private String parseStringField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int start = idx + key.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}