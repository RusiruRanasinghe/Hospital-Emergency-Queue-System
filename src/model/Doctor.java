package model;

public class Doctor {
    private final int doctorId;
    private final String name;
    private boolean available;

    public Doctor(int doctorId, String name) {
        this.doctorId = doctorId;
        this.name = name;
        this.available = true;
    }

    public int getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Doctor{id=" + doctorId + ", name='" + name + "', available=" + available + "}";
    }
}
