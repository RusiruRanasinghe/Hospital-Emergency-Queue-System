package ds;
import model.Patient;
import java.util.ArrayList;
import java.util.List;


public class TreatedHistoryList {
    private static class Node{
        Patient patient;
        Node prev;
        Node next;

        Node (Patient patient){
            this.patient = patient;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public TreatedHistoryList(){
        head = null;
        tail = null;
        size = 0;
    }

    //insert at last
    public void addToHistory(Patient patient){
        Node newNode = new Node(patient);

        if(head == null){
            head = tail = newNode;
        }else{
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    //remove last patient
    public Patient removeLast(){
        if (tail == null) return null;

        Patient remove = tail.patient;

        if(head == tail){
            head = tail = null;
        }else {
            tail = tail.prev;
            tail.next = null;
        }

        size--;
        return remove;
    }

    //traverse forward
    public List<Patient> getAllHistory() {

        List<Patient> list = new ArrayList<>();
        Node current = head;

        while(current != null){
            list.add(current.patient);
            current = current.next;
        }

        return list;
    }

    //get last patients
    public List<Patient> getLastN(int n){
        List<Patient> list = new ArrayList<>();
        Node current = tail;
        int count = 0;

        while(current != null && count < n){
            list.add(current.patient);
            current = current.prev;
            count++;
        }

        return list;
    }

    public int size(){
        return size;
    }

    public boolean isEmply(){
        return size == 0;
    }

}
