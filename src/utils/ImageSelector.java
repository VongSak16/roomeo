package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Image;
import java.io.File;

public class ImageSelector {

    private String selectedFilePath;

    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    public void selectAndScaleImage(JButton button, String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(fileType + " files", "jpg", "jpeg", "png"));
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedFilePath = selectedFile.getAbsolutePath();

            if (selectedFile.exists()) {
                System.out.println("Selected file: " + selectedFilePath);  // Log the selected file path
                setButtonIcon(button, selectedFilePath);
                button.setText(null); // Clear the text if image is set
            } else {
                System.out.println("File does not exist: " + selectedFilePath);
                button.setIcon(null); // Reset icon if no valid file is selected
                button.setText("No Image Selected");
            }
        } else {
            System.out.println("No file selected");
            button.setIcon(null); // Reset icon if no file is selected
            button.setText("No Image Selected");
        }
    }

    public void setButtonIcon(JButton button, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();

        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();

        // Add a check for valid dimensions
        if (buttonWidth > 0 && buttonHeight > 0) {
            // Scale image based on button dimensions
            int originalWidth = image.getWidth(null);
            int originalHeight = image.getHeight(null);

            double scaleFactor = Math.min((double) buttonWidth / originalWidth, (double) buttonHeight / originalHeight);
            int scaledWidth = (int) (originalWidth * scaleFactor);
            int scaledHeight = (int) (originalHeight * scaleFactor);

            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));

            System.out.println("Image set with scaled width: " + scaledWidth + ", scaled height: " + scaledHeight);
        } else {
            System.out.println("Button dimensions are not valid for scaling.");
        }
    }

    public boolean imageIsExist(String imagePath) {
        File imageFile = new File(imagePath);
        boolean exists = imageFile.exists() && imageFile.isFile();
        System.out.println("Image existence check for path: " + imagePath + " - Exists: " + exists);
        return exists;
    }

}
