package gui.application.form.other;

import controller.TenantController;
import database.model.Tenant;
import gui.application.dialog.TenantCreateDialog;
import gui.application.dialog.TenantEditDialog;
import gui.application.dialog.TenantViewDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import gui.table.cell.TableActionCellEditor;
import gui.table.cell.TableActionCellRender;
import gui.table.cell.TableActionEvent;

/**
 * * @author XAX
 */
public class FormTenant extends javax.swing.JPanel {

    private JFrame parentFrame;
    private TenantController tenantController;

    public FormTenant() {
    }

    public FormTenant(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        initializeController();
        initializeListeners();
        refreshTenantList();
    }

    private void initializeController() {
        try {
            tenantController = new TenantController();
        } catch (SQLException ex) {
            Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeListeners() {
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TenantCreateDialog createDialog = new TenantCreateDialog(parentFrame, tenantController, FormTenant.this);
                createDialog.setVisible(true);
                refreshTenantList();
            }
        });

        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int tenantId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                    viewTenant(tenantId);
                }
            }
        });

        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int tenantId = (int) tblDisplay.getValueAt(row, 0); // Assuming ID is in the first column
                Tenant tenantToEdit = null;
                try {
                    tenantToEdit = tenantController.getTenantById(tenantId);
                } catch (SQLException ex) {
                    Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (tenantToEdit != null) {
                    TenantEditDialog editDialog = new TenantEditDialog(parentFrame, tenantController, FormTenant.this, tenantToEdit);
                    editDialog.setVisible(true);
                    refreshTenantList();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Tenant not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onDelete(int row) {
                if (tblDisplay.isEditing()) {
                    tblDisplay.getCellEditor().stopCellEditing();
                }
                try {
                    int tenantId = (int) tblDisplay.getValueAt(row, 0); // Assuming tenantId is in the first column
                    boolean isDeleted = tenantController.deleteTenantById(tenantId);
                    if (isDeleted) {
                        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
                        model.removeRow(row);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Unable to delete tenant.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(parentFrame, "Error deleting tenant: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ClassCastException ex) {
                    Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(parentFrame, "Invalid tenant ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        refreshTable(event);
    }

    private void viewTenant(int tenantId) {
        Tenant tenantToView = null;
        try {
            tenantToView = tenantController.getTenantById(tenantId);
        } catch (SQLException ex) {
            Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tenantToView != null) {
            TenantViewDialog viewDialog = new TenantViewDialog(parentFrame, FormTenant.this, tenantToView);
            viewDialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(parentFrame, "Tenant not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTenantList() {
        List<Tenant> tenants = null;
        try {
            tenants = tenantController.getAllTenants();
        } catch (SQLException ex) {
            Logger.getLogger(FormTenant.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0);
        for (Tenant tenant : tenants) {
            model.addRow(new Object[]{
                tenant.getId(),
                tenant.getName(),
                tenant.getPhone(),});
        }
    }

    private void refreshTable(TableActionEvent event) {
        refreshTenantList();
        tblDisplay.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        tblDisplay.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        tblDisplay.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplay = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();

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
                "ID", "Name", "Phone", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay.setRowHeight(44);
        jScrollPane1.setViewportView(tblDisplay);

        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/table/cell/add.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                        .addGap(33, 33, 33))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDisplay;
    // End of variables declaration//GEN-END:variables
}
