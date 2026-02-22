import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Patient;
import service.EmergencyQueueSystem;

import java.util.List;


public class MainFX extends Application {
    private final EmergencyQueueSystem system = new EmergencyQueueSystem();

    private final TableView<Patient> table = new TableView<>();
    private final ObservableList<Patient> tableData = FXCollections.observableArrayList();

    private final Label nextPatientLabel = new Label("Next patient: -");
    private final Label alertLabel = new Label("");
    private final Label waitLabel = new Label("Estimated wait: -");

    @Override
    public void start(Stage stage){

        // Add doctors
        system.addDoctor(1, "Dr. Silva");
        system.addDoctor(2, "Dr. Perera");

        //input
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField emergencyField = new TextField();
        ComboBox<Integer> triageBox = new ComboBox<>(FXCollections.observableArrayList(1,2,3,4,5));
        triageBox.setValue(3);

        TextField bpSysField = new TextField();
        TextField bpDiaField = new TextField();
        TextField hrField = new TextField();
        TextField tempField = new TextField();
        TextField spo2Field = new TextField();

        nameField.setPromptText("Name");
        ageField.setPromptText("Age");
        emergencyField.setPromptText("Emergency Type");
        bpSysField.setPromptText("BP Sys");
        bpDiaField.setPromptText("BP Dia");
        hrField.setPromptText("Heart Rate");
        tempField.setPromptText("Temp");
        spo2Field.setPromptText("SpO2");

        Button registerBtn = new Button("Register Patient");
        registerBtn.setOnAction(e -> {
            alertLabel.setText("");
            try {
                Patient p = system.registerPatient(
                        nameField.getText().trim(),
                        Integer.parseInt(ageField.getText().trim()),
                        emergencyField.getText().trim(),
                        triageBox.getValue(),
                        Integer.parseInt(bpSysField.getText().trim()),
                        Integer.parseInt(bpDiaField.getText().trim()),
                        Integer.parseInt(hrField.getText().trim()),
                        Double.parseDouble(tempField.getText().trim()),
                        Integer.parseInt(spo2Field.getText().trim())
                );

                if (p.getPriorityScore() >= 1000) {
                    alertLabel.setText("⚠ CRITICAL ALERT: Emergency override applied!");
                }

                refreshTable();
                clearFields(nameField, ageField, emergencyField, bpSysField, bpDiaField, hrField, tempField, spo2Field);

            } catch (Exception ex) {
                showError("Invalid input", "Please enter valid numbers for age/vitals.");
            }
        });

         // ---- Queue table ----
        setupTable();

        Button refreshBtn = new Button("Refresh Queue");
        refreshBtn.setOnAction(e -> refreshTable());

        Button treatBtn = new Button("Treat Next");
        treatBtn.setOnAction(e -> {
            Patient p = system.treatNextPatient();
            if (p == null) {
                nextPatientLabel.setText("Next patient: - (queue empty)");
            } else {
                nextPatientLabel.setText("Next patient: " + p.getPatientId() + " - " + p.getName()
                        + " | Doctor: " + (p.getAssignedDoctorId() == null ? "None" : p.getAssignedDoctorId()));
            }
            refreshTable();
        });
    }
}
