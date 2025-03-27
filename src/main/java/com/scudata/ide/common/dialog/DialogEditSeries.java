package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.scudata.common.MessageManager;
import com.scudata.dm.Sequence;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.control.PanelSeries;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * ���б༭�Ի���
 *
 */
public class DialogEditSeries extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * �������
	 */
	private PanelSeries panelSeries = new PanelSeries(this);
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
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
	 * ���캯��
	 */
	public DialogEditSeries(JDialog parent) {
		super(parent, "���б༭", true);
		try {
			initUI();
			setSize(400, 300);
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
		setTitle(mm.getMessage("dialogeditseries.title")); // ���б༭
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
	 * �������ж���
	 * 
	 * @param param
	 */
	public void setSequence(Sequence seq) {
		panelSeries.setSequence(seq);
	}

	/**
	 * ȡ���ж���
	 * 
	 * @return
	 */
	public Sequence getSequence() {
		return panelSeries.getSequence();
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		JPanel jPanel2 = new JPanel(new VFlowLayout());
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogEditSeries_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogEditSeries_jBCancel_actionAdapter(
				this));
		jBAdd.setMnemonic('A');
		jBAdd.setText("����(A)");
		jBAdd.addActionListener(new DialogEditSeries_jBAdd_actionAdapter(this));
		jBDel.setMnemonic('D');
		jBDel.setText("ɾ��(D)");
		jBDel.addActionListener(new DialogEditSeries_jBDel_actionAdapter(this));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogEditSeries_this_windowAdapter(this));
		jBUp.setMnemonic('U');
		jBUp.setText("����(U)");
		jBUp.addActionListener(new DialogEditSeries_jBUp_actionAdapter(this));
		jBDown.setMnemonic('W');
		jBDown.setText("����(W)");
		jBDown.addActionListener(new DialogEditSeries_jBDown_actionAdapter(this));
		jBInsert.setMnemonic('I');
		jBInsert.setText("����(I)");
		jBInsert.addActionListener(new DialogEditSeries_jBInsert_actionAdapter(
				this));
		this.getContentPane().add(panelSeries, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK);
		jPanel2.add(jBCancel);
		jPanel2.add(new JPanel());
		jPanel2.add(jBAdd);
		jPanel2.add(jBInsert);
		jPanel2.add(jBDel);
		jPanel2.add(jBUp);
		jPanel2.add(jBDown);
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		if (!panelSeries.checkData()) {
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
		panelSeries.addRow();
	}

	/**
	 * ɾ����ť�¼�
	 * 
	 * @param e
	 */
	void jBDel_actionPerformed(ActionEvent e) {
		panelSeries.deleteRows();
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
		panelSeries.rowUp();
	}

	/**
	 * ���ư�ť�¼�
	 * 
	 * @param e
	 */
	void jBDown_actionPerformed(ActionEvent e) {
		panelSeries.rowDown();
	}

	/**
	 * ���밴ť�¼�
	 * 
	 * @param e
	 */
	void jBInsert_actionPerformed(ActionEvent e) {
		panelSeries.insertRow();
	}
}

class DialogEditSeries_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBOK_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogEditSeries_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBCancel_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogEditSeries_jBAdd_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBAdd_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBAdd_actionPerformed(e);
	}
}

class DialogEditSeries_jBDel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBDel_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBDel_actionPerformed(e);
	}
}

class DialogEditSeries_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogEditSeries adaptee;

	DialogEditSeries_this_windowAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogEditSeries_jBUp_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBUp_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBUp_actionPerformed(e);
	}
}

class DialogEditSeries_jBDown_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBDown_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBDown_actionPerformed(e);
	}
}

class DialogEditSeries_jBInsert_actionAdapter implements
		java.awt.event.ActionListener {
	DialogEditSeries adaptee;

	DialogEditSeries_jBInsert_actionAdapter(DialogEditSeries adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBInsert_actionPerformed(e);
	}
}
