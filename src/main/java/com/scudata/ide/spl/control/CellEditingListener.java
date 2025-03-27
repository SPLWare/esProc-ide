package com.scudata.ide.spl.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import com.scudata.cellset.datamodel.CellSet;
import com.scudata.cellset.datamodel.NormalCell;
import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.CellLocation;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.ToolBarPropertyBase;
import com.scudata.ide.spl.GCSpl;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.SPL;
import com.scudata.ide.spl.SheetSpl;

/**
 * �༭ʱ���ڵ�ǰ��Ԫ���ý���ʱ�ļ��̼�������
 */
public class CellEditingListener implements KeyListener {
	/** �༭�ؼ� */
	protected SplControl control;

	/** ������� */
	protected ContentPanel cp;

	/**
	 * �Ƿ���CTRL��
	 */
	protected boolean isCtrlDown = false;

	/**
	 * ���������캯��
	 * 
	 * @param control �༭�ؼ�
	 * @param panel   �������
	 */
	public CellEditingListener(SplControl control, ContentPanel panel) {
		this.control = control;
		this.cp = panel;
	}

	/**
	 * �����ͷ�ʱ����༭��������������ı�
	 * 
	 * @param e �����¼�
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isCtrlDown = false;
		}
	}

	/**
	 * ��������ʱ�Ĵ�������������µ������¼����س�����Ctrl+���Ҽ�����Ӧ�ı䵱ǰ��Ԫ��
	 * 
	 * @param e �����¼�
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		boolean isMatching = isMatching();
		switch (key) {
		case KeyEvent.VK_ENTER:
			if (!GV.isCellEditing) {// ���Թ���������
				stopMatch();
				return;
			}
			if (e.isAltDown()) {
				stopMatch();
				if (GVSpl.appSheet != null
						&& GVSpl.appSheet instanceof SheetSpl)
					((SheetSpl) GVSpl.appSheet).calcActiveCell(false);
				break;
			} else if (e.isControlDown()) {
				stopMatch();
				JTextComponent ta = getSource(e);
				int c = ta.getCaretPosition();
				try {
					String head = ta.getText(0, c);
					String end = ta.getText(c, ta.getText().length() - c);
					cp.setEditorText(head + "\n" + end);
					ta.requestFocus();
					ta.setCaretPosition(c + 1);
				} catch (BadLocationException ex) {
				} catch (IllegalArgumentException ex1) {
				}
			} else if (e.isShiftDown()) {
				stopMatch();
				// �������ݡ�ִ�У������Ƿ���ʵ�ʸĶ�������ʾ������Զ���ס�����ƶ����
				if (GVSpl.appSheet != null
						&& GVSpl.appSheet instanceof SheetSpl)
					((SheetSpl) GVSpl.appSheet).calcActiveCell();
				break;
			} else {
				if (isMatching) {
					keyPressed(key);
					break;
				}
				CellSetParser parser = new CellSetParser(control.cellSet);
				PgmNormalCell cell;
				int nextCol = -1;
				CellLocation cl = control.getActiveCell();
				if (cl == null)
					return;
				int curRow = cl.getRow();
				int curCol = cl.getCol();
				for (int c = curCol + 1; c <= control.cellSet.getColCount(); c++) {
					if (parser.isColVisible(c)) {
						cell = control.cellSet.getPgmNormalCell(curRow, c);
						if (!cell.isNoteBlock() && !cell.isNoteCell()) {
							if (StringUtils.isValidString(parser.getDispText(
									curRow, c))) {
								nextCol = c;
								break;
							}
						}
					}
				}
				if (nextCol > 0) {
					control.getContentPanel().submitEditor();
					control.getContentPanel().revalidate();
					control.scrollToArea(control.setActiveCell(
							new CellLocation(curRow, nextCol), true));
				} else {
					if (control.getActiveCell() != null) {
						CellSet ics = control.getCellSet();
						if (curRow == ics.getRowCount()) {
							control.getContentPanel().submitEditor();
							control.getContentPanel().revalidate();
							appendOneRow();
						}
					}
					control.scrollToArea(control.toDownCell());
				}
				if (GVSpl.panelValue != null)
					GVSpl.panelValue.tableValue.setLocked(false);
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if (control.getActiveCell() == null) {
				break;
			}
			if (isMatching) {
				stopMatch();
			}
			JTextComponent ta = getSource(e);
			NormalCell nc = (NormalCell) control.getCellSet().getCell(
					control.getActiveCell().getRow(),
					control.getActiveCell().getCol());
			String value = nc.getExpString();
			value = value == null ? GCSpl.NULL : value;
			ta.setText(value);
			control.getContentPanel().reloadEditorText();
			control.fireEditorInputing(value);
			cp.requestFocus();
			break;
		case KeyEvent.VK_TAB:
			if (e.isShiftDown()) {
				if (isMatching) {
					stopMatch();
				}
				control.scrollToArea(control.toLeftCell());
			} else if (e.isControlDown()) {
				// CTRL-TAB���ͳ��л���ǰ�SHEET������EXCEL��
				if (isMatching) {
					stopMatch();
				}
				if (GVSpl.appFrame != null && GVSpl.appFrame instanceof SPL)
					((SPL) GVSpl.appFrame).showNextSheet(isCtrlDown);
				isCtrlDown = true;
			} else {
				if (isMatching) {
					break;
				}
				if (control.getActiveCell() != null) {
					control.getContentPanel().submitEditor();
				}
				int curCol = control.getActiveCell().getCol();
				CellSet ics = control.getCellSet();
				if (curCol == ics.getColCount()) {
					control.getContentPanel().submitEditor();
					appendOneCol();
				}
				control.scrollToArea(control.toRightCell());
			}
			break;
		case KeyEvent.VK_RIGHT:
			JTextComponent tar = getSource(e);
			if (tar.getText() == null || tar.getText().equals("")) {
				control.scrollToArea(control.toRightCell());
			} else {
				return;
			}
			break;
		case KeyEvent.VK_LEFT:
			JTextComponent tal = getSource(e);
			if (tal.getText() == null || tal.getText().equals("")) {
				control.scrollToArea(control.toLeftCell());
			} else {
				return;
			}
			break;
		case KeyEvent.VK_UP:
			if (isMatching) {
				keyPressed(key);
				break;
			}
			if (e.isAltDown()) {
				if (e.getSource() == GV.toolBarProperty.getWindowEditor()) {// ���Թ���������
					e.consume();
				}
				return;
			}
			JTextComponent tau = getSource(e);
			if (tau.getText() == null || tau.getText().equals("")) {
				control.scrollToArea(control.toUpCell());
			} else {
				return;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (isMatching) {
				keyPressed(key);
				break;
			}
			if (e.isAltDown()) {
				if (e.getSource() == GV.toolBarProperty.getWindowEditor()) {// ���Թ���������
					e.consume();
				}
				return;
			}
			JTextComponent tad = getSource(e);
			if (tad.getText() == null || tad.getText().equals("")) {
				control.scrollToArea(control.toDownCell());
				// } else if (e.isShiftDown()) {
				// startMatch(tad);
				// return;
			} else {
				return;
			}
			break;
		case KeyEvent.VK_F2: {
			if (control.getActiveCell() == null) {
				break;
			}
			if (isMatching) {
				stopMatch();
			}
			if (GVSpl.toolBarProperty != null)
				GVSpl.toolBarProperty.getWindowEditor().requestFocus();
			break;
		}
		case KeyEvent.VK_Z: {
			if (e.isControlDown() && control.getActiveCell() != null) {
				if (isMatching) {
					stopMatch();
				}
				control.getContentPanel().undoEditor();
			}
			break;
		}
		default:
			SplEditor editor = ControlUtils.extractSplEditor(control);
			if (e.getKeyCode() == KeyEvent.VK_C && e.isAltDown()
					&& !e.isControlDown()) {
				if (e.isShiftDown()) {
					if (editor != null && editor.canCopyPresent()) {
						editor.copyPresent();
						break;
					}
				} else {
					if (editor != null)
						editor.altC();
					break;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_V && e.isAltDown()
					&& !e.isControlDown()) {
				if (!e.isShiftDown()) {
					if (GM.canPaste())
						if (editor != null)
							editor.altV();
					break;
				}
			}
			return;
		}
		e.consume();
	}

	/**
	 * ���̰����¼�
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * ȡԴ���
	 * 
	 * @param e
	 * @return
	 */
	protected JTextComponent getSource(KeyEvent e) {
		Object src = e.getSource();
		if (src instanceof JTextComponent) {
			return (JTextComponent) src;
		}
		if (src instanceof ToolBarPropertyBase) {
			return ((ToolBarPropertyBase) src).getWindowEditor();
		}
		return null;
	}

	protected boolean isMatching() {
		return false;
	}

	protected void keyPressed(int keyCode) {
	}

	protected void stopMatch() {
	}

	protected void appendOneRow() {
		SplEditor editor = ControlUtils.extractSplEditor(control);
		if (editor != null)
			editor.appendRows(1);
	}

	protected void appendOneCol() {
		SplEditor editor = ControlUtils.extractSplEditor(control);
		if (editor != null)
			editor.appendCols(1);
	}
}
