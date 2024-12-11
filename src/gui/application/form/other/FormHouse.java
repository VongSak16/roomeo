package gui.application.form.other;

import controller.HouseController;
import database.model.House;
import gui.application.dialog.HouseCreateDialog;
import gui.application.dialog.HouseEditDialog;
import gui.application.dialog.HouseViewDialog;
import gui.table.cell.TableActionCellEditor;
import gui.table.cell.TableActionCellRender;
import gui.table.cell.TableActionEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormHouse extends javax.swing.JPanel {

    private JFrame parentFrame;
    private HouseController houseController;

    public FormHouse() {
    }

    public FormHouse(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        initializeController();
        initializeListeners();
        refreshHouseList();
    }

    private void initializeController() {
        try {
            houseController = new HouseController();
        } catch (SQLException ex) {
            Logger.getLogger(FormHouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeListeners() {
        // Add button listener
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HouseCreateDialog createDialog = new HouseCreateDialog(parentFrame, houseController, FormHouse.this);
                createDialog.setVisible(true);
                refreshHouseList();
            }
        });

        // Table click listener
        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int houseId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column

                    House houseToView = null;
                    try {
                        houseToView = houseController.getHouseById(houseId);
                    } catch (SQLException ex) {
                        Logger.getLogger(FormProperty.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (houseToView != null) {
                        HouseViewDialog viewDialog = new HouseViewDialog(parentFrame, FormHouse.this, houseToView);
                        viewDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "House not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Table action events
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int houseId = (int) tblDisplay.getValueAt(row, 0); // Assuming ID is in the first column
                House houseToEdit = null;
                try {
                    houseToEdit = houseController.getHouseById(houseId);
                } catch (SQLException ex) {
                    Logger.getLogger(FormHouse.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (houseToEdit != null) {
                    HouseEditDialog editDialog = new HouseEditDialog(parentFrame, houseController, FormHouse.this, houseToEdit);
                    editDialog.setVisible(true);
                    refreshHouseList();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "House not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onDelete(int row) {
                if (tblDisplay.isEditing()) {
                    tblDisplay.getCellEditor().stopCellEditing();
                }

                int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "Are you sure you want to delete this house?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int houseId = (int) tblDisplay.getValueAt(row, 0); // Assuming houseId is in the first column
                        boolean isDeleted = houseController.deleteHouseById(houseId);
                        if (isDeleted) {
                            DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
                            model.removeRow(row);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Unable to delete house.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(FormHouse.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Error deleting house: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassCastException ex) {
                        Logger.getLogger(FormHouse.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Invalid house ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        // Refresh table with action events
        refreshTable(event);
    }

    private void refreshTable(TableActionEvent event) {
        refreshHouseList();

        //tblDisplay.setRowHeight(100);
        //tblDisplay.getColumnModel().getColumn(7).setCellRenderer(new ImageButtonRenderer());
        tblDisplay.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        tblDisplay.getColumnModel().getColumn(8).setCellEditor(new TableActionCellEditor(event));
        tblDisplay.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(javax.swing.JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });
    }

    public void refreshHouseList() {
        List<House> houses = null;
        try {
            houses = houseController.getAllHouses();
        } catch (SQLException ex) {
            Logger.getLogger(FormHouse.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0); // Clear the table
        for (House house : houses) {
            model.addRow(new Object[]{
                house.getId(),
                house.getName(),
                house.getProperty().getName() != null ? house.getProperty().getName() : "",
                house.getLocation(),
                house.getType(),
                house.getFee(),
                house.getPrice(),
                house.getAvailability(), //house.getPhoto() != null ? "resources/images/houses/" + house.getPhoto() : ""
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplay = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();

        tblDisplay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Property", "Location", "Type", "Fee", "Price", "Availabillity", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay.setRowHeight(44);
        jScrollPane1.setViewportView(tblDisplay);

        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/table/cell/add.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                        .addGap(33, 33, 33))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 806, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDisplay;
    // End of variables declaration//GEN-END:variables
}
