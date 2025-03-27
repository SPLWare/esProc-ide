package com.scudata.ide.spl.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.scudata.app.common.Section;
import com.scudata.common.IntArrayList;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.control.TransferableObject;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * 
 * �ⲿ���������ؼ�
 *
 */
public class TableExtLibs extends JScrollPane {

	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/** ������ */
	public static final byte COL_NAME = 1;
	/** ѡ���� */
	private final byte COL_SELECT = 2;

	/** ��� */
	private final String TITLE_INDEX = mm.getMessage("tableselectname.index");
	/** Ŀ¼�� */
	private final String TITLE_NAME = IdeSplMessage.get().getMessage(
			"tableselectname.dirname");
	/** ѡ�� */
	private final String TITLE_SELECT = mm.getMessage("tableselectname.select");

	/**
	 * ��ؼ�
	 */
	private JTableEx tableNames;

	/**
	 * �Ƿ�ȫѡ
	 */
	private boolean selectAll = false;

	/**
	 * �Ѵ��ڵ�����
	 */
	private Vector<String> existNames;

	/**
	 * �Ѵ��ڵ�������ʾ����ɫ
	 */
	private boolean existColor = true;

	/**
	 * �Ƿ���ֹ�仯
	 */
	private boolean preventChange = false;

	/**
	 * ��������
	 */
	private String filter = null;

	/**
	 * ѡ�������ӳ��
	 */
	private Map<String, Boolean> nameSelected = new HashMap<String, Boolean>();

	/**
	 * �����
	 */
	private Component parent;

	/**
	 * ���캯��
	 */
	public TableExtLibs(JDialog parent) {
		this.parent = parent;
		tableNames = new JTableEx(TITLE_INDEX + "," + TITLE_NAME + ","
				+ TITLE_SELECT) {

			private static final long serialVersionUID = 1L;

			public void setValueAt(Object aValue, int row, int column) {
				if (!isItemDataChanged(row, column, aValue)) {
					return;
				}
				super.setValueAt(aValue, row, column);
				if (preventChange) {
					return;
				}
				setDataChanged();
				if (column == COL_SELECT) {
					boolean selected = aValue != null
							&& ((Boolean) aValue).booleanValue();
					rowSelectedChanged(row, selected);
					String name = (String) data.getValueAt(row, COL_NAME);
					nameSelected.put(name, new Boolean(selected));
					Object tmp;
					boolean rSelected;
					for (int i = 0; i < getRowCount(); i++) {
						tmp = data.getValueAt(i, COL_SELECT);
						rSelected = tmp != null
								&& ((Boolean) tmp).booleanValue();
						if (selected != rSelected) {
							selectAll = false;
							tableNames.getTableHeader().repaint();
							return;
						}
					}
					selectAll = selected;
					allRowsSelected(selectAll);
					tableNames.getTableHeader().repaint();
				}
			}

			public void rowfocusChanged(int oldRow, int newRow) {
				rowChanged(oldRow, newRow);
			}

			public void doubleClicked(int xpos, int ypos, int row, int col,
					MouseEvent e) {
				doubleClick(row, col);
			}
		};
		init();
	}

	/**
	 * �����Ѵ��ڵ�������ʾ����ɫ
	 * 
	 * @param existColor
	 */
	public void setExistColor(boolean existColor) {
		this.existColor = existColor;
	}

	/**
	 * ȡѡ�����
	 * 
	 * @return
	 */
	public int getSelectedRow() {
		return tableNames.getSelectedRow();
	}

	/**
	 * �й��仯��
	 * 
	 * @param oldRow
	 *            ���к�
	 * @param newRow
	 *            ���к�
	 */
	public void rowChanged(int oldRow, int newRow) {
	}

	/**
	 * ���˫��
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 */
	public void doubleClick(int row, int col) {
	}

	/**
	 * ��ѡ��״̬�仯
	 * 
	 * @param row
	 *            �к�
	 * @param selected
	 *            �Ƿ�ѡ����
	 */
	public void rowSelectedChanged(int row, boolean selected) {
	}

