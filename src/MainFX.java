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

        // ---- Update / Re-triage ----
        TextField updateId = new TextField();
        updateId.setPromptText("Patient ID");

        ComboBox<Integer> updateTriage = new ComboBox<>(FXCollections.observableArrayList(1,2,3,4,5));
        updateTriage.setPromptText("Triage");

        TextField updateSpo2 = new TextField();
        updateSpo2.setPromptText("SpO2");

        TextField updateHr = new TextField();
        updateHr.setPromptText("HR");

        Button updateBtn = new Button("Update Priority (Re-triage)");
        updateBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(updateId.getText().trim());

                Integer triage = (updateTriage.getValue() == null) ? null : updateTriage.getValue();
                Integer spo2 = updateSpo2.getText().isBlank() ? null : Integer.parseInt(updateSpo2.getText().trim());
                Integer hr = updateHr.getText().isBlank() ? null : Integer.parseInt(updateHr.getText().trim());

                // keep others null (no change)
                boolean ok = system.updatePatient(id, triage, null, null, hr, null, spo2);
                if (!ok) showError("Not found", "Patient ID not found in queue.");
                refreshTable();

            } catch (Exception ex) {
                showError("Invalid input", "Enter a valid patient ID / values.");
            }
        });

        // ---- Estimated wait time ----
        TextField waitId = new TextField();
        waitId.setPromptText("Patient ID");
        Button waitBtn = new Button("Calculate Wait");
        waitBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(waitId.getText().trim());
                double mins = system.estimatedWaitMinutes(id);
                waitLabel.setText("Estimated wait: " + String.format("%.1f", mins) + " minutes");
            } catch (Exception ex) {
                showError("Invalid input", "Enter a valid patient ID.");
            }
        });

         // ---- Layout ----
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Name"), 0,0); form.add(nameField, 1,0);
        form.add(new Label("Age"), 2,0); form.add(ageField, 3,0);
        form.add(new Label("Emergency"), 0,1); form.add(emergencyField, 1,1);
        form.add(new Label("Triage"), 2,1); form.add(triageBox, 3,1);

        form.add(new Label("BP Sys"), 0,2); form.add(bpSysField, 1,2);
        form.add(new Label("BP Dia"), 2,2); form.add(bpDiaField, 3,2);
        form.add(new Label("HR"), 0,3); form.add(hrField, 1,3);
        form.add(new Label("Temp"), 2,3); form.add(tempField, 3,3);
        form.add(new Label("SpO2"), 0,4); form.add(spo2Field, 1,4);

        HBox formButtons = new HBox(10, registerBtn);
        VBox topBox = new VBox(8, new Label("Patient Registration"), form, formButtons, alertLabel);
        topBox.setPadding(new Insets(10));

        Button historyBtn = new Button("Show Treatment History");

        historyBtn.setOnAction(e -> showTreatmentHistory());

        Button doctorLoadBtn = new Button("Show Doctor Loads");
        doctorLoadBtn.setOnAction(e -> showDoctorLoads());

        HBox queueButtons = new HBox(10, refreshBtn, treatBtn, doctorLoadBtn);
        
        
        VBox queueBox = new VBox(10, new Label("Revised Emergency Queue (Sorted)"), table, queueButtons, nextPatientLabel);
        queueBox.setPadding(new Insets(10));

        HBox updateBox = new HBox(10, updateId, updateTriage, updateSpo2, updateHr, updateBtn);
        updateBox.setPadding(new Insets(10));

        HBox waitBox = new HBox(10, waitId, waitBtn, waitLabel);
        waitBox.setPadding(new Insets(10));

        VBox root = new VBox(10, topBox, queueBox, new Separator(), new Label("Re-triage / Update"), updateBox,
                new Separator(), new Label("Estimated Waiting Time"), waitBox);

        Scene scene = new Scene(root, 1050, 750);
        stage.setTitle("Hospital Emergency Queue Management System (JavaFX)");
        stage.setScene(scene);
        stage.show();

        refreshTable();
    }

    private void showDoctorLoads() {

        List<Doctor> doctors = system.getAllDoctors();

        if (doctors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Doctor Loads");
            alert.setHeaderText(null);
            alert.setContentText("No doctors available.");
            alert.showAndWait();
            return;
        }

        TableView<Doctor> doctorTable = new TableView<>();
        ObservableList<Doctor> doctorData = FXCollections.observableArrayList(doctors);

        TableColumn<Doctor, String> idCol = new TableColumn<>("Doctor ID");
        idCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getDoctorId())));

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Doctor, String> loadCol = new TableColumn<>("Current Load");
        loadCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getCurrentLoad())));

        doctorTable.getColumns().addAll(idCol, nameCol, loadCol);
        doctorTable.setItems(doctorData);
        doctorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox root = new VBox(10,
                new Label("Doctor Load Balancing (Min Heap)"),
                doctorTable);
        root.setPadding(new Insets(10));

        Stage doctorStage = new Stage();
        doctorStage.setTitle("Doctor Load Dashboard");
        doctorStage.setScene(new Scene(root, 500, 350));
        doctorStage.show();
    }

    private void showTreatmentHistory() {

        List<Patient> history = system.getTreatmentHistory();

        if (history.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Treatment History");
            alert.setHeaderText(null);
            alert.setContentText("No treated patients yet.");
            alert.showAndWait();
            return;
        }

        TableView<Patient> historyTable = new TableView<>();
        ObservableList<Patient> historyData = FXCollections.observableArrayList(history);

        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getPatientId())));

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Patient, String> triageCol = new TableColumn<>("Triage");
        triageCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getTriageLevel())));

        TableColumn<Patient, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getStatus())));

        TableColumn<Patient, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getAssignedDoctorId() == null
                                ? "-"
                                : String.valueOf(c.getValue().getAssignedDoctorId())
                ));

        historyTable.getColumns().addAll(idCol, nameCol, triageCol, statusCol, doctorCol);
        historyTable.setItems(historyData);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox root = new VBox(10, new Label("Treatment History"), historyTable);
        root.setPadding(new Insets(10));

        Stage historyStage = new Stage();
        historyStage.setTitle("Treatment History");
        historyStage.setScene(new Scene(root, 700, 400));
        historyStage.show();
    }

    private void setupTable() {
        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPatientId())));

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Patient, String> triageCol = new TableColumn<>("Triage");
        triageCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getTriageLevel())));

        TableColumn<Patient, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getPriorityScore())));

        TableColumn<Patient, String> spo2Col = new TableColumn<>("SpO2");
        spo2Col.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSpo2())));

        TableColumn<Patient, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getStatus())));

        TableColumn<Patient, String> docCol = new TableColumn<>("Doctor");
        docCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAssignedDoctorId() == null ? "-" : String.valueOf(c.getValue().getAssignedDoctorId()))
        );

        table.getColumns().setAll(idCol, nameCol, triageCol, scoreCol, spo2Col, statusCol, docCol);
        table.setItems(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshTable() {
        List<Patient> sorted = system.viewQueueSorted(); // uses heap logic
        tableData.setAll(sorted);
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
}
