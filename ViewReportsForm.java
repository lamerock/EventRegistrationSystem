import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewReportsForm extends JInternalFrame {
    public ViewReportsForm() {
        setTitle("View Reports");
        setSize(400, 300);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        initializeComponents();
    }

    private void initializeComponents() {
        // Set layout
        setLayout(new FlowLayout());

        // Create buttons
        JButton generateReportsButton = new JButton("Generate Reports");
        JButton viewEventStatisticsButton = new JButton("View Event Statistics");
        JButton closeButton = new JButton("Close");

        // Add action listeners
        generateReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReports();
            }
        });

        viewEventStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEventStatistics();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        // Add buttons to the form
        add(generateReportsButton);
        add(viewEventStatisticsButton);
        add(closeButton);
    }

    private void generateReports() {
        // Logic for generating reports
        String report = "Please select a report type to generate:\n" +
                        "1. Event Statistics\n" +
                        "2. Registration Summary\n" +
                        "3. Payment Report";

        String input = JOptionPane.showInputDialog(this, report);
        if (input != null) {
            switch (input.trim()) {
                case "1":
                    generateEventStatisticsReport();
                    break;
                case "2":
                    generateRegistrationSummaryReport();
                    break;
                case "3":
                    generatePaymentReport();
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid selection.");
            }
        }
    }

    private void generateEventStatisticsReport() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT status, COUNT(*) AS total FROM events GROUP BY status";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder report = new StringBuilder("Event Statistics Report:\n");
            while (rs.next()) {
                report.append("Status: ").append(rs.getString("status"))
                      .append(", Total: ").append(rs.getInt("total")).append("\n");
            }
            JOptionPane.showMessageDialog(this, report.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void generateRegistrationSummaryReport() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT COUNT(*) AS totalRegistrations FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String report = "Registration Summary Report:\n" +
                                "Total Registrations: " + rs.getInt("totalRegistrations");
                JOptionPane.showMessageDialog(this, report);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void generatePaymentReport() {
        // Placeholder: Implement payment report logic if applicable
        // This assumes you have a payments table or similar
        JOptionPane.showMessageDialog(this, "Payment report generation not implemented.");
    }

    private void viewEventStatistics() {
        StringBuilder report = new StringBuilder();
        report.append("Event Statistics:\n\n");

        try (Connection conn = DatabaseConnection.connect()) {
            // Count of events by status
            String sqlStatusCount = "SELECT status, COUNT(*) AS total FROM events GROUP BY status";
            PreparedStatement pstmtStatus = conn.prepareStatement(sqlStatusCount);
            ResultSet rsStatus = pstmtStatus.executeQuery();
            while (rsStatus.next()) {
                report.append("Status: ").append(rsStatus.getString("status"))
                      .append(", Total: ").append(rsStatus.getInt("total")).append("\n");
            }

            // Average duration of events
            String sqlAvgDuration = "SELECT AVG(strftime('%s', end_time) - strftime('%s', start_time)) AS avg_duration FROM events";
            PreparedStatement pstmtAvgDuration = conn.prepareStatement(sqlAvgDuration);
            ResultSet rsAvgDuration = pstmtAvgDuration.executeQuery();
            if (rsAvgDuration.next()) {
                report.append("Average Event Duration: ")
                      .append(rsAvgDuration.getInt("avg_duration") / 3600).append(" hours\n");
            }

            // Upcoming events
            report.append("\nUpcoming Events:\n");
            String sqlUpcomingEvents = "SELECT title, start_time FROM events WHERE start_time > datetime('now')";
            PreparedStatement pstmtUpcoming = conn.prepareStatement(sqlUpcomingEvents);
            ResultSet rsUpcoming = pstmtUpcoming.executeQuery();
            while (rsUpcoming.next()) {
                report.append("Title: ").append(rsUpcoming.getString("title"))
                      .append(", Start Time: ").append(rsUpcoming.getString("start_time")).append("\n");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }

        // Display the report
        JOptionPane.showMessageDialog(this, report.toString());
    }
}
