package main.traningboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ControlTrainingBoard implements Initializable {
    private DayStatus selectionDay;
    private Workout selectionWorkout;
    private final int[] data = new int[5];
    @FXML
    private ListView<String> DateData;
    private void updateDate() {
        sleepHour.setValue(selectionDay.sleep.getHour());
        sleepMinute.setValue(selectionDay.sleep.getMinute());
        ___.setSelected(selectionDay.___);
        wakeupHour.setValue(selectionDay.wakeup.getHour());
        wakeupMinute.setValue(selectionDay.wakeup.getMinute());
        updateDateData();
        workoutList.getItems().clear();
        workoutList.getItems().addAll(selectionDay.dayWorkout);
        date.setText(selectionDay.date.format(DayStatus.DATE_FORMATTER));
    }

    private void updateDateData() {
        DateData.getItems().clear();
        DateData.getItems().add("Sleep At\n" + selectionDay.sleep.format(DayStatus.TIME_FORMATTER));
        DateData.getItems().add("Wakeup At\n" + selectionDay.wakeup.format(DayStatus.TIME_FORMATTER));
        Duration duration = Duration.between(selectionDay.sleep, selectionDay.wakeup);
        if (duration.isNegative()) {
            duration = duration.plus(Duration.ofHours(24));
        }
        DateData.getItems().add("Sleep period\n" + duration.toHours() + ":" + duration.toMinutesPart());
        DateData.getItems().add("___\n" + selectionDay.___);
        int[] w = selectionDay.dayData();
        for (Workout.EXERCISE e : Workout.EXERCISE.values()) {
            DateData.getItems().add(e.name() + '\n' + w[e.index]);
        }
    }
    @FXML
    private ListView<String> WorkoutData;
    private void updateWorkoutData(int[] exData) {
        WorkoutData.getItems().clear();
        workoutHour.setValue(selectionWorkout.time.getHour());
        workoutMinute.setValue(selectionWorkout.time.getMinute());
        WorkoutData.getItems().add("Time\n" + selectionWorkout.time.format(DayStatus.TIME_FORMATTER));
        for (Workout.EXERCISE e : Workout.EXERCISE.values()) {
            WorkoutData.getItems().add(e.name() + '\n' + exData[e.index]);
        }
    }
    @FXML
    private Text date;
    @FXML
    private ListView<DayStatus> dateList;
    @FXML
    private TextArea inputData;
    @FXML
    private ComboBox<Integer> sleepHour;
    @FXML
    private ComboBox<Integer> sleepMinute;
    @FXML
    private ListView<String> streakShow;
    private void updateStreakShow() {
        streakShow.getItems().clear();
        for (StreakData s : StreakData.values()) {
            streakShow.getItems().add(s.toString());
        }
    }
    @FXML private CheckBox ___;
    @FXML
    private ComboBox<Integer> wakeupHour;

    @FXML
    private ComboBox<Integer> wakeupMinute;

    @FXML
    private ComboBox<Integer> workoutHour;

    @FXML
    private ComboBox<Integer> workoutMinute;

    @FXML
    private ListView<Workout> workoutList;

    @FXML private VBox buttonBox;
    @FXML private Button saveDateBtn;
    @FXML private Button saveWorkoutBtn;


    @FXML
    void chooseDate() {
        selectionDay = dateList.getSelectionModel().getSelectedItem();
        updateDate();
        buttonBox.setDisable(!selectionDay.isToday());
        saveDateBtn.setDisable(!selectionDay.isToday());
    }

    @FXML
    void chooseWorkout() {
        selectionWorkout = workoutList.getSelectionModel().getSelectedItem();
        updateWorkoutData(selectionWorkout.exercise);
    }

    @FXML
    void createNew() {
        selectionWorkout = null;
        workoutHour.setValue(LocalTime.now().getHour());
        workoutMinute.setValue(LocalTime.now().getMinute());
        WorkoutData.getItems().clear();
        saveWorkoutBtn.setDisable(true);
    }

    @FXML
    void delete() {
        workoutList.getItems().remove(workoutList.getSelectionModel().getSelectedIndex());
        selectionDay.dayWorkout.clear();
        selectionDay.dayWorkout.addAll(workoutList.getItems());
        selectionWorkout = null;
        WorkoutData.getItems().clear();
        saveWorkoutBtn.setDisable(true);
    }

    @FXML
    void clear() {
        WorkoutData.getItems().clear();
        saveWorkoutBtn.setDisable(true);
    }

    @FXML
    void loadData(MouseEvent event) {
        getIntArray(inputData.getText());
        WorkoutData.getItems().clear();
        WorkoutData.getItems().add("Time\n" + workoutHour.getValue() + ":" + workoutMinute.getValue());
        for (Workout.EXERCISE e : Workout.EXERCISE.values()) {
            WorkoutData.getItems().add(e.name() + '\n' + data[e.index]);
        }
        saveWorkoutBtn.setDisable(false);
    }

    private void getIntArray(String s) {
        Scanner scanner = new Scanner(s);
        Arrays.fill(data, 0);
        int i = 0;
        while (scanner.hasNextInt() && i < 5) {
            data[i] = scanner.nextInt();
            i++;
        }
    }

    @FXML
    void saveDate(MouseEvent event) {
        selectionDay.date = LocalDate.now();
        selectionDay.sleep = LocalTime.of(sleepHour.getValue(), sleepMinute.getValue());
        selectionDay.___ = ___.isSelected();
        selectionDay.wakeup = LocalTime.of(wakeupHour.getValue(), wakeupMinute.getValue());
        updateDateData();
    }

    @FXML
    void saveWorkout(MouseEvent event) {
        if (selectionWorkout == null) {
            selectionWorkout = new Workout(LocalTime.of(workoutHour.getValue(), workoutMinute.getValue()), data);
            selectionDay.dayWorkout.add(selectionWorkout);
        } else {
            selectionWorkout.time = LocalTime.of(workoutHour.getValue(), workoutMinute.getValue());
            selectionWorkout.exercise = Arrays.copyOf(data, data.length);
        }
        workoutList.getItems().clear();
        workoutList.getItems().addAll(selectionDay.dayWorkout);
        updateWorkoutData(selectionWorkout.exercise);
    }

    @FXML
    void setNow() {
        workoutHour.setValue(LocalTime.now().getHour());
        workoutMinute.setValue(LocalTime.now().getMinute());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> hour = FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23);
        sleepHour.setItems(hour);
        wakeupHour.setItems(hour);
        workoutHour.setItems(hour);
        ObservableList<Integer> minute = FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23,24,25,26,27,28,29,
                30,31,32,33,34,35,36,37,38,39,
                40,41,42,43,44,45,46,47,48,49,
                50,51,52,53,54,55,56,57,58,59
        );
        sleepMinute.setItems(minute);
        wakeupMinute.setItems(minute);
        workoutMinute.setItems(minute);
        updateStreakShow();
        dateList.getItems().addAll(TrainingBoard.trainingBoard);
        selectionDay = TrainingBoard.today;
        updateDate();
        workoutHour.setValue(LocalTime.now().getHour());
        workoutMinute.setValue(LocalTime.now().getMinute());
    }
}
