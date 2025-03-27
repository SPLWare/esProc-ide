package com.scudata.ide.common.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.event.EventListenerList;

import com.scudata.common.Logger;

/**
 * ������༭��
 */

public abstract class AbstractComboBoxEditor implements ComboBoxEditor {

	/**
	 * �������б�
	 */
	EventListenerList listenerList = new EventListenerList();

	/**
	 * ���Ӽ�����
	 */
	public void addActionListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}

	/**
	 * ɾ��������
	 */
	public void removeActionListener(ActionListener listener) {
		listenerList.remove(ActionListener.class, listener);
	}

	/**
	 * ִ���¼�
	 * 
	 * @param e
	 */
	protected void fireActionPerformed(ActionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				Logger.debug(listeners[i].getClass().getName());
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

}
