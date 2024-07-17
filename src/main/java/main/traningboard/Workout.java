package main.traningboard;

import java.time.LocalTime;
import java.util.Scanner;

public class Workout {
    public LocalTime time;
    public int[] exercise = new int[EXERCISE.values().length];
    public enum EXERCISE {
        SQUAT(0), PUSH_UP(1), JUMP(2), PLANK_RUN(3), LUNGE(4);
        final int index;

        EXERCISE(int index) {
            this.index = index;
        }
    }

    public Workout() {

    }

    public Workout (String saveString) {
        Scanner scanner = new Scanner(saveString);
        time = LocalTime.parse(scanner.next(), DayStatus.TIME_FORMATTER);
        for (int i = 0; i < exercise.length; i++) {
            exercise[i] = scanner.nextInt();
        }
    }

    public Workout(LocalTime time, int[] exercise) {
        this.time = time;
        this.exercise = exercise;
    }

    public String saveString() {
        StringBuilder s= new StringBuilder();
        for (int i : exercise) {
            s.append(i).append(' ');
        }
        return time.format(DayStatus.TIME_FORMATTER) + " " +
                s.toString().trim();
    }

    @Override
    public String toString() {
        return time.format(DayStatus.TIME_FORMATTER);
    }
}
