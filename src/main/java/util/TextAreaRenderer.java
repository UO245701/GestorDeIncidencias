package util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        setText(value != null ? value.toString() : "");

        setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);

        int height = getPreferredSize().height;

        if (table.getRowHeight(row) != height) {
            table.setRowHeight(row, height);
        }

        return this;
    }
}
