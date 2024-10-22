import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ApproveEventsForm extends JInternalFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public ApproveEventsForm() {
        setTitle("Approve Events");
        setSize(800, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        initializeComponents();
        loadEventData();
    }

    private void initializeComponents() {
        // Create table with a DefaultTableModel
        String[] columnNames = {"ID", "Organizer ID", "Title", "Description", "Start Time", "End Time", "Location", "Status", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(eventsTable);

        // Create buttons
        JButton approveButton = new JButton("Approve Event");
        JButton denyButton = new JButton("Deny Event");
        JButton closeButton = new JButton("Close");

        // Add action listeners
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveEvent();
            }
        });

        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                denyEvent();
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
        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEventData() {
        tableModel.setRowCount(0); // Clear existing rows
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM events WHERE status = 'pending'"; // Only show pending events
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getInt("organizer_id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("description"));
                row.add(rs.getTimestamp("start_time"));
                row.add(rs.getTimestamp("end_time"));
                row.add(rs.getString("location"));
                row.add(rs.getString("status"));
                row.add(rs.getTimestamp("created_at"));
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void approveEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) tableModel.getValueAt(selectedRow, 0); // Get event ID

            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "UPDATE events SET status = 'approved' WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, eventId);
                pstmt.executeUpdate();
                loadEventData(); // Refresh the table
                JOptionPane.showMessageDialog(this, "Event approved successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to approve.");
        }
    }

    private void denyEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) tableModel.getValueAt(selectedRow, 0); // Get event ID

            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "UPDATE events SET status = 'cancelled' WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, eventId);
                pstmt.executeUpdate();
                loadEventData(); // Refresh the table
                JOptionPane.showMessageDialog(this, "Event denied successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to deny.");
        }
    }
}
