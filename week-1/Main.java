import mypack.MyPackage;

abstract class Main {
    /**
     * This is the main method that serves as the entry point for the Java application.
     * It prints "Hello, World!" to the console.
     */
    public String fname = "Kien";
    public int age = 24;
    public abstract void study();
}

class Student extends Main {
    public int graduationYear = 2024;
    public void study() {
        System.out.println("Studying all day long");
    }
}