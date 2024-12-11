package gui.application.form.other;

import controller.UserController;
import database.model.User;
import gui.application.dialog.UserCreateDialog;
import gui.application.dialog.UserEditDialog;
import gui.application.dialog.UserViewDialog;
import gui.table.cell.ImageButtonRenderer;
import gui.table.cell.TableActionCellEditor;
import gui.table.cell.TableActionCellRender;
import gui.table.cell.TableActionEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FormUser extends javax.swing.JPanel {

    private JFrame parentFrame;
    private UserController userController;

//    public FormUser() {
//    }
    public FormUser(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        initializeController();
        initializeListeners();
        refreshUserList();
    }

    private void initializeController() {
        try {
            userController = new UserController();
        } catch (SQLException ex) {
            Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeListeners() {
        // Add button listener
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserCreateDialog createDialog = new UserCreateDialog(parentFrame, userController, FormUser.this);
                createDialog.setVisible(true);
                refreshUserList();
            }
        });

        // Table click listener
        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int userId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                    viewUser(userId);
                }
            }
        });

        // Table action events
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int userId = (int) tblDisplay.getValueAt(row, 0); // Assuming ID is in the first column
                User userToEdit = null;
                try {
                    userToEdit = userController.getUserById(userId);
                } catch (SQLException ex) {
                    Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (userToEdit != null) {
                    UserEditDialog editDialog = new UserEditDialog(parentFrame, userController, FormUser.this, userToEdit);
                    editDialog.setVisible(true);
                    refreshUserList();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onDelete(int row) {
                if (tblDisplay.isEditing()) {
                    tblDisplay.getCellEditor().stopCellEditing();
                }
                int response = JOptionPane.showConfirmDialog(
                        parentFrame,
                        "Are you sure you want to delete this user?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        int userId = (int) tblDisplay.getValueAt(row, 0); // Assuming userId is in the first column
                        boolean isDeleted = userController.deleteUserById(userId);
                        if (isDeleted) {
                            DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
                            model.removeRow(row);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Unable to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassCastException ex) {
                        Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(parentFrame, "Invalid user ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        // Refresh table with action events
        refreshTable(event);
    }

    private void refreshTable(TableActionEvent event) {
        refreshUserList();

        tblDisplay.setRowHeight(100);
        tblDisplay.getColumnModel().getColumn(5).setCellRenderer(new ImageButtonRenderer());

        tblDisplay.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        tblDisplay.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event));
        tblDisplay.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(javax.swing.JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });
    }

    private void viewUser(int userId) {
        User userToView = null;
        try {
            userToView = userController.getUserById(userId);
        } catch (SQLException ex) {
            Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (userToView != null) {
            UserViewDialog viewDialog = new UserViewDialog(parentFrame, FormUser.this, userToView);
            viewDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshUserList() {
        List<User> users = null;
        try {
            users = userController.getAllUsers();
        } catch (SQLException ex) {
            Logger.getLogger(FormUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0); // Clear the table
        for (User user : users) {
            model.addRow(new Object[]{
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole(),
                user.getPhone(),
                user.getProfile_picture() != null ? "resources/images/users/" + user.getProfile_picture() : "",
                user.getProperty() != null ? user.getProperty().getName() : ""
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
                "ID", "Name", "Username", "Role", "Phone", "Profile Picture", "Property", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
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
            .addGap(0, 464, Short.MAX_VALUE)
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
