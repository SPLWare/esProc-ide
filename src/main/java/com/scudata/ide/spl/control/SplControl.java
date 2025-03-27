package com.scudata.ide.spl.control;

import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;

import com.scudata.cellset.ICellSet;
import com.scudata.cellset.datamodel.NormalCell;
import com.scudata.cellset.datamodel.PgmCellSet;
import com.scudata.common.Area;
import com.scudata.common.ByteMap;
import com.scudata.common.CellLocation;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.control.ControlBase;
import com.scudata.ide.common.control.ControlUtilsBase;
import com.scudata.ide.spl.SheetSpl;

/**
 * ����༭�ؼ�
 *
 */
public abstract class SplControl extends ControlBase {

	private static final long serialVersionUID = 1L;

	/**
	 * �������
	 */
	public PgmCellSet cellSet;

	/** �ڱ༭ʱ��ѡ�еĵ�Ԫ������ */
	public Vector<Object> m_selectedAreas = new Vector<Object>();

	/** ��ǰ��Ԫ���λ�� */
	public CellLocation m_activeCell;

	/** ����ִ��ʱ�ĵ�ǰλ�� */
	public CellLocation stepPosition;

	/** �ϵ� */
	protected ArrayList<CellLocation> breakPoints = new ArrayList<CellLocation>();

	/** ��������� */
	protected CellLocation calcPos;

	/** �༭״̬ */
	int status;

	/** �༭ʱ��ѡ�е��кż� */
	Vector<Integer> m_selectedCols = new Vector<Integer>();

	/** �༭ʱ��ѡ�е��кż� */
	Vector<Integer> m_selectedRows = new Vector<Integer>();

	/** �༭ʱ�Ƿ�ѡ������������ */
	boolean m_cornerSelected = false;

	/** �༭���������� */
	ArrayList<EditorListener> m_editorListener;

	/** ������� */
	public ContentPanel contentView = null;

	/** �༭ʱ�����׸��X�������� */
	int[] cellX;

	/** �༭ʱ�����׸��Y�������� */
	int[] cellY;

	/** �༭ʱ�����׸�Ŀ������ */
	int[] cellW;

	/** �༭ʱ�����׸�ĸ߶����� */
	int[] cellH;

	/** ��ʾ���� */
	public float scale = 1.0f;

	/**
	 * ��ǰ����궯����ѡ����ӻ���ѡ�б༭��
	 */
	public boolean isSelectingCell = false;

	/**
	 * �б�ͷ���б�ͷ���
	 */
	JPanel rowHeaderView = null, colHeaderView = null;
	/**
	 * �б�ͷ���
	 */
	ColHeaderPanel headerPanel = null;

	/**
	 * ҳ�����
	 */
	protected SheetSpl sheet;

	/**
	 * ���캯��
	 */
	public SplControl() {
		this(1, 1);
	}

	/**
	 * ���캯��
	 * 
	 * @param rows
	 *            ����
	 * @param cols
	 *            ����
	 */
	public SplControl(int rows, int cols) {
		super();
		m_editorListener = new ArrayList<EditorListener>();
		getHorizontalScrollBar().setUnitIncrement(10);
		getVerticalScrollBar().setUnitIncrement(10);
		cellSet = new PgmCellSet(rows, cols);
	}

	/**
	 * ����Sheet����
	 * 
	 * @param sheet
	 */
	public void setSheet(SheetSpl sheet) {
		this.sheet = sheet;
	}

	/**
	 * ȡSheet����
	 * 
	 * @return
	 */
	public SheetSpl getSheet() {
		return sheet;
	}

