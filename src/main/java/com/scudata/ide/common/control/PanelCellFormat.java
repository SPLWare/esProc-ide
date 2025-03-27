package com.scudata.ide.common.control;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import com.scudata.common.MessageManager;
import com.scudata.common.Types;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JListEx;

/**
 * ��ʽ�༭���
 */
abstract public class PanelCellFormat extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * ��ʽ�ı���ؼ�
	 */
	private JTextField jTFFormat = new JTextField();
	/**
	 * ��ʽ�����б�ؼ�����ֵ,����,����,ʱ��,����ʱ��,����,��ѧ������
	 */
	private JListEx jListType = new JListEx(
			mm.getMessage("dialogcellformat.colnames"));
	/**
	 * �����б�ؼ�
	 */
	private JList<String> jListSample = new JList<String>();

	/**
	 * ��ֵ
	 */
	private static final int TYPE_NUMBER = 0;
	/**
	 * ����
	 */
	private static final int TYPE_CURRENCY = 1;
	/**
	 * ����
	 */
	private static final int TYPE_DATE = 2;
	/**
	 * ʱ��
	 */
	private static final int TYPE_TIME = 3;
	/**
	 * ����ʱ��
	 */
	private static final int TYPE_DATE_TIME = 4;
	/**
	 * �ٷ���
	 */
	private static final int TYPE_PERCENT = 5;
	/**
	 * ��ѧ����
	 */
	private static final int TYPE_SCIENTIFIC_COUNTING = 6;

	/**
	 * ���캯��
	 */
	public PanelCellFormat(JDialog parent) {
		try {
			rqInit();
		} catch (Exception e) {
			GM.showException(parent, e);
		}
	}

	/**
	 * ���ø�ʽ
	 * 
	 * @param format
	 *            ��ʽ��
	 */
	public void setFormat(String format) {
		jTFFormat.setText(format);
	}

	/**
	 * ȡ��ʽ
	 * 
	 * @return ��ʽ��
	 */
	public String getFormat() {
		return jTFFormat.getText();
	}

	/**
	 * ȡ��ʽ����
	 * 
	 * @return
	 */
	public byte getFormatType() {
		switch (jListType.getSelectedIndex()) {
		case TYPE_NUMBER:
		case TYPE_CURRENCY:
		case TYPE_PERCENT:
		case TYPE_SCIENTIFIC_COUNTING:
			return Types.DT_DOUBLE;
		case TYPE_DATE:
			return Types.DT_DATE;
		case TYPE_TIME:
			return Types.DT_TIME;
		case TYPE_DATE_TIME:
			return Types.DT_DATETIME;
		default:
			return Types.DT_DEFAULT;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void rqInit() throws Exception {
		setLayout(new GridBagLayout());
		JLabel jLabel1 = new JLabel();
		JLabel jLabel2 = new JLabel();
		jLabel1.setText(mm.getMessage("dialogcellformat.format")); // ��ʽ
		jLabel2.setText(mm.getMessage("dialogcellformat.type")); // ����
		jListType
				.addListSelectionListener(new PanelCellFormat_jListType_listSelectionAdapter(
						this));
		jListSample
				.addListSelectionListener(new PanelCellFormat_jListSample_listSelectionAdapter(
						this));
		jListSample
				.addMouseListener(new PanelCellFormat_jListSample_mouseAdapter(
						this));
		add(jLabel1, GM.getGBC(1, 1));
		add(jTFFormat, GM.getGBC(1, 2, true));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(jLabel2, BorderLayout.NORTH);
		add(panel1, GM.getGBC(2, 1, false, true));
		JPanel panel2 = new JPanel();
		JScrollPane jScrollPane1 = new JScrollPane();
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane1.getViewport().add(jListType, null);
		jScrollPane2.getViewport().add(jListSample, null);
		panel2.setLayout(new GridLayout(1, 2, 10, 0));
		panel2.add(jScrollPane1);
		panel2.add(jScrollPane2);
		add(panel2, GM.getGBC(2, 2, true, true));
		jListType.setSelectedIndex(0);
	}

	/**
	 * ����ʽ���ͱ仯ʱ����ʾ��Ӧ������
	 * 
	 * @param e
	 */
	void jListType_valueChanged(ListSelectionEvent e) {
		switch (jListType.getSelectedIndex()) {
		case TYPE_NUMBER:
			jListSample.setListData(new String[] { "#0.00", "#.00", "#.#",
					"#0.000", "#.000", "#,##0.00", "#,###.00", "#,###.#",
					"#,##0.000", "#,###.000" });
			break;
		case TYPE_CURRENCY:
			jListSample.setListData(new String[] { "��#0.00", "��#.00", "��#.#",
					"��#0.000", "��#.000", "��#,##0.00", "��#,###.00", "��#,###.#",
					"��#,##0.000", "��#,###.000", "$#0.00", "$#.00", "$#.#",
					"$#0.000", "$#.000", "$#,##0.00", "$#,###.00", "$#,###.#",
					"$#,##0.000", "$#,###.000" });
			break;
		case TYPE_DATE:
			jListSample.setListData(GC.DATE_FORMATS);
			break;
		case TYPE_TIME:
			jListSample.setListData(GC.TIME_FORMATS);
			break;
		case TYPE_DATE_TIME:
			jListSample.setListData(GC.DATE_TIME_FORMATS);
			break;
		case TYPE_PERCENT:
			jListSample.setListData(new String[] { "#0.00%", "#.00%", "#.#%",
					"#0.000%", "#.000%", "#,##0.00%", "#,###.00%", "#,###.#%",
					"#,##0.000%", "#,###.000%" });
			break;
		case TYPE_SCIENTIFIC_COUNTING:
			jListSample.setListData(new String[] { "0.#E0", "0.##E0",
					"0.###E0", "00.#E0", "00.##E0", "00.###E0", "##0.#E0",
					"##0.##E0", "##0.###E0" });
			break;
		}

	}

	/**
	 * ����ʽѡ�к�
	 */
	public abstract void formatSelected();

	/**
	 * ѡ����������
	 * 
	 * @param e
	 */
	void jListSample_valueChanged(ListSelectionEvent e) {
		jTFFormat.setText(jListSample.getSelectedValue() == null ? ""
				: jListSample.getSelectedValue().toString());
	}

	/**
	 * ���˫������������formatSelected()����
	 * 
	 * @param e
	 */
	void jListSample_mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			formatSelected();
		}
	}
}

class PanelCellFormat_jListType_listSelectionAdapter implements
		javax.swing.event.ListSelectionListener {
	PanelCellFormat adaptee;

	PanelCellFormat_jListType_listSelectionAdapter(PanelCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void valueChanged(ListSelectionEvent e) {
		adaptee.jListType_valueChanged(e);
	}
}

class PanelCellFormat_jListSample_listSelectionAdapter implements
		javax.swing.event.ListSelectionListener {
	PanelCellFormat adaptee;

	PanelCellFormat_jListSample_listSelectionAdapter(PanelCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void valueChanged(ListSelectionEvent e) {
		adaptee.jListSample_valueChanged(e);
	}
}

class PanelCellFormat_jListSample_mouseAdapter extends
		java.awt.event.MouseAdapter {
	PanelCellFormat adaptee;

	PanelCellFormat_jListSample_mouseAdapter(PanelCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.jListSample_mouseClicked(e);
	}
}
