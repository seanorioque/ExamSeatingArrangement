package Model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class ExamSeatingServiceModel {

    public List<StudentModel> loadStudentsFromCSV(File file) throws IOException {
        List<StudentModel> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    students.add(new StudentModel(
                            data[0].trim(), // CourseCode
                            data[1].trim(), // CourseName
                            data[2].trim(), // ExamDateTime
                            data[3].trim(), // StudentId
                            data[4].trim(), // StudentName
                            data[5].trim()  // Section
                    ));
                }
            }
        }
        return students;
    }

    public List<StudentModel> filterStudents(List<StudentModel> students, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return new ArrayList<>(students);
        }

        String lowerCaseFilter = searchText.toLowerCase();
        List<StudentModel> filteredStudents = new ArrayList<>();

        for (StudentModel student : students) {
            if (student.getStudentId().toLowerCase().contains(lowerCaseFilter) ||
                    student.getStudentName().toLowerCase().contains(lowerCaseFilter) ||
                    student.getCourseCode().toLowerCase().contains(lowerCaseFilter) ||
                    student.getCourseName().toLowerCase().contains(lowerCaseFilter) ||
                    student.getSection().toLowerCase().contains(lowerCaseFilter)) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    public List<SeatMapModel> assignRooms(List<StudentModel> students, String sortCriteria, int maxStudentsPerRoom) {
        List<SeatMapModel> roomAssignments = new ArrayList<>();
        Map<String, List<StudentModel>> groupedStudents = new HashMap<>();

        // Group students by criteria
        for (StudentModel student : students) {
            String key;
            switch (sortCriteria) {
                case "Course Code":
                    key = student.getCourseCode() + "_" + student.getExamDateTime() + "_" + student.getSection();
                    break;
                case "Exam Date/Time":
                    key = student.getExamDateTime() + "_" + student.getCourseCode() + "_" + student.getSection();
                    break;
                case "Section":
                    key = student.getSection() + "_" + student.getCourseCode() + "_" + student.getExamDateTime();
                    break;
                default:
                    key = student.getCourseCode() + "_" + student.getExamDateTime() + "_" + student.getSection();
            }

            groupedStudents.computeIfAbsent(key, k -> new ArrayList<>()).add(student);
        }

        int roomCounter = 400;

        // Create room assignments
        for (Map.Entry<String, List<StudentModel>> entry : groupedStudents.entrySet()) {
            List<StudentModel> studentGroup = entry.getValue();
            String[] keyParts = entry.getKey().split("_");

            // Determine correct order of key parts based on sort criteria
            String courseCode = keyParts[0];
            String examDateTime = keyParts[1];
            String section = keyParts[2];

            if (sortCriteria.equals("Exam Date/Time")) {
                courseCode = keyParts[1];
                examDateTime = keyParts[0];
            } else if (sortCriteria.equals("Section")) {
                section = keyParts[0];
                courseCode = keyParts[1];
                examDateTime = keyParts[2];
            }

            // Calculate and create needed rooms
            int numRooms = (int) Math.ceil((double) studentGroup.size() / maxStudentsPerRoom);
            for (int i = 0; i < numRooms; i++) {
                String roomNumber = "Room " + roomCounter++;
                int startIdx = i * maxStudentsPerRoom;
                int endIdx = Math.min(startIdx + maxStudentsPerRoom, studentGroup.size());

                roomAssignments.add(new SeatMapModel(
                        roomNumber, courseCode, examDateTime, section,
                        studentGroup.subList(startIdx, endIdx)));
            }
        }

        return roomAssignments;
    }

    public static boolean exportRoomAssignments(Stage owner, List<SeatMapModel> roomAssignments) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Room Assignments");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("room_assignments.csv");

        File file = fileChooser.showSaveDialog(owner);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Room Number,Course Code,Exam Date/Time,Section,Student Count");

                for (SeatMapModel room : roomAssignments) {
                    writer.println(String.format("%s,%s,%s,%s,%d",
                            room.getRoomNumber(), room.getCourseCode(), room.getExamDateTime(),
                            room.getSection(), room.getStudentCount()));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}