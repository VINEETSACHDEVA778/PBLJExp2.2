import java.io.*;
import java.util.*;

// ==================== Main Application ====================
public class CombinedExp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Autoboxing/Unboxing Demo");
            System.out.println("2. Student Serialization Demo");
            System.out.println("3. Employee Management");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    AutoboxingUnboxingDemo.run(sc);
                    break;
                case 2:
                    StudentSerializationDemo.run(sc);
                    break;
                case 3:
                    EmployeeManagement.run(sc);
                    break;
                case 4:
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 4);

        sc.close();
    }
}

// ==================== Autoboxing/Unboxing Demo ====================
class AutoboxingUnboxingDemo {
    public static void run(Scanner sc) {
        System.out.println("\n--- Autoboxing / Unboxing Demo ---");
        System.out.print("Enter numbers separated by commas: ");
        String input = sc.nextLine();

        ArrayList<Integer> numbers = new ArrayList<>();
        String[] parts = input.split(",");

        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                try {
                    Integer num = Integer.valueOf(trimmed); // Autoboxing
                    numbers.add(num);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid number: " + trimmed);
                }
            }
        }

        int sum = 0;
        for (Integer num : numbers) {
            sum += num; // Unboxing
        }

        System.out.println("Sum of valid numbers = " + sum);
    }
}

// ==================== Student Serialization Demo ====================
class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int studentID;
    private String name;
    private String grade;

    public Student(int studentID, String name, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    public void displayStudentInfo() {
        System.out.println("\n--- Student Details ---");
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Grade: " + grade);
    }
}

class StudentSerializationDemo {
    private static final String FILE_NAME = "student.ser";

    public static void run(Scanner scanner) {
        System.out.println("\n--- Student Serialization Demo ---");

        System.out.print("Enter Student ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter Student ID (number): ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Student Grade: ");
        String grade = scanner.nextLine();

        Student student = new Student(id, name, grade);

        // Serialization
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(student);
            System.out.println("\n✅ Student object has been serialized to " + FILE_NAME);

        } catch (IOException e) {
            System.out.println("❌ Error during serialization: " + e.getMessage());
            return;
        }

        // Deserialization
        try (FileInputStream fileIn = new FileInputStream(FILE_NAME);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            Student deserializedStudent = (Student) in.readObject();
            System.out.println("\n✅ Student object has been deserialized.");
            deserializedStudent.displayStudentInfo();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error during deserialization: " + e.getMessage());
        }
    }
}

// ==================== Employee Management ====================
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int id;
    private String designation;
    private double salary;

    public Employee(String name, int id, String designation, double salary) {
        this.name = name;
        this.id = id;
        this.designation = designation;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee ID: " + id +
               ", Name: " + name +
               ", Designation: " + designation +
               ", Salary: " + salary;
    }
}

class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset(); // Avoid writing a new header
    }
}

class EmployeeManagement {
    private static final String FILE_NAME = "employees.dat";

    public static void run(Scanner sc) {
        int choice;
        do {
            System.out.println("\n==== Employee Management System ====");
            System.out.println("1. Add an Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Exit Employee Management");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addEmployee(sc);
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    System.out.println("Exiting Employee Management...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 3);
    }

    private static void addEmployee(Scanner sc) {
        try {
            System.out.print("Enter Employee ID: ");
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter numeric ID: ");
                sc.next();
            }
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Employee Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Designation: ");
            String designation = sc.nextLine();

            System.out.print("Enter Salary: ");
            while (!sc.hasNextDouble()) {
                System.out.print("Invalid input. Enter numeric salary: ");
                sc.next();
            }
            double salary = sc.nextDouble();

            Employee emp = new Employee(name, id, designation, salary);

            try (FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
                 ObjectOutputStream oos = (new File(FILE_NAME).length() == 0)
                         ? new ObjectOutputStream(fos)
                         : new AppendableObjectOutputStream(fos)) {

                oos.writeObject(emp);
                System.out.println("\n✅ Employee added successfully!");

            } catch (IOException e) {
                System.out.println("❌ Error saving employee: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
    }

    private static void displayEmployees() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            System.out.println("\n⚠️ No employees found yet.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            System.out.println("\n--- Employee Details ---");
            while (true) {
                try {
                    Employee emp = (Employee) ois.readObject();
                    System.out.println(emp);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error reading employees: " + e.getMessage());
        }
    }
}
