package gui.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import gui.application.Application;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Locale;

/**
 *
 * @author XAX
 */
public class FormSetting extends javax.swing.JPanel {

    public FormSetting() {
        initComponents();
//        lbLanguage.putClientProperty(FlatClientProperties.STYLE, ""
//                + "font:$h1.font");
        Properties props = loadSettingsPropties();
        String currentLanguage = props.getProperty("language") != null ? props.getProperty("language") : "English";
        cbLanguage.setSelectedItem(currentLanguage);
        lbLanguage.setText(Application.getString("language") + " :");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbLanguage = new javax.swing.JLabel();
        cbLanguage = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();

        lbLanguage.setText("Language:");

        cbLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Khmer", "English" }));

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 570, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbLanguage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
    }// </editor-fold>//GEN-END:initComponents

    public static Properties loadSettingsPropties() {
        // Load properties
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("src/gui/theme/settings.properties")) {
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception (e.g., file not found)
        }
        return props;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:

        Properties props = loadSettingsPropties();

        props.setProperty("language", cbLanguage.getSelectedItem().toString());
        try (FileOutputStream out = new FileOutputStream("src/gui/theme/settings.properties")) {
            props.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cbLanguage.getSelectedItem().equals("English")) {
            Application.updateResourceBundle(new Locale("eng", "US"));

        } else if (cbLanguage.getSelectedItem().equals("Khmer")) {
            Application.updateResourceBundle(new Locale("kh", "KH"));
        }

        Application.restart();
    }//GEN-LAST:event_btnSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbLanguage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbLanguage;
    // End of variables declaration//GEN-END:variables
}
