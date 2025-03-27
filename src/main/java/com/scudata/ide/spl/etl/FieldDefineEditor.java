package com.scudata.ide.spl.etl;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * �ֶζ���༭��
 * 
 * @author Joancy
 *
 */
public class FieldDefineEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;
	protected ArrayList<FieldDefine> editingVal = null;
	private Dialog owner;

	private JButton button = new JButton();
	FieldDefineIcon icon = new FieldDefineIcon();
	
	int defineType;
	
	/**
	 * ���캯��
	 * @param owner ������
	 * @param defineType ��������
	 */
	public FieldDefineEditor(Dialog owner,int defineType) {
		super(new JCheckBox());
		this.owner = owner;
		this.defineType = defineType;
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
		IFieldDefineDialog iDialog;
		Dialog dialog=null;
		if(defineType==EtlConsts.INPUT_FIELDDEFINE_NORMAL){
			dialog = new FieldDefineDialog(owner);
		}else if(defineType==EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD ||
				defineType==EtlConsts.INPUT_FIELDDEFINE_FIELD_EXP ||
				defineType==EtlConsts.INPUT_FIELDDEFINE_RENAME_FIELD
				){
			dialog = new ExportFieldsDialog(owner,defineType);
		}else if(defineType==EtlConsts.INPUT_FIELDDEFINE_FIELD_DIM){
			dialog = new FieldDimDialog(owner);
		}
		iDialog = (IFieldDefineDialog)dialog;
		iDialog.setFieldDefines(editingVal);
		
		Point p = button.getLocationOnScreen();
		dialog.setLocation(p.x, p.y + button.getHeight());
		dialog.setVisible(true);
		if (iDialog.getOption() == JOptionPane.OK_OPTION) {
			editingVal = iDialog.getFieldDefines();
			icon.setFieldDefines(editingVal);
			this.stopCellEditing();
		}
		dialog.dispose();
	}

	/**
	 * ʵ�ָ���ĳ��󷽷�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editingVal = (ArrayList<FieldDefine>)value;
		if (isSelected) {
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setBackground(table.getBackground());
		}
		
		icon.setFieldDefines(editingVal);
		return button;
	}

	/**
	 * ��ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		return editingVal;
	}

	/**
	 * ֹͣ��ǰ�༭
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
