package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.scudata.app.common.AppConsts;
import com.scudata.app.common.AppUtil;
import com.scudata.cellset.datamodel.PgmCellSet;
import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.common.MessageManager;
import com.scudata.common.Sentence;
import com.scudata.common.StringUtils;
import com.scudata.dm.Env;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.JListEx;
import com.scudata.ide.spl.GMSpl;
import com.scudata.util.CellSetUtil;

/**
 * ���ļ��в���/�滻�Ի���
 *
 */
public abstract class DialogFileReplace extends RQDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Common������Դ
	 */
	private MessageManager mm = IdeCommonMessage.get();
	/**
	 * �����ַ���
	 */
	private String searchString = "";
	/**
	 * �滻�ַ���
	 */
	private String replaceString = "";
	/**
	 * ������ѡ��
	 */
	private int searchFlag = 0;

	/**
	 * ���캯��
	 * 
	 * @param owner
	 *            ���ؼ�
	 */
	public DialogFileReplace(Frame owner) {
		super(owner, "���ļ��в���/�滻", 600, 500);
		try {
			this.setModal(false);
			setTitle(mm.getMessage("dialogfilereplace.title"));
			init();
			GM.centerWindow(this);
		} catch (Exception e) {
			GM.showException(this, e);
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param buf
	 * @return
	 */
	public abstract PgmCellSet readEncryptedCellSet(String filePath,
			String fileName) throws Exception;

	/**
	 * ȡ�ļ��б�
	 * 
	 * @return
	 */
	private List<File> getFiles() {
		String sDir = jTFDir.getText();
		if (!StringUtils.isValidString(sDir)) {
			GM.messageDialog(this, mm.getMessage("dialogfilereplace.selectdir")); // ��ѡ���ļ�����Ŀ¼��
			return null;
		}
		File dir = new File(sDir);
		if (!dir.exists()) {
			GM.messageDialog(this,
					mm.getMessage("dialogfilereplace.dirnotexists", sDir)); // Ŀ¼��{0}�����ڡ�
			return null;
		}
		if (!dir.isDirectory()) {
			GM.messageDialog(this,
					mm.getMessage("dialogfilereplace.notdir", sDir)); // {0}����һ��Ŀ¼��
			return null;
		}
		File[] subFiles = dir.listFiles();
		if (subFiles == null || subFiles.length == 0) {
			GM.messageDialog(this,
					mm.getMessage("dialogfilereplace.nofilefound")); // Ŀ¼��û�в��ҵ��ļ���
			return null;
		}
		List<File> files = new ArrayList<File>();
		getSubFiles(dir, files, jCBSub.isSelected());
		if (files.isEmpty()) {
			GM.messageDialog(this,
					mm.getMessage("dialogfilereplace.nofilefound")); // Ŀ¼��û�в��ҵ��ļ���
			return null;
		}
		return files;
	}

	/**
	 * ȡĿ¼���ļ�
	 * 
	 * @param dir
	 *            Ŀ¼
	 * @param files
	 *            �ļ��б�����
	 * @param isSub
	 *            �Ƿ������Ŀ¼
	 */
	private void getSubFiles(File dir, List<File> files, boolean isSub) {
		File[] subFiles = dir.listFiles();
		if (subFiles != null) {
			for (File f : subFiles) {
				if (f.isFile()) {
					for (int i = 0; i < FILE_TYPES.length; i++) {
						if (f.getAbsolutePath().endsWith("." + FILE_TYPES[i])) {
							files.add(f);
							break;
						}
					}
				} else if (f.isDirectory() && isSub) {
					getSubFiles(f, files, isSub);
				}
			}
		}
	}

	/**
	 * ��������ѡ��
	 */
	private void setSearchConfig() {
		searchString = jTFSearch.getText();
		replaceString = jTFReplace.getText();
		searchFlag = 0;
		if (!jCBQuote.isSelected()) {
			searchFlag += Sentence.IGNORE_QUOTE;
		}
		if (!jCBPars.isSelected()) {
			searchFlag += Sentence.IGNORE_PARS;
		}
		if (!jCBSensitive.isSelected()) {
			searchFlag += Sentence.IGNORE_CASE;
		}
		if (jCBWordOnly.isSelected()) {
			searchFlag += Sentence.ONLY_PHRASE;
		}
	}

	protected LinkedHashMap<String, String> fileResultMap = new LinkedHashMap<String, String>();

	protected Map<String, String> passwordMap = new HashMap<String, String>();

	/**
	 * ����
	 * 
	 * @param isReplace
	 *            �Ƿ��滻
	 */
	private void search(boolean isReplace) {
		fileResultMap.clear();
		jListResult.setListData(new Object[0]);
		setSearchConfig();
		if (searchString == null || "".equals(searchString)) {
			GM.messageDialog(this,
					mm.getMessage("dialogfilereplace.searchnull")); // �������ݲ���Ϊ�ա�
			return;
		}
		List<File> files = getFiles();
		if (files == null || files.isEmpty()) {
			return;
		}
		try {
			File dir = new File(jTFDir.getText());
			String sDir = dir.getAbsolutePath();
			for (File f : files) {
				String filePath = f.getAbsolutePath();
				String fileName = getFileName(sDir, filePath);
				if (!f.canRead()) {
					fileResultMap.put(filePath, mm.getMessage(
							"dialogfilereplace.cannotread", fileName)); // �ļ���{0}û�ж�ȡȨ�ޡ�
					continue;
				}
				if (isReplace && !f.canWrite()) {
					fileResultMap.put(filePath, mm.getMessage(
							"dialogfilereplace.cannotwrite", fileName)); // �ļ���{0}û��д��Ȩ�ޡ�
					continue;
				}
				boolean isSplFile = filePath.toLowerCase().endsWith(
						"." + AppConsts.FILE_SPL);
				PgmCellSet cellSet;
				if (isSplFile) {
					cellSet = GMSpl.readSPL(filePath);
				} else if (CellSetUtil.isEncrypted(filePath)) {
					cellSet = readEncryptedCellSet(filePath, fileName);
					if (cellSet == null)
						continue;
				} else {
					cellSet = CellSetUtil.readPgmCellSet(filePath);
				}
				if (cellSet == null)
					continue;
				if (filePath != null) {
					cellSet.setName(filePath);
				}
				int searchCount = 0;
				int rc = cellSet.getRowCount();
				int cc = cellSet.getColCount();
				PgmNormalCell cell;
				for (int r = 1; r <= rc; r++) {
					for (int c = 1; c <= cc; c++) {
						cell = cellSet.getPgmNormalCell(r, c);
						if (cell != null) {
							String exp = cell.getExpString();
							if (exp != null) {
								int stringIndex = Sentence.indexOf(exp, 0,
										searchString, searchFlag);
								if (stringIndex >= 0) {
									if (isReplace) {
										exp = Sentence.replace(exp,
												stringIndex, searchString,
												replaceString, searchFlag);
										cell.setExpString(exp);
									}
									searchCount++;
								}
							}
						}
					}
				}
				if (searchCount == 0) {
					continue;
				}
				if (isReplace) {
					if (isSplFile) {
						AppUtil.writeSPLFile(filePath, cellSet);
					} else {
						CellSetUtil.writePgmCellSet(filePath, cellSet);
					}
					fileResultMap.put(filePath, mm.getMessage(
							"dialogfilereplace.replacecount", fileName,
							searchCount));// �ļ���{0}���滻��{1}����Ԫ��
				} else {
					fileResultMap.put(filePath, mm.getMessage(
							"dialogfilereplace.searchcount", fileName,
							searchCount));// �ļ���{0}�в��ҵ���{1}����Ԫ��
				}
			}
			Iterator<String> it = fileResultMap.keySet().iterator();
			Vector<String> codes = new Vector<String>();
			Vector<String> disps = new Vector<String>();
			while (it.hasNext()) {
				String key = it.next();
				String value = fileResultMap.get(key);
				codes.add(key);
				disps.add(value);
			}
			jListResult.x_setData(codes, disps);
		} catch (Exception e) {
			GM.showException(this, e);
		}
	}

	/**
	 * ȡ�ļ���
	 * 
	 * @param sDir
	 * @param filePath
	 * @return
	 */
	private String getFileName(String sDir, String filePath) {
		String fileName = filePath.substring(sDir.length());
		if (fileName != null) {
			int startIndex = -1;
			for (int i = 0; i < fileName.length(); i++) {
				char c = fileName.charAt(i);
				if (c == '/' || c == '\\') {
					startIndex = i;
				} else {
					break;
				}
			}
			if (startIndex > -1) {
				fileName = fileName.substring(startIndex + 1);
			}
		}
		return fileName;
	}

	protected void closeDialog(int option) {
		super.closeDialog(option);
		GM.setWindowDimension(this);
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		panelCenter.setLayout(new GridBagLayout());
		GridBagConstraints gbc;

		panelCenter.add(jLSearch, GM.getGBC(0, 0));
		panelCenter.add(jTFSearch, GM.getGBC(0, 1, true));
		panelCenter.add(jBSearch, GM.getGBC(0, 2));

		panelCenter.add(jLReplace, GM.getGBC(1, 0));
		panelCenter.add(jTFReplace, GM.getGBC(1, 1, true));
		panelCenter.add(jBReplace, GM.getGBC(1, 2));

		panelCenter.add(jLDir, GM.getGBC(2, 0));
		panelCenter.add(jTFDir, GM.getGBC(2, 1, true));
		panelCenter.add(jBDir, GM.getGBC(2, 2));

		gbc = GM.getGBC(3, 0);
		gbc.gridwidth = 2;
		JPanel panelDirOpt = new JPanel(new BorderLayout());
		panelDirOpt.add(jCBSub, BorderLayout.WEST);
		panelDirOpt.add(new JPanel(), BorderLayout.CENTER);
		panelCenter.add(panelDirOpt, gbc);
		panelCenter.add(jBCancel, GM.getGBC(3, 2));

		JPanel panelOpt = new JPanel(new GridLayout(2, 2));
		panelOpt.setBorder(BorderFactory.createTitledBorder(mm
				.getMessage("dialogfilereplace.option"))); // ѡ��
		panelOpt.add(jCBSensitive);
		panelOpt.add(jCBWordOnly);
		panelOpt.add(jCBQuote);
		panelOpt.add(jCBPars);

		gbc = GM.getGBC(4, 0, true);
		gbc.gridwidth = 3;
		panelCenter.add(panelOpt, gbc);

		gbc = GM.getGBC(5, 0, true, true);
		gbc.gridwidth = 3;
		panelCenter.add(new JScrollPane(jListResult), gbc);

		this.remove(panelSouth);
		jCBSensitive.setText(mm.getMessage("dialogfilereplace.sensitive")); // ���ִ�Сд
		jCBWordOnly.setText(mm.getMessage("dialogfilereplace.wordonly")); // ��������������
		jCBQuote.setText(mm.getMessage("dialogfilereplace.quote")); // ���������е���
		jCBPars.setText(mm.getMessage("dialogfilereplace.pars")); // ����Բ�����е���

		jBDir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String lastDir = jTFDir.getText();
				if (!StringUtils.isValidString(lastDir)) {
					if (StringUtils.isValidString(Env.getMainPath())) {
						lastDir = Env.getMainPath();
					}
				}
				String sDir = GM.dialogSelectDirectory(DialogFileReplace.this,
						lastDir);
				if (sDir != null) {
					jTFDir.setText(sDir);
				}
			}

		});

		jBSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				search(false);
			}

		});

		jBReplace.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				search(true);
			}

		});

		jListResult.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (jListResult.isSelectionEmpty())
						return;
					// fileResultMap
					try {
						String disp = (String) jListResult.getSelectedValue();
						String filePath = (String) jListResult
								.x_getCodeItem(disp);
						openSheet(filePath);
					} catch (Exception ex) {
						GM.showException(DialogFileReplace.this, ex);
					}
				}
			}
		});
	}

	/**
	 * ���ļ�
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	protected void openSheet(String filePath) throws Exception {
		GV.appFrame.openSheetFile(filePath);
	}

	/**
	 * �ļ�����
	 */
	private final String[] FILE_TYPES = AppConsts.SPL_FILE_EXTS.split(",");
	/**
	 * Ŀ¼
	 */
	private JLabel jLDir = new JLabel(mm.getMessage("dialogfilereplace.dir"));
	/**
	 * Ŀ¼�ı���
	 */
	private JTextField jTFDir = new JTextField();
	/**
	 * ѡ��Ŀ¼
	 */
	private JButton jBDir = new JButton(
			mm.getMessage("dialogfilereplace.dirbutton"));

	/**
	 * �Ƿ������Ŀ¼
	 */
	private JCheckBox jCBSub = new JCheckBox(
			mm.getMessage("dialogfilereplace.containssub"));
	/**
	 * ��������
	 */
	private JLabel jLSearch = new JLabel(
			mm.getMessage("dialogfilereplace.searchstr"));
	/**
	 * ���������ı���
	 */
	private JTextField jTFSearch = new JTextField();
	/**
	 * ���Ұ�ť
	 */
	private JButton jBSearch = new JButton(
			mm.getMessage("dialogfilereplace.searchbutton"));
	/**
	 * �滻Ϊ
	 */
	private JLabel jLReplace = new JLabel(
			mm.getMessage("dialogfilereplace.replaceto"));
	/**
	 * �滻�ı���
	 */
	private JTextField jTFReplace = new JTextField();
	/**
	 * �滻��ť
	 */
	private JButton jBReplace = new JButton(
			mm.getMessage("dialogfilereplace.replacebutton"));
	/**
	 * �Ƿ��Сд����
	 */
	private JCheckBox jCBSensitive = new JCheckBox();
	/**
	 * �Ƿ�������������
	 */
	private JCheckBox jCBWordOnly = new JCheckBox();
	/**
	 * �Ƿ����������
	 */
	private JCheckBox jCBQuote = new JCheckBox();
	/**
	 * �Ƿ����������
	 */
	private JCheckBox jCBPars = new JCheckBox();
	/**
	 * ����б�
	 */
	private JListEx jListResult = new JListEx();
}
