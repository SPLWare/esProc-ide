package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * ����Դ���ͶԻ���
 *
 */
public class DialogDataSourceType extends JDialog {
	private static final long serialVersionUID = 1L;

	/** JDBC */
	public static final byte TYPE_RELATIONAL = 0;
	/** ODBC */
	public static final byte TYPE_ODBC = 1;
	/** ESSBASE������ʱȡ���� */
	public static final byte TYPE_ESSBASE = 2;
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CLOSED_OPTION;
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * JDBC��ť
	 */
	private JRadioButton jRBRelational = new JRadioButton();
	/**
	 * ODBC��ť
	 */
	private JRadioButton jRBOdbc = new JRadioButton();
	/**
	 * ���ݿ�����
	 */
	private TitledBorder titledBorder1;

	/**
	 * ���캯��
	 */
	public DialogDataSourceType(JDialog parent) {
		super(parent, "���ݿ�����", true);
		try {
			initUI();
			resetLangText();
			this.setSize(300, 150);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
		} catch (Exception x) {
			GM.showException(this, x);
		}
	}

	/**
	 * ȡ���ڹر�ѡ��
	 * 
	 * @return
	 */
	public int getOption() {
		return m_option;
	}

	/**
	 * ȡ����Դ����
	 * 
	 * @return
	 */
	public byte getDataSourceType() {
		if (jRBRelational.isSelected()) {
			return TYPE_RELATIONAL;
		} else if (jRBOdbc.isSelected()) {
			return TYPE_ODBC;
		} else {
			return TYPE_ESSBASE;
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetLangText() {
		this.setTitle(mm.getMessage("dialogdatasourcetype.title"));
		jBOK.setText(mm.getMessage("button.ok"));
		jBCancel.setText(mm.getMessage("button.cancel"));
		jRBRelational.setText(mm.getMessage("dialogdatasourcetype.relational"));
		jRBOdbc.setText(mm.getMessage("dialogdatasourcetype.jrbodbc"));
		titledBorder1.setTitle(mm
				.getMessage("dialogdatasourcetype.databasetype")); // ���ݿ�����
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(142, 142, 142)), "���ݿ�����");
		this.getContentPane().setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		JPanel jPanel1 = new JPanel();
		panel1.setLayout(new VFlowLayout());
		panel1.setForeground(Color.black);
		jBOK.setDefaultCapable(true);
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogDataSourceType_jBOK_actionAdapter(this));

		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.setDefaultCapable(false);
		jBCancel.addActionListener(new DialogDataSourceType_jBCancel_actionAdapter(
				this));

		this.addWindowListener(new DialogDataSourceType_this_windowAdapter(this));
		ButtonGroup buttonGroup1 = new ButtonGroup();
		jPanel1.setLayout(new VFlowLayout());
		jPanel1.setBorder(titledBorder1);
		jRBRelational.setSelected(true);
		jRBRelational.setText("ֱ����ϵ���ݿ�");
		jRBRelational
				.addMouseListener(new DialogDataSourceType_jRBRelational_mouseAdapter(
						this));
		this.setResizable(false);
		jRBOdbc.setText("ODBC����Դ");
		jRBOdbc.addMouseListener(new DialogDataSourceType_jRBOdbc_mouseAdapter(
				this));
		panel1.add(jBOK);
		panel1.add(jBCancel);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jRBRelational, null);
		jPanel1.add(jRBOdbc, null);
		getContentPane().add(panel1, BorderLayout.EAST);
		buttonGroup1.add(jRBRelational);
		buttonGroup1.add(jRBOdbc);
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
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

	/**
	 * ˫��JDBC��ť
	 * 
	 * @param e
	 */
	void jRBRelational_mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			m_option = JOptionPane.OK_OPTION;
			dispose();
		}
	}

	/**
	 * ˫��ODBC��ť
	 * 
	 * @param e
	 */
	void jRBOdbc_mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			m_option = JOptionPane.OK_OPTION;
			dispose();
		}
	}

}

class DialogDataSourceType_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogDataSourceType adaptee;

	DialogDataSourceType_jBOK_actionAdapter(DialogDataSourceType adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogDataSourceType_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogDataSourceType adaptee;

	DialogDataSourceType_jBCancel_actionAdapter(DialogDataSourceType adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogDataSourceType_this_windowAdapter extends
		java.awt.event.WindowAdapter {
	DialogDataSourceType adaptee;

	DialogDataSourceType_this_windowAdapter(DialogDataSourceType adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogDataSourceType_jRBRelational_mouseAdapter extends
		java.awt.event.MouseAdapter {
	DialogDataSourceType adaptee;

	DialogDataSourceType_jRBRelational_mouseAdapter(DialogDataSourceType adaptee) {
		this.adaptee = adaptee;
	}

	public void mousePressed(MouseEvent e) {
		adaptee.jRBRelational_mousePressed(e);
	}
}

class DialogDataSourceType_jRBOdbc_mouseAdapter extends
		java.awt.event.MouseAdapter {
	DialogDataSourceType adaptee;

	DialogDataSourceType_jRBOdbc_mouseAdapter(DialogDataSourceType adaptee) {
		this.adaptee = adaptee;
	}

	public void mousePressed(MouseEvent e) {
		adaptee.jRBOdbc_mousePressed(e);
	}
}