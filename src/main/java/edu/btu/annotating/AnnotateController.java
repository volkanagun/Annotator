package edu.btu.annotating;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.spreadsheet.Grid;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AnnotateController implements Initializable {


    @FXML
    private GridPane gridPane;

    @FXML
    private Button loadButton;

    @FXML
    private TextField textField;

    private int maxHSize = 4, maxVSize = 10;
    private int minWidth = 400, percent = 25;
    @FXML
    private ProgressBar progressBar;

    private AnalysisIO io = new AnalysisIO();


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    private Label wordLabel(String word) {
        Label label = new Label(word);
        Font font = Font.font("Verdana", FontWeight.BOLD, 15);
        label.setFont(font);
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }

    private Font analysisFont(boolean isSelected) {
        return (isSelected ? Font.font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, 12) : Font.font("Verdana", FontWeight.BOLD, 12));
    }

    private Label analysisLabel(int paragraphKey, int sentenceKey, String difference, Analysis analysis) {
        Label label = new Label(difference);
        Boolean isSelected = io.isChecked(paragraphKey, sentenceKey, analysis);
        Font font = analysisFont(isSelected);
        label.setFont(font);
        label.setTextFill(Color.BLUE);

        return label;
    }

    private RadioButton analysisCheck(int paragraphKey, int sentenceKey, Analysis analysis, Analysis selected) {

        RadioButton radioButton = new RadioButton();
        Font font = Font.font("Verdana", FontWeight.BOLD, 10);

        radioButton.setPadding(new Insets(0, 0, 0, 5));
        radioButton.setFont(font);
        radioButton.setTextFill(Color.AZURE);
        radioButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (radioButton.isSelected()) io.check(paragraphKey, sentenceKey, analysis);
            }
        });

        return radioButton;
    }

    private void initPane(int wordSize) {
        int maxRows = (wordSize / maxHSize) * (maxVSize + 1);

        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        for (int i = 0; i < maxHSize; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHalignment(HPos.LEFT);
            column.setMinWidth(minWidth);
            column.setHgrow(Priority.ALWAYS);
            column.setPercentWidth(percent);
            gridPane.getColumnConstraints().add(column);
        }

        for (int i = 0; i < maxRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setMaxHeight(40);
            row.setMinHeight(20);
            row.setVgrow(Priority.ALWAYS);
            row.setPercentHeight(20);
            gridPane.getRowConstraints().add(row);
        }

        gridPane.setPadding(new Insets(150, 0, 0, 50));
        gridPane.setGridLinesVisible(true);
    }

    public List<String> difference(List<Analysis> analysisList) {
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < analysisList.size(); i++) {
            String[] array = analysisList.get(i).getAnnotation().split("\\+");
            List<String> diffSet = new ArrayList<>();
            for (String item : array) {
                boolean exists = false;

                for (int j = 0; j < analysisList.size(); j++) {
                    if (j != i) {
                        Analysis analysis = analysisList.get(j);
                        if (analysis.getAnnotation().contains(item)) exists = true;
                    }
                }

                if (!exists) diffSet.add(item);
            }

            if (diffSet.isEmpty()) {
                List<String> arrayList = new ArrayList<>();
                diffSet.add(analysisList.get(i).getAnnotation());
            }

            String text = "";
            for (String item : diffSet) {
                text += "+" + item;
            }

            resultList.add(text);
        }


        return resultList;
    }

    private void initTable(AnalysisSequence analysisSequence) {


        int paragraphKey = analysisSequence.paragraphKey;
        int sentenceKey = analysisSequence.hashCode();
        List<String> words = analysisSequence.getWords();
        List<List<Analysis>> analysisList = analysisSequence.getSuggestedList();
        List<Analysis> selectedList = analysisSequence.getSelectedList();
        initPane(words.size());

        for (int i = 0; i < words.size(); i++) {

            int icol = i % maxHSize;
            int irow = i / maxHSize * maxVSize;

            String word = words.get(i);
            Label labelWord = wordLabel(word);

            gridPane.add(labelWord, icol, irow, 1, 1);

            List<Analysis> allAnalysis = analysisList.get(i);
            Analysis selectedAnalysis = selectedList.get(i);
            List<String> differenceList = difference(allAnalysis);
            ToggleGroup toggleGroup = new ToggleGroup();
            for (int r = 0; r < differenceList.size(); r++) {
                Analysis currentAnalysis = allAnalysis.get(r);
                Label labelAnalysis = analysisLabel(paragraphKey, sentenceKey, differenceList.get(r), currentAnalysis);
                RadioButton checkAnalysis = analysisCheck(paragraphKey, sentenceKey, currentAnalysis, selectedAnalysis);
                checkAnalysis.setToggleGroup(toggleGroup);

                if (currentAnalysis.equals(selectedAnalysis)) {
                    checkAnalysis.setSelected(true);
                } else {
                    checkAnalysis.setSelected(false);
                }

                GridPane pane = new GridPane();
                ColumnConstraints column1 = new ColumnConstraints();
                ColumnConstraints column2 = new ColumnConstraints();
                column1.setMinWidth(300);
                column2.setMinWidth(50);
                pane.getColumnConstraints().add(column1);
                pane.getColumnConstraints().add(column2);
                pane.add(labelAnalysis, 0, 0, 1, 1);
                pane.add(checkAnalysis, 1, 0, 1, 1);
                pane.setAlignment(Pos.CENTER_LEFT);
                gridPane.add(pane, icol, irow + r + 1, 1, 1);


            }

        }

    }

    public void next(ActionEvent event) {
        AnalysisSequence sequence = io.next();
        initTable(sequence);
        io.saveXML();
    }

    public void previous(ActionEvent event) {
        AnalysisSequence sequence = io.previous();
        initTable(sequence);
    }

    public void load(ActionEvent event) {
        String lines = textField.getText();
        if (lines.trim().isEmpty()) io.initialize(progressBar, 10);
        else io.initialize(progressBar, Integer.parseInt(lines.trim()));
    }


}