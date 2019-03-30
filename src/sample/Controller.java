package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.*;

public class Controller {

    @FXML
    private GridPane valuesContainer;
    @FXML
    private TextArea resultText;

    @FXML
    private Pane containerC;

    @FXML
    private Pane containerW;

    private static final int MATRIX_SIZE = 5;

    private int[][] valuesMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private int[] valuesC = new int[MATRIX_SIZE];
    private Float[] valuesW = new Float[MATRIX_SIZE];

    @FXML
    public void initialize() {
        initViews();
    }

    private void initViews() {
        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                refreshValues();
            }
        };
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (i == j) {
                    Label label = new Label();
                    label.setText("   -");
                    label.setPrefHeight(48);
                    label.setPrefWidth(48);
                    valuesContainer.add(label, i, j);
                } else {
                    ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(0, 1));
                    cb.getSelectionModel().selectFirst();
                    cb.setPrefHeight(48);
                    cb.setPrefWidth(48);
                    cb.setOnAction(eventHandler);
                    valuesContainer.add(cb, i, j);
                }
            }
        }
    }

    public void fillRandomValues(ActionEvent actionEvent) {
        setRandomValues();
        refreshValues();
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void refreshValues() {
        clearValues();
        updateValues();
        calculateC();
        calculateW();
        setResult();
    }

    private void clearValues() {
        valuesMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
        valuesC = new int[MATRIX_SIZE];
        valuesW = new Float[MATRIX_SIZE];
    }

    private void updateValues() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (i == j) {
                    continue;
                } else {
                    try {
                        valuesMatrix[i][j] = Integer.parseInt(getChoiceBoxFromGrid(i, j).getValue().toString());
                    } catch (Exception e) {
                        int x;
                    }
                }
            }
        }
    }

    private void setRandomValues() {
        Random random = new Random();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                int value = (random.nextInt(11) + 1) / 10;
                ChoiceBox choiceBox = getChoiceBoxFromGrid(i, j);
                if (choiceBox != null) {
                    choiceBox.setValue(value);
                }
            }
        }
    }

    private void calculateC() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                valuesC[i] += valuesMatrix[i][j];
            }
        }
        int i = 0;
        for (Node children : containerC.getChildren()) {
            ((Label) children).setText(String.valueOf(valuesC[i++]));
        }
    }

    private void calculateW() {
        float sum = Arrays.stream(valuesC).sum();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            valuesW[i] = valuesC[i] / sum;
        }
        int i = 0;
        for (Node children : containerW.getChildren()) {
            ((Label) children).setText(String.format("%.3f", valuesW[i++]));
        }
    }

    private void setResult() {
        List<Pair<Integer, Float>> valuesToIndex = new ArrayList();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            valuesToIndex.add(new Pair(i + 1, valuesW[i]));
        }
        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort(valuesToIndex, new Comparator<Pair<Integer, Float>>() {
            @Override
            public int compare(Pair<Integer, Float> pair1, Pair<Integer, Float> pair2) {
                return Float.compare(pair2.getValue(), pair1.getValue());
            }
        });
        for (int i = 0; i < valuesToIndex.size(); i++) {
            stringBuilder.append(String.format("Z%d = %.3f ; ", valuesToIndex.get(i).getKey(), valuesToIndex.get(i).getValue()));
        }
        resultText.setText(stringBuilder.toString());
    }

    private ChoiceBox getChoiceBoxFromGrid(int row, int col) {
        for (Node node : valuesContainer.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof ChoiceBox) {
                return (ChoiceBox) node;
            }
        }
        return null;
    }
}



