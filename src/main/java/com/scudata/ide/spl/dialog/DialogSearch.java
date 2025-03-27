package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.scudata.app.common.Section;
import com.scudata.cellset.datamodel.NormalCell;
import com.scudata.common.CellLocation;
import com.scudata.common.MessageManager;
import com.scudata.common.SegmentSet;
import com.scudata.common.Sentence;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.IAtomicCmd;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.AtomicCell;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.control.SplControl;
import com.scudata.ide.spl.control.SplEditor;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * �����Ի���
 *
 */
public class DialogSearch extends JDialog {
	private static final long serialVersionUID = 1L;
	// private SplEditor splEditor = null;
	// private EditControl splControl = null;

	private static Section searchKeys = new Section(),
			replaceKeys = new Section();
	private static SegmentSet status = new SegmentSet("");

	/**
	 * �������ݱ�ǩ
	 */
	private JLabel jLabel1 = new JLabel();
	/**
	 * �滻Ϊ��ǩ
	 */
	private JLabel jLabel2 = new JLabel();
	/**
	 * ��������������
	 */
	private JComboBox jCBSearch = new JComboBox();
	/**
	 * �滻Ϊ����������
	 */
	private JComboBox jCBReplace = new JComboBox();
	/**
	 * ������ť
	 */
	private JButton jBSearch = new JButton();
	/**
	 * �滻��ť
	 */
	private JButton jBReplace = new JButton();
	/**
	 * ȫ���滻��ť
	 */
	private JButton jBReplaceAll = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();
	/**
	 * ��Сд���и�ѡ��
	 */
	private JCheckBox jCBSensitive = new JCheckBox();
	/**
	 * �������������ʸ�ѡ��
	 */
	private JCheckBox jCBWordOnly = new JCheckBox();
	/**
	 * ���������е��ʸ�ѡ��
	 */
	private JCheckBox jCBQuote = new JCheckBox();
	/**
	 * ����Բ�����е��ʸ�ѡ��
	 */
	private JCheckBox jCBPars = new JCheckBox();
	/**
	 * �������ӷ�Χ
	 */
	private TitledBorder titledBorder1;

	/**
	 * �������Է�Χ
	 */
	private TitledBorder titledBorder2;
	/**
	 * ������������Դ������
	 */
	private MessageManager splMM = IdeSplMessage.get();

	/**
	 * �洢����ѡ��ļ�
	 */
	private static final String KEY_QUOTE = "quote";
	private static final String KEY_RARS = "pars";
	private static final String KEY_WORDONLY = "wordonly";
	private static final String KEY_CASE = "case";

	/**
	 * �Ƿ��滻��true�滻��false����
	 */
	private boolean isReplace = false;
	/**
	 * ���������ť
	 */
	private boolean isSearchClicked = false;
	/**
	 * �������ַ���
	 */
	private String searchString = "";
	/**
	 * �滻���ַ���
	 */
	private String replaceString = "";
	/**
	 * ������ѡ��
	 */
	private int searchFlag;
	/**
	 * �ַ����е����
	 */
	private int stringIndex = -1;
	/**
	 * ��������
	 */
	private int searchedRow = -1;
	/**
	 * ��������
	 */
	private int searchedCol = -1;
	/**
	 * ����ѡ������������
	 */
	private boolean searchSelectedCells = false;
	/**
	 * ȫ���滻��ʼ��
	 */
	private int replaceAllStartRow = 1;
	/**
	 * ȫ���滻��ʼ��
	 */
	private int replaceAllStartCol = 1;

