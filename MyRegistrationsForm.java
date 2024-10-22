import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyRegistrationsForm extends JInternalFrame {
    private JTable registrationsTable;
    private DefaultTableModel tableModel;

    public MyRegistrationsForm() {
        setTitle("My Registrations");
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

    private int getCurrentAttendeeId() {
      return UserSession.getInstance().getAttendeeId();
  }
}
