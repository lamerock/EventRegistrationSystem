import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrganizerPanel extends JFrame {

    public OrganizerPanel() {
        setTitle("Organizer Panel");
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
        JMenu eventMenu = new JMenu("Events");
        
        // Create Menu Items
        JMenuItem createEventItem = new JMenuItem("Create New Event");
        JMenuItem manageEventsItem = new JMenuItem("Manage My Events");
        JMenuItem approveRegistrationsItem = new JMenuItem("Approve Registrations");
        JMenuItem cancelEventItem = new JMenuItem("Cancel Event");
        JMenuItem logoutItem = new JMenuItem("Logout");

        // Add action listeners
        createEventItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewEventForm();
            }
        });

        manageEventsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageMyEventsForm();
            }
        });

        approveRegistrationsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveRegistrationsForm();
            }
        });

        cancelEventItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEventForm();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Add Menu Items to Menu
        eventMenu.add(createEventItem);
        eventMenu.add(manageEventsItem);
        eventMenu.add(approveRegistrationsItem);
        eventMenu.add(cancelEventItem);
        eventMenu.addSeparator(); // Adds a separator line
        eventMenu.add(logoutItem);

        // Add Menu to Menu Bar
        menuBar.add(eventMenu);

        return menuBar;
    }

    private void createNewEventForm() {
        // Instantiate and display CreateNewEventForm
        JInternalFrame createNewEventFrame = new CreateNewEventForm();
        add(createNewEventFrame);
        createNewEventFrame.setVisible(true);
    }

    private void manageMyEventsForm() {
        // Instantiate and display ManageMyEventsForm
        JInternalFrame manageMyEventsFrame = new ManageMyEventsForm();
        add(manageMyEventsFrame);
        manageMyEventsFrame.setVisible(true);
    }

    private void approveRegistrationsForm() {
        // Instantiate and display ApproveRegistrationsForm
        JInternalFrame approveRegistrationsFrame = new ApproveRegistrationsForm();
        add(approveRegistrationsFrame);
        approveRegistrationsFrame.setVisible(true);
    }

    private void cancelEventForm() {
        // Instantiate and display CancelEventForm
        JInternalFrame cancelEventFrame = new CancelEventForm();
        add(cancelEventFrame);
        cancelEventFrame.setVisible(true);
    }
   
    private void logout() {
        // Logic to log out the organizer and return to the login screen
        JOptionPane.showMessageDialog(this, "You have been logged out.");
        dispose(); // Close the Organizer Panel
        // Optionally, you can redirect to the login form here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrganizerPanel());
    }
}