	/**
	 * ����ȫѡ/ȫ��ѡ������
	 * 
	 * @param allSelected
	 *            �Ƿ�ѡ��
	 */
	public void allRowsSelected(boolean allSelected) {
	}

	/**
	 * ȡ����
	 * 
	 * @return
	 */
	public int getRowCount() {
		return tableNames.getRowCount();
	}

	/**
	 * ȡ��Ԫ��ֵ
	 * 
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @return
	 */
	public Object getValueAt(int row, int col) {
		return tableNames.data.getValueAt(row, col);
	}

	/**
	 * ���ù�������
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * ���������б�
	 * 
	 * @param names
	 *            ����
	 * @param reset
	 *            �Ƿ�����
	 * @param selectExist
	 *            �Ƿ�ѡ���Ѿ��ڵ�����
	 */
	public synchronized void setNames(Vector<String> names, boolean reset,
			boolean selectExist) {
		try {
			preventChange = true;
			tableNames.acceptText();
			tableNames.removeAllRows();
			selectAll = false;
			if (!reset)
				nameSelected.clear();
			if (names == null) {
				return;
			}
			int size = names.size();
			String name;
			Pattern p = null;
			if (StringUtils.isValidString(filter)) {
				try {
					p = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
				} catch (Throwable t) {
				}
			}
			boolean isAllSelected = true;
			Matcher m;
			Boolean selected;
			for (int r = 0; r < size; r++) {
				Object n = names.get(r);
				name = n == null ? null : n.toString();
				if (p != null) {
					m = p.matcher(name);
					if (!m.find())
						continue;
				}
				selected = Boolean.FALSE;
				if (reset) {
					Object tmp = nameSelected.get(name);
					if (tmp != null && tmp instanceof Boolean) {
						selected = (Boolean) tmp;
					}
				} else if (selectExist && existNames != null) {
					selected = new Boolean(existNames.contains(name));
				}
				tableNames.insertRow(-1, new Object[] { new Integer(r + 1),
						name, selected }, false);
				if (!selected.booleanValue())
					isAllSelected = false;
				if (!reset)
					nameSelected.put(name, selected);
			}
			tableNames.resetIndex();
			if (tableNames.getRowCount() > 0) {
				tableNames.selectRow(0);
			}

			if (isAllSelected) {
				selectAll = true;
				tableNames.getTableHeader().repaint();
			}
			repaint();
		} finally {
			preventChange = false;
		}
	}

	/**
	 * ȡѡ�������
	 * 
	 * @param tableName
	 *            ����������б�������tableName.fieldName���ޱ���ʱֱ�ӷ���fieldName��
	 * @return
	 */
	public String[] getSelectedNames(String tableName) {
		return getSelectedNames(tableName, ".");
	}

	/**
	 * ȡѡ�������
	 * 
	 * @param tableName
	 *            ����������б�������tableName.fieldName���ޱ���ʱֱ�ӷ���fieldName��
	 * @param opt
	 *            �������ֶ��������ӷ���
	 * @return
	 */
	public String[] getSelectedNames(String tableName, String opt) {
		tableNames.acceptText();
		int count = tableNames.getRowCount();
		Section sec = new Section();
		Object tmp;
		String name;
		for (int i = 0; i < count; i++) {
			tmp = tableNames.data.getValueAt(i, COL_SELECT);
			if (tmp == null || !((Boolean) tmp).booleanValue()) {
				continue;
			}
			tmp = tableNames.data.getValueAt(i, COL_NAME);
			if (StringUtils.isValidString(tmp)) {
				name = (String) tmp;
				if (StringUtils.isValidString(tableName)) {
					if (!name.startsWith(tableName + opt)) {
						name = tableName + opt + name;
					}
				}
				sec.addSection(name);
			}
		}
		return sec.toStringArray();
	}

	/**
	 * ȡѡ������
	 * 
	 * @return
	 */
	public int[] getSelectedIndexes() {
		tableNames.acceptText();
		int count = tableNames.getRowCount();
		if (count == 0)
			return null;
		IntArrayList list = new IntArrayList();
		for (int i = 0; i < count; i++) {
			if (isRowSelected(i))
				list.addInt(i);
		}
		if (list.isEmpty())
			return null;
		return list.toIntArray();
	}

