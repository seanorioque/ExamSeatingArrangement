package Controller;

import Model.SeatMapModel;
import Model.StudentModel;
import View.HelpPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SeatMapController {

    @FXML private Label roomInfoLabel;
    @FXML private GridPane seatGrid;
    private SeatMapModel roomAssignment;

    public void setRoomAssignment(SeatMapModel roomAssignment) {
        this.roomAssignment = roomAssignment;

        // Set room info
        roomInfoLabel.setText(String.format("Room: %s\nCourse: %s\nExam Date/Time: %s\nSection: %s\nTotal Students: %d",
                roomAssignment.getRoomNumber(), roomAssignment.getCourseCode(),
                roomAssignment.getExamDateTime(), roomAssignment.getSection(),
                roomAssignment.getStudents().size()));

        seatGrid.getChildren().clear();
        displaySeats(10); // Fixed 10 columns grid
    }

    private void displaySeats(int columns) {
        int totalStudents = roomAssignment.getStudents().size();

        for (int i = 0; i < totalStudents; i++) {
            StudentModel student = roomAssignment.getStudents().get(i);

            VBox seatBox = new VBox(10);
            seatBox.getChildren().addAll(
                    new Label("Seat " + (i + 1)),
                    new Label("ID: " + student.getStudentId()),
                    new Label(student.getStudentName())
            );
            seatBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-border-radius: 5;");

            seatGrid.add(seatBox, i % columns, i / columns);
        }
    }

    @FXML
    private void printSeatMap() {
        if (roomAssignment == null) {
            HelpPopUpView.showAlert(Alert.AlertType.WARNING, "No Data", "No Seat Map", "No seat map data to print.");
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(seatGrid.getScene().getWindow())) {
            VBox printContent = createPrintContent();

            if (job.printPage(printContent)) {
                job.endJob();
                HelpPopUpView.showAlert(Alert.AlertType.INFORMATION, "Print Successful",
                        "Seat Map Printed", "Seat map has been sent to the printer.");
            } else {
                HelpPopUpView.showAlert(Alert.AlertType.ERROR, "Print Failed",
                        "Printing Error", "Failed to print seat map.");
            }
        } else if (job == null) {
            HelpPopUpView.showAlert(Alert.AlertType.ERROR, "No Printer",
                    "No Printer Available", "No printer was found.");
        }
    }

    private VBox createPrintContent() {
        VBox printContent = new VBox(10);

        // Add header information
        printContent.getChildren().addAll(
                new Label("Room: " + roomAssignment.getRoomNumber()),
                new Label("Course: " + roomAssignment.getCourseCode()),
                new Label("Exam Date/Time: " + roomAssignment.getExamDateTime()),
                new Label("Section: " + roomAssignment.getSection()),
                new Label("Total Students: " + roomAssignment.getStudents().size()),
                new Label("Seat Map:")
        );

        // Create grid for printing
        GridPane printGrid = new GridPane();
        printGrid.setHgap(20);
        printGrid.setVgap(20);
        printGrid.setPadding(new javafx.geometry.Insets(20));

        for (Node node : seatGrid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
            if (row != null && col != null) {
                printGrid.add(node, col, row);
            }
        }

        printContent.getChildren().add(printGrid);
        return printContent;
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }
}