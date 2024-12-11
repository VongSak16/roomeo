package gui.application.form.other;

import controller.BookingController;
import database.model.Booking;
//import gui.application.dialog.BookingCreateDialog;
//import gui.application.dialog.BookingEditDialog;
//import gui.application.dialog.BookingViewDialog;
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
public class FormBooking extends javax.swing.JPanel {

    private JFrame parentFrame;
    private BookingController bookingController;

    public FormBooking() {
    }

    public FormBooking(JFrame parentFrame) {
        initComponents();
        this.parentFrame = parentFrame;
        initializeController();
        initializeListeners();
        refreshBookingList();
    }

    private void initializeController() {
        try {
            bookingController = new BookingController();
        } catch (SQLException ex) {
            Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeListeners() {


        tblDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblDisplay.getSelectedRow() != -1) {
                    int selectedRow = tblDisplay.getSelectedRow();
                    int bookingId = (int) tblDisplay.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                    viewBooking(bookingId);
                }
            }
        });

        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
//                int bookingId = (int) tblDisplay.getValueAt(row, 0); // Assuming ID is in the first column
//                Booking bookingToEdit = null;
//                try {
//                    bookingToEdit = bookingController.getBookingById(bookingId);
//                } catch (SQLException ex) {
//                    Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                if (bookingToEdit != null) {
//                    BookingEditDialog editDialog = new BookingEditDialog(parentFrame, bookingController, FormBooking.this, bookingToEdit);
//                    editDialog.setVisible(true);
//                    refreshBookingList();
//                } else {
//                    JOptionPane.showMessageDialog(parentFrame, "Booking not found!", "Error", JOptionPane.ERROR_MESSAGE);
//                }
            }

            @Override
            public void onDelete(int row) {
//                if (tblDisplay.isEditing()) {
//                    tblDisplay.getCellEditor().stopCellEditing();
//                }
//                try {
//                    int bookingId = (int) tblDisplay.getValueAt(row, 0); // Assuming bookingId is in the first column
//                    boolean isDeleted = bookingController.deleteBookingById(bookingId);
//                    if (isDeleted) {
//                        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
//                        model.removeRow(row);
//                    } else {
//                        JOptionPane.showMessageDialog(parentFrame, "Unable to delete booking.", "Error", JOptionPane.ERROR_MESSAGE);
//                    }
//                } catch (SQLException | IOException ex) {
//                    Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
//                    JOptionPane.showMessageDialog(parentFrame, "Error deleting booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                } catch (ClassCastException ex) {
//                    Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
//                    JOptionPane.showMessageDialog(parentFrame, "Invalid booking ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                }
            }
        };
        refreshTable(event);
    }

    private void viewBooking(int bookingId) {
//        Booking bookingToView = null;
//        try {
//            bookingToView = bookingController.getBookingById(bookingId);
//        } catch (SQLException ex) {
//            Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (bookingToView != null) {
//            BookingViewDialog viewDialog = new BookingViewDialog(parentFrame, FormBooking.this, bookingToView);
//            viewDialog.setVisible(true);
//
//        } else {
//            JOptionPane.showMessageDialog(parentFrame, "Booking not found!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    public void refreshBookingList() {
        List<Booking> bookings = null;
        try {
            bookings = bookingController.getAllBookings();
        } catch (SQLException ex) {
            Logger.getLogger(FormBooking.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) tblDisplay.getModel();
        model.setRowCount(0);
        for (Booking booking : bookings) {
            model.addRow(new Object[]{
                booking.getId(),
                booking.getTenant() != null ? booking.getTenant().getName(): "", // Assuming you want to show tenant name
                booking.getBooking_date(),
                booking.getStatus(),
                booking.getTotal_fee(),
                booking.getTotal_price(),
                booking.getUser() != null ? booking.getUser().getUsername() : ""
            }
            );
        }
    }

    private void refreshTable(TableActionEvent event) {
        refreshBookingList();

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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDisplay = new javax.swing.JTable();

        tblDisplay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tenant", "Booking Date", "Status", "Total Fee", "Total Price", "By User "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDisplay.setRowHeight(44);
        jScrollPane1.setViewportView(tblDisplay);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDisplay;
    // End of variables declaration//GEN-END:variables
}
