package com.scudata.ide.common.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 * JTable��Ԫ��ť�༭��
 *
 */
public class ButtonEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

	/**
	 * ���ؼ�
	 */
	protected JTable table;
	/**
	 * ���ڱ༭����
	 */
	protected int editingRow = -1;
	/**
	 * ���ڱ༭����
	 */
	protected int editingCol = -1;
	/**
	 * ���ڱ༭��ֵ
	 */
	protected Object editingVal = null;

	/**
	 * �����ؼ�
	 */
	private JButton button = new JButton();

	/**
	 * ���캯��
	 */
	public ButtonEditor() {
		super(new JCheckBox());
		button.setText("...");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clicked();
			}
		});
	}

	/**
	 * �����
	 */
	protected void clicked() {
	}

	/**
	 * ���ر༭�ؼ�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.table = table;
		editingRow = row;
		return button;
	}

	/**
	 * ȡ��Ԫ��༭ֵ
	 */
	public Object getCellEditorValue() {
		return editingVal;
	}

	/**
	 * ֹͣ��Ԫ��༭
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	/**
	 * �༭ֹͣ
	 */
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
