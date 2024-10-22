import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewEventDetailsForm extends JInternalFrame {
    private JTextArea eventDetailsTextArea;
    private JButton loadDetailsButton;
    private int selectedEventId;

    public ViewEventDetailsForm() {
        setTitle("View Event Details");
        setSize(400, 300);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create a text area for event details
        eventDetailsTextArea = new JTextArea();
        eventDetailsTextArea.setEditable(false);
        add(new JScrollPane(eventDetailsTextArea), BorderLayout.CENTER);

        // Create a button to load event details
        loadDetailsButton = new JButton("Load Event Details");
        loadDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEventDetails(selectedEventId); // You need to set this ID based on the selected event
            }
        });

        add(loadDetailsButton, BorderLayout.SOUTH);
    }

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

    // Method to set the selected event ID
    public void setSelectedEventId(int eventId) {
        this.selectedEventId = eventId;
    }
}
