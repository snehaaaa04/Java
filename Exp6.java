import java.io.*;

class Student implements Serializable{
    private int studentId;
    private String name;
    public Student(int studentId, String name){
        this.studentId = studentId;
        this.name = name;
    }
    public void display(){
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + name);
    }
}

public class Main {
    public static void main(String[] args) {
        String filename = "student.ser";
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
            Student s1 = new Student(101, "Sneha");
            out.writeObject(s1);
            System.out.println("Object serialized");

        }catch(IOException e){
            e.printStackTrace();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Student s2 = (Student) in.readObject();
            System.out.println("Object deserialized:");
            s2.display();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