	/**
	 * ���캯��
	 */
	public DialogSearch() {
		super(GV.appFrame, "����", false);
		try {
			initUI();
			init();
			GM.setDialogDefaultButton(this, jBSearch, jBCancel);
			jCBSearch.requestFocus();
			resetLangText();
			setResizable(true);
			pack();
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetLangText() {
		setTitle(splMM.getMessage("dialogsearch.title")); // ����
		titledBorder1.setTitle(splMM.getMessage("dialogsearch.titleborder1")); // �������ӷ�Χ
		titledBorder2.setTitle(splMM.getMessage("dialogsearch.titleborder2")); // �������Է�Χ
		jLabel1.setText(splMM.getMessage("dialogsearch.searchcontent")); // ��������
		jLabel2.setText(splMM.getMessage("dialogsearch.replaceas")); // �滻Ϊ
		jBSearch.setText(splMM.getMessage("button.search")); // ����(F)
		jBReplace.setText(splMM.getMessage("button.replace")); // �滻(R)
		jBReplaceAll.setText(splMM.getMessage("button.replaceall")); // ȫ���滻(A)
		jBCancel.setText(splMM.getMessage("button.close")); // �ر�(C)
		jCBSensitive.setText(splMM.getMessage("dialogsearch.casesensitive")); // ���ִ�Сд
		jCBWordOnly.setText(splMM.getMessage("dialogsearch.wordonly")); // ��������������
		jCBQuote.setText(splMM.getMessage("dialogsearch.ignorequote")); // ���������е���
		jCBPars.setText(splMM.getMessage("dialogsearch.ignorepars")); // ����Բ�����е���
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		String sTmp;
		sTmp = status.get(KEY_CASE);
		if (StringUtils.isValidString(sTmp)) {
			jCBSensitive.setSelected(new Boolean(sTmp).booleanValue());
		}
		sTmp = status.get(KEY_WORDONLY);
		if (StringUtils.isValidString(sTmp)) {
			jCBWordOnly.setSelected(new Boolean(sTmp).booleanValue());
		}
		sTmp = status.get(KEY_QUOTE);
		if (StringUtils.isValidString(sTmp)) {
			jCBQuote.setSelected(new Boolean(sTmp).booleanValue());
		}
		sTmp = status.get(KEY_RARS);
		if (StringUtils.isValidString(sTmp)) {
			jCBPars.setSelected(new Boolean(sTmp).booleanValue());
		}
	}

	/**
	 * ��������ѡ��
	 */
	private void rememberStatus() {
		status.put(KEY_CASE, new Boolean(jCBSensitive.isSelected()).toString());
		status.put(KEY_WORDONLY,
				new Boolean(jCBWordOnly.isSelected()).toString());
		status.put(KEY_QUOTE, new Boolean(jCBQuote.isSelected()).toString());
		status.put(KEY_RARS, new Boolean(jCBPars.isSelected()).toString());

	}

	/**
	 * ����������
	 */
	private void resetDropItems() {
		String sTmp;
		sTmp = (String) jCBSearch.getSelectedItem();
		jCBSearch.removeAllItems();
		searchKeys.unionSection(sTmp);
		for (int i = searchKeys.size() - 1; i >= 0; i--) {
			jCBSearch.addItem(searchKeys.getSection(i));
		}
		jCBSearch.setSelectedItem(sTmp);

		sTmp = (String) jCBReplace.getSelectedItem();
		jCBReplace.removeAllItems();
		replaceKeys.unionSection(sTmp);
		for (int i = replaceKeys.size() - 1; i >= 0; i--) {
			jCBReplace.addItem(replaceKeys.getSection(i));
		}
		jCBReplace.setSelectedItem(sTmp);
	}

	/**
	 * ��������ؼ�
	 * 
	 * @param editor
	 *            ����༭��
	 */
	// public void setControl(SplEditor editor) {
	// setControl(editor, isReplace);
	// }

	/**
	 * ��������ؼ�
	 * 
	 * @param editor
	 *            ����༭��
	 * @param replace
	 *            �Ƿ��滻��true�滻��false����
	 */
	public void setConfig(boolean replace) {
		// this.splEditor = editor;
		this.isReplace = replace;
		// this.splControl = (EditControl) editor.getComponent();
		resetDropItems();
		if (!replace) {
			setTitle(splMM.getMessage("dialogsearch.title")); // ����
		} else {
			setTitle(splMM.getMessage("dialogsearch.replace")); // �滻
		}
		jCBReplace.setEnabled(replace);
		jBReplace.setEnabled(replace);
		jBReplaceAll.setEnabled(replace);
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)), "�������ӷ�Χ");
		titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(148, 145, 140)), "�������Է�Χ");
		JPanel panel1 = new JPanel();
		BorderLayout borderLayout1 = new BorderLayout();
		JPanel jPanel1 = new JPanel();
		VFlowLayout vFlowLayout1 = new VFlowLayout();
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		panel1.setLayout(gridBagLayout1);
		jLabel1.setText("��������");
		jLabel2.setText("�滻Ϊ");
		jBSearch.setText("����(F)");
		jBSearch.setMnemonic('F');
		jBSearch.addActionListener(new DialogSearch_jBSearch_actionAdapter(this));
		jBReplace.setText("�滻(R)");
		jBReplace.setMnemonic('R');
		jBReplace.addActionListener(new DialogSearch_jBReplace_actionAdapter(
				this));
		jBReplaceAll.setText("ȫ���滻(A)");
		jBReplaceAll.setMnemonic('A');
		jBReplaceAll
				.addActionListener(new DialogSearch_jBReplaceAll_actionAdapter(
						this));
		jBCancel.setText("�ر�(C)");
		jBCancel.setMnemonic('C');
		jBCancel.addActionListener(new DialogSearch_jBCancel_actionAdapter(this));
		jCBSensitive.setMaximumSize(new Dimension(95, 27));
		jCBSensitive.setText("���ִ�Сд");
		jCBWordOnly.setText("��������������");
		JPanel jPanel2 = new JPanel();
		jPanel2.setBorder(titledBorder2);
		jCBSearch.setEditable(true);
		jCBReplace.setEditable(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(borderLayout1);
		this.addWindowListener(new DialogSearch_this_windowAdapter(this));
		jPanel1.setLayout(vFlowLayout1);
		getContentPane().add(panel1, BorderLayout.CENTER);

		panel1.add(jLabel1, GM.getGBC(1, 1));
		panel1.add(jCBSearch, GM.getGBC(1, 2, true));
		panel1.add(jLabel2, GM.getGBC(2, 1));
		panel1.add(jCBReplace, GM.getGBC(2, 2, true));

		JPanel tmp = new JPanel(new GridLayout(2, 2));
		tmp.add(jCBSensitive);
		tmp.add(jCBWordOnly);
		tmp.add(jCBQuote);
		tmp.add(jCBPars);
		GridBagConstraints gbc = GM.getGBC(3, 1);
		gbc.gridwidth = 2;
		panel1.add(tmp, gbc);

		gbc = GM.getGBC(4, 1, true, true);
		gbc.gridwidth = 2;
		panel1.add(new JLabel(), gbc);

		this.getContentPane().add(jPanel1, BorderLayout.EAST);
		jPanel1.add(jBSearch, null);
		jPanel1.add(jBReplace, null);
		jPanel1.add(jBReplaceAll, null);
		jPanel1.add(jBCancel, null);
	}

	/**
	 * ��������ѡ��
	 * 
	 * @param searchString
	 *            �������ַ���
	 * @param replaceString
	 *            �滻���ַ���
	 */
	private void setSearchConfig(String searchString, String replaceString) {
		this.searchString = searchString;
		if (replaceString == null) {
			replaceString = "";
		}
		this.replaceString = replaceString;
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

	/**
	 * ����
	 * 
	 * @return
	 */
	private boolean search() {
		return search(false);
	}

	/**
	 * �滻
	 * 
	 * @return
	 */
	private boolean replace() {
		Vector<IAtomicCmd> cmds = new Vector<IAtomicCmd>();
		stringIndex = -1;
		boolean lb = replace(false, cmds);
		if (lb) {
			if (GVSpl.splEditor != null) {
				GVSpl.splEditor.executeCmd(cmds);
				search();
			}
		}
		return lb;
	}

	/**
	 * ȫ���滻
	 * 
	 * @return
	 */
	private int replaceAll() {
		int count = 0;
		Vector<IAtomicCmd> cmds = new Vector<IAtomicCmd>();
		while (replace(true, cmds)) {
			count++;
		}

		if (count > 0) {
			if (GVSpl.splEditor != null)
				GVSpl.splEditor.executeCmd(cmds);
		}
		replaceAllStartRow = 1;
		replaceAllStartCol = 1;
		return count;
	}

	/**
	 * �滻
	 * 
	 * @param replaceAll
	 *            �Ƿ�ȫ���滻��trueȫ���滻��false�滻
	 * @param cmds
	 *            ԭ�����
	 * @return
	 */
	private boolean replace(boolean replaceAll, Vector<IAtomicCmd> cmds) {
		if (search(replaceAll)) {
			if (GVSpl.splEditor != null) {
				SplControl splControl = GVSpl.splEditor.getComponent();
				NormalCell nc = (NormalCell) splControl.cellSet.getCell(
						searchedRow, searchedCol);
				AtomicCell ac = new AtomicCell(splControl, nc);
				byte propId = AtomicCell.CELL_EXP;
				ac.setProperty(propId);
				String exp = nc.getExpString();
				int flag = searchFlag;
				if (!replaceAll) {
					flag += Sentence.ONLY_FIRST;
				}
				exp = Sentence.replace(exp, stringIndex, searchString,
						replaceString, flag);
				ac.setValue(exp);
				cmds.add(ac);
				if (!replaceAll) {
					stringIndex += replaceString.length() - 1;
				} else {
					stringIndex = nc.getExpString().length() - 1;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * ����
	 * 
	 * @param replaceAll
	 *            �Ƿ�ȫ���滻��trueȫ���滻��false�滻
	 * @return
	 */
	private boolean search(boolean replaceAll) {
		if (GVSpl.splEditor == null)
			return false;
		SplEditor splEditor = GVSpl.splEditor;
		SplControl splControl = splEditor.getComponent();
		int startRow = 1, endRow = splControl.cellSet.getRowCount();
		int startCol = 1, endCol = splControl.cellSet.getColCount();
		searchSelectedCells = false;
		if (!splEditor.selectedRects.isEmpty()
				&& (splEditor.getSelectedRect().getColCount() > 1 || splEditor
						.getSelectedRect().getRowCount() > 1)) {
			startRow = splEditor.getSelectedRect().getBeginRow();
			startCol = splEditor.getSelectedRect().getBeginCol();
			endRow = splEditor.getSelectedRect().getEndRow();
			endCol = splEditor.getSelectedRect().getEndCol();
			searchSelectedCells = true;
		}
		int activeRow = startRow;
		int activeCol = startCol;
		CellLocation cp = splControl.getActiveCell();
		if (cp != null) {
			activeRow = cp.getRow();
			activeCol = cp.getCol();
		}
		boolean found = false;
		if (!replaceAll) {
			found = search(activeRow, activeCol, activeRow, endCol, replaceAll);
			if (found) {
				return true;
			}
			found = search(activeRow + 1, startCol, endRow, endCol, replaceAll);
			if (found) {
				return true;
			}
			found = search(startRow, startCol, activeRow - 1, endCol,
					replaceAll);
			if (found) {
				return true;
			}
			found = search(activeRow, startCol, activeRow, activeCol - 1,
					replaceAll);
			if (found) {
				return true;
			}
		} else {
			found = search(replaceAllStartRow, replaceAllStartCol,
					replaceAllStartRow, endCol, replaceAll);
			if (found) {
				return true;
			}
			found = search(replaceAllStartRow + 1, 1, endRow, endCol,
					replaceAll);
			if (found) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ����
	 * 
	 * @param startRow
	 *            ��ʼ��
	 * @param startCol
	 *            ��ʼ��
	 * @param endRow
	 *            ������
	 * @param endCol
	 *            ������
	 * @param replaceAll
	 *            �Ƿ�ȫ���滻��trueȫ���滻��false�滻
	 * @return
	 */
	private boolean search(int startRow, int startCol, int endRow, int endCol,
			boolean replaceAll) {
		boolean found = false;
		SplControl splControl = GVSpl.splEditor.getComponent();
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				NormalCell nc = (NormalCell) splControl.cellSet.getCell(row,
						col);
				String exp = nc.getExpString();
				if (exp == null) {
					stringIndex = -1;
					continue;
				} else {
					stringIndex = Sentence.indexOf(exp, stringIndex + 1,
							searchString, searchFlag);
					if (stringIndex >= 0) {
						found = true;
					}
				}
				if (found) {
					searchedRow = row;
					searchedCol = col;
					replaceAllStartRow = row;
					replaceAllStartCol = col;
					if (!replaceAll) {
						splControl.setSearchedCell(row, col,
								searchSelectedCells);
						this.requestFocus();
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ������ť�¼�
	 * 
	 * @param e
	 */
	void jBSearch_actionPerformed(ActionEvent e) {
		resetDropItems();
		isSearchClicked = true;
		setSearchConfig((String) jCBSearch.getSelectedItem(), "");
		if (search()) {
		} else {
			GM.messageDialog(
					this,
					splMM.getMessage("dialogsearch.cantfindword",
							jCBSearch.getSelectedItem()));
		}
	}

	/**
	 * �滻��ť�¼�
	 * 
	 * @param e
	 */
	void jBReplace_actionPerformed(ActionEvent e) {
		if (isSearchClicked) {
			// ����������ˣ����滻�ӵ�ǰλ�ÿ�ʼ�滻
			stringIndex = -1;
		}
		isSearchClicked = false;
		resetDropItems();
		String search, replace;
		search = (String) jCBSearch.getSelectedItem();
		replace = (String) jCBReplace.getSelectedItem();
		setSearchConfig(search, replace);

		if (replace()) {
		} else {
			GM.messageDialog(
					this,
					splMM.getMessage("dialogsearch.cantfindword",
							jCBSearch.getSelectedItem()));
		}

	}

	/**
	 * ȫ���滻��ť�¼�
	 * 
	 * @param e
	 */
	void jBReplaceAll_actionPerformed(ActionEvent e) {
		resetDropItems();
		String search, replace;
		search = (String) jCBSearch.getSelectedItem();
		replace = (String) jCBReplace.getSelectedItem();
		setSearchConfig(search, replace);
		int i = replaceAll();
		GM.messageDialog(this,
				splMM.getMessage("dialogsearch.totalreplace", i + ""));

	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		rememberStatus();
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ���ڹر��¼�
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		rememberStatus();
		GM.setWindowDimension(this);
		dispose();
	}

}

class DialogSearch_jBSearch_actionAdapter implements
		java.awt.event.ActionListener {
	DialogSearch adaptee;

	DialogSearch_jBSearch_actionAdapter(DialogSearch adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBSearch_actionPerformed(e);
	}
}

class DialogSearch_jBReplace_actionAdapter implements
		java.awt.event.ActionListener {
	DialogSearch adaptee;

	DialogSearch_jBReplace_actionAdapter(DialogSearch adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBReplace_actionPerformed(e);
	}
}

class DialogSearch_jBReplaceAll_actionAdapter implements
		java.awt.event.ActionListener {
	DialogSearch adaptee;

	DialogSearch_jBReplaceAll_actionAdapter(DialogSearch adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBReplaceAll_actionPerformed(e);
	}
}

class DialogSearch_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogSearch adaptee;

	DialogSearch_jBCancel_actionAdapter(DialogSearch adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogSearch_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogSearch adaptee;

	DialogSearch_this_windowAdapter(DialogSearch adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
