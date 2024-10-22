import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAvailableEventsForm extends JInternalFrame {
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public ViewAvailableEventsForm() {
        setTitle("View Available Events");
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
    }

    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT id, title, description, start_time, end_time, location, status FROM events WHERE status = 'approved'";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("location"),
                        rs.getString("status")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage());
        }
    }
}
