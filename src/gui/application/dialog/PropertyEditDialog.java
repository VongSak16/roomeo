package gui.application.dialog;

import gui.application.dialog.select.SelectUserDialog;
import com.formdev.flatlaf.FlatClientProperties;
import controller.PropertyController;
import database.model.User;
import database.model.Property;
import gui.application.form.other.FormProperty;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import utils.ImageSelector;

/**
 * * @author SAK
 */
public class PropertyEditDialog extends javax.swing.JDialog {

    private String logoPhotoPath;
    private ImageSelector imageSelector = new ImageSelector();
    private FormProperty parentFormProperty;
    private PropertyController propertyController;
    private Property currentProperty;
    private User selectUser;

    public PropertyEditDialog(JFrame parent, PropertyController propertyController, FormProperty parentFormProperty, Property property) {
        super(parent, true);
        this.parentFormProperty = parentFormProperty;
        this.propertyController = propertyController;
        this.currentProperty = property;
        setLayout(new MigLayout("al center center"));
        initComponents();
        setLocationRelativeTo(parent);

        // Prefill the form with current property data
        txtfName.setText(currentProperty.getName());
        txtfInfo.setText(currentProperty.getInfo());
        if (currentProperty.getOwner() != null) {
            btnSelectUser.setText(currentProperty.getOwner().getName());
        }
        logoPhotoPath = "resources/images/properties/" + currentProperty.getLogo();
        imageSelector.setButtonIcon(btnSelectLogo, logoPhotoPath);

        // Set the existing photo as the button's icon
        if (new File(logoPhotoPath).exists()) {
            imageSelector.setButtonIcon(btnSelectLogo, logoPhotoPath);
            btnSelectLogo.setText(null);
        }

        btnCancel.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.red");
        btnApply.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.blue");

        btnSelectLogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSelectLogo.setText(null);
                imageSelector.selectAndScaleImage(btnSelectLogo, "Image");
                logoPhotoPath = imageSelector.getSelectedFilePath();
            }
        });

        btnSelectUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectUserDialog propertySelectUserDialog = new SelectUserDialog(parent, PropertyEditDialog.this);
                propertySelectUserDialog.setVisible(true);
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
                String info = txtfInfo.getText().trim();
                User owner = selectUser != null ? selectUser : (currentProperty.getOwner() != null ? currentProperty.getOwner() : null);

                if (name.isEmpty() || info.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.");
                } else {
                    try {
                        Map<String, Object> propertyAttributes = new HashMap<>();
                        propertyAttributes.put("id", currentProperty.getId());
                        propertyAttributes.put("name", name);
                        propertyAttributes.put("logoPath", logoPhotoPath);
                        propertyAttributes.put("info", info);
                        propertyAttributes.put("owner", owner); // Assuming you set the owner somewhere

                        propertyController.updateProperty(propertyAttributes);
                        // Refresh property list
                        parentFormProperty.refreshPropertyList();
                        dispose();
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void setSelectedUser(User selectUser) {
        if (selectUser != null) {
            this.btnSelectUser.setText(selectUser.getName());

            if (!imageSelector.imageIsExist("resources/images/properties/" + currentProperty.getLogo())) {
                if (btnSelectLogo.getIcon() == null) {
                    System.out.print("yes");
                    logoPhotoPath = "resources/images/users/" + selectUser.getProfile_picture();
                    imageSelector.setButtonIcon(btnSelectLogo, logoPhotoPath);
                    btnSelectUser.setText(null);
                }
            }

        } else {
            this.btnSelectUser.setText("Select User who owns the property");
        }
        this.selectUser = selectUser;
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
        txtfInfo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnSelectLogo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSelectUser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Name");

        jLabel2.setText("Info");

        txtfInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setText("Logo:");

        btnSelectLogo.setText("Logo Photo");

        jLabel4.setText("Owner");

        btnApply.setText("Apply");

        btnCancel.setText("Cancel");

        btnSelectUser.setText("Select Owner");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addComponent(btnSelectLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtfInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(txtfName)
                            .addComponent(btnSelectUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btnSelectUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelectLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, txtfName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, txtfInfo});

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
                .addGap(75, 75, 75)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(372, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectLogo;
    private javax.swing.JButton btnSelectUser;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtfInfo;
    private javax.swing.JTextField txtfName;
    // End of variables declaration//GEN-END:variables
}
