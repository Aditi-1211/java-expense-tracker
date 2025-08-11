package Tracker;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private final String FILE_NAME = System.getProperty("user.dir") + File.separator + "expenses.csv";

    public void addExpense(String category, double amount, LocalDate date, String note) {
        expenses.add(new Expense(category, amount, date, note));
        System.out.println("✅ Expense added successfully!");
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("\n--- All Expenses ---");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    public void filterByCategory(String category) {
        boolean found = false;
        System.out.println("\n--- Expenses in Category: " + category + " ---");
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses found in this category.");
        }
    }

    public void filterByMonth(int month, int year) {
        boolean found = false;
        System.out.println("\n--- Expenses for " + month + "/" + year + " ---");
        for (Expense e : expenses) {
            if (e.getDate().getMonthValue() == month && e.getDate().getYear() == year) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses found for this month/year.");
        }
    }

    public void viewSummary() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        System.out.println("\n💰 Total Expenses: ₹" + total);
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                bw.write(e.toCSV());
                bw.newLine();
            }
            System.out.println("💾 Data saved to file: " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        expenses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split into max 4 parts so note stays intact
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String note = parts[3];
                    expenses.add(new Expense(category, amount, date, note));
                }
            }
            System.out.println("📂 Data loaded from file!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous records found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Expense> getAllExpenses() {
        return expenses;
    }

}
