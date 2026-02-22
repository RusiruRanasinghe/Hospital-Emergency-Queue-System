package ds;

import model.Doctor;
import java.util.ArrayList;

public class DoctorMinHeap {

    private final ArrayList<Doctor> heap = new ArrayList<>();

    public void insert(Doctor doctor) {
        heap.add(doctor);
        heapifyUp(heap.size() - 1);
    }

    public Doctor extractMin() {
        if (heap.isEmpty()) return null;

        Doctor min = heap.get(0);
        Doctor last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return min;
    }

    public void updateHeap() {
        for (int i = heap.size()/2; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).getCurrentLoad() <
                    heap.get(parent).getCurrentLoad()) {
                swap(index, parent);
                index = parent;
            } else break;
        }
    }

    private void heapifyDown(int index) {
        int smallest = index;
        int left = 2*index + 1;
        int right = 2*index + 2;

        if (left < heap.size() &&
                heap.get(left).getCurrentLoad() <
                        heap.get(smallest).getCurrentLoad()) {
            smallest = left;
        }

        if (right < heap.size() &&
                heap.get(right).getCurrentLoad() <
                        heap.get(smallest).getCurrentLoad()) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    private void swap(int i, int j) {
        Doctor temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public ArrayList<Doctor> getAllDoctors() {
        return heap;
    }
}