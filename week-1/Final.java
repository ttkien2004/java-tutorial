// package week-1;
import java.util.ArrayList;
import java.util.Scanner;

public class Final {
    static ArrayList<Student> students = new ArrayList<Student>();

    static class Student {
        public String fname;
        public int age;
        public String graduationYear;

        public void setFname(String fname) {
            this.fname = fname;
        }
        public void setAge(int age){
            this.age = age;
        }
        public void setGraduationYear(String graduationYear) {
            this.graduationYear = graduationYear;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option from 1 to 4: \n1. Add Student\n2. View Students\n3. Delete Student\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1: // Add Student 
                    System.out.println("Enter student first name:");
                    String fname = scanner.nextLine();
                    System.out.println("Enter student age:");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter student graduation year:");
                    String graduationYear = scanner.nextLine();

                    Student student = new Student();
                    student.setFname(fname);
                    student.setAge(age);
                    student.setGraduationYear(graduationYear);
                    students.add(student);
                    System.out.println("Student added successfully!");
                    break;
                case 2: // View students
                    System.out.println("List of students:");
                    System.out.println(students.size() + " students found.");
                    for (Student s : students) {
                        System.out.println("Name: " + s.fname + ", Age: " + s.age + ", Graduation Year: " + s.graduationYear);
                    }
                    break;
                case 3: // Delete Student
                    System.out.println("Enter the fname of the student to delete: ");
                    String delFname = scanner.nextLine();
                    for (Student s: students) {
                        if (s.fname.equals(delFname)) {
                            students.remove(s);
                            break;
                        }
                    }
                    break;
                case 4: //Exit
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }   
        }
    }
}
