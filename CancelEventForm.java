import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class CancelEventForm extends JInternalFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public CancelEventForm() {
        setTitle("Cancel Event");
        setSize(600, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create the table to display events
        String[] columnNames = {"ID", "Title", "Description", "Start Time", "End Time", "Location", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventsTable = new JTable(tableModel);
        loadData();

        // Create a JScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel Event");
        JButton closeButton = new JButton("Close");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEvent();
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

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.connect()) {
            // SQL query to get events for the current organizer
            String sql = "SELECT id, title, description, start_time, end_time, location, status " +
                         "FROM events WHERE organizer_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getCurrentOrganizerId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Vector<Object> row = new Vector<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("title"));
                        row.add(rs.getString("description"));
                        row.add(rs.getString("start_time"));
                        row.add(rs.getString("end_time"));
                        row.add(rs.getString("location"));
                        row.add(rs.getString("status"));
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage());
        }
    }

    private void cancelEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to cancel.");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        updateEventStatus(eventId, "cancelled");
    }

    private void updateEventStatus(int eventId, String status) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE events SET status = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, eventId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Event cancelled successfully.");
                loadData(); // Refresh the table data
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling event: " + ex.getMessage());
        }
    }
    private int getCurrentOrganizerId() {
      return UserSession.getInstance().getOrganizerId();
  }
}
