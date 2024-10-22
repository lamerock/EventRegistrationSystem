public class UserSession {
  private static UserSession instance;
  private int organizerId;
  private int attendeeId;

  private UserSession() {
      // Private constructor to prevent instantiation
  }

  public static UserSession getInstance() {
      if (instance == null) {
          instance = new UserSession();
      }
      return instance;
  }

  public int getOrganizerId() {
      return organizerId;
  }

  public void setOrganizerId(int organizerId) {
      this.organizerId = organizerId;
  }
  public int getAttendeeId() {
    return attendeeId;
}

public void setAttendeeId(int attendeeId) {
    this.attendeeId = attendeeId;
}
}
