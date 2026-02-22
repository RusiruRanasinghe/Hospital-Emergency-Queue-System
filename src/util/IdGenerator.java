package util;

public class IdGenerator {
    private static int nextPatientId = 1000;
    private static long nextArrivalOrder = 1;

    public static int nextPatientId() {
        return nextPatientId++;
    }

    public static long nextArrivalOrder() {
        return nextArrivalOrder++;
    }
}