	/**
	 * ȡ���Ƿ�ѡ��
	 * 
	 * @param row
	 *            �к�
	 * @return
	 */
	public boolean isRowSelected(int row) {
		Object s = tableNames.data.getValueAt(row, COL_SELECT);
		return s != null && ((Boolean) s).booleanValue();
	}

	/**
	 * �������Ƿ�ѡ��
	 * 
	 * @param row
	 *            �к�
	 * @param selected
	 *            �Ƿ�ѡ��
	 */
	public void setRowSelected(int row, boolean selected) {
		tableNames.data.setValueAt(new Boolean(selected), row, COL_SELECT);
		tableNames.acceptText();
	}

	/**
	 * �����Ѿ����ڵ�����
	 * 
	 * @param existNames
	 *            �Ѿ����ڵ�����
	 */
	public void setExistNames(Vector<String> existNames) {
		this.existNames = existNames;
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		this.getViewport().add(tableNames);
		tableNames.setRowHeight(20);
		tableNames.getTableHeader().setReorderingAllowed(false);
		tableNames.setIndexCol(0);
		tableNames.setColumnEditable(COL_NAME, false);
		tableNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = tableNames.getTableHeader();
		header.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int col = tableNames.columnAtPoint(e.getPoint());
				if (col == COL_SELECT) {
					selectAll(!selectAll);
					setDataChanged();
				}
			}
		});
		final int selectWidth = 75;
		tableNames.getColumn(COL_SELECT).setHeaderRenderer(
				new DefaultTableCellRenderer() {

					private static final long serialVersionUID = 1L;

					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						JCheckBox cb = new JCheckBox(TITLE_SELECT);
						cb.setSelected(selectAll);
						JPanel p = new JPanel(new BorderLayout());
						p.add(cb, BorderLayout.CENTER);
						p.setFont(table.getFont());
						p.setBorder(new TableHeaderBorder());
						p.setPreferredSize(new Dimension(selectWidth, 20));
						return p;
					}
				});
		tableNames.setColumnFixedWidth(COL_SELECT, selectWidth);
		tableNames.setColumnCheckBox(COL_SELECT);
		tableNames.getColumn(COL_NAME).setCellRenderer(
				new DefaultTableCellRenderer() {

					private static final long serialVersionUID = 1L;

					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						Component c = super
								.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);
						if (existColor && existNames != null
								&& existNames.contains(value)) {
							c.setForeground(Color.BLUE);
						} else {
							c.setForeground(Color.BLACK);
						}
						return c;
					}
				});

		DragGestureListener dgl = new DragGestureListener() {
			public void dragGestureRecognized(DragGestureEvent dge) {
				try {
					String[] names = getSelectedNames(null);
					if (names == null) {
						return;
					}
					Transferable tf = new TransferableObject(names);
					if (tf != null) {
						dge.startDrag(
								Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
								tf);
					}
				} catch (Exception x) {
					GM.showException(parent, x);
				}
			}
		};
		DragSource ds = DragSource.getDefaultDragSource();
		ds.createDefaultDragGestureRecognizer(tableNames,
				DnDConstants.ACTION_COPY, dgl);
	}

	/**
	 * ��ʼ����ť
	 * 
	 * @param b
	 */
	public void initButton(JButton b) {
		Dimension d = new Dimension(22, 22);
		b.setMaximumSize(d);
		b.setMinimumSize(d);
		b.setPreferredSize(d);
	}

	/**
	 * ȫѡ/ȫ��ѡ
	 * 
	 * @param selected
	 *            �Ƿ�ȫѡ
	 */
	public void selectAll(boolean selected) {
		selectAll = selected;
		int count = tableNames.getRowCount();
		for (int r = 0; r < count; r++) {
			tableNames.data.setValueAt(new Boolean(selectAll), r, COL_SELECT);
			nameSelected.put((String) tableNames.data.getValueAt(r, COL_NAME),
					new Boolean(selectAll));
		}
		tableNames.getTableHeader().repaint();
		allRowsSelected(selectAll);
	}

	/**
	 * ���ݱ仯��
	 */
	protected void setDataChanged() {
	}
}