import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ManageMyEventsForm extends JInternalFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public ManageMyEventsForm() {
        setTitle("Manage My Events");
        setSize(600, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create the table to display events
        String[] columnNames = {"ID", "Organizer ID", "Title", "Description", "Start Time", "End Time", "Location", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventsTable = new JTable(tableModel);
        loadData();

        // Create a JScrollPane for the table
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Edit Event");
        JButton deleteButton = new JButton("Delete Event");
        JButton closeButton = new JButton("Close");

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEvent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEvent();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT id, organizer_id, title, description, start_time, end_time, location, status FROM events WHERE organizer_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getCurrentOrganizerId());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getInt("organizer_id"));
                    row.add(rs.getString("title"));
                    row.add(rs.getString("description"));
                    row.add(rs.getString("start_time"));
                    row.add(rs.getString("end_time"));
                    row.add(rs.getString("location"));
                    row.add(rs.getString("status"));
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage());
        }
    }

    private void editEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit.");
            return;
        }

        // Get the selected event ID
        int eventId = (int) tableModel.getValueAt(selectedRow, 0);

        // Open the EditEventForm with the selected event's details
        EditEventForm editEventForm = new EditEventForm(eventId);
        editEventForm.setVisible(true);
        getParent().add(editEventForm); // Add the form to the parent frame
    }

    private void deleteEvent() {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "DELETE FROM events WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, eventId);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Event deleted successfully.");
                    loadData(); // Refresh the table data
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting event: " + ex.getMessage());
            }
        }
    }

    private int getCurrentOrganizerId() {
        return UserSession.getInstance().getOrganizerId();
    }
}
