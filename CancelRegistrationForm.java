import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CancelRegistrationForm extends JInternalFrame {
    private JTable registrationsTable;
    private DefaultTableModel tableModel;

    public CancelRegistrationForm() {
        setTitle("Cancel Registration");
        setSize(600, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create the table to display registrations
        String[] columnNames = {"ID", "Event ID", "Attendee ID", "Registration Time", "Payment Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        registrationsTable = new JTable(tableModel);
        loadData();

        // Create a JScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(registrationsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel Registration");
        JButton closeButton = new JButton("Close");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelRegistration();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        int currentAttendeeId = getCurrentAttendeeId(); // Assume this method retrieves the current attendee's ID

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM registrations WHERE attendee_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, currentAttendeeId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Object[] row = {
                            rs.getInt("id"),
                            rs.getInt("event_id"),
                            rs.getInt("attendee_id"),
                            rs.getString("registration_time"),
                            rs.getString("payment_status")
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading registrations: " + ex.getMessage());
        }
    }

    private void cancelRegistration() {
        int selectedRow = registrationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int registrationId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "DELETE FROM registrations WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, registrationId);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Registration cancelled successfully.");
                        loadData(); // Refresh the table data
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel registration.");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error cancelling registration: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a registration to cancel.");
        }
    }

    private int getCurrentAttendeeId() {
      return UserSession.getInstance().getAttendeeId();
  }
}
