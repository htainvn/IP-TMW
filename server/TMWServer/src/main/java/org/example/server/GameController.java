package org.example.server;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.example.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.crypto.*;

@Component
public class GameController {
  @Getter
  @Setter
  private Integer numPlayer = 0;
  private Integer round = 0;

  private Integer iteration = -1;

  private Integer lastIteration = 0;

  @Getter
  private String gameid = "";
  private Boolean isGamePlayable = Boolean.FALSE;
  private Quiz quiz = new Quiz();
  private ArrayList<Quiz> quizzes = new ArrayList<>();
  private ArrayList<Character> currentWord = new ArrayList<>();
  private Boolean inGuessingCountdown = Boolean.FALSE;
  private Boolean isCharacterRevealed = Boolean.TRUE;

  public GameController() {
    quizzes = new ArrayList<>();
    File currentDirFile = new File(".");
    String helper = currentDirFile.getAbsolutePath();
    Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
    File file = new File(root.toString() + "/src/main/resources/database.txt");
    try {
      Scanner scanner = new Scanner(file);
      // Load quizzes from file
      int cnt = Integer.parseInt(scanner.nextLine());
      for (int i = 0; i < cnt; i++) {
        Quiz quiz = new Quiz();
        quiz.setAnswer(scanner.nextLine().toLowerCase().replaceAll(" ", ""));
        quiz.setHint(scanner.nextLine());
        quizzes.add(quiz);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void newGame() {
    gameid = UUID.randomUUID().toString();
    setRandomQuiz();
  }

  public Integer getIteration() {
    return iteration % numPlayer;
  }

//  public boolean startGame() {}

  public void setRandomQuiz() {
    int idx = (int) (Math.random() * quizzes.size());
    quiz = quizzes.get(idx);
    currentWord = new ArrayList<>();
    for (int i = 0; i < quiz.getAnswer().length(); i++) {
      if (quiz.getAnswer().charAt(i) == ' ') {
        currentWord.add(' ');
      } else {
        currentWord.add('_');
      }
    }
  }

  public void setGuessMode(Boolean status) {
    inGuessingCountdown = status;
  }

  public void startGuessingCountdown(int seconds) {
    inGuessingCountdown = Boolean.TRUE;
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

  public Boolean guessKeyword(String keyword) {
    if (quiz.getAnswer().equals(keyword)) {
      for (int i = 0; i < currentWord.size(); i++) {
        currentWord.set(i, quiz.getAnswer().charAt(i));
      }
      return true;
    }
    return false;
  }

  public void nextIteration() {
    if (!isCharacterRevealed) iteration++;
    setCharacterRevealedMode(false);
    inGuessingCountdown = false;
  }

  public void reset() {
    iteration = 0;
    isCharacterRevealed = true;
    inGuessingCountdown = false;
  }

  public void stopGame() {
    iteration = -1;
    numPlayer = 0;
    isGamePlayable = false;
    isCharacterRevealed = true;
    inGuessingCountdown = false;
  }

  public Boolean isGamePlayable() { return isGamePlayable; }

  public void start() {
    isGamePlayable = true;
    iteration = 0;
  }

  public String getGameState() {
    return "GameID: " + gameid + "\n"
        + "Hint: " + quiz.getHint() + "\n"
        + "Current: " + currentWord + "\n";
  }

  public Boolean ifGameEnds() {
    /*
    if (round > 5) {
      return true;
    }
    else {
      return false;
    }
     */
    return iteration > 5 || currentWord.toString().equals(quiz.getAnswer());
  }

  public void setNewRound() {
    round++;
    iteration = 0;
  }

  public String getHint() {
    return quiz.getHint();
  }

    public Integer getCurrentIteration() {
        return iteration;
    }
}
