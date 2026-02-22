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
    

    //traverse forward
    

    //get last patients
    

    public int size(){
        return size;
    }

    public boolean isEmply(){
        return size == 0;
    }

}
