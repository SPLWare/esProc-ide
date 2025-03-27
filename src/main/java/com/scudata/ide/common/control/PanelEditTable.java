package com.scudata.ide.common.control;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;

import com.scudata.app.common.Section;
import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.dm.BaseRecord;
import com.scudata.dm.Context;
import com.scudata.dm.DataStruct;
import com.scudata.dm.Param;
import com.scudata.dm.Table;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.util.Variant;

/**
 * �����༭���
 */
public class PanelEditTable extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * ����ҳ
	 */
	private final byte TAB_NORMAL = 0;
	/**
	 * ����ҳ
	 */
	private final byte TAB_DATA = 1;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/** ����� */
	private final byte COL_INDEX = 0;
	/** ������ */
	private final byte COL_NAME = 1;
	/** ������ */
	private final byte COL_PK = 2;

	/** ����б��� */
	private final String STR_INDEX = mm.getMessage("paneledittable.index");

	/**
	 * �����������,����,����
	 */
	private JTableEx tableNormal = new JTableEx(
			mm.getMessage("paneledittable.tablenormal")) {
		private static final long serialVersionUID = 1L;

		/**
		 * ˫�������񣬵����ı��༭�Ի���
		 */
		public void doubleClicked(int xpos, int ypos, int row, int col,
				MouseEvent e) {
			switch (col) {
			case COL_NAME:
				GM.dialogEditTableText(parent, tableNormal, row, col);
				break;
			}
		}

		/**
		 * �ύ����ʱ����ṹ�仯
		 */
		public void setValueAt(Object aValue, int row, int column) {
			if (!isItemDataChanged(row, column, aValue)) {
				return;
			}
			String oldName = null;
			if (column == COL_NAME && getValueAt(row, column) != null) {
				oldName = (String) data.getValueAt(row, column);
			}

			if (preventChange) {
				super.setValueAt(aValue, row, column);
				return;
			}
			if (column == COL_NAME) {
				String newName = aValue == null ? null : (String) aValue;
				if (!alterTable(newName, oldName)) {
					return;
				}
				super.setValueAt(aValue, row, column);
				tableStructChanged();
			} else {
				super.setValueAt(aValue, row, column);
			}
		}
	};

	/**
	 * JTabbedPane����
	 */
	private JTabbedPane jTabMain = new JTabbedPane();

	/**
	 * ���ݱ����
	 */
	private JTableEx tableData;
	/**
	 * �Ƿ���ֹ�仯
	 */
	private boolean preventChange = false;

	/**
	 * ��������
	 */
	private Param param;

	/**
	 * ��������
	 */
	private Table table;
	
	/**
	 * �����
	 */
	private Component parent;

	/**
	 * ���캯��
	 * 
	 * @param param
	 *            ��������
	 */
	public PanelEditTable(Component parent, Param param) {
		try {
			preventChange = true;
			this.parent = parent;
			this.param = param;
			rqInit();
			this.table = (Table) param.getValue();
			initConstTable();
		} catch (Exception ex) {
			GM.showException(parent, ex);
		} finally {
			preventChange = false;
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public Param getParam() {
		table.dataStruct().setPrimary(getPrimary());
		param.setValue(table);
		return param;
	}

	/**
	 * �޸�����
	 * 
	 * @param newName
	 *            ������
	 * @param oldName
	 *            ������
	 * @return
	 */
	private boolean alterTable(String newName, String oldName) {
		return alterTable(newName, oldName, -1);
	}

	/**
	 * �޸�����
	 * 
	 * @param newName
	 *            ������
	 * @param oldName
	 *            ������
	 * @param row
	 *            �к�
	 * @return
	 */
	private boolean alterTable(String newName, String oldName, int row) {
		if (!StringUtils.isValidString(newName)
				&& !StringUtils.isValidString(oldName)) {
			return false;
		}
		int rowCount = tableNormal.getRowCount();
		if (rowCount < 0) {
			return false;
		}
		if (StringUtils.isValidString(newName)
				&& (table == null || table.dataStruct() == null || table
						.dataStruct().getFieldCount() == 0)) {
			table = new Table(new String[] { newName });
			return true;
		}
		String oldNames[];
		String newNames[];
		String nNames[] = table.dataStruct().getFieldNames();
		if (StringUtils.isValidString(oldName)
				&& StringUtils.isValidString(newName)) { // rename
			oldNames = new String[nNames.length];
			newNames = new String[nNames.length];
			int index = -1;
			for (int i = 0; i < nNames.length; i++) {
				if (oldName.equals(nNames[i])) {
					index = i;
					break;
				}
			}
			System.arraycopy(nNames, 0, oldNames, 0, nNames.length);
			System.arraycopy(nNames, 0, newNames, 0, nNames.length);
			if (index > -1) {
				newNames[index] = newName;
			}
		} else if (StringUtils.isValidString(oldName)) { // delete
			oldNames = new String[nNames.length - 1];
			newNames = new String[nNames.length - 1];
			int index = -1;
			for (int i = 0; i < nNames.length; i++) {
				if (oldName.equals(nNames[i])) {
					index = i;
					break;
				}
			}
			if (index > 0) {
				System.arraycopy(nNames, 0, oldNames, 0, index);
				System.arraycopy(nNames, 0, newNames, 0, index);
			}
			if (index != nNames.length - 1) {
				System.arraycopy(nNames, index + 1, oldNames, index,
						nNames.length - 1 - index);
				System.arraycopy(nNames, index + 1, newNames, index,
						nNames.length - 1 - index);
			}
		} else {
			oldNames = new String[nNames.length + 1];
			newNames = new String[oldNames.length];
			if (row >= 0) { // insert
				if (row > 0) {
					System.arraycopy(nNames, 0, oldNames, 0, row);
					System.arraycopy(nNames, 0, newNames, 0, row);
				}
				newNames[row] = newName;
				System.arraycopy(nNames, row, oldNames, row + 1,
						oldNames.length - row - 1);
				System.arraycopy(nNames, row, newNames, row + 1,
						oldNames.length - row - 1);
			} else { // add
				System.arraycopy(nNames, 0, oldNames, 0, nNames.length);
				System.arraycopy(nNames, 0, newNames, 0, nNames.length);
				newNames[nNames.length] = newName;
			}
		}
		try {
			table.alter(newNames, oldNames);
		} catch (Exception e) {
			GM.showException(parent, e);
			return false;
		}
		return true;
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	void rqInit() throws Exception {
		JPanel panelNormal = new JPanel(new GridBagLayout());
		jTabMain.addChangeListener(new PanelEditTable_this_changeAdapter(this));
		panelNormal.add(new JScrollPane(tableNormal),
				GM.getGBC(0, 0, true, true));

		jTabMain.add(panelNormal, mm.getMessage("paneledittable.normal")); // ����

		tableData = new JTableEx() {
			private static final long serialVersionUID = 1L;

			public void doubleClicked(int xpos, int ypos, int row, int col,
					MouseEvent e) {
				if (col <= COL_INDEX) {
					return;
				}
				GM.dialogEditTableText(parent, tableData, row, col);
			}

			public void setValueAt(Object aValue, int row, int column) {
				if (!isItemDataChanged(row, column, aValue)) {
					return;
				}
				if (!preventChange) {
					BaseRecord record = table.getRecord(row + 1);
					int nc = record.getFieldCount();
					column--;
					if (column < nc) {
						if (aValue instanceof String) {
							aValue = PgmNormalCell
									.parseConstValue((String) aValue);
						}
						record.set(column, aValue);
					}
					column++;
				}
				super.setValueAt(aValue, row, column);
			}
		};

		tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableData.setRowHeight(20);
		tableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableData.getTableHeader().setReorderingAllowed(false);
		tableData.setClickCountToStart(1);

		jTabMain.add(new JScrollPane(tableData),
				mm.getMessage("paneledittable.data")); // ����
		initTable(tableNormal);
		tableNormal.setColumnCheckBox(COL_PK);
		tableNormal.getColumn(COL_PK).setMaxWidth(80);
		tableNormal.setColumnWidth(COL_PK, 80);
		tableNormal.getColumn(COL_PK).setMaxWidth(500);

		this.setLayout(new BorderLayout());
		this.add(jTabMain, BorderLayout.CENTER);
	}

	/**
	 * ��ʼ��������
	 */
	private void initConstTable() {
		if (table == null) {
			return;
		}
		DataStruct ds = table.dataStruct();
		if (ds == null) {
			return;
		}
		// ������ͨ�ֶ�
		initNormalTable(ds.getFieldNames(), ds.getPrimary());
		// ����������
		resetTableData();
	}

	/**
	 * ��ʼ���ֶκ�����
	 * 
	 * @param normalNames
	 *            �ֶ���
	 * @param primarys
	 *            ������
	 */
	private void initNormalTable(String[] normalNames, String[] primarys) {
		if (normalNames == null) {
			return;
		}
		Section pks = null;
		if (primarys != null) {
			pks = new Section(primarys);
		}
		for (int i = 0; i < normalNames.length; i++) {
			tableNormal.addRow();
			tableNormal.data.setValueAt(normalNames[i], i, COL_NAME);
			Boolean isPk = Boolean.FALSE;
			if (pks != null && pks.containsSection(normalNames[i])) {
				isPk = Boolean.TRUE;
			}
			tableNormal.data.setValueAt(isPk, i, COL_PK);
		}
	}

	/**
	 * ��ȡ��ǰ���ڱ༭��JTable����
	 * 
	 * @return
	 */
	private JTableEx getEditingTable() {
		switch (jTabMain.getSelectedIndex()) {
		case TAB_NORMAL:
			return tableNormal;
		case TAB_DATA:
			return tableData;
		}
		return null;
	}

	/**
	 * �������
	 * 
	 * @return
	 */
	public boolean checkData() {
		tableNormal.acceptText();
		tableData.acceptText();

		HashSet<String> keys = new HashSet<String>();
		String key;
		int count = tableNormal.getRowCount();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				key = (String) tableNormal.data.getValueAt(i, COL_NAME);
				if (!StringUtils.isValidString(key)) {
					jTabMain.setSelectedIndex(TAB_NORMAL);
					GM.messageDialog(
							parent,
							mm.getMessage("paneledittable.emptyname",
									String.valueOf(i + 1))); // �ڣ�{0}���ֶ���Ϊ�ա�
					return false;
				}
				if (keys.contains(key)) {
					jTabMain.setSelectedIndex(TAB_NORMAL);
					GM.messageDialog(
							parent,
							mm.getMessage("paneledittable.existname",
									String.valueOf(i + 1))); // �ڣ�{0}���ֶ����ظ���
					return false;
				}
				keys.add(key);
			}
		} else {
			int option = GM.optionDialog(parent,
					mm.getMessage("paneledittable.norow"), // ��ṹ���ٵ���һ���ֶΣ��Ƿ�����ȱʡ�ֶΣ�
					mm.getMessage("public.prompt"), // ��ʾ
					JOptionPane.OK_CANCEL_OPTION);
			switch (option) {
			case JOptionPane.OK_OPTION:
				jTabMain.setSelectedIndex(TAB_NORMAL);
				addRow();
				break;
			case JOptionPane.CANCEL_OPTION:
				return false;
			default:
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	private String[] getPrimary() {
		Section pks = new Section();
		Object pk;
		for (int i = 0; i < tableNormal.getRowCount(); i++) {
			pk = tableNormal.data.getValueAt(i, COL_PK);
			if (pk != null) {
				if (((Boolean) pk).booleanValue()) {
					pks.addSection((String) tableNormal.data.getValueAt(i,
							COL_NAME));
				}
			}
		}
		return pks.toStringArray();
	}

	/**
	 * ������
	 */
	public void rowUp() {
		JTableEx editingTable = getEditingTable();
		if (editingTable == null) {
			return;
		}
		editingTable.acceptText();
		int row = editingTable.getSelectedRow();
		if (row < 1) {
			return;
		}
		editingTable.shiftRowUp(row);
	}

	/**
	 * ������
	 */
	public void rowDown() {
		JTableEx editingTable = getEditingTable();
		if (editingTable == null) {
			return;
		}
		int row = editingTable.getSelectedRow();
		if (row < 0 || row == editingTable.getRowCount() - 1) {
			return;
		}
		editingTable.shiftRowDown(row);
	}

	/**
	 * ������
	 */
	public void addRow() {
		JTableEx editingTable = getEditingTable();
		if (editingTable == null) {
			return;
		}
		editingTable.acceptText();
		if (editingTable.equals(tableData)) {
			if (table == null) {
				return;
			}
			int count = tableData.getSelectedRowCount();
			if (count == 0) {
				count = 1;
			}
			for (int i = 0; i < count; i++) {
				tableData.addRow();
				table.newLast();
			}
			int rowCount = tableData.getRowCount();
			tableData.setRowSelectionInterval(rowCount - count, rowCount - 1);
		} else {
			String name = null;
			switch (jTabMain.getSelectedIndex()) {
			case TAB_NORMAL:
				name = GM.getTableUniqueName(tableNormal, COL_NAME, "col");
				break;
			}
			int r = editingTable.addRow();
			editingTable.data.setValueAt(name, r, COL_NAME);
			if (jTabMain.getSelectedIndex() == TAB_NORMAL) {
				tableNormal.data.setValueAt(Boolean.FALSE, r, COL_PK);
				alterTable(name, null);
				tableStructChanged();
			}
		}
	}

	/**
	 * ������
	 */
	public void insertRow() {
		JTableEx editingTable = getEditingTable();
		if (editingTable == null) {
			return;
		}
		editingTable.acceptText();
		int selectedRow = editingTable.getSelectedRow();
		if (selectedRow < 0) {
			selectedRow = 0;
		}
		if (editingTable.equals(tableData)) {
			if (table == null) {
				return;
			}
			tableData.insertRow(selectedRow,
					new Object[tableData.getColumnCount()]);
			table.insert(selectedRow + 1);
		} else {
			String name = null;
			switch (jTabMain.getSelectedIndex()) {
			case TAB_NORMAL:
				name = GM.getTableUniqueName(tableNormal, COL_NAME, "col");
				break;
			// case TAB_ALIAS:
			// name = GM.getTableUniqueName(tableAlias, COL_NAME, "alias");
			// break;
			}
			editingTable.insertRow(selectedRow,
					new Object[editingTable.getColumnCount()]);
			editingTable.data.setValueAt(name, selectedRow, COL_NAME);
			if (jTabMain.getSelectedIndex() == TAB_NORMAL) {
				tableNormal.data.setValueAt(Boolean.FALSE, selectedRow, COL_PK);
				// tableNormal.data.setValueAt(new Integer(ORDER_NONE),
				// selectedRow, COL_ORDER);
				// if (isConst) {
				alterTable(name, null, selectedRow);
				// }
				tableStructChanged();
			}
			// else if (jTabMain.getSelectedIndex() == TAB_ALIAS) {
			// tableAlias.data.setValueAt("exp", selectedRow, COL_EXP);
			// if (isConst) {
			// dscTable();
			// }
			// tableStructChanged();
			// }
		}
		// resetExp();
	}

	public void deleteRows() {
		JTableEx editingTable = getEditingTable();
		if (editingTable == null) {
			return;
		}
		if (editingTable.getSelectedRowCount() == 0) {
			return;
		}

		if (editingTable.equals(tableNormal)) {
			if (tableNormal.getRowCount() == 1) {
				GM.messageDialog(parent,
						mm.getMessage("paneledittable.atleastonerow")); // ��ṹ���ٵ���һ���ֶΡ�
				// ���ݽṹ���ֶ���Ŀ����Ϊ0
				return;
			}

			int row = tableNormal.getSelectedRow();
			String oldName = null;
			if (tableNormal.data.getValueAt(row, COL_NAME) != null) {
				oldName = (String) tableNormal.data.getValueAt(row, COL_NAME);
			}
			alterTable(null, oldName);
			editingTable.deleteSelectedRow();
			tableStructChanged();
		} else {
			editingTable.deleteSelectedRow();
			tableStructChanged();
			if (editingTable.equals(tableData)) {
				int rows[] = tableData.getSelectedRows();
				for (int i = rows.length - 1; i >= 0; i--) {
					table.delete(rows[i] + 1);
				}
			}
		}

	}

	/**
	 * ��ʼ��JTable�ؼ�
	 * 
	 * @param table
	 */
	private void initTable(JTableEx table) {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.getTableHeader().setReorderingAllowed(false);
		table.setIndexCol(COL_INDEX);
		table.setClickCountToStart(1);
	}

	/**
	 * ��ṹ�����仯
	 */
	private void tableStructChanged() {
		if (preventChange) {
			return;
		}
		resetTableData();
	}

	/**
	 * ���ñ�����
	 */
	private void resetTableData() {
		tableData.data.setRowCount(0);
		int colCount = tableData.getColumnCount();
		for (int i = colCount - 1; i >= 0; i--) {
			tableData.deleteColumn(tableData.getColumn(i));
		}
		if (table == null) {
			return;
		}
		DataStruct ds = table.dataStruct();
		if (ds == null) {
			return;
		}
		String[] colNames = ds.getFieldNames();
		if (colNames != null) {
			tableData.addColumn(STR_INDEX);
			for (int i = 0; i < colNames.length; i++) {
				tableData.addColumn(colNames[i]);
			}
		}
		if (tableData.getColumnCount() > 0) {
			tableData.setIndexCol(COL_INDEX);
		}
		resetTableDataStyle();

		int rowCount = table.length();
		if (rowCount == 0) {
			return;
		}
		for (int i = 1; i <= rowCount; i++) {
			BaseRecord record = table.getRecord(i);
			int r = tableData.addRow();
			for (int j = 0; j < colNames.length; j++) {
				Object val = null;
				try {
					val = record.getFieldValue(colNames[j]);
					val = Variant.toString(val);
				} catch (Exception e) {
					continue;
				}
				if (val instanceof BaseRecord) {
					BaseRecord tmp = (BaseRecord) val;
					val = GM.getRecordDispName(tmp, new Context());
				}
				tableData.data.setValueAt(val, r, j + 1);
			}
		}
		tableData.repaint();
	}

	/**
	 * �������ݱ�ı༭���
	 */
	private void resetTableDataStyle() {
		if (table == null) {
			return;
		}
		DataStruct ds = table.dataStruct();
		if (ds == null) {
			return;
		}
		for (int i = 1; i <= ds.getFieldCount(); i++) {
			tableData.setColumnDefaultEditor(i);
		}
		GM.setEditStyle(tableData);
		tableData.repaint();
	}

	/**
	 * ѡ���TAB�����ı�
	 * 
	 * @param e
	 */
	void this_stateChanged(ChangeEvent e) {
		if (preventChange) {
			return;
		}
		tableNormal.acceptText();
		if (jTabMain.getSelectedIndex() == TAB_DATA) {
			resetTableData();
		}
	}

}

class PanelEditTable_this_changeAdapter implements
		javax.swing.event.ChangeListener {
	PanelEditTable adaptee;

	PanelEditTable_this_changeAdapter(PanelEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void stateChanged(ChangeEvent e) {
		adaptee.this_stateChanged(e);
	}
}
