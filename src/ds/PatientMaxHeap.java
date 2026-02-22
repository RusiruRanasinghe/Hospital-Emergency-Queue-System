package ds;

import model.Patient;

import java.util.*;

public class PatientMaxHeap {
    private final ArrayList<Patient> heap = new ArrayList<>();
    private final HashMap<Integer, Integer> idToIndex = new HashMap<>();

    public int size() { return heap.size(); }
    public boolean isEmpty() { return heap.isEmpty(); }

    public Patient peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public Patient searchById(int id) {
        Integer idx = idToIndex.get(id);
        return (idx == null) ? null : heap.get(idx);
    }

    public List<Patient> searchByName(String namePart) {
        String q = namePart.toLowerCase();
        List<Patient> res = new ArrayList<>();
        for (Patient p : heap) {
            if (p.getName().toLowerCase().contains(q)) res.add(p);
        }
        res.sort(this::compareForDisplay);
        return res;
    }

    public void insert(Patient p) {
        heap.add(p);
        int idx = heap.size() - 1;
        idToIndex.put(p.getPatientId(), idx);
        heapifyUp(idx);
    }

    public Patient extractMax() {
        if (heap.isEmpty()) return null;

        Patient max = heap.get(0);
        Patient last = heap.remove(heap.size() - 1);
        idToIndex.remove(max.getPatientId());

        if (!heap.isEmpty()) {
            heap.set(0, last);
            idToIndex.put(last.getPatientId(), 0);
            heapifyDown(0);
        }
        return max;
    }

    public boolean removeById(int id) {
        Integer idxObj = idToIndex.get(id);
        if (idxObj == null) return false;

        int idx = idxObj;
        int lastIdx = heap.size() - 1;

        if (idx == lastIdx) {
            Patient removed = heap.remove(lastIdx);
            idToIndex.remove(removed.getPatientId());
            return true;
        }

        swap(idx, lastIdx);
        Patient removed = heap.remove(lastIdx);
        idToIndex.remove(removed.getPatientId());

        // Re-balance from idx
        heapifyUp(idx);
        heapifyDown(idx);
        return true;
    }

    public boolean updatePatientKey(int id) {
        Integer idxObj = idToIndex.get(id);
        if (idxObj == null) return false;

        int idx = idxObj;
        heapifyUp(idx);
        heapifyDown(idx);
        return true;
    }

    public List<Patient> viewQueueSorted() {
        ArrayList<Patient> copy = new ArrayList<>(heap);
        copy.sort(this::compareForDisplay);
        return copy;
    }

    // Heap ordering: higher score first, then lower triage, then earlier arrivalOrder
    private int compare(Patient a, Patient b) {
        int scoreCmp = Double.compare(a.getPriorityScore(), b.getPriorityScore());
        if (scoreCmp != 0) return scoreCmp; // ascending (for max-heap we’ll use > checks)
        int triageCmp = Integer.compare(b.getTriageLevel(), a.getTriageLevel()); // lower triage is better -> treat as "higher"
        if (triageCmp != 0) return triageCmp;
        return Long.compare(b.getArrivalOrder(), a.getArrivalOrder()); // earlier arrivalOrder is better -> treat as "higher"
    }

    // For display sorting: reverse of heap compare (highest first)
    private int compareForDisplay(Patient a, Patient b) {
        // Highest first
        if (Double.compare(b.getPriorityScore(), a.getPriorityScore()) != 0)
            return Double.compare(b.getPriorityScore(), a.getPriorityScore());
        if (Integer.compare(a.getTriageLevel(), b.getTriageLevel()) != 0)
            return Integer.compare(a.getTriageLevel(), b.getTriageLevel());
        return Long.compare(a.getArrivalOrder(), b.getArrivalOrder());
    }

    private void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            // if heap[i] "greater" than heap[parent], swap
            if (compare(heap.get(i), heap.get(parent)) > 0) {
                swap(i, parent);
                i = parent;
            } else break;
        }
    }

    private void heapifyDown(int i) {
        int n = heap.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < n && compare(heap.get(left), heap.get(largest)) > 0) largest = left;
            if (right < n && compare(heap.get(right), heap.get(largest)) > 0) largest = right;

            if (largest != i) {
                swap(i, largest);
                i = largest;
            } else break;
        }
    }

    private void swap(int i, int j) {
        Patient a = heap.get(i);
        Patient b = heap.get(j);
        heap.set(i, b);
        heap.set(j, a);
        idToIndex.put(a.getPatientId(), j);
        idToIndex.put(b.getPatientId(), i);
    }
}
