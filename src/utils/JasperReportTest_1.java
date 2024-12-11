package utils;

import database.model.BookingItems;
import database.model.House;
import database.model.Property;
import database.model.Room;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class JasperReportTest_1 {

    public static void main(String[] args) {
        try {
            // Specify the path to the JRXML file
            String report = "src\\utils\\InvoiceReport_1_1.jrxml";

            // Compile the JRXML report
            JasperReport jasperReport = JasperCompileManager.compileReport(report);
            System.out.println("Report compiled successfully!");

            // Create a list to hold BookingItems
            List<BookingItems> bookingItems2 = new ArrayList<>();

            // Add a new BookingItems object to the list
            bookingItems2.add(new BookingItems(
                    1, // id
                    null, // booking (null)
                    LocalDateTime.now(), // check_in_date (current date and time)
                    LocalDateTime.now(), // check_out_date (current date and time)
                    LocalDateTime.now(), // paid_date (current date and time)
                    "Confirmed", // status
                    new Room(0, "asdsda", null, null, null, 10f, 10f, null, null),
                    null, // house (null)
                    10.0f, // fee
                    10.0f // price
            ));
            bookingItems2.add(new BookingItems(
                    1, // id
                    null, // booking (null)
                    LocalDateTime.now(), // check_in_date (current date and time)
                    LocalDateTime.now(), // check_out_date (current date and time)
                    LocalDateTime.now(), // paid_date (current date and time)
                    "Not Confirmed", // status
                    null,
                    new House(0, "adasdad", null, null, null, 10f, 10f, null, null),
                    10.0f, // fee
                    10.0f // price
            ));

            // Create a data source from the bookingItems2 list
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookingItems2);

            // Parameters for the report
            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("parameter1", new Date().toString());
//            parameters.put("parameter2", "asdasda");

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("Report filled successfully!");

            // Show the report in a Swing window
            SwingUtilities.invokeLater(() -> JasperViewer.viewReport(jasperPrint, false));
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error on report: " + e.getMessage());
        }
    }
}
