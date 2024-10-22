import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewEventDetailsForm extends JInternalFrame {
    private JTextArea eventDetailsTextArea;
    private JTable eventsTable;
    private DefaultTableModel tableModel;
    private JButton loadDetailsButton;
    private int selectedEventId;

    public ViewEventDetailsForm() {
        setTitle("View Event Details");
        setSize(600, 400);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create the table model and JTable for events
        tableModel = new DefaultTableModel();
        eventsTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Start Time");
        tableModel.addColumn("End Time");

        // Add table selection listener to get the selected event ID
        eventsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && eventsTable.getSelectedRow() >= 0) {
                    selectedEventId = (int) tableModel.getValueAt(eventsTable.getSelectedRow(), 0);
                }
            }
        });

        // Load events into the table
        loadEventsData();

        // Add the table to a scroll pane
        add(new JScrollPane(eventsTable), BorderLayout.NORTH);

        // Create a text area for event details
        eventDetailsTextArea = new JTextArea();
        eventDetailsTextArea.setEditable(false);
        add(new JScrollPane(eventDetailsTextArea), BorderLayout.CENTER);

        // Create a button to load event details
        loadDetailsButton = new JButton("Load Event Details");
        loadDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEventDetails(selectedEventId); // Load details based on the selected event
            }
        });

        add(loadDetailsButton, BorderLayout.SOUTH);
    }

    // Method to load event details based on selected event ID
    private void loadEventDetails(int eventId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM events WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, eventId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("Title: ").append(rs.getString("title")).append("\n");
                        details.append("Description: ").append(rs.getString("description")).append("\n");
                        details.append("Start Time: ").append(rs.getString("start_time")).append("\n");
                        details.append("End Time: ").append(rs.getString("end_time")).append("\n");
                        details.append("Location: ").append(rs.getString("location")).append("\n");
                        details.append("Status: ").append(rs.getString("status")).append("\n");
                        eventDetailsTextArea.setText(details.toString());
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading event details: " + ex.getMessage());
        }
    }

    // Method to load events data into the JTable
    private void loadEventsData() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT id, title, start_time, end_time FROM events";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");
                    tableModel.addRow(new Object[]{id, title, startTime, endTime});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading events data: " + ex.getMessage());
        }
    }
}
