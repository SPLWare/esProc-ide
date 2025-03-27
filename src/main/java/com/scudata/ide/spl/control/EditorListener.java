package com.scudata.ide.spl.control;

import java.awt.event.MouseEvent;
import java.util.Vector;

import com.scudata.common.Area;

/**
 * �༭�ؼ�������༭��֮�䴫����Ϣ�Ľӿ�
 */
public interface EditorListener {
	/**
	 * ����һ��������Ҽ���Ϣ���ṩ��ݲ˵��ĵ�λ��
	 * 
	 * @param e
	 *            ����¼�
	 * @param clickPlace
	 *            ����һ���λ�ã�GC�ж���ĳ���
	 */
	public void rightClicked(MouseEvent e, int clickPlace);

	/**
	 * ���˫��
	 * 
	 * @param e
	 *            ����¼�
	 */
	public void doubleClicked(MouseEvent e);

	/**
	 * ����ѡ���¼�
	 * 
	 * @param vectRegion
	 *            ѡ�е������������ڲ�����Ϊ�������CellRegion(�ӿ�֧�ֶ�ѡ���ڲ���ûʵ��)
	 * @param selectedRows
	 *            ѡ�е��б��
	 * @param selectedColumns
	 *            ѡ�е��б��
	 * @param selectedAll
	 *            �Ƿ�ѡ������������
	 */
	public void regionsSelect(Vector<Object> vectRegion,
			Vector<Integer> selectedRows, Vector<Integer> selectedColumns,
			boolean selectedAll, boolean keyEvent);

	/**
	 * �п�䶯��Ϣ
	 * 
	 * @param vectColumn
	 *            �䶯�����������ڲ�����Ϊ�к�Integer
	 * @param nWidth
	 *            �䶯�Ŀ��
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean columnWidthChange(Vector<Integer> vectColumn, float nWidth);

	/**
	 * �и߱䶯��Ϣ
	 * 
	 * @param vectRow
	 *            �䶯�����������ڲ�����Ϊ�к�Integer
	 * @param nHeight
	 *            �䶯�ĸ߶�
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean rowHeightChange(Vector<Integer> vectRow, float nHeight);

	/**
	 * ����ճ����Ϣ
	 * 
	 * @param area
	 *            ճ���ı������
	 * @param nRowPos
	 *            ճ������λ��
	 * @param nColumnPos
	 *            ճ������λ��
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean cellRegionPaste(Area area, int nRowPos, int nColumnPos);

	/**
	 * ������չ��Ϣ����ûʵ�֣�
	 * 
	 * @param area
	 *            ��չ�ı������
	 * @param nColumnExpand
	 *            ����չ��(����������չ������������չ��0������չ)
	 * @param nRowExpand
	 *            ����չ��(����������չ������������չ��0������չ)
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean cellRegionExpand(Area area, int nColumnExpand, int nRowExpand);

	/**
	 * ����������Ϣ����ûʵ�֣�
	 * 
	 * @param area
	 *            �����ı������
	 * @param nRowShrink
	 *            ����������������ȥ��������0����������
	 * @param nColumnShrink
	 *            ����������������ȥ��������0����������
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean cellRegionShrink(Area area, int nColumnShrink, int nRowShrink);

	/**
	 * ����ı��༭��Ϣ
	 * 
	 * @param row
	 *            �༭���к�
	 * @param col
	 *            �༭���к�
	 * @param strText
	 *            �༭����ı�
	 * @return ture ��Ϣ�ѱ�����false ��Ϣδ������
	 */
	public boolean cellTextInput(int row, int col, String strText);

	/**
	 * ���ͱ༭������¼����ı�
	 * 
	 * @param text
	 *            ����¼����ı�
	 */
	public void editorInputing(String text);

	/**
	 * ����ƶ�
	 * 
	 * @param row
	 *            ��
	 * @param col
	 *            ��
	 */
	public void mouseMove(int row, int col);

	/**
	 * �������ƶ�
	 */
	public void scrollBarMoved();

}
