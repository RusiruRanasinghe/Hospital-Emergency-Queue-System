package service;

import ds.DoctorMinHeap;
import ds.PatientMaxHeap;
import ds.TreatedHistoryList;
import engine.PriorityEngine;
import model.Doctor;
import model.Patient;
import util.IdGenerator;

import java.time.LocalDateTime;
import java.util.*;

public class EmergencyQueueSystem {
    private final TreatedHistoryList treatedHistory = new TreatedHistoryList();
    private final PatientMaxHeap heap = new PatientMaxHeap();
    private final PriorityEngine engine = new PriorityEngine();
    private final ArrayList<Doctor> doctors = new ArrayList<>();
    private final HashMap<Integer, Integer> doctorAssignments = new HashMap<>(); // patientId -> doctorId
    private final DoctorMinHeap doctorHeap = new DoctorMinHeap();

    public List<Doctor> getAllDoctors() {
        return doctorHeap.getAllDoctors();
    }

    public List<Patient> getTreatmentHistory() {
        return treatedHistory.getAllHistory();
    }

    public List<Patient> getLastNTreated(int n) {
        return treatedHistory.getLastN(n);
    }

    // Avg treatment times (minutes) by triage level
    private final Map<Integer, Integer> avgTime = Map.of(
            1, 25,
            2, 20,
            3, 15,
            4, 10,
            5, 7
    );

    public void addDoctor(int id, String name) {
        Doctor doctor = new Doctor(id, name);
        doctorHeap.insert(doctor);
    }
}
