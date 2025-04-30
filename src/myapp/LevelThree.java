package myapp;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LevelThree {

    private static final String SECRET_WORD = "neurotransmitter";
    private static final int GAME_DURATION_SECONDS = 45;
    private static final String[] MATH_WORDS = {
        "Matrix", "Arithmetic", "Parabola", "Limit", "Integral", "Function", "Hypotenuse",
        "Radius", "Tangent", "Logarithm", "Binomial", "Equation", "Theorem", "Modulus",
        "Factorial", "Polynomial", "Gradient", "Asymptote", "Discriminant", "Vertex",
        "Domain", "Range", "Rational", "Sine", "Cosine", "Area", "Volume", "Numeric",
        "Sequence", "Addition", "Subtraction", "Multiplication", "Division", "Fraction",
        "Decimal", "Percentage", "Average", "Triangle", "Square", "Circle", "Number",
        "Even", "Odd", "Angle", "Length", "Width", "Height", "Chart", "Graph"
    };

    private Label underscore;
    private Label timerLabel;
    private int timeRemaining = GAME_DURATION_SECONDS;
    private final Set<Character> guessedLetters = new HashSet<>();
    private MediaPlayer mediaPlayer;
    private Timeline gameTimer;
    private Timeline wordSpawner;
    private boolean gameEnded = false;

    public Scene createScene(Stage stage) {
        StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: black;");
        Label text = new Label("Welcome to the place where \n"+" your mind betrays you");
        text.setFont(Font.font("Serif", FontWeight.BOLD, 36));
        text.setTextFill(Color.GREY);
        StackPane.setAlignment(text, Pos.CENTER);
        layout.getChildren().add(text);

        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> {
            layout.getChildren().remove(text);
            playVideo(layout);
            startGame(stage, layout);
        });
        pause.play();

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        return new Scene(layout, screenWidth, screenHeight);
    }

    private void playVideo(StackPane layout) {
        String videoPath = "file:///C:/Users/Lenovo/Desktop/p2.mp4";
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(1000);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false);
        mediaView.setOpacity(0.5);
        mediaView.setMouseTransparent(true);

        layout.getChildren().add(0, mediaView);
        mediaPlayer.setAutoPlay(true);
    }

    private void startGame(Stage stage, StackPane layout) {
        VBox gameContent = new VBox(20);
        gameContent.setAlignment(Pos.CENTER);
        gameContent.setPadding(new Insets(50, 0, 0, 0));

        underscore = new Label(getHiddenWord());
        underscore.setFont(Font.font("Serif", FontWeight.BOLD, 36));
        underscore.setTextFill(Color.LIGHTGRAY);

        timerLabel = new Label();
        timerLabel.setFont(Font.font("Serif", FontWeight.BOLD, 28));
        timerLabel.setTextFill(Color.GREY);
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
            	underscore.setTextFill(Color.LIGHTGRAY);
                if (!underscore.getText().contains("_")) {
                    stopGame();
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

        startTimer(stage, layout);
    }

    private void startTimer(Stage stage, StackPane layout) {
        FadeTransition flash = new FadeTransition(Duration.millis(200), timerLabel);
        flash.setFromValue(1.0);
        flash.setToValue(0.3);
        flash.setCycleCount(Animation.INDEFINITE);
        flash.setAutoReverse(true);
        flash.play();

        AtomicInteger fakeTimeShown = new AtomicInteger(80);  
        timeRemaining = 45; 
        gameEnded = false;

        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (gameEnded) return;
            fakeTimeShown.decrementAndGet();  
            timeRemaining--;  

            Platform.runLater(() -> {
             
                int displayTime = fakeTimeShown.get();
                timerLabel.setText(displayTime + "s");

          
                if (fakeTimeShown.get() == 35) {
                
                    for (int i = 10; i >= 0; i--) {
                        final int countdown = i;
                        Timeline realCountdown = new Timeline(
                            new KeyFrame(Duration.seconds(1), event -> {
                                timerLabel.setText(countdown + "s");
                            })
                        );
                        realCountdown.setCycleCount(1);
                        realCountdown.play();
                    }


                    Label message = new Label("You’ve been lied to...");
                    message.setFont(Font.font("Serif", FontWeight.BOLD, 28));
                    message.setTextFill(Color.DARKRED);
                    message.setStyle("-fx-background-color: black;");
                    StackPane.setAlignment(message, Pos.CENTER);
                    layout.getChildren().add(message);

                    FadeTransition msgFade = new FadeTransition(Duration.seconds(3), message);
                    msgFade.setFromValue(1.0);
                    msgFade.setToValue(0.0);
                    msgFade.setOnFinished(ev -> layout.getChildren().remove(message));
                    msgFade.play();
                }

                if (timeRemaining <= 3) {
                    timerLabel.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    ScaleTransition stretch = new ScaleTransition(Duration.millis(100), timerLabel);
                    stretch.setByX(0.2);
                    stretch.setByY(0.2);
                    stretch.setAutoReverse(true);
                    stretch.setCycleCount(2);
                    stretch.play();
                }

                if (timeRemaining <= 0) {
                    flash.stop();
                    timerLabel.setText("Time's Up!");
                    timerLabel.setTextFill(Color.DARKRED);
                    gameEnded = true;
                    stopGame();
                    showEndScreen(stage, false);
                }
            });
        }));

        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();

        wordSpawner = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            showRandomMathWord(layout);
        }));
        wordSpawner.setCycleCount(Timeline.INDEFINITE);
        wordSpawner.play();
    }

    private void showRandomMathWord(StackPane layout) {
        String word = MATH_WORDS[new Random().nextInt(MATH_WORDS.length)];
        Label wordLabel = new Label(word);
        int fontSize = new Random().nextInt(22) + 18;
        wordLabel.setFont(Font.font("Serif", fontSize));
        int greyShade = new Random().nextInt(156) + 100;
        wordLabel.setTextFill(Color.rgb(greyShade, greyShade, greyShade));
        double startX = new Random().nextDouble() * 1000 - 500;
        double startY = new Random().nextDouble() * 600 - 300;
        wordLabel.setTranslateX(startX);
        wordLabel.setTranslateY(startY);
        layout.getChildren().add(wordLabel);

        double endX = new Random().nextDouble() * 1000 - 500;
        double endY = new Random().nextDouble() * 600 - 300;

        TranslateTransition move = new TranslateTransition(Duration.seconds(3), wordLabel);
        move.setToX(endX);
        move.setToY(endY);
        FadeTransition fade = new FadeTransition(Duration.seconds(3), wordLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        RotateTransition rotate = new RotateTransition(Duration.seconds(3), wordLabel);
        rotate.setByAngle(new Random().nextBoolean() ? 360 : -360);

        ParallelTransition anim = new ParallelTransition(move, fade, rotate);
        anim.setOnFinished(e -> layout.getChildren().remove(wordLabel));
        anim.play();
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

    private void stopGame() {
        if (gameTimer != null) gameTimer.stop();
        if (wordSpawner != null) wordSpawner.stop();
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    private void showEndScreen(Stage stage, boolean won) {
        VBox endLayout = new VBox(20);
        endLayout.setAlignment(Pos.CENTER);
        endLayout.setStyle("-fx-background-color: black;");
        endLayout.setPadding(new Insets(50));

        Label endLabel = new Label(won
                ? "You survived...\nbut did they?"
                : "You believed the lies...\n \tyou little fool");
        endLabel.setFont(Font.font("Serif", FontWeight.BOLD, 40));
        endLabel.setTextFill(Color.DARKRED);

        Button nextLevelButton = new Button("Ready to regret?");
        nextLevelButton.setFont(Font.font("Serif", FontWeight.BOLD, 20));
        nextLevelButton.setOnAction(e -> {
            Ending nextLevel = new Ending();
            Scene nextScene = nextLevel.createScene(stage);
            stage.setScene(nextScene);
            stage.setFullScreen(true);
        });

        endLayout.getChildren().addAll(endLabel, nextLevelButton);
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();

        Scene endScene = new Scene(endLayout, width, height);
        stage.setScene(endScene);
      
    }

}
