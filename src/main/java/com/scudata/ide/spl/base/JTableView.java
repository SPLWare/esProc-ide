package com.scudata.ide.spl.base;

import java.awt.HeadlessException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.DBConfig;
import com.scudata.common.DBInfo;
import com.scudata.common.Matrix;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.dm.BaseRecord;
import com.scudata.dm.DBObject;
import com.scudata.dm.DataStruct;
import com.scudata.dm.FileObject;
import com.scudata.dm.Sequence;
import com.scudata.dm.Table;
import com.scudata.ide.common.AppendDataThread;
import com.scudata.ide.common.DBTypeEx;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.control.CellRect;
import com.scudata.ide.common.control.CellSelection;
import com.scudata.ide.common.dialog.DialogCellFormat;
import com.scudata.ide.common.swing.AllPurposeEditor;
import com.scudata.ide.common.swing.AllPurposeRenderer;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.ide.spl.resources.IdeSplMessage;
import com.scudata.util.Variant;

public abstract class JTableView extends JTableEx {
	private static final long serialVersionUID = 1;
	/**
	 * ��������Դ������
	 */
	private MessageManager mm = IdeSplMessage.get();

	/**
	 * ��Ա
	 */
	private final String COL_SERIES = mm.getMessage("jtablevalue.menber");

	/** ȱʡ */
	private final byte TYPE_DEFAULT = 0;
	/** ��� */
	private final byte TYPE_TABLE = 1;
	/** ���� */
	private final byte TYPE_SERIES = 2;
	/** ��¼ */
	private final byte TYPE_RECORD = 3;
	/** ������ */
	private final byte TYPE_PMT = 4;
	/** ���У���ʱ���ã�������ͨ���н�����ʾ */
	private final byte TYPE_SERIESPMT = 5;
	/** DBInfo���� */
	private final byte TYPE_DB = 6;
	/** FileObject���� */
	private final byte TYPE_FILE = 7;
	/**
	 * ֵ����
	 */
	private byte m_type = TYPE_DEFAULT;

	/**
	 * ��һ��
	 */
	private final int COL_FIRST = 0;

	/**
	 * ֵ
	 */
	private Object value;

	/**
	 * ����������ȡ�ͷ��صĶ�ջ
	 */
	private Stack<Object> undo = new Stack<Object>();
	private Stack<Object> redo = new Stack<Object>();

	/** ����ֵ */
	private final short iCOPY = 11;
	/** �����и�ʽ */
	private final short iFORMAT = 17;

	/**
	 * ���캯��
	 */
	public JTableView() {
		super();
	}

	/**
	 * ���˫��ʱ��ȡ����
	 */
	public void doubleClicked(int xpos, int ypos, int row, int col, MouseEvent e) {
		drillValue(row, col);
	}

	/**
	 * ����Ҽ��˵�
	 */
	public void rightClicked(int xpos, int ypos, int row, int col, MouseEvent e) {
		JPopupMenu pm = new JPopupMenu();
		JMenuItem mItem;
		int selectedRow = getSelectedRow();
		int selectedCol = getSelectedColumn();
		boolean selectCell = selectedRow != -1 && selectedCol != -1;
		mItem = new JMenuItem(mm.getMessage("jtablevalue.copy")); // ����
		mItem.setIcon(GM.getMenuImageIcon("copy"));
		mItem.setName(String.valueOf(iCOPY));
		mItem.addActionListener(popAction);
		mItem.setEnabled(selectCell);
		pm.add(mItem);

		if (selectedCol > -1
				&& (m_type == TYPE_TABLE || m_type == TYPE_PMT || m_type == TYPE_SERIESPMT)) {
			mItem = new JMenuItem(mm.getMessage("jtablevalue.editformat")); // �и�ʽ�༭
			mItem.setIcon(GM.getMenuImageIcon("blank"));
			mItem.setName(String.valueOf(iFORMAT));
			mItem.addActionListener(popAction);
			pm.addSeparator();
			pm.add(mItem);
		}

		pm.show(e.getComponent(), e.getX(), e.getY());
	}

