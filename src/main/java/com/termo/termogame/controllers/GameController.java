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
            if (buffer[currentRow].length() >= model.getTamanhoPalavra()) {
                ev.consume();
                return;
            }

            char up = Character.toUpperCase(ch.charAt(0));
            buffer[currentRow].append(up);
            view.setCellLetter(currentRow, currentCol, String.valueOf(up));

            currentCol++;
            view.celulaFocada(currentRow, Math.min(currentCol, model.getTamanhoPalavra() - 1));
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
            if (currentCol < model.getTamanhoPalavra() - 1) currentCol++;
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

        System.out.println("Nova palavra alvo (DEBUG): " + model.getPalavraAlvo());
    }

    private void sendAttempt() {
        if (buffer[currentRow].length() == model.getTamanhoPalavra()
                && !buffer[currentRow].toString().contains(" ")) {

            String guess = buffer[currentRow].toString();
            EstadoDaLetra[] result = model.verificar(guess);

            for (int c = 0; c < result.length; c++)
                view.setEstadoDasCelulas(currentRow, c, result[c]);

            if (model.isCorrect(guess)) {
                view.setMensagem("Parabéns! Você acertou em " + model.getTentativa() + " tentativa(s)!");
                lockInput();
                return;
            } else if (!model.hasTentativasRestantes()) {
                view.setMensagem("Fim de jogo. A palavra era: (veja console de debug).");
                lockInput();
                return;
            } else {
                currentRow++;
                currentCol = 0;
                view.celulaFocada(currentRow, currentCol);
                view.setMensagem("Tentativa " + (model.getTentativa() + 1) + " de " + model.getMaxTentativas() + ".");
            }
        } else {
            view.setMensagem("A palavra precisa ter " + model.getTamanhoPalavra() + " letras.");
        }
    }
}
