package com.scudata.ide.spl.control;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.scudata.cellset.IColCell;
import com.scudata.common.Area;
import com.scudata.common.CellLocation;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.control.ControlUtilsBase;
import com.scudata.ide.spl.GCSpl;

/**
 * �б�ͷ������
 *
 */
public class ColHeaderListener implements MouseMotionListener, MouseListener,
		KeyListener {
	/**
	 * ����ؼ�
	 */
	private SplControl control;

	/** ��ѡ��Ŀ�ʼ�к� */
	private int startSelectedCol;

	/** �п�ı�ʱ����ʼX���� */
	private int resizeStartX;

	/** �п�ı�ʱ�ı������к� */
	private int resizeStartCol;

	/** �п�ı�ǰ��ԭʼ��� */
	private float oldCellWidth;

	/** �п�ı�����е���ʱ��� */
	private int tmpWidth;

	/**
	 * �Ƿ�ɱ༭
	 */
	private boolean editable = true;

	/**
	 * ��ǰ��
	 */
	private transient CellLocation activeCell = null;

	/**
	 * ֧����ͷ��ѡ
	 */
	protected boolean supportMultiSelect = true;

	/**
	 * ���������캯��
	 * 
	 * @param control
	 *            ����ؼ�
	 */
	public ColHeaderListener(SplControl control) {
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
	public ColHeaderListener(SplControl control, boolean editable) {
		this.control = control;
		this.editable = editable;
	}

	/**
	 * �������¼�
	 */
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
		control.getColumnHeader().getView().requestFocus();
		int col = (int) ControlUtils.lookupHeaderIndex(e.getX(), control.cellX,
				control.cellW);
		if (col < 0) {
			return;
		}
		boolean colIsSelected = false;
		if (control.m_selectedCols.size() > 0) {
			for (int i = 0; i < control.m_selectedCols.size(); i++) {
				Integer c = (Integer) control.m_selectedCols.get(i);
				if (c.intValue() == col) {
					colIsSelected = true;
					break;
				}
			}
		}
		if (e.getButton() == MouseEvent.BUTTON1 || !colIsSelected) {
			// ��δѡ�е����׸��Ҽ�ʱ�Ƚ���ѡ��
			if (control.status != GCSpl.STATUS_CELLRESIZE
					|| e.getButton() != MouseEvent.BUTTON1) {
				resizeStartCol = 0;
				if (!e.isControlDown() || !supportMultiSelect) {
					control.clearSelectedArea();
					control.m_selectedCols.clear();
				}
				control.m_selectedRows.clear();

				// ѧϰexcel��ѡ��������׸���Ϊ��ǰ��
				int firstRow = control.getContentPanel().drawStartRow;
				if (firstRow < 1 || firstRow > control.cellSet.getRowCount())
					firstRow = 1;
				control.setActiveCell(new CellLocation(firstRow, col), false);

				control.m_cornerSelected = false;
				if (e.isShiftDown() && this.startSelectedCol > 0) {
					control.m_selectedCols.clear();
					int start = col < this.startSelectedCol ? col
							: this.startSelectedCol;
					int end = col < this.startSelectedCol ? this.startSelectedCol
							: col;
					for (int i = start; i <= end; i++) {
						control.addSelectedCol(new Integer(i));
					}
					control.addSelectedArea(
							new Area(1, start, control.cellSet.getRowCount(),
									end), true);
				} else {
					this.startSelectedCol = col;
					control.addSelectedCol(new Integer(col));
					control.addSelectedArea(
							new Area(1, col, control.cellSet.getRowCount(), col),
							false);
				}
				control.repaint();
				control.fireRegionSelect(true);
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				control.getContentPanel().submitEditor();
				resizeStartX = e.getX();
				resizeStartCol = (int) ControlUtils.lookupHeaderIndex(
						resizeStartX, control.cellX, control.cellW);
				oldCellWidth = control.cellW[col] / control.scale;
				tmpWidth = control.cellW[col];
			}
		}
		showPopup(e);
		activeCell = control.getActiveCell();
		control.setActiveCell(null);
	}

	/**
	 * �������ͷ�ʱ�Ĵ���
	 *
	 * @param e
	 *            ����¼�
	 */
	public void mouseReleased(MouseEvent e) {
		if (!editable) {
			showPopup(e);
			return;
		}
		int x = e.getX();
		int col = (int) ControlUtils.lookupHeaderIndex(x, control.cellX,
				control.cellW);
		// ��קʱ��ˢ�¿ؼ�����ϲ�����ֻҪ����ק�ˣ��ͽ�ֹ���,�������⣬��ק�����˻����ɹ��û���ߵ���һ��
		if (activeCell != null && activeCell.getCol() == col) {
			control.setActiveCell(activeCell, false);
		}

		if (control.status == GCSpl.STATUS_CELLRESIZE) {
			if (resizeStartCol > 0) {
				if (col != resizeStartCol) {
					col = resizeStartCol;
				}
				Vector<Integer> willResizeCols = new Vector<Integer>();
				willResizeCols.add(new Integer(col));
				if (control.m_selectedCols.size() > 0) {
					int c1 = ((Integer) control.m_selectedCols.get(0))
							.intValue();
					int c2 = ((Integer) control.m_selectedCols
							.get(control.m_selectedCols.size() - 1)).intValue();
					int selectedStartCol = (int) Math.min(c1, c2);
					int selectedEndCol = (int) Math.max(c1, c2);
					if (col >= selectedStartCol && col <= selectedEndCol) {
						willResizeCols = control.m_selectedCols;
					}
				}
				IColCell cc = control.cellSet.getColCell(col);
				if (cc != null) {
					cc.setWidth(oldCellWidth);
					resizeColWidth(willResizeCols, tmpWidth / control.scale);
				}
			}
		} else {
			control.fireRegionSelect(true);
			control.status = GCSpl.STATUS_NORMAL;
		}

		final MouseEvent me = e;
		Thread t = new Thread() {
			public void run() {
				showPopup(me);
			}
		};
		SwingUtilities.invokeLater(t); // �ӳٵ���������˵��ڿؼ��б���ס
	}

	protected void resizeColWidth(Vector<Integer> willResizeCols, float newWidth) {
		control.fireColHeaderResized(willResizeCols, newWidth);
	}

	/**
	 * ������¼�
	 */
	public void mouseClicked(MouseEvent e) {
		// ˫���б���ĸ��ߣ������Զ������п�Ĺ���
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			int x = e.getX();
			int col = (int) ControlUtils.lookupHeaderIndex(x, control.cellX,
					control.cellW);
			if (x >= control.cellX[col] + control.cellW[col] - 2
					&& x <= control.cellX[col] + control.cellW[col]) {
				Vector<Integer> cols = new Vector<Integer>();
				cols.add(new Integer(col));
				SplEditor editor = ControlUtils.extractSplEditor(control);
				if (editor != null) {
					editor.selectedCols = cols;
					editor.adjustColWidth();
				}
			} else {
				mouseDoubleClicked(e, col);
			}
		}
	}

	public void mouseDoubleClicked(MouseEvent e, int col) {
	}

	/**
	 * ����˳��¼�
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * �������������϶����ʱ�Ĵ���
	 *
	 * @param e
	 *            ����¼�
	 */
	public void mouseDragged(MouseEvent e) {
		if (!editable) {
			return;
		}
		int x = e.getX();
		int col = (int) ControlUtils.lookupHeaderIndex(x, control.cellX,
				control.cellW);
		if (control.status == GCSpl.STATUS_NORMAL) {
			if (col < 0) {
				return;
			}
			int start = this.startSelectedCol < col ? this.startSelectedCol
					: col;
			int end = this.startSelectedCol > col ? this.startSelectedCol : col;
			if (start <= 0) { // wunan
				return;
			}
			control.m_selectedCols.clear(); // ��ק�����ٷ���ʱ���б���ѡ
			for (int i = start; i <= end; i++) {
				control.addSelectedCol(new Integer(i));
			}
			control.addSelectedArea(
					new Area(1, start, control.cellSet.getRowCount(), end),
					true);
			if (ControlUtils.scrollToVisible(control.getColumnHeader(),
					control, 0, col)) {
				Point p1 = control.getColumnHeader().getViewPosition();
				Point p2 = control.getViewport().getViewPosition();
				p2.x = p1.x;
				control.getViewport().setViewPosition(p2);
			}
			control.repaint();
			control.fireRegionSelect(true);
		}
		if (control.status == GCSpl.STATUS_CELLRESIZE) {
			if (resizeStartCol > 0) {
				if (col != resizeStartCol) {
					col = resizeStartCol;
				}
				tmpWidth = tmpWidth + x - resizeStartX;
				resizeStartX = x;
				if (tmpWidth < 1) {
					tmpWidth = 1;
				}
				float newWidth = tmpWidth / control.scale;
				IColCell cc = control.cellSet.getColCell(col);
				if (cc != null)
					cc.setWidth(newWidth);
				control.getColumnHeader().getView().repaint();

				// ��ק���̲�ʵʱˢ�����ݣ��ᵼ�����ֵ�wrapBuffer��������
				control.getViewport().getView().repaint();
				ControlUtilsBase.clearWrapBuffer();
			}
		}
	}

	/**
	 * ����ƶ�ʱ�Ĵ���
	 *
	 * @param e
	 *            ����¼�
	 */
	public void mouseMoved(MouseEvent e) {
		if (!editable) {
			return;
		}
		int x = e.getX();
		int col = ControlUtils.lookupHeaderIndex(x, control.cellX,
				control.cellW);
		if (col < 0) {
			control.status = GCSpl.STATUS_NORMAL;
			control.getColumnHeader()
					.getView()
					.setCursor(
							Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return;
		}
		if (x >= control.cellX[col] + control.cellW[col] - 2
				&& x <= control.cellX[col] + control.cellW[col]) {
			control.status = GCSpl.STATUS_CELLRESIZE;
			control.getColumnHeader()
					.getView()
					.setCursor(
							Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		} else {
			control.status = GCSpl.STATUS_NORMAL;
			control.getColumnHeader()
					.getView()
					.setCursor(
							Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * �Ҽ������˵�
	 * 
	 * @param e
	 */
	void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			control.fireRightClicked(e, GC.SELECT_STATE_COL);
		}
	}

	/**
	 * ����̧���¼�
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * ���̱����µĴ��� �����µ���shift+���������Ӧ�ı䵱ǰѡ�е���
	 *
	 * @param e
	 *            �����¼�
	 */
	public void keyPressed(KeyEvent e) {
		if (!editable) {
			return;
		}
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT:
			if (!e.isShiftDown()) {
				control.toRightCell();
				e.consume();
				return;
			}
			int start = ((Integer) control.m_selectedCols.get(0)).intValue();
			int end = ((Integer) control.m_selectedCols
					.get(control.m_selectedCols.size() - 1)).intValue();
			if (this.startSelectedCol == start) {
				end++;
			} else {
				start++;
			}
			if (end > control.cellSet.getColCount()) {
				end = (int) control.cellSet.getColCount();
			}
			if (start < 1) {
				start = 1;
			}
			control.m_selectedRows.clear();
			control.m_cornerSelected = false;
			control.m_selectedCols.clear();
			for (int i = start; i <= end; i++) {
				control.addSelectedCol(new Integer(i));
			}
			control.addSelectedArea(
					new Area(1, (int) start, control.cellSet.getRowCount(),
							(int) end), true);
			if (ControlUtils.scrollToVisible(control.getColumnHeader(),
					control, 0, (int) end)) {
				Point p1 = control.getColumnHeader().getViewPosition();
				Point p2 = control.getViewport().getViewPosition();
				p2.x = p1.x;
				control.getViewport().setViewPosition(p2);
			}
			control.repaint();
			control.fireRegionSelect(true);
			e.consume();
			break;
		case KeyEvent.VK_LEFT:
			if (!e.isShiftDown()) {
				control.toLeftCell();
				e.consume();
				return;
			}
			start = ((Integer) control.m_selectedCols.get(0)).intValue();
			end = ((Integer) control.m_selectedCols.get(control.m_selectedCols
					.size() - 1)).intValue();
			if (this.startSelectedCol == end) {
				start--;
			} else {
				end--;
			}
			if (end > control.cellSet.getColCount()) {
				end = (int) control.cellSet.getColCount();
			}
			if (start < 1) {
				start = 1;
			}
			control.m_selectedRows.clear();
			control.m_cornerSelected = false;
			control.m_selectedCols.clear();
			for (int i = start; i <= end; i++) {
				control.addSelectedCol(new Integer(i));
			}
			control.addSelectedArea(
					new Area(1, (int) start, control.cellSet.getRowCount(),
							(int) end), true);
			if (ControlUtils.scrollToVisible(control.getColumnHeader(),
					control, 0, (int) start)) {
				Point p1 = control.getColumnHeader().getViewPosition();
				Point p2 = control.getViewport().getViewPosition();
				p2.x = p1.x;
				control.getViewport().setViewPosition(p2);
			}
			control.repaint();
			e.consume();
			break;
		default:
			return;
		}
	}

	/**
	 * ���������¼�
	 */
	public void keyTyped(KeyEvent e) {
	}

}
