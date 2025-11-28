package com.termo.termogame.models;

import com.termo.termogame.enums.EstadoDaLetra;

import java.util.*;

public class GameModel {
    private List<String> listaPalavras = new ArrayList<>();
    private String palavraAlvo;
    private final int tamanhoPalavra = 5;
    private final int maxTentativas = 6;
    private int tentativa = 0;

    public GameModel() {
        carregarDicionario();
        pegarNovaPalavra();
    }

    private void carregarDicionario() {
        try (Scanner sc = new Scanner(Objects.requireNonNull(
                getClass().getResourceAsStream("/com/termo/termogame/dicionario.txt")))) {

            while (sc.hasNextLine()) {
                listaPalavras.add(sc.nextLine().trim().toUpperCase());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar dicionario.txt", e);
        }
    }

    private void pegarNovaPalavra() {
        List<String> filtered = new ArrayList<>();
        for (String w : listaPalavras) {
            if (w.length() == tamanhoPalavra) filtered.add(w);
        }

        if (filtered.isEmpty())
            throw new IllegalStateException("Nenhuma palavra com o tamanho definido.");

        palavraAlvo = filtered.get(new Random().nextInt(filtered.size()));
        System.out.println("Nova palavra alvo (DEBUG): " + palavraAlvo);
        tentativa = 0;
    }

    public void novaPalavra() {
        pegarNovaPalavra();
    }

    public void resetarTentativas() {
        tentativa = 0;
    }

    public int getTamanhoPalavra() {
        return tamanhoPalavra;
    }

    public int getMaxTentativas() {
        return maxTentativas;
    }

    public int getTentativa() {
        return tentativa;
    }

    public boolean hasTentativasRestantes() {
        return tentativa < maxTentativas;
    }

    public EstadoDaLetra[] verificar(String guess) {
        guess = guess.toUpperCase();
        EstadoDaLetra[] result = new EstadoDaLetra[tamanhoPalavra];
        char[] palavraAlvoArr = palavraAlvo.toCharArray();
        char[] guessArr = guess.toCharArray();

        boolean[] letrasUsadas = new boolean[tamanhoPalavra];

        for (int i = 0; i < tamanhoPalavra; i++) {
            if (guessArr[i] == palavraAlvoArr[i]) {
                result[i] = EstadoDaLetra.GREEN;
                letrasUsadas[i] = true;
            }
        }

        for (int i = 0; i < tamanhoPalavra; i++) {
            if (result[i] == EstadoDaLetra.GREEN) continue;

            boolean found = false;
            for (int j = 0; j < tamanhoPalavra; j++) {
                if (!letrasUsadas[j] && guessArr[i] == palavraAlvoArr[j]) {
                    found = true;
                    letrasUsadas[j] = true;
                    break;
                }
            }

            result[i] = found ? EstadoDaLetra.YELLOW : EstadoDaLetra.GRAY;
        }

        tentativa++;
        return result;
    }

    public boolean isCorrect(String guess) {
        return guess.equalsIgnoreCase(palavraAlvo);
    }
    public String getPalavraAlvo() {
        return palavraAlvo;
    }

}
