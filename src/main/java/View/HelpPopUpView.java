package View;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class HelpPopUpView {

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void showHelpDialog() {
        String helpContent =
                "1. Load CSV File: (CSV format: Course Code| Course Name| Exam Date/Time| Student ID | Student Name| Section)\n\n" +
                        "2. Max Students per Room: Set the maximum number of students allowed in each room.\n\n" +
                        "3. Sort by: Choose how to group students (by Course Code, Exam Date/Time, or Section).\n\n" +
                        "4. Assign Rooms: Generate room assignments based on your settings.\n\n" +
                        "5. Student Data Tab: View, add, edit, or delete student records.\n\n" +
                        "6. Room Assignments Tab: View assigned rooms and seating maps.\n\n" +
                        "7. Export: Save student data or room assignments to CSV files.\n\n" +
                        "8. Print: Print room assignments for physical distribution.";

        showAlert(Alert.AlertType.INFORMATION, "HELP!!!! TULONGG", "Exam Seating Arrangement System", helpContent);
    }

    public static void exit() {
        if (showConfirmation("Confirm Exit", "Exit Application",
                "Are you sure you want to exit? Any unsaved data will be lost.")) {
            Platform.exit();
        }
    }
}