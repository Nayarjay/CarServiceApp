package org.example.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RabbitLogService {

    private final List<LogEntry> logs = new CopyOnWriteArrayList<>();
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void addLog(String direction, String queue, String message) {
        logs.add(new LogEntry(LocalTime.now().format(TIME_FMT), direction, queue, message));
        if (logs.size() > 100) {
            logs.remove(0);
        }
    }

    public List<LogEntry> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(logs));
    }

    public List<LogEntry> getLogsSince(int fromIndex) {
        if (fromIndex >= logs.size()) return Collections.emptyList();
        return Collections.unmodifiableList(new ArrayList<>(logs.subList(fromIndex, logs.size())));
    }

    public int getLogCount() {
        return logs.size();
    }

    public static class LogEntry {
        private final String time;
        private final String direction;
        private final String queue;
        private final String message;

        public LogEntry(String time, String direction, String queue, String message) {
            this.time = time;
            this.direction = direction;
            this.queue = queue;
            this.message = message;
        }

        public String getTime() { return time; }
        public String getDirection() { return direction; }
        public String getQueue() { return queue; }
        public String getMessage() { return message; }
    }
}
