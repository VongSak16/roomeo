package gui.application;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import gui.application.form.HeaderForm;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import gui.application.form.LoginForm;
import gui.application.form.MainForm;
import gui.application.form.other.FormSetting;
import java.util.Locale;
import raven.toast.Notifications;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Raven
 */
public class Application extends javax.swing.JFrame {

    private static Application app;
    private final MainForm mainForm;
    private final LoginForm loginForm;
    private static ResourceBundle rb = ResourceBundle.getBundle("gui/theme/lang");

    public Application() {
        initComponents();
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainForm = new MainForm(this);
        loginForm = new LoginForm();
        setContentPane(loginForm);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Notifications.getInstance().setJFrame(this);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }

    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainForm);
        app.mainForm.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        app.mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
                app,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // If user selected "Yes", proceed with logout
        if (response == JOptionPane.YES_OPTION) {
            FlatAnimatedLafChange.showSnapshot();
            app.setContentPane(app.loginForm);
            app.loginForm.applyComponentOrientation(app.getComponentOrientation());
            SwingUtilities.updateComponentTreeUI(app.loginForm);
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        }
    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }

    public static void restart() {
        FlatAnimatedLafChange.showSnapshot();

        // Optionally, reset or clear any data if needed
        // e.g., clear fields, reset states, etc.
        // Set the content pane back to the main form (or whichever form you want)
        app.setContentPane(app.mainForm);
        app.mainForm.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0); // Reset the selected menu
        app.mainForm.hideMenu(); // Hide the menu if applicable

        app.loginForm.updateUIComponents();

        // Update the UI components
        SwingUtilities.updateComponentTreeUI(app.mainForm);

        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static String getString(String key) {
        return rb.getString(key);
    }

    public static void updateResourceBundle(Locale locale) {
        Locale.setDefault(locale);
        rb = ResourceBundle.getBundle("gui/theme/lang");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        FlatLaf.registerCustomDefaultsSource("gui.theme");
        //FlatRobotoFont.install();
        //UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        try {
            InputStream fontStream = Application.class.getResourceAsStream("/gui/font/Hanuman-Regular.otf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            UIManager.put("defaultFont", font.deriveFont(Font.PLAIN, 13f));
        } catch (Exception e) {
            System.err.println("Font format exception: " + e.getMessage());
        }

        Properties props = FormSetting.loadSettingsPropties();
        //Check Language in Settings
        String currentLanguage = props.getProperty("language");
        if (currentLanguage.equals("English") || currentLanguage == null) {
            updateResourceBundle(new Locale("eng", "US"));

        } else if (currentLanguage.equals("Khmer")) {
            updateResourceBundle(new Locale("kh", "KH"));
        }

        System.out.println(rb.getString("name"));
        rb = ResourceBundle.getBundle("gui/theme/lang");
        System.out.println(rb.getString("name"));

        //Check Mode in Settings
        String currentMode = props.getProperty("mode");
        if (currentMode.equals("Dark") || currentLanguage == null) {
            FlatMacDarkLaf.setup();

        } else if (currentMode.equals("Light")) {
            FlatMacLightLaf.setup();
        }

        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
            //  app.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            app.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
