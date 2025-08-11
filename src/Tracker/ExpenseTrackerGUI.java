package Tracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private DefaultTableModel tableModel;

    public ExpenseTrackerGUI() {
        manager = new ExpenseManager();
        manager.loadFromFile();

        setTitle("Personal Expense Tracker");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table to display expenses
        tableModel = new DefaultTableModel(new Object[]{"Date", "Category", "Amount", "Note"}, 0);
        JTable table = new JTable(tableModel);
        loadTableData();

        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton addBtn = new JButton("Add Expense");
        JButton filterCatBtn = new JButton("Filter by Category");
        JButton filterMonthBtn = new JButton("Filter by Month");
        JButton summaryBtn = new JButton("View Summary");
        JButton saveBtn = new JButton("Save & Exit");

        // Button actions
        addBtn.addActionListener(e -> addExpense());
        filterCatBtn.addActionListener(e -> filterByCategory());
        filterMonthBtn.addActionListener(e -> filterByMonth());
        summaryBtn.addActionListener(e -> showSummary());
        saveBtn.addActionListener(e -> {
            manager.saveToFile();
            JOptionPane.showMessageDialog(this, "Data saved. Exiting...");
            System.exit(0);
        });

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(filterCatBtn);
        buttonPanel.add(filterMonthBtn);
        buttonPanel.add(summaryBtn);
        buttonPanel.add(saveBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // Clear existing
        for (Expense e : manager.getAllExpenses()) {
            tableModel.addRow(new Object[]{e.getDate(), e.getCategory(), e.getAmount(), e.getNote()});
        }
    }

    private void addExpense() {
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField noteField = new JTextField();

        Object[] fields = {
                "Category:", categoryField,
                "Amount:", amountField,
                "Date (yyyy-mm-dd):", dateField,
                "Note:", noteField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());
                LocalDate date = LocalDate.parse(dateField.getText());
                String note = noteField.getText();

                manager.addExpense(category, amount, date, note);
                loadTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        }
    }

    private void filterByCategory() {
        String category = JOptionPane.showInputDialog(this, "Enter category to filter:");
        if (category != null && !category.isEmpty()) {
            tableModel.setRowCount(0);
            for (Expense e : manager.getAllExpenses()) {
                if (e.getCategory().equalsIgnoreCase(category)) {
                    tableModel.addRow(new Object[]{e.getDate(), e.getCategory(), e.getAmount(), e.getNote()});
                }
            }
        }
    }

    private void filterByMonth() {
        String monthStr = JOptionPane.showInputDialog(this, "Enter month (1-12):");
        String yearStr = JOptionPane.showInputDialog(this, "Enter year (e.g., 2025):");

        try {
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            tableModel.setRowCount(0);
            for (Expense e : manager.getAllExpenses()) {
                if (e.getDate().getMonthValue() == month && e.getDate().getYear() == year) {
                    tableModel.addRow(new Object[]{e.getDate(), e.getCategory(), e.getAmount(), e.getNote()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid month/year!");
        }
    }

    private void showSummary() {
        double total = manager.getAllExpenses().stream().mapToDouble(Expense::getAmount).sum();
        JOptionPane.showMessageDialog(this, "Total Expenses: ₹" + total);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}
