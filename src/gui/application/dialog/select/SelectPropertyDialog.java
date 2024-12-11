/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui.application.dialog.select;

import com.formdev.flatlaf.FlatClientProperties;
import controller.PropertyController;
import database.model.Property;
import gui.application.dialog.HouseCreateDialog;
import gui.application.dialog.HouseEditDialog;
import gui.application.dialog.RoomCreateDialog;
import gui.application.dialog.RoomEditDialog;
import gui.application.dialog.UserCreateDialog;
import gui.application.dialog.UserEditDialog;
import gui.application.form.other.FormProperty;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SAK
 */
public class SelectPropertyDialog extends javax.swing.JDialog {

    private JFrame parent;
    private List<Property> properties;

    public SelectPropertyDialog(JFrame parent, JDialog mySelectDialog) {
        super(parent, true);

        this.parent = parent;
        initComponents();
        setLocationRelativeTo(parent);
        refreshPropertyList();

        btnCancel.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.red");
        btnApply.putClientProperty(FlatClientProperties.STYLE, "" + "background: $App.accent.blue");

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblDisplay.getSelectedRow();

                // Handle the "None" checkbox selection
                if (chbNone.isSelected()) {
                    setSelectedProperty(mySelectDialog, null);
                } else if (selectedRow != -1) {
                    int propertyId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                    Property selectedProperty = properties.stream()
                            .filter(p -> p.getId() == propertyId)
                            .findFirst()
                            .orElse(null);
                    setSelectedProperty(mySelectDialog, selectedProperty);
                }

                dispose(); // Close the dialog
            }
        });
    }

    private void setSelectedProperty(JDialog dialog, Property property) {
        if (dialog instanceof UserCreateDialog) {
            ((UserCreateDialog) dialog).setSelectedProperty(property);
        } else if (dialog instanceof UserEditDialog) {
            ((UserEditDialog) dialog).setSelectedProperty(property);
            //user
        } else if (dialog instanceof HouseCreateDialog) {
            ((HouseCreateDialog) dialog).setSelectedProperty(property);
        } else if (dialog instanceof HouseEditDialog) {
            ((HouseEditDialog) dialog).setSelectedProperty(property);
            //house
        } else if (dialog instanceof RoomCreateDialog) {
            ((RoomCreateDialog) dialog).setSelectedProperty(property);
        } else if (dialog instanceof RoomCreateDialog) {
            ((RoomEditDialog) dialog).setSelectedProperty(property);
        }
    }

    public void refreshPropertyList() {
        properties = null;
        try {
            properties = new PropertyController().getAllProperties();
        } catch (SQLException ex) {
            Logger.getLogger(FormProperty.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0); // Clear the table
        for (Property property : properties) {
            model.addRow(new Object[]{
                property.getId(),
                property.getName(),
                property.getLogo(),
                property.getInfo(),});
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplay = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        chbNone = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblDisplay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Logo", "Info"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay.setRowHeight(44);
        jScrollPane1.setViewportView(tblDisplay);

        btnCancel.setText("Cancel");

        btnApply.setText("Apply");

        jLabel1.setText("Select a Property");

        chbNone.setText("Set To None");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbNone, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnCancel)
                    .addComponent(btnApply)
                    .addComponent(chbNone))
                .addGap(19, 19, 19))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCancel, jLabel1});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JCheckBox chbNone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDisplay;
    // End of variables declaration//GEN-END:variables
}