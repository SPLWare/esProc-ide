package com.scudata.ide.spl.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.scudata.common.CellLocation;

/**
 * ���ѡ�е�Ԫ��ʱ��ʾ��Ԫ��༭���ļ�����
 */
public class ShowEditorListener implements MouseListener {
	/** ����������� */
	private ContentPanel cp;

	/**
	 * ���������캯��
	 * 
	 * @param panel
	 *            �����������
	 */
	public ShowEditorListener(ContentPanel panel) {
		this.cp = panel;
	}

	/**
	 * ��갴���¼�������ʾ��Ԫ��༭�ؼ�
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		CellLocation pos = ControlUtils.lookupCellPosition(x, y, cp);
		if (pos == null) {
			return;
		}
		cp.getControl().setActiveCell(pos);
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

}
