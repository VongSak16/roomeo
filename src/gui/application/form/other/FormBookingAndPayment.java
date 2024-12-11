package gui.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import controller.BookingAndPaymentController;
import database.model.*;
import gui.application.dialog.RentalViewDialog;
import gui.application.dialog.RoomViewDialog;
import gui.application.dialog.select.SelectTenantDialog;
import raven.datetime.component.date.*;
import utils.ImageSelector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import services.AuthService;

public class FormBookingAndPayment extends javax.swing.JPanel {

    private static final Logger LOGGER = Logger.getLogger(FormBookingAndPayment.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String IMAGES_PATH = "resources/images/tenants/";

    private final BookingAndPaymentController controller;
    private final List<Rental> cachedRentals;
    private final Timer searchTimer;

    private Rental selectedRental;

    private Tenant tenant;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private int amountMonth = 1;
    private double fee;
    private double price;
    private double cost;
    private boolean isPayNow;
    private double total_price;
    private double total_fee;

    private JFrame parentFrame;
    private DatePicker datePicker = new DatePicker();

    private List<BookingItems> bookingItems;

    public FormBookingAndPayment(JFrame parentFrame) {
        initComponents();
        this.controller = initController();
        this.cachedRentals = initCachedRentals();
        this.searchTimer = new Timer(300, e -> realtimeSearch());
        this.parentFrame = parentFrame;
        searchTimer.setRepeats(false);

        this.bookingItems = new ArrayList<>();

        setupUIComponents();
        setupEventListeners(parentFrame);
        initDatePicker();
        refreshTable();
    }

    private BookingAndPaymentController initController() {
        try {
            return new BookingAndPaymentController();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to initialize controller", ex);
            throw new RuntimeException("Failed to initialize controller", ex);
        }
    }

    private List<Rental> initCachedRentals() {
        try {
            return controller.getAllRentals();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch rentals", ex);
            throw new RuntimeException("Failed to fetch rentals", ex);
        }
    }

    private void setupUIComponents() {
        // Setup button styles
        btnCancel.putClientProperty(FlatClientProperties.STYLE, "background: $App.accent.red");
        btnPreview.putClientProperty(FlatClientProperties.STYLE, "background: $App.accent.purple");
        btnConfirm.putClientProperty(FlatClientProperties.STYLE, "background: $App.accent.blue");

        // Setup table renderer
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblDisplay.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    }

    private void setupEventListeners(JFrame parentFrame) {
        setupTenantCheckboxListener(parentFrame);
        setupTableClickListener();
        setupSpinnerListener();
        setupSearchListeners();
        setupBtnBookListeners();
        setupBtnPriviewListeners();
        setupTableDisplayDoubleClickListener();
    }

    private void setupTenantCheckboxListener(JFrame parentFrame) {
        chbOldTenant.addActionListener(e -> {
            if (chbOldTenant.isSelected()) {
                SelectTenantDialog dialog = new SelectTenantDialog(parentFrame);
                dialog.setVisible(true);
                tenant = dialog.getSelectedTenant();
            } else {
                tenant = null;
            }
            updateTenantInfo(tenant);
        });
    }

    private void setupTableDisplayDoubleClickListener() {
        // Table click listener
        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int rentalId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column

                    Room roomToView = null;
                    try {
                        roomToView = (Room) controller.getRentalByID(rentalId);
                    } catch (SQLException ex) {
                        Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (roomToView != null) {
                        RentalViewDialog viewDialog = new RentalViewDialog(parentFrame, roomToView);
                        viewDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private void setupTableClickListener() {
        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTableClick(e);
                updateCheckoutDate();
            }
        });
    }

    private void handleTableClick(MouseEvent e) {
        int row = tblDisplay.rowAtPoint(e.getPoint());
        if (row >= 0) {
            int id = (int) tblDisplay.getValueAt(row, 0);
            updateSelectedRentalInfo(id);
        } else {
            clearRentalInfo();
        }
    }

    private void updateSelectedRentalInfo(int id) {
        try {
            selectedRental = controller.getRentalByID(id);
            if (selectedRental != null) {
                updateRentalPricing(selectedRental);

                // Display additional info based on Room or House
                if (selectedRental instanceof Room) {
                    Room room = (Room) selectedRental;
                    // Example: Show room number
                    txtfCost.setText(" $ " + cost);
                } else if (selectedRental instanceof House) {
                    House house = (House) selectedRental;
                    // Example: Show house name
                    txtfCost.setText(" $ " + cost);
                }
            } else {
                txtfCost.setText("Rental not found.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error fetching rental", ex);
            txtfCost.setText("Error fetching rental information.");
        }
    }

    private void updateRentalPricing(Rental rental) {
        fee = rental instanceof Room ? ((Room) rental).getFee() : ((House) rental).getFee();
        price = rental instanceof Room ? ((Room) rental).getPrice() : ((House) rental).getPrice();
        updateCost();
    }

    private void clearRentalInfo() {
        fee = 0;
        price = 0;
        cost = 0;
        txtfCost.setText("");
    }

    private void setupSpinnerListener() {
        spnAmountMonth.addChangeListener(e -> {
            amountMonth = (Integer) spnAmountMonth.getValue();
            updateCost();
            updateCheckoutDate();
        });
    }

    private void setupBtnBookListeners() {
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBtnBook();
                clearSearchFields();
            }
        });
    }

    private void updateBtnBook() {
        if (selectedRental != null) { // Check if a rental is selected
            BookingItems bi;
            if (selectedRental instanceof Room) {
                bi = new BookingItems(0, null, checkin, checkout, LocalDateTime.now(),
                        null, (Room) selectedRental, null, fee * amountMonth, price * amountMonth);
            } else {
                bi = new BookingItems(0, null, checkin, checkout, LocalDateTime.now(),
                        null, null, (House) selectedRental, fee * amountMonth, price * amountMonth);
            }

            if (chbPayNow.isSelected()) {
                bi.setStatus("Paid");
            } else {
                bi.setStatus("Unpaid");
            }

            bookingItems.add(bi);
            refreshTable2();

            // Optionally remove the selectedRental from tblDisplay after booking
            DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
            int selectedRow = tblDisplay.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            }
        } else {
            // Handle case where no rental is selected (e.g., show a message)
            JOptionPane.showMessageDialog(this, "Please select a rental to book.",
                    "No Rental Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateCost() {
        cost = amountMonth * (fee + price);
        txtfCost.setText("$ " + cost);
    }

    private void updateCheckoutDate() {
        if (checkin == null) {
            checkin = LocalDateTime.now();
        }
        checkout = calculateCheckout(checkin, amountMonth);
        lblCheckOut.setText(checkout.format(DATE_FORMATTER));
    }

    private void initDatePicker() {
        configureDatePicker(datePicker);
        setupDatePickerListener(datePicker);
    }

    private void configureDatePicker(DatePicker datePicker) {
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setDateSelectionAble(localDate -> !localDate.isBefore(LocalDate.now()));
        datePicker.setEditor(txtfDatePicker);

        datePicker.setSelectedDate(LocalDate.now());
        add(txtfDatePicker, "width 250");
    }

    private void setupDatePickerListener(DatePicker datePicker) {
        datePicker.addDateSelectionListener(dateEvent -> {
            if (datePicker.isDateSelected()) {
                LocalDate selectedDate = datePicker.getSelectedDate();
                checkin = selectedDate.atTime(LocalTime.now());
                updateCheckoutDate();
            }
        });
    }

    private LocalDateTime calculateCheckout(LocalDateTime checkin, int amountMonth) {
        return checkin != null ? checkin.plusMonths(amountMonth) : null;
    }

    private void updateTenantInfo(Tenant tenant) {
        if (tenant != null) {
            setTenantDetails();
            updateTenantPhotos();
        } else {
            clearTenantInfo();
        }
    }

    private void setTenantDetails() {
        txtfTenantName.setText(tenant.getName());
        txtfTenantPhone.setText(tenant.getPhone());
    }

    private void updateTenantPhotos() {
        ImageSelector imageSelector = new ImageSelector();
        updateTenantPhoto(imageSelector, tenant.getId_front_photo(), btnSelectTenantFrontID, "Front ID");
        updateTenantPhoto(imageSelector, tenant.getId_back_photo(), btnSelectTenantBackID, "Back ID");
    }

    private void updateTenantPhoto(ImageSelector imageSelector, String photoId, JButton button, String defaultText) {
        String photoPath = IMAGES_PATH + photoId;
        if (new File(photoPath).exists()) {
            imageSelector.setButtonIcon(button, photoPath);
            button.setText(null);
        } else {
            button.setText(defaultText);
            button.setIcon(null);
        }
    }

    private void clearTenantInfo() {
        txtfTenantName.setText(null);
        txtfTenantPhone.setText(null);
        btnSelectTenantBackID.setText("Back ID");
        btnSelectTenantFrontID.setText("Front ID");
        btnSelectTenantBackID.setIcon(null);
        btnSelectTenantFrontID.setIcon(null);
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0);

        for (Rental rental : cachedRentals) {
            addRentalToModel(rental, model);
        }
    }

    private void refreshTable2() {
        total_price = 0;
        total_fee = 0;
        DefaultTableModel model = (DefaultTableModel) tblDisplay2.getModel();
        model.setRowCount(0);

        for (BookingItems bookingItem : bookingItems) {
            addBookingItemToModel(bookingItem, model);
            total_price += bookingItem.getPrice();
            total_fee += bookingItem.getFee();
        }
    }

    private void setupSearchListeners() {
        DocumentListener documentListener = createDocumentListener();
        addListenersToComponents(documentListener);
    }

    private DocumentListener createDocumentListener() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
        };
    }

    private void addListenersToComponents(DocumentListener listener) {
        txtfType.getDocument().addDocumentListener(listener);
        txtfLocation.getDocument().addDocumentListener(listener);
        spnMinRange.addChangeListener(e -> searchTimer.restart());
        spnMaxRange.addChangeListener(e -> searchTimer.restart());
        spnAmountMonth.addChangeListener(e -> searchTimer.restart());
        chbOnlyRoom.addChangeListener(e -> searchTimer.restart());
    }

    private void realtimeSearch() {
        String type = txtfType.getText().toLowerCase();
        String location = txtfLocation.getText().toLowerCase();
        double minRange = (double) spnMinRange.getValue();
        double maxRange = (double) spnMaxRange.getValue();

        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0);

        cachedRentals.stream()
                .filter(rental -> matchesCriteria(rental, type, location, minRange, maxRange))
                .filter(rental -> rental instanceof Room || !chbOnlyRoom.isSelected())
                .forEach(rental -> addRentalToModel(rental, model));
    }

    private boolean matchesCriteria(Rental rental, String type, String location, double minRange, double maxRange) {
        boolean matchesType = rental instanceof Room
                ? ((Room) rental).getType().toLowerCase().contains(type)
                : ((House) rental).getName().toLowerCase().contains(type);

        boolean matchesLocation = rental instanceof Room
                ? ((Room) rental).getLocation().toLowerCase().contains(location)
                : ((House) rental).getLocation().toLowerCase().contains(location);

        double totalCost = rental.getFee() + rental.getPrice();
        boolean matchesPrice = (minRange == 0 && maxRange == 0)
                || (totalCost >= minRange && totalCost <= maxRange);

        return matchesType && matchesLocation && matchesPrice;
    }

    private void addRentalToModel(Rental rental, DefaultTableModel model) {
        if (rental instanceof Room) {
            Room room = (Room) rental;
            model.addRow(new Object[]{
                room.getId(),
                room.getRoom_number() + " (Room)",
                room.getType(),
                room.getLocation(),
                "$ " + room.getFee(),
                "$ " + room.getPrice()
            });
        } else if (rental instanceof House) {
            House house = (House) rental;
            model.addRow(new Object[]{
                house.getId(),
                house.getName() + " (House)",
                house.getType(),
                house.getLocation(),
                "$ " + house.getFee(),
                "$ " + house.getPrice()
            });
        }
    }

    private void addBookingItemToModel(BookingItems bookingItem, DefaultTableModel model) {
        // Simplified to show only essential booking details
        model.addRow(new Object[]{
            bookingItem.getId(),
            (bookingItem.getRoom() != null)
            ? bookingItem.getRoom().getRoom_number() + " (Room)"
            : bookingItem.getHouse().getName() + " (House)",
            bookingItem.getCheck_in_date().format(DATE_FORMATTER), // Format date
            bookingItem.getCheck_out_date().format(DATE_FORMATTER), // Format date
            bookingItem.getPaid_date().format(DATE_FORMATTER),
            bookingItem.getStatus(),
            "$" + bookingItem.getFee(),
            "$" + bookingItem.getPrice(),});
    }

    private void clearSearchFields() {
        // Clear UI search fields 
        txtfType.setText("");
        txtfLocation.setText("");
        spnMinRange.setValue(0.0);
        spnMaxRange.setValue(0.0);
        datePicker.setSelectedDate(LocalDate.now());
        //txtfDatePicker.setText(""); // Assuming this is for check-in date
        spnAmountMonth.setValue(1);
        lblCheckOut.setText("");
        txtfCost.setText("");
        chbPayNow.setSelected(false);

        // Clear/Reset relevant private variables 
        checkin = null;
        checkout = null;
        amountMonth = 1;
        fee = 0.0;
        price = 0.0;
        cost = 0.0;
        selectedRental = null; // Important to reset this!
        // ... reset any other variables affected by the search
    }

    private void setupBtnPriviewListeners() {
        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonPriview();
            }
        });
    }

    private Booking booking;

    private void ButtonPriview() {
        if (booking == null) {
            booking = new Booking(0, AuthService.getAuthenticatedUser(), tenant, LocalDateTime.now(), "Partiel Paid", total_fee, total_price);
        }
        try {
            // Specify the path to the JRXML file
            String report = "src\\utils\\InvoiceReport_1_1.jrxml";

            // Compile the JRXML report
            JasperReport jasperReport = JasperCompileManager.compileReport(report);
            System.out.println("Report compiled successfully!");

            // Create a data source from the bookingItems2 list
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookingItems);

            // Parameters for the report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Date", new java.text.SimpleDateFormat("dd/MM/yy").format(
                    java.util.Date.from(booking.getBooking_date()
                            .atZone(java.time.ZoneId.systemDefault()).toInstant()
                    )
            ).toString());
            parameters.put("Id", booking.getId() + "");

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("Report filled successfully!");

            // Show the report in a Swing window
            SwingUtilities.invokeLater(() -> JasperViewer.viewReport(jasperPrint, false));
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error on report: " + e.getMessage());
        }

    }

    private void ButtonConfirm() {
        if (bookingItems != null && !bookingItems.isEmpty()) {
            // Gather the attributes for the booking
            booking = new Booking(0, AuthService.getAuthenticatedUser(), tenant, LocalDateTime.now(), "Partiel Paid", total_fee, total_price);
            try {
                // Call the method to add the booking and get the generated booking ID
                int bookingId = controller.addBooking(booking);

                booking.setId(bookingId);

                controller.addBookingItems(booking, bookingItems);

                ButtonPriview();
                booking = null;

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding booking: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No booking items selected.");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSelectTenantBackID = new javax.swing.JButton();
        btnSelectTenantFrontID = new javax.swing.JButton();
        chbOldTenant = new javax.swing.JCheckBox();
        txtfTenantPhone = new javax.swing.JTextField();
        txtfTenantName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtfType = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtfLocation = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        btnBook = new javax.swing.JButton();
        chbOnlyRoom = new javax.swing.JCheckBox();
        txtfDatePicker = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        chbOnlyRoom1 = new javax.swing.JCheckBox();
        spnAmountMonth = new javax.swing.JSpinner();
        lblCheckOut1 = new javax.swing.JLabel();
        spnMaxRange = new javax.swing.JSpinner();
        spnMinRange = new javax.swing.JSpinner();
        lblCheckOut2 = new javax.swing.JLabel();
        lblCheckOut = new javax.swing.JTextField();
        txtfCost = new javax.swing.JTextField();
        chbPayNow = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplay = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDisplay2 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnConfirm = new javax.swing.JButton();
        btnPreview = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        lb.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lb.setText("Booking and Payment");

        btnSelectTenantBackID.setText("Back ID ");

        btnSelectTenantFrontID.setText("Front ID ");

        chbOldTenant.setText("Old Tenant?");

        jLabel3.setText("Phone");

        jLabel1.setText("Tenant Name");

        jLabel5.setText("Cost Range");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("To");

        txtfType.setColumns(20);
        txtfType.setLineWrap(true);
        txtfType.setRows(5);
        txtfType.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtfType);

        jLabel7.setText("Type");

        jLabel8.setText("Location");

        txtfLocation.setColumns(20);
        txtfLocation.setLineWrap(true);
        txtfLocation.setRows(5);
        txtfLocation.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtfLocation);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Search Available Rentals");

        btnBook.setText("Book");

        chbOnlyRoom.setText("Only Room?");

        txtfDatePicker.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        jLabel12.setText("Check In");

        jLabel13.setText("Amount Month");

        chbOnlyRoom1.setText("Agree to Terms and Conditions");

        spnAmountMonth.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        lblCheckOut1.setText("Check Out Date");

        spnMaxRange.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));

        spnMinRange.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));

        lblCheckOut2.setText("Cost");

        lblCheckOut.setEditable(false);

        txtfCost.setEditable(false);

        chbPayNow.setText("Pay Now");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBook, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chbOnlyRoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtfTenantName)
                            .addComponent(btnSelectTenantBackID, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(btnSelectTenantFrontID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chbOldTenant, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtfTenantPhone, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(chbOnlyRoom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCheckOut2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCheckOut1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spnAmountMonth)
                                    .addComponent(txtfDatePicker)
                                    .addComponent(jScrollPane3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(spnMinRange, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(spnMaxRange, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2)
                                    .addComponent(lblCheckOut)))
                            .addComponent(txtfCost, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(chbPayNow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {spnMaxRange, spnMinRange});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtfTenantName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfTenantPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbOldTenant, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectTenantFrontID, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectTenantBackID, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbOnlyRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(spnMaxRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnMinRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(spnAmountMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCheckOut1)
                    .addComponent(lblCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCheckOut2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtfCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbPayNow, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(chbOnlyRoom1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBook)
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel12, jLabel13, jLabel5, jLabel7, jLabel8, lblCheckOut1, lblCheckOut2, spnAmountMonth, txtfDatePicker});

        tblDisplay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Rental", "Type", "Location", "Fee", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay.setRowHeight(44);
        jScrollPane1.setViewportView(tblDisplay);
        if (tblDisplay.getColumnModel().getColumnCount() > 0) {
            tblDisplay.getColumnModel().getColumn(0).setMinWidth(10);
            tblDisplay.getColumnModel().getColumn(0).setMaxWidth(100);
            tblDisplay.getColumnModel().getColumn(1).setMinWidth(200);
            tblDisplay.getColumnModel().getColumn(1).setMaxWidth(400);
            tblDisplay.getColumnModel().getColumn(4).setMinWidth(10);
            tblDisplay.getColumnModel().getColumn(4).setMaxWidth(220);
            tblDisplay.getColumnModel().getColumn(5).setMinWidth(10);
            tblDisplay.getColumnModel().getColumn(5).setMaxWidth(220);
        }

        tblDisplay2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Rental", "Check n", "CheckOut", "Status", "Fee", "Price", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay2.setRowHeight(44);
        jScrollPane4.setViewportView(tblDisplay2);

        jLabel10.setText("Booking Rentals");

        jLabel11.setText("Available Rentals");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(406, 406, 406))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
                                        .addGap(2, 2, 2))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );

        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        btnPreview.setText("Preview");

        btnCancel.setText("Cancel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        ButtonConfirm();
    }//GEN-LAST:event_btnConfirmActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnSelectTenantBackID;
    private javax.swing.JButton btnSelectTenantFrontID;
    private javax.swing.JCheckBox chbOldTenant;
    private javax.swing.JCheckBox chbOnlyRoom;
    private javax.swing.JCheckBox chbOnlyRoom1;
    private javax.swing.JCheckBox chbPayNow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField lblCheckOut;
    private javax.swing.JLabel lblCheckOut1;
    private javax.swing.JLabel lblCheckOut2;
    private javax.swing.JSpinner spnAmountMonth;
    private javax.swing.JSpinner spnMaxRange;
    private javax.swing.JSpinner spnMinRange;
    private javax.swing.JTable tblDisplay;
    private javax.swing.JTable tblDisplay2;
    private javax.swing.JTextField txtfCost;
    private javax.swing.JFormattedTextField txtfDatePicker;
    private javax.swing.JTextArea txtfLocation;
    private javax.swing.JTextField txtfTenantName;
    private javax.swing.JTextField txtfTenantPhone;
    private javax.swing.JTextArea txtfType;
    // End of variables declaration//GEN-END:variables
}
