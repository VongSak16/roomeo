package gui.application.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import gui.application.Application;
import gui.application.form.other.FormProperty;
import gui.application.form.other.FormBooking;
import gui.application.form.other.FormDashboard;
import gui.application.form.other.FormBookingAndPayment;
import gui.application.form.other.FormReport;
import gui.application.form.other.FormHouse;
import gui.application.form.other.FormProfile;
import gui.application.form.other.FormRoom;
import gui.application.form.other.FormSetting;
import gui.application.form.other.FormTenant;
import gui.application.form.other.FormUser;
import gui.menu.Menu;
import gui.menu.MenuAction;
import javax.swing.JFrame;
import services.AuthService;

/**
 *
 * @author XAX
 */
public class MainForm extends JLayeredPane {

    private JFrame parentFrame;

    public MainForm(JFrame parentFrame) {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("gui/icon/svg/" + icon, 0.8f));
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " + subIndex));
//            if (index == 0) {
//                Application.showForm(new FormDashboard());
//            } else if (index == 1) {
//                if (subIndex == 1) {
//                    Application.showForm(new FormBookingAndPayment());
//                } else if (subIndex == 2) {
//                    Application.showForm(new FormRead());
//                } else {
//                    action.cancel();
//                }
//            } else if (index == 9) {
//                Application.logout();
//            } else {
//                action.cancel();
//            }
            if (index == 0) {
                Application.showForm(new FormDashboard());
            } else if (index == 1) {
                Application.showForm(new FormBookingAndPayment(parentFrame));
            } else if (index == 2) {
                Application.showForm(new FormReport());
            } else if (index == 3) {
                Application.showForm(new FormTenant(parentFrame));
            } else if (index == 4) {
                Application.showForm(new FormBooking(parentFrame));
            } else if (index == 5) {
                Application.showForm(new FormProperty(parentFrame));
            } else if (index == 6) {
                Application.showForm(new FormRoom(parentFrame));
            } else if (index == 7) {
                Application.showForm(new FormHouse(parentFrame));
            } else if (index == 8) {
                Application.showForm(new FormUser(parentFrame));
            } else if (index == 9) {
                Application.showForm(new FormProfile());
            } else if (index == 10) {
                Application.showForm(new FormSetting());
            } else if (index == 11) {
                Application.logout();
            } else {
                action.cancel();
            }

        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon("gui/icon/svg/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
