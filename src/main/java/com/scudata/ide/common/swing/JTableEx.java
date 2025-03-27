package com.scudata.ide.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.scudata.app.common.Section;
import com.scudata.common.ArgumentTokenizer;
import com.scudata.common.MessageManager;
import com.scudata.common.Sentence;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.control.ControlUtilsBase;
import com.scudata.ide.common.resources.IdeCommonMessage;

/**
 * ��չ��JTable��ע��: ����ͨ��jTable.getValueAt���ܷ��ʱ������˵���,���� һ���� jTable.data.getValueAt
 */

public class JTableEx extends JTable implements MouseListener,
		JTableExListener, ChangeListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * ���ģ��
	 */
	public DefaultTableModel data = new DefaultTableModel();

	/**
	 * ��ʾ����ֹʹ��data.setRowCount(0);����this.removeAllRows()
	 */
	private int indexCol = -1;

	/**
	 * columnKeeper ���ڱ���ס�б��е��ж���ʹ��hideColumn()�Լ� showColumn()��������ʵ��
	 * ע�⶯̬����Table�е��е�ʱ��ֻ��ʹ��Table.AddColumn(col); ע��!!!!!!!!!!!!!!!!!!!!!!
	 * ����Ҫʹ��Table.data.AddColumn(col)
	 */
	private HashMap<Object, TableColumn> columnKeeper = new HashMap<Object, TableColumn>();

	/**
	 * �Ƿ񼤻��н���仯
	 */
	private boolean ifFireRowfocusChangedEvent = true;

	/**
	 * ���������������
	 */
	public HashSet<JTableExListener> clickedListener = new HashSet<JTableExListener>();

	/**
	 * ���캯��
	 */
	public JTableEx() {
		setModel(data);
		addMouseListener(this);
		addTableExListener(this);
		getColumnModel().addColumnModelListener(new TableColListener(this));
		// Ĭ�ϲ�֧��Ų���У�����˫���¼����ò���
		getTableHeader().setReorderingAllowed(false);

	}

	/**
	 * ���캯��
	 * 
	 * @param colNames
	 *            ��������
	 */
	public JTableEx(String[] colNames) {
		this();
		for (int i = 0; i < colNames.length; i++) {
			addColumn(colNames[i]);
			setColumnDefaultEditor(i);
		}
		holdColumnNames();
		oldRow = -1;
	}

	/**
	 * ���캯��
	 * 
	 * @param columnNames
	 *            ���ŷָ�������
	 */
	public JTableEx(String columnNames) {
		this(new Section(columnNames).toStringArray());
	}

	/**
	 * ����Ҽ�����¼�
	 */
	public void rightClicked(int xpos, int ypos, int row, int col, MouseEvent e) {
	}

	/**
	 * ���˫���¼�
	 */
	public void doubleClicked(int xpos, int ypos, int row, int col, MouseEvent e) {
	}

	/**
	 * ������¼�
	 */
	public void clicked(int xpos, int ypos, int row, int col, MouseEvent e) {
	}

	/**
	 * �н���仯�¼�
	 */
	public void rowfocusChanged(int oldRow, int newRow) {
	}

	/**
	 * �н������ڱ仯�¼�
	 * 
	 * @param oldRow
	 * @param newRow
	 */
	public void rowfocusChanging(int oldRow, int newRow) {
	}

	/**
	 * ������¼�
	 */
	public void mouseClicked(final MouseEvent e) {
		java.awt.Container p = getParent();
		java.awt.Container ct = getTopLevelAncestor();
		int absoluteX = e.getX(), absoluteY = e.getY();
		while (p != ct) {
			absoluteX += p.getX();
			absoluteY += p.getY();
			p = p.getParent();
		}
		fireClicked(absoluteX, absoluteY, e);
	}

	/**
	 * ��갴���¼�
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * ���������¼�
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * ����ͷ��¼�
	 */
	public void mouseReleased(MouseEvent e) {
		// ����ѡ���ж�����û���ػ�����
		repaint();
	}

	/**
	 * ����˳��¼�
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * ��������
	 */
	void holdColumnNames() {
		if (columnKeeper == null) {
			return;
		}
		columnKeeper.clear();

		TableColumnModel cm = getColumnModel();
		Enumeration cols = cm.getColumns();
		TableColumn tmpCol;
		Object tmpName;
		while (cols.hasMoreElements()) {
			tmpCol = (TableColumn) cols.nextElement();
			tmpCol.setCellEditor(new SimpleEditor(new JTextField(), this));
			tmpName = tmpCol.getIdentifier();
			columnKeeper.put(tmpName, tmpCol);
		}
	}

	/**
	 * ���õ�����ν���༭״̬
	 * 
	 * @param startCount
	 */
	public void setClickCountToStart(int startCount) {
		int rows = getRowCount();
		int cols = getColumnCount();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				CellEditor editor = getCellEditor(row, col);
				if (editor != null && editor instanceof DefaultCellEditor) {
					((DefaultCellEditor) editor)
							.setClickCountToStart(startCount);
				}
			}
		}
	}

	/**
	 * ֵ�Ƿ����仯��
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @param newValue
	 *            ��ֵ
	 * @return
	 */
	protected boolean isItemDataChanged(int row, int column, Object newValue) {
		if (column == indexCol) {
			return false;
		}
		Object oldValue = getValueAt(row, column);
		if (oldValue instanceof String && newValue instanceof String) {
			if (StringUtils.isValidString(oldValue)) {
				return !oldValue.equals(newValue);
			} else {
				return StringUtils.isValidString(newValue);
			}
		} else {
			if (oldValue != null) {
				return !oldValue.equals(newValue);
			} else {
				return newValue != null && !newValue.equals("");
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @param exportTitle
	 *            �Ƿ񵼳�����
	 * @param fw
	 *            FileWriter
	 * @param dosFormat
	 *            ��ʽ
	 * @return
	 * @throws Exception
	 */
	public boolean exportData(boolean exportTitle, FileWriter fw,
			boolean dosFormat) throws Exception {
		String sRow = "", sTmp = "";
		int rc, cc, r, c;
		cc = getColumnCount();
		rc = getRowCount();
		String rowSep;
		if (dosFormat) {
			rowSep = "\r\n";
		} else {
			rowSep = "\r";
		}

		if (exportTitle) {
			if (cc == 0) {
				return false;
			}
			for (c = 0; c < cc; c++) {
				sTmp += "\t" + getColumnName(c);
			}
			sRow = rowSep + sTmp.substring(1);
		}

		for (r = 0; r < rc; r++) {
			sTmp = "";
			for (c = 0; c < cc; c++) {
				sTmp += "\t" + data.getValueAt(r, c);
			}
			sRow += rowSep + sTmp.substring(1);
		}

		fw.write(sRow.substring(rowSep.length()));
		return true;
	}

	/**
	 * ���ñ�ģ��
	 */
	public void setModel(TableModel dataModel) {
		data = (DefaultTableModel) dataModel;
		super.setModel(data);
		holdColumnNames();
		oldRow = -1;
	}

	/**
	 * ��������
	 * 
	 * @param newColNames
	 *            �����б�
	 */
	public void resetColumns(ArrayList newColNames) {
		data.setColumnCount(0);
		for (int i = 0; i < newColNames.size(); i++) {
			data.addColumn(newColNames.get(i));
		}
		holdColumnNames();
	}

	/**
	 * �����ύ����
	 */
	public void acceptText() {
		if (getCellEditor() != null && getRowCount() > 0) {
			int r = getSelectedRow();
			int c = getRowCount();
			int e = getEditingRow();
			if (e >= c) {
				return;
			}
			getCellEditor().stopCellEditing();
		}
	}

	/**
	 * ���Ӽ�����
	 * 
	 * @param jcl
	 */
	public void addTableExListener(JTableExListener jcl) {
		clickedListener.add(jcl);
	}

	/**
	 * ɾ��������
	 * 
	 * @param jcl
	 */
	public void removeClickedListener(JTableExListener jcl) {
		clickedListener.remove(jcl);
	}

	/**
	 * ����������¼�
	 * 
	 * @param xpos
	 *            X����
	 * @param ypos
	 *            Y����
	 * @param e
	 *            ����¼�
	 */
	public void fireClicked(int xpos, int ypos, MouseEvent e) {
		Iterator it = clickedListener.iterator();
		int row, col;
		row = this.getSelectedRow();
		col = this.getSelectedColumn();

		if (row == -1) {
			row = this.rowAtPoint(new java.awt.Point(e.getX(), e.getY()));
		}
		int pcol = this.columnAtPoint(new java.awt.Point(e.getX(), e.getY()));
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (col != pcol && pcol > -1) {
				col = pcol;
				setColumnSelectionInterval(col, col);
			}
		} else {
			if (col == -1) {
				col = pcol;
				setColumnSelectionInterval(col, col);
			}
		}
		while (it.hasNext()) {
			JTableExListener lis = (JTableExListener) it.next();
			if (e.getButton() == MouseEvent.BUTTON3) {
				lis.rightClicked(xpos, ypos, row, col, e);
				continue;
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				switch (e.getClickCount()) {
				case 1:
					lis.clicked(xpos, ypos, row, col, e);
					break;
				case 2:
					lis.doubleClicked(xpos, ypos, row, col, e);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * ������
	 * 
	 * @param colName
	 */
	public void addColumn(String colName) {
		data.addColumn(colName);
		holdColumnNames();
	}

	/**
	 * ����ɾ����ָ�����У���������Model
	 * 
	 * @param aColumn
	 *            TableColumn�ж���
	 */
	public void deleteColumn(TableColumn aColumn) {
		String colName = (String) aColumn.getIdentifier();

		int cc = data.getColumnCount();
		DefaultTableModel dataModel = new DefaultTableModel();

		for (int i = 0; i < cc; i++) {
			String tmpName = (String) data.getColumnName(i);
			if (tmpName.equalsIgnoreCase(colName)) {
				continue;
			}
			Vector cData = getColumnData(i);
			dataModel.addColumn(tmpName, cData);
		}
		setModel(dataModel);
	}

	/**
	 * ȡ�ж���
	 * 
	 * @param columnIndex
	 *            �к�
	 * @return
	 */
	public TableColumn getColumn(int columnIndex) {
		return getColumnModel().getColumn(columnIndex);
	}

	/**
	 * ȡ�ж���
	 * 
	 * @param colName
	 *            ����
	 * @return
	 */
	public int getColumnIndex(String colName) {
		return getColumnIndex(colName, false);
	}

	/**
	 * ȡ�����
	 * 
	 * @param colName
	 *            ����
	 * @param includeHideColumns
	 *            �Ƿ����������
	 * @return
	 */
	public int getColumnIndex(String colName, boolean includeHideColumns) {
		if (includeHideColumns) {
			for (int i = 0; i < data.getColumnCount(); i++) {
				String name = data.getColumnName(i);
				if (name.equals(colName)) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < this.getColumnCount(); i++) {
				String name = getColumnName(i);
				if (name.equals(colName)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * ɾ��������
	 */
	public void removeAllRows() {
		data.setRowCount(0);
		oldRow = -1;
	}

	/**
	 * ȡ������
	 * 
	 * @param colIndex
	 *            �к�
	 * @return
	 */
	public Vector<Object> getColumnData(int colIndex) {
		Vector<Object> cData = new Vector<Object>();
		for (int r = 0; r < data.getRowCount(); r++) {
			cData.add(data.getValueAt(r, colIndex));
		}
		return cData;
	}

	/**
	 * ��С�����м���
	 */
	private transient HashMap<Integer, String> minimizedColumn = new HashMap<Integer, String>();

	/**
	 * ��С���У������еĿ����Ϊ0�������ڿ�����
	 * 
	 * @param colIndex
	 *            int
	 */
	public void minimizeColumn(int columnIndex) {
		TableColumn tc = getColumn(columnIndex);
		if (tc.getPreferredWidth() == 0) {
			return;
		}
		String oldConfig = tc.getMaxWidth() + "|" + tc.getMinWidth() + "|"
				+ tc.getPreferredWidth();
		minimizedColumn.put(columnIndex, oldConfig);
		tc.setMaxWidth(0);
		tc.setMinWidth(0);
		tc.setPreferredWidth(0);
		tc.setResizable(false);
	}

	/**
	 * ��ԭ��С������
	 * 
	 * @param colIndex
	 *            int
	 */
	public void recoverColumn(int columnIndex) {
		TableColumn tc = getColumn(columnIndex);
		tc.setResizable(true);
		String oldConfig = minimizedColumn.get(columnIndex);
		if (oldConfig == null) {
			return;
		}
		ArgumentTokenizer at = new ArgumentTokenizer(oldConfig, '|');
		try {
			tc.setMaxWidth(Integer.parseInt(at.nextToken()));
		} catch (Exception e) {
		}
		try {
			tc.setMinWidth(Integer.parseInt(at.nextToken()));
		} catch (Exception e) {
		}
		try {
			tc.setPreferredWidth(Integer.parseInt(at.nextToken()));
		} catch (Exception e) {
		}
	}

	/**
	 * ������
	 * 
	 * @param columnName
	 *            ����
	 */
	public void hideColumn(String columnName) {
		if (!isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = getColumn(columnName);
		Object id = col.getIdentifier();
		columnKeeper.put(id, col);
		removeColumn(col);
	}

	/**
	 * �������Ƿ����
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param enable
	 *            �Ƿ����
	 */
	public void setColumnEnable(int columnIndex, boolean enable) {
		setColumnEnable(getColumnName(columnIndex), enable);
	}

	/**
	 * �������Ƿ����
	 * 
	 * @param columnName
	 *            ����
	 * @param enable
	 *            �Ƿ�ɱ༭
	 */
	public void setColumnEnable(String columnName, boolean enable) {
		if (!isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = getColumn(columnName);
		Object o = col.getCellRenderer();

		int align = ALIGN_LEFT;
		if (o != null && o instanceof DefaultTableCellRenderer) {
			align = ((DefaultTableCellRenderer) o).getHorizontalAlignment();
		}
		JTextField jtf = new JTextField();
		jtf.setHorizontalAlignment(align);

		if (enable) {
			col.setCellEditor(new DefaultCellEditor(jtf));
			DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
			dtcr.setHorizontalAlignment(align);
			col.setCellRenderer(dtcr);
		} else {
			col.setCellEditor(new DisabledEditor(jtf));
			DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
			dtcr.setHorizontalAlignment(align);
			dtcr.setForeground(disabledForeColor);
			dtcr.setBackground(disabledBackColor);
			col.setCellRenderer(dtcr);
		}
	}

	/**
	 * ȡ�б༭��
	 * 
	 * @param columnIndex
	 *            �к�
	 * @return
	 */
	public TableCellEditor getColumnEditor(int columnIndex) {
		TableColumn tc = getColumn(getColumnName(columnIndex));
		return tc.getCellEditor();
	}

	/**
	 * ������ȱʡ�༭�ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 */
	public void setColumnDefaultEditor(int columnIndex) {
		setColumnDefaultEditor(getColumnName(columnIndex));
	}

	/**
	 * ������ȱʡ�༭�ؼ�
	 * 
	 * @param columnName
	 *            ����
	 */
	public void setColumnDefaultEditor(String columnName) {
		TableColumn col = getColumn(columnName);
		col.setCellEditor(new SimpleEditor(new JTextField(), this));

		DefaultTableCellRenderer dr = new DefaultTableCellRenderer();
		col.setCellRenderer(dr);
	}

	/**
	 * ��������ֵ�༭�ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 */
	public void setColumnSpinner(int columnIndex) {
		setColumnSpinner(getColumnName(columnIndex));
	}

	/**
	 * ��������ֵ�༭�ؼ�
	 * 
	 * @param columnName
	 *            ����
	 */
	public void setColumnSpinner(String columnName) {
		DefaultCellEditor integerEditor = new JTextAreaEditor(this,
				JTextAreaEditor.TYPE_UNSIGNED_INTEGER);
		TableColumn tc = getColumn(columnName);
		tc.setCellEditor(integerEditor);
	}

	/**
	 * �����и�ѡ��ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 */
	public void setColumnCheckBox(int columnIndex) {
		setColumnCheckBox(getColumnName(columnIndex));
	}

	/**
	 * �����и�ѡ��ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param enable
	 *            �Ƿ�ɱ༭
	 */
	public void setColumnCheckBox(int columnIndex, boolean enable) {
		setColumnCheckBox(getColumnName(columnIndex), enable);
	}

	/**
	 * �����и�ѡ��ؼ�
	 * 
	 * @param columnName
	 *            ����
	 */
	public void setColumnCheckBox(String columnName) {
		setColumnCheckBox(columnName, true);
	}

	/**
	 * �����и�ѡ��ؼ�
	 * 
	 * @param columnName
	 *            ����
	 * @param enable
	 *            �Ƿ�ɱ༭
	 */
	public void setColumnCheckBox(String columnName, final boolean enable) {
		JCheckBox checkBoxEditor = new JCheckBox();
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stateChanged(new ChangeEvent(e.getSource()));
			}
		};
		checkBoxEditor.addActionListener(al);
		checkBoxEditor.setEnabled(enable);
		checkBoxEditor.setHorizontalAlignment(JLabel.CENTER);

		TableCellEditor cellEditor = new DefaultCellEditor(checkBoxEditor) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(EventObject anEvent) {
				return enable;
			}

			public Component getTableCellEditorComponent(JTable table,
					Object value, boolean isSelected, int row, int column) {
				Component c = super.getTableCellEditorComponent(table, value,
						isSelected, row, column);
				if (isSelected) {
					c.setForeground(table.getSelectionForeground());
					c.setBackground(table.getSelectionBackground());
				} else {
					c.setForeground(table.getForeground());
					c.setBackground(table.getBackground());
				}
				return c;
			}
		};

		TableCellRenderer cellRenderer = new CheckBoxRenderer();

		TableColumn col = getColumn(columnName);
		col.setCellEditor(cellEditor);
		col.setCellRenderer(cellRenderer);
	}

	/**
	 * �����������ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param codeItems
	 *            ����ֵ
	 * @param dispItems
	 *            ��ʾֵ
	 * @return
	 */
	public JComboBoxEx setColumnDropDown(int columnIndex, Vector codeItems,
			Vector dispItems) {
		return setColumnDropDown(getColumnName(columnIndex), codeItems,
				dispItems);
	}

	/**
	 * �����������ؼ�
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param codeItems
	 *            ����ֵ
	 * @param dispItems
	 *            ��ʾֵ
	 * @param editable
	 *            �Ƿ�ɱ༭
	 * @return
	 */
	public JComboBoxEx setColumnDropDown(int columnIndex, Vector codeItems,
			Vector dispItems, boolean editable) {
		// ��η�������������ʱ����ǰ�������ؼ�Ҫ�������������������ˢ��
		acceptText();

		return setColumnDropDown(getColumnName(columnIndex), codeItems,
				dispItems, editable);
	}

	/**
	 * �����������ؼ�
	 * 
	 * @param columnName
	 *            ����
	 * @param codeItems
	 *            ����ֵ
	 * @param dispItems
	 *            ��ʾֵ
	 * @return
	 */
	public JComboBoxEx setColumnDropDown(String columnName, Vector codeItems,
			Vector dispItems) {
		return setColumnDropDown(columnName, codeItems, dispItems, false);
	}

	/**
	 * �����������ؼ�
	 * 
	 * @param columnName
	 *            ����
	 * @param codeItems
	 *            ����ֵ
	 * @param dispItems
	 *            ��ʾֵ
	 * @param editable
	 *            �Ƿ�ɱ༭
	 * @return
	 */
	public JComboBoxEx setColumnDropDown(final String columnName,
			Vector codeItems, Vector dispItems, boolean editable) {
		JComboBoxEx combo = new JComboBoxEx();
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeEvent ce = new ChangeEvent(e.getSource());
				stateChanged(ce);
			}
		};
		combo.addActionListener(al);
		combo.x_setData(codeItems, dispItems);
		combo.setEditable(editable);
		TableColumn col = getColumn(columnName);
		TableCellEditor cellEditor = new JComboBoxExEditor(combo);
		TableCellRenderer cellRenderer = new JComboBoxExRenderer(combo);
		col.setCellEditor(cellEditor);
		col.setCellRenderer(cellRenderer);
		return combo;
	}

	/**
	 * �������Ƿ�ɱ༭
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param allowEdit
	 *            �Ƿ�ɱ༭
	 */
	public void setColumnEditable(int columnIndex, boolean allowEdit) {
		setColumnEditable(getColumnName(columnIndex), allowEdit);
	}

	/**
	 * �������Ƿ�ɱ༭
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param allowEdit
	 *            �Ƿ�ɱ༭
	 * @param centerAlign
	 *            �Ƿ������ʾ
	 */
	public void setColumnEditable(int columnIndex, boolean allowEdit,
			boolean centerAlign) {
		setColumnEditable(getColumnName(columnIndex), allowEdit, centerAlign);
	}

	/**
	 * �������Ƿ�ɱ༭
	 * 
	 * @param columnName
	 *            ����
	 * @param allowEdit
	 *            �Ƿ�ɱ༭
	 */
	public void setColumnEditable(String columnName, boolean allowEdit) {
		setColumnEditable(columnName, allowEdit, false);
	}

	/**
	 * �������Ƿ�ɱ༭
	 * 
	 * @param columnName
	 *            ����
	 * @param allowEdit
	 *            �Ƿ�ɱ༭
	 * @param centerAlign
	 *            �Ƿ������ʾ
	 */
	public void setColumnEditable(String columnName, boolean allowEdit,
			final boolean centerAlign) {
		if (!isColumnVisible(columnName)) {
			return;
		}
		if (!allowEdit) {
			TableColumn col = getColumn(columnName);
			col.setCellEditor(new JTextAreaEditor(this,
					JTextAreaEditor.TYPE_TEXT_READONLY) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(EventObject anEvent) {
					return false;
				}

				public Component getTableCellEditorComponent(JTable table,
						Object value, boolean isSelected, int row, int column) {
					Component c = super.getTableCellEditorComponent(table,
							value, isSelected, row, column);
					if (isSelected) {
						setForeground(table.getSelectionForeground());
						setBackground(table.getSelectionBackground());
					} else {
						setForeground(table.getForeground());
						setBackground(table.getBackground());
					}
					if (centerAlign) {
						if (c instanceof JLabel) {
							JLabel text = (JLabel) c;
							text.setHorizontalAlignment(JTextField.CENTER);
						}
					}
					return c;
				}
			});

			TableCellRenderer tcr = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					Component c = super.getTableCellRendererComponent(table,
							value, isSelected, hasFocus, row, column);
					if (isSelected) {
						setForeground(table.getSelectionForeground());
						setBackground(table.getSelectionBackground());
					} else {
						setForeground(table.getForeground());
						setBackground(table.getBackground());
					}
					if (c instanceof JLabel) {
						JLabel jLabel = (JLabel) c;
						if (centerAlign) {
							jLabel.setHorizontalAlignment(JTextField.CENTER);
						}
						Font font = jLabel.getFont();
						if (!ControlUtilsBase.canDisplayText(font,
								jLabel.getText())) {
							font = new Font("Dialog", font.getStyle(),
									font.getSize());
							jLabel.setFont(font);
						}
					}
					return c;
				}
			};
			col.setCellRenderer(tcr);
		} else {
			this.setColumnDefaultEditor(columnName);
		}
	}

	/**
	 * �����е������
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param width
	 *            ���
	 */
	public void setColumnFixedWidth(int columnIndex, int width) {
		String columnName = getColumnName(columnIndex);
		if (!isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = getColumn(columnName);
		col.setPreferredWidth(width);
		col.setMaxWidth(width);
	}

	/**
	 * �����п��
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param width
	 *            ���
	 */
	public void setColumnWidth(int columnIndex, int width) {
		setColumnWidth(getColumnName(columnIndex), width);
	}

	/**
	 * �����п��
	 * 
	 * @param columnName
	 *            ����
	 * @param width
	 *            ���
	 */
	public void setColumnWidth(String columnName, int width) {
		if (!isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = getColumn(columnName);
		col.setPreferredWidth(width);
	}

	/** ��������� */
	static public int ALIGN_LEFT = JLabel.LEFT;
	/** ������ж��� */
	static public int ALIGN_CENTER = JLabel.CENTER;
	/** �����Ҷ��� */
	static public int ALIGN_RIGHT = JLabel.RIGHT;

	/**
	 * �����еĺ������
	 * 
	 * @param columnIndex
	 *            �к�
	 * @param alignment
	 *            ���뷽ʽ
	 */
	public void setColumnAlign(int columnIndex, int alignment) {
		setColumnAlign(getColumnName(columnIndex), alignment);
	}

	/**
	 * �����еĺ������
	 * 
	 * @param columnName
	 *            ����
	 * @param alignment
	 *            ���뷽ʽ
	 */
	public void setColumnAlign(String columnName, int alignment) {
		if (!isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = getColumn(columnName);
		Object oldRender = col.getCellRenderer();
		if (oldRender == null
				|| (oldRender != null && oldRender instanceof DefaultTableCellRenderer)) {
			DefaultTableCellRenderer cbRender = new DefaultTableCellRenderer();
			cbRender.setHorizontalAlignment(alignment);
			col.setCellRenderer(cbRender);
		}
		Object oldEditor = col.getCellEditor();
		if (oldEditor == null
				|| (oldEditor != null && oldEditor instanceof SimpleEditor)) {
			JTextField tf = new JTextField();
			tf.setHorizontalAlignment(alignment);
			col.setCellEditor(new SimpleEditor(tf, this));
		}
	}

	/**
	 * ȡ������
	 * 
	 * @param row
	 *            �к�
	 * @return ����������
	 */
	public Object[] getRowDataArray(int row) {
		int colCount = data.getColumnCount();
		Object[] rowData = new Object[colCount];

		if (row >= 0 && row < data.getRowCount()) {
			for (int i = 0; i < colCount; i++) {
				rowData[i] = data.getValueAt(row, i);
			}
		}
		return rowData;
	}

	/**
	 * ȡ�����ݵ��ַ�������\t�ָ�
	 * 
	 * @param row
	 *            �к�
	 * @return
	 */
	public String getRowData(int row) {
		StringBuffer rowData = new StringBuffer(1024);
		Object[] rowVals = getRowDataArray(row);
		for (int i = 0; i < rowVals.length; i++) {
			Object val = rowVals[i];
			if (val == null) {
				val = "";
			}
			rowData.append(val.toString());
			if (i < rowVals.length - 1) {
				rowData.append("\t");
			}
		}
		return rowData.toString();
	}

	/**
	 * ȡ���ݿ飬��\t��\n�ָ�
	 * 
	 * @return
	 */
	public String getBlockData() {
		StringBuffer sb = new StringBuffer(1024);
		acceptText();
		for (int i = 0; i < getRowCount(); i++) {
			if (this.isRowSelected(i)) {
				String row = getRowData(i);
				sb.append(row);
				if (i < getRowCount() - 1) {
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * �������ݿ���ַ���
	 * 
	 * @param ls_data
	 *            ��\t��\n�ָ�
	 * @return
	 */
	public int setBlockData(String ls_data) {
		acceptText();
		if (!StringUtils.isValidString(ls_data)) {
			return -1;
		}
		int row, col, r = 0;
		String ls_row;
		row = getSelectedRow();
		col = getSelectedColumn();
		if (row < 0) {
			row = getRowCount() + 1;
		}
		if (col < 0) {
			col = 0;
		}
		ls_data = Sentence.replace(ls_data, "\r\n", "\r", Sentence.IGNORE_CASE);
		ls_data = Sentence.replace(ls_data, "\n", "\r", Sentence.IGNORE_CASE);

		ArgumentTokenizer rows = new ArgumentTokenizer(ls_data, '\r');
		while (rows.hasMoreTokens()) {
			ls_row = rows.nextToken();
			if (!StringUtils.isValidString(ls_row)) {
				continue;
			}
			if (row >= getRowCount()) {
				row = addRow();
			}

			int li_col = col, colCount = data.getColumnCount();
			ArgumentTokenizer items = new ArgumentTokenizer(ls_row, '\t');
			String item;
			while (items.hasMoreTokens()) {
				item = items.nextToken();
				data.setValueAt(item, row, li_col);
				li_col++;
				if (li_col == colCount) {
					break;
				}
			}
			row++;
			r++;
		}
		return r;
	}

	/**
	 * ���������
	 * 
	 * @param indexCol
	 */
	public void setIndexCol(int indexCol) {
		this.indexCol = indexCol;
		setColumnAlign(indexCol, ALIGN_CENTER);
		setColumnEditable(indexCol, false, true);
		setColumnFixedWidth(indexCol, 40);
	}

	/**
	 * ���������
	 */
	public void resetIndex() {
		if (indexCol == -1) {
			return;
		}

		int c = getRowCount();
		for (int i = 0; i < c; i++) {
			data.setValueAt(new Integer(i + 1), i, indexCol);
		}
	}

	/**
	 * ���������
	 * 
	 * @param colIndex
	 *            �����
	 * @param colDesc
	 *            ������
	 * @return
	 */
	public boolean verifyColumnData(int colIndex, String colDesc) {
		return verifyColumnData(colIndex, colDesc, true);
	}

	/**
	 * ���������
	 * 
	 * @param colIndex
	 *            �����
	 * @param colDesc
	 *            ������
	 * @param caseRepeat
	 *            �Ƿ����������ظ�
	 * @return
	 */
	public boolean verifyColumnData(int colIndex, String colDesc,
			boolean caseRepeat) {
		return verifyColumnData(colIndex, colDesc, caseRepeat, GV.appFrame);
	}

	/**
	 * ���������
	 * 
	 * @param colIndex
	 *            �����
	 * @param colDesc
	 *            ������
	 * @param caseRepeat
	 *            �Ƿ����������ظ�
	 * @param parent
	 *            �����
	 * @return
	 */
	public boolean verifyColumnData(int colIndex, String colDesc,
			boolean caseRepeat, Component parent) {
		return verifyColumnData(colIndex, colDesc, true, caseRepeat, parent);
	}

	/**
	 * ���������
	 * 
	 * @param colIndex
	 *            �����
	 * @param colDesc
	 *            ������
	 * @param caseNull
	 *            �Ƿ����������Ƿ�Ϊ��
	 * @param caseRepeat
	 *            �Ƿ����������ظ�
	 * @param parent
	 *            �����
	 * @return
	 */
	public boolean verifyColumnData(int colIndex, String colDesc,
			boolean caseNull, boolean caseRepeat, Component parent) {
		acceptText();

		HashSet<Object> keys = new HashSet<Object>();
		int r = getRowCount();
		int nullCount = 0;
		String key;
		for (int i = 0; i < r; i++) {
			Object tmp = data.getValueAt(i, colIndex);
			key = tmp != null ? tmp.toString() : null;
			if (caseNull && !StringUtils.isValidString(key)) {
				GM.messageDialog(parent, mm.getMessage("jtableex.null",
						String.valueOf((i + 1)), colDesc), mm
						.getMessage("public.note"), JOptionPane.WARNING_MESSAGE); // �ڣ�{0}�е�{1}Ϊ�ա�
				return false;
			}
			if (caseRepeat && keys.contains(key)) {
				GM.messageDialog(parent,
						mm.getMessage("jtableex.repeat", colDesc, key),
						mm.getMessage("public.note"),
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			if (!StringUtils.isValidString(key)) {
				nullCount++;
			}
			if (caseRepeat && nullCount > 1) { // Ϊ���ÿ�ֵҲ�����ظ�
				GM.messageDialog(parent, mm.getMessage("jtableex.repeat")
						+ colDesc + ": " + key, mm.getMessage("public.note"),
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			keys.add(key);
		}
		return true;
	}

	/**
	 * �������Ƿ����
	 * 
	 * @param colName
	 *            ����
	 * @param vis
	 *            �Ƿ����
	 */
	public void setColumnVisible(String colName, boolean vis) {
		if (vis) {
			showColumn(colName);
		} else {
			hideColumn(colName);
		}
	}

	/**
	 * ����ѡ����
	 * 
	 * @return
	 */
	public int shiftUp() {
		return shiftRowUp(-1);
	}

	/**
	 * ����ָ���ļ�¼��
	 * 
	 * @param row
	 *            int��Ҫ�ƶ����кţ����ΪС��0���������ƶ���ǰѡ�е���
	 * @return int���ƶ�������к�,����-1��ʾû���ƶ�
	 */
	public int shiftRowUp(int row) {
		acceptText();
		int start, end;
		if (row < 0) {
			int[] rows = getSelectedRows();
			if (rows == null || rows.length == 0)
				return -1;
			start = rows[0];
			end = rows[rows.length - 1];
		} else {
			start = row;
			end = row;
		}

		if (start <= 0) {
			return -1;
		}
		data.moveRow(start, end, start - 1);
		selectRows(start - 1, end - 1);
		resetIndex();
		return start - 1;
	}

	/**
	 * ����ѡ����
	 * 
	 * @return
	 */
	public int shiftDown() {
		return shiftRowDown(-1);
	}

	/**
	 * ����ָ����
	 * 
	 * @param row
	 *            �к�
	 * @return
	 */
	public int shiftRowDown(int row) {
		acceptText();
		int start, end;
		if (row < 0) {
			int[] rows = getSelectedRows();
			if (rows == null || rows.length == 0)
				return -1;
			start = rows[0];
			end = rows[rows.length - 1];
		} else {
			start = row;
			end = row;
		}
		if (start < 0 || end >= getRowCount() - 1) {
			return -1;
		}
		data.moveRow(start, end, start + 1);
		selectRows(start + 1, end + 1);
		resetIndex();
		return start + 1;
	}

	/**
	 * ��ʾ��
	 * 
	 * @param columnName
	 *            ����
	 */
	public void showColumn(String columnName) {
		if (isColumnVisible(columnName)) {
			return;
		}
		TableColumn col = (TableColumn) columnKeeper.get(columnName);
		if (col == null) {
			return;
		}
		addColumn(col);
	}

	/**
	 * ɾ��ѡ�е��м���ɾ������ѡ����Ч��¼
	 */
	public boolean deleteSelectedRows() {
		acceptText();
		int cr = getSelectedRow();
		if (cr < 0) {
			return false;
		}
		removeCurrentRow();
		int r = getRowCount() - 1;
		clearSelection();
		if (cr < r) {
			r = cr;
		}
		selectRow(r);
		resetIndex();
		return true;
	}

	/**
	 * ɾ������ѡ�����
	 * 
	 * @return
	 */
	public boolean deleteSelectedRow() {
		acceptText();
		int cr = getSelectedRow();
		if (cr < 0) {
			return false;
		}
		data.removeRow(getSelectedRow());
		int r = getRowCount() - 1;
		clearSelection();

		if (cr < r) {
			r = cr;
		}
		selectRow(r);
		resetIndex();
		return true;
	}

	/**
	 * ɾ����ǰѡ�еļ�¼�м� ���ǲ�Ӱ��ѡ�е��м���
	 * 
	 * @return ɾ��������
	 */
	public int removeCurrentRow() {
		acceptText();
		int cr = 0;
		for (int i = this.getRowCount(); i >= 0; i--) {
			if (this.isRowSelected(i)) {
				data.removeRow(i);
				cr++;
			}
		}
		return cr;
	}

	/**
	 * ɾ��ָ����
	 * 
	 * @param row
	 *            �к�
	 * @return
	 */
	public int removeRow(int row) {
		if (row < 0) {
			return -1;
		}
		int c = data.getRowCount();
		if (row >= c) {
			return -1;
		}
		data.removeRow(row);
		int t = getRowCount();
		if (t == row) {
			row--;
		}
		selectRow(row);
		setEditingRow(row);
		return row;
	}

	/**
	 * ѡ��ָ����
	 * 
	 * @param row
	 *            �к�
	 */
	public void selectRow(int row) {
		DefaultListSelectionModel selectModel = new DefaultListSelectionModel();
		selectModel.addSelectionInterval(row, row);
		setSelectionModel(selectModel);
		setEditingRow(row);
		int n;
		n = row;
		// if (n == oldRow) {
		// return;
		// }
		fireRowfocusChanged(oldRow, row);
		oldRow = n;
	}

	/**
	 * ����Ķ����ƶ���
	 * 
	 * @param start
	 * @param end
	 */
	void selectRows(int start, int end) {
		DefaultListSelectionModel selectModel = new DefaultListSelectionModel();
		selectModel.addSelectionInterval(start, end);
		setSelectionModel(selectModel);
	}

	public void selectRows(int[] rows) {
		if (rows == null || rows.length == 0) {
			return;
		}
		DefaultListSelectionModel selectModel = new DefaultListSelectionModel();
		for (int i = 0; i < rows.length; i++) {
			selectModel.addSelectionInterval(rows[i], rows[i]);
		}
		setSelectionModel(selectModel);
	}

	/**
	 * ��ѡ��ֻ��������������
	 * 
	 * @param cols
	 */
	public void selectCols(int[] cols) {
		if (cols == null || cols.length == 0) {
			return;
		}
		setColumnSelectionInterval(cols[0], cols[cols.length - 1]);
	}

	public void selectCol(int col) {
		setColumnSelectionInterval(col, col);
	}

	/**
	 * �ڱ���SearchColumn��������Ҷ���value
	 * 
	 * @param value
	 *            Object
	 * @param searchColumn
	 *            int
	 * @return int, �ҵ��ö��󷵻ض������ڵ��к�,���򷵻�-1
	 */
	public int searchValue(Object value, int searchColumn) {
		Object o;
		for (int i = 0; i < getRowCount(); i++) {
			o = getValueAt(i, searchColumn);
			if (value.equals(o)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * �ڱ���SearchColumn���������Name
	 * 
	 * @param name
	 *            String
	 * @param searchColumn
	 *            int
	 * @return int, �ҵ��ö��󷵻ض������ڵ��к�,���򷵻�-1
	 */
	public int searchName(String name, int searchColumn, boolean caseSens) {
		Object o;
		for (int i = 0; i < getRowCount(); i++) {
			o = getValueAt(i, searchColumn);
			if (caseSens) {
				if (name.equals(o)) {
					return i;
				}
			} else {
				if (StringUtils.isValidString(o)
						&& name.equalsIgnoreCase((String) o)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * ׷����
	 * 
	 * @return
	 */
	public int addRow() {
		return addRow(true);
	}

	/**
	 * ׷����
	 * 
	 * @param resetIndex
	 *            �Ƿ����������
	 * @return
	 */
	public int addRow(boolean resetIndex) {
		return insertRow(-1, null, resetIndex);
	}

	/**
	 * ׷����
	 * 
	 * @param oa
	 *            ׷�ӵ�����������
	 * @return
	 */
	public int addRow(Object[] oa) {
		return addRow(oa, true);
	}

	/**
	 * ׷����
	 * 
	 * @param oa
	 *            ׷�ӵ�����������
	 * @param resetIndex
	 *            �Ƿ����������
	 * @return
	 */
	public int addRow(Object[] oa, boolean resetIndex) {
		return insertRow(-1, oa, resetIndex);
	}

	/**
	 * ������
	 * 
	 * @param row
	 *            �к�
	 * @param rowData
	 *            ����������
	 * @return
	 */
	public int insertRow(int row, Object[] rowData) {
		return insertRow(row, rowData, true);
	}

	/**
	 * ������
	 * 
	 * @param row
	 *            �к�
	 * @param rowData
	 *            ����������
	 * @param resetIndex
	 *            �Ƿ����������
	 * @return
	 */
	public int insertRow(int row, Object[] rowData, boolean resetIndex) {
		acceptText();
		int r;
		if (row > -1 && row < data.getRowCount()) {
			data.insertRow(row, rowData);
			r = row;
		} else {
			data.addRow(rowData);
			r = data.getRowCount() - 1;
		}

		if (resetIndex) {
			setEditingRow(r);
			resetIndex();
			selectRow(r);
		}
		return r;
	}

	/**
	 * ���Ƿ����
	 * 
	 * @param column
	 *            �б�ʶ��
	 * @return
	 */
	public boolean isColumnVisible(Object column) {
		TableColumnModel cm = this.getColumnModel();
		TableColumn tc;
		for (int i = 0; i < cm.getColumnCount(); i++) {
			tc = cm.getColumn(i);
			if (tc.getIdentifier().equals(column)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �й��仯
	 * 
	 * @param oldRow
	 *            ֮ǰѡ�����
	 * @param newRow
	 *            ��ѡ�����
	 */
	private void fireRowfocusChanged(int oldRow, int newRow) {
		if (!ifFireRowfocusChangedEvent) {
			return;
		}
		Iterator it = clickedListener.iterator();
		if (oldRow >= this.getRowCount()) {
			oldRow = -1;
		}
		while (it.hasNext()) {
			JTableExListener lis = (JTableExListener) it.next();
			lis.rowfocusChanged(oldRow, newRow);
		}
		this.resizeAndRepaint();
	}

	/**
	 * ȡ���н���仯
	 */
	public void disableRowfocusChanged() {
		ifFireRowfocusChangedEvent = false;
	}

	/**
	 * �����н���仯
	 */
	public void enableRowfocusChanged() {
		ifFireRowfocusChangedEvent = true;
	}

	/**
	 * ѡ���б仯
	 */
	public void valueChanged(ListSelectionEvent e) {
		int r = getSelectedRow();
		if (r < 0) {
			return;
		}
		if (e.getValueIsAdjusting()) {
			return;
		}
		int n = r;
		if (n == oldRow) {
			return;
		}
		fireRowfocusChanged(oldRow, r);
		oldRow = n;
	}

	/**
	 * ֮ǰѡ�����
	 */
	private int oldRow = -1;

	/**
	 * �����õı���ɫ
	 */
	private Color disabledBackColor = Color.lightGray;

	/**
	 * �����õ�ǰ��ɫ
	 */
	private Color disabledForeColor = Color.black;

	/**
	 * ���ý�ֹ�е���ɫ
	 * 
	 * @param c
	 *            Color
	 */
	public void setDisabledColor(Color foreColor, Color backColor) {
		disabledForeColor = foreColor;
		disabledBackColor = backColor;
	}

	/**
	 * focusGained
	 * 
	 * @param e
	 *            FocusEvent
	 */
	public void focusGained(FocusEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	/**
	 * 
	 * �����õĵ�Ԫ��༭��
	 */
	class DisabledEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;

		public DisabledEditor(JTextField jTxt) {
			super(jTxt);
			jTxt.setBackground(disabledBackColor);
			jTxt.setEditable(false);
		}

		public boolean isCellEditable(EventObject anEvent) {
			return true;
		}
	}

	/**
	 * ȡȱʡ�ĵ�Ԫ��༭��
	 * 
	 * @param tf
	 * @param parent
	 * @return
	 */
	public DefaultCellEditor getDefaultCellEditor(JTextField tf, JTableEx parent) {
		return new SimpleEditor(tf, parent);
	}

	/**
	 * ȱʡ�ĵ�Ԫ��༭��
	 *
	 */
	class SimpleEditor extends DefaultCellEditor implements KeyListener,
			MouseListener, FocusListener {

		private static final long serialVersionUID = 1L;
		JTableEx parent;

		public SimpleEditor(JTextField tf, JTableEx parent) {
			super(tf);
			tf.addKeyListener(this);
			tf.addFocusListener(this);
			tf.addMouseListener(this);
			tf.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			this.parent = parent;
			this.setClickCountToStart(1);
		}

		public void mouseClicked(final MouseEvent e) {
			JComponent editor = (JComponent) e.getSource();

			java.awt.Container p;
			java.awt.Container ct = editor.getTopLevelAncestor();
			int absoluteX = e.getX() + editor.getX(), absoluteY = e.getY()
					+ editor.getY();
			p = editor.getParent();
			while (p != ct) {
				absoluteX += p.getX();
				absoluteY += p.getY();
				p = p.getParent();
			}
			parent.fireClicked(absoluteX, absoluteY, e);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		/**
		 * focusGained
		 * 
		 * @param e
		 *            FocusEvent
		 */
		public void focusGained(FocusEvent e) {
			parent.focusGained(e);
		}

		/**
		 * focusLost
		 * 
		 * @param e
		 *            FocusEvent
		 */
		public void focusLost(FocusEvent e) {
			// ֻ����ֵ�Ķ���ʱ�������ܣ����򵥻������ֱ�ӱ༭���Լ���Ӱ�쵽���뷨
			// if( edited ){
			// parent.acceptText();
			// edited = false;
			// }
		}

		/**
		 * keyPressed
		 * 
		 * @param e
		 *            KeyEvent
		 */
		public void keyPressed(KeyEvent e) {
			parent.keyPressed(e);
		}

		/**
		 * keyReleased
		 * 
		 * @param e
		 *            KeyEvent
		 */
		public void keyReleased(KeyEvent e) {
			if (e != null) {
				Object src = e.getSource();
				if (src instanceof JTextField) {
					// ����editable��TextField��Ȼ�ܽ��յ������¼�
					JTextField txt = (JTextField) src;
					if (!txt.isEditable())
						return;
				}
				stateChanged(new ChangeEvent(src));
			}
		}

		/**
		 * keyTyped
		 * 
		 * @param e
		 *            KeyEvent
		 */
		public void keyTyped(KeyEvent e) {
		}
	}

	/**
	 * ����м�����
	 *
	 */
	class TableColListener implements TableColumnModelListener {
		JTableEx table;

		public TableColListener(JTableEx table) {
			this.table = table;
		}

		public void columnAdded(TableColumnModelEvent e) {
		};

		public void columnMarginChanged(ChangeEvent e) {
			table.acceptText();
		};

		public void columnMoved(TableColumnModelEvent e) {
		};

		public void columnRemoved(TableColumnModelEvent e) {
		};

		public void columnSelectionChanged(ListSelectionEvent e) {
		};
	}

	/**
	 * ����ڵı༭�������������¼�,�����ϲ�ı༭״̬����
	 */
	public void stateChanged(ChangeEvent e) {
	}

}
