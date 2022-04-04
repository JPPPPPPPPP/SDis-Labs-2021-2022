package pt.ulisboa.tecnico.classes.classserver;

import java.util.logging.Logger;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.Student;

public class Classe {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    ClassState classState = new ClassState();

    private boolean mode = false;

    public void openEnrollments(int cap) {
        classState.setOpenEnrollments(true);
        classState.setCapacity(cap);
    }

    public void closeEnrollments() {
        classState.setOpenEnrollments(false);
    }

    public void cancelEnrollment(String studentId) {
        Student temp = classState.getStudentFromEnrolled(studentId);
        classState.removeFromEnrolled(temp);
        classState.addToDiscarded(temp);
    }

    public boolean isEnrolledStudent(String studentId) {
        return this.classState.getEnrolledStudents().containsKey(studentId);
    }

    public ClassState getClassState() {
        return classState;
    }

    public Student createStudent(String id,String name){
        Student  s= Student.newBuilder().setStudentId(id).setStudentName(name).build();
        return s;
    }

    public void enroll(String id ,String nome){

        if (classState.getOpenEnrollments()==true){
            if (classState.getCapacity()>0){
                classState.addToEnrolled(createStudent(id,nome));
            }
        }
    }

    public boolean getMode() {
        return mode;
    }

    public void setMode() {
        mode = true;
    }
}
