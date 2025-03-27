package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.scudata.common.MessageManager;
import com.scudata.dm.Param;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.control.PanelEditTable;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * ���༭�Ի���
 *
 */
public class DialogEditTable extends JDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * ���༭���
	 */
	private PanelEditTable panelTable;
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * ���Ӱ�ť
	 */
	private JButton jBAdd = new JButton();
	/**
	 * ɾ����ť
	 */
	private JButton jBDel = new JButton();
	/**
	 * ���밴ť
	 */
	private JButton jBInsert = new JButton();
	/**
	 * ���ư�ť
	 */
	private JButton jBUp = new JButton();
	/**
	 * ���ư�ť
	 */
	private JButton jBDown = new JButton();

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;

	/**
	 * ���캯��
	 * 
	 * @param param
	 *            ��������
	 */
	public DialogEditTable(JDialog parent, Param param) {
		super(parent, "���༭", true);
		try {
			panelTable = new PanelEditTable(this, param);
			initUI();
			setSize(550, 400);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
			resetText();
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetText() {
		setTitle(mm.getMessage("dialogedittable.title")); // ���б༭
		jBOK.setText(mm.getMessage("button.ok"));
		jBCancel.setText(mm.getMessage("button.cancel"));
		jBAdd.setText(mm.getMessage("button.add"));
		jBInsert.setText(mm.getMessage("button.insert"));
		jBDel.setText(mm.getMessage("button.delete"));
		jBUp.setText(mm.getMessage("button.shiftup"));
		jBDown.setText(mm.getMessage("button.shiftdown"));
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
	 * ȡ����
	 * 
	 * @return
	 */
	public Param getParam() {
		return panelTable.getParam();
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		JPanel jPanel1 = new JPanel();
		JPanel jPanel2 = new JPanel();
		VFlowLayout vFlowLayout1 = new VFlowLayout();
		jPanel2.setLayout(vFlowLayout1);
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogEditTable_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogEditTable_jBCancel_actionAdapter(
				this));
		jBAdd.setMnemonic('A');
		jBAdd.setText("����(A)");
		jBAdd.addActionListener(new DialogEditTable_jBAdd_actionAdapter(this));
		jBDel.setMnemonic('D');
		jBDel.setText("ɾ��(D)");
		jBDel.addActionListener(new DialogEditTable_jBDel_actionAdapter(this));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogEditTable_this_windowAdapter(this));
		jBUp.setMnemonic('U');
		jBUp.setText("����(U)");
		jBUp.addActionListener(new DialogEditTable_jBUp_actionAdapter(this));
		jBDown.setMnemonic('W');
		jBDown.setText("����(W)");
		jBDown.addActionListener(new DialogEditTable_jBDown_actionAdapter(this));
		jBInsert.setMnemonic('I');
		jBInsert.setText("����(I)");
		jBInsert.addActionListener(new DialogEditTable_jBInsert_actionAdapter(
				this));
		this.getContentPane().add(panelTable, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK, null);
		jPanel2.add(jBCancel, null);
		jPanel2.add(jPanel1, null);
		jPanel2.add(jBAdd, null);
		jPanel2.add(jBInsert, null);
		jPanel2.add(jBDel, null);
		jPanel2.add(jBUp, null);
		jPanel2.add(jBDown, null);
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		if (!panelTable.checkData()) {
			return;
		}
		m_option = JOptionPane.OK_OPTION;
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ���Ӱ�ť�¼�
	 * 
	 * @param e
	 */
	void jBAdd_actionPerformed(ActionEvent e) {
		panelTable.addRow();
	}

	/**
	 * ɾ����ť�¼�
	 * 
	 * @param e
	 */
	void jBDel_actionPerformed(ActionEvent e) {
		panelTable.deleteRows();
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
	 * ���ư�ť�¼�
	 * 
	 * @param e
	 */
	void jBUp_actionPerformed(ActionEvent e) {
		panelTable.rowUp();
	}

	/**
	 * ���ư�ť�¼�
	 * 
	 * @param e
	 */
	void jBDown_actionPerformed(ActionEvent e) {
		panelTable.rowDown();
	}

	/**
	 * ���밴ť�¼�
	 * 
	 * @param e
	 */
	void jBInsert_actionPerformed(ActionEvent e) {
		panelTable.insertRow();
	}
}

class DialogEditTable_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBOK_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogEditTable_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBCancel_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogEditTable_jBAdd_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBAdd_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBAdd_actionPerformed(e);
	}
}

class DialogEditTable_jBDel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBDel_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBDel_actionPerformed(e);
	}
}

class DialogEditTable_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogEditTable adaptee;

	DialogEditTable_this_windowAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogEditTable_jBUp_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBUp_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBUp_actionPerformed(e);
	}
}

class DialogEditTable_jBDown_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBDown_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBDown_actionPerformed(e);
	}
}

class DialogEditTable_jBInsert_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditTable adaptee;

	DialogEditTable_jBInsert_actionAdapter(DialogEditTable adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBInsert_actionPerformed(e);
	}
}
