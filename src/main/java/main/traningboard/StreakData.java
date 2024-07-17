package main.traningboard;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public enum StreakData {
    STREAK_DAY(0),
    TOTAL_SLEEP_EARLY(1),
    TOTAL_WAKEUP_EARLY(2),
    TOTAL_ENOUGH_SLEEP(3),
    TOTAL_NO(4),
    TOTAL_SQUAT(5),
    TOTAL_PUSHUP(6),
    TOTAL_JUMP(7),
    TOTAL_PLANK_RUN(8),
    TOTAL_LUNGE(9);

    final int index;

    StreakData(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return name() + '\n' + TrainingBoard.streak[index];
    }

    public static void main(String[] args) {
        System.out.println(LocalTime.of(23, 0));
        LocalDateTime newSleep = LocalDateTime.now();
        LocalDateTime newWakeup = LocalDateTime.now().minusHours(2).minusMinutes(12);
        System.out.println(Duration.between(newWakeup, newSleep).compareTo(Duration.ofHours(3)));
    }
}
