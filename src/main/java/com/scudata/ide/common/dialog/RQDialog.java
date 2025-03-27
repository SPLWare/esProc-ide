package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;

/**
 * �Ի������
 *
 */
public class RQDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * ȷ�ϰ�ť
	 */
	public JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	public JButton jBCancel = new JButton();

	/**
	 * �˳�ѡ��
	 */
	protected int m_option = JOptionPane.CLOSED_OPTION;
	/**
	 * �м����
	 */
	protected JPanel panelCenter = new JPanel(new BorderLayout());
	/**
	 * �ײ���ť���
	 */
	protected JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	/**
	 * ȱʡ���
	 */
	private static final int DEF_WIDTH = 400;
	/**
	 * ȱʡ�߶�
	 */
	private static final int DEF_HEIGHT = 300;

	/**
	 * ���캯��
	 */
	public RQDialog() {
		this("");
	}

	/**
	 * ���캯��
	 * 
	 * @param title
	 *            ����
	 */
	public RQDialog(String title) {
		this(title, DEF_WIDTH, DEF_HEIGHT);
	}

	/**
	 * ���캯��
	 * 
	 * @param title
	 *            ����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public RQDialog(String title, int width, int height) {
		this(GV.appFrame, title, width, height);
	}

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            �����
	 * @param title
	 *            ����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public RQDialog(Frame owner, String title, int width, int height) {
		super(owner, title, true);
		if (owner != null)
			this.setIconImage(owner.getIconImage());
		init();
		setSize(width, height);
		GM.setDialogDefaultButton(this, jBOK, jBCancel);
	}

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            �����
	 * @param title
	 *            ����
	 */
	public RQDialog(Dialog owner, String title) {
		this(owner, title, DEF_WIDTH, DEF_HEIGHT);
	}

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            �����
	 * @param title
	 *            ����
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public RQDialog(Dialog owner, String title, int width, int height) {
		super(owner, title, true);
		if (GV.appFrame != null)
			this.setIconImage(GV.appFrame.getIconImage());
		init();
		setSize(width, height);
		GM.setDialogDefaultButton(this, jBOK, jBCancel);
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
	 * ��ʼ��
	 */
	private void init() {
		jBOK.setMnemonic('O');
		jBOK.addActionListener(new RQDialog_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.addActionListener(new RQDialog_jBCancel_actionAdapter(this));
		jBOK.setText(IdeCommonMessage.get().getMessage("button.ok"));
		jBCancel.setText(IdeCommonMessage.get().getMessage("button.cancel"));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		getContentPane().add(panelSouth, BorderLayout.SOUTH);
		panelSouth.add(jBOK);
		panelSouth.add(jBCancel);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				// closeDialog(JOptionPane.CLOSED_OPTION);
				try {
					removeWindowListener(RQDialog.this.getWindowListeners()[0]);
				} catch (Throwable t) {
				}
				RQDialog.this.dispose();
				whenWindowClosed();
			}

			public void windowOpened(WindowEvent e) {
				dialogOpened();
			}
		});
		this.setResizable(true);
	}

	protected void whenWindowClosed() {
	}

	/**
	 * �Ի����ʱ
	 */
	protected void dialogOpened() {

	}

	/**
	 * �رնԻ���
	 * 
	 * @param option
	 */
	protected void closeDialog(int option) {
		try {
			this.removeWindowListener(this.getWindowListeners()[0]);
		} catch (Throwable t) {
		}
		m_option = option;
		this.dispose();
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 * @return
	 */
	protected boolean okAction(ActionEvent e) {
		return true;
	}

	/**
	 * ȡ����ť�¼�
	 */
	protected void cancelAction() {
		closeDialog(JOptionPane.CANCEL_OPTION);
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		if (!okAction(e)) {
			return;
		}
		closeDialog(JOptionPane.OK_OPTION);
	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		cancelAction();
	}
}

class RQDialog_jBOK_actionAdapter implements ActionListener {
	RQDialog adaptee;

	RQDialog_jBOK_actionAdapter(RQDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class RQDialog_jBCancel_actionAdapter implements ActionListener {
	RQDialog adaptee;

	RQDialog_jBCancel_actionAdapter(RQDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}