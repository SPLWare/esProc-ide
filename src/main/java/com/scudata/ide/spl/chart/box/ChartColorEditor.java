package com.scudata.ide.spl.chart.box;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import com.scudata.chart.*;
import com.scudata.dm.*;
import com.scudata.ide.common.swing.*;

/**
 * �����ɫ�༭��
 * 
 * @author Joancy
 *
 */
public class ChartColorEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 6687705686119328614L;
	protected Object editingVal = null;
	private Dialog owner;

	private JButton button = new JButton();
	private ChartColorIcon icon = new ChartColorIcon();

	/**
	 * ����һ�������ɫ�༭��
	 * @param owner ������
	 */
	public ChartColorEditor(Dialog owner) {
		super(new JCheckBox());
		this.owner = owner;
		button.setIcon(icon);
		button.setHorizontalAlignment(JButton.CENTER);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clicked();
			}
		});
	}

	protected void clicked() {
		icon.setSize(button.getWidth(), button.getHeight());
		ChartColor cc = (ChartColor) editingVal;
		ChartColorDialog dialog = new ChartColorDialog(owner);
		dialog.setChartColor(cc);
		Point p = button.getLocationOnScreen();
		dialog.setVisible(true);
		if (dialog.getOption() == JOptionPane.OK_OPTION) {
			editingVal = dialog.getChartColor();
			icon.setChartColor(dialog.getChartColor());
			this.stopCellEditing();
		}
		dialog.dispose();
	}

	/**
	 * ʵ�ָ���ĳ��󷽷�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (value != null && !(value instanceof ChartColor)) {
			value = new ChartColor();
			table.setValueAt(value, row, column);
		}
		editingVal = value;
		if (isSelected) {
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setBackground(table.getBackground());
		}
		ChartColor cc = null;
		if (editingVal != null) {
			cc = (ChartColor) editingVal;
		}
		icon.setChartColor(cc);
		return button;
	}

	/**
	 * ��ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		return editingVal;
	}

	/**
	 * ֹͣ�༭
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}

/**
 * �����ɫ��Ⱦ��
 * 
 * @author Joancy
 *
 */
class ChartColorRender extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = -7052984657716825594L;
	private ChartColorIcon icon = new ChartColorIcon();

	public ChartColorRender() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setText(" ");
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		ChartColor cc;
		if (value == null) {
			cc = new ChartColor();
		} else if (value instanceof Color) {
			cc = new ChartColor((Color) value);
		} else if (value instanceof Number) {
			cc = new ChartColor(new Color(((Number) value).intValue()));
		} else if (value instanceof ChartColor) {
			cc = (ChartColor) value;
		} else if (value instanceof Sequence) {
			cc = (ChartColor)((Sequence) value).get(1);
		} else {
			throw new RuntimeException("Invalid value:"+value);
		}
		icon.setChartColor(cc);
		int w = ((JTableEx) table).getColumn(column).getWidth();
		int h = table.getRowHeight(row);
		this.setPreferredSize(new Dimension(w, h));
		icon.setSize(w, h);
		setIcon(icon);
		return this;
	}
}

/**
 * �����ɫ����ͼ����
 * 
 * @author Joancy
 *
 */
class ChartColorIcon implements Icon {
	private ChartColor cc = new ChartColor();
	private int width, height;
	
	public ChartColorIcon() {
		this(null);
	}

	public ChartColorIcon(ChartColor c) {
		if (c == null) {
			c = new ChartColor();
		}
		this.cc = c;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if(Utils.setPaint((Graphics2D) g, 0, 0, width, height, cc)){
			g.fillRect(0, 0, width, height);
		}else{
			ColorIcon.fillTransparent(g, width, height,8);
		}
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public void setChartColor(ChartColor c) {
		if (c == null)
			c = new ChartColor();
		this.cc = c;
	}

}
