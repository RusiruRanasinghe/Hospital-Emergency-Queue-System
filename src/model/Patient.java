package model;

import java.time.LocalDateTime;

public class Patient {
    private final int patientId;
    private String name;
    private int age;
    private String emergencyType;
    private int triageLevel; // 1-5 (1 = critical)
    private final LocalDateTime arrivalTime;
    private final long arrivalOrder;

    // Vitals
    private int bpSys;
    private int bpDia;
    private int heartRate;
    private double temperature;
    private int spo2;

    private double priorityScore;
    private Status status;
    private Integer assignedDoctorId;

    public enum Status { WAITING, IN_TREATMENT, DISCHARGED, TRANSFERRED }

    public Patient(int patientId, String name, int age, String emergencyType, int triageLevel,
                   LocalDateTime arrivalTime, long arrivalOrder,
                   int bpSys, int bpDia, int heartRate, double temperature, int spo2) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.emergencyType = emergencyType;
        this.triageLevel = triageLevel;
        this.arrivalTime = arrivalTime;
        this.arrivalOrder = arrivalOrder;

        this.bpSys = bpSys;
        this.bpDia = bpDia;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.spo2 = spo2;

        this.status = Status.WAITING;
        this.assignedDoctorId = null;
        this.priorityScore = 0;
    }

    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmergencyType() { return emergencyType; }
    public int getTriageLevel() { return triageLevel; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public long getArrivalOrder() { return arrivalOrder; }

    public int getBpSys() { return bpSys; }
    public int getBpDia() { return bpDia; }
    public int getHeartRate() { return heartRate; }
    public double getTemperature() { return temperature; }
    public int getSpo2() { return spo2; }

    public double getPriorityScore() { return priorityScore; }
    public Status getStatus() { return status; }
    public Integer getAssignedDoctorId() { return assignedDoctorId; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
    public void setTriageLevel(int triageLevel) { this.triageLevel = triageLevel; }

    public void setBpSys(int bpSys) { this.bpSys = bpSys; }
    public void setBpDia(int bpDia) { this.bpDia = bpDia; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public void setSpo2(int spo2) { this.spo2 = spo2; }

    public void setPriorityScore(double priorityScore) { this.priorityScore = priorityScore; }
    public void setStatus(Status status) { this.status = status; }
    public void setAssignedDoctorId(Integer assignedDoctorId) { this.assignedDoctorId = assignedDoctorId; }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + patientId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", emergency='" + emergencyType + '\'' +
                ", triage=" + triageLevel +
                ", bp=" + bpSys + "/" + bpDia +
                ", HR=" + heartRate +
                ", temp=" + temperature +
                ", SpO2=" + spo2 +
                ", score=" + String.format("%.2f", priorityScore) +
                ", status=" + status +
                ", doctor=" + (assignedDoctorId == null ? "-" : assignedDoctorId) +
                ", arrivalOrder=" + arrivalOrder +
                '}';
    }
}