	/**
	 * �Ҽ��˵��¼�����
	 */
	private ActionListener popAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem mItem = (JMenuItem) e.getSource();
			short cmd = Short.parseShort(mItem.getName());
			switch (cmd) {
			case iCOPY:
				copyValue();
				break;
			case iFORMAT:
				colFormat();
				break;
			}
		}
	};

	/**
	 * �и�ʽ�༭
	 */
	private void colFormat() {
		int col = getSelectedColumn();
		if (col < 0) {
			return;
		}
		String colName = null;
		if (!StringUtils.isValidString(colName)) {
			colName = getColumnName(col);
		}
		String format = GM.getColumnFormat(colName);
		DialogCellFormat dcf = new DialogCellFormat();
		if (format != null) {
			dcf.setFormat(format);
		}
		dcf.setVisible(true);
		if (dcf.getOption() == JOptionPane.OK_OPTION) {
			format = dcf.getFormat();
			GM.saveFormat(colName, format);
			setColFormat(col, format);
		}

	}

	/**
	 * �����и�ʽ
	 * 
	 * @param col    �к�
	 * @param format ��ʽ
	 */
	private void setColFormat(int col, String format) {
		TableColumn tc = getColumn(col);
		tc.setCellEditor(new AllPurposeEditor(new JTextField(), this));
		tc.setCellRenderer(new AllPurposeRenderer(format));
		this.repaint();
	}

	/**
	 * ˢ��
	 */
	public void refresh() {
		privateAction(value);
	}

	/**
	 * ֵ�Ƿ�null
	 * 
	 * @return
	 */
	public boolean valueIsNull() {
		return value == null;
	}

	/**
	 * ˢ�°�ť״̬
	 */
	public abstract void refreshValueButton();

	/**
	 * ��������
	 */
	public void setData() {
		if (resetThread != null) {
			resetThread.stopThread();
			try {
				resetThread.join();
			} catch (Exception e) {
			}
		}
		resetThread = null;
		Sequence s;
		switch (m_type) {
		case TYPE_DB:
			s = dbTable;
			break;
		case TYPE_TABLE:
		case TYPE_PMT:
		case TYPE_SERIESPMT:
		case TYPE_SERIES:
			s = (Sequence) value;
			break;
		default:
			return;
		}
		SwingUtilities.invokeLater(resetThread = new ResetDataThread(s));
	}

	/**
	 * �߳�ʵ��
	 */
	private ResetDataThread resetThread = null;

	/**
	 * ��������(����)���ݵ��߳�
	 *
	 */
	class ResetDataThread extends Thread {
		Sequence seq;
		boolean isStoped = false, isFinished = false;

		ResetDataThread(Sequence seq) {
			this.seq = seq;
		}

		public void run() {
			try {
				if (seq == null) {
					removeAllRows();
					return;
				}
				if (isStoped)
					return;
				Object rowData;
				for (int i = 1, count = seq.length(); i <= count; i++) {
					if (isStoped)
						return;
					rowData = seq.get(i);

					if (rowData instanceof BaseRecord) {
						insertRow(-1,
								getRecordData((BaseRecord) seq.get(i), i - 1),
								false);
					} else {
						insertRow(-1, new Object[] { seq.get(i) }, false);
					}
				}
			} catch (Exception ex) {
			} finally {
				isFinished = true;
			}
		}

		void stopThread() {
			seq = null;
			isStoped = true;
		}

		boolean isFinished() {
			return isFinished;
		}
	}

	/**
	 * ��ȡ��¼������
	 * 
	 * @param record ��¼����
	 * @param r      �к�
	 * @return
	 */
	private Object[] getRecordData(BaseRecord record, int r) {
		if (record == null || r < 0)
			return null;
		int colCount = this.getColumnCount();
		Object[] rowData = new Object[colCount];
		if (m_type == TYPE_TABLE) {
			for (int i = 0; i < colCount; i++) {
				rowData[i] = record.getFieldValue(i);
			}
		} else {
			DataStruct ds = record.dataStruct();
			String nNames[] = ds.getFieldNames();
			if (nNames != null) {
				Object val;
				for (int j = 0; j < nNames.length; j++) {
					try {
						val = record.getFieldValue(nNames[j]);
					} catch (Exception e) {
						// ȡ��������ʾ��
						val = null;
					}
					int col = getColumnIndex(nNames[j]);
					if (col > -1)
						rowData[col] = val;
				}
			}
		}
		return rowData;
	}

	/**
	 * ��Ԫ���Ƿ���Ա༭
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/**
	 * ����
	 */
	public void clear() {
		undo.clear();
		redo.clear();
		value = null;
		initJTable();
	}

	/**
	 * ���õ�Ԫ��ֵ
	 * 
	 * @param value ��Ԫ��ֵ
	 */
	public void setValue(Object value) {
		setValue(value, false);
	}

	/**
	 * ���õ�Ԫ��ֵ
	 * 
	 * @param value         ��Ԫ��ֵ
	 * @param privateAction �Ƿ��ڲ����ö���
	 */
	private synchronized void setValue(Object value, boolean privateAction) {
		this.value = value;
		dbTable = null;
		try {
			initJTable();
			if (!privateAction) {
				undo.clear();
				redo.clear();
			}
			refreshValueButton();
			if (value == null) {
				if (resetThread != null) {
					resetThread.stopThread();
					try {
						resetThread.join();
					} catch (Exception e) {
					}
				}
				resetThread = null;
				return;
			}
			m_type = getValueType(value);
			switch (m_type) {
			case TYPE_TABLE:
				initTable((Table) value);
				break;
			case TYPE_PMT:
				initPmt((Sequence) value);
				break;
			case TYPE_RECORD:
				initRecord((BaseRecord) value);
				break;
			case TYPE_SERIES:
				initSeries((Sequence) value);
				break;
			case TYPE_SERIESPMT:
				initSeriesPmt((Sequence) value);
				break;
			case TYPE_DB:
				initDB((DBObject) value);
				break;
			case TYPE_FILE:
				initFile((FileObject) value);
				break;
			default:
				initDefault(value);
				break;
			}
		} finally {
			setData();
		}
	}

	/**
	 * ��ʼ�����ؼ�
	 */
	private void initJTable() {
		removeAllRows();
		data.getDataVector().clear();
		int colCount = getColumnCount();
		if (colCount == 0) {
			return;
		}
		for (int i = colCount - 1; i >= 0; i--) {
			try {
				deleteColumn(getColumn(i));
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * ȡֵ������
	 * 
	 * @param value ֵ
	 * @return
	 */
	private byte getValueType(Object value) {
		if (value instanceof Table) {
			return TYPE_TABLE;
		} else if (value instanceof Sequence) {
			if (((Sequence) value).isPurePmt()) {
				return TYPE_PMT;
			} else if (((Sequence) value).isPmt()) {
				return TYPE_SERIESPMT;
			}
			return TYPE_SERIES;
		} else if (value instanceof BaseRecord) {
			return TYPE_RECORD;
		} else if (value instanceof DBObject) {
			return TYPE_DB;
		} else if (value instanceof FileObject) {
			return TYPE_FILE;
		} else {
			return TYPE_DEFAULT;
		}
	}

	/**
	 * ��ʼ�����
	 * 
	 * @param table ���
	 * @return
	 */
	private int initTable(Table table) {
		DataStruct ds = table.dataStruct();
		setTableColumns(ds);
		setEditStyle(ds);
		return table.length();
	}

	/**
	 * ��ʼ��������
	 * 
	 * @param pmt ������
	 * @return
	 */
	private int initPmt(Sequence pmt) {
		DataStruct ds = pmt.dataStruct();
		setTableColumns(ds);
		setEditStyle(ds);
		return pmt.length();
	}

	/**
	 * ��ʼ������
	 * 
	 * @param pmt
	 * @return
	 */
	private int initSeriesPmt(Sequence pmt) {
		DataStruct ds = ((BaseRecord) pmt.ifn()).dataStruct();
		setTableColumns(ds);
		setEditStyle(ds);
		return pmt.length();
	}

	/**
	 * ��ʼ������
	 * 
	 * @param series
	 * @return
	 */
	private int initSeries(Sequence series) {
		addColumn(COL_SERIES);
		setColumnWidth(COL_FIRST, 200);
		TableColumn tc = getColumn(COL_FIRST);
		tc.setCellEditor(new AllPurposeEditor(new JTextField(), this));
		tc.setCellRenderer(new AllPurposeRenderer());
		return series.length();
	}

	/**
	 * ��ʼ����¼
	 * 
	 * @param record ��¼
	 * @return
	 */
	private int initRecord(BaseRecord record) {
		DataStruct ds = record.dataStruct();
		setTableColumns(ds);
		try {
			AppendDataThread.addRecordRow(this, record);
		} catch (Exception ex) {
			GM.showException(GV.appFrame, ex);
		}
		setEditStyle(ds);
		return 1;
	}

	/**
	 * ���ñ༭���
	 * 
	 * @param ds ���ݽṹ
	 */
	private void setEditStyle(DataStruct ds) {
		String cols[] = ds.getFieldNames();
		TableColumn tc;
		for (int i = 0; i < cols.length; i++) {
			tc = this.getColumn(i);
			String format = GM.getColumnFormat(cols[i]);
			if (StringUtils.isValidString(format)) {
				tc.setCellEditor(new AllPurposeEditor(new JTextField(), this));
				tc.setCellRenderer(new AllPurposeRenderer(format));
			}
		}

		GM.setEditStyle(this);
	}

	/**
	 * ���ñ�����
	 * 
	 * @param ds ���ݽṹ
	 */
	private void setTableColumns(DataStruct ds) {
		String nNames[] = ds.getFieldNames();
		if (nNames != null) {
			for (int i = 0; i < nNames.length; i++) {
				addColumn(nNames[i]);
			}
		}
	}

	/**
	 * DBInfo�����Ӧ�����
	 */
	private Table dbTable = null;

	/** ������ */
	private final String TITLE_NAME = mm.getMessage("jtablevalue.name");
	/** ����ֵ */
	private final String TITLE_PROP = mm.getMessage("jtablevalue.property");

	/**
	 * ��ʼ��DBInfo����
	 * 
	 * @param db DBInfo����
	 * @return
	 */
	private int initDB(DBObject db) {
		dbTable = new Table(new String[] { TITLE_NAME, TITLE_PROP });
		addColumn(TITLE_NAME); // ����
		addColumn(TITLE_PROP); // ����
		for (int i = 0; i < this.getColumnCount(); i++) {
			setColumnEditable(i, false);
		}
		if (db == null || db.getDbSession() == null) {
			return 0;
		}
		initDBTable(db);
		return dbTable.length();
	}

	/** ����Դ���� */
	private final String DB_NAME = mm.getMessage("jtablevalue.dbname");
	/** �û��� */
	private final String USER = mm.getMessage("jtablevalue.user");
	/** ���ݿ����� */
	private final String DB_TYPE = mm.getMessage("jtablevalue.dbtype");
	/** �������� */
	private final String DRIVER = mm.getMessage("jtablevalue.driver");
	/** ����ԴURL */
	private final String URL = mm.getMessage("jtablevalue.url");
	/** ��������ģʽ */
	private final String USE_SCHEMA = mm.getMessage("jtablevalue.useschema");
	/** ���������޶��� */
	private final String ADD_TILDE = mm.getMessage("jtablevalue.addtilde");

	private void initDBTable(DBObject db) {
		DBInfo info = db.getDbSession().getInfo();
		dbTable.newLast(new Object[] { DB_NAME, info.getName() });
		if (info instanceof DBConfig) {
			int type = info.getDBType();
			dbTable.newLast(new Object[] { DB_TYPE,
					DBTypeEx.getDBTypeName(type) });

			DBConfig dc = (DBConfig) info;
			dbTable.newLast(new Object[] { DRIVER, dc.getDriver() });
			dbTable.newLast(new Object[] { URL, dc.getUrl() });
			dbTable.newLast(new Object[] { USER, dc.getUser() });
			dbTable.newLast(new Object[] { USE_SCHEMA,
					Boolean.toString(dc.isUseSchema()) });
			dbTable.newLast(new Object[] { ADD_TILDE,
					Boolean.toString(dc.isAddTilde()) });
		}
	}

	/**
	 * ��ʼ��FileObject����
	 * 
	 * @param file FileObject����
	 * @return
	 */
	private int initFile(FileObject file) {
		addColumn(mm.getMessage("public.file"));
		return initSingleValue(file);
	}

	/**
	 * ��ʼ����ֵͨ
	 * 
	 * @param value ��ֵͨ
	 * @return
	 */
	private int initDefault(Object value) {
		addColumn(mm.getMessage("public.value"));
		return initSingleValue(value);
	}

	/**
	 * ��ʼ��������ֵ
	 * 
	 * @param value
	 * @return
	 */
	private int initSingleValue(final Object value) {
		setColumnWidth(COL_FIRST, 200);
		TableColumn tc = getColumn(0);
		tc.setCellEditor(new AllPurposeEditor(new JTextField(), this));
		tc.setCellRenderer(new AllPurposeRenderer());
		if (getRowCount() == 0) {
			addRow();
		}
		data.setValueAt(value, 0, COL_FIRST);
		return 1;
	}

	/**
	 * ��ʾ��ֵ
	 */
	public void dispCellValue() {
		int r = getSelectedRow();
		int c = getSelectedColumn();
		drillValue(r, c);
	}

	/**
	 * ��ȡ��Աֵ
	 * 
	 * @param row �к�
	 * @param col �к�
	 */
	private void drillValue(int row, int col) {
		Object newValue = null;
		switch (m_type) {
		case TYPE_TABLE:
		case TYPE_PMT:
		case TYPE_SERIES:
		case TYPE_SERIESPMT:
			Sequence s = (Sequence) value;
			Object temp = s.get(row + 1);
			if (temp instanceof BaseRecord) {
				BaseRecord r = (BaseRecord) temp;
				if (r.dataStruct() != null && s.dataStruct() != null
						&& !r.dataStruct().equals(s.dataStruct())) { // �칹����
					newValue = temp;
				}
			}
			break;
		default:
			break;
		}
		if (newValue == null) {
			newValue = data.getValueAt(row, col);
			if (newValue == null) {
				return;
			}
		}
		if (newValue.equals(value)) { // ��ȡ��Ԫ���Ǳ���ʱ
			return;
		}
		redo.clear();
		undo.push(value);
		value = newValue;
		privateAction(value);
	}

	/**
	 * �ڲ�����ֵ������������״̬
	 * 
	 * @param newValue
	 */
	private void privateAction(Object newValue) {
		setValue(newValue, true);
	}

	/**
	 * �Ƿ���Գ���
	 * 
	 * @return
	 */
	public boolean canUndo() {
		return !undo.empty();
	}

	/**
	 * �Ƿ��������
	 * 
	 * @return
	 */
	public boolean canRedo() {
		return !redo.empty();
	}

	/**
	 * ����
	 */
	public void undo() {
		redo.push(value);
		value = undo.pop();
		privateAction(value);
	}

	/**
	 * ����
	 */
	public void redo() {
		undo.push(value);
		value = redo.pop();
		privateAction(value);
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public boolean copyValue() {
		int rows[] = getSelectedRows();
		if (rows == null || rows.length == 0) {
			return false;
		}
		int cc = getColumnCount();
		Matrix matrix = new Matrix(rows.length, cc);
		CellRect cr = new CellRect(0, (short) 0, rows.length - 1,
				(short) (cc - 1));
		for (int r = 0; r < rows.length; r++) {
			for (int c = 0; c < getColumnCount(); c++) {
				Object value = data.getValueAt(rows[r], c);
				PgmNormalCell pnc = new PgmNormalCell();
				pnc.setValue(value);
				try {
					pnc.setExpString(Variant.toExportString(value));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				matrix.set(r, c, pnc);
			}
		}
		GV.cellSelection = new CellSelection(matrix, cr, null);
		Clipboard cb = null;
		try {
			cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (HeadlessException e) {
			cb = null;
		}
		String strCS = GM.getCellSelectionString(matrix, false);
		if (cb != null) {
			cb.setContents(new StringSelection(strCS), null);
		}
		GV.cellSelection.systemClip = strCS;
		return true;
	}

	/**
	 * ȡ��Ԫ��ֵ
	 */
	public Object getValueAt(int row, int col) {
		try {
			return super.getValueAt(row, col);
		} catch (Exception ex) {
			return null;
		}
	}

}
