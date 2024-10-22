import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNewEventForm extends JInternalFrame {
    private JTextField titleField;
    private JTextArea descriptionField;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;

    public CreateNewEventForm() {
        setTitle("Create New Event");
        setSize(450, 500);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setLayout(new BorderLayout());

        // Create a panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Event Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(titleField, gbc);

        // Description field
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionField), gbc);

        // Start Time field
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Start Time:"), gbc);
        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "yyyy-MM-dd HH:mm:ss");
        startTimeSpinner.setEditor(startTimeEditor);
        startTimeSpinner.setValue(new Date()); // Set to current date and time
        gbc.gridx = 1;
        inputPanel.add(startTimeSpinner, gbc);

        // End Time field
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("End Time:"), gbc);
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "yyyy-MM-dd HH:mm:ss");
        endTimeSpinner.setEditor(endTimeEditor);
        endTimeSpinner.setValue(new Date()); // Set to current date and time
        gbc.gridx = 1;
        inputPanel.add(endTimeSpinner, gbc);

        // Location field
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Location:"), gbc);
        locationField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(locationField, gbc);

        // Status field
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Status:"), gbc);
        statusComboBox = new JComboBox<>(new String[]{"pending", "approved", "canceled"});
        gbc.gridx = 1;
        inputPanel.add(statusComboBox, gbc);

        // Add input panel to the frame
        add(inputPanel, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton createEventButton = new JButton("Create New Event");
        JButton closeButton = new JButton("Close");

        // Button Icons (Optional)
        //createEventButton.setIcon(new ImageIcon("path/to/create-icon.png")); // Set appropriate icon path
        //closeButton.setIcon(new ImageIcon("path/to/close-icon.png")); // Set appropriate icon path

        createEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEvent();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        buttonPanel.add(createEventButton);
        buttonPanel.add(closeButton);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createEvent() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String status = (String) statusComboBox.getSelectedItem();

        // Input validation
        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Get the start and end time from JSpinner
        Date startDate = (Date) startTimeSpinner.getValue();
        Date endDate = (Date) endTimeSpinner.getValue();

        // Format the dates to the required format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);

        // Insert the event into the database
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO events (organizer_id, title, description, start_time, end_time, location, status, created_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, getCurrentOrganizerId()); // Replace with actual organizer ID
                pstmt.setString(2, title);
                pstmt.setString(3, description);
                pstmt.setString(4, startTime); // Use formatted start time
                pstmt.setString(5, endTime); // Use formatted end time
                pstmt.setString(6, location);
                pstmt.setString(7, status);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Event created successfully.");
                clearFields(); // Clear fields after submission
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error creating event: " + ex.getMessage());
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionField.setText("");
        startTimeSpinner.setValue(new Date()); // Reset to current date and time
        endTimeSpinner.setValue(new Date()); // Reset to current date and time
        locationField.setText("");
        statusComboBox.setSelectedIndex(0);
    }

    private int getCurrentOrganizerId() {
        return UserSession.getInstance().getOrganizerId();
    }
}
