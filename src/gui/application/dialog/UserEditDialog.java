package gui.application.dialog;

import gui.application.dialog.select.SelectPropertyDialog;
import com.formdev.flatlaf.FlatClientProperties;
import controller.UserController;
import database.model.Property;
import database.model.User;
import gui.application.form.other.FormUser;
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
public class UserEditDialog extends javax.swing.JDialog {

    private String profilePicturePath;
    private String frontPhotoPath;
    private String backPhotoPath;
    private Property selectProperty;
    private ImageSelector imageSelector = new ImageSelector();
    private FormUser parentFormUser;
    private UserController userController;
    private User currentUser;
    private JFrame parent;

    public UserEditDialog(JFrame parent, UserController userController, FormUser parentFormUser, User user) {
        super(parent, true);

        this.parent = parent;
        this.parentFormUser = parentFormUser;
        this.userController = userController;
        this.currentUser = user;
        setLayout(new MigLayout("al center center"));
        initComponents();
        setLocationRelativeTo(parent);

        btnCancel.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.red");
        btnApply.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.blue");

        // Prefill the form with current user data
        txtfName.setText(currentUser.getName());
        txtfUsername.setText(currentUser.getUsername());
        cboRole.setSelectedItem(currentUser.getRole());
        txtfPhone.setText(currentUser.getPhone());
        txtfPassword.setEchoChar('o');
        if (currentUser.getProperty() != null) {
            btnSelectProperty.setText(currentUser.getProperty().getName());
        } else {
            btnSelectProperty.setText("Select Property he/she affiliate");
        }

        profilePicturePath = currentUser.getProfile_picture() != null ? "resources/images/users/" + currentUser.getProfile_picture() : null;
        frontPhotoPath = currentUser.getId_front_photo() != null ? "resources/images/users/" + currentUser.getId_front_photo() : null;
        backPhotoPath = currentUser.getId_back_photo() != null ? "resources/images/users/" + currentUser.getId_back_photo() : null;

        if (profilePicturePath != null && new File(profilePicturePath).exists()) {
            imageSelector.setButtonIcon(btnSelectProfilePhoto, profilePicturePath);
            btnSelectProfilePhoto.setText(null);
        }

        if (frontPhotoPath != null && new File(frontPhotoPath).exists()) {
            imageSelector.setButtonIcon(btnSelectFrontPhoto, frontPhotoPath);
            btnSelectFrontPhoto.setText(null);
        }

        if (backPhotoPath != null && new File(backPhotoPath).exists()) {
            imageSelector.setButtonIcon(btnSelectBackPhoto, backPhotoPath);
            btnSelectBackPhoto.setText(null);
        }

        //Do Operation
        btnSelectProfilePhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSelector.selectAndScaleImage(btnSelectProfilePhoto, "Image");
                profilePicturePath = imageSelector.getSelectedFilePath();
            }
        });

        btnSelectFrontPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSelector.selectAndScaleImage(btnSelectFrontPhoto, "Image");
                frontPhotoPath = imageSelector.getSelectedFilePath();
            }
        });

        btnSelectBackPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageSelector.selectAndScaleImage(btnSelectBackPhoto, "Image");
                backPhotoPath = imageSelector.getSelectedFilePath();
            }
        });

        btnSelectProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectPropertyDialog userSelectPropertyDialog = new SelectPropertyDialog(parent, UserEditDialog.this);
                userSelectPropertyDialog.setVisible(true);
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
                int id = currentUser.getId();
                String name = txtfName.getText().trim();
                String username = txtfUsername.getText().trim();
                String password = txtfPassword.getText().trim();
                String role = cboRole.getSelectedItem().toString();
                String phone = txtfPhone.getText().trim();

                // Handle password logic
                if (txtfPassword.getText().isEmpty()) {
                    password = currentUser.getPassword(); // Keep the existing hashed password
                }
                if (password == null) {
                    password = ""; // Assign a default value if currentUser.getPassword() is null
                }

                // Handle property logic
                Property property = selectProperty != null ? selectProperty : currentUser.getProperty(); // Use selected property or current user's property

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.");
                } else {
                    try {
                        Map<String, Object> userAttributes = new HashMap<>();
                        userAttributes.put("id", id);
                        userAttributes.put("name", name);
                        userAttributes.put("name", name);
                        userAttributes.put("username", username);
                        if (!txtfPassword.getText().isEmpty()) {
                            userAttributes.put("password", password); // Only add new password if user has entered one
                        }
                        userAttributes.put("role", role);
                        userAttributes.put("phone", phone);
                        userAttributes.put("profilePicturePath", profilePicturePath);
                        userAttributes.put("frontPhotoPath", frontPhotoPath);
                        userAttributes.put("backPhotoPath", backPhotoPath);
                        userAttributes.put("property", property);
                        userController.updateUser(userAttributes);
                        // Refresh user list
                        parentFormUser.refreshUserList();
                        dispose();
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

    }

    public void setSelectedProperty(Property selectProperty) {
        if (selectProperty != null) {
            this.btnSelectProperty.setText(selectProperty.getName());
        } else {
            this.btnSelectProperty.setText("Select Property he/she affiliate");
        }
        this.selectProperty = selectProperty;
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtfName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnSelectBackPhoto = new javax.swing.JButton();
        btnSelectFrontPhoto = new javax.swing.JButton();
        txtfUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cboRole = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtfPhone = new javax.swing.JTextField();
        btnSelectProfilePhoto = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        btnSelectProperty = new javax.swing.JButton();
        txtfPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel1.setText("Name");

        jLabel3.setText("Identification's Photos:");

        btnSelectBackPhoto.setText("Back Photos");

        btnSelectFrontPhoto.setText("Front Photo");

        jLabel5.setText("Username");

        jLabel6.setText("Password");

        jLabel7.setText("Role");

        cboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Employee", "Owner" }));

        jLabel8.setText("Phone");

        btnSelectProfilePhoto.setText("Profile Picture");

        jLabel9.setText("Profile Picture");

        jLabel10.setText("Property");

        btnCancel.setText("Cancel");

        btnApply.setText("Apply");

        btnSelectProperty.setText("Select Property he/she affiliate");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtfName)
                                            .addComponent(txtfPassword)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtfPhone)
                                            .addComponent(btnSelectProperty, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtfUsername)
                            .addComponent(cboRole, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSelectProfilePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSelectFrontPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSelectBackPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel6, jLabel8});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(cboRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(btnSelectProperty))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addComponent(btnSelectProfilePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelectFrontPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelectBackPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(btnCancel))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboRole, jLabel1, jLabel10, jLabel3, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, txtfName, txtfPhone, txtfUsername});

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(755, Short.MAX_VALUE)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jScrollPane2)
                    .addGap(23, 23, 23)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(724, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectBackPhoto;
    private javax.swing.JButton btnSelectFrontPhoto;
    private javax.swing.JButton btnSelectProfilePhoto;
    private javax.swing.JButton btnSelectProperty;
    private javax.swing.JComboBox<String> cboRole;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtfName;
    private javax.swing.JPasswordField txtfPassword;
    private javax.swing.JTextField txtfPhone;
    private javax.swing.JTextField txtfUsername;
    // End of variables declaration//GEN-END:variables
}
