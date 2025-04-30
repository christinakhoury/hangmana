package myapp;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import java.util.*;

public class LevelOne {
    private final String secretWord = "enthusiastic";
    private final Set<Character> guessedLetters = new HashSet<>();
    private Label wordLabel;
    private int timeLeft = 50;
    private boolean gameEnded = false;
    private int wrongGuessCount = 0;
    private MediaPlayer mediaPlayer;

    public Scene createScene(Stage stage) {
        StackPane levelLayout = new StackPane();
        levelLayout.setStyle("-fx-background-color: black;");

        Label riddleLabel = new Label(
                "\tTwo whispers guide your way,\n" +
                        "one speaks truth, the other leads astray...\n" +
                        "\tTrust wrong and catch it all decay"
        );
        riddleLabel.setTextFill(Color.GREY);
        riddleLabel.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 35));
        riddleLabel.setWrapText(true);
        riddleLabel.setAlignment(Pos.CENTER);
        levelLayout.getChildren().add(riddleLabel);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            levelLayout.getChildren().remove(riddleLabel);
            playVideo(levelLayout);
            startGuessingGame(levelLayout);
            scheduleHintDisplay(levelLayout);
        });
        pause.play();

        Scene scene = new Scene(levelLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        stage.setMaximized(true); 
        scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustLayout(levelLayout));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustLayout(levelLayout));
        return scene;
    }

    private void adjustLayout(StackPane levelLayout) {
    }

    private void playVideo(StackPane levelLayout) {
        String vid = "file:///C:/Users/Lenovo/Desktop/g2.mp4";
        Media media = new Media(vid);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(false);
        mediaView.setOpacity(0.4);
        mediaView.setMouseTransparent(true);
        mediaView.fitWidthProperty().bind(levelLayout.widthProperty());
        mediaView.fitHeightProperty().bind(levelLayout.heightProperty());
        levelLayout.getChildren().add(0, mediaView);
        mediaPlayer.setAutoPlay(true);
    }

    private void startGuessingGame(StackPane levelLayout) {
        VBox guessBox = new VBox(20);
        guessBox.setAlignment(Pos.CENTER);
        guessBox.setPadding(new Insets(40, 0, 0, 0));
        wordLabel = new Label(maskWord());
        wordLabel.setTextFill(Color.WHITE);
        wordLabel.setFont(Font.font("Serif", FontWeight.BOLD, 40));
        TextField guessField = new TextField();
        guessField.setPromptText("Enter a letter");
        guessField.setMaxWidth(100);

        Button submitGuessButton = new Button("Guess");

        submitGuessButton.setOnAction(e -> {
            if (gameEnded) return;

            String guess = guessField.getText().trim().toLowerCase();
            guessField.clear();

            if (guess.length() != 1 || !guess.matches("[a-zA-Z]")) {
                new Alert(Alert.AlertType.WARNING, "Please enter a single alphabet letter.").show();
                return;
            }

            char guessedChar = guess.charAt(0);
            if (guessedLetters.contains(guessedChar)) {
                new Alert(Alert.AlertType.INFORMATION, "You already guessed that letter.").show();
                return;
            }

            guessedLetters.add(guessedChar);

            if (secretWord.indexOf(guessedChar) >= 0) {
                wordLabel.setText(maskWord());
                wordLabel.setTextFill(Color.WHITE);
                if (!wordLabel.getText().contains("_")) {
                    gameEnded = true;
                    Stage stage = (Stage) wordLabel.getScene().getWindow();
                    stopVideo();
                    showWinScreen(stage);
                }
            } else {
                wrongGuessCount++;
                indicateWrongGuess();
                new Alert(Alert.AlertType.WARNING, "☠ Careful! It's hungry for errors...").show();

                if (wrongGuessCount >= 8) {
                    gameEnded = true;
                    Stage stage = (Stage) wordLabel.getScene().getWindow();
                    stopVideo();
                    showLossScreen(stage);
                }
            }
        });
        guessField.setOnAction(e -> submitGuessButton.fire());
        HBox inputBox = new HBox(10, guessField, submitGuessButton);
        inputBox.setAlignment(Pos.CENTER);
        guessBox.getChildren().addAll(wordLabel, inputBox);
        StackPane.setAlignment(guessBox, Pos.TOP_CENTER);
        levelLayout.getChildren().add(guessBox);

        Label timerLabel = new Label("Time Left: 50s");
        timerLabel.setFont(Font.font("Creepster", FontWeight.EXTRA_BOLD, 28));
        timerLabel.setTextFill(Color.LIGHTGRAY);
        StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(timerLabel, new Insets(20, 30, 0, 0));
        levelLayout.getChildren().add(timerLabel);
        startTimer(levelLayout, timerLabel);}

    private void startTimer(StackPane levelLayout, Label timerLabel) {
        FadeTransition flash = new FadeTransition(Duration.millis(500), timerLabel);
        flash.setFromValue(1.0);
        flash.setToValue(0.3);
        flash.setCycleCount(Animation.INDEFINITE);
        flash.setAutoReverse(true);
        flash.play();
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (gameEnded) return;
            timeLeft--;
            Platform.runLater(() -> {
                timerLabel.setText(timeLeft + "s");

                if (timeLeft == 10) {
                    flash.stop();
                    flash.setDuration(Duration.millis(200));
                    flash.play();
                }

                if (timeLeft <= 0) {
                    flash.stop();
                    timerLabel.setText("Time's Up!");
                    timerLabel.setTextFill(Color.DARKRED);
                    gameEnded = true;
                    Stage stage = (Stage) levelLayout.getScene().getWindow();
                    stopVideo();
                    showLossScreen(stage);
                }
            });
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private String maskWord() {
        StringBuilder masked = new StringBuilder();
        for (char c : secretWord.toCharArray()) {
            if (guessedLetters.contains(c)) {
                masked.append(c).append(" ");
            } else {
                masked.append("_ ");
            }
        }
        return masked.toString().trim();
    }

    private void indicateWrongGuess() {
        wordLabel.setTextFill(Color.RED);
    }

    private void scheduleHintDisplay(StackPane layout) {
        List<String[]> hintPairs = Arrays.asList(
                new String[]{"Heart beats fast", "Heart feels slow"},
                new String[]{"Can't wait to begin", "Wishing it would end"},
                new String[]{"Like a child on their birthday", "Like someone falling asleep"}
        );

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> showHints(layout, hintPairs.get(0))),
                new KeyFrame(Duration.seconds(20), e -> showHints(layout, hintPairs.get(1))),
                new KeyFrame(Duration.seconds(35), e -> showHints(layout, hintPairs.get(2)))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showHints(StackPane layout, String[] pair) {
        Collections.shuffle(Arrays.asList(pair));

        Label hintLeft = new Label(pair[0]);
        Label hintRight = new Label(pair[1]);
        hintLeft.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 28));
        hintRight.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 28));
        hintLeft.setTextFill(Color.DARKRED);
        hintRight.setTextFill(Color.DARKRED);
        hintLeft.setStyle("-fx-effect: dropshadow(gaussian, black, 6, 0.5, 2, 2);");
        hintRight.setStyle("-fx-effect: dropshadow(gaussian, black, 6, 0.5, 2, 2);");
        hintLeft.setWrapText(true);
        hintRight.setWrapText(true);
        hintLeft.setMaxWidth(220);
        hintRight.setMaxWidth(220);
        hintLeft.setOpacity(0);
        hintRight.setOpacity(0);

        StackPane.setAlignment(hintLeft, Pos.CENTER_LEFT);
        StackPane.setMargin(hintLeft, new Insets(120, 0, 0, 100));
        StackPane.setAlignment(hintRight, Pos.CENTER_RIGHT);
        StackPane.setMargin(hintRight, new Insets(120, 100, 0, 0));
        layout.getChildren().addAll(hintLeft, hintRight);

        for (Label hint : new Label[]{hintLeft, hintRight}) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), hint);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(1), hint);
            scaleUp.setFromX(1);
            scaleUp.setFromY(1);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            new ParallelTransition(fadeIn, scaleUp).play();
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> {
            for (Label hint : new Label[]{hintLeft, hintRight}) {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), hint);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(ev -> layout.getChildren().remove(hint));
                fadeOut.play();
            }
        });
        delay.play();
    }

    private void showLossScreen(Stage stage) {
        StackPane lossLayout = new StackPane();
        lossLayout.setStyle("-fx-background-color: black;");

        Label lossMessage = new Label("They begged, you guessed...\n and now they are dead...");
        lossMessage.setTextFill(Color.DARKRED);
        lossMessage.setFont(Font.font("Serif", FontWeight.BOLD, 40));

        Button continueButton = new Button("You couldn't save them...Maybe you could save the next (;");
        continueButton.setOnAction(e -> {
            stopVideo();
            LevelTwo levelTwo = new LevelTwo();
            Scene levelTwoScene = levelTwo.createScene(stage);
            stage.setScene(levelTwoScene);
            stage.show();
        });

        VBox vbox = new VBox(30, lossMessage, continueButton);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(continueButton, new Insets(20, 0, 0, 0));
        lossLayout.getChildren().add(vbox);

        Scene lossScene = new Scene(lossLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        stage.setMaximized(true); 
        stage.setScene(lossScene);
        stage.show();
    }

    private void showWinScreen(Stage stage) {
        StackPane winLayout = new StackPane();
        winLayout.setStyle("-fx-background-color: black;");

        Label winMessage = new Label("\t\tYou survived...\nBut the voices are still whispering...");
        winMessage.setTextFill(Color.DARKRED);
        winMessage.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, 36));
        winMessage.setWrapText(true);
        winMessage.setAlignment(Pos.CENTER);

        Button continueButton = new Button("Continue...");
        continueButton.setOnAction(e -> {
            stopVideo();
            LevelTwo levelTwo = new LevelTwo();
            Scene levelTwoScene = levelTwo.createScene(stage);
            stage.setScene(levelTwoScene);
            stage.show();
        });

        VBox vbox = new VBox(30, winMessage, continueButton);
        vbox.setAlignment(Pos.CENTER);
        winLayout.getChildren().add(vbox);

        Scene winScene = new Scene(winLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        stage.setMaximized(true); 
        stage.setScene(winScene);
        stage.show();
    }

    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
