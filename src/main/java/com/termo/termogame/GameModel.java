package com.termo.termogame;

import java.util.*;

public class GameModel {
    private List<String> wordList = new ArrayList<>();
    private String target;
    private final int wordLength = 5;
    private final int maxAttempts = 6;
    private int attempt = 0;

    public GameModel() {
        loadDictionary();
        pickNewTarget();
    }

    private void loadDictionary() {
        try (Scanner sc = new Scanner(Objects.requireNonNull(
                getClass().getResourceAsStream("/com/termo/termogame/dicionario.txt")))) {

            while (sc.hasNextLine()) {
                wordList.add(sc.nextLine().trim().toUpperCase());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar dicionario.txt", e);
        }
    }

    private void pickNewTarget() {
        List<String> filtered = new ArrayList<>();
        for (String w : wordList) {
            if (w.length() == wordLength) filtered.add(w);
        }

        if (filtered.isEmpty())
            throw new IllegalStateException("Nenhuma palavra com o tamanho definido.");

        target = filtered.get(new Random().nextInt(filtered.size()));
        System.out.println("Nova palavra alvo (DEBUG): " + target);
        attempt = 0;
    }

    public void newRandomWord() {
        pickNewTarget();
    }

    public void resetAttempts() {
        attempt = 0;
    }

    public int getWordLength() {
        return wordLength;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttempt() {
        return attempt;
    }

    public boolean hasAttemptsLeft() {
        return attempt < maxAttempts;
    }

    public LetterState[] checkGuess(String guess) {
        guess = guess.toUpperCase();
        LetterState[] result = new LetterState[wordLength];
        char[] targetArr = target.toCharArray();
        char[] guessArr = guess.toCharArray();

        boolean[] markedTarget = new boolean[wordLength];

        for (int i = 0; i < wordLength; i++) {
            if (guessArr[i] == targetArr[i]) {
                result[i] = LetterState.GREEN;
                markedTarget[i] = true;
            }
        }

        for (int i = 0; i < wordLength; i++) {
            if (result[i] == LetterState.GREEN) continue;

            boolean found = false;
            for (int j = 0; j < wordLength; j++) {
                if (!markedTarget[j] && guessArr[i] == targetArr[j]) {
                    found = true;
                    markedTarget[j] = true;
                    break;
                }
            }

            result[i] = found ? LetterState.YELLOW : LetterState.GRAY;
        }

        attempt++;
        return result;
    }

    public boolean isCorrect(String guess) {
        return guess.equalsIgnoreCase(target);
    }
    public String getTargetWord() {
        return target;
    }

}
