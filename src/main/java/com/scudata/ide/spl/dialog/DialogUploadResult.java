package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.FTPInfo;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.dialog.DialogInputText;
import com.scudata.ide.common.swing.JTableEx;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * �ϴ���FTP�Ľ���Ի���
 *
 */
public class DialogUploadResult extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * �رհ�ť
	 */
	private JButton jBClose = new JButton();
	/**
	 * ��������Դ������
	 */
	private MessageManager mm = IdeSplMessage.get();

	/** ����� */
	private final int COL_INDEX = 0;
	/** �������� */
	private final int COL_HOST = 1;
	/** �û��� */
	private final int COL_USER = 2;
	/** ����� */
	private final int COL_RESULT = 3;
	/** �쳣��Ϣ�� */
	private final int COL_EXCEPTION = 4;

	/** ��� */
	private final String TITLE_INDEX = mm
			.getMessage("dialoguploadresult.index");
	/** ������ */
	private final String TITLE_HOST = mm
			.getMessage("dialoguploadresult.hostname");
	/** �û� */
	private final String TITLE_USER = mm.getMessage("dialoguploadresult.user");
	/** ��� */
	private final String TITLE_RESULT = mm
			.getMessage("dialoguploadresult.result");
	/** �쳣��Ϣ */
	private final String TITLE_EXCEPTION = mm
			.getMessage("dialoguploadresult.exception");

	/**
	 * �����ؼ�
	 */
	private JTableEx tableResult = new JTableEx(TITLE_INDEX + "," + TITLE_HOST
			+ "," + TITLE_USER + "," + TITLE_RESULT + "," + TITLE_EXCEPTION) {
		private static final long serialVersionUID = 1L;

		public void doubleClicked(int xpos, int ypos, int row, int col,
				MouseEvent e) {
			switch (col) {
			case COL_HOST:
			case COL_USER:
			case COL_EXCEPTION:
				Object tmp = data.getValueAt(row, col);
				if (tmp == null)
					return;
				DialogInputText dit = new DialogInputText(
						DialogUploadResult.this, false);
				dit.setText((String) tmp);
				dit.setVisible(true);
				break;
			}
		}
	};

	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CLOSED_OPTION;
	/** �ɹ� */
	private final String S_SUCCESS = mm
			.getMessage("dialoguploadresult.success");
	/** ʧ�� */
	private final String S_FAILD = mm.getMessage("dialoguploadresult.faild");

	/**
	 * ���캯��
	 */
	public DialogUploadResult(JDialog parent) {
		super(parent, "���浽FTP���", true);
		try {
			init();
			setSize(600, 300);
			resetText();
			GM.setDialogDefaultButton(this, new JButton(), jBClose);
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ���ý��
	 * 
	 * @param ftpInfos
	 *            FTP������Ϣ
	 * @param successed
	 *            �Ƿ��ϴ��ɹ�
	 * @param exceptions
	 *            �쳣��Ϣ
	 */
	public void setResult(FTPInfo[] ftpInfos, boolean[] successed,
			String[] exceptions) {
		for (int i = 0; i < ftpInfos.length; i++) {
			int row = tableResult.addRow();
			tableResult.data.setValueAt(ftpInfos[i].getHost(), row, COL_HOST);
			tableResult.data.setValueAt(ftpInfos[i].getUser(), row, COL_USER);
			tableResult.data.setValueAt(successed[i] ? S_SUCCESS : S_FAILD,
					row, COL_RESULT);
			tableResult.data.setValueAt(exceptions[i], row, COL_EXCEPTION);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetText() {
		jBClose.setText(mm.getMessage("button.close"));
		setTitle(mm.getMessage("dialoguploadresult.title"));// ���浽FTP���
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
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		JPanel panelEast = new JPanel(new VFlowLayout());
		jBClose.setMnemonic('C');
		jBClose.setText("�ر�(C)");
		jBClose.addActionListener(this);
		this.getContentPane().add(panelEast, BorderLayout.EAST);
		panelEast.add(jBClose, null);
		JPanel panelCenter = new JPanel(new GridBagLayout());
		panelCenter.add(new JScrollPane(tableResult),
				GM.getGBC(1, 1, true, true));
		this.getContentPane().add(panelCenter, BorderLayout.CENTER);
		this.addWindowListener(new DialogUploadResult_this_windowAdapter(this));
		tableResult.setIndexCol(COL_INDEX);
		tableResult.setRowHeight(22);
		tableResult.setColumnEditable(COL_HOST, false);
		tableResult.setColumnEditable(COL_USER, false);
		tableResult.setColumnEditable(COL_RESULT, false);
		tableResult.setColumnEditable(COL_EXCEPTION, false);

		tableResult.setColumnFixedWidth(COL_RESULT, 60);

		tableResult.getColumn(COL_RESULT).setCellRenderer(
				new DefaultTableCellRenderer() {
					private static final long serialVersionUID = 1L;

					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						Component comp = super
								.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);
						if (S_FAILD.equals(value))
							comp.setForeground(Color.RED);
						return comp;
					}
				});
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
	 * �ؼ��¼�
	 */
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (jBClose.equals(src)) {
			GM.setWindowDimension(this);
			dispose();
		}
	}
}

class DialogUploadResult_this_windowAdapter extends
		java.awt.event.WindowAdapter {
	DialogUploadResult adaptee;

	DialogUploadResult_this_windowAdapter(DialogUploadResult adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}