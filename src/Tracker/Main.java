package Tracker;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner sc = new Scanner(System.in);
        manager.loadFromFile();

        while (true) {
            System.out.println("\n==== Personal Expense Tracker ====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Summary");
            System.out.println("4. Filter by Category");
            System.out.println("5. Filter by Month");
            System.out.println("6. Save & Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter date (yyyy-mm-dd) or press Enter for today: ");
                    String dateInput = sc.nextLine();
                    LocalDate date;
                    if (dateInput.isEmpty()) {
                        date = LocalDate.now();
                    } else {
                        date = LocalDate.parse(dateInput);
                    }

                    System.out.print("Enter note: ");
                    String note = sc.nextLine();

                    manager.addExpense(category, amount, date, note);
                    break;

                case 2:
                    manager.viewExpenses();
                    break;

                case 3:
                    manager.viewSummary();
                    break;

                case 4:
                    System.out.print("Enter category to filter: ");
                    String cat = sc.nextLine();
                    manager.filterByCategory(cat);
                    break;

                case 5:
                    System.out.print("Enter month (1-12): ");
                    int month = sc.nextInt();
                    System.out.print("Enter year (e.g., 2025): ");
                    int year = sc.nextInt();
                    sc.nextLine();
                    manager.filterByMonth(month, year);
                    break;

                case 6:
                    manager.saveToFile();
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
