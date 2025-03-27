package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * �и��п�Ի���
 *
 */
public class DialogRowHeight extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * �и߻��п�
	 */
	private JSpinner jSPSize = new JSpinner(new SpinnerNumberModel(0f, 0f,
			999f, 5f));
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
	/**
	 * �Ƿ��иߣ����иߣ����п�
	 */
	private boolean isRow;
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * ���캯��
	 * 
	 * @param isRow
	 *            �Ƿ��иߣ����иߣ����п�
	 * @param size
	 *            �и߻��п�
	 */
	public DialogRowHeight(boolean isRow, float size) {
		super(GV.appFrame, "", true);
		try {
			this.isRow = isRow;
			initUI();
			init(size);
			setSize(300, 100);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
		} catch (Exception ex) {
			GM.showException(this, ex);
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
	 * ȡ�и�
	 * 
	 * @return
	 */
	public float getRowHeight() {
		return ((Number) jSPSize.getValue()).floatValue();
	}

	/**
	 * ��ʼ��
	 * 
	 * @param size
	 */
	private void init(float size) {
		if (isRow) {
			setTitle(mm.getMessage("dialogrowheight.rowheight")); // �и�
		} else {
			setTitle(mm.getMessage("dialogrowheight.colwidth")); // �п�
		}
		jSPSize.setValue(new Double(size));
		jSPSize.setPreferredSize(new Dimension(0, 25));
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
		VFlowLayout vFlowLayout2 = new VFlowLayout();
		jPanel2.setLayout(vFlowLayout1);
		jBOK.setMnemonic('O');
		jBOK.setText(mm.getMessage("button.ok")); // ȷ��(O)
		jBOK.addActionListener(new DialogRowHeight_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.setText(mm.getMessage("button.cancel")); // ȡ��(C)
		jBCancel.addActionListener(new DialogRowHeight_jBCancel_actionAdapter(
				this));
		jPanel1.setLayout(vFlowLayout2);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogRowHeight_this_windowAdapter(this));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK, null);
		jPanel2.add(jBCancel, null);
		jPanel1.add(jSPSize, null);
	}

	/**
	 * �رմ���
	 */
	private void close() {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * �رմ����¼�
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		close();
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		if (jSPSize.getValue() == null
				|| !(jSPSize.getValue() instanceof Number)) {
			String exp = isRow ? mm.getMessage("dialogrowheight.rowheight")
					: mm.getMessage("dialogrowheight.colwidth");
			GM.messageDialog(this,
					mm.getMessage("dialogrowheight.validval", exp)); // ���Ϸ���{0}
			return;
		}
		m_option = JOptionPane.OK_OPTION;
		close();
	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		close();
	}

}

class DialogRowHeight_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogRowHeight adaptee;

	DialogRowHeight_this_windowAdapter(DialogRowHeight adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogRowHeight_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogRowHeight adaptee;

	DialogRowHeight_jBOK_actionAdapter(DialogRowHeight adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogRowHeight_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogRowHeight adaptee;

	DialogRowHeight_jBCancel_actionAdapter(DialogRowHeight adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}
