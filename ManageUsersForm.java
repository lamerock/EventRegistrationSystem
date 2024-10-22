import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ManageUsersForm extends JInternalFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUsersForm() {
        setTitle("Manage Users");
        setSize(600, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        initializeComponents();
        loadUserData();
    }

    private void initializeComponents() {
        // Create table with a DefaultTableModel
        String[] columnNames = {"User ID", "Username", "Password", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Create buttons
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton closeButton = new JButton("Close");

        // Add action listeners
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        // Set layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        tableModel.setRowCount(0); // Clear existing rows
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("password")); // Handle securely in production
                row.add(rs.getString("role"));
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void addUser() {
        // Logic to add a new user (you can create a separate dialog for this)
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        String role = JOptionPane.showInputDialog(this, "Enter Role:");

        // Validate input
        if (isInputValid(username, password, role)) {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, role);
                pstmt.executeUpdate();
                loadUserData(); // Refresh the table
                JOptionPane.showMessageDialog(this, "User added successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String password = (String) tableModel.getValueAt(selectedRow, 2);
            String role = (String) tableModel.getValueAt(selectedRow, 3);

            // Use input dialogs to edit the user information
            String newUsername = JOptionPane.showInputDialog(this, "Edit Username:", username);
            String newPassword = JOptionPane.showInputDialog(this, "Edit Password:", password);
            String newRole = JOptionPane.showInputDialog(this, "Edit Role:", role);

            // Validate input
            if (isInputValid(newUsername, newPassword, newRole)) {
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newUsername);
                    pstmt.setString(2, newPassword);
                    pstmt.setString(3, newRole);
                    pstmt.setInt(4, userId);
                    pstmt.executeUpdate();
                    loadUserData(); // Refresh the table
                    JOptionPane.showMessageDialog(this, "User updated successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "DELETE FROM users WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                    loadUserData(); // Refresh the table
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    // Method to validate input
    private boolean isInputValid(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (role == null || role.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Role cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
