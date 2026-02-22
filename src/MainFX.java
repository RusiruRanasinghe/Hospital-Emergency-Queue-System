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
    }
}
