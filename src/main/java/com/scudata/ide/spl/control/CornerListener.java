package com.scudata.ide.spl.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.scudata.ide.common.GC;

/**
 * 
 * �������ļ�����
 */
public class CornerListener implements MouseListener {
	/**
	 * ����ؼ�
	 */
	private SplControl control;
	/**
	 * �Ƿ���Ա༭
	 */
	private boolean editable = true;

	/**
	 * ���������캯��
	 * 
	 * @param control
	 *            ����ؼ�
	 */
	public CornerListener(SplControl control) {
		this(control, true);
	}

	/**
	 * ���������캯��
	 * 
	 * @param control
	 *            ����ؼ�
	 * @param editable
	 *            �Ƿ���Ա༭
	 */
	public CornerListener(SplControl control, boolean editable) {
		this.control = control;
		this.editable = editable;
	}

	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * ����������ʱ�Ĵ���
	 * 
	 * @param e
	 *            ����¼�
	 */
	public void mousePressed(MouseEvent e) {
		if (!editable) {
			showPopup(e);
			return;
		}
		control.selectAll();
		showPopup(e);
	}

	/**
	 * ��갴���뿪�¼�
	 */
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	/**
	 * ��ʾ�Ҽ������˵�
	 * 
	 * @param e
	 */
	void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			control.fireRightClicked(e, GC.SELECT_STATE_DM);
		}
	}

}
