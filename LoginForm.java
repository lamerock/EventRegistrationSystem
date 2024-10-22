import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton logoutButton;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);
        
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 80, 80, 25);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        panel.add(logoutButton);
        logoutButton.setVisible(false); // Hide initially
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        authenticateUser(username, password);
    }

    private void authenticateUser(String username, String password) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT id, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int idx = rs.getInt("id");
                JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);
                // Redirect based on role
                redirectUser(role, idx);
                logoutButton.setVisible(true); // Show logout button
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage());
        }
    }

    private void redirectUser(String role, int idx) {
        // Implement redirection logic based on role
        if (role.equals("admin")) {
            new AdminPanel();
        } else if (role.equals("organizer")) {
            UserSession.getInstance().setOrganizerId(idx);
            new OrganizerPanel();
        } else if (role.equals("attendee")) {
            UserSession.getInstance().setAttendeeId(idx);
            new AttendeePanel();
        }
        this.dispose(); // Close login form
    }

    private void logout() {
        // Clear session data if any (not applicable in this simple case)
        JOptionPane.showMessageDialog(this, "Logged out successfully!");
        new LoginForm(); // Show the login form again
        this.dispose(); // Close the current panel
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
