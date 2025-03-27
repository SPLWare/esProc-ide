package com.scudata.ide.spl.etl;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * �ָ����ָ����ַ����༭��
 * 
 * @author Joancy
 *
 */
public class StringListEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	protected ArrayList<String> editingVal = null;
	private Dialog owner;

	private JButton button = new JButton();
	StringListIcon icon = new StringListIcon();

	/**
	 * ���캯��
	 * @param owner ������
	 */
	public StringListEditor(Dialog owner) {
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
		StringListDialog dialog = new StringListDialog(owner);
		dialog.setList(editingVal);
		Point p = button.getLocationOnScreen();
		dialog.setLocation(p.x, p.y + button.getHeight());
		dialog.setVisible(true);
		if (dialog.getOption() == JOptionPane.OK_OPTION) {
			editingVal = dialog.getList();
			icon.setList(editingVal);
			this.stopCellEditing();
		}
		dialog.dispose();
	}

	/**
	 * ʵ�ָ�����󷽷������ر༭�ؼ�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editingVal = (ArrayList<String>)value;
		if (isSelected) {
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setBackground(table.getBackground());
		}
		
		icon.setList(editingVal);
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

