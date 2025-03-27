package com.scudata.ide.common.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * JTable��Ԫ����ɫ�༭��Ⱦ��
 *
 */
public class ColorCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * ��ɫͼ��
	 */
	private ColorIcon icon = new ColorIcon();

	/**
	 * ���캯��
	 */
	public ColorCellRenderer() {
		setOpaque(true);
		setIcon(icon);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	/**
	 * ����������ʾ�����
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Object color = null;
		if (value instanceof Integer) {
			int rgb = ((Integer) value).intValue();
			color = new Color(rgb, true);
		} else if (value instanceof Color) {
			color = (Color) value;
		} else {
			color = value;
		}
		icon.setColor(color);
		setText(" ");

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		return this;
	}
}
