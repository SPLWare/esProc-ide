package com.scudata.ide.common.swing;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.scudata.common.StringUtils;

/**
 * JTable��Ԫ�������༭�ؼ�
 *
 */
public class ESPasswordBoxEditor extends AbstractCellEditor implements
		TableCellEditor, ICellComponent {
	private static final long serialVersionUID = 1L;
	/**
	 * �����ؼ�
	 */
	private JPasswordField pw1;
	/**
	 * �Ƿ���Ա༭
	 */
	private boolean editable = true;

	/**
	 * ���캯��
	 */
	public ESPasswordBoxEditor() {
		pw1 = new JPasswordField();
		pw1.setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		String text = pw1.getText();
		return text;
	}

	/**
	 * ���ر༭�Ŀؼ�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return getCellComponent(value);
	}

	/**
	 * ���������ؼ�
	 *
	 * @param value
	 *            Object
	 * @return Component
	 */
	public Component getCellComponent(Object value) {
		pw1.setEditable(editable);
		if (StringUtils.isValidString(value)) {
			pw1.setText((String) value);
		} else {
			pw1.setText(null);
		}
		return pw1;
	}

	/**
	 * �����Ƿ�ɱ༭
	 *
	 * @param editable
	 *            boolean
	 */
	public void setCellEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * ȡ�༭���ַ���ֵ
	 *
	 * @return String
	 */
	public String getStringValue() {
		return (String) getCellEditorValue();
	}
}
