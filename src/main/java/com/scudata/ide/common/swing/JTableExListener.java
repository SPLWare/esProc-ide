package com.scudata.ide.common.swing;

import java.awt.event.MouseEvent;

/**
 * ���ؼ�������
 *
 */
public interface JTableExListener {
	/**
	 * ����Ҽ����
	 * 
	 * @param xpos
	 *            X����
	 * @param ypos
	 *            Y����
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param e
	 *            ����¼�
	 */
	public void rightClicked(int xpos, int ypos, int row, int col, MouseEvent e);

	/**
	 * ���˫��
	 * 
	 * @param xpos
	 *            X����
	 * @param ypos
	 *            Y����
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param e
	 *            ����¼�
	 */
	public void doubleClicked(int xpos, int ypos, int row, int col, MouseEvent e);

	/**
	 * �����
	 *
	 * @param xpos
	 *            X����
	 * @param ypos
	 *            Y����
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param e
	 *            ����¼�
	 */
	public void clicked(int xpos, int ypos, int row, int col, MouseEvent e);

	/**
	 * �н��㷢���仯
	 * 
	 * @param oldRow
	 *            ֮ǰѡ�����
	 * @param newRow
	 *            ��ѡ�����
	 */
	public void rowfocusChanged(int oldRow, int newRow);
	public void stateChanged(javax.swing.event.ChangeEvent arg0);
	public void fireClicked(int xpos, int ypos, MouseEvent e);	
}
