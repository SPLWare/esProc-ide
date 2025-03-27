package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.dm.Context;
import com.scudata.dm.Param;
import com.scudata.dm.ParamList;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ��������Ի���
 *
 */
public class DialogInputArgument extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/** ��ʾ������ */
	private final int COL_DISP = 0;
	/** ��ֵ�� */
	private final int COL_VALUE = 1;
	/** ������ */
	private final int COL_NAME = 2;
	/** ����˵�� */
	private final String TITLE_DISP = mm.getMessage("dialoginputargument.name");
	/** ��ֵ�� */
	private final String TITLE_VALUE = mm
			.getMessage("dialoginputargument.value");
	/** ��ʵ���� */
	private final String TITLE_NAME = "TITLE_NAME";

	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CLOSED_OPTION;

	/**
	 * �������ؼ�
	 */
	private JTableEx paraTable = new JTableEx(new String[] { TITLE_DISP,
			TITLE_VALUE, TITLE_NAME }) {
		private static final long serialVersionUID = 1L;

		public void doubleClicked(int xpos, int ypos, int row, int col,
				MouseEvent e) {
			if (col != COL_VALUE) {
				return;
			}
			GM.dialogEditTableText(DialogInputArgument.this, paraTable, row,
					col);
		}
	};

	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();

	/**
	 * ���캯��
	 * 
	 * @param ctx
	 *            ������
	 */
	public DialogInputArgument(Context ctx) {
		super(GV.appFrame, "���ò���ֵ��˫����ֵ�е����༭���ڡ�", true);
		try {
			initUI();
			setSize(400, 300);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
			setResizable(true);
			resetText();
		} catch (Throwable t) {
			GM.showException(this, t);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetText() {
		setTitle(mm.getMessage("dialoginputargument.title")); // ���ò���ֵ��˫����ֵ�е����༭���ڡ�
		jBOK.setText(mm.getMessage("button.ok"));
		jBCancel.setText(mm.getMessage("button.cancel"));
	}

	/**
	 * ȡ�˳�ѡ��
	 * 
	 * @return
	 */
	public int getOption() {
		return m_option;
	}

	/**
	 * �����Ƿ�ɱ༭
	 * 
	 * @param editable
	 *            �Ƿ�ɱ༭
	 */
	public void setEditable(boolean editable) {
		if (!editable) {
			paraTable.setColumnEditable(COL_VALUE, false);
		}
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		this.getContentPane().setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		JScrollPane jScrollPane1 = new JScrollPane();
		panel1.setLayout(new VFlowLayout());
		panel1.setForeground(Color.black);
		paraTable.setCellSelectionEnabled(false);
		jBOK.setDefaultCapable(true);
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogInputArgument_jBOK_actionAdapter(this));

		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.setDefaultCapable(false);
		jBCancel.addActionListener(new DialogInputArgument_jBCancel_actionAdapter(
				this));

		jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
		this.addWindowListener(new DialogInputArgument_this_windowAdapter(this));
		jScrollPane1.getViewport().add(paraTable, null);
		panel1.add(jBOK);
		panel1.add(jBCancel);
		paraTable.setRowHeight(20);
		paraTable.setColumnEnable(TITLE_DISP, false);
		paraTable.setColumnWidth(TITLE_DISP, 75);
		paraTable.setColumnVisible(TITLE_NAME, false);
		getContentPane().add(panel1, BorderLayout.EAST);
		getContentPane().add(jScrollPane1, BorderLayout.CENTER);
	}

	/**
	 * ���ò����б�
	 * 
	 * @param paras
	 */
	public void setParam(ParamList paras) {
		try {
			for (int i = 0; i < paras.count(); i++) {
				Param p = paras.get(i);
				if (p.getKind() != Param.VAR)
					continue;
				int row = paraTable.addRow();
				paraTable.data.setValueAt(StringUtils.isValidString(p
						.getRemark()) ? p.getRemark() : p.getName(), row,
						COL_DISP);
				paraTable.data.setValueAt(p.getEditValue(), row, COL_VALUE);
				paraTable.data.setValueAt(p.getName(), row, COL_NAME);
			}
		} catch (Exception e) {
			GM.showException(this, e);
		}
	}

	/**
	 * ȡ������ֵӳ��
	 * 
	 * @return
	 */
	public HashMap<String, Object> getParamValue() {
		return paramMap;
	}

	/**
	 * ������ֵӳ��
	 */
	private HashMap<String, Object> paramMap = null;

	/**
	 * ȡ������ֵӳ��
	 * 
	 * @return
	 */
	private boolean checkParam() {
		paramMap = new HashMap<String, Object>();
		String name; // , value
		paraTable.acceptText();
		Object o;
		// ȡ������ֵ
		for (int i = 0; i < paraTable.getRowCount(); ++i) {
			o = paraTable.data.getValueAt(i, COL_NAME);
			if (o == null)
				continue;
			name = o.toString();
			o = paraTable.data.getValueAt(i, COL_VALUE);
			if (StringUtils.isValidString(o)) {
				try {
					// ��ȷ��ʱ�������������Բ�׽�쳣
					o = PgmNormalCell.parseConstValue((String) o);
				} catch (Exception ex) {
					paraTable.selectRow(i);
					GM.showException(
							this,
							ex,
							true,
							GM.getLogoImage(this, true),
							IdeSplMessage.get().getMessage(
									"dialoginputargument.parseerrorpre", name));
					return false;
				}
			} else {
				o = null;
			}
			paramMap.put(name, o);
		}
		return true;
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		if (!checkParam()) {
			paramMap = null;
			return;
		}

		GM.setWindowDimension(this);
		m_option = JOptionPane.OK_OPTION;
		dispose();
	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		m_option = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	/**
	 * ���ڹر��¼�
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}

}

class DialogInputArgument_this_windowAdapter extends
		java.awt.event.WindowAdapter {
	DialogInputArgument adaptee;

	DialogInputArgument_this_windowAdapter(DialogInputArgument adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogInputArgument_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputArgument adaptee;

	DialogInputArgument_jBOK_actionAdapter(DialogInputArgument adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogInputArgument_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputArgument adaptee;

	DialogInputArgument_jBCancel_actionAdapter(DialogInputArgument adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}
