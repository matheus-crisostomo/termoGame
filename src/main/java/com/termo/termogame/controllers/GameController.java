package com.termo.termogame.controllers;

import com.termo.termogame.models.GameModel;
import com.termo.termogame.enums.EstadoDaLetra;
import com.termo.termogame.views.TermoView;
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

        buffer = new StringBuilder[view.getLinhas()];
        for (int i = 0; i < buffer.length; i++) buffer[i] = new StringBuilder();

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

            if (buffer[currentRow].length() >= max && currentCol >= buffer[currentRow].length()) {
                ev.consume();
                return;
            }

            char up = Character.toUpperCase(ch.charAt(0));

            if (currentCol < buffer[currentRow].length()) {
                buffer[currentRow].setCharAt(currentCol, up);
            } else {
                buffer[currentRow].insert(currentCol, up);
            }

            if (buffer[currentRow].length() > max) {
                buffer[currentRow].setLength(max);
            }

            atualizarViewDaLinha(currentRow);

            if (currentCol < max - 1) currentCol++;
            view.celulaFocada(currentRow, currentCol);

            ev.consume();
        }
    }

    private void onKeyPressed(KeyEvent ev) {
        KeyCode code = ev.getCode();

        if (code == KeyCode.BACK_SPACE) {
            if (currentCol > 0 || buffer[currentRow].length() > 0) {
                if (currentCol > 0 && currentCol <= buffer[currentRow].length()) {
                    buffer[currentRow].deleteCharAt(currentCol - 1);
                    currentCol--;
                } else if (buffer[currentRow].length() > 0) {
                    buffer[currentRow].deleteCharAt(buffer[currentRow].length() - 1);
                    currentCol = Math.max(0, buffer[currentRow].length());
                }

                atualizarViewDaLinha(currentRow);
                view.celulaFocada(currentRow, currentCol);
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
            view.celulaFocada(currentRow, currentCol);
            ev.consume();
            return;
        }

        if (code == KeyCode.RIGHT) {
            int max = model.getTamanhoPalavra();
            if (currentCol < max - 1) currentCol++;
            if (currentCol > buffer[currentRow].length()) currentCol = buffer[currentRow].length();
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

        for (int i = 0; i < buffer.length; i++)
            buffer[i] = new StringBuilder();

        for (int r = 0; r < view.getLinhas(); r++)
            view.limparLinhas(r);

        currentRow = 0;
        currentCol = 0;

        unlockInput();

        view.setMensagem("Novo jogo iniciado! Digite a palavra.");
        view.celulaFocada(0, 0);
        view.getSendButton().setDisable(false);

        System.out.println("Nova palavra alvo (DEBUG): " + model.getPalavraAlvo());
    }

    private void sendAttempt() {
        int tamanho = model.getTamanhoPalavra();
        if (buffer[currentRow].length() == tamanho && !buffer[currentRow].toString().contains(" ")) {

            String guess = buffer[currentRow].toString();
            EstadoDaLetra[] result = model.verificar(guess);

            for (int c = 0; c < result.length; c++)
                view.setEstadoDasCelulas(currentRow, c, result[c]);

            if (model.isCorrect(guess)) {
                view.setMensagem("Parabéns! Você acertou em " + model.getTentativa() + " tentativa(s)!");
                view.getSendButton().setDisable(true);
                lockInput();
                return;
            } else if (!model.hasTentativasRestantes()) {
                view.setMensagem("Fim de jogo. A palavra era: (veja console de debug).");
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
        StringBuilder sb = buffer[r];

        for (int c = 0; c < cols; c++) {
            if (c < sb.length()) {
                view.setCellLetter(r, c, String.valueOf(sb.charAt(c)));
            } else {
                view.setCellLetter(r, c, "");
            }
            view.getLayout();
        }
    }
}
