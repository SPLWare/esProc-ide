package com.scudata.ide.spl.etl;

import java.awt.Dialog;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.chart.Consts;
import com.scudata.dm.Sequence;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.ide.common.swing.JTextAreaEditor;
import com.scudata.ide.spl.chart.ImageEditor;
import com.scudata.ide.spl.chart.ImageRenderer;
import com.scudata.ide.spl.chart.box.DefaultParamTableRender;
import com.scudata.ide.spl.dialog.DialogTextEditor;
import com.scudata.ide.spl.resources.ChartMessage;
import com.scudata.util.Variant;

/**
 * �����༭��
 */
public class TableParamEdit extends JTableEx {
	private static final long serialVersionUID = 924940299890651265L;
	private String NAMECOL = ChartMessage.get().getMessage("label.propname"); // "��������";
	private String VALUECOL = ChartMessage.get().getMessage("label.propvalue"); // "����ֵ";
	private String EXPCOL = ChartMessage.get().getMessage("label.propexp"); // "����ֵ���ʽ";
	private String EDITSTYLECOL = "editstyle";
	private String OBJCOL = "objcol"; // ParamInfo���������null����������

	private int iNAMECOL = 1;
	private int iVALUECOL = 2;
	private int iEXPCOL = 3;
	private int iEDITSTYLECOL = 4;
	private int iOBJCOL = 5; // ParamInfo���������null����������

	private HashMap<String, ArrayList<Object[]>> hiddenMap = new HashMap<String, ArrayList<Object[]>>(); // �����е�����
																											// ����ArrayList
																											// ÿ������Object[]

	private JDialog owner;

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            ������
	 */
	public TableParamEdit(JDialog owner) {
		this.owner = owner;
		String[] colNames = new String[] { " ", NAMECOL, VALUECOL, EXPCOL,
				EDITSTYLECOL, OBJCOL };
		data.setColumnIdentifiers(colNames);

		this.setRowHeight(25);
		DefaultParamTableRender render = new DefaultParamTableRender();
		TableColumn tc;
		tc = getColumn(0);
		tc.setMaxWidth(20);
		tc.setMinWidth(20);
		tc.setCellEditor(new ImageEditor());
		tc.setCellRenderer(new ImageRenderer());

		tc = getColumn(iNAMECOL);
		tc.setCellRenderer(render);
		tc.setPreferredWidth(200);

		tc = getColumn(iVALUECOL);
		tc.setCellEditor(new EtlRowEditor(this, iEDITSTYLECOL, owner));
		tc.setCellRenderer(new EtlRowRenderer(iEDITSTYLECOL, owner));

		tc = getColumn(iEXPCOL);
		tc.setCellEditor(new JTextAreaEditor(this));
		tc.setCellRenderer(render);

		setColumnVisible(EDITSTYLECOL, false);
		setColumnVisible(OBJCOL, false);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSelectionAllowed(true); // �п�ѡ
		setRowSelectionAllowed(true); // �п�ѡ
	}

