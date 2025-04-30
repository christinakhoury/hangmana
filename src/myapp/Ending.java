package myapp;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Ending {

    public Scene createScene(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        String videoPath = "file:///C:/Users/Lenovo/Desktop/p3.mp4";
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(false);
        mediaView.setOpacity(0.4);
        root.getChildren().add(mediaView);
        mediaPlayer.setAutoPlay(true);

        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        mediaView.setFitWidth(width);
        mediaView.setFitHeight(height);

        String fullMessage = "You held them close to your heart…\n" +
                "But not the wisdom to shield them from the fall.\n\n" +
                "It wasn’t love you lacked—\n" +
                "It was the knowledge that slipped through the cracks.\n\n" +
                "Now, their silence echoes in the spaces where learning should’ve lived.\n\n" +
                "Let your love be led by truth,\n" +
                "So no soul you hold ever fades from what you failed to know.\n";

        Label credits = new Label("");
        credits.setTextFill(Color.LIGHTGRAY);
        credits.setFont(Font.font("Serif", FontWeight.BOLD, 50));
        credits.setWrapText(true);
        credits.setMaxWidth(width * 0.8);
        credits.setTranslateY(height / 2);  
        root.getChildren().add(credits);

       
        Timeline typing = new Timeline();
        StringBuilder currentText = new StringBuilder();

        for (int i = 0; i < fullMessage.length(); i++) {
            final int index = i;
            KeyFrame frame = new KeyFrame(Duration.millis(70 * i), e -> {
                currentText.append(fullMessage.charAt(index));
                credits.setText(currentText.toString());
            });
            typing.getKeyFrames().add(frame);
        }

        typing.play();
        TranslateTransition scroll = new TranslateTransition(Duration.seconds(25), credits);  
        scroll.setFromY(height / 2);
        scroll.setToY(-height / 2 - 100);
        scroll.play();
        Timeline fadeOut = new Timeline(
            new KeyFrame(Duration.seconds(25), new KeyValue(credits.opacityProperty(), 0))
        );
        fadeOut.play();

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

        return scene;
    }
}