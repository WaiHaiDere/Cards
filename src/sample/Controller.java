package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private List<String> questionList = new ArrayList<>();
    private List<String> answerList = new ArrayList<>();
    private List<Integer> doneList = new ArrayList<Integer>();

    private String fileChosen;

    private int counter = 0;

    private int currentIndex;
    private int numberOfPlays = 0;

    private Random rand = new Random();

    @FXML
    private Button restartBtn;

    @FXML
    private Button answerImportBtn;

    @FXML
    private Button questionImportBtn;

    @FXML
    private Button flashCardBtn;

    @FXML
    private Button startBtn;


    public void handleFlashCardAction(ActionEvent actionEvent) {

        if (counter == 0) {
            if (numberOfPlays == questionList.size()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Finished");
                alert.setHeaderText(null);
                alert.setContentText("Please press restart");
                alert.showAndWait();
            } else {
                currentIndex = randomNumber();
                flashCardBtn.setText(questionList.get(currentIndex));
                counter++;
                numberOfPlays++;
            }
        } else if (counter == 1) {
            flashCardBtn.setText(answerList.get(currentIndex));
            counter++;
        } else if(counter == 2){
            flashCardBtn.setText("Next Question");
            counter = 0;
        }


    }


    public void handleRestartBtn(ActionEvent actionEvent) {
        counter = 0;
        flashCardBtn.setText("");
        doneList.clear();
        numberOfPlays = 0;
        answerImportBtn.setDisable(false);
        questionImportBtn.setDisable(false);
        flashCardBtn.setDisable(true);
        restartBtn.setDisable(true);
        startBtn.setDisable(false);
        flashCardBtn.setText("Start");

    }

    public void handleQuestionImport(ActionEvent actionEvent) {
        questionList.clear();
        getFile("Choose Questions to import", questionImportBtn, questionList);
    }

    public void handleAnswerImport(ActionEvent actionEvent) {
        answerList.clear();
        getFile("Choose Answers to import", answerImportBtn, answerList);
    }


    private void getFile(String title, Button btn, List listToAdd) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(btn.getScene().getWindow());

        if ((selectedFile != null) && (selectedFile.getPath().substring(selectedFile.getAbsolutePath().lastIndexOf('.')).equals(".txt"))) {
            fileChosen = selectedFile.getAbsolutePath();
            String fileName = selectedFile.getName();

            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().equals("")) {
                        listToAdd.add(line);
                    }
                }

                showAlert("", fileName + " added");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showAlert("ERROR - Not a valid file", "Please choose a .txt file to import");
        }


    }

    private void showAlert(String title, String message) {

        Alert nonSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
        nonSelectedAlert.setTitle(title);
        nonSelectedAlert.setHeaderText(null);
        nonSelectedAlert.setContentText(message);
        nonSelectedAlert.showAndWait();
    }


    private int randomNumber() {

        int n = rand.nextInt(questionList.size());


        while (doneList.contains(n)) {
            n = rand.nextInt(questionList.size());
        }

        doneList.add(n);

        return n;

    }

    public void handleStartAction(ActionEvent actionEvent) {
        if (questionList.isEmpty() || answerList.isEmpty()) {
            showAlert("Question or Answer list not imported", "Please use the import buttons to import a question file AND answer file");
        } else if ((questionList.size() != answerList.size())) {
            showAlert("Question file and Answer file do not match", "Please make sure your question amd answer files match up");
        } else {
            answerImportBtn.setDisable(true);
            questionImportBtn.setDisable(true);
            flashCardBtn.setDisable(false);
            restartBtn.setDisable(false);
            startBtn.setDisable(true);

        }
    }
}
