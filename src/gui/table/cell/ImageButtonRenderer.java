package gui.table.cell;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import utils.ImageSelector;

/**
 * Renderer for the table cell that displays an image button.
 */
public class ImageButtonRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Call the superclass method
        Component com = super.getTableCellRendererComponent(jtable, value, isSelected, hasFocus, row, column);

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));

        String profilePicturePath = value + "";

        ImageSelector imageSelector = new ImageSelector();

        // Check if the profile picture path is valid
        if (profilePicturePath != null) {
            // Check if the file exists and is not an empty string
            File imgFile = new File(profilePicturePath);
            if (imgFile.exists() && imgFile.length() > 0) {
                ImageIcon icon = new ImageIcon(profilePicturePath);
                Image image = icon.getImage();
                int originalWidth = image.getWidth(null);
                int originalHeight = image.getHeight(null);
                double scaleFactor = Math.min((double) 100 / originalWidth, (double) 100 / originalHeight);
                int scaledWidth = (int) (originalWidth * scaleFactor);
                int scaledHeight = (int) (originalHeight * scaleFactor);
                Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

                // Check if the icon has valid dimensions
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    // Set the icon directly to the button
                    button.setIcon(new ImageIcon(scaledImage));
                    //System.out.println("Image loaded successfully: " + profilePicturePath);
                } else {
                    //System.out.println("Image is empty or has invalid dimensions: " + profilePicturePath);
                    // Optionally set a default icon
                    button.setIcon(new ImageIcon("path/to/default/image.png"));
                }
                //System.out.println("Image loaded successfully: " + profilePicturePath);
            } else {
                //System.out.println("Image file does not exist or is empty: " + profilePicturePath);
                // Optionally set a default or placeholder image here
            }
        } else {
            System.out.println("Profile picture path is null or empty");
            // Optionally set a default or placeholder image here
        }

        // Apply styling for alternate rows or selected rows
        if (!isSelected && row % 2 == 0) {
            button.putClientProperty(FlatClientProperties.STYLE, "background:$Table.background");
        } else if (!isSelected) {
            button.putClientProperty(FlatClientProperties.STYLE, "background:$Table.alternateRowColor");
        } else {
            button.putClientProperty(FlatClientProperties.STYLE, "background:$Table.selectionBackground");
        }
        // Return the panel containing the button as the renderer component
        return button;
    }
}
