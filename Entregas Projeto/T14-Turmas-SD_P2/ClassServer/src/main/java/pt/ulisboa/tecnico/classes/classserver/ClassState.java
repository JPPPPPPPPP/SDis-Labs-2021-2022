package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassState {
    int capacity;
    boolean openEnrollments = false;
    Map<String, Student> enrolled = new ConcurrentHashMap<>();
    Map<String, Student> discarded = new ConcurrentHashMap<>();

    public ClassState () {

    }

    public void setCapacity(int cap) {
        this.capacity = cap;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setOpenEnrollments(boolean newState) {
        this.openEnrollments = newState;
    }

    public void setEnrolled(List<Student> students) {
        enrolled.clear();
        for (Student s : students) {
            enrolled.put(s.getStudentId(),s);
        }
    }

    public void setDiscarded(List<Student> students) {
        discarded.clear();
        for (Student s : students) {
            discarded.put(s.getStudentId(),s);
        }
    }

    public boolean getOpenEnrollments() {
        return this.openEnrollments;
    }

    public Map<String, Student> getEnrolledStudents() {
        return this.enrolled;
    }

    public Map<String, Student> getDiscardedStudents() {
        return this.discarded;
    }

    public Student getStudentFromEnrolled(String studentId) {
        return this.enrolled.get(studentId);
    }

    public Student getStudentFromDiscarded(String studentId) {
        return this.discarded.get(studentId);
    }

    public void addToEnrolled(Student s) {
        this.enrolled.put(s.getStudentId(), s);
    }

    public void removeFromEnrolled(Student s) {
        this.enrolled.remove(s.getStudentId());
    }

    public void addToDiscarded(Student s) {
        this.discarded.put(s.getStudentId(), s);
    }

    public void removeFromDiscarded(Student s) {
        this.discarded.remove(s.getStudentId());
    }

    public ClassesDefinitions.ClassState asMessage () {
        return ClassesDefinitions.ClassState.newBuilder()
                .setCapacity(capacity)
                .setOpenEnrollments(openEnrollments)
                .addAllEnrolled(enrolled.values())
                .addAllDiscarded(discarded.values())
                .build();
    }
}