import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEventForm extends JInternalFrame {
    private JTextField titleField;
    private JTextArea descriptionField;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private int eventId; // Store event ID for editing

    public EditEventForm(int eventId) {
        this.eventId = eventId;
        setTitle("Edit Event");
        setSize(450, 500);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Initialize fields
        titleField = new JTextField(20);
        descriptionField = new JTextArea(3, 20);
        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        locationField = new JTextField(20);
        statusComboBox = new JComboBox<>(new String[]{"pending", "approved", "canceled"});

        // Date editors
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "yyyy-MM-dd HH:mm:ss"));
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, "yyyy-MM-dd HH:mm:ss"));

        // Add fields to panel
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Event Title:"), gbc);
        gbc.gridx = 1; inputPanel.add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; inputPanel.add(new JScrollPane(descriptionField), gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1; inputPanel.add(startTimeSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1; inputPanel.add(endTimeSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; inputPanel.add(locationField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; inputPanel.add(statusComboBox, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // Load event details
        loadEventDetails();

        // Create buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Changes");
        JButton closeButton = new JButton("Close");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEvent();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEventDetails() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT title, description, start_time, end_time, location, status FROM events WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, eventId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    titleField.setText(rs.getString("title"));
                    descriptionField.setText(rs.getString("description"));
                    startTimeSpinner.setValue(rs.getTimestamp("start_time"));
                    endTimeSpinner.setValue(rs.getTimestamp("end_time"));
                    locationField.setText(rs.getString("location"));
                    statusComboBox.setSelectedItem(rs.getString("status"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading event details: " + ex.getMessage());
        }
    }

    private void updateEvent() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String status = (String) statusComboBox.getSelectedItem();
        Date startDate = (Date) startTimeSpinner.getValue();
        Date endDate = (Date) endTimeSpinner.getValue();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);

        // Update the event in the database
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE events SET title = ?, description = ?, start_time = ?, end_time = ?, location = ?, status = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, startTime);
                pstmt.setString(4, endTime);
                pstmt.setString(5, location);
                pstmt.setString(6, status);
                pstmt.setInt(7, eventId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Event updated successfully.");
                dispose(); // Close the form after saving
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating event: " + ex.getMessage());
        }
    }
}
