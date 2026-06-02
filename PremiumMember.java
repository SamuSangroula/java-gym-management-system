
/**
 * PremiumMember represents high-tier gym members with special privileges.
 * Premium members receive exclusive benefits like personal trainers and discounts
 * for full payments.
 *
 * @author Samu Sangroula
 * @version 0.5
 */
public class PremiumMember extends GymMember {

    private final double premiumCharge = 50000;   // Fixed annual fee for premium membership
    private String personalTrainer;               // Assigned fitness coach 
    private boolean isFullPayment;                // Whether full payment has been made
    private double paidAmount;                    // Total amount paid so far
    private double discountAmount;                // Discount applied for full payment

    /**
     * Creates a new premium gym member with all required information. Premium
     * members start with 0 payment and inactive membership status.
     *
     * @param id Unique identifier for this member
     * @param name Full name of the member
     * @param location Where the member is located/resides
     * @param phone Contact phone number
     * @param email Email address
     * @param gender Member's gender
     * @param DOB Date of Birth (YYYY-MM-DD format)
     * @param membershipStartDate When the membership begins (YYYY-MM-DD format)
     * @param personalTrainer Name of the assigned personal trainer
     */
    public PremiumMember(int id, String name, String location, String phone, String email,
            String gender, String DOB, String membershipStartDate, String personalTrainer) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.personalTrainer = personalTrainer;
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
    }

    /**
     * Records a gym visit and adds loyalty points. Premium members earn 10
     * loyalty points per visit (double regular members).
     */
    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 10;
        }
    }

    /**
     * Processes a payment toward the premium membership fee. Members can pay in
     * installments until reaching the full amount.
     *
     * @param amount The payment amount to process
     * @return A message indicating the payment status and remaining balance
     */
    public String payDueAmount(double amount) {
        if (isFullPayment) {
            return "Payment already completed.";
        }

        if (amount <= 0) {
            return "Invalid amount entered.";
        }

        if (paidAmount + amount > premiumCharge) {
            return "Amount exceeds the total premium charge.";
        }

        paidAmount += amount;
        if (paidAmount == premiumCharge) {
            isFullPayment = true;
            return "Payment completed successfully. Membership fully paid.";
        }

        double remainingAmount = premiumCharge - paidAmount;
        return "Payment of " + amount + " received. Remaining amount: " + remainingAmount;
    }

    /**
     * Calculates a discount for members who pay the full premium charge at
     * once. Premium members get a 10% discount for paying in full.
     */
    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = premiumCharge * 0.10;
        } else {
            discountAmount = 0;
        }
    }

    /**
     * Resets a premium member's status back to the initial state. This clears
     * all payment information and trainer assignment.
     */
    public void revertPremiumMember() {
        super.resetMember();
        this.personalTrainer = "";
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
    }

    /**
     * Displays all member information including premium-specific details. This
     * extends the parent class display method with premium information.
     */
    @Override
    public void display() {
        super.display();
        System.out.println("Personal Trainer: " + personalTrainer);
        System.out.println("Paid Amount: " + paidAmount);
        System.out.println("Full Payment: " + isFullPayment);
        System.out.println("Remaining Amount: " + (premiumCharge - paidAmount));
        if (isFullPayment) {
            System.out.println("Discount Amount: " + discountAmount);
        }
    }

    /**
     * Gets the total premium membership fee.
     *
     * @return The premium charge amount
     */
    public double getPremiumCharge() {
        return premiumCharge;
    }

    /**
     * Gets the name of the assigned personal trainer.
     *
     * @return The personal trainer's name
     */
    public String getPersonalTrainer() {
        return personalTrainer;
    }

    /**
     * Checks if the member has made full payment.
     *
     * @return true if fully paid, false otherwise
     */
    public boolean getIsFullPayment() {
        return isFullPayment;
    }

    /**
     * Gets the total amount paid so far.
     *
     * @return The paid amount
     */
    public double getPaidAmount() {
        return paidAmount;
    }

    /**
     * Gets the discount applied for full payment.
     *
     * @return The discount amount
     */
    public double getDiscountAmount() {
        return discountAmount;
    }
}
