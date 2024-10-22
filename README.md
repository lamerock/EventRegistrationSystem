# Event Registration System

## Overview

The Event Registration System is a Java-based desktop application built with Swing and SQLite. The system allows users to manage and register for events. It includes role-based access control for different types of users: **Attendees**, **Organizers**, and **Administrators**.

### Features

1. **Attendee Panel:**
   - View Available Events
   - Register for Events
   - View Registered Events
   - Cancel Registration
   - Logout

2. **Organizer Panel:**
   - Create New Events
   - Manage My Events (Edit/Delete)
   - Approve or Deny Event Registrations
   - Cancel Events
   - Logout

3. **Admin Panel:**
   - Manage Users (Add, Edit, Delete)
   - Approve or Deny Events
   - View System Reports (Event Statistics, Registration Summaries, Payment Reports)
   - Logout

## Technologies Used

- **Java (Swing)** for the graphical user interface (GUI)
- **SQLite** for database storage and management
- **JDBC** for database connectivity

## Installation

### Prerequisites

1. Install Java (JDK 8 or higher).
2. Install SQLite.
3. Clone this repository or download the source files.

### Database Setup

1. Create a SQLite database named `events.db`.
2. Execute the provided SQL scripts to set up the necessary tables (`events`, `users`, `registrations`, etc.).
3. Use sample data to populate the tables if needed.

### Running the Application

1. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
2. Compile and run the main application class: `Main.java`.

## User Roles

### Attendee
- Register for events, view details, and manage registrations.

### Organizer
- Manage events, approve/deny registrations, and cancel events they manage.

### Admin
- Full control over user management, event approval, and viewing reports.

## Key Forms and Functionality

### AttendeePanel
- **View Available Events:** Displays upcoming events in a table format.
- **My Registrations:** Lists events the attendee has registered for.
- **View Event Details:** Displays detailed information about selected events.
- **Cancel Registration:** Allows the user to cancel event registration.

### OrganizerPanel
- **Create New Event:** Organizers can create new events by entering event details (title, description, date, location).
- **Manage My Events:** Allows organizers to edit or delete their created events.
- **Approve/Deny Registrations:** Organizers can approve or deny attendee registrations for their events.

### AdminPanel
- **Manage Users:** Admins can add, edit, or delete users (attendees, organizers).
- **Approve Events:** Admins can approve or deny events created by organizers before they are visible to attendees.
- **View Reports:** Admins can generate reports such as event statistics, registration summaries, and payment reports.

## Error Handling

The system includes error handling to manage database exceptions, input validation (e.g., checking for empty fields), and user-friendly error messages.

## Future Improvements

- **Email Notifications:** Implement email notifications for event approvals and registration confirmations.
- **Multi-language Support:** Add localization for different languages.
- **Search and Filtering:** Enhance event listings with search and filter options.

## Contributors

- **GERARD JAMES PAGLINGAYEN** - Developer

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
