import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ApproveRegistrationsForm extends JInternalFrame {
    private JTable registrationsTable;
    private DefaultTableModel tableModel;


    public ApproveRegistrationsForm() {
        setTitle("Approve Registrations");
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
        JButton approveButton = new JButton("Approve Registration");
        JButton denyButton = new JButton("Deny Registration");
        JButton closeButton = new JButton("Close");

        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveRegistration();
            }
        });

        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                denyRegistration();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);
        buttonPanel.add(closeButton);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.connect()) {
            // SQL query to get registrations for the current organizer's events
            String sql = "SELECT r.id, r.event_id, r.attendee_id, r.registration_time, r.payment_status " +
                         "FROM registrations r " +
                         "JOIN events e ON r.event_id = e.id " +
                         "WHERE e.organizer_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getCurrentOrganizerId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getInt("event_id"));
                        row.add(rs.getInt("attendee_id"));
                        row.add(rs.getString("registration_time"));
                        row.add(rs.getString("payment_status"));
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading registrations: " + ex.getMessage());
        }
    }

    private void approveRegistration() {
        int selectedRow = registrationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a registration to approve.");
            return;
        }

        int registrationId = (int) tableModel.getValueAt(selectedRow, 0);
        updateRegistrationStatus(registrationId, "confirmed");
    }

    private void denyRegistration() {
        int selectedRow = registrationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a registration to deny.");
            return;
        }

        int registrationId = (int) tableModel.getValueAt(selectedRow, 0);
        updateRegistrationStatus(registrationId, "cancelled");
    }

    private void updateRegistrationStatus(int registrationId, String status) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE registrations SET payment_status = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, registrationId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration " + status + " successfully.");
                loadData(); // Refresh the table data
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating registration status: " + ex.getMessage());
        }
    }
    private int getCurrentOrganizerId() {
      return UserSession.getInstance().getOrganizerId();
  }

}
