package com.scudata.ide.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.scudata.common.StringUtils;

/**
 * JTable��Ԫ���������Ⱦ��
 *
 */
public class ESPasswordBoxRenderer implements TableCellRenderer, ICellComponent {

	/**
	 * ����༭��
	 */
	private JPasswordField pw1 = new JPasswordField();

	/**
	 * ���캯��
	 */
	public ESPasswordBoxRenderer() {
	}

	/**
	 * ȡ��ʾ�Ŀؼ�
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = getCellComponent(value);
		CellAppr appr = getCellAppr(table, isSelected);
		return appr.apply(c);
	}

	/**
	 * ȡ��Ԫ��ؼ�
	 */
	public Component getCellComponent(Object value) {
		if (value != null) {
			if (StringUtils.isValidString(value)) {
				pw1.setText((String) value);
			} else {
				try {
					pw1.setText(String.valueOf(value));
				} catch (Exception e) {
				}
			}
		}
		return pw1;
	}

	/**
	 * setCellEditable
	 *
	 * @param editable
	 *            boolean
	 */
	public void setCellEditable(boolean editable) {
	}

	/**
	 * getStringValue
	 *
	 * @return String
	 */
	public String getStringValue() {
		return "";
	}

	/**
	 * ȡ��Ԫ����ʾ����
	 * 
	 * @param table
	 * @param isSelected
	 * @return
	 */
	public CellAppr getCellAppr(JTable table, boolean isSelected) {
		CellAppr appr = new CellAppr();
		if (isSelected) {
			appr.setForeground(table.getSelectionForeground());
			appr.setBackground(table.getSelectionBackground());
		} else {
			appr.setForeground(table.getForeground());
			appr.setBackground(table.getBackground());
		}
		return appr;
	}

	/**
	 * ��Ԫ����ʾ����
	 *
	 */
	class CellAppr {
		/**
		 * ǰ��ɫ
		 */
		private Color foreground = Color.black;
		/**
		 * ����ɫ
		 */
		private Color background = Color.white;

		/**
		 * ����
		 */
		private Font font;

		/**
		 * ���캯��
		 */
		public CellAppr() {
		}

		/**
		 * ���캯��
		 * 
		 * @param foreground
		 *            ǰ��ɫ
		 * @param background
		 *            ����ɫ
		 */
		public CellAppr(Color foreground, Color background) {
			this.foreground = foreground;
			this.background = background;
		}

		/**
		 * ȡǰ��ɫ
		 * 
		 * @return
		 */
		public Color getForeground() {
			return foreground;
		}

		/**
		 * ȡ����ɫ
		 * 
		 * @return
		 */
		public Color getBackground() {
			return background;
		}

		/**
		 * ����ǰ��ɫ
		 * 
		 * @param c
		 */
		public void setForeground(Color c) {
			foreground = c;
		}

		/**
		 * ���ñ���ɫ
		 * 
		 * @param c
		 */
		public void setBackground(Color c) {
			background = c;
		}

		/**
		 * ��������
		 * 
		 * @param font
		 */
		public void setFont(Font font) {
			this.font = font;
		}

		/**
		 * ȡ����
		 * 
		 * @return
		 */
		public Font getFont() {
			return font;
		}

		/**
		 * ����Ӧ�õ��ؼ�
		 * 
		 * @param c
		 * @return
		 */
		public Component apply(Component c) {
			if (c instanceof Container) {
				Container con = (Container) c;
				if (con.getComponentCount() == 0) {
					c.setForeground(getForeground());
					c.setBackground(getBackground());
					if (font != null) {
						c.setFont(font);
					}
				} else {
					Component[] cons = con.getComponents();
					for (int i = 0; i < cons.length; i++) {
						apply(cons[i]);
					}
				}
			} else {
				c.setForeground(getForeground());
				c.setBackground(getBackground());
				if (font != null) {
					c.setFont(font);
				}
			}
			return c;
		}
	}
}
