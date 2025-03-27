package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.scudata.common.DBConfig;
import com.scudata.common.MessageManager;
import com.scudata.common.ODBCUtil;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.DataSource;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JComboBoxEx;
import com.scudata.ide.common.swing.VFlowLayout;

/**
 * ODBC����Դ�Ի���
 *
 */
public class DialogODBCDataSource extends JDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
	/**
	 * ������Դ����
	 */
	private String oldDSName;
	/**
	 * URL
	 */
	public static final String ODBC_URL = "jdbc:odbc:";
	/**
	 * �ַ���
	 */
	public static final String ODBC_CHARSET = "GBK";
	/**
	 * ����
	 */
	public static final String ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();

	/**
	 * ����Դ����
	 */
	private JLabel jLabel1 = new JLabel();
	/**
	 * ����Դ�����ı���
	 */
	private JTextField jDSName = new JTextField();
	/**
	 * ODBC����
	 */
	private JLabel jLabel3 = new JLabel();
	/**
	 * ODBC�����ı���
	 */
	private JComboBoxEx jODBCName = new JComboBoxEx();
	/**
	 * �û���
	 */
	private JLabel jLabel4 = new JLabel();
	/**
	 * �û����ı���
	 */
	private JTextField jUser = new JTextField();
	/**
	 * ����
	 */
	private JLabel jLabel5 = new JLabel();
	/**
	 * ���������
	 */
	private JPasswordField jPassword = new JPasswordField();
	/**
	 * �����Ƿ��ģʽ��
	 */
	private JCheckBox jUseSchema = new JCheckBox();
	/**
	 * �Ƿ��Сд����
	 */
	private JCheckBox jCaseSentence = new JCheckBox();
	/**
	 * �������Ƿ���޶���
	 */
	private JCheckBox jCBIsAddTilde = new JCheckBox();

	/**
	 * �Ѿ����ڵ�����
	 */
	private Vector<String> existNames;

	/**
	 * ���캯��
	 */
	public DialogODBCDataSource(JDialog parent) {
		super(parent, "ODBC����Դ", true);
		try {
			setSize(400, 300);
			initUI();
			init();
			resetLangText();
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetLangText() {
		setTitle(mm.getMessage("dialogodbcdatasource.title")); // ODBC����Դ
		jBCancel.setText(mm.getMessage("button.cancel"));
		jBOK.setText(mm.getMessage("button.ok"));
		jLabel1.setText(mm.getMessage("dialogodbcdatasource.dsname")); // ����Դ����
		jLabel3.setText(mm.getMessage("dialogodbcdatasource.odbcname")); // ODBC����
		jLabel4.setText(mm.getMessage("dialogodbcdatasource.user")); // �û���
		jLabel5.setText(mm.getMessage("dialogodbcdatasource.password")); // ����
		jUseSchema.setText(mm.getMessage("dialogodbcdatasource.useschema")); // ʹ�ô�ģʽ�ı�����
		jCaseSentence.setText(mm
				.getMessage("dialogodbcdatasource.casesentence")); // ��Сд����
		jCBIsAddTilde.setText(mm.getMessage("dialogdatasourcepara.isaddtilde"));
	}

	/**
	 * ��������Դ����
	 * 
	 * @param config
	 */
	public void set(DBConfig config) {
		if (!DialogDataSource.isLocalDataSource(this, new DataSource(config),
				false)) {
			jBOK.setEnabled(false);
		}
		oldDSName = config.getName();
		jDSName.setText(config.getName());
		String url = config.getUrl();
		if (url.startsWith(ODBC_URL)) {
			url = url.substring(ODBC_URL.length());
		}
		jODBCName.setSelectedItem(url);
		jUser.setText(config.getUser());
		String pwd = config.getPassword();
		try {
			jPassword.setText(pwd); // PwdUtils.decrypt(pwd)
		} catch (Exception x) {
		}
		jUseSchema.setSelected(config.isUseSchema());
		jCaseSentence.setSelected(config.isCaseSentence());
		jCBIsAddTilde.setSelected(config.isAddTilde());
	}

	/**
	 * ȡ����Դ����
	 * 
	 * @return
	 */
	public DataSource get() {
		DBConfig config = new DBConfig();
		config.setName(jDSName.getText());
		// config.setDBCharset(ODBC_CHARSET);
		// config.setClientCharset(ODBC_CHARSET);
		config.setDriver(ODBC_DRIVER);
		config.setUrl(ODBC_URL + (String) jODBCName.getSelectedItem());
		config.setUser(jUser.getText());
		String pwd = new String(jPassword.getPassword());
		config.setPassword(pwd); // PwdUtils.encrypt(pwd)
		config.setUseSchema(jUseSchema.isSelected());
		config.setCaseSentence(jCaseSentence.isSelected());
		config.setAddTilde(jCBIsAddTilde.isSelected());
		DataSource ds = new DataSource(config);
		return ds;
	}

	/**
	 * �����Ѿ����ڵ�����
	 * 
	 * @param existNames
	 *            �Ѿ����ڵ�����
	 */
	public void setExistNames(Vector<String> existNames) {
		this.existNames = existNames;
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
	 * �����Ƿ��޸���
	 * 
	 * @return
	 */
	public boolean isNameChanged() {
		return !jDSName.getText().equalsIgnoreCase(oldDSName);
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogODBCDataSource_this_windowAdapter(this));
		JPanel jPanel1 = new JPanel();
		JPanel jPanel2 = new JPanel();
		VFlowLayout vFlowLayout1 = new VFlowLayout();
		jPanel2.setLayout(vFlowLayout1);
		jBCancel.setMnemonic('C');
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogODBCDataSource_jBCancel_actionAdapter(
				this));
		jBOK.setMnemonic('O');
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogODBCDataSource_jBOK_actionAdapter(this));
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		GridBagLayout gridBagLayout2 = new GridBagLayout();
		JPanel jPanel5 = new JPanel();
		jPanel1.setLayout(gridBagLayout1);
		jLabel1.setText("����Դ����");
		jLabel3.setText("ODBC����");
		jLabel4.setText("�û���");
		jLabel5.setText("����");
		jUseSchema.setText("ʹ�ô�ģʽ�ı�����");
		jCaseSentence.setText("��Сд����");
		jCBIsAddTilde.setText("ʹ�ô����ŵ�SQL");
		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(gridBagLayout2);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOK, null);
		jPanel2.add(jBCancel, null);

		jPanel1.add(jPanel3, GM.getGBC(1, 1, true));
		jPanel1.add(jPanel5, GM.getGBC(3, 1, true, true));

		jPanel3.add(jLabel1, GM.getGBC(1, 1));
		jPanel3.add(jDSName, GM.getGBC(1, 2, true));
		jPanel3.add(jLabel3, GM.getGBC(3, 1));
		jPanel3.add(jODBCName, GM.getGBC(3, 2, true));
		jPanel3.add(jLabel4, GM.getGBC(4, 1));
		jPanel3.add(jUser, GM.getGBC(4, 2, true));
		jPanel3.add(jLabel5, GM.getGBC(5, 1));
		jPanel3.add(jPassword, GM.getGBC(5, 2, true));
		GridBagConstraints gbc = GM.getGBC(6, 1, true);
		gbc.gridwidth = 2;
		jPanel3.add(jUseSchema, gbc);
		gbc = GM.getGBC(7, 1, true);
		gbc.gridwidth = 2;
		jPanel3.add(jCaseSentence, gbc);
		gbc = GM.getGBC(8, 1, true);
		gbc.gridwidth = 2;
		jPanel3.add(jCBIsAddTilde, gbc);
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		int height = 28;
		jDSName.setPreferredSize(new Dimension(0, height));
		jODBCName.setPreferredSize(new Dimension(0, height));
		jUser.setPreferredSize(new Dimension(0, height));
		jPassword.setPreferredSize(new Dimension(0, height));
		jUseSchema.setPreferredSize(new Dimension(0, height));
		jCaseSentence.setPreferredSize(new Dimension(0, height));
		jODBCName.setEditable(true);

		ArrayList dsList = ODBCUtil.getDataSourcesName(ODBCUtil.SYS_DSN
				| ODBCUtil.USER_DSN);
		jODBCName.setListData(dsList.toArray());
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		try {
			if (!StringUtils.isValidString(jDSName.getText())) {
				throw new Exception(
						mm.getMessage("dialogdatasourcepara.emptydsname"));
			}
			if (!StringUtils.isValidString(jODBCName.getSelectedItem())) {
				throw new Exception(
						mm.getMessage("dialogdatasourcepara.emptyodbc"));
			}
			if (existNames != null) {
				if (existNames.contains(jDSName.getText())) {
					GM.messageDialog(this,
							mm.getMessage("dialogdatasource.existdsname")
									+ jDSName.getText(),
							mm.getMessage("public.note"),
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (isNameChanged() && GM.isExistDataSource(this, get())) {
				return;
			}
			GM.setWindowDimension(this);
			m_option = JOptionPane.OK_OPTION;
			dispose();
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
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
	 * ���ڹر��¼�
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}
}

class DialogODBCDataSource_jBOK_actionAdapter implements
		java.awt.event.ActionListener {
	DialogODBCDataSource adaptee;

	DialogODBCDataSource_jBOK_actionAdapter(DialogODBCDataSource adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogODBCDataSource_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogODBCDataSource adaptee;

	DialogODBCDataSource_jBCancel_actionAdapter(DialogODBCDataSource adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogODBCDataSource_this_windowAdapter extends
		java.awt.event.WindowAdapter {
	DialogODBCDataSource adaptee;

	DialogODBCDataSource_this_windowAdapter(DialogODBCDataSource adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
