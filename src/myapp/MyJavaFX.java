package myapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.geometry.Pos;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.ArrayList;
import java.util.List;

public class MyJavaFX extends Application {

    protected List<String> lovedNames = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        VBox inputLayout = new VBox(15);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.setStyle("-fx-padding: 20;");

        Label instruction = new Label("\t\tIn a world full of souls,\nname three ones who make you whole:");
        instruction.setTextFill(Color.WHITE);
        instruction.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 18));
        TextField name1 = new TextField();
        TextField name2 = new TextField();
        TextField name3 = new TextField();
        HBox nameBox1 = new HBox(10, new Label("Name 1:"), name1);
        HBox nameBox2 = new HBox(10, new Label("Name 2:"), name2);
        HBox nameBox3 = new HBox(10, new Label("Name 3:"), name3);
        nameBox1.setAlignment(Pos.CENTER);
        nameBox2.setAlignment(Pos.CENTER);
        nameBox3.setAlignment(Pos.CENTER);
        Button submitBtn = new Button("Submit");
        Button playBtn = new Button("Play Game");
        playBtn.setVisible(false);
        playBtn.setFont(Font.font("System", FontWeight.BOLD, 18));
        playBtn.setOnAction(e -> {
            LevelOne levelOne = new LevelOne();
            Scene levelOneScene = levelOne.createScene(primaryStage);
            primaryStage.setScene(levelOneScene);
        });
        submitBtn.setOnAction(e -> {
            String n1 = name1.getText().trim();
            String n2 = name2.getText().trim();
            String n3 = name3.getText().trim();

            if (n1.isEmpty() || n2.isEmpty() || n3.isEmpty() ||
                    !n1.matches("[a-zA-Z]+") || !n2.matches("[a-zA-Z]+") || !n3.matches("[a-zA-Z]+")) {
                new Alert(Alert.AlertType.ERROR, "Please enter alphabetic characters only.").showAndWait();
            } else {
                lovedNames.clear();
                lovedNames.add(n1);
                lovedNames.add(n2);
                lovedNames.add(n3);
                Scene gameScene = createGameScene(primaryStage, playBtn);
                primaryStage.setScene(gameScene);
                primaryStage.setMaximized(true);
            }
        });

        inputLayout.getChildren().addAll(instruction, nameBox1, nameBox2, nameBox3, submitBtn);

        StackPane root = new StackPane(inputLayout);
        root.setStyle("-fx-background-color: black;");
        Scene inputScene = new Scene(root, 1000, 600);

        primaryStage.setTitle("...");
        primaryStage.setScene(inputScene);
        primaryStage.show();}

    private Scene createGameScene(Stage stage, Button playBtn) {
        VBox gameLayout = new VBox(20);
        gameLayout.setStyle("-fx-padding: 20; -fx-background-color: black;");
        Label message = new Label("Their souls are hang by a thread...\nPlay the game or watch them shred");
        message.setTextFill(Color.DARKRED);
        message.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 40));
        message.setAlignment(Pos.CENTER);
        message.setVisible(false);
        VBox messageBox = new VBox(20, message, playBtn);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setVisible(false);
        StackPane mediaStack = new StackPane();
        mediaStack.setAlignment(Pos.CENTER);
        gameLayout.getChildren().add(mediaStack);

        String videoPath = "file:///C:/Users/Lenovo/Downloads/vid.mp4";
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(stage.widthProperty());
        mediaView.fitHeightProperty().bind(stage.heightProperty());
        mediaStack.getChildren().addAll(mediaView, messageBox);
        mediaPlayer.setAutoPlay(true);

        mediaPlayer.setOnEndOfMedia(() -> {
            messageBox.setVisible(true);
            message.setVisible(true);
            playBtn.setVisible(true);
        });
        StackPane root = new StackPane(gameLayout);
        root.setStyle("-fx-background-color: black;");
        return new Scene(root);}
    
    public static void main(String[] args) {
        launch(args);
    }}