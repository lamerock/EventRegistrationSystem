import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttendeePanel extends JFrame {

    public AttendeePanel() {
        setTitle("Attendee Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setJMenuBar(createMenuBar());

        // Make the panel visible
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create Menu
        JMenu eventMenu = new JMenu("Manage Registration");
        
        // Create Menu Items
        JMenuItem viewAvailableEventsItem = new JMenuItem("View Available Events");
        JMenuItem myRegistrationItem = new JMenuItem("My Registrations");
        JMenuItem viewEventDetailsItem = new JMenuItem("View Event Details");
        JMenuItem cancelRegistrationItem = new JMenuItem("Cancel Registration");
        JMenuItem logoutItem = new JMenuItem("Logout");

        // Add action listeners
        viewAvailableEventsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAvailableEventsForm();
            }
        });

        myRegistrationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myRegistrationForm();
            }
        });

        viewEventDetailsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEventDetailsForm();
            }
        });

        cancelRegistrationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelRegistrationForm();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Add Menu Items to Menu
        eventMenu.add(viewAvailableEventsItem);
        eventMenu.add(myRegistrationItem);
        eventMenu.add(viewEventDetailsItem);
        eventMenu.add(cancelRegistrationItem);
        eventMenu.addSeparator(); // Adds a separator line
        eventMenu.add(logoutItem);

        // Add Menu to Menu Bar
        menuBar.add(eventMenu);

        return menuBar;
    }

    private void viewAvailableEventsForm() {
        // Instantiate and display CreateNewEventForm
        JInternalFrame viewAvailableEventsFrame = new ViewAvailableEventsForm();
        add(viewAvailableEventsFrame);
        viewAvailableEventsFrame.setVisible(true);
    }

    private void myRegistrationForm() {
        // Instantiate and display ManageMyEventsForm
        JInternalFrame myRegistrationsFrame = new MyRegistrationsForm();
        add(myRegistrationsFrame);
        myRegistrationsFrame.setVisible(true);
    }

    private void viewEventDetailsForm() {
        // Instantiate and display ApproveRegistrationsForm
        JInternalFrame viewEventDetailsFrame = new ViewEventDetailsForm();
        add(viewEventDetailsFrame);
        viewEventDetailsFrame.setVisible(true);
    }

    private void cancelRegistrationForm() {
        // Instantiate and display CancelEventForm
        JInternalFrame cancelRegistrationFrame = new CancelRegistrationForm();
        add(cancelRegistrationFrame);
        cancelRegistrationFrame.setVisible(true);
    }
   
    private void logout() {
        // Logic to log out the organizer and return to the login screen
        JOptionPane.showMessageDialog(this, "You have been logged out.");
        dispose(); // Close the Organizer Panel
        // Optionally, you can redirect to the login form here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendeePanel());
    }
}
