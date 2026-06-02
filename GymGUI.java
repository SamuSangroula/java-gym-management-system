
/**
 * GymGUI - Main user interface for the Gym Management System.
 * This class handles all UI components, member management operations,
 * and user interactions for the gym administration application.
 *
 * Features:
 * - Member registration (Regular and Premium)
 * - Membership activation
 * - Attendance tracking
 * - Member information display
 * - Premium membership management
 *
 * Created: April 2025
 *
 * @author Samu Sangroula
 * @version 0.5
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class GymGUI {

    // Constants for UI dimensions and layout
    static private final int WINDOW_SIZE = 800;
    static private final int MENU_WIDTH = 25;

    // Main UI components
    static private JFrame mainFrame;
    static private JPanel panelMenu, panelLogoContainer, panelNavigationContainer, panelActivateMembership, panelViewMembers, panelAddRegularMember, panelContent, panelSecondary, panelMarkAttendance;
    // static private int currentSelectedUserId = 0;

    // Navigation menu options
    static private final String[] NAVIGATION_BUTTONS = {
        "Add Regular Member",
        "Add Premium Member",
        "Activate Membership",
        "Mark Attendance",
        "Display"
    };

    // Form field configuration for member management
    // Format: {Label, Field Type, Enabled (1=yes/0=no), Options, Member Type}
    static private final String[][] MEMBER_FORM_FIELDS = {
        {"ID", "text", "1", "", "regular/premium"},
        {"Full Name", "text", "1", "", "regular/premium"},
        {"Date of Birth", "date", "1", "", "regular/premium"},
        {"Phone Number", "text", "1", "", "regular/premium"},
        {"Email", "text", "1", "", "regular/premium"},
        {"Gender", "radio", "1", "Male,Female", "regular/premium"},
        {"Location", "text", "1", "", "regular/premium"},
        {"Membership Start Date", "date", "1", "", "regular/premium"},
        {"Referral Source", "text", "1", "", "regular/premium"},
        {"Paid Amount", "text", "1", "", "regular/premium"},
        {"Removal Reason", "text", "1", "", "regular/premium"},
        {"Trainer's Name", "text", "1", "", "premium/premium"},
        {"Plan", "dropdown", "1", "Basic,Standard,Deluxe", "regular/premium"},
        {"Regular Plan Price", "text", "1", "", "regular/premium"},
        {"Premium Plan Price", "text", "0", "", "regular/premium"},
        {"Discount Amount", "text", "0", "", "regular/premium"}
    };

    // Tracks which panel is currently active
    static private int currentPanel = 0;

    // Collection of all gym members
    static ArrayList<GymMember> members = new ArrayList<>();

    /**
     * Creates the main application window with appropriate size and settings.
     */
    static void createFrame() {
        mainFrame = new JFrame("Gym Application");
        mainFrame.setSize(1200, WINDOW_SIZE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
    }

    /**
     * Adds the navigation menu to the left side of the application. This
     * includes the logo and navigation buttons for different functions.
     */
    static void addMenu() {
        // Create main menu panel
        panelMenu = new JPanel();
        panelMenu.setBounds(0, 0, 200, WINDOW_SIZE);
        panelMenu.setBackground(Color.white);
        panelMenu.setLayout(null);
        mainFrame.add(panelMenu);

        // Add logo at the top of menu
        panelLogoContainer = new JPanel();
        panelLogoContainer.setBounds(10, 10, 180, 200);
        panelLogoContainer.setBackground(Color.WHITE);
        ImageIcon logo = new ImageIcon(
                new ImageIcon("Logo.png").getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)
        );
        JLabel logoLabel = new JLabel(logo);

        panelLogoContainer.add(logoLabel);
        panelMenu.add(panelLogoContainer);

        // Add navigation buttons below logo
        panelNavigationContainer = new JPanel();
        panelNavigationContainer.setBounds(10, 210, 180, 400);
        panelNavigationContainer.setLayout(new GridLayout(5, 1, 0, 5));
        panelNavigationContainer.setBackground(Color.WHITE);

        panelMenu.add(panelNavigationContainer);

        // Add each navigation button with its action listener
        for (int i = 0; i < NAVIGATION_BUTTONS.length; i++) {
            final int index = i;
            JButton btn = customButton(NAVIGATION_BUTTONS[i], e -> {
                currentPanel = index;
                switchPanel();
            });
            panelNavigationContainer.add(btn);
        }
    }

    /**
     * Initializes the secondary panel used as a placeholder. This panel is used
     * for content that isn't yet shown.
     */
    static void addPanels() {
        panelSecondary = new JPanel();
        panelSecondary.setBackground(Color.BLUE);
        panelSecondary.setBounds(200, 0, 1000, 800);
        mainFrame.add(panelSecondary);
    }

    /**
     * Creates a form panel for adding members (regular or premium). Dynamically
     * generates form fields based on member type.
     *
     * @param memberType Type of member to create form for ("regular" or
     * "premium")
     */
    static void addPnlMember(String memberType) {
        panelContent = new JPanel();
        panelContent.setBounds(200, 0, 1000, 800);
        panelContent.setBackground(Color.WHITE);
        panelContent.setLayout(null);

        // Layout parameters for the form
        int leftLabelX = 30;
        int leftFieldX = 200;
        int rightLabelX = 450;
        int rightFieldX = 630;
        int labelWidth = 200;
        int fieldWidth = 180;
        int rowHeight = 30;
        int spacing = 20;
        int startY = 50;

        // Store form components for later reference
        ArrayList<Object> formComponents = new ArrayList<>();

        // Create form fields based on the MEMBER_FORM_FIELDS configuration
        for (int i = 0; i < MEMBER_FORM_FIELDS.length; i++) {
            int row = i / 2;
            boolean isLeftSide = (i % 2 == 0);

            int labelX = isLeftSide ? leftLabelX : rightLabelX;
            int fieldX = isLeftSide ? leftFieldX : rightFieldX;
            int posY = startY + (row * (rowHeight + spacing));

            // Create and add field label
            JLabel label = new JLabel(MEMBER_FORM_FIELDS[i][0], JLabel.LEFT);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            label.setBounds(labelX, posY, labelWidth, rowHeight);
            panelContent.add(label);

            // Determine field type and create appropriate component
            String fieldType = MEMBER_FORM_FIELDS[i][1];
            String[] fieldScope = MEMBER_FORM_FIELDS[i][4].split("/");

            // Create different form components based on field type
            switch (fieldType.toLowerCase()) {
                case "text":
                    // Create text field component
                    JTextField textField = new JTextField();
                    textField.setBounds(fieldX, posY, fieldWidth, rowHeight);
                    formComponents.add(textField);

                    // Determine if this field should be enabled for current member type
                    textField.setEnabled(false);
                    label.setForeground(Color.gray);

                    if (fieldScope[0].contains(memberType) || fieldScope[1].contains(memberType)) {
                        textField.setEnabled(true);
                        label.setForeground(Color.BLACK);
                    }

                    if (MEMBER_FORM_FIELDS[i][2] == "1") {
                        textField.setEnabled(true);
                    } else {
                        textField.setEnabled(false);
                    }
                    panelContent.add(textField);
                    break;

                case "date":
                    // Create date picker component
                    JPanel datePicker = createDatePicker(fieldX, posY, fieldWidth, rowHeight);
                    formComponents.add(datePicker);

                    boolean enableDatePanel = false;
                    if (fieldScope[0].contains(memberType) || fieldScope[1].contains(memberType)) {
                        enableDatePanel = true;
                    }

                    // Enable/disable all components in the date panel
                    for (int j = 0; j < datePicker.getComponentCount(); j++) {
                        java.awt.Component component = datePicker.getComponent(j);
                        if (component instanceof JComboBox) {
                            component.setEnabled(enableDatePanel);
                        }
                    }

                    panelContent.add(datePicker);
                    break;

                case "radio":
                    // Create radio button group
                    JPanel radioPanel = new JPanel();
                    radioPanel.setLayout(new GridLayout(1, 2));
                    radioPanel.setBounds(fieldX, posY, fieldWidth, rowHeight);

                    String[] radios = MEMBER_FORM_FIELDS[i][3].split(",");
                    ButtonGroup genderGroup = new ButtonGroup();

                    for (int j = 0; j < radios.length; j++) {
                        JRadioButton rButton = new JRadioButton(radios[j]);
                        genderGroup.add(rButton);
                        radioPanel.add(rButton);
                    }

                    formComponents.add(genderGroup); // Store the button group
                    if (fieldScope[0].contains(memberType) || fieldScope[1].contains(memberType)) {
                        panelContent.add(radioPanel);
                    }
                    break;

                case "dropdown":
                    // Create dropdown/combobox component
                    JComboBox<String> dropdown = new JComboBox<>();
                    String[] dropDowns = MEMBER_FORM_FIELDS[i][3].split(",");

                    for (int j = 0; j < dropDowns.length; j++) {
                        dropdown.addItem(dropDowns[j]);
                    }

                    dropdown.setBounds(fieldX, posY, fieldWidth, rowHeight);
                    formComponents.add(dropdown);

                    // Add special listener for Plan dropdown to update prices
                    if (MEMBER_FORM_FIELDS[i][0].equals("Plan")) {
                        dropdown.addActionListener(e -> {
                            String selectedPlan = (String) dropdown.getSelectedItem();

                            // Create temporary member to get plan prices
                            RegularMember temp = new RegularMember(-1, "", "", "", "", "", "", "", "");

                            // Update Regular Plan Price field
                            for (int j = 0; j < formComponents.size(); j++) {
                                if (j == 13 && formComponents.get(j) instanceof JTextField) {
                                    JTextField priceField = (JTextField) formComponents.get(j);
                                    if (selectedPlan.equalsIgnoreCase("Basic")) {
                                        priceField.setText("" + temp.getPlanPrice("Basic"));
                                    } else if (selectedPlan.equalsIgnoreCase("Standard")) {
                                        priceField.setText("" + temp.getPlanPrice("Standard"));
                                    } else if (selectedPlan.equalsIgnoreCase("Deluxe")) {
                                        priceField.setText("" + temp.getPlanPrice("Deluxe"));
                                    }
                                    break;
                                }
                            }

                            // Update Premium Price field if applicable
                            if (memberType.equals("premium")) {
                                for (int j = 0; j < formComponents.size(); j++) {
                                    if (j == 14 && formComponents.get(j) instanceof JTextField) {
                                        JTextField premiumPriceField = (JTextField) formComponents.get(j);
                                        premiumPriceField.setText("50000");
                                        break;
                                    }
                                }
                            }
                        });
                    }

                    if (fieldScope[0].contains(memberType) || fieldScope[1].contains(memberType)) {
                        panelContent.add(dropdown);
                    }
                    break;

                default:
                    // Default to text field for any unrecognized type
                    JTextField defaultField = new JTextField();
                    defaultField.setBounds(fieldX, posY, fieldWidth, rowHeight);
                    formComponents.add(defaultField);
                    panelContent.add(defaultField);
                    break;
            }
        }

        // Add submit button to create the member
        String submitButtonTitle = memberType == "regular" ? "Add Regular Member" : "Add Premium Member";

        JButton submitButton = customButton(submitButtonTitle, e -> {
            try {
                // Get values from form components
                String id = ((JTextField) formComponents.get(0)).getText();
                String fullName = ((JTextField) formComponents.get(1)).getText();
                String phone = ((JTextField) formComponents.get(3)).getText();
                String email = ((JTextField) formComponents.get(4)).getText();

                // Get selected gender from radio button group
                ButtonGroup genderGroup = (ButtonGroup) formComponents.get(5);
                String gender = "";
                for (java.util.Enumeration<AbstractButton> buttons = genderGroup.getElements();
                        buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        gender = button.getText();
                        break;
                    }
                }

                String location = ((JTextField) formComponents.get(6)).getText();
                String referralSource = ((JTextField) formComponents.get(8)).getText();

                // Get formatted dates from date pickers
                String dob = getDateFromPicker((JPanel) formComponents.get(2));
                String membershipStartDate = getDateFromPicker((JPanel) formComponents.get(7));

                // Validate required fields
                if (fullName.isEmpty() || phone.isEmpty() || gender.isEmpty()
                        || location.isEmpty() || dob.isEmpty() || membershipStartDate.isEmpty()) {
                    showMessage("Validation Error", "Please fill in all required fields including dates", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate new ID if not provided
                int memberId;
                if (id.isEmpty()) {
                    // Find highest ID and increment by 1
                    memberId = 1;
                    for (GymMember member : members) {
                        if (member.getId() >= memberId) {
                            memberId = member.getId() + 1;
                        }
                    }
                } else {
                    try {
                        memberId = Integer.parseInt(id);
                        // Check if ID already exists
                        for (GymMember member : members) {
                            if (member.getId() == memberId) {
                                showMessage("Duplicate ID", "Member ID already exists. Please use a different ID or leave blank for auto-generation.", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        showMessage("Validation Error", "Member ID must be a number", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Create appropriate type of member
                if (memberType == "regular") {
                    RegularMember newMember = new RegularMember(
                            memberId,
                            fullName,
                            location,
                            phone,
                            email,
                            gender,
                            dob,
                            membershipStartDate,
                            referralSource
                    );
                    members.add(newMember);
                } else {
                    PremiumMember newMember = new PremiumMember(
                            memberId,
                            fullName,
                            location,
                            phone,
                            email,
                            gender,
                            dob,
                            membershipStartDate,
                            referralSource
                    );
                    members.add(newMember);
                }

                showMessage("Success", "New " + memberType + " member added successfully!\nMember ID: " + memberId, JOptionPane.INFORMATION_MESSAGE);

                // Clear form after successful submission
                for (Object component : formComponents) {
                    if (component instanceof JTextField) {
                        ((JTextField) component).setText("");
                    } else if (component instanceof ButtonGroup) {
                        ((ButtonGroup) component).clearSelection();
                    } else if (component instanceof JComboBox) {
                        ((JComboBox<?>) component).setSelectedIndex(0);
                    } else if (component instanceof JPanel && ((JPanel) component).getComponentCount() >= 3) {
                        for (int i = 0; i < ((JPanel) component).getComponentCount(); i++) {
                            if (((JPanel) component).getComponent(i) instanceof JComboBox) {
                                ((JComboBox<?>) ((JPanel) component).getComponent(i)).setSelectedIndex(0);
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                showMessage("Error", "Error adding member: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        submitButton.setBounds(leftLabelX, 3 * startY + (MEMBER_FORM_FIELDS.length * (1 * spacing)), 120 * 2, 2 * rowHeight);
        panelContent.add(submitButton);
        mainFrame.add(panelContent);
    }

    /**
     * Creates a styled button with consistent appearance and hover effects.
     *
     * @param title Button text to display
     * @param listener ActionListener to handle button clicks
     * @return A customized JButton
     */
    static JButton customButton(String title, ActionListener listener) {
        JButton button = new JButton(title);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(117, 37, 255));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);

        // Add hover effects for better user experience
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(85, 0, 231));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(117, 37, 255));
            }
        });

        button.addActionListener(listener);

        return button;
    }

    /**
     * Switches between different panels based on user navigation selection.
     * Hides all panels and shows only the currently selected one.
     */
    static void switchPanel() {
        if (panelContent != null) {
            mainFrame.remove(panelContent);
        }

        // Hide all panels first for clean switching
        if (panelSecondary != null) {
            panelSecondary.setVisible(false);
        }
        if (panelViewMembers != null) {
            panelViewMembers.setVisible(false);
        }
        if (panelActivateMembership != null) {
            panelActivateMembership.setVisible(false);
        }
        if (panelMarkAttendance != null) {
            panelMarkAttendance.setVisible(false);
        }

        // Show appropriate panel based on selection
        switch (currentPanel) {
            case 0: // Add Regular Member
                addPnlMember("regular");
                panelContent.setVisible(true);
                break;
            case 1: // Add Premium Member
                addPnlMember("premium");
                panelContent.setVisible(true);
                break;
            case 2: // Activate Membership
                panelActivateMembership.setVisible(true);
                break;
            case 3: // Mark Attendance
                panelMarkAttendance.setVisible(true);
                break;
            case 4: // View Members
                panelViewMembers.setVisible(true);
                break;
        }

        // Update UI to reflect changes
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Creates the panel for viewing all members in a table format. This
     * provides an overview of all registered members and their details.
     */
    static void addPnlViewMembers() {
        panelViewMembers = new JPanel();
        panelViewMembers.setBounds(200, 0, 1000, 800);
        panelViewMembers.setBackground(Color.WHITE);
        panelViewMembers.setLayout(null);

        JLabel titleLabel = new JLabel("Member Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 500, 40);
        panelViewMembers.add(titleLabel);

        // Define table columns for member display
        final String[] columnNames = {
            "ID", "Name", "Gender", "Phone", "Email",
            "Location", "DOB", "Start Date", "Type", "Plan/Trainer", "Active Status"
        };

        // Populate table data from members list
        String[][] data = new String[members.size()][columnNames.length];

        for (int i = 0; i < members.size(); i++) {
            GymMember member = members.get(i);
            data[i][0] = member.getId() + "";
            data[i][1] = member.getName();
            data[i][2] = member.getGender();
            data[i][3] = member.getPhone();
            data[i][4] = member.getEmail();
            data[i][5] = member.getLocation();
            data[i][6] = member.getDOB();
            data[i][7] = member.getMembershipStartDate();

            // Handle different member types
            if (member instanceof PremiumMember) {
                data[i][8] = "Premium";
                data[i][9] = ((PremiumMember) member).getPersonalTrainer();
            } else if (member instanceof RegularMember) {
                data[i][8] = "Regular";
                data[i][9] = "N/A";
            }

            data[i][10] = member.getActiveStatus() + "";
        }

        // Create and configure table
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Add table to scrollable pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 960, 600);
        panelViewMembers.add(scrollPane);

        mainFrame.add(panelViewMembers);
        panelViewMembers.setVisible(false);
    }

    /**
     * Creates a date picker component with day, month, and year dropdowns.
     *
     * @param x X position of the date picker
     * @param y Y position of the date picker
     * @param width Width of the date picker
     * @param height Height of the date picker
     * @return A panel containing the date picker components
     */
    static JPanel createDatePicker(int x, int y, int width, int height) {
        JPanel datePanel = new JPanel();
        datePanel.setBounds(x, y, width, height);
        datePanel.setLayout(new GridLayout(1, 3, 5, 0));
        datePanel.setBackground(Color.WHITE);

        // Create day dropdown (1-31)
        JComboBox<String> dayCombo = new JComboBox<>();
        dayCombo.addItem("Day");
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(String.format("%02d", i));
        }

        // Create month dropdown (1-12)
        JComboBox<String> monthCombo = new JComboBox<>();
        monthCombo.addItem("Month");
        for (int i = 1; i <= 12; i++) {
            monthCombo.addItem(String.format("%02d", i));
        }

        // Create year dropdown (current year to 100 years ago)
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.addItem("Year");
        int currentYear = java.time.Year.now().getValue();
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearCombo.addItem(i + "");
        }

        // Add components to panel
        datePanel.add(dayCombo);
        datePanel.add(monthCombo);
        datePanel.add(yearCombo);

        return datePanel;
    }

    /**
     * Extracts a date string from the date picker component. Returns a
     * formatted date string (YYYY-MM-DD) or empty string if incomplete.
     *
     * @param datePicker The date picker panel to extract date from
     * @return Formatted date string or empty string
     */
    static String getDateFromPicker(JPanel datePicker) {
        // Verify date picker has all required components
        if (datePicker.getComponentCount() != 3) {
            return "";
        }

        // Get selected values from each dropdown
        JComboBox<?> dayCombo = (JComboBox<?>) datePicker.getComponent(0);
        JComboBox<?> monthCombo = (JComboBox<?>) datePicker.getComponent(1);
        JComboBox<?> yearCombo = (JComboBox<?>) datePicker.getComponent(2);

        // Check if all dropdowns have selections
        if (dayCombo.getSelectedIndex() <= 0 || monthCombo.getSelectedIndex() <= 0
                || yearCombo.getSelectedIndex() <= 0) {
            return "";
        }

        // Format date as YYYY-MM-DD
        String day = (String) dayCombo.getSelectedItem();
        String month = (String) monthCombo.getSelectedItem();
        String year = (String) yearCombo.getSelectedItem();

        return year + "-" + month + "-" + day;
    }

    /**
     * Creates the panel for activating and reverting memberships. This allows
     * staff to manage membership status and premium memberships.
     */
    static void addPnlActivateMembership() {
        panelActivateMembership = new JPanel();
        panelActivateMembership.setBounds(200, 0, 1000, 800);
        panelActivateMembership.setBackground(Color.WHITE);
        panelActivateMembership.setLayout(null);

        JLabel titleLabel = new JLabel("Membership Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 500, 40);
        panelActivateMembership.add(titleLabel);

        // Add ID input field
        JLabel idLabel = new JLabel("Enter Member ID:");
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        idLabel.setBounds(20, 80, 200, 30);
        panelActivateMembership.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(220, 80, 150, 30);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelActivateMembership.add(idField);

        // Add button to activate membership
        JButton activateButton = customButton("Activate Membership", e -> {
            try {
                int memberId = Integer.parseInt(idField.getText().trim());
                boolean memberFound = false;

                for (GymMember member : members) {
                    if (member.getId() == memberId) {
                        memberFound = true;
                        if (member.getActiveStatus()) {
                            showMessage("Already Active",
                                    "Member Id:" + memberId + " (" + member.getName() + ") already has an active membership.",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            member.activateMembership();
                            showMessage("Success",
                                    "Membership activated successfully for Member #" + memberId + " (" + member.getName() + ")",
                                    JOptionPane.INFORMATION_MESSAGE);
                            idField.setText("");
                        }
                        break;
                    }
                }

                if (!memberFound) {
                    showMessage("Member Not Found",
                            "No member found with ID: " + memberId,
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Invalid Input",
                        "Please enter a valid member ID (numbers only)",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        activateButton.setBounds(20, 130, 200, 40);
        panelActivateMembership.add(activateButton);

        // Add new button to calculate discount for premium members
        JButton calculateDiscountButton = customButton("Calculate Discount", e -> {
            try {
                int memberId = Integer.parseInt(idField.getText().trim());
                boolean memberFound = false;

                for (GymMember member : members) {
                    if (member.getId() == memberId) {
                        memberFound = true;

                        if (!(member instanceof PremiumMember)) {
                            showMessage("Not Premium Member",
                                    "Member #" + memberId + " (" + member.getName() + ") is not a Premium member.\n"
                                    + "Discounts are only available for Premium members.",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            PremiumMember premiumMember = (PremiumMember) member;

                            if (!premiumMember.getIsFullPayment()) {
                                showMessage("Payment Incomplete",
                                        "Member #" + memberId + " (" + member.getName() + ") has not completed full payment.\n"
                                        + "Discount is only calculated for fully paid memberships.",
                                        JOptionPane.WARNING_MESSAGE);
                            } else {
                                // Calculate discount and show results
                                premiumMember.calculateDiscount();

                                showMessage("Discount Calculated",
                                        "Discount calculated for Premium Member #" + memberId + " (" + member.getName() + ")\n"
                                        + "Premium Charge: Rs. " + premiumMember.getPremiumCharge() + "\n"
                                        + "Discount Amount: Rs. " + premiumMember.getDiscountAmount() + " (10%)\n"
                                        + "Final Amount: Rs. " + (premiumMember.getPremiumCharge() - premiumMember.getDiscountAmount()),
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        break;
                    }
                }

                if (!memberFound) {
                    showMessage("Member Not Found",
                            "No member found with ID: " + memberId,
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Invalid Input",
                        "Please enter a valid member ID (numbers only)",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        calculateDiscountButton.setBounds(440, 130, 200, 40);
        panelActivateMembership.add(calculateDiscountButton);

        // Add button to revert regular memberships
        JButton revertRegularButton = customButton("Revert Regular Membership", e -> {
            try {
                int memberId = Integer.parseInt(idField.getText().trim());
                boolean memberFound = false;

                for (GymMember member : members) {
                    if (member.getId() == memberId) {
                        memberFound = true;

                        if (!(member instanceof RegularMember)) {
                            showMessage("Not Regular Member",
                                    "Member #" + memberId + " (" + member.getName() + ") is not a Regular member.",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            // Prompt user for removal reason
                            String removalReason = getUserInput(
                                    "Please enter reason for reverting membership:",
                                    "Removal Reason");

                            if (removalReason != null) {
                                RegularMember regularMember = (RegularMember) member;
                                regularMember.revertRegularMember(removalReason);

                                showMessage("Success",
                                        "Regular membership successfully reverted for Member #" + memberId
                                        + " (" + member.getName() + ")\nMembership status is now inactive.",
                                        JOptionPane.INFORMATION_MESSAGE);
                                idField.setText("");
                            }
                        }
                        break;
                    }
                }

                if (!memberFound) {
                    showMessage("Member Not Found",
                            "No member found with ID: " + memberId,
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Invalid Input",
                        "Please enter a valid member ID (numbers only)",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        revertRegularButton.setBounds(20, 180, 200, 40);
        panelActivateMembership.add(revertRegularButton);

        // Add button to revert premium memberships
        JButton revertButton = customButton("Revert Premium Membership", e -> {
            try {
                int memberId = Integer.parseInt(idField.getText().trim());
                boolean memberFound = false;

                for (GymMember member : members) {
                    if (member.getId() == memberId) {
                        memberFound = true;

                        if (!(member instanceof PremiumMember)) {
                            showMessage("Not Premium Member",
                                    "Member #" + memberId + " (" + member.getName() + ") is not a Premium member.",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            PremiumMember premiumMember = (PremiumMember) member;
                            premiumMember.revertPremiumMember();

                            showMessage("Success",
                                    "Premium membership successfully reverted for Member #" + memberId
                                    + " (" + member.getName() + ")\nMembership status is now inactive.",
                                    JOptionPane.INFORMATION_MESSAGE);
                            idField.setText("");
                        }
                        break;
                    }
                }

                if (!memberFound) {
                    showMessage("Member Not Found",
                            "No member found with ID: " + memberId,
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Invalid Input",
                        "Please enter a valid member ID (numbers only)",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        revertButton.setBounds(230, 180, 200, 40);
        panelActivateMembership.add(revertButton);

        // Create table to show member status
        final String[] columnNames = {
            "ID", "Name", "Membership Type", "Status"
        };

        String[][] data = new String[members.size()][columnNames.length];

        for (int i = 0; i < members.size(); i++) {
            GymMember member = members.get(i);
            data[i][0] = member.getId() + "";
            data[i][1] = member.getName();
            data[i][2] = (member instanceof PremiumMember) ? "Premium" : "Regular";
            data[i][3] = member.getActiveStatus() ? "Active" : "Inactive";
        }

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 240, 800, 400);

        // Add refresh button to update table after changes
        JButton refreshButton = customButton("Refresh Member List", e -> {
            panelActivateMembership.remove(scrollPane);
            updateActivationTable(scrollPane, table);
            panelActivateMembership.add(scrollPane);
            panelActivateMembership.revalidate();
            panelActivateMembership.repaint();
        });

        refreshButton.setBounds(230, 130, 200, 40);
        panelActivateMembership.add(refreshButton);
        panelActivateMembership.add(scrollPane);

        mainFrame.add(panelActivateMembership);
        panelActivateMembership.setVisible(false);
    }

    /**
     * Updates the member table in the activate membership panel. Refreshes data
     * to reflect any changes in member status.
     *
     * @param scrollPane The scroll pane containing the table
     * @param table The member table to update
     */
    static void updateActivationTable(JScrollPane scrollPane, JTable table) {
        final String[] columnNames = {
            "ID", "Name", "Membership Type", "Status"
        };

        String[][] data = new String[members.size()][columnNames.length];

        for (int i = 0; i < members.size(); i++) {
            GymMember member = members.get(i);
            data[i][0] = member.getId() + "";
            data[i][1] = member.getName();
            data[i][2] = (member instanceof PremiumMember) ? "Premium" : "Regular";
            data[i][3] = member.getActiveStatus() ? "Active" : "Inactive";
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    /**
     * Creates the panel for marking member attendance. Allows staff to record
     * gym visits and track attendance points.
     */
    static void addPnlMarkAttendance() {
        panelMarkAttendance = new JPanel();
        panelMarkAttendance.setBounds(200, 0, 1000, 800);
        panelMarkAttendance.setBackground(Color.WHITE);
        panelMarkAttendance.setLayout(null);

        JLabel titleLabel = new JLabel("Mark Attendance");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 500, 40);
        panelMarkAttendance.add(titleLabel);

        // Add ID input field
        JLabel idLabel = new JLabel("Enter Member ID:");
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        idLabel.setBounds(20, 80, 200, 30);
        panelMarkAttendance.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(220, 80, 150, 30);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelMarkAttendance.add(idField);

        // Add button to mark attendance
        JButton markButton = customButton("Mark Attendance", e -> {
            try {
                int memberId = Integer.parseInt(idField.getText().trim());
                boolean memberFound = false;

                for (GymMember member : members) {
                    if (member.getId() == memberId) {
                        memberFound = true;
                        if (!member.getActiveStatus()) {
                            showMessage("Inactive Membership",
                                    "Member #" + memberId + " (" + member.getName() + ") has an inactive membership.\n"
                                    + "Please activate the membership before marking attendance.",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            member.markAttendance();
                            String loyaltyInfo = "";

                            // Check if regular member is eligible for upgrade
                            if (member instanceof RegularMember) {
                                RegularMember regMember = (RegularMember) member;
                                if (regMember.getIsEligibleForUpgrade()) {
                                    loyaltyInfo = "\nThis member is now eligible for a plan upgrade!";
                                }
                            }

                            showMessage("Success",
                                    "Attendance marked successfully for Member #" + memberId + " (" + member.getName() + ")\n"
                                    + "Current attendance: " + member.getAttendance() + "\n"
                                    + "Loyalty points: " + member.getLoyaltyPoints() + loyaltyInfo,
                                    JOptionPane.INFORMATION_MESSAGE);
                            idField.setText("");
                        }
                        break;
                    }
                }

                if (!memberFound) {
                    showMessage("Member Not Found", "No member found with ID: " + memberId, JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Invalid Input", "Please enter a valid member ID (numbers only)", JOptionPane.ERROR_MESSAGE);
            }
        });

        markButton.setBounds(20, 130, 200, 40);
        panelMarkAttendance.add(markButton);

        // Create table to show attendance and loyalty information
        final String[] columnNames = {
            "ID", "Name", "Membership Type", "Status", "Attendance Count", "Loyalty Points"
        };

        String[][] data = new String[members.size()][columnNames.length];

        for (int i = 0; i < members.size(); i++) {
            GymMember member = members.get(i);
            data[i][0] = member.getId() + "";
            data[i][1] = member.getName();
            data[i][2] = (member instanceof PremiumMember) ? "Premium" : "Regular";
            data[i][3] = member.getActiveStatus() ? "Active" : "Inactive";
            data[i][4] = member.getAttendance() + "";
            data[i][5] = member.getLoyaltyPoints() + "";
        }

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 200, 800, 400);

        // Add refresh button to update table after marking attendance
        JButton refreshButton = customButton("Refresh Attendance List", e -> {
            panelMarkAttendance.remove(scrollPane);
            updateAttendanceTable(scrollPane, table);
            panelMarkAttendance.add(scrollPane);
            panelMarkAttendance.revalidate();
            panelMarkAttendance.repaint();
        });

        refreshButton.setBounds(230, 130, 200, 40);
        panelMarkAttendance.add(refreshButton);
        panelMarkAttendance.add(scrollPane);

        mainFrame.add(panelMarkAttendance);
        panelMarkAttendance.setVisible(false);
    }

    /**
     * Updates the member table in the mark attendance panel. Refreshes data to
     * reflect changes in attendance and loyalty points.
     *
     * @param scrollPane The scroll pane containing the table
     * @param table The attendance table to update
     */
    static void updateAttendanceTable(JScrollPane scrollPane, JTable table) {
        final String[] columnNames = {
            "ID", "Name", "Membership Type", "Status", "Attendance Count", "Loyalty Points"
        };

        String[][] data = new String[members.size()][columnNames.length];

        for (int i = 0; i < members.size(); i++) {
            GymMember member = members.get(i);
            data[i][0] = member.getId() + "";
            data[i][1] = member.getName();
            data[i][2] = (member instanceof PremiumMember) ? "Premium" : "Regular";
            data[i][3] = member.getActiveStatus() ? "Active" : "Inactive";
            data[i][4] = member.getAttendance() + "";
            data[i][5] = member.getLoyaltyPoints() + "";
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    /**
     * Displays a standardized message dialog to the user. This centralizes
     * dialog creation for consistent appearance and behavior.
     *
     * @param title The title for the dialog window
     * @param message The message to display to the user
     * @param messageType The type of message (JOptionPane.INFORMATION_MESSAGE,
     * ERROR_MESSAGE, etc.)
     */
    static void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(mainFrame, message, title, messageType);
    }

    /**
     * Displays an input dialog to get text input from the user.
     *
     * @param message The message/question to display
     * @param title The title of the input dialog
     * @return The text entered by the user, or null if canceled
     */
    static String getUserInput(String message, String title) {
        return JOptionPane.showInputDialog(mainFrame, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Application entry point. Initializes all components and displays the main
     * window.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        createFrame();
        addMenu();
        addPanels();
        addPnlMember("regular");
        seedDummyData();
        addPnlViewMembers();
        addPnlActivateMembership();
        addPnlMarkAttendance();
        switchPanel();

        mainFrame.setVisible(true);

        new GymGUI();
    }

    /**
     * Seeds the system with sample member data for testing and demonstration
     * purposes. This helps us have some data to work with when the application
     * starts.
     */
    static void seedDummyData() {
        // Add some premium members with personal trainers
        members.add(new PremiumMember(1, "Ram Bahadur Thapa", "Kathmandu", "9841000001",
                "ram.thapa@example.com",
                "Male", "1990-05-07", "2025-02-04", "Bishnu Gurung"));

        // Add some regular members with various plans
        members.add(new RegularMember(2, "Sita Adhikari", "Pokhara", "9842000002", "sita.adhikari@example.com",
                "Female", "1985-10-15", "2023-01-01", "Krishna Pandey"));

        members.add(new PremiumMember(3, "Krishna Sharma", "Chitwan", "9843000003",
                "krishna.sharma@example.com",
                "Male", "1992-03-12", "2024-06-15", "Ram Karki"));

        members.add(new RegularMember(4, "Gita Bhandari", "Biratnagar", "9844000004",
                "gita.bhandari@example.com",
                "Female", "1988-07-22", "2023-09-10", "Sita Magar"));

        members.add(new PremiumMember(5, "Bishal Raut", "Lalitpur", "9845000005", "bishal.raut@example.com",
                "Male",
                "1995-11-30", "2025-01-20", "Rajesh Shrestha"));

        members.add(new RegularMember(6, "Mina Tamang", "Dharan", "9846000006", "mina.tamang@example.com",
                "Female",
                "1991-04-18", "2023-05-25", "Kamal Rai"));
    }
}
