package com.scudata.ide.spl.etl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.GM;

/**
 * �����������
 * 
 * @author Joancy
 *
 */
public class ParamInputPanel extends JPanel {
	private static final long serialVersionUID = 7836761146175300849L;

	private TableParamEdit table;
	JLabel lbType = new JLabel();
	JLabel lbDesc = new JLabel("���Ա༭");
	ParamInfoList infoList;
	MessageManager mm = FuncMessage.get();
	JDialog owner;

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            ������
	 */
	public ParamInputPanel(JDialog owner) {
		this.owner = owner;
		setLayout(new BorderLayout());
		lbType.setForeground(Color.red);
		JPanel tmp = new JPanel(new GridBagLayout());
		tmp.add(lbType, GM.getGBC(1, 1));
		tmp.add(lbDesc, GM.getGBC(1, 2, true));
		add(tmp, BorderLayout.NORTH);
		table = new TableParamEdit(owner);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	/**
	 * ���ò�����Ϣ
	 * 
	 * @param funcType
	 *            ��������
	 * @param funcTitle
	 *            ��������
	 * @param funcDesc
	 *            ������������Ϣ
	 * @param list
	 *            �����Ĳ�����Ϣ�б�
	 */
	public void setParamInfoList(String funcType, String funcTitle,
			String funcDesc, ParamInfoList list) {
		this.infoList = list;
		if (funcType.isEmpty()) {
			lbType.setText(funcTitle);
		} else {
			lbType.setText(funcType + "." + funcTitle);
		}
		lbDesc.setText(" " + funcDesc);
		table.setParamEdit(list);
	}

	/**
	 * ��ȡ�����༭��
	 * 
	 * @return �����༭��
	 */
	public TableParamEdit getParamTable() {
		return table;
	}

	/**
	 * ��ȡ������Ϣ�б�
	 * 
	 * @return ������Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		table.acceptText();
		return infoList;
	}

}
