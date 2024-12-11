package gui.application.dialog;

import com.formdev.flatlaf.FlatClientProperties;
import controller.HouseController;
import controller.RoomController;
import database.model.User;
import database.model.Property;
import database.model.Room;
import gui.application.dialog.select.SelectPropertyDialog;
import gui.application.form.other.FormRoom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import utils.ImageSelector;

/**
 * * @author SAK
 */
public class RoomEditDialog extends javax.swing.JDialog {

    private String photoPath;
    private ImageSelector imageSelector = new ImageSelector();
    private FormRoom parentForm;
    private User selectedUser;
    private Property selectedProperty;

    public RoomEditDialog(JFrame parent, RoomController controller, FormRoom parentForm, Room currentRoom) {
        super(parent, true);
        this.parentForm = parentForm;
        setLayout(new MigLayout("al center center"));
        initComponents();
        setLocationRelativeTo(parent);

        // Prefill the form with current room data
        txtfName.setText(currentRoom.getRoom_number());
        txtfLocation.setText(currentRoom.getLocation());
        txtfType.setText(currentRoom.getType());
        cboAvailability.setSelectedItem(currentRoom.getAvailability());
        txtfFee.setText(String.valueOf(currentRoom.getFee()));
        txtfPrice.setText(String.valueOf(currentRoom.getPrice()));

        if (currentRoom.getProperty() != null) {
            btnSelectProperty.setText(currentRoom.getProperty().getName());
            selectedProperty = currentRoom.getProperty();
        }

        photoPath = "resources/images/rooms/" + currentRoom.getPhoto();
        imageSelector.setButtonIcon(btnSelectPhoto, photoPath);

        // Set the existing photo as the button's icon
        if (new File(photoPath).exists()) {
            imageSelector.setButtonIcon(btnSelectPhoto, photoPath);
            btnSelectPhoto.setText(null);
        }

        btnCancel.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.red");
        btnApply.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.blue");

        btnSelectPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSelectPhoto.setText(null);
                imageSelector.selectAndScaleImage(btnSelectPhoto, "House Image");
                photoPath = imageSelector.getSelectedFilePath();
            }
        });

        btnSelectProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectPropertyDialog propertySelectDialog = new SelectPropertyDialog(parent, RoomEditDialog.this);
                propertySelectDialog.setVisible(true);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtfName.getText().trim();
                String location = txtfLocation.getText().trim();
                String type = txtfType.getText().trim();
                String availability = cboAvailability.getSelectedItem().toString();
                double fee = Double.parseDouble(txtfFee.getText().trim());
                double price = Double.parseDouble(txtfPrice.getText().trim());

                try {
                    fee = Double.parseDouble(txtfFee.getText().trim());
                    price = Double.parseDouble(txtfPrice.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid fee or price.");
                    return;
                }

                if (name.isEmpty() || location.isEmpty() || type.isEmpty() || availability.isEmpty() || selectedProperty == null) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.");
                } else {
                    try {
                        // Only check for duplicate room number if it's changed
                        if (!name.equals(currentRoom.getRoom_number()) && controller.room_numberExists(name)) {
                            JOptionPane.showMessageDialog(null, "Room Number already exists.");
                            return;
                        }
                        Map<String, Object> houseAttributes = new HashMap<>();
                        houseAttributes.put("id", currentRoom.getId());
                        houseAttributes.put("room_number", name);
                        houseAttributes.put("location", location);
                        houseAttributes.put("type", type);
                        houseAttributes.put("fee", fee);
                        houseAttributes.put("price", price);
                        houseAttributes.put("availability", availability);
                        houseAttributes.put("property", selectedProperty);
                        houseAttributes.put("photoPath", photoPath);

                        controller.updateRoom(houseAttributes);
                        parentForm.refreshRoomList();
                        dispose();

                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        );
    }

    public void setSelectedProperty(Property property) {
        if (property != null) {
            this.selectedProperty = property;
            btnSelectProperty.setText(property.getName());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtfName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnSelectProperty = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cboAvailability = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtfFee = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnSelectPhoto = new javax.swing.JButton();
        txtfPrice = new javax.swing.JTextField();
        btnApply = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtfType = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtfLocation = new javax.swing.JTextArea();
        btnCancel = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Room Number");

        jLabel2.setText("Avaiilability");

        btnSelectProperty.setText("Select Property");

        jLabel7.setText("Type");

        cboAvailability.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Available", "Maintaince", "Rent" }));

        jLabel5.setText("Property");

        jLabel3.setText("Fee");

        jLabel8.setText("Price");

        jLabel9.setText("Location");

        btnSelectPhoto.setText("Photo Room");

        btnApply.setText("Apply");

        txtfType.setColumns(20);
        txtfType.setRows(5);
        jScrollPane1.setViewportView(txtfType);

        txtfLocation.setColumns(20);
        txtfLocation.setRows(5);
        jScrollPane2.setViewportView(txtfLocation);

        btnCancel.setText("Cancel");

        jLabel6.setText("Room Photo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfName)
                            .addComponent(txtfFee)
                            .addComponent(btnSelectProperty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(cboAvailability, javax.swing.GroupLayout.Alignment.TRAILING, 0, 204, Short.MAX_VALUE)
                            .addComponent(txtfPrice))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSelectPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(189, 189, 189)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel5, jLabel8});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSelectProperty)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtfFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnSelectPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(btnCancel))
                .addGap(27, 27, 27))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSelectProperty, cboAvailability, jLabel1, txtfFee, txtfName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jLabel8});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectPhoto;
    private javax.swing.JButton btnSelectProperty;
    private javax.swing.JComboBox<String> cboAvailability;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtfFee;
    private javax.swing.JTextArea txtfLocation;
    private javax.swing.JTextField txtfName;
    private javax.swing.JTextField txtfPrice;
    private javax.swing.JTextArea txtfType;
    // End of variables declaration//GEN-END:variables
}
