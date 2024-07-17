package main.traningboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TrainingBoard extends Application {
    public static LocalDate beginDay = LocalDate.of(2024,6,4);
    public static DayStatus today;
    public static int[] streak = new int[StreakData.values().length];
    public static List<DayStatus> trainingBoard = new ArrayList<>();
    public static final String PATHSAVE = "D:\\Program Files\\Intellij\\My Program\\TraningBoard\\src\\main\\java\\main\\traningboard\\ExerciseData";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TrainingBoard.class.getResource("TrainingBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws FileNotFoundException {
        setup();
        launch();
        saveData();
    }

    static void setup() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(PATHSAVE));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        streak[0] = (int) ChronoUnit.DAYS.between(beginDay,LocalDate.now());
        scanner.nextInt();
        for (int i = 1; i < streak.length; i++) {
            streak[i] = scanner.nextInt();
        }

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine()).append('\n');
        }
        int openIndex = sb.indexOf("(");
        int closeIndex = sb.indexOf(")", openIndex);
        while (openIndex != -1) {
            trainingBoard.add(new DayStatus(sb.substring(openIndex + 1, closeIndex)));
            openIndex = sb.indexOf("(", closeIndex);
            closeIndex = sb.indexOf(")", openIndex);
        }
        scanner.close();
        today = updateToday();
        trainingBoard.sort(Comparator.comparing(DayStatus::hashCode).reversed());
    }

    public static DayStatus updateToday() {
        for (DayStatus day : trainingBoard) {
            if (day.isToday()) {
                return day;
            }
        }
        DayStatus today = new DayStatus(LocalDate.now(), LocalTime.MAX, false, LocalTime.MIN, new ArrayList<>());
        trainingBoard.add(today);
        updateYesterdayToStreak();
        return today;
    }

    static void updateYesterdayToStreak() {
        for (DayStatus day : TrainingBoard.trainingBoard) {
            if (day.isYesterday()) {
                if (day.sleep.isBefore(LocalTime.of(23, 59)) && day.sleep.getHour() >= 19) {
                    streak[StreakData.TOTAL_SLEEP_EARLY.index]++;
                }
                if (!day.___) {
                    streak[StreakData.TOTAL_NO.index]++;
                }
                if (day.wakeup.getHour() <= 8 && day.wakeup.getHour() >= 3) {
                    streak[StreakData.TOTAL_WAKEUP_EARLY.index]++;
                }
                Duration duration = Duration.between(day.sleep, day.wakeup);
                if (duration.isNegative()) {
                    duration = duration.plus(Duration.ofHours(24));
                }
                if (duration.toHours() >= 6 && duration.toHours() <= 9) {
                    streak[StreakData.TOTAL_ENOUGH_SLEEP.index]++;
                }
                int[] data = day.dayData();
                for (int i = StreakData.TOTAL_SQUAT.index; i < StreakData.values().length; i++) {
                    streak[i] += data[i - StreakData.TOTAL_SQUAT.index];
                }
            }
        }
    }

    static void saveData() {
        try {
            FileWriter fileWriter = new FileWriter(PATHSAVE);
            for (int i : streak) {
                fileWriter.write(i + " ");
            }
            for (DayStatus dayStatus : trainingBoard) {
                fileWriter.write("(" + dayStatus.saveString() + ")\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}