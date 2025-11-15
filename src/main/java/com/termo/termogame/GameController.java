package com.termo.termogame;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    private final GameModel model;
    private final TermoView view;

    private int currentRow = 0;
    private int currentCol = 0;
    private final StringBuilder[] buffer;

    public GameController(GameModel model, TermoView view) {
        this.model = model;
        this.view = view;

        buffer = new StringBuilder[view.getRows()];
        for (int i = 0; i < buffer.length; i++) buffer[i] = new StringBuilder();


        view.getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        view.getRoot().addEventFilter(KeyEvent.KEY_TYPED, this::onKeyTyped);

        view.getRestartButton().setOnAction(e -> restartGame());
        view.getSendButton().setOnAction(e -> sendAttempt());
    }

    public void start() {
        view.setMessage("Digite a palavra de " + model.getWordLength() + " letras. Pressione ENTER para enviar.");
        view.focusCell(currentRow, currentCol);
    }

    private void onKeyTyped(KeyEvent ev) {
        String ch = ev.getCharacter();
        if (ch == null || ch.isEmpty()) return;
        if (ch.charAt(0) < 32) return;

        if (ch.matches("[a-zA-ZÀ-ÿ]")) {
            if (buffer[currentRow].length() >= model.getWordLength()) {
                ev.consume();
                return;
            }

            char up = Character.toUpperCase(ch.charAt(0));
            buffer[currentRow].append(up);
            view.setCellLetter(currentRow, currentCol, String.valueOf(up));

            currentCol++;
            view.focusCell(currentRow, Math.min(currentCol, model.getWordLength() - 1));
        }
    }


    private void onKeyPressed(KeyEvent ev) {
        KeyCode code = ev.getCode();

        if (code == KeyCode.BACK_SPACE) {
            if (buffer[currentRow].length() > 0) {
                int last = buffer[currentRow].length() - 1;
                buffer[currentRow].deleteCharAt(last);
                view.setCellLetter(currentRow, last, "");
                currentCol = last;
                view.focusCell(currentRow, currentCol);
            }
            ev.consume();
            return;
        }
        if (code == KeyCode.ENTER) {
            ev.consume();
            return;
        }
        if (code == KeyCode.LEFT) {
            if (currentCol > 0) currentCol--;
            view.focusCell(currentRow, currentCol);
            ev.consume();
            return;
        }

        if (code == KeyCode.RIGHT) {
            if (currentCol < model.getWordLength() - 1) currentCol++;
            view.focusCell(currentRow, currentCol);
            ev.consume();
            return;
        }
    }

    private void lockInput() {
        view.getRoot().removeEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        view.getRoot().removeEventFilter(KeyEvent.KEY_TYPED, this::onKeyTyped);
    }


    private void unlockInput() {
        view.getRoot().setDisable(false);
    }

    private void restartGame() {
        model.newRandomWord();
        model.resetAttempts();

        for (int i = 0; i < buffer.length; i++)
            buffer[i] = new StringBuilder();

        for (int r = 0; r < view.getRows(); r++)
            view.clearRow(r);

        currentRow = 0;
        currentCol = 0;

        unlockInput();

        view.setMessage("Novo jogo iniciado! Digite a palavra.");
        view.focusCell(0, 0);

        System.out.println("Nova palavra alvo (DEBUG): " + model.getTargetWord());
    }

    private void sendAttempt() {
        if (buffer[currentRow].length() == model.getWordLength()
                && !buffer[currentRow].toString().contains(" ")) {

            String guess = buffer[currentRow].toString();
            LetterState[] result = model.checkGuess(guess);

            for (int c = 0; c < result.length; c++)
                view.setCellState(currentRow, c, result[c]);

            if (model.isCorrect(guess)) {
                view.setMessage("Parabéns! Você acertou em " + model.getAttempt() + " tentativa(s)!");
                lockInput();
                return;
            } else if (!model.hasAttemptsLeft()) {
                view.setMessage("Fim de jogo. A palavra era: (veja console de debug).");
                lockInput();
                return;
            } else {
                currentRow++;
                currentCol = 0;
                view.focusCell(currentRow, currentCol);
                view.setMessage("Tentativa " + (model.getAttempt() + 1) + " de " + model.getMaxAttempts() + ".");
            }
        } else {
            view.setMessage("A palavra precisa ter " + model.getWordLength() + " letras.");
        }
    }
}
