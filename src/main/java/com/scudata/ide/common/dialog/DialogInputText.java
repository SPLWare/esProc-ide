package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.scudata.common.Logger;
import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * �ı��Ի���
 *
 */
public class DialogInputText extends DialogMaxmizable {
	private static final long serialVersionUID = 1L;
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * ȷ�ϰ�ť
	 */
	public JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	public JButton jBCancel = new JButton();
	/**
	 * ���ư�ť
	 */
	public JButton jBCopy = new JButton();
	/**
	 * ճ����ť
	 */
	public JButton jBPaste = new JButton();

	/**
	 * �ı��༭�ؼ�
	 */
	public JEditorPane jTextPane1 = new JEditorPane();
	/**
	 * �˳�ѡ��
	 */
	protected int m_option = JOptionPane.CLOSED_OPTION;

	/**
	 * ���캯��
	 * 
	 * @param editable
	 *            �Ƿ���Ա༭
	 */
	public DialogInputText(boolean editable) {
		this(GV.appFrame, editable);
	}

	/**
	 * ���캯��
	 * 
	 * @param frame
	 *            �����
	 * @param editable
	 *            �Ƿ���Ա༭
	 */
	public DialogInputText(Frame frame, boolean editable) {
		this(frame, IdeCommonMessage.get().getMessage(
				"dialoginputtext.texteditbox"), editable); // �ı��༭��
	}

	/**
	 * ���캯��
	 * 
	 * @param frame
	 *            �����
	 * @param title
	 *            ����
	 * @param editable
	 *            �Ƿ���Ա༭
	 */
	public DialogInputText(Frame frame, String title, boolean editable) {
		super(frame, title, true);
		initDialog(editable);
	}

	/**
	 * ���캯��
	 * 
	 * @param frame
	 *            �����
	 * @param editable
	 *            �Ƿ���Ա༭
	 */
	public DialogInputText(Dialog parent, boolean editable) {
		this(parent, IdeCommonMessage.get().getMessage(
				"dialoginputtext.texteditbox"), true);
	}

	public DialogInputText(Dialog parent, String title, boolean editable) {
		super(parent, IdeCommonMessage.get().getMessage(
				"dialoginputtext.texteditbox"), true);
		initDialog(editable);
	}

	/**
	 * ��ʼ���Ի���
	 * 
	 * @param editable
	 */
	private void initDialog(boolean editable) {
		try {
			initUI();
			jTextPane1.setEditable(editable);
			setSize(400, 300);
			resetLongText(editable);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
		} catch (Exception ex) {
			Logger.error(ex);
		}
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
	 * �����ı�
	 * 
	 * @param text
	 */
	public void setText(String text) {
		jTextPane1.setText(text);
		jTextPane1.selectAll();
	}

	/**
	 * ȡ�ı�
	 * 
	 * @return
	 */
	public String getText() {
		return jTextPane1.getText();
	}

	/**
	 * ���ø��ı�
	 * 
	 * @param url
	 */
	public void setRichText(String url) {
		try {
			File file = new File(url);
			jTextPane1.setContentType("text/html");
			jTextPane1.setPage("file:" + file.getAbsolutePath());
			jTextPane1.setEditable(false);
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ֻ��ʾ�رպ͸��ư�ť
	 */
	public void setMessageMode() {
		jBOK.setEnabled(false);
		jBOK.setVisible(false);
		jBCancel.setText(mm.getMessage("button.closex"));
		jBCancel.setMnemonic('X');
		jBPaste.setEnabled(false);
		jBPaste.setVisible(false);
	}

	/**
	 * ����������Դ
	 * 
	 * @param editable
	 */
	private void resetLongText(boolean editable) {
		setTitle(editable ? IdeCommonMessage.get().getMessage(
				"dialoginputtext.texteditbox") : mm
				.getMessage("dialoginputtext.title"));
		jBOK.setText(mm.getMessage("button.ok"));
		jBCancel.setText(mm.getMessage("dialoginputtext.buttoncancel"));
		jBCopy.setText(mm.getMessage("button.copy"));
		jBPaste.setText(mm.getMessage("button.paste"));
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		JPanel panel1 = new JPanel();
		BorderLayout borderLayout1 = new BorderLayout();
		JScrollPane jScrollPane1 = new JScrollPane();
		JPanel jPanel1 = new JPanel();
		VFlowLayout vFlowLayout1 = new VFlowLayout();
		panel1.setLayout(borderLayout1);
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogInputText_jBOK_actionAdapter(this));
		jPanel1.setLayout(vFlowLayout1);
		jBCancel.setMnemonic('X');
		jBCancel.setText("ȡ��(X)");
		jBCancel.addActionListener(new DialogInputText_jBCancel_actionAdapter(
				this));
		jBCopy.setMnemonic('C');
		jBCopy.setText("����(C)");
		jBCopy.addActionListener(new DialogInputText_jBCopy_actionAdapter(this));
		jBPaste.setMnemonic('P');
		jBPaste.setText("ճ��(P)");
		jBPaste.addActionListener(new DialogInputText_jBPaste_actionAdapter(
				this));
		getContentPane().add(panel1);
		panel1.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jTextPane1, null);
		panel1.add(jPanel1, BorderLayout.EAST);
		jPanel1.add(jBOK, null);
		jPanel1.add(jBCancel, null);
		jPanel1.add(jBCopy, null);
		jPanel1.add(jBPaste, null);
	}

	/**
	 * ȷ�ϰ�ť
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		m_option = JOptionPane.OK_OPTION;
		this.dispose();
	}

	/**
	 * ȡ����ť
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		m_option = JOptionPane.CANCEL_OPTION;
		this.dispose();
	}

	/**
	 * ���ư�ť
	 * 
	 * @param e
	 */
	void jBCopy_actionPerformed(ActionEvent e) {
		jTextPane1.copy();
	}

	/**
	 * ճ����ť
	 * 
	 * @param e
	 */
	void jBPaste_actionPerformed(ActionEvent e) {
		jTextPane1.setText(GM.clipBoard());
	}
}

class DialogInputText_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputText adaptee;

	DialogInputText_jBOK_actionAdapter(DialogInputText adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogInputText_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputText adaptee;

	DialogInputText_jBCancel_actionAdapter(DialogInputText adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogInputText_jBCopy_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputText adaptee;

	DialogInputText_jBCopy_actionAdapter(DialogInputText adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCopy_actionPerformed(e);
	}
}

class DialogInputText_jBPaste_actionAdapter implements
		java.awt.event.ActionListener {
	DialogInputText adaptee;

	DialogInputText_jBPaste_actionAdapter(DialogInputText adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBPaste_actionPerformed(e);
	}
}
