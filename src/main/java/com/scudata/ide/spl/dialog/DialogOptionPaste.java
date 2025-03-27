package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.control.SplEditor;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ѡ��ճ���Ի���
 *
 */
public class DialogOptionPaste extends JDialog {
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
	 * ճ��ǰĿ��������
	 */
	private TitledBorder titledBorder1;
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
	/**
	 * �����ʵ�����
	 */
	private JRadioButton jRBCol = new JRadioButton();
	/**
	 * �����ʵ�����
	 */
	private JRadioButton jRBRow = new JRadioButton();
	/**
	 * Ŀ�������������
	 */
	private JRadioButton jRBBottom = new JRadioButton();
	/**
	 * Ŀ�������������
	 */
	private JRadioButton jRBRight = new JRadioButton();
	/**
	 * �������ʽ
	 */
	private JCheckBox jCBAdjust = new JCheckBox();
	/**
	 * ������������Դ������
	 */
	private MessageManager splMM = IdeSplMessage.get();

	/**
	 * ���캯��
	 */
	public DialogOptionPaste() {
		super(GV.appFrame, "ѡ��ʽճ��", true);
		try {
			initUI();
			setSize(400, 250);
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
			resetLangText();
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetLangText() {
		setTitle(splMM.getMessage("dialogoptionpaste.title")); // ѡ��ʽճ��
		jBOK.setText(splMM.getMessage("button.ok")); // ȷ��(O)
		jBCancel.setText(splMM.getMessage("button.cancel")); // ȡ��(C)
		titledBorder1.setTitle(splMM.getMessage("dialogoptionpaste.titleborder1")); // ճ��ǰĿ��������
		jRBCol.setText(splMM.getMessage("dialogoptionpaste.insertcol")); // �����ʵ�����(L)
		jRBRow.setText(splMM.getMessage("dialogoptionpaste.insertrow")); // �����ʵ�����(H)
		jRBBottom.setText(splMM.getMessage("dialogoptionpaste.bottom")); // Ŀ�������������(D)
		jRBRight.setText(splMM.getMessage("dialogoptionpaste.right")); // Ŀ�������������(R)
		jCBAdjust.setText(splMM.getMessage("dialogoptionpaste.adjust"));
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
	 * �Ƿ�������ʽ
	 * 
	 * @return
	 */
	public boolean isAdjustExp() {
		return jCBAdjust.isSelected();
	}

	/**
	 * ȡճ��ѡ��
	 * 
	 * @return
	 */
	public byte getPasteOption() {
		if (jRBRow.isSelected()) {
			return SplEditor.PASTE_OPTION_INSERT_ROW;
		} else if (jRBCol.isSelected()) {
			return SplEditor.PASTE_OPTION_INSERT_COL;
		} else if (jRBBottom.isSelected()) {
			return SplEditor.PASTE_OPTION_PUSH_BOTTOM;
		}
		return SplEditor.PASTE_OPTION_PUSH_RIGHT;
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
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)),
				"ճ��ǰĿ��������");
		jPanel2.setLayout(vFlowLayout1);
		jPanel1.setLayout(gridBagLayout1);
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogOptionPaste_jBOK_actionAdapter(this));
		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogOptionPaste_jBCancel_actionAdapter(this));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogOptionPaste_this_windowAdapter(this));
		VFlowLayout VFlowLayout2 = new VFlowLayout();
		ButtonGroup buttonGroup1 = new ButtonGroup();
		JPanel jPOption = new JPanel();
		jPOption.setBorder(titledBorder1);
		jPOption.setLayout(VFlowLayout2);
		jRBCol.setMnemonic('L');
		jRBCol.setText("�����ʵ�����(L)");
		jRBRow.setMinimumSize(new Dimension(120, 26));
		jRBRow.setMnemonic('H');
		jRBRow.setText("�����ʵ�����(H)");
		jRBRow.setSelected(true);
		jRBBottom.setMnemonic('D');
		jRBBottom.setText("Ŀ�������������(D)");
		jRBRight.setToolTipText("");
		jRBRight.setMnemonic('R');
		jRBRight.setText("Ŀ�������������(R)");
		JPanel panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(jPanel1, BorderLayout.CENTER);
		JPanel panelAdjust = new JPanel(new GridBagLayout());
		panelAdjust.add(jCBAdjust, GM.getGBC(1, 1));
		panelCenter.add(panelAdjust, BorderLayout.SOUTH);
		this.getContentPane().add(panelCenter, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK, null);
		jPanel2.add(jBCancel, null);

		jPanel1.add(jPOption, GM.getGBC(1, 1, true, true));
		jPOption.add(jRBRow, null);
		jPOption.add(jRBCol, null);
		jPOption.add(jRBBottom, null);
		jPOption.add(jRBRight, null);
		buttonGroup1.add(jRBRow);
		buttonGroup1.add(jRBCol);
		buttonGroup1.add(jRBBottom);
		buttonGroup1.add(jRBRight);
	}

	/**
	 * �رմ���
	 */
	private void close() {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ���ڹر��¼�
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

class DialogOptionPaste_jBOK_actionAdapter implements java.awt.event.ActionListener {
	DialogOptionPaste adaptee;

	DialogOptionPaste_jBOK_actionAdapter(DialogOptionPaste adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogOptionPaste_jBCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogOptionPaste adaptee;

	DialogOptionPaste_jBCancel_actionAdapter(DialogOptionPaste adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogOptionPaste_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogOptionPaste adaptee;

	DialogOptionPaste_this_windowAdapter(DialogOptionPaste adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
