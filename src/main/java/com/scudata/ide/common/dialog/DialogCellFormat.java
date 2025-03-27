package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.control.PanelCellFormat;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * ��ʽ�Ի���
 */
public class DialogCellFormat extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * ȷ����ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * ��ʽ���
	 */
	private PanelCellFormat panelFormat = new PanelCellFormat(this) {
		private static final long serialVersionUID = 1L;

		public void formatSelected() {
			jBOK_actionPerformed(null);
		}
	};

	/**
	 * ���ڹرյĶ���
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * ���캯��
	 */
	public DialogCellFormat() {
		super(GV.appFrame, "��ʽ�༭", true);
		try {
			initUI();
			resetLangText();
			pack();
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
		} catch (Exception e) {
			GM.showException(this, e);
		}

	}

	/**
	 * ���ø�ʽ
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		panelFormat.setFormat(format);
	}

	/**
	 * ȡ��ʽ
	 * 
	 * @return
	 */
	public String getFormat() {
		return panelFormat.getFormat();
	}

	/**
	 * ȡ��ʽ����
	 * 
	 * @return
	 */
	public byte getFormatType() {
		return panelFormat.getFormatType();
	}

	/**
	 * ��������������Դ
	 */
	private void resetLangText() {
		this.setTitle(mm.getMessage("dialogcellformat.title")); // ��ʽ�༭
		jBOK.setText(mm.getMessage("button.ok")); // ȷ��(O)
		jBCancel.setText(mm.getMessage("button.cancel")); // ȡ��(C)
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogCellFormat_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogCellFormat_jBCancel_actionAdapter(
				this));
		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new VFlowLayout());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogCellFormat_this_windowAdapter(this));
		this.getContentPane().add(panelFormat, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK, null);
		jPanel2.add(jBCancel, null);
	}

	/**
	 * ȡ���ڹرն���
	 * 
	 * @return
	 */
	public int getOption() {
		return this.m_option;
	}

	/**
	 * ȷ����ť����
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		this.m_option = JOptionPane.OK_OPTION;
		dispose();
	}

	/**
	 * ȡ����ť����
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		this.m_option = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	/**
	 * ���ڹر�����
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}

}

class DialogCellFormat_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogCellFormat adaptee;

	DialogCellFormat_jBOK_actionAdapter(DialogCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogCellFormat_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogCellFormat adaptee;

	DialogCellFormat_jBCancel_actionAdapter(DialogCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogCellFormat_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogCellFormat adaptee;

	DialogCellFormat_this_windowAdapter(DialogCellFormat adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