	/**
	 * ���ò�����Ϣ�б�
	 * 
	 * @param list
	 *            ������Ϣ�б�
	 */
	public void setParamEdit(ParamInfoList list) {
		acceptText();
		data.setRowCount(0);
		hiddenMap.clear();
		TableColumn tc;
		tc = getColumn(iVALUECOL);
		tc.setCellEditor(new EtlRowEditor(this, iEDITSTYLECOL, owner));
		tc.setCellRenderer(new EtlRowRenderer(iEDITSTYLECOL, owner));

		// ��������
		java.util.List<ParamInfo> root = list.getRootParams();
		for (int i = 0; i < root.size(); i++) {
			ParamInfo pi = root.get(i);
			Object row[] = new Object[6];
			row[0] = null;
			row[iNAMECOL] = pi.getTitle();
			Object value = pi.getValue();
			if (value instanceof String) {
				if (value.toString().startsWith("=")) {
					row[iEXPCOL] = value.toString().substring(1);
				} else
					row[iVALUECOL] = value;
			} else {
				row[iVALUECOL] = value;
			}
			row[iEDITSTYLECOL] = new Integer(pi.getInputType());
			row[iOBJCOL] = pi;
			data.addRow(row);
		}
		java.util.List<String> groups = list.getGroupNames();
		if (groups != null) {
			for (int i = 0; i < groups.size(); i++) {
				String groupName = groups.get(i);
				Object[] grow = new Object[6];
				if (i == groups.size() - 1)
					grow[0] = new Byte(GC.TYPE_LASTMINUS);
				else
					grow[0] = new Byte(GC.TYPE_MINUS);
				grow[iNAMECOL] = groupName;
				grow[iEDITSTYLECOL] = new Integer(Consts.INPUT_NORMAL);
				data.addRow(grow);
				ArrayList<Object[]> grows = new ArrayList<Object[]>();
				java.util.List<ParamInfo> glist = list.getParams(groupName);
				for (int j = 0; j < glist.size(); j++) {
					ParamInfo pi = glist.get(j);
					Object row[] = new Object[6];
					if (j == glist.size() - 1)
						row[0] = new Byte(GC.TYPE_LASTNODE);
					else
						row[0] = new Byte(GC.TYPE_NODE);
					row[iNAMECOL] = pi.getTitle();
					Object value = pi.getValue();
					if (value instanceof String) {
						if (value.toString().startsWith("=")) {
							row[iEXPCOL] = value.toString().substring(1);
							row[iVALUECOL] = pi.getDefValue();
						} else
							row[iVALUECOL] = value;
					} else {
						if (value instanceof ArrayList) {
							row[iVALUECOL] = arrayList2Series((ArrayList) value);
							pi.setValue(row[iVALUECOL]);
						} else
							row[iVALUECOL] = value;
					}
					row[iEDITSTYLECOL] = new Integer(pi.getInputType());
					row[iOBJCOL] = pi;
					data.addRow(row);
					grows.add(row);
				}
				hiddenMap.put(groupName, grows);
			}
		}
	}

	/**
	 * ʵ�����˫�������¼�
	 * 
	 * @param xpos
	 *            ������
	 * @param ypos
	 *            ������
	 * @param row
	 *            �к�
	 * @param col
	 *            �к�
	 * @param e
	 *            ����¼�
	 */
	public void doubleClicked(int xpos, int ypos, int row, int col, MouseEvent e) {
		if (col == 0) {
			return;
		}
		if (!isCellEditable(row, col)) {
			return;
		}
		if (col == iVALUECOL) {
			Object editStyle = data.getValueAt(row, iEDITSTYLECOL);
			byte es = ((Number) editStyle).byteValue();
			if (es == Consts.INPUT_FILE) {
				String fileExt = "ctx,btx,csv,txt,xls,xlsx";
				File file = GM.dialogSelectFile(owner, fileExt);
				if (file != null) {
					String txt = file.getAbsolutePath();
					Object src = e.getSource();
					if (src instanceof JTextField) {
						JTextField tf = (JTextField) src;
						tf.setText(txt);
					}
					setValueAt(txt, row, col);
					acceptText();
				}
				return;
			} else if (es != Consts.INPUT_NORMAL
					&& es != EtlConsts.INPUT_ONLYPROPERTY) {
				// ������ʽ�Լ�����������ʱ��ʹ�ú����ı����봰��
				return;
			}
		}

		Object val = data.getValueAt(row, col);
		if (val != null && !(val instanceof String)) {
			return;
		}

		DialogTextEditor dte = new DialogTextEditor(owner);
		String exp = (String) val;
		dte.setText(exp);
		dte.setVisible(true);
		if (dte.getOption() == JOptionPane.OK_OPTION) {
			String txt = dte.getText();
			Object src = e.getSource();
			if (src instanceof JTextField) {
				JTextField tf = (JTextField) src;
				tf.setText(txt);
			}
			setValueAt(txt, row, col);
			acceptText();
		}

	}

