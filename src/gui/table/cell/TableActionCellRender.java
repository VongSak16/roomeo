package gui.table.cell;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author RAVEN
 */
public class TableActionCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSeleted, boolean bln1, int row, int column) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSeleted, bln1, row, column);
        PanelAction action = new PanelAction();
        if (isSeleted == false && row % 2 == 0) {
            action.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:$Table.background");
        } else if (isSeleted == false && row % 2 != 0) {
            action.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:$Table.alternateRowColor");
        } else {
            action.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:$Table.selectionBackground");
        }
        return action;
    }
}
