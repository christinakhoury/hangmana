module JavaFxAPP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires javafx.graphics; 
    exports myapp; // this is important if you're using the 'myapp' package
}
