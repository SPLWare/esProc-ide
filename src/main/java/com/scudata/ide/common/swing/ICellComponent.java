package com.scudata.ide.common.swing;

import java.awt.Component;

/**
 * JTable��Ԫ������ӿ�
 *
 */
public interface ICellComponent {
	/**
	 * ȡ��Ԫ�����
	 * 
	 * @param value
	 *            ��ֵ
	 * @return
	 */
	public Component getCellComponent(Object value);

	/**
	 * ���õ�Ԫ���Ƿ�ɱ༭
	 * 
	 * @param editable
	 *            ��Ԫ���Ƿ�ɱ༭
	 */
	public void setCellEditable(boolean editable);

	/**
	 * ȡ�ַ���ֵ
	 * 
	 * @return
	 */
	public String getStringValue();
}