	/**
	 * ʵ��������¼�
	 */
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (e.getButton() == MouseEvent.BUTTON1) {
			int row = getSelectedRow();
			int col = getSelectedColumn();
			if (col != 0) { // �����������ʱ��������չ��������
				return;
			}
			if (data.getValueAt(row, col) == null
					|| !(data.getValueAt(row, col) instanceof Byte)) {
				return;
			}
			byte oldType = ((Byte) data.getValueAt(row, col)).byteValue();
			byte newType = oldType;
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			Object rowData[];
			String key = (String) data.getValueAt(row, 1);
			acceptText();
			switch (oldType) {
			case GC.TYPE_MINUS:
				newType = GC.TYPE_PLUS;
			case GC.TYPE_LASTMINUS:
				newType = GC.TYPE_LASTPLUS;
				while (row + 1 < data.getRowCount()
						&& data.getValueAt(row + 1, iOBJCOL) != null) { // �ҵ���β����һ��������Ϊֹ
					rowData = new Object[data.getColumnCount()];
					for (int c = 0; c < data.getColumnCount(); c++) {
						rowData[c] = data.getValueAt(row + 1, c);
					}
					list.add(rowData);
					data.removeRow(row + 1);
				}
				hiddenMap.put(key, list);
				break;
			case GC.TYPE_PLUS:
				newType = GC.TYPE_MINUS;
			case GC.TYPE_LASTPLUS:
				newType = GC.TYPE_LASTMINUS;
				expand(key, row + 1);
				break;
			}
			data.setValueAt(new Byte(newType), row, col);
			acceptText();
		}
	}

	/**
	 * �ж�ָ���������Ƿ�����༭
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public boolean isCellEditable(int row, int column) {
		ParamInfo info = (ParamInfo) data.getValueAt(row, iOBJCOL);
		if (info == null) { // �����в��ܱ༭
			return false;
		}
		if (column == iEXPCOL) {
			Object val = data.getValueAt(row, iVALUECOL);
			if (val instanceof Boolean) {
				return false;
			}
			int type = (Integer) data.getValueAt(row, iEDITSTYLECOL);
			return !EtlConsts.isDisableExpEditType(type);
		}
		return column != iNAMECOL;
	}

	/**
	 * ʵ��ĳ������ֵ���޸ĺ󴥷����¼�
	 * 
	 * @param aValue
	 *            �µ�����ֵ
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setValueAt(Object aValue, int row, int column) {
		Object oldValue = getValueAt(row, column);
		if (oldValue == null)
			oldValue = "";
		if (column == iEXPCOL) {
			aValue = aValue.toString().trim();
		}
		if (Variant.isEquals(aValue, oldValue)) {
			return;
		}
		super.setValueAt(aValue, row, column);
		ParamInfo info = (ParamInfo) data.getValueAt(row, iOBJCOL);
		if (info != null) {
			if (column == iVALUECOL) { // ����ֵ��Ҫ�ڱ��ʽ����ʾ���������뷽ʽѡ���ֵ
				super.setValueAt(toExpString(info, aValue), row, iEXPCOL);
			} else if (column == iEXPCOL) { // ������ʽ��Ҫ���ܽ�����ֵ��ʾ��ֵ�ж�Ӧ����ʾֵ
				super.setValueAt(toValueObject(info, aValue.toString()), row,
						iVALUECOL);
				if (aValue.toString().trim().length() == 0) {
					aValue = info.getDefValue();
				} else
					aValue = "=" + aValue.toString();
			} else
				return;
			info.setValue(aValue);
		}
	}

	private void expand(String groupName, int row) {
		ArrayList<Object[]> list = hiddenMap.get(groupName);
		for (int i = 0; i < list.size(); i++) {
			data.insertRow(row + i, list.get(i));
		}
	}

	/**
	 * ���ܵ�ǰ�༭�õ���ֵ
	 */
	public void acceptText() {
		if (this.isEditing()) {
			this.getCellEditor().stopCellEditing();
		}
	}

	/**
	 * ��ֵvalue���ݲ�����Ϣinfo��������Ͷ���ת��Ϊ�ı���
	 * 
	 * @param info
	 *            ������Ϣ
	 * @param value
	 *            ֵ
	 * @return �ı���
	 */
	public static String toExpString(ParamInfo info, Object value) {
		int inputType = info.getInputType();
		switch (inputType) {
		case Consts.INPUT_COLOR:
		case Consts.INPUT_LINESTYLE:
		case Consts.INPUT_TEXTURE:
		case Consts.INPUT_POINTSTYLE:
		case Consts.INPUT_FONTSTYLE:
		case Consts.INPUT_COLUMNSTYLE:
		case Consts.INPUT_DROPDOWN:
		case Consts.INPUT_CHARTCOLOR:
		case Consts.INPUT_ARROW:
		case Consts.INPUT_TICKS:
		case Consts.INPUT_UNIT:
		case Consts.INPUT_COORDINATES:
		case Consts.INPUT_AXISLOCATION:
		case Consts.INPUT_FONTSIZE:
		case Consts.INPUT_HALIGN:
		case Consts.INPUT_VALIGN:
		case Consts.INPUT_LEGENDICON:
			return value == null ? "" : value.toString();
		default:
			return "";
		}
	}

	/**
	 * ���ı���ֵexp���ݲ�����Ϣinfo������Ͷ���ת��Ϊ��Ӧ��ֵ
	 * 
	 * @param info
	 *            ������Ϣ
	 * @param exp
	 *            �ı���ֵ
	 * @return ����ֵ
	 */
	public static Object toValueObject(ParamInfo info, String exp) {
		int inputType = info.getInputType();
		switch (inputType) {
		case Consts.INPUT_NORMAL:
		case Consts.INPUT_EXP:
		case Consts.INPUT_DATE:
			return "";
		case Consts.INPUT_CHARTCOLOR:
			return info.getDefValue();
		default:
			Object value = PgmNormalCell.parseConstValue(exp);
			if (isRightType(inputType, value))
				return value;
			return info.getDefValue();
		}
	}

	/**
	 * �ж�ֵvalue�Ƿ������typeƥ��
	 * 
	 * @param type
	 *            �༭������
	 * @param value
	 *            ����ֵ
	 * @return һ��ʱ����true�����򷵻�false
	 */
	public static boolean isRightType(int type, Object value) {
		switch (type) {
		case Consts.INPUT_ANGLE:
		case Consts.INPUT_ARROW:
		case Consts.INPUT_AXISLOCATION:
		case Consts.INPUT_COLOR:
		case Consts.INPUT_COLUMNSTYLE:
		case Consts.INPUT_COORDINATES:
		case Consts.INPUT_FONTSIZE:
		case Consts.INPUT_FONTSTYLE:
		case Consts.INPUT_HALIGN:
		case Consts.INPUT_VALIGN:
		case Consts.INPUT_INTEGER:
		case Consts.INPUT_LEGENDICON:
		case Consts.INPUT_LINESTYLE:
		case Consts.INPUT_POINTSTYLE:
		case Consts.INPUT_TEXTURE:
		case Consts.INPUT_TICKS:
		case Consts.INPUT_UNIT:
			return value instanceof Integer;
		case Consts.INPUT_DOUBLE:
			return value instanceof Double;
		case Consts.INPUT_FONT:
			return value instanceof String;
		case Consts.INPUT_CHECKBOX:
			return value instanceof Boolean;
		}
		return false;
	}

	private Sequence arrayList2Series(ArrayList list) {
		Sequence series = new Sequence();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof ArrayList) {
				series.add(arrayList2Series((ArrayList) o));
			} else
				series.add(o);
		}
		return series;
	}

}
