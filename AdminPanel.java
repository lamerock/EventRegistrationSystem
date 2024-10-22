import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    private JDesktopPane desktop;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktop = new JDesktopPane();
        setContentPane(desktop);
        createMenuBar();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu userMenu = new JMenu("Users");
        JMenuItem manageUsersItem = new JMenuItem("Manage Users");
        manageUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageUsersForm();
            }
        });
        userMenu.add(manageUsersItem);
        menuBar.add(userMenu);

        JMenu eventMenu = new JMenu("Events");
        JMenuItem approveEventsItem = new JMenuItem("Approve Events");
        approveEventsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openApproveEventsForm();
            }
        });
        eventMenu.add(approveEventsItem);
        menuBar.add(eventMenu);

        JMenu reportMenu = new JMenu("Reports");
        JMenuItem viewReportsItem = new JMenuItem("View Reports");
        viewReportsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewReportsForm();
            }
        });
        reportMenu.add(viewReportsItem);
        menuBar.add(reportMenu);

        setJMenuBar(menuBar);
    }

    private void openManageUsersForm() {
        ManageUsersForm manageUsersForm = new ManageUsersForm();
        desktop.add(manageUsersForm);
        manageUsersForm.setVisible(true);
    }

    private void openApproveEventsForm() {
        ApproveEventsForm approveEventsForm = new ApproveEventsForm();
        desktop.add(approveEventsForm);
        approveEventsForm.setVisible(true);
    }

    private void openViewReportsForm() {
        ViewReportsForm viewReportsForm = new ViewReportsForm();
        desktop.add(viewReportsForm);
        viewReportsForm.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminPanel();
    }
}
