package com.termo.termogame.controllers;

import com.termo.termogame.models.GameModel;
import com.termo.termogame.enums.EstadoDaLetra;
import com.termo.termogame.views.TermoView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    private final GameModel model;
    private final TermoView view;

    private boolean jogoEncerrado = false;


    private int currentRow = 0;
    private int currentCol = 0;
    private final char[][] buffer;

    public GameController(GameModel model, TermoView view) {
        this.model = model;
        this.view = view;

        buffer = new char[view.getLinhas()][model.getTamanhoPalavra()];
        for (int r = 0; r < buffer.length; r++) {
            for (int c = 0; c < buffer[r].length; c++) {
                buffer[r][c] = '\0';
            }
        }

        view.getLayout().addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        view.getLayout().addEventFilter(KeyEvent.KEY_TYPED, this::onKeyTyped);

        view.getRestartButton().setOnAction(e -> restartGame());
        view.getSendButton().setOnAction(e -> sendAttempt());
    }

    public void start() {
        view.setMensagem("Digite a palavra de " + model.getTamanhoPalavra() + " letras. Clique no botão ENVIAR apos digitar.");
        view.celulaFocada(currentRow, currentCol);
    }

    private void onKeyTyped(KeyEvent ev) {
        String ch = ev.getCharacter();
        if (ch == null || ch.isEmpty()) return;
        if (ch.charAt(0) < 32) return;

        if (ch.matches("[a-zA-ZÀ-ÿ]")) {
            int max = model.getTamanhoPalavra();


            if (currentCol >= max) {
                ev.consume();
                return;
            }

            char up = Character.toUpperCase(ch.charAt(0));
            buffer[currentRow][currentCol] = up;

            atualizarViewDaLinha(currentRow);

            if (currentCol < max - 1) currentCol++;
            view.celulaFocada(currentRow, currentCol);

            ev.consume();
        }
    }

    private void onKeyPressed(KeyEvent ev) {
        KeyCode code = ev.getCode();

        if (code == KeyCode.BACK_SPACE) {
            if (currentCol >= 0) {
                buffer[currentRow][currentCol] = '\0';
                atualizarViewDaLinha(currentRow);
                view.celulaFocada(currentRow, currentCol);
            }
            ev.consume();
            return;
        }
        if (code == KeyCode.ENTER) {
            sendAttempt();
            ev.consume();
            return;
        }
        if (code == KeyCode.LEFT) {
            if (currentCol > 0) currentCol--;
            view.celulaFocada(currentRow, currentCol);
            ev.consume();
            return;
        }

        if (code == KeyCode.RIGHT) {
            int max = model.getTamanhoPalavra();
            if (currentCol < max - 1) currentCol++;
            view.celulaFocada(currentRow, currentCol);
            ev.consume();
            return;
        }
    }

    private void lockInput() {
        view.getLayout().removeEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        view.getLayout().removeEventFilter(KeyEvent.KEY_TYPED, this::onKeyTyped);
    }

    private void unlockInput() {
        view.getLayout().setDisable(false);
    }

    private void restartGame() {
        model.novaPalavra();
        model.resetarTentativas();
        jogoEncerrado = false;

        for (int r = 0; r < buffer.length; r++) {
            for (int c = 0; c < buffer[r].length; c++) {
                buffer[r][c] = '\0';
            }
        }

        for (int r = 0; r < view.getLinhas(); r++)
            view.limparLinhas(r);

        currentRow = 0;
        currentCol = 0;

        unlockInput();

        view.setMensagem("Novo jogo iniciado! Digite a palavra.");
        view.celulaFocada(0, 0);
        view.getSendButton().setDisable(false);

        System.out.println("Nova palavra alvo (DEBUG): "
                + model.getPalavraAlvo());
    }

    private void sendAttempt() {
        if (jogoEncerrado) return;
        int tamanho = model.getTamanhoPalavra();
        int count = 0;
        for (char ch : buffer[currentRow]) {
            if (ch != '\0') count++;
        }

        if (count == tamanho) {
            StringBuilder sb = new StringBuilder();
            for (char ch : buffer[currentRow]) {
                if (ch != '\0') sb.append(ch);
            }
            String guess = sb.toString();

            EstadoDaLetra[] result = model.verificar(guess);

            for (int c = 0; c < result.length; c++)
                view.setEstadoDasCelulas(currentRow, c, result[c]);

            if (model.isCorrect(guess)) {
                view.setMensagem("Parabéns! Você acertou em " + model.getTentativa() + " tentativa(s)!");
                view.getSendButton().setDisable(true);
                jogoEncerrado = true;
                lockInput();
                return;
            } else if (!model.hasTentativasRestantes()) {
                view.setMensagem("Fim de jogo. A palavra era: "+model.getPalavraAlvo());
                jogoEncerrado = true;
                lockInput();
                return;
            } else {
                currentRow++;
                currentCol = 0;
                if (currentRow >= view.getLinhas()) {
                    currentRow = view.getLinhas() - 1;
                    lockInput();
                    view.setMensagem("Número de linhas excedido.");
                    return;
                }
                view.celulaFocada(currentRow, currentCol);
                view.setMensagem("Tentativa " + (model.getTentativa() + 1) + " de " + model.getMaxTentativas() + ".");
            }
        } else {
            view.setMensagem("A palavra precisa ter " + tamanho + " letras.");
        }
    }

    private void atualizarViewDaLinha(int r) {
        int cols = model.getTamanhoPalavra();

        for (int c = 0; c < cols; c++) {
            char ch = buffer[r][c];
            view.setCellLetter(r, c, ch == '\0' ? "" : String.valueOf(ch));
        }
    }
}
