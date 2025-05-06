package com.example.examseatingarangementfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExamSeatingArrangementApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/examseatingarangementfinal/MainView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Exam Seating Arrangement System");  // Set the title of the application window
        primaryStage.setScene(new Scene(root)); //Create a new Scene with the loaded root node and set it to the primary stage
        primaryStage.show(); //Display the application window
    }
    // shows window

    public static void main(String[] args) {
        launch(args);
    }
}
