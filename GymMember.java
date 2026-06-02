
/**
 * Abstract base class representing a Gym Member.
 * This serves as the foundation for all member types in our gym management system.
 */
public abstract class GymMember {

    // Basic member information - these fields are protected so subclasses can access them directly
    protected int id;
    protected String name;
    protected String location;
    protected String phone;
    protected String email;
    protected String gender;
    protected String DOB;          // Date of Birth
    protected String membershipStartDate;
    protected int attendance;      // How many times the member has visited
    protected double loyaltyPoints; // Points earned through gym activities
    protected boolean activeStatus; // Whether the membership is currently active

    /**
     * Creates a new gym member with basic information. All members start with
     * inactive status and zero attendance/loyalty points.
     *
     * @param id Unique identifier for this member
     * @param name Full name of the member
     * @param location Where the member is located/resides
     * @param phone Contact phone number
     * @param email Email address
     * @param gender Member's gender
     * @param DOB Date of Birth (YYYY-MM-DD format)
     * @param membershipStartDate When the membership begins (YYYY-MM-DD format)
     */
    public GymMember(int id, String name, String location, String phone, String email,
            String gender, String DOB, String membershipStartDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.DOB = DOB;
        this.membershipStartDate = membershipStartDate;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = false;
    }

    /**
     * Records a gym visit for this member. Each membership type handles
     * attendance differently, so this is abstract.
     */
    public abstract void markAttendance();

    /**
     * Activates the membership so the member can use gym facilities. This is
     * called when a member pays their dues or rejoins after inactivity.
     */
    public void activateMembership() {
        this.activeStatus = true;
    }

    /**
     * Deactivates an active membership. This might happen if a member stops
     * paying, takes a break, or violates gym policies.
     */
    public void deactivateMembership() {
        if (this.activeStatus) {
            this.activeStatus = false;
        }
    }

    /**
     * Resets a member's activity stats and deactivates membership. Useful when
     * starting a new membership period or after long absence.
     */
    public void resetMember() {
        this.activeStatus = false;
        this.attendance = 0;
        this.loyaltyPoints = 0;
    }

    /**
     * Prints out all basic information about this member. Good for debugging or
     * detailed member views.
     */
    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("DOB: " + DOB);
        System.out.println("Membership Start Date: " + membershipStartDate);
        System.out.println("Attendance: " + attendance);
        System.out.println("Loyalty Points: " + loyaltyPoints);
        System.out.println("Active Status: " + activeStatus);
    }

    // Getter methods for all member properties
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDOB() {
        return DOB;
    }

    public String getMembershipStartDate() {
        return membershipStartDate;
    }

    public int getAttendance() {
        return attendance;
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public boolean getActiveStatus() {
        return activeStatus;
    }
}
