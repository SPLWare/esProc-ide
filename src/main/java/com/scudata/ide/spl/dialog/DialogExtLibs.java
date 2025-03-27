package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.scudata.app.config.RaqsoftConfig;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.GMSpl;
import com.scudata.ide.spl.base.TableExtLibs;
import com.scudata.ide.spl.resources.IdeSplMessage;
import com.scudata.resources.AppMessage;

/**
 * �ⲿ�����öԻ���
 *
 */
public class DialogExtLibs extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * ��������Դ������
	 */
	private MessageManager mm = IdeSplMessage.get();
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * �ⲿ��·���༭��
	 */
	private JTextField jTFExtLibsPath = new JTextField();
	/**
	 * �ⲿ��·��ѡ��ť
	 */
	private JButton jBExtLibsPath = new JButton();
	/**
	 * �ⲿ���������ؼ�
	 */
	private TableExtLibs tableNames;
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CANCEL_OPTION;
	/**
	 * �Ѿ����ڵ��ⲿ�������б�
	 */
	private Vector<String> existNames = new Vector<String>();
	/**
	 * �ⲿ��Ŀ¼�����б�
	 */
	private Vector<String> dirNames = new Vector<String>();

	/**
	 * �����
	 */
	private Frame parent;

	/**
	 * ���캯��
	 * 
	 * @param config
	 *            ����������
	 * @param frame
	 *            ������
	 * @param extLibsPath
	 *            �ⲿ���·��
	 * @param extLibs
	 *            ���õ��ⲿ���б�
	 */
	public DialogExtLibs(RaqsoftConfig config, Frame frame, String extLibsPath,
			List<String> extLibs) {
		super(frame, IdeSplMessage.get().getMessage("dialogselectnames.title"),
				true);
		this.parent = frame;
		initDialog(config, extLibsPath, extLibs);
	}

	/**
	 * ���캯��
	 * 
	 * @param config
	 *            ����������
	 * @param jdialog
	 *            ������
	 * @param extLibsPath
	 *            �ⲿ���·��
	 * @param extLibs
	 *            ���õ��ⲿ���б�
	 */
	public DialogExtLibs(RaqsoftConfig config, JDialog jdialog,
			String extLibsPath, List<String> extLibs) {
		super(jdialog, IdeSplMessage.get()
				.getMessage("dialogselectnames.title"), true);
		initDialog(config, extLibsPath, extLibs);
	}

	/**
	 * ��ʼ���Ի���
	 * 
	 * @param config
	 * @param extLibsPath
	 * @param extLibs
	 */
	private void initDialog(RaqsoftConfig config, String extLibsPath,
			List<String> extLibs) {
		tableNames = new TableExtLibs(this);
		try {
			init();
			setConfig(config, extLibsPath, extLibs);
			setSize(500, 450);
			resetText();
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
			setResizable(true);
		} catch (Exception e) {
			GM.showException(this, e);
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
	 * ȡ�ⲿ��ĸ�Ŀ¼
	 * 
	 * @return
	 */
	public String getExtLibsPath() {
		return jTFExtLibsPath.getText();
	}

	/**
	 * �ⲿ���б�
	 * 
	 * @return
	 */
	public List<String> getExtLibs() {
		String[] selectedNames = tableNames.getSelectedNames(null);
		if (selectedNames != null && selectedNames.length == 0)
			return null;
		List<String> extLibs = new ArrayList<String>();
		for (String name : selectedNames) {
			extLibs.add(name);
		}
		return extLibs;
	}

	/**
	 * �ϴ�ģʽ
	 */
	private boolean isUploadMode = false;

	public void setUploadMode() {
		isUploadMode = true;
	}

	/**
	 * �����ⲿ������
	 * 
	 * @param config
	 *            ����������
	 * @param extLibsPath
	 *            �ⲿ���Ŀ¼
	 * @param extLibs
	 *            �ⲿ���б�
	 */
	private void setConfig(RaqsoftConfig config, String extLibsPath,
			List<String> extLibs) {
		jTFExtLibsPath.setText(extLibsPath);
		List<String> libs = extLibs;
		if (libs != null)
			existNames.addAll(libs);
		setExtLibsPath(false);
	}

	/**
	 * �����ⲿ���Ŀ¼
	 * 
	 * @param showException
	 */
	private synchronized void setExtLibsPath(boolean showException) {
		dirNames.clear();
		String extLibsPath = jTFExtLibsPath.getText();
		if (!StringUtils.isValidString(extLibsPath)) {
			tableNames.setExistNames(null);
			tableNames.setExistColor(true);
			tableNames.setNames(null, false, true);
			return;
		}
		extLibsPath = GMSpl.getAbsolutePath(extLibsPath);
		File extLibsDir = new File(extLibsPath);
		if (!extLibsDir.exists() || !extLibsDir.isDirectory()) {
			if (showException)
				GM.messageDialog(this,
						AppMessage.get().getMessage("configutil.noextpath"));
			else
				return;
		}
		File[] subDirs = extLibsDir.listFiles();
		if (subDirs != null) {
			for (File sd : subDirs) {
				if (sd.isDirectory()) {
					if (isExtLibrary(sd)) {
						dirNames.add(sd.getName());
					}
				}
				// ��Ŀ¼�µ�jar�Ѿ��ڸ�Ŀ¼���ع������ﲻ������
			}
		}
		int existSize = existNames.size();
		for (int i = existSize - 1; i >= 0; i--) {
			if (!dirNames.contains(existNames.get(i))) {
				existNames.remove(i);
			}
		}

		tableNames.setExistNames(existNames);
		tableNames.setExistColor(true);
		tableNames.setNames(dirNames, false, true);
	}

	/**
	 * �Ƿ��ⲿ��
	 * 
	 * @param dir
	 * @return
	 */
	private boolean isExtLibrary(File dir) {
		File[] fs = dir.listFiles();
		List<File> jars = new ArrayList<File>();
		for (File f : fs) {
			if (f.getName().endsWith(".jar")) {
				jars.add(f);
			}
		}
		Pattern p = Pattern
				.compile("com/scudata/lib/(\\w+)/functions.properties");
		for (File f : jars) {
			JarFile jf = null;
			try {
				jf = new JarFile(f);
				Enumeration<JarEntry> jee = jf.entries();
				while (jee.hasMoreElements()) {
					JarEntry je = jee.nextElement();
					Matcher m = p.matcher(je.getName());
					if (m.matches()) {
						return true;
					}
				}
			} catch (Exception e) {
				continue;
			} finally {
				if (jf != null)
					try {
						jf.close();
					} catch (IOException e) {
					}
			}
		}
		return false;
	}

	/**
	 * ����������Դ
	 */
	private void resetText() {
		jBOK.setText(mm.getMessage("button.ok")); // ȷ��(O)
		jBCancel.setText(mm.getMessage("button.cancel")); // ȡ��(C)
		jBExtLibsPath.setText(IdeCommonMessage.get().getMessage(
				"dialogoptions.select")); // ѡ��
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		this.getContentPane().setLayout(new BorderLayout());
		JPanel panelEast = new JPanel();
		VFlowLayout vf = new VFlowLayout();
		vf.setAlignment(VFlowLayout.TOP);
		vf.setHorizontalFill(true);
		panelEast.setLayout(vf);
		panelEast.add(jBOK);
		panelEast.add(jBCancel);
		jBOK.addActionListener(this);
		jBCancel.addActionListener(this);
		jBOK.setText("ȷ��(O)");
		jBOK.setMnemonic('O');
		jBCancel.setText("ȡ��(C)");
		jBCancel.setMnemonic('C');
		this.getContentPane().add(panelEast, BorderLayout.EAST);
		JPanel panelNorth = new JPanel(new GridBagLayout());
		JLabel jLExtLibsPath = new JLabel(IdeCommonMessage.get().getMessage(
				"dialogoptions.extlibspath"));
		panelNorth.add(jLExtLibsPath, GM.getGBC(0, 0));
		panelNorth.add(jTFExtLibsPath, GM.getGBC(0, 1, true));
		panelNorth.add(jBExtLibsPath, GM.getGBC(0, 2));
		JPanel panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(panelNorth, BorderLayout.NORTH);
		panelCenter.add(tableNames, BorderLayout.CENTER);
		JLabel labelTips = new JLabel(IdeSplMessage.get().getMessage(
				"dialogselectnames.tips"));
		labelTips.setForeground(Color.BLUE);
		panelCenter.add(labelTips, BorderLayout.SOUTH);
		this.getContentPane().add(panelCenter, BorderLayout.CENTER);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		jTFExtLibsPath.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					jBOK.requestFocusInWindow();
					setExtLibsPath(true);
				}
			}
		});
		jBExtLibsPath.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String oldDir = jTFExtLibsPath.getText();
				if (StringUtils.isValidString(oldDir)) {
					File f = new File(oldDir);
					if (f != null && f.exists())
						oldDir = f.getParent();
				}
				if (!StringUtils.isValidString(oldDir))
					oldDir = GV.lastDirectory;
				String newPath = GM.dialogSelectDirectory(DialogExtLibs.this,
						oldDir);
				if (newPath != null) {
					jTFExtLibsPath.setText(newPath);
					setExtLibsPath(true);
				}
			}
		});
	}

	/**
	 * �رմ���
	 */
	private void close() {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ��ť�¼�
	 */
	public void actionPerformed(ActionEvent e) {
		Object c = e.getSource();
		if (c == null) {
			return;
		}
		if (c.equals(jBOK)) {
			if (isUploadMode) {
				String path = getExtLibsPath();
				if (!StringUtils.isValidString(path)) {
					GM.messageDialog(parent,
							mm.getMessage("dialogextlibs.emptypath")); // ��ѡ���ⲿ��Ŀ¼��
					return;
				}
				List<String> libs = getExtLibs();
				if (libs == null || libs.isEmpty()) {
					GM.messageDialog(parent,
							mm.getMessage("dialogextlibs.emptylib")); // ��ѡ���ⲿ�⡣
					return;
				}
			}
			m_option = JOptionPane.OK_OPTION;
			close();
		} else if (c.equals(jBCancel)) {
			close();
		}
	}
}
