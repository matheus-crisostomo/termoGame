package com.termo.termogame.views;

import com.termo.termogame.enums.LetterState;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

public class TermoView {
    private final BorderPane root = new BorderPane();
    private final GridPane grid = new GridPane();
    private final Label title = new Label("TERMO DOS CRIA");
    private final Label message = new Label();
    private final Button restartButton = new Button("Reiniciar");
    private final Button sendButton = new Button("Enviar");

    private final int cols;
    private final int rows;
    private final StackPane[][] cells;
    private final Label[][] letters;

    public TermoView(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.cells = new StackPane[rows][cols];
        this.letters = new Label[rows][cols];
        createLayout();
    }

    private void createLayout() {
        root.setPrefSize(600, 800);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        title.setFont(Font.font("Arial Black", 24));
        title.getStyleClass().add("title");
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                StackPane box = new StackPane();
                box.getStyleClass().add("cell");
                Label lbl = new Label("");
                lbl.getStyleClass().add("cell-letter");
                lbl.setFont(Font.font("Arial Black", 36));
                box.getChildren().add(lbl);

                grid.add(box, c, r);
                cells[r][c] = box;
                letters[r][c] = lbl;
            }
        }

        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        title.setFont(Font.font("Arial Black", 24));
        title.getStyleClass().add("title");

        message.getStyleClass().add("message");
        message.setFont(Font.font("Arial", 16));

        sendButton.setFont(Font.font("Arial Black", 18));
        sendButton.getStyleClass().add("send-button");

        restartButton.setFont(Font.font("Arial Black", 18));
        restartButton.getStyleClass().add("restart-button");


        bottomBox.getChildren().addAll(message,sendButton, restartButton);

        root.setCenter(grid);
        root.setBottom(bottomBox);
        root.getStyleClass().add("root");
    }

    public Node getRoot() {
        return root;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public void setCellLetter(int row, int col, String ch) {
        letters[row][col].setText(ch == null ? "" : ch);
    }

    public void clearRow(int row) {
        for (int c = 0; c < cols; c++) setCellLetter(row, c, "");
        for (int c = 0; c < cols; c++) {
            cells[row][c].getStyleClass().removeAll("gray", "yellow", "green");
            cells[row][c].getStyleClass().add("cell");
        }
    }

    public void setCellState(int row, int col, LetterState state) {
        StackPane cell = cells[row][col];
        cell.getStyleClass().removeAll("gray", "yellow", "green");
        switch (state) {
            case GRAY -> cell.getStyleClass().add("gray");
            case YELLOW -> cell.getStyleClass().add("yellow");
            case GREEN -> cell.getStyleClass().add("green");
        }
    }

    public void setMessage(String txt) {
        message.setText(txt);
    }

    public void focusCell(int row, int col) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                cells[r][c].getStyleClass().remove("focused");

        if (row < rows && col < cols && row >= 0 && col >= 0)
            cells[row][col].getStyleClass().add("focused");
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public Button getSendButton() {
        return sendButton;
    }
}
