package gui.application.form.other;

import database.model.User;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import services.AuthService;
import utils.ImageSelector;

/**
 *
 * @author XAX
 */
public class FormProfile extends javax.swing.JPanel {

    public FormProfile() {
        initComponents();

        User currentUser = AuthService.getAuthenticatedUser();
        txtfName.setText("Name: " + currentUser.getName());
        txtfRole.setText("Role: " + currentUser.getRole());
        txtfUsername.setText("@Username: " + currentUser.getUsername());
        txtfPhone.setText("Phone: " + currentUser.getPhone());

        btnSelectProfilePhoto.setText(null);
        String profilePicturePath = currentUser.getProfile_picture() != null ? "resources/images/users/" + currentUser.getProfile_picture() : null;
        if (profilePicturePath != null && new File(profilePicturePath).exists()) {
            setButtonIcon(btnSelectProfilePhoto, profilePicturePath);
            btnSelectProfilePhoto.setText(null);
        }

    }

    private void setButtonIcon(JButton button, String imagePath) {
        // Set fixed dimensions for scaling
        int buttonWidth = 122; // Adjust to your desired width
        int buttonHeight = 122; // Adjust to your desired height

        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        int originalWidth = image.getWidth(null);
        int originalHeight = image.getHeight(null);
        double scaleFactor = Math.min((double) buttonWidth / originalWidth, (double) buttonHeight / originalHeight);
        int scaledWidth = (int) (originalWidth * scaleFactor);
        int scaledHeight = (int) (originalHeight * scaleFactor);
        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        // Ensuring the button maintains its fixed size
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtfName = new javax.swing.JTextField();
        txtfUsername = new javax.swing.JTextField();
        txtfPhone = new javax.swing.JTextField();
        txtfRole = new javax.swing.JTextField();
        btnSelectProfilePhoto = new javax.swing.JButton();

        txtfName.setEditable(false);
        txtfName.setText("Name");

        txtfUsername.setEditable(false);
        txtfUsername.setText("@Usename");

        txtfPhone.setEditable(false);
        txtfPhone.setText("Phone");

        txtfRole.setEditable(false);
        txtfRole.setText("Role");

        btnSelectProfilePhoto.setText("Profile Picture");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btnSelectProfilePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtfUsername)
                    .addComponent(txtfName)
                    .addComponent(txtfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtfRole))
                .addContainerGap(438, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtfRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnSelectProfilePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                .addContainerGap(305, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelectProfilePhoto;
    private javax.swing.JTextField txtfName;
    private javax.swing.JTextField txtfPhone;
    private javax.swing.JTextField txtfRole;
    private javax.swing.JTextField txtfUsername;
    // End of variables declaration//GEN-END:variables
}
