package main.traningboard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class DayStatus {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E,dd MMM yyyy");
    public LocalDate date;
    public LocalTime sleep;
    public boolean ___;
    public LocalTime wakeup;
    public List<Workout> dayWorkout;

    public DayStatus(String saveString) {
        Scanner scanner = new Scanner(saveString);
        date = LocalDate.parse(scanner.next());
        sleep = LocalTime.parse(scanner.next(), TIME_FORMATTER);
        ___ = scanner.nextBoolean();
        wakeup = LocalTime.parse(scanner.next(), TIME_FORMATTER);
        dayWorkout = new ArrayList<>();
        while (scanner.hasNextLine()) {
            dayWorkout.add(new Workout(scanner.nextLine()));
        }
    }

    public DayStatus(LocalDate date, LocalTime sleep, boolean ___, LocalTime wakeup, List<Workout> dayWorkout) {
        this.date = date;
        this.sleep = sleep;
        this.___ = ___;
        this.wakeup = wakeup;
        this.dayWorkout = dayWorkout;
    }

    public int[] dayData() {
        int[] res = new int[Workout.EXERCISE.values().length];
        for (Workout e : dayWorkout) {
            for (int i = 0; i < res.length; i++) {
                res[i] += e.exercise[i];
            }
        }
        return res;
    }
    @Override
    public String toString() {
        return date.format(DATE_FORMATTER);
    }

    public String saveString() {
        StringBuilder sb = new StringBuilder(date + " " +
                sleep.format(TIME_FORMATTER) + " " + ___ + " " +
                wakeup.format(TIME_FORMATTER) + " ");
        for (Workout workout : dayWorkout) {
            sb.append(workout.saveString()).append('\n');
        }
        return sb.toString().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayStatus)) return false;
        DayStatus dayStatus = (DayStatus) o;
        return date.equals(dayStatus.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    public boolean isToday() {
        return date.equals(LocalDate.now());
    }

    public boolean isYesterday() {
        return date.equals(LocalDate.now().minusDays(1));
    }
}
