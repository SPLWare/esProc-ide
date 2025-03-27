package com.scudata.ide.spl.control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.scudata.ide.common.GC;

/**
 * ����༭�����Ҽ�����¼�
 *
 */
public class EditorRightClicked extends MouseAdapter {
	/**
	 * ����ؼ�
	 */
	private SplControl control;

	/**
	 * ���캯��
	 * 
	 * @param control
	 *            ����ؼ�
	 */
	public EditorRightClicked(SplControl control) {
		this.control = control;
	}

	/**
	 * ��갴��
	 */
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	/**
	 * ����ͷ�
	 */
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	/**
	 * ��ʾ�Ҽ������˵�
	 * 
	 * @param e
	 */
	void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			control.fireRightClicked(e, GC.SELECT_STATE_CELL);
		}
	}

}
