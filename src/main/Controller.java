package main;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.robot.Robot;

import java.util.concurrent.atomic.AtomicBoolean;


public class Controller {

    @FXML
    private TextField textFieldAX;
    @FXML
    private TextField textFieldAY;
    @FXML
    private TextField textFieldBX;
    @FXML
    private TextField textFieldBY;
    @FXML
    private TextField textFieldDelayTime;
    @FXML
    private Label labelMinutes;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;
    @FXML
    private ProgressBar progressBar;

    private AtomicBoolean flag;
    private Alert alert;


    public void initialize (){
        flag = new AtomicBoolean();
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error input data");
    }
    private void mouseMove()  {
        double pointAX = Double.parseDouble(textFieldAX.getText());
        double pointAY = Double.parseDouble(textFieldAY.getText());
        double pointBX = Double.parseDouble(textFieldBX.getText());
        double pointBY = Double.parseDouble(textFieldBY.getText());
        int delayTime = Integer.parseInt(textFieldDelayTime.getText())* 1000;
        Robot robot = new Robot();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                flag.set(true);
                while (flag.get()){
                    Thread.sleep(delayTime);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                robot.mouseMove(pointBX, pointBY);
                        }
                    });
                    Thread.sleep(delayTime);
                    if (!flag.get()){
                        break;
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            robot.mouseMove(pointAX, pointAY);
                        }
                    });
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void handleStart() {
        buttonStop.setDisable(false);
        buttonStart.setDisable(true);
        progressBar.setVisible(true);
        mouseMove();
    }

    @FXML
    public void handleStop() {
        flag.set(false);
        buttonStart.setDisable(false);
        buttonStop.setDisable(true);
        progressBar.setVisible(false);
    }

    @FXML
    public void pointFieldRelease(){
        String regex = "(\\d){1,4}"; // max 4 digit (to 9999 )
        if (!textFieldAX.getText().matches(regex) || !textFieldBX.getText().matches(regex) ||
            !textFieldAY.getText().matches(regex) || !textFieldBY.getText().matches(regex)) {
            alert.setContentText("Incorrect data. Only digit are acceptable \nMax  4 digit \nEnter correct value");
            alert.showAndWait();
        }
    }

    @FXML
    public void timeFieldRelease (){
        String regex = "(\\d){1,3}"; // max 3 digit ( 999 second)
        if (!textFieldDelayTime.getText().matches(regex)){
            alert.setContentText("Incorrect data. Only digit are acceptable \nMax  3 digit \nEnter correct value");
            alert.showAndWait();
        } else {
            int seconds = Integer.parseInt(textFieldDelayTime.getText());
            int minutes = seconds / 60;
            int moduloSeconds = seconds % 60;
            labelMinutes.setText( minutes + " minute " + moduloSeconds + " seconds"  );
        }
    }

}
