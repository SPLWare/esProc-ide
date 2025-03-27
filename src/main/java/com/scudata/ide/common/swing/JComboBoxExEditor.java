package com.scudata.ide.common.swing;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import com.scudata.common.StringUtils;

/**
 * JTable��Ԫ��������༭��
 *
 */
public class JComboBoxExEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	/**
	 * ������ؼ�
	 */
	JComboBoxEx combo;

	/**
	 * ���캯��
	 * 
	 * @param cbe
	 *            ������ؼ�
	 */
	public JComboBoxExEditor(JComboBoxEx cbe) {
		super(cbe);
		combo = cbe;
	}

	/**
	 * ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		if (combo.isEditable()) {
			try {
				if (combo.getEditor().getEditorComponent() instanceof JTextField) {
					JTextField jtf = (JTextField) combo.getEditor()
							.getEditorComponent();
					if (StringUtils.isValidString(jtf.getText()))
						return jtf.getText();
				}
			} catch (Exception ex) {
			}
		}
		return combo.x_getSelectedItem();
	}

	/**
	 * ȡ�ؼ�
	 * 
	 * @return
	 */
	public JComboBoxEx getJComboBoxEx() {
		return combo;
	}

}