	/**
	 * �������������������
	 */
	public void setSplScrollBarListener() {
		getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				// ���ˢ����������ʱˢ�£���ʱ�ٶȺܿ첻����
				contentView.repaint();
			}
		});

		getHorizontalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						contentView.repaint();
					}
				});

	}

	/**
	 * ���ø����Ƿ�ѡ��
	 * 
	 * @param isSelect
	 */
	public void setSelectCell(boolean isSelect) {
		this.isSelectingCell = isSelect;
	}

	/**
	 * ȡ�������
	 * 
	 * @return
	 */
	public ContentPanel getContentPanel() {
		return contentView;
	}

	/**
	 * ȡ�б�ͷ���
	 * 
	 * @return
	 */
	public JPanel getRowHeaderPanel() {
		return rowHeaderView;
	}

	/**
	 * ȡ�б�ͷ���
	 * 
	 * @return
	 */
	public JPanel getColHeaderPanel() {
		return colHeaderView;
	}

	/**
	 * ���öϵ�
	 * 
	 * @param breakPoints
	 */
	public void setBreakPoints(ArrayList<CellLocation> breakPoints) {
		this.breakPoints = breakPoints;
	}

	/**
	 * ȡ�ϵ�
	 * 
	 * @return
	 */
	public ArrayList<CellLocation> getBreakPoints() {
		return breakPoints;
	}

	/**
	 * �Ƿ������˶ϵ����
	 * 
	 * @param row
	 *            int
	 * @return boolean
	 */
	public boolean isBreakPointRow(int row) {
		Iterator<CellLocation> it = breakPoints.iterator();
		while (it.hasNext()) {
			CellLocation cp = it.next();
			if (cp.getRow() == row) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ɾ���жϵ�
	 * 
	 * @param row
	 *            ָ�����к�
	 */
	public void removeRowBreakPoints(int row) {
		for (int i = breakPoints.size() - 1; i >= 0; i--) {
			CellLocation cp = (CellLocation) breakPoints.get(i);
			if (cp.getRow() == row) {
				breakPoints.remove(i);
			} else if (cp.getRow() > row) {
				cp.setRow(cp.getRow() - 1);
			}
		}
	}

	/**
	 * ɾ���жϵ�
	 * 
	 * @param col
	 *            ָ�����к�
	 */
	public void removeColBreakPoints(int col) {
		for (int i = breakPoints.size() - 1; i >= 0; i--) {
			CellLocation cp = (CellLocation) breakPoints.get(i);
			if (cp.getCol() == col) {
				breakPoints.remove(i);
			} else if (cp.getCol() > col) {
				cp.setCol(cp.getCol() - 1);
			}
		}
	}

	/**
	 * ���öϵ�
	 */
	public void setBreakPoint() {
		CellLocation cp = getActiveCell();
		if (cp == null) {
			return;
		}
		if (breakPoints.contains(cp)) {
			breakPoints.remove(cp);
		} else {
			breakPoints.add(cp);
		}
		repaint();
	}

	/**
	 * �Ƿ�ϵ��
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @return
	 */
	public boolean isBreakPointCell(int row, int col) {
		Iterator<CellLocation> it = breakPoints.iterator();
		while (it.hasNext()) {
			CellLocation cp = it.next();
			if (cp.getRow() == row && cp.getCol() == col) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �Ƿ�ѡ�еĸ�
	 * 
	 * @return
	 */
	public boolean isSelectingCell() {
		return isSelectingCell;
	}

	/**
	 * ȡ��ǰ��
	 * 
	 * @return
	 */
	public CellLocation getActiveCell() {
		return m_activeCell;
	}

	/**
	 * ���õ�������ִ�и�����
	 * 
	 * @param cp
	 *            ��������ִ�и�����
	 */
	public void setStepPosition(CellLocation cp) {
		this.stepPosition = cp;
	}

	/**
	 * ȡ��������ִ�и�����
	 * 
	 * @return
	 */
	public CellLocation getStepPosition() {
		return stepPosition;
	}

	/**
	 * ����Ҫ����ĸ�����
	 * 
	 * @param cp
	 */
	public void setCalcPosition(CellLocation cp) {
		this.calcPos = cp;
	}

	/**
	 * ȡҪ����ĸ�����
	 * 
	 * @return
	 */
	public CellLocation getCalcPosition() {
		return calcPos;
	}

	/**
	 * ���¼��ر༭�ؼ����ı�
	 */
	public void reloadEditorText() {
		this.contentView.reloadEditorText();
	}

	/**
	 * ȡ���ӵ�����
	 * 
	 * @param row
	 *            ���ӵ��к�
	 * @param col
	 *            ���ӵ��к�
	 * @return
	 */
	public Point[] getCellPoint(int row, int col) {
		Point p[] = new Point[2];
		int x1 = cellX[col];
		int y1 = cellY[row];
		int x2 = x1 + cellW[col];
		int y2 = y1 + cellH[row];
		p[0] = new Point(x1, y1);
		p[1] = new Point(x2, y2);
		return p;
	}

	/**
	 * ���õ�ǰ��
	 * 
	 * @param pos
	 *            ���ӵ�����
	 * @return
	 */
	public Area setActiveCell(CellLocation pos) {
		return setActiveCell(pos, true);
	}

	/**
	 * ���õ�ǰ��
	 * 
	 * @param pos
	 *            ���ӵ�����
	 * @param clearSelection
	 *            �Ƿ����֮ǰ��ѡ������
	 * @return
	 */
	public Area setActiveCell(CellLocation pos, boolean clearSelection) {
		return setActiveCell(pos, clearSelection, true);
	}

	/**
	 * ���õ�ǰ��
	 * 
	 * @param pos
	 *            ��ǰ��Ԫ��λ�� clearSelection : ͨ�����Ҫ���ѡ������Ϊѡ������ʱ���ƶ��ؼ���ѡ��򣬱��������ѡ��
	 *            ������ѡ���кܶ����ƣ����ܲ���� �������ø�λ�ú�ĵ�����������,�����ϲ��סѡ�е�����
	 * @param clearSelection
	 *            �Ƿ����֮ǰ��ѡ������
	 * @param scrollToVisible
	 *            ��ǰ��û����ʾʱ���Ƿ��������ǰ��ʹ����ʾ
	 * @return
	 */
	public Area setActiveCell(CellLocation pos, boolean clearSelection,
			boolean scrollToVisible) {
		pos = ControlUtilsBase.checkPosition(pos, getCellSet());
		Area a;
		if (pos == null) {
			contentView.submitEditor();
			m_activeCell = null;
			contentView.setEditorVisible(false);
			a = null;
		} else {
			int row = pos.getRow();
			int col = pos.getCol();
			a = new Area(row, col, row, col);
			contentView.rememberedRow = row;
			contentView.rememberedCol = col;

			if (clearSelection) {
				m_selectedRows.clear();
				m_selectedCols.clear();
				m_cornerSelected = false;
			}

			contentView.submitEditor();
			m_activeCell = pos;
			if (scrollToVisible)
				ControlUtils.scrollToVisible(getViewport(), this, pos.getRow(),
						pos.getCol());
			repaint();
			contentView.initEditor(ContentPanel.MODE_HIDE);
			contentView.requestFocus();
		}

		return a;
	}

	/**
	 * ����ǰ��Ԫ����һ��ͬһ��λ�õĵ�Ԫ���Ϊ��ǰ��Ԫ��
	 */
	public Area toUpCell() {
		if (m_activeCell == null) {
			return null;
		}
		CellSetParser parser = new CellSetParser(this.cellSet);
		int row = m_activeCell.getRow();
		int col = m_activeCell.getCol();
		row--;
		if (row < 1)
			return null;
		while (!parser.isRowVisible(row)) {
			row--;
			if (row < 1) {
				return null;
			}
		}
		if (row < 1) {
			return null;
		}
		return setActiveCell(new CellLocation(row, col));
	}

	/**
	 * ����ǰ��Ԫ����һ��ͬһ��λ�õĵ�Ԫ���Ϊ��ǰ��Ԫ��
	 */
	public Area toDownCell() {
		if (m_activeCell == null) {
			return null;
		}
		CellSetParser parser = new CellSetParser(this.cellSet);
		int row = m_activeCell.getRow();
		int col = m_activeCell.getCol();
		row++;
		if (row > cellSet.getRowCount())
			return null;
		while (!parser.isRowVisible(row)) {
			row++;
			if (row > contentView.cellSet.getRowCount()) {
				return null;
			}
		}

		if (row > contentView.cellSet.getRowCount()) {
			return null;
		}
		return setActiveCell(new CellLocation(row, col));
	}

	/**
	 * ����ǰ��Ԫ�����һ��ͬһ��λ�õĵ�Ԫ���Ϊ��ǰ��Ԫ��
	 */
	public Area toLeftCell() {
		if (m_activeCell == null) {
			return null;
		}
		int row = m_activeCell.getRow();
		int col = m_activeCell.getCol();
		col--;
		if (col < 1) {
			return null;
		}
		CellSetParser parser = new CellSetParser(cellSet);
		while (!parser.isColVisible(col)) {
			col--;
			if (col < 1) {
				return null;
			}
		}
		if (col < 1) {
			return null;
		}
		return setActiveCell(new CellLocation(row, col));
	}

	/**
	 * ����ǰ��Ԫ���ұ�һ��ͬһ��λ�õĵ�Ԫ���Ϊ��ǰ��Ԫ��
	 */
	public Area toRightCell() {
		if (m_activeCell == null) {
			return null;
		}
		int row = m_activeCell.getRow();
		int col = m_activeCell.getCol();
		CellSetParser parser = new CellSetParser(cellSet);
		col++;
		if (col > contentView.cellSet.getColCount()) {
			return null;
		}
		while (!parser.isColVisible(col)) {
			col++;
			if (col > contentView.cellSet.getColCount()) {
				return null;
			}
		}

		if (col > contentView.cellSet.getColCount()) {
			return null;
		}
		updateCoords();
		return setActiveCell(new CellLocation(row, col));
	}

	/**
	 * �ڱ�����һ�е��"tab"����ĩ�к�����һ��ʱ�����±�����׵�����Ϳ��
	 */
	protected void updateCoords() {
		int cols = cellSet.getColCount() + 1;
		if (cellX == null || cols != cellX.length) {
			cellX = new int[cols];
			cellW = new int[cols];
		}
		CellSetParser parser = new CellSetParser(cellSet);
		for (int i = 1; i < cols; i++) {
			if (i == 1) {
				cellX[i] = 1;
			} else {
				cellX[i] = cellX[i - 1] + cellW[i - 1];
			}
			if (!parser.isColVisible(i)) {
				cellW[i] = 0;
			} else {
				cellW[i] = (int) cellSet.getColCell(i).getWidth();
			}
		}
	}

	/**
	 * ����ǰѡ��������չ��ָ�����򣬰�SHIFT+����ʱ����
	 * 
	 * @param region
	 *            Ҫ��չ��������
	 */
	public void selectToArea(Area region) {
		addSelectedArea(region, true);
		m_selectedCols.clear();
		m_selectedRows.clear();
		m_cornerSelected = false;
		fireRegionSelect(true);
		ControlUtils.scrollToVisible(getViewport(), this, region.getBeginRow(),
				region.getEndCol());
		repaint();
		contentView.requestFocus();
	}

	/**
	 * ����ǰѡ��������չ����ǰ������һ��ͬһ��λ��
	 * 
	 * @param tarPos
	 *            Ŀ���������
	 */
	public void selectToDownCell(CellLocation tarPos) {
		Area region = getSelectedArea(-1);
		int startRow = region.getBeginRow();
		int endRow = region.getEndRow();
		int startCol = region.getBeginCol();
		int endCol = region.getEndCol();
		int row = m_activeCell.getRow();
		CellSetParser parser = new CellSetParser(cellSet);
		if (tarPos != null) {
			int tarRow = tarPos.getRow();
			if (startRow < row) {
				if (tarRow > row) {
					startRow = row;
					endRow = tarRow;
				} else {
					startRow = tarRow;
				}
			} else {
				endRow = tarRow;
			}
		} else {
			if (startRow < row) {
				int nextRow = startRow;
				while (true) {
					int tempRow = nextRow;
					boolean hasCellCrossRows = false;
					if (nextRow > contentView.cellSet.getRowCount()) {
						break;
					}
					if (!hasCellCrossRows) {
						nextRow = tempRow + 1;
						if (nextRow > contentView.cellSet.getRowCount()) {
							break;
						}
						if (!parser.isRowVisible(nextRow))
							continue;
						break;
					}
				}
				if (nextRow > row) {
					nextRow = row;
					endRow++;
				}
				startRow = nextRow;
			} else {
				endRow++;
				if (endRow > contentView.cellSet.getRowCount())
					return;
				while (!parser.isRowVisible(endRow)) {
					endRow++;
					if (endRow > contentView.cellSet.getRowCount())
						return;
				}
			}
		}
		if (endRow > contentView.cellSet.getRowCount()
				|| !parser.isRowVisible(endRow)) {
			return;
		}
		region = new Area(startRow, startCol, endRow, endCol);
		addSelectedArea(region, true);
		m_selectedCols.clear();
		m_selectedRows.clear();
		m_cornerSelected = false;
		fireRegionSelect(true);
		ControlUtils.scrollToVisible(getViewport(), this, region.getEndRow(),
				region.getEndCol());
		repaint();
		contentView.requestFocus();
	}

	/**
	 * ����ǰѡ��������չ����ǰ������һ��ͬһ��λ��
	 * 
	 * @param tarPos
	 *            Ŀ���������
	 */
	void selectToUpCell(CellLocation tarPos) {
		Area region = getSelectedArea(-1);
		int startRow = region.getBeginRow();
		int endRow = region.getEndRow();
		int startCol = region.getBeginCol();
		int endCol = region.getEndCol();
		int row = m_activeCell.getRow();
		CellSetParser parser = new CellSetParser(cellSet);
		if (tarPos != null) {
			int tarRow = tarPos.getRow();
			if (endRow > row) {
				if (tarRow < row) {
					startRow = tarRow;
					endRow = row;
				} else {
					endRow = tarRow;
				}
			} else {
				startRow = tarRow;
			}
		} else {
			if (endRow > row) {
				int nextRow = endRow;
				while (true) {
					int tempRow = nextRow;
					boolean hasCellCrossRows = false;
					if (nextRow < 1) {
						break;
					}
					if (!hasCellCrossRows) {
						nextRow = tempRow - 1;
						if (nextRow < 1) {
							break;
						}
						if (!parser.isRowVisible(nextRow))
							continue;
						break;
					}
				}
				if (nextRow < row) {
					nextRow = row;
					startRow--;
				}
				endRow = nextRow;
			} else {
				startRow--;
				if (startRow < 1)
					return;
				while (!parser.isRowVisible(startRow)) {
					startRow--;
					if (startRow < 1)
						return;
				}
			}
		}
		if (startRow < 1 || !parser.isRowVisible(startRow)) {
			return;
		}
		region = new Area(startRow, startCol, endRow, endCol);

		addSelectedArea(region, true);
		m_selectedCols.clear();
		m_selectedRows.clear();
		m_cornerSelected = false;
		fireRegionSelect(true);
		ControlUtils.scrollToVisible(getViewport(), this, region.getBeginRow(),
				region.getEndCol());
		repaint();
		contentView.requestFocus();
	}

	/**
	 * ����ǰѡ��������չ����ǰ������һ��ͬһ��λ��
	 * 
	 * @param tarPos
	 *            Ŀ���������
	 */
	void selectToRightCell(CellLocation tarPos) {
		Area region = getSelectedArea(-1);
		int startRow = region.getBeginRow();
		int endRow = region.getEndRow();
		int startCol = region.getBeginCol();
		int endCol = region.getEndCol();
		int col = m_activeCell.getCol();
		if (tarPos != null) {
			int tarCol = tarPos.getCol();
			if (startCol < col) {
				if (tarCol > col) {
					startCol = col;
					endCol = tarCol;
				} else {
					startCol = tarCol;
				}
			} else {
				endCol = tarCol;
			}
		} else {
			if (startCol < col) {
				int nextCol = startCol;
				while (true) {
					int tempCol = nextCol;
					boolean hasCellCrossCols = false;
					if (nextCol > contentView.cellSet.getColCount()) {
						break;
					}
					if (!hasCellCrossCols) {
						nextCol = tempCol + 1;
						break;
					}
				}
				if (nextCol > col) {
					nextCol = col;
					endCol++;
				}
				startCol = nextCol;
			} else {
				endCol++;
			}
		}
		if (endCol > contentView.cellSet.getColCount()) {
			return;
		}
		region = new Area(startRow, startCol, endRow, endCol);

		addSelectedArea(region, true);
		m_selectedCols.clear();
		m_selectedRows.clear();
		m_cornerSelected = false;
		fireRegionSelect(true);
		ControlUtils.scrollToVisible(getViewport(), this, region.getEndRow(),
				region.getEndCol());
		repaint();
		contentView.requestFocus();
	}

	/**
	 * ����ǰѡ��������չ����ǰ������һ��ͬһ��λ��
	 * 
	 * @param tarPos
	 *            Ŀ���������
	 */
	void selectToLeftCell(CellLocation tarPos) {
		Area region = getSelectedArea(-1);
		int startRow = region.getBeginRow();
		int endRow = region.getEndRow();
		int startCol = region.getBeginCol();
		int endCol = region.getEndCol();
		int col = m_activeCell.getCol();
		if (tarPos != null) {
			int tarCol = tarPos.getCol();
			if (endCol > col) {
				if (tarCol < col) {
					startCol = tarCol;
					endCol = col;
				} else {
					endCol = tarCol;
				}
			} else {
				startCol = tarCol;
			}
		} else {
			if (endCol > col) {
				int nextCol = endCol;
				while (true) {
					int tempCol = nextCol;
					boolean hasCellCrossCols = false;
					if (nextCol < 1) {
						break;
					}
					if (!hasCellCrossCols) {
						nextCol = tempCol - 1;
						break;
					}
				}
				if (nextCol < col) {
					nextCol = col;
					startCol--;
				}
				endCol = nextCol;
			} else {
				startCol--;
			}
		}
		if (startCol < 1) {
			return;
		}
		region = new Area(startRow, startCol, endRow, endCol);

		addSelectedArea(region, true);
		m_selectedCols.clear();
		m_selectedRows.clear();
		m_cornerSelected = false;
		fireRegionSelect(true);
		ControlUtils.scrollToVisible(getViewport(), this, region.getBeginRow(),
				region.getBeginCol());
		repaint();
		contentView.requestFocus();
	}

	/**
	 * ����ѡ�����
	 * 
	 * @param c
	 *            �к�
	 */
	public void addSelectedCol(Integer c) {
		if (m_selectedCols.contains(c)) {
			return;
		}
		m_selectedCols.add(c);
	}

	/**
	 * ����ѡ�����
	 * 
	 * @param r
	 *            �к�
	 */
	public void addSelectedRow(Integer r) {
		if (m_selectedRows.contains(r)) {
			return;
		}
		m_selectedRows.add(r);
	}

	/**
	 * ���ѡ�������
	 */
	public void clearSelectedArea() {
		m_selectedAreas.clear();
	}

	/**
	 * ȡѡ�������
	 * 
	 * @return
	 */
	public Vector<Object> getSelectedAreas() {
		return m_selectedAreas;
	}

	/**
	 * ����ѡ�������
	 * 
	 * @param newAreas
	 *            ѡ�������
	 */
	public void setSelectedAreas(Vector<Object> newAreas) {
		m_selectedAreas = newAreas;
	}

	/**
	 * ȡ��ָ��������index<0ʱ��ʾȡ����Top����
	 * 
	 * @param index
	 *            int
	 * @return Area
	 */
	public Area getSelectedArea(int index) {
		if (m_selectedAreas.isEmpty()) {
			return null;
		}
		if (index < 0) {
			index = m_selectedAreas.size() - 1;
		}
		return (Area) m_selectedAreas.get(index);
	}

	/**
	 * ����ѡ�������
	 * 
	 * @param a
	 *            ѡ�������
	 */
	public void setSelectedArea(Area a) {
		if (a == null) {
			return;
		}
		clearSelectedArea();
		m_selectedAreas.add(a);
	}

	/**
	 * ȫѡ��������
	 */
	public void selectAll() {
		m_cornerSelected = true;
		int rows = cellSet.getRowCount();
		int cols = (int) cellSet.getColCount();

		m_selectedCols.clear();
		for (int i = 1; i <= cols; i++) {
			m_selectedCols.add(new Integer(i));
		}
		m_selectedRows.clear();
		for (int i = 1; i <= rows; i++) {
			// conrol��add�������ж��з��ظ���,�ж�ʱ�ܷ�ʱ��
			m_selectedRows.add(new Integer(i));
		}
		setSelectedArea(new Area(1, (int) 1, rows, cols));
		repaint();
		fireRegionSelect(true);
	}

	/**
	 * ����ѡ�������
	 * 
	 * @param a
	 *            ѡ�������
	 * @param removeLast
	 *            �Ƿ�ɾ����һ��ѡ�������
	 */
	public void addSelectedArea(Area a, boolean removeLast) {
		if (a == null || m_selectedAreas.contains(a)) {
			return;
		}
		if (removeLast && !m_selectedAreas.isEmpty()) {
			m_selectedAreas.remove(m_selectedAreas.size() - 1);
		}
		m_selectedAreas.add(a);
	}

	/**
	 * ���ɲ����ƿؼ�
	 */
	public void draw() {
		JPanel corner = createCorner();
		if (corner != null) {
			this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, corner);
		}

		colHeaderView = createColHeaderView();
		if (colHeaderView != null) {
			JViewport colHeader = new JViewport();
			this.setColumnHeader(colHeader);
			this.setColumnHeaderView(colHeaderView);
		}
		rowHeaderView = createRowHeaderView();
		if (rowHeaderView != null) {
			JViewport rowHeader = new JViewport();
			this.setRowHeader(rowHeader);
			this.setRowHeaderView(rowHeaderView);
		}
		contentView = createContentView();
		if (contentView != null) {
			this.getViewport().setView(contentView);
			this.getViewport().setAutoscrolls(true);
		}
	}

	/**
	 * ������ָ������
	 * 
	 * @param newArea
	 *            Ҫ��ʾ������
	 */
	public void scrollToArea(Area newArea) {
		if (newArea == null) {
			return;
		}
		setSelectedArea(newArea);
		if (ControlUtils.scrollToVisible(getViewport(), this,
				newArea.getBeginRow(), newArea.getBeginCol())) {
			ContentPanel cp = getContentPanel();
			JScrollBar hBar = getHorizontalScrollBar();
			JScrollBar vBar = getVerticalScrollBar();

			hBar.setValue(cp.getColOffset(newArea.getBeginCol(), scale));
			vBar.setValue(cp.getRowOffset(newArea.getBeginRow(), scale));

		}
		fireRegionSelect(true);
	}

	/**
	 * ������������Ͻ����
	 * 
	 * @return
	 */
	abstract JPanel createCorner();

	/**
	 * ����������ϱ�ͷ���
	 * 
	 * @return
	 */
	abstract JPanel createColHeaderView();

	/**
	 * ������������ͷ���
	 * 
	 * @return
	 */
	abstract JPanel createRowHeaderView();

	/** ���������������� */
	abstract ContentPanel createContentView();

	/**
	 * ��������
	 * 
	 * @param cellSet
	 *            �������
	 */
	public void setCellSet(PgmCellSet cellSet) {
		this.cellSet = cellSet;
		scale = 1.0f;
		ByteMap bm = cellSet.getCustomPropMap();
		if (bm != null) {
			Object scaleObj = bm.get(GC.CELLSET_SCALE);
			if (scaleObj != null) {
				scale = ((Number) scaleObj).floatValue();
			}
		}
		draw();
	}

	/**
	 * ����������
	 * 
	 * @return �������
	 */
	public PgmCellSet getCellSet() {
		return cellSet;
	}

	/**
	 * ����������ӿ�
	 */
	public ICellSet getICellSet() {
		return cellSet;
	}

	/**
	 * �������༭�¼�������
	 * 
	 * @param listener
	 *            ������ʵ��
	 */
	public void addEditorListener(EditorListener listener) {
		this.m_editorListener.add(listener);
	}

	/**
	 * �����иߵ�����Ϣ
	 * 
	 * @param vectHeader
	 *            �б�ż���
	 * @param newHeight
	 *            �µ��и�ֵ
	 */
	void fireRowHeaderResized(Vector<Integer> vectHeader, float newHeight) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.rowHeightChange(vectHeader, newHeight);
		}
		Point hp = this.getRowHeader().getViewPosition();
		Point p = this.getViewport().getViewPosition();
		this.getRowHeader().setView(
				this.rowHeaderView == null ? this.createRowHeaderView()
						: this.rowHeaderView);
		this.getRowHeader().setViewPosition(hp);
		this.getViewport().setViewPosition(p);
		contentView.requestFocus();
		repaint();
	}

	/**
	 * �����п������Ϣ
	 * 
	 * @param vectHeader
	 *            �б�ż���
	 * @param newWidth
	 *            �µ��п�ֵ
	 */
	void fireColHeaderResized(Vector<Integer> vectHeader, float newWidth) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.columnWidthChange(vectHeader, newWidth);
		}
		Point hp = this.getColumnHeader().getViewPosition();
		Point p = this.getViewport().getViewPosition();
		this.getColumnHeader().setView(
				this.colHeaderView == null ? this.createColHeaderView()
						: this.colHeaderView);
		this.getColumnHeader().setViewPosition(hp);
		this.getViewport().setViewPosition(p);
		contentView.requestFocus();

		repaint();
	}

	/**
	 * ���������ƶ���Ϣ��δʵ��
	 * 
	 * @return
	 */
	void fireRegionMove() {
	}

	/**
	 * ��������ճ����Ϣ��δʵ��
	 * 
	 * @return
	 */
	void fireRegionPaste() {
	}

	/**
	 * ��������ѡ����Ϣ
	 * 
	 * @param keyEvent
	 *            boolean �����¼�����������ֵˢ��
	 */
	void fireRegionSelect(boolean keyEvent) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.regionsSelect(m_selectedAreas, this.m_selectedRows,
					this.m_selectedCols, this.m_cornerSelected, keyEvent);
		}
	}

	/**
	 * ������Ԫ���ı�ֵ�༭������Ϣ
	 * 
	 * @param pos
	 *            ���༭�ĵ�Ԫ��λ��
	 * @param text
	 *            ��������ı�
	 */
	public void fireCellTextInput(CellLocation pos, String text) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.cellTextInput(pos.getRow(), pos.getCol(), text);
		}
	}

	/**
	 * ������Ԫ���ı������༭��Ϣ
	 * 
	 * @param text
	 *            ���ڱ��༭���ı�
	 */
	void fireEditorInputing(String text) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.editorInputing(text);
		}
	}

	/**
	 * ����ƶ��¼�
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 */
	void fireMouseMove(int row, int col) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.mouseMove(row, col);
		}
	}

	/**
	 * ��������һ��¼�
	 * 
	 * @param e
	 *            ����¼�
	 * @param clickPlace
	 *            �һ�λ�ã�GC�ж���ĳ���
	 */
	void fireRightClicked(MouseEvent e, int clickPlace) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.rightClicked(e, clickPlace);
		}
	}

	/**
	 * �������˫���¼�
	 * 
	 * @param e
	 */
	void fireDoubleClicked(MouseEvent e) {
		for (int i = 0; i < this.m_editorListener.size(); i++) {
			EditorListener listener = (EditorListener) this.m_editorListener
					.get(i);
			listener.doubleClicked(e);
		}
	}

	/**
	 * ������
	 * 
	 * @param col
	 *            ����λ��
	 * @param count
	 *            ���������
	 */
	public List<NormalCell> insertColumn(int col, int count) {
		if (col > cellSet.getColCount() || col < 0) {
			col = cellSet.getColCount();
		}
		List<NormalCell> cells = insertCol(cellSet, col, count);
		resetControlWidth();
		return cells;
	}

	/**
	 * ������
	 * 
	 * @param cellSet
	 * @param col
	 * @param count
	 */
	protected List<NormalCell> insertCol(PgmCellSet cellSet, int col, int count) {
		return cellSet.insertCol(col, count);
	}

	/**
	 * ׷����
	 * 
	 * @param count
	 *            ׷�ӵ�����
	 */
	public void addColumn(int count) {
		addCol(cellSet, count);
		resetControlWidth();
	}

	/**
	 * ׷����
	 * 
	 * @param cellSet
	 * @param count
	 */
	protected void addCol(PgmCellSet cellSet, int count) {
		cellSet.addCol(count);
	}

	/**
	 * �������С��ı��п�ʱ������ؼ����
	 */
	protected void resetControlWidth() {
		Point hp = this.getColumnHeader().getViewPosition();
		Point p = this.getViewport().getViewPosition();
		this.getColumnHeader().setView(this.createColHeaderView());
		this.getColumnHeader().setViewPosition(hp);
		this.getViewport().setViewPosition(p);
		contentView.requestFocus();
		repaint();
	}

	/**
	 * �������С��ı��и�ʱ������ؼ��߶�
	 */
	protected void resetControlHeight() {
		Point hp = this.getRowHeader().getViewPosition();
		Point p = this.getViewport().getViewPosition();
		this.getRowHeader().setView(this.createRowHeaderView());
		this.getRowHeader().setViewPosition(hp);
		this.getViewport().setViewPosition(p);
		contentView.requestFocus();
		repaint();
	}

	/**
	 * ɾ����
	 * 
	 * @param col
	 *            ɾ��λ��
	 * @param count
	 *            ɾ��������
	 */
	public List<NormalCell> removeColumn(int col, int count) {
		List<NormalCell> adjustCells = null;
		if (col <= cellSet.getColCount() && col > 0) {
			adjustCells = removeCol(cellSet, col, count);
			if (getActiveCell() != null) {
				int activeCellStartCol = getActiveCell().getCol();
				if (col <= activeCellStartCol) {
					activeCellStartCol -= count;
					if (activeCellStartCol < 1) {
						activeCellStartCol = 1;
					}
					getActiveCell().setCol(activeCellStartCol);
				}
			}
		}
		return adjustCells;
	}

	/**
	 * ɾ����
	 * 
	 * @param cellSet
	 * @param col
	 * @param count
	 * @return
	 */
	protected List<NormalCell> removeCol(PgmCellSet cellSet, int col, int count) {
		return cellSet.removeCol(col, count);
	}

	/**
	 * ������
	 * 
	 * @param row
	 *            ��λ��
	 * @param count
	 *            ���������
	 */
	public List<NormalCell> insertRow(int row, int count) {
		if (row > cellSet.getRowCount() || row < 0) {
			row = cellSet.getRowCount();
		}
		List<NormalCell> cells = insertRow(cellSet, row, count);
		resetControlHeight();
		return cells;
	}

	/**
	 * ������
	 * 
	 * @param cellSet
	 * @param row
	 * @param count
	 */
	protected List<NormalCell> insertRow(PgmCellSet cellSet, int row, int count) {
		return cellSet.insertRow(row, count);
	}

	/**
	 * ������ʾ�ı���
	 * 
	 * @param ratio
	 *            �ٷֱȵ�����
	 */
	public void setDisplayScale(int ratio) {
		Point p = this.getViewport().getViewPosition();
		p.x = (int) (p.x / scale);
		p.y = (int) (p.y / scale);
		this.scale = ratio / 100f;
		this.getColumnHeader().setView(this.createColHeaderView());
		this.getRowHeader().setView(this.createRowHeaderView());
		contentView = this.createContentView();
		this.getViewport().setView(contentView);
		p.x = (int) (p.x * scale);
		p.y = (int) (p.y * scale);
		this.getViewport().setViewPosition(p);
		this.getColumnHeader().setViewPosition(new Point(p.x, 0));
		this.getRowHeader().setViewPosition(new Point(0, p.y));
		repaint();
	}

	/**
	 * ȡ��ʾ�ı���
	 * 
	 * @return
	 */
	public float getDisplayScale() {
		return scale;
	}

	/**
	 * ׷����
	 * 
	 * @param count
	 *            ׷�ӵ�����
	 */
	public void addRow(int count) {
		addRow(cellSet, count);
		resetControlHeight();
	}

	/**
	 * ׷����
	 * 
	 * @param cellSet
	 * @param count
	 */
	protected void addRow(PgmCellSet cellSet, int count) {
		cellSet.addRow(count);
	}

	/**
	 * ɾ����
	 * 
	 * @param row
	 *            ��λ��
	 * @param count
	 *            ɾ��������
	 */
	public List<NormalCell> removeRow(int row, int count) {
		List<NormalCell> adjustCells = null;
		if (row <= cellSet.getRowCount() && row > 0) {
			adjustCells = removeRow(cellSet, row, count);
			if (getActiveCell() != null) {
				int activeCellStartRow = getActiveCell().getRow();
				if (row <= activeCellStartRow) {
					activeCellStartRow -= count;
					if (activeCellStartRow < 1) {
						activeCellStartRow = 1;
					}
					getActiveCell().setRow(activeCellStartRow);
				}
			}
		}
		return adjustCells;
	}

	/**
	 * ɾ����
	 * 
	 * @param cellSet
	 * @param row
	 * @param count
	 * @return
	 */
	protected List<NormalCell> removeRow(PgmCellSet cellSet, int row, int count) {
		return cellSet.removeRow(row, count);
	}

	/**
	 * ɾ�����к����������������
	 */
	public void clearSelectedAreas() {
		clearSelectedArea();
		m_selectedRows.clear();
		m_selectedCols.clear();
		fireRegionSelect(false);
	}

	/**
	 * ��ÿؼ��е�����༭��
	 */
	public JTextComponent getEditor() {
		if (contentView == null) {
			return null;
		}
		JComponent textEditor = contentView.getEditor();
		if (textEditor == null || !(textEditor instanceof JTextComponent)) {
			return null;
		}
		return (JTextComponent) textEditor;
	}

	/**
	 * ��������ƥ�䵽�ĸ���
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param searchInSelectedCells
	 *            �Ƿ���ѡ��������������
	 */
	public void setSearchedCell(int row, int col, boolean searchInSelectedCells) {
		setActiveCell(new CellLocation(row, col));
		ControlUtils.scrollToVisible(this.getViewport(), this, row, col);
		if (!searchInSelectedCells) {
			setSelectedArea(new Area(row, col, row, col));
			this.fireRegionSelect(true);
		}
		this.repaint();
	}

	/**
	 * �رտؼ�
	 */
	public void dispose() {
		cellSet = null;
		m_selectedAreas = null;
		m_selectedCols = null;
		m_selectedRows = null;
		m_editorListener = null;
		if (contentView != null) {
			contentView.dispose();
		}
		cellX = null;
		cellY = null;
		cellW = null;
		cellH = null;
	}

	public void setScale(float newScale) {
		float oldScale = scale;
		// Boolean isEditable = null;
		// if (contentView != null) {
		// isEditable = contentView.isEditable();
		// }
		this.scale = newScale;

		// drawʱ�����´���������壬�ĳ�ˢ��
		// draw();
		if (colHeaderView != null) {
			Point pCol = getViewport().getViewPosition();
			pCol.setLocation(pCol.getX() * scale / oldScale, pCol.getY()
					* scale / oldScale);
			this.setColumnHeaderView(colHeaderView);
			this.getColumnHeader().setViewPosition(pCol);
		}
		if (rowHeaderView != null) {
			Point pRow = getViewport().getViewPosition();
			pRow.setLocation(pRow.getX() * scale / oldScale, pRow.getY()
					* scale / oldScale);
			this.setRowHeaderView(rowHeaderView);
			this.getRowHeader().setViewPosition(pRow);
		}
		if (contentView != null) {
			Point pContent = getViewport().getViewPosition();
			pContent.setLocation(pContent.getX() * scale / oldScale,
					pContent.getY() * scale / oldScale);
			this.getViewport().setView(contentView);
			this.getViewport().setViewPosition(pContent);
		}

		// if (isEditable != null)
		// contentView.setEditable(isEditable);
		ByteMap bm = this.cellSet.getCustomPropMap();
		if (bm == null) {
			bm = new ByteMap();
			this.cellSet.setCustomPropMap(bm);
		}
		bm.put(GC.CELLSET_SCALE, scale);
	}

}
