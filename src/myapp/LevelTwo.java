package myapp;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.Set;

public class LevelTwo {

    private static final String SECRET_WORD = "wikipidia";
    private static final int GAME_DURATION_SECONDS = 50;
    private Label underscore;
    private Label timerLabel;
    private Timeline gameTimer;
    private int timeRemaining = GAME_DURATION_SECONDS;
    private final Set<Character> guessedLetters = new HashSet<>();
    private MediaPlayer mediaPlayer;

    public Scene createScene(Stage stage) {
        StackPane levelLayout = new StackPane();
        levelLayout.setStyle("-fx-background-color: black;");
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Label phraseLabel = new Label("Every sound you hear...\n"+
        " once had a name.");
        phraseLabel.setTextFill(Color.GREY);
        phraseLabel.setFont(Font.font("Serif", FontWeight.BOLD, 40));
        StackPane.setAlignment(phraseLabel, Pos.CENTER);
        StackPane.setMargin(phraseLabel, new Insets(200, 0, 0, 0));
        levelLayout.getChildren().addAll(phraseLabel);
        PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(event -> {
            levelLayout.getChildren().remove(phraseLabel);
            playVideo(levelLayout, screenWidth, screenHeight);
            startGame(levelLayout, stage);
        });
        pause.play();

        return new Scene(levelLayout, screenWidth, screenHeight);
    }

    private void playVideo(StackPane levelLayout, double width, double height) {
        String videoPath = "file:///C:/Users/Lenovo/Desktop/g1.mp4";
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(width);
        mediaView.setFitHeight(height);
        mediaView.setPreserveRatio(false);
        mediaView.setOpacity(0.7);
        mediaView.setMouseTransparent(true);
        levelLayout.getChildren().add(0, mediaView);
        mediaPlayer.setAutoPlay(true);
    }

    private void startGame(StackPane layout, Stage stage) {
        VBox gameContent = new VBox(20);
        gameContent.setAlignment(Pos.CENTER);
        gameContent.setPadding(new Insets(50, 0, 0, 0));

        underscore = new Label(getHiddenWord());
        underscore.setFont(Font.font("Consolas", FontWeight.BOLD, 36));
        underscore.setTextFill(Color.GREY);

        timerLabel = new Label();
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        timerLabel.setTextFill(Color.RED);
        timerLabel.setVisible(false);
        StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(timerLabel, new Insets(20));

        TextField guessField = new TextField();
        guessField.setPromptText("Enter a letter");
        guessField.setMaxWidth(100);

        Button guessButton = new Button("Guess");
        guessButton.setOnAction(e -> {
            String input = guessField.getText().trim().toLowerCase();
            guessField.clear();

            if (input.length() != 1 || !input.matches("[a-zA-Z]")) {
                new Alert(Alert.AlertType.WARNING, "Please enter one letter.").show();
                return;
            }

            char guessed = input.charAt(0);
            if (guessedLetters.contains(guessed)) {
                new Alert(Alert.AlertType.INFORMATION, "Already guessed that letter.").show();
                return;
            }

            guessedLetters.add(guessed);

            if (SECRET_WORD.indexOf(guessed) >= 0) {
            	underscore.setText(getMaskedWord());
            	underscore.setTextFill(Color.GREY);
                if (!underscore.getText().contains("_")) {
                    gameTimer.stop();
                    stopVideo();
                    showEndScreen(stage, true);
                }
            } else {
            	underscore.setTextFill(Color.DARKRED);
            }
        });

        guessField.setOnAction(e -> guessButton.fire());

        HBox inputBox = new HBox(10, guessField, guessButton);
        inputBox.setAlignment(Pos.CENTER);

        gameContent.getChildren().addAll(underscore, inputBox);

        layout.getChildren().addAll(gameContent, timerLabel);
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            if (timeRemaining <= 8) {
                timerLabel.setText("⏳ " + timeRemaining + "s");
                timerLabel.setVisible(true);
            }
            if (timeRemaining <= 0) {
                gameTimer.stop();
                stopVideo();
                showEndScreen(stage, false);
            }
        }));

        gameTimer.setCycleCount(GAME_DURATION_SECONDS);
        gameTimer.play();
    }

    private String getHiddenWord() {
        return "_ ".repeat(SECRET_WORD.length()).trim();
    }

    private String getMaskedWord() {
        StringBuilder masked = new StringBuilder();
        for (char c : SECRET_WORD.toCharArray()) {
            if (guessedLetters.contains(c)) {
                masked.append(c).append(" ");
            } else {
                masked.append("_ ");
            }
        }
        return masked.toString().trim();
    }

    private void showEndScreen(Stage stage, boolean won) {
        VBox endLayout = new VBox(20);
        endLayout.setAlignment(Pos.CENTER);
        endLayout.setStyle("-fx-background-color: black;");

        Label endLabel = new Label(won ? "\t\t\tVictory?\n\tOr just a brief hallucination?" : "You couldn't save them...\n \tIt thanks you (;");
        endLabel.setFont(Font.font("Serif", FontWeight.BOLD, 32));
        endLabel.setTextFill(Color.DARKRED);

        Button nextButton = new Button("deeper we go...");
        nextButton.setOnAction(e -> {
            LevelThree levelThree = new LevelThree();
            Scene nextScene = levelThree.createScene(stage);
            stage.setScene(nextScene);
        });

        endLayout.getChildren().addAll(endLabel, nextButton);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Scene endScene = new Scene(endLayout, screenWidth, screenHeight);
        stage.setScene(endScene);
    }

    private void stopVideo() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }
}
