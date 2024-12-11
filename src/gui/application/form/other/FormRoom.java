package gui.application.form.other;

import controller.RoomController;
import database.model.Room;
import gui.application.dialog.RoomCreateDialog;
import gui.application.dialog.RoomEditDialog;
import gui.application.dialog.RoomViewDialog;
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

public class FormRoom extends javax.swing.JPanel {

    private JFrame parentFrame;
    private RoomController roomController;

    public FormRoom() {
    }

    public FormRoom(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        initializeController();
        initializeListeners();
        refreshRoomList();
    }

    private void initializeController() {
        try {
            roomController = new RoomController(); // Initialize the RoomController
        } catch (SQLException ex) {
            Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeListeners() {
        // Add button listener
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Manual Create", "Magic Create"};
                int choice = JOptionPane.showOptionDialog(
                        parentFrame,
                        "Choose room creation method:",
                        "Room Creation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0] // Default to "Manual Create"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    // Manual Create option selected
                    RoomCreateDialog createDialog = new RoomCreateDialog(parentFrame, roomController, FormRoom.this);
                    createDialog.setVisible(true);
                    refreshRoomList();
                } else if (choice == JOptionPane.NO_OPTION) {
                    // Magic Create option selected
//                    RoomMagicCreateDialog magicDialog = new RoomMagicCreateDialog(parentFrame, roomController, FormRoom.this);
//                    magicDialog.setVisible(true);
//                    refreshRoomList();
                }
            }

        });

        // Table click listener
        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int roomId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column

                    Room roomToView = null;
                    try {
                        roomToView = roomController.getRoomById(roomId);
                    } catch (SQLException ex) {
                        Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (roomToView != null) {
                        RoomViewDialog viewDialog = new RoomViewDialog(parentFrame, FormRoom.this, roomToView);
                        viewDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Table action events
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int roomId = (int) tblDisplay.getValueAt(row, 0); // Assuming ID is in the first column
                Room roomToEdit = null;
                try {
                    roomToEdit = roomController.getRoomById(roomId);
                } catch (SQLException ex) {
                    Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (roomToEdit != null) {
                    RoomEditDialog editDialog = new RoomEditDialog(parentFrame, roomController, FormRoom.this, roomToEdit);
                    editDialog.setVisible(true);
                    refreshRoomList();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onDelete(int row) {
                if (tblDisplay.isEditing()) {
                    tblDisplay.getCellEditor().stopCellEditing();
                }

                int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "Are you sure you want to delete this room?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int roomId = (int) tblDisplay.getValueAt(row, 0); // Assuming roomId is in the first column
                        boolean isDeleted = roomController.deleteRoomById(roomId);
                        if (isDeleted) {
                            DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
                            model.removeRow(row);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Unable to delete room.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Error deleting room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassCastException ex) {
                        Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Invalid room ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        // Refresh table with action events
        refreshTable(event);
    }

    private void refreshTable(TableActionEvent event) {
        refreshRoomList();

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

    public void refreshRoomList() {
        List<Room> rooms = null;
        try {
            rooms = roomController.getAllRooms(); // Fetch all rooms from the controller
        } catch (SQLException ex) {
            Logger.getLogger(FormRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0); // Clear the table
        for (Room room : rooms) {
            model.addRow(new Object[]{
                room.getId(),
                room.getRoom_number(),
                room.getProperty() != null ? room.getProperty().getName() : "",
                room.getLocation(),
                room.getType(),
                room.getFee(),
                room.getPrice(),
                room.getAvailability(), // Add any other room attributes as needed, such as room photo
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
                "ID", "Room_Number", "Property", "Location", "Type", "Fee", "Price", "Availabillity", "Action"
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
