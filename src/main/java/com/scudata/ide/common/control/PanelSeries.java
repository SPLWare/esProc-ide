package com.scudata.ide.common.control;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.dm.Sequence;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JTableEx;

/**
 * �����б༭���
 *
 */
public class PanelSeries extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * �����
	 */
	private final int COL_INDEX = 0;
	/**
	 * ֵ��
	 */
	private final int COL_VALUE = 1;
	/**
	 * ������ֵ�ı����
	 */
	private JTableEx tableSeq = new JTableEx(
			mm.getMessage("panelseries.tableparam")) { // ���,ֵ
		private static final long serialVersionUID = 1L;

		/**
		 * ˫�������ı��Ի���༭ֵ
		 */
		public void doubleClicked(int xpos, int ypos, int row, int col,
				MouseEvent e) {
			GM.dialogEditTableText(parent, tableSeq, row, col);
		}

		/**
		 * ֵ�ύʱת������Ӧ�Ķ���
		 */
		public void setValueAt(Object aValue, int row, int column) {
			if (!isItemDataChanged(row, column, aValue)) {
				return;
			}
			super.setValueAt(aValue, row, column);
			if (preventChange) {
				return;
			}
			try {
				if (StringUtils.isValidString(aValue)) {
					aValue = PgmNormalCell.parseConstValue((String) aValue);
				}
				seq.set(row + 1, aValue);
			} catch (Exception e) {
				GM.showException(parent, e);
				return;
			}
		}
	};

	/**
	 * �����ж���
	 */
	private Sequence seq;

	/**
	 * �Ƿ���ֹ�仯
	 */
	private boolean preventChange = false;

	/**
	 * �����
	 */
	private Component parent;

	/**
	 * ���캯��
	 */
	public PanelSeries(Component parent) {
		try {
			this.parent = parent;
			rqInit();
			initTable();
		} catch (Exception ex) {
			GM.showException(parent, ex);
		}
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void rqInit() throws Exception {
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(tableSeq), BorderLayout.CENTER);
	}

	/**
	 * ���ó�������
	 * 
	 * @param param
	 */
	public void setSequence(Sequence seq) {
		if (seq == null) {
			tableSeq.data.setRowCount(0);
			this.seq = new Sequence();
		} else {
			this.seq = seq;
			preventChange = true;
			refresh();
			preventChange = false;
		}
	}

	/**
	 * ȡ��������
	 * 
	 * @return
	 */
	public Sequence getSequence() {
		return seq;
	}

	/**
	 * ˢ��
	 */
	private void refresh() {
		tableSeq.removeAllRows();
		tableSeq.data.setRowCount(0);
		int rowCount = seq.length();
		if (rowCount < 1) {
			return;
		}
		for (int i = 1; i <= rowCount; i++) {
			int r = tableSeq.addRow();
			Object value = seq.get(i);
			tableSeq.data.setValueAt(value, r, COL_VALUE);
		}
	}

	/**
	 * ȫѡ
	 */
	public void selectAll() {
		tableSeq.acceptText();
		tableSeq.selectAll();
	}

	/**
	 * ������
	 */
	public void rowUp() {
		tableSeq.acceptText();
		int row = tableSeq.getSelectedRow();
		if (row < 0) {
			return;
		}
		tableSeq.shiftRowUp(row);
	}

	/**
	 * ������
	 */
	public void rowDown() {
		tableSeq.acceptText();
		int row = tableSeq.getSelectedRow();
		if (row < 0) {
			return;
		}
		tableSeq.shiftRowDown(row);
	}

	/**
	 * ������
	 */
	public void addRow() {
		tableSeq.acceptText();
		seq.add(null);
		refresh();
	}

	/**
	 * ������
	 */
	public void insertRow() {
		tableSeq.acceptText();
		int row = tableSeq.getSelectedRow();
		if (row < 0) {
			return;
		}
		seq.insert(row + 1, null);
		refresh();
		tableSeq.setRowSelectionInterval(row, row);
	}

	/**
	 * �������
	 * 
	 * @return
	 */
	public boolean checkData() {
		tableSeq.acceptText();
		return true;
	}

	/**
	 * �����ݸ��Ƶ�������
	 */
	public void clipBoard() {
		String blockData = tableSeq.getBlockData();
		GM.clipBoard(blockData);
	}

	/**
	 * ɾ��ѡ�е���
	 */
	public void deleteRows() {
		tableSeq.acceptText();
		int rows[] = tableSeq.getSelectedRows();
		if (rows.length == 0) {
			return;
		}
		for (int i = rows.length - 1; i >= 0; i--) {
			seq.delete(rows[i] + 1);
		}
		refresh();
	}

	/**
	 * ��ʼ����ؼ�
	 */
	private void initTable() {
		preventChange = true;
		tableSeq.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSeq.setRowHeight(20);
		tableSeq.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSeq.getTableHeader().setReorderingAllowed(false);
		tableSeq.setClickCountToStart(1);
		tableSeq.setIndexCol(COL_INDEX);
		tableSeq.setColumnWidth(COL_VALUE, 250);
		preventChange = false;
	}
}
