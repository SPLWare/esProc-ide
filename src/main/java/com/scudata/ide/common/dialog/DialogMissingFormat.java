package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.scudata.app.config.ConfigUtil;
import com.scudata.common.ArgumentTokenizer;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JTableEx;

/**
 * ȱʧֵ����
 *
 */
public class DialogMissingFormat extends RQDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * ���캯��
	 * 
	 * @param owner �����
	 */
	public DialogMissingFormat(Dialog parent) {
		super(parent, "ȱʧֵ����");
		try {
			init();
			GM.centerWindow(this);
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ����ȱʧֵ����
	 * 
	 * @param missingExps
	 */
	public void setMissingFormat(String missingExps) {
		tableList.removeAllRows();
		if (StringUtils.isValidString(missingExps)) {
			ArgumentTokenizer at = new ArgumentTokenizer(missingExps, ConfigUtil.MISSING_SEP);
			while (at.hasNext()) {
				String exp = at.next();
				if (StringUtils.isValidString(exp)) {
					int r = tableList.addRow();
					tableList.data.setValueAt(exp, r, COL_EXP);
				}
			}
		}
	}

	/**
	 * ȡȱʧֵ����
	 * 
	 * @return
	 */
	public String getMissingFormat() {
		tableList.acceptText();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tableList.getRowCount(); i++) {
			Object tmp = tableList.data.getValueAt(i, COL_EXP);
			if (StringUtils.isValidString(tmp)) {
				if (buf.length() > 0) {
					buf.append(ConfigUtil.MISSING_SEP);
				}
				buf.append((String) tmp);
			}
		}
		return buf.toString();
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		MessageManager mm = IdeCommonMessage.get();
		setTitle(mm.getMessage("dialognullstrings.title")); // ȱʧֵ����
		tableList = new JTableEx(new String[] { mm.getMessage("dialognullstrings.nullstrings") });
		panelCenter.add(new JScrollPane(tableList), BorderLayout.CENTER);
		JLabel labelNote = new JLabel(mm.getMessage("dialognullstrings.casesen"));
		JPanel panelNorth = new JPanel(new GridBagLayout());
		panelNorth.add(labelNote, GM.getGBC(0, 0, true));
		panelNorth.add(buttonAdd, GM.getGBC(0, 1, false, false, 0));
		panelNorth.add(buttonDel, GM.getGBC(0, 2));
		panelCenter.add(panelNorth, BorderLayout.NORTH);
		buttonAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tableList.addRow();
				tableList.requestFocusInWindow();
			}
		});
		buttonDel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tableList.acceptText();
				tableList.deleteSelectedRows();
			}
		});

	}

	protected void closeDialog(int option) {
		super.closeDialog(option);
		GM.setWindowDimension(this);
	}

	/**
	 * ���Ӱ�ť
	 */
	private JButton buttonAdd = GM.getCommonIconButton(GM.B_ADD);
	/**
	 * ɾ����ť
	 */
	private JButton buttonDel = GM.getCommonIconButton(GM.B_DEL);
	/**
	 * ���ʽ��
	 */
	private final int COL_EXP = 0;
	/**
	 * �б�ؼ�
	 */
	private JTableEx tableList;
}
