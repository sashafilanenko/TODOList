package org.example.model;

import org.example.storage.JsonStorage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonTaskRepository implements TaskRepository {

    private static final String TASKS_FILE    = "tasks.json";
    private static final String HISTORY_FILE  = "history.json";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private List<Task> tasks;
    private List<Task> historyTasks;
    private int currentId;

    public JsonTaskRepository() {
        this.tasks        = loadTasks(TASKS_FILE);
        this.historyTasks = loadTasks(HISTORY_FILE);
        this.currentId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void save(Task task) {
        if (task.getId() == 0) {
            task.setId(currentId++);
            tasks.add(task);
            saveTasks(tasks, TASKS_FILE);
        }
    }

    @Override
    public void saveToHistory(Task task) {
        task.setCompleteAt(LocalDateTime.now());
        historyTasks.add(task);
        saveTasks(historyTasks, HISTORY_FILE);
    }

    @Override
    public void deleteById(int id) {
        Task found = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst().orElse(null);
        if (found != null) {
            tasks.remove(found);
            saveToHistory(found);
            saveTasks(tasks, TASKS_FILE);
        }
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Task> findAllHistory() {
        return new ArrayList<>(historyTasks);
    }

    private void saveTasks(List<Task> list, String file) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(taskToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try {
            JsonStorage.write(file, sb.toString());
        } catch (Exception e) {
            System.err.println("[JsonRepo] Ошибка записи: " + e.getMessage());
        }
    }

    private String taskToJson(Task t) {
        return String.format(
                """
                  {
                    "id": %d,
                    "title": "%s",
                    "category": "%s",
                    "isCompleted": %b,
                    "createAt": "%s",
                    "completeAt": "%s"
                  }""",
                t.getId(),
                escape(t.getTitle()),
                escape(t.getCategory()),
                t.isCompleted(),
                t.getCreateAt() != null  ? t.getCreateAt().format(FMT)  : "",
                t.getCompleteAt() != null ? t.getCompleteAt().format(FMT) : ""
        );
    }

    private List<Task> loadTasks(String file) {
        List<Task> result = new ArrayList<>();
        if (!JsonStorage.exists(file)) return result;
        try {
            String json = JsonStorage.read(file);
            String[] objects = json.split("\\{");
            for (String obj : objects) {
                if (!obj.contains("\"id\"")) continue;
                Task t = new Task();
                t.setId(         parseIntField(obj,    "id"));
                t.setTitle(      parseStrField(obj,    "title"));
                t.setCategory(   parseStrField(obj,    "category"));
                t.setCompleted(  parseBoolField(obj,   "isCompleted"));
                String ca = parseStrField(obj, "createAt");
                String co = parseStrField(obj, "completeAt");
                if (ca != null && !ca.isEmpty())
                    t.setCreateAt(LocalDateTime.parse(ca, FMT));   // нужен setCreateAt
                if (co != null && !co.isEmpty())
                    t.setCompleteAt(LocalDateTime.parse(co, FMT));
                result.add(t);
            }
        } catch (Exception e) {
            System.err.println("[JsonRepo] Ошибка чтения " + file + ": " + e.getMessage());
        }
        return result;
    }

    private int parseIntField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) return 0;
        int s = idx + key.length();
        int e = s;
        while (e < json.length() && (Character.isDigit(json.charAt(e)) || json.charAt(e) == '-')) e++;
        try { return Integer.parseInt(json.substring(s, e).trim()); }
        catch (NumberFormatException ex) { return 0; }
    }

    private String parseStrField(String json, String field) {
        String key = "\"" + field + "\": \"";
        int idx = json.indexOf(key);
        if (idx == -1) { key = "\"" + field + "\":\""; idx = json.indexOf(key); }
        if (idx == -1) return "";
        int s = idx + key.length();
        int e = json.indexOf("\"", s);
        return e == -1 ? "" : json.substring(s, e);
    }

    private boolean parseBoolField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx == -1) return false;
        return json.substring(idx + key.length()).trim().startsWith("true");
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}