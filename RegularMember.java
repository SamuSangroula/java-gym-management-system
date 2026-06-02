
/**
 * RegularMember represents standard gym members with tiered pricing plans.
 * Regular members can earn loyalty points through attendance and become eligible for plan upgrades.
 *
 * @author Samu Sangroula
 * @version 0.5
 */
public class RegularMember extends GymMember {

    private final int attendanceLimit = 30;     // Number of visits needed for upgrade eligibility
    private boolean isEligibleForUpgrade;       // Whether member qualifies for plan upgrade
    private String removalReason;               // If membership was terminated, stores the reason
    private String referralSource;              // How the member heard about the gym
    private String plan;                        // Current subscription plan (basic, standard, deluxe)
    private double price;                       // Monthly price for the current plan

    /**
     * Creates a new regular gym member with basic membership. All regular
     * members start with the basic plan at 6500 price point.
     *
     * @param id Unique identifier for this member
     * @param name Full name of the member
     * @param location Where the member is located/resides
     * @param phone Contact phone number
     * @param email Email address
     * @param gender Member's gender
     * @param DOB Date of Birth (YYYY-MM-DD format)
     * @param membershipStartDate When the membership begins (YYYY-MM-DD format)
     * @param referralSource How the member learned about our gym
     */
    public RegularMember(int id, String name, String location, String phone, String email,
            String gender, String DOB, String membershipStartDate, String referralSource) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.isEligibleForUpgrade = false;
        this.removalReason = "";
        this.referralSource = referralSource;
        this.plan = "basic";
        this.price = 6500;
    }

    /**
     * Records a gym visit and adds loyalty points. Regular members earn 5
     * loyalty points per visit. After reaching the attendance limit, they
     * become eligible for plan upgrades.
     */
    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 5;
            if (attendance >= attendanceLimit) {
                isEligibleForUpgrade = true;
            }
        }
    }

    /**
     * Gets the price for a specific membership plan. This is useful when
     * displaying plan options or calculating upgrade costs.
     *
     * @param plan The plan name to get pricing for (basic, standard, deluxe)
     * @return The price of the plan, or -1 if the plan name is invalid
     */
    public double getPlanPrice(String plan) {
        switch (plan.toLowerCase()) {
            case "basic":
                return 6500;
            case "standard":
                return 12500;
            case "deluxe":
                return 18500;
            default:
                return -1;  // Invalid plan name
        }
    }

    /**
     * Upgrades the member's plan if they're eligible. Members need to reach the
     * attendance limit before upgrading.
     *
     * @param newPlan The plan to upgrade to
     * @return A message indicating success or the reason for failure
     */
    public String upgradePlan(String newPlan) {
        if (!isEligibleForUpgrade) {
            return "Member is not eligible for upgrade yet.";
        }

        if (newPlan.equalsIgnoreCase(plan)) {
            return "Member is already on this plan.";
        }

        double newPrice = getPlanPrice(newPlan);
        if (newPrice == -1) {
            return "Invalid plan selected.";
        }

        this.plan = newPlan;
        this.price = newPrice;
        return "Plan upgraded successfully to " + newPlan + ".";
    }

    /**
     * Resets a member's status and records the reason for removal. This is used
     * when a membership is cancelled or terminated.
     *
     * @param removalReason The reason why the membership was terminated
     */
    public void revertRegularMember(String removalReason) {
        super.resetMember();
        this.isEligibleForUpgrade = false;
        this.plan = "basic";
        this.price = 6500;
        this.removalReason = removalReason;
    }

    /**
     * Displays all member information including regular-specific details. This
     * extends the parent class display method.
     */
    @Override
    public void display() {
        super.display();
        System.out.println("Plan: " + plan);
        System.out.println("Price: " + price);
        if (!removalReason.isEmpty()) {
            System.out.println("Removal Reason: " + removalReason);
        }
    }

    /**
     * Sets the member's subscription plan.
     *
     * @param plan The plan name to set
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }

    /**
     * Sets the membership price.
     *
     * @param price The price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Checks if the member is eligible for a plan upgrade.
     *
     * @return true if eligible for upgrade, false otherwise
     */
    public boolean getIsEligibleForUpgrade() {
        return isEligibleForUpgrade;
    }

    /**
     * Gets the reason for membership termination if any.
     *
     * @return The removal reason or empty string if active
     */
    public String getRemovalReason() {
        return removalReason;
    }

    /**
     * Gets the source that referred this member to the gym.
     *
     * @return The referral source
     */
    public String getReferralSource() {
        return referralSource;
    }

    /**
     * Gets the member's current subscription plan.
     *
     * @return The plan name
     */
    public String getPlan() {
        return plan;
    }

    /**
     * Gets the price of the member's current plan.
     *
     * @return The current price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the number of visits required before becoming upgrade-eligible.
     *
     * @return The attendance limit
     */
    public int getAttendanceLimit() {
        return attendanceLimit;
    }
}
