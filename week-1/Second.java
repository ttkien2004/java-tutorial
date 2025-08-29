// package mypack;
import mypack.MyPackage;

public class Second {
    public static void main(String[] args) {
        Student student = new Student();
        System.out.println("Name: " + student.fname);
        System.out.println("Age: " + student.age);
        System.out.println("Graduation Year: " + student.graduationYear);
        student.study();

        MyPackage myPackage = new MyPackage();
        myPackage.display();
    }
}
