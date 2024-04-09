package org.example.server;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;
import lombok.Getter;
import org.example.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.crypto.*;

@Component
public class GameController {
  @Getter
  private Integer iteration = 0;
  @Getter
  private String gameid;
  private Boolean isGamePlayable;
  private Quiz quiz;
  private ArrayList<Quiz> quizzes;
  private ArrayList<Character> currentWord;
  private Boolean inGuessingCountdown;
  private Boolean isCharacterRevealed;

  public GameController() {
    quizzes = new ArrayList<>();
    File file = new File("D:\\IP\\IP-TMW\\server\\TMWServer\\src\\main\\java\\org\\example\\resource\\database.txt");
    try {
      Scanner scanner = new Scanner(file);
      // Load quizzes from file
      int cnt = Integer.parseInt(scanner.nextLine());
      for (int i = 0; i < cnt; i++) {
        Quiz quiz = new Quiz();
        quiz.setHint(scanner.nextLine());
        quiz.setAnswer(scanner.nextLine());
        quizzes.add(quiz);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void newGame() {
    try {
      gameid = javax.crypto
              .KeyGenerator
              .getInstance("AES")
              .generateKey()
              .toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    setRandomQuiz();
  }

//  public boolean startGame() {}

  public void setRandomQuiz() {
    isGamePlayable = true;
    int idx = (int) (Math.random() * quizzes.size());
    quiz = quizzes.get(idx);
    currentWord = new ArrayList<>();
    for (int i = 0; i < quiz.getAnswer().length(); i++) {
      currentWord.add('_');
    }
  }

  public void startGuessingCountdown(int seconds) {
    inGuessingCountdown = true;
    new Thread(() -> {
      try {
        Thread.sleep(seconds * 1000L);
        inGuessingCountdown = false;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void setCharacterRevealedMode(Boolean status) { isCharacterRevealed = status; }

  public Boolean isPlayersAllowedToGuess() {
    return inGuessingCountdown;
  }

  public Boolean guessCharacter(Character c) {
    boolean found = false;
    for (int i = 0; i < currentWord.size(); i++) {
      if (currentWord.get(i) == '_') {
        if (quiz.getAnswer().charAt(i) == c) {
          found = true;
          currentWord.set(i, c);
        }
      }
    }
    return found;
  }

  public void nextIteration() {
    if (!isCharacterRevealed) iteration++;
    setCharacterRevealedMode(false);
    inGuessingCountdown = false;
  }

  public void reset() {
    iteration = 0;
    isGamePlayable = false;
    isCharacterRevealed = false;
    inGuessingCountdown = false;
  }

  public void stopGame() { isGamePlayable = false; }

  public Boolean isGamePlayable() { return isGamePlayable; }

  public void process() {
    if (isGamePlayable() && !isPlayersAllowedToGuess()) {
      startGuessingCountdown(5);
    }
  }

}
