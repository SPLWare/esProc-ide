package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.scudata.app.common.AppConsts;
import com.scudata.app.config.ConfigConsts;
import com.scudata.app.config.ConfigUtil;
import com.scudata.app.config.RaqsoftConfig;
import com.scudata.cellset.IStyle;
import com.scudata.common.Escape;
import com.scudata.common.Logger;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.dm.Env;
import com.scudata.ide.common.ConfigOptions;
import com.scudata.ide.common.ConfigUtilIde;
import com.scudata.ide.common.DataSourceListModel;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.LookAndFeelManager;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.ColorComboBox;
import com.scudata.ide.common.swing.JComboBoxEx;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.ide.spl.GMSpl;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.base.PanelEnv;
import com.scudata.ide.spl.resources.IdeSplMessage;
import com.scudata.parallel.UnitContext;

/**
 * ������ѡ���
 *
 */
public class DialogOptions extends JDialog {

	private static final long serialVersionUID = 1L;
	/**
	 * ������Դ������
	 */
	public MessageManager mm = IdeCommonMessage.get();
	/**
	 * �˳�ѡ��
	 */
	private int m_option = JOptionPane.CLOSED_OPTION;
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton jBOK = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton jBCancel = new JButton();

	/**
	 * ������ʽ������
	 */
	private JComboBoxEx jCBLNF = new JComboBoxEx();

	/**
	 * �Ƿ��Զ�����
	 */
	private JCheckBox jCBAutoBackup = new JCheckBox();

	/**
	 * �Ƿ��Զ������������Դ��ѡ��
	 */
	private JCheckBox jCBAutoConnect = new JCheckBox();

	private final String AUTO_SAVE_TIP = mm.getMessage(
			"dialogoptions.autosavetip",
			ConfigOptions.iAutoSaveMinutes.intValue());
	/**
	 * �Ƿ��Զ����渴ѡ��
	 */
	private JCheckBox jCBAutoSave = new JCheckBox(
			mm.getMessage("dialogoptions.autosave"));

	private JLabel jLAutoSaveInterval = new JLabel(
			mm.getMessage("dialogoptions.autosaveinterval"));

	protected GridLayout ideOptLayout = new GridLayout(5, 2);
	protected JPanel jPIdeOpt = new JPanel();

	/**
	 * �Զ������ʱ����
	 */
	private JSpinner jSAutoSaveInterval = new JSpinner(new SpinnerNumberModel(
			ConfigOptions.iAutoSaveMinutes.intValue(), 1, 60 * 24, 1));

	private JLabel jLAutoSaveMinutes = new JLabel(
			mm.getMessage("dialogoptions.autosaveminutes"));

	// private JLabel jLAutoSaveDir = new JLabel("�½��ļ�����·��");

	// private JTextField jTFAutoSaveDir = new JTextField();

	/**
	 * �Զ�����ַ���β��\0��ѡ��
	 */
	private JCheckBox jCBAutoTrimChar0 = new JCheckBox();

	/**
	 * ������������������ǩ
	 */
	private JLabel jLUndoCount = new JLabel(IdeSplMessage.get().getMessage(
			"dialogoptions.undocount")); // ����/������������
	/**
	 * �����������������ؼ�
	 */
	private JSpinner jSUndoCount = new JSpinner(new SpinnerNumberModel(20, 5,
			Integer.MAX_VALUE, 1));

	/**
	 * ���ǩ�ؼ�
	 */
	protected JTabbedPane tabMain = new JTabbedPane();

	/**
	 * ���ݿ����ӳ�ʱʱ��
	 */
	private JSpinner jSConnectTimeout = new JSpinner(new SpinnerNumberModel(10,
			0, Integer.MAX_VALUE, 1));

	/**
	 * �����С�ؼ�
	 */
	private JSpinner jSFontSize = new JSpinner(new SpinnerNumberModel(12, 1,
			36, 1));

	/**
	 * ���쳣д����־�ļ��ؼ�
	 */
	// private JCheckBox jCBLogException = new JCheckBox();
	/**
	 * ���ӵ����ݿ�ʱ��ȴ�
	 */
	private JLabel jLabelTimeout = new JLabel();

	/**
	 * ��
	 */
	private JLabel jLabel9 = new JLabel();

	/**
	 * �ӹܿ���̨��ѡ��
	 */
	private JCheckBox jCBIdeConsole = new JCheckBox();

	/**
	 * �Զ�������ļ���ѡ��
	 */
	private JCheckBox jCBAutoOpen = new JCheckBox();

	/**
	 * ��ʾ���ݿ�ṹ��ѡ��
	 */
	private JCheckBox jCBShowDBStruct = new JCheckBox();

	/**
	 * ע�⣺����ѡ��������ɫ��ѡ����Ҫ��������IDE������Ч��
	 */
	private JLabel jLabelNote = new JLabel();

	/**
	 * Ӧ�ó�����۱�ǩ
	 */
	private JLabel jLabel22 = new JLabel();

	/**
	 * ���䴰��λ�ô�С��ѡ��
	 */
	private JCheckBox jCBWindow = new JCheckBox();

	/**
	 * ���ݳ����Ԫ����ʾ��ѡ��
	 */
	private JCheckBox jCBDispOutCell = new JCheckBox();
	/**
	 * ���������ʽ�༭��ѡ��
	 */
	private JCheckBox jCBMultiLineExpEditor = new JCheckBox();

	/**
	 * ����ִ��ʱ�����渴ѡ��
	 */
	private JCheckBox jCBStepLastLocation = new JCheckBox();

	/**
	 * �Զ������и߸�ѡ��
	 */
	private JCheckBox jCBAutoSizeRowHeight = new JCheckBox();

	/**
	 * �Ƿ��Ǩע�͸��еĵ�Ԫ��
	 */
	private JCheckBox jCBAdjustNoteCell = new JCheckBox(
			mm.getMessage("dialogoptions.adjustnotecell"));

	/**
	 * ��־�����ǩ
	 */
	private JLabel jLabelLevel = new JLabel();

	/**
	 * ��־����������
	 */
	private JComboBoxEx jCBLevel = new JComboBoxEx();

	/**
	 * ��־���ͱ�ǩ
	 */
	private JLabel jLabelLogType = new JLabel();

	/**
	 * ��־����������
	 */
	private JComboBoxEx jCBLogType = new JComboBoxEx();

	/**
	 * JAVA������ڴ��ǩ
	 */
	private JLabel jLXmx = new JLabel(mm.getMessage("dialogoptions.xmx"));
	/**
	 * JAVA������ڴ��ı���
	 */
	private JTextField jTFXmx = new JTextField();

	/**
	 * GC��־�ļ�
	 */
	private JLabel jLGCLog = new JLabel(mm.getMessage("dialogoptions.gclog"));

	private JTextField jTFGCLog = new JTextField();

	private JButton jBGCLog = new JButton(mm.getMessage("dialogoptions.select"));

	/**
	 * ��ӡGC����ϸ��־
	 */
	private JCheckBox jCBPrintGCDetails = new JCheckBox(
			mm.getMessage("dialogoptions.printgcdetails"));
	/**
	 * ��ӡGCʱ���
	 */
	private JCheckBox jCBPrintGCTimeStamps = new JCheckBox(
			mm.getMessage("dialogoptions.printgctime"));
	/**
	 * ��ӡGC����ʱ���
	 */
	private JCheckBox jCBPrintGCDateStamps = new JCheckBox(
			mm.getMessage("dialogoptions.printgcdate"));
	/**
	 * ��ӡGCʱ�ѵ���Ϣ
	 */
	private JCheckBox jCBPrintHeapAtGC = new JCheckBox(
			mm.getMessage("dialogoptions.printheap"));

	/**
	 * �����ؼ�
	 */
	private JSpinner jSPRowCount;

	/**
	 * �����ؼ�
	 */
	private JSpinner jSPColCount;

	/**
	 * �и߿ؼ�
	 */
	private JSpinner jSPRowHeight;

	/**
	 * �п�ؼ�
	 */
	private JSpinner jSPColWidth;

	/**
	 * ����ǰ��ɫ�ؼ�
	 */
	private ColorComboBox constFColor;
	/**
	 * ��������ɫ�ؼ�
	 */
	private ColorComboBox constBColor;
	/**
	 * ע�͸�ǰ��ɫ�ؼ�
	 */
	private ColorComboBox noteFColor;
	/**
	 * ע�͸񱳾�ɫ�ؼ�
	 */
	private ColorComboBox noteBColor;
	/**
	 * ��ֵ��ǰ��ɫ�ؼ�
	 */
	private ColorComboBox valFColor;
	/**
	 * ��ֵ�񱳾�ɫ�ؼ�
	 */
	private ColorComboBox valBColor;
	/**
	 * ��ֵ��ǰ��ɫ�ؼ�
	 */
	private ColorComboBox nValFColor;
	/**
	 * ��ֵ�񱳾�ɫ�ؼ�
	 */
	private ColorComboBox nValBColor;
	/**
	 * ��������������
	 */
	private JComboBox jCBFontName;
	/**
	 * �����С������
	 */
	private JComboBoxEx jCBFontSize;
	/**
	 * ���帴ѡ��
	 */
	private JCheckBox jCBBold;
	/**
	 * б�帴ѡ��
	 */
	private JCheckBox jCBItalic;
	/**
	 * �»��߸�ѡ��
	 */
	private JCheckBox jCBUnderline;
	/**
	 * �����߸�ѡ��
	 */
	private JCheckBox jCBGridline;
	/**
	 * ����༭�ؼ�
	 */
	private JSpinner jSPIndent;
	/**
	 * ˮƽ����������
	 */
	private JComboBoxEx jCBHAlign;
	/**
	 * ��ֱ����������
	 */
	private JComboBoxEx jCBVAlign;

	/**
	 * �ֺ�
	 */
	private JLabel labelFontSize = new JLabel(
			mm.getMessage("dialogoptions.fontsize"));

	/**
	 * ������ʾ��Ա���༭�ؼ�
	 */
	private JSpinner jSSeqMembers;
	/**
	 * ����
	 */
	private JLabel labelLocale = new JLabel(
			mm.getMessage("dialogoptions.labellocale"));
	/**
	 * ����
	 */
	private JLabel labelFontName = new JLabel(
			mm.getMessage("dialogoptions.fontname"));

	/**
	 * ��������������
	 */
	private JComboBoxEx jCBLocale = new JComboBoxEx();

	/** ����ҳ */
	private final byte TAB_NORMAL = 0;
	/** ����ҳ */
	private final byte TAB_ENV = 1;

	/**
	 * �Ƿ�ڵ��ѡ��
	 */
	private boolean isUnit = false;

	/**
	 * �Ƿ�����CTRL��
	 */
	private boolean isCtrlDown = false;

	/**
	 * �������
	 */
	protected PanelEnv panelEnv;

	/**
	 * ���캯��
	 */
	public DialogOptions() {
		this(GV.appFrame, false);
	}

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            �����ڿؼ�
	 * @param isUnit
	 *            �Ƿ�ڵ��ѡ���
	 */
	public DialogOptions(JFrame parent, boolean isUnit) {
		super(parent, "ѡ��", true);
		try {
			this.isUnit = isUnit;
			if (isUnit) {
				loadUnitServerConfig();
				GV.dsModel = new DataSourceListModel();
			}
			initUI();
			load();
			int dialogWidth = 830;
			int dialogHeight = 590;
			if (GC.LANGUAGE == GC.ASIAN_CHINESE && !isUnit) {
				dialogWidth = 700;
			}
			setSize(dialogWidth, dialogHeight);
			if (isUnit) {
				ConfigOptions.bWindowSize = Boolean.FALSE;
			}
			GM.setDialogDefaultButton(this, jBOK, jBCancel);
			resetLangText();
			jCBMultiLineExpEditor.setVisible(false);
			addListener(tabMain);
			setResizable(true);
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
	 * ���̼�����
	 */
	private KeyListener keyListener = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if (isUnit)
				return;
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				if (e.isControlDown()) {
					showNextTab();
					isCtrlDown = true;
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				isCtrlDown = false;
			}
		}

		public void keyTyped(KeyEvent e) {
		}
	};

	/**
	 * ��ʾ��һ����ǩҳ
	 */
	private void showNextTab() {
		int size = tabMain.getComponentCount();
		if (size <= 1) {
			return;
		}
		int index = size - 1;
		int i = tabMain.getSelectedIndex();
		if (isCtrlDown) {
			index = size - 1;
		} else {
			if (i == size - 1) {
				index = 0;
			} else {
				index = i + 1;
			}
		}
		tabMain.setSelectedIndex(index);
	}

	/**
	 * ���Ӱ���������
	 * 
	 * @param comp
	 */
	private void addListener(JComponent comp) {
		Component[] comps = comp.getComponents();
		if (comps != null) {
			for (int i = 0; i < comps.length; i++) {
				if (comps[i] instanceof JComponent) {
					JComponent jcomp = (JComponent) comps[i];
					jcomp.addKeyListener(keyListener);
					addListener(jcomp);
				}
			}
		}
	}

	/**
	 * ����������Դ
	 */
	void resetLangText() {
		setTitle(mm.getMessage("dialogoptions.title")); // ѡ��
		jBOK.setText(mm.getMessage("button.ok"));
		jBCancel.setText(mm.getMessage("button.cancel"));
		// Normal
		jCBIdeConsole.setText(mm.getMessage("dialogoptions.ideconsole")); // �ӹܿ���̨
		jCBAutoOpen.setText(mm.getMessage("dialogoptions.autoopen")); // �Զ��򿪣�����ļ���
		jCBAutoBackup.setText(mm.getMessage("dialogoptions.autobackup")); // ����ʱ�Զ����ݣ����ļ���׺.BAK��
		// jCBLogException.setText(mm.getMessage("dialogoptions.logexception"));
		// // ���쳣д����־�ļ�
		jCBAutoConnect.setText(mm.getMessage("dialogoptions.autoconnect")); // �Զ����ӣ�������ӣ�
		jCBAutoTrimChar0.setText(mm.getMessage("dialogoptions.autotrimchar0")); // �Զ�����ַ���β��\0
		jCBWindow.setText(mm.getMessage("dialogoptions.windowsize")); // ���䴰��λ�ô�С
		jLabel22.setText(mm.getMessage("dialogoptions.applnf")); // Ӧ�ó������
		jLabelTimeout.setText(mm.getMessage("dialogoptions.timeoutnum")); // ���ӵ����ݿ�ʱ��ȴ�
		jLabel9.setText(mm.getMessage("dialogoptions.second")); // ��
		jLabelNote.setText(mm.getMessage("dialogoptions.attention")); // ע�⣺����ѡ��������ɫ��ѡ����Ҫ��������IDE������Ч��
		jLabelLevel.setText(mm.getMessage("dialogoptions.loglevel")); // ��־����
		jLabelLogType.setText(mm.getMessage("dialogoptions.logtype")); // ��־����
		jCBDispOutCell.setText(mm.getMessage("dialogoptions.dispoutcell")); // ���ݳ����Ԫ����ʾ
		jCBAutoSizeRowHeight.setText(mm
				.getMessage("dialogoptions.autosizerowheight")); // �Զ������и�
		jCBShowDBStruct.setText(mm.getMessage("dialogoptions.showdbstruct"));

		jCBMultiLineExpEditor.setText(mm.getMessage("dialogoptions.multiline")); // ���������ʽ�༭��
		jCBStepLastLocation.setText(mm
				.getMessage("dialogoptions.steplastlocation")); // ����ִ��ʱ������

	}

	protected void saveCustom() {
	}

	/**
	 * ����ѡ��
	 * 
	 * @return
	 * @throws Throwable
	 */
	private boolean save() throws Throwable {
		if (!checkXmx()) {
			if (!isUnit)
				this.tabMain.setSelectedIndex(TAB_NORMAL);
			return false;
		}
		Map<String, String> jvmArgMap = new HashMap<String, String>();

		// Xmx, Xms
		String newXmx = jTFXmx.getText();
		if (isArgChanged(oldXmx, newXmx)) {
			newXmx = newXmx.trim();
			try {
				Integer.parseInt(newXmx);
				newXmx += "M"; // ûд��λƴ��M
			} catch (Exception e) {
			}
			if (StringUtils.isValidString(newXmx)) {
				jvmArgMap.put(GMSpl.KEY_XMX, GMSpl.KEY_XMX + newXmx);
				jvmArgMap.put(GMSpl.KEY_XMS, GMSpl.KEY_XMS + newXmx);
			} else {
				jvmArgMap.put(GMSpl.KEY_XMX, "");
				jvmArgMap.put(GMSpl.KEY_XMS, "");
			}
		}
		// -Xloggc:
		String newGCLogFile = jTFGCLog.getText();
		if (isArgChanged(oldGCLogFile, newGCLogFile)) {
			if (StringUtils.isValidString(newGCLogFile)) {
				if (GM.isWindowsOS()) // windowsϵͳ������
					newGCLogFile = Escape.addEscAndQuote(newGCLogFile);
				jvmArgMap
						.put(GMSpl.KEY_GC_LOG, GMSpl.KEY_GC_LOG + newGCLogFile);
			} else {
				jvmArgMap.put(GMSpl.KEY_GC_LOG, "");
			}
		}

		// -XX:+PrintGCDetails
		boolean newPrintGCDetails = jCBPrintGCDetails.isSelected();
		if (oldPrintGCDetails != newPrintGCDetails) {
			jvmArgMap.put(GMSpl.KEY_PRINT_GC_DETAILS,
					newPrintGCDetails ? GMSpl.KEY_PRINT_GC_DETAILS : "");
		}

		// -XX:+PrintGCTimeStamps
		boolean newPrintGCTimeStamps = jCBPrintGCTimeStamps.isSelected();
		if (oldPrintGCTimeStamps != newPrintGCTimeStamps) {
			jvmArgMap.put(GMSpl.KEY_PRINT_GC_TIMESTAMPS,
					newPrintGCTimeStamps ? GMSpl.KEY_PRINT_GC_TIMESTAMPS : "");
		}

		// -XX:+PrintGCDateStamps
		boolean newPrintGCDateStamps = jCBPrintGCDateStamps.isSelected();
		if (oldPrintGCDateStamps != newPrintGCDateStamps) {
			jvmArgMap.put(GMSpl.KEY_PRINT_GC_DATESTAMPS,
					newPrintGCDateStamps ? GMSpl.KEY_PRINT_GC_DATESTAMPS : "");
		}

		// -XX:+PrintHeapAtGC
		boolean newPrintHeapAtGC = jCBPrintHeapAtGC.isSelected();
		if (oldPrintHeapAtGC != newPrintHeapAtGC) {
			jvmArgMap.put(GMSpl.KEY_PRINT_GC_HEAP,
					newPrintHeapAtGC ? GMSpl.KEY_PRINT_GC_HEAP : "");
		}

		if (!jvmArgMap.isEmpty())
			GMSpl.setJVMArgs(jvmArgMap);

		// Normal
		ConfigOptions.iUndoCount = (Integer) jSUndoCount.getValue();
		ConfigOptions.bIdeConsole = new Boolean(jCBIdeConsole.isSelected());
		ConfigOptions.bAutoOpen = new Boolean(jCBAutoOpen.isSelected());
		ConfigOptions.bAutoBackup = new Boolean(jCBAutoBackup.isSelected());
		// ConfigOptions.bLogException = new
		// Boolean(jCBLogException.isSelected());
		ConfigOptions.bAutoConnect = new Boolean(jCBAutoConnect.isSelected());
		ConfigOptions.bAutoSave = new Boolean(jCBAutoSave.isSelected());
		ConfigOptions.iAutoSaveMinutes = ((Number) jSAutoSaveInterval
				.getValue()).intValue();
		// ConfigOptions.sBackupDirectory = jTFAutoSaveDir.getText();
		ConfigOptions.bAutoTrimChar0 = new Boolean(
				jCBAutoTrimChar0.isSelected());
		ConfigOptions.bAdjustNoteCell = new Boolean(
				jCBAdjustNoteCell.isSelected());
		ConfigOptions.bWindowSize = new Boolean(jCBWindow.isSelected());
		ConfigOptions.iLookAndFeel = (Byte) jCBLNF.x_getSelectedItem();
		ConfigOptions.iConnectTimeout = (Integer) jSConnectTimeout.getValue();
		ConfigOptions.iFontSize = ((Integer) jSFontSize.getValue())
				.shortValue();
		ConfigOptions.bDispOutCell = new Boolean(jCBDispOutCell.isSelected());
		ConfigOptions.bMultiLineExpEditor = new Boolean(
				jCBMultiLineExpEditor.isSelected());
		ConfigOptions.bStepLastLocation = new Boolean(
				jCBStepLastLocation.isSelected());
		ConfigOptions.bAutoSizeRowHeight = new Boolean(
				jCBAutoSizeRowHeight.isSelected());
		ConfigOptions.bShowDBStruct = new Boolean(jCBShowDBStruct.isSelected());

		ConfigOptions.iRowCount = (Integer) jSPRowCount.getValue();
		ConfigOptions.iColCount = (Integer) jSPColCount.getValue();
		ConfigOptions.fRowHeight = new Float(jSPRowHeight.getValue().toString());
		ConfigOptions.fColWidth = new Float(jSPColWidth.getValue().toString());
		ConfigOptions.iConstFColor = new Color(constFColor.getColor()
				.intValue());
		ConfigOptions.iConstBColor = new Color(constBColor.getColor()
				.intValue());
		ConfigOptions.iNoteFColor = new Color(noteFColor.getColor().intValue());
		ConfigOptions.iNoteBColor = new Color(noteBColor.getColor().intValue());
		ConfigOptions.iValueFColor = new Color(valFColor.getColor().intValue());
		ConfigOptions.iValueBColor = new Color(valBColor.getColor().intValue());
		ConfigOptions.iNValueFColor = new Color(nValFColor.getColor()
				.intValue());
		ConfigOptions.iNValueBColor = new Color(nValBColor.getColor()
				.intValue());
		ConfigOptions.sFontName = (String) jCBFontName.getSelectedItem();
		Object oSize = jCBFontSize.x_getSelectedItem();
		Short iSize;
		if (oSize instanceof String) { // �û�ֱ�����������
			iSize = new Short((String) oSize);
		} else {
			iSize = (Short) oSize;
		}
		ConfigOptions.iFontSize = iSize;
		ConfigOptions.bBold = new Boolean(jCBBold.isSelected());
		ConfigOptions.bItalic = new Boolean(jCBItalic.isSelected());
		ConfigOptions.bUnderline = new Boolean(jCBUnderline.isSelected());
		ConfigOptions.bGridline = new Boolean(jCBGridline.isSelected());
		ConfigOptions.iIndent = (Integer) jSPIndent.getValue();
		ConfigOptions.iSequenceDispMembers = (Integer) jSSeqMembers.getValue();
		ConfigOptions.iHAlign = (Byte) jCBHAlign.x_getSelectedItem();
		ConfigOptions.iVAlign = (Byte) jCBVAlign.x_getSelectedItem();
		ConfigOptions.iLocale = (Byte) jCBLocale.x_getSelectedItem();
		saveCustom();

		panelEnv.save();

		if (GVSpl.splEditor != null) {
			GVSpl.splEditor.getComponent().repaint();
		}
		String sLogLevel = (String) jCBLevel.x_getSelectedItem();
		GV.config.setLogLevel(sLogLevel);
		Logger.setLevel(sLogLevel);

		String sLogType = (String) jCBLogType.x_getSelectedItem();
		GV.config.setLogType(sLogType);
		Logger.setLogType(sLogType);

		ConfigOptions.save(!isUnit);
		try {
			ConfigUtilIde.writeConfig(!isUnit);
		} catch (Exception ex) {
			GM.showException(this, ex);
		}
		return true;
	}

	private boolean isArgChanged(String oldArg, String newArg) {
		if (StringUtils.isValidString(oldArg)) {
			if (!StringUtils.isValidString(newArg)) // ɾ��
				return true;
			if (!oldArg.equalsIgnoreCase(newArg)) { // �޸�
				return true;
			}
		} else {
			if (StringUtils.isValidString(newArg)) { // ����
				return true;
			}
		}
		return false;
	}

	private String oldXmx = null;
	private boolean oldPrintGCDetails = false;
	private boolean oldPrintGCTimeStamps = false;
	private boolean oldPrintGCDateStamps = false;
	private String oldGCLogFile = null;
	private boolean oldPrintHeapAtGC = false;

	/**
	 * ����ѡ��
	 */
	private void load() {
		final String[] JVM_ARG_KEYS = new String[] { GMSpl.KEY_XMX,
				GMSpl.KEY_PRINT_GC_DETAILS, GMSpl.KEY_PRINT_GC_TIMESTAMPS,
				GMSpl.KEY_PRINT_GC_DATESTAMPS, GMSpl.KEY_GC_LOG,
				GMSpl.KEY_PRINT_GC_HEAP };
		Map<String, String> jvmArgMap = GMSpl.getJVMArgs(JVM_ARG_KEYS);
		if (jvmArgMap != null) {
			// Xmx
			String xmx = jvmArgMap.get(GMSpl.KEY_XMX);
			if (StringUtils.isValidString(xmx)) {
				oldXmx = xmx.substring(GMSpl.KEY_XMX.length());
				if (StringUtils.isValidString(oldXmx)) {
					oldXmx = oldXmx.trim();
					jTFXmx.setText(oldXmx);
				}
			}
			// -Xloggc:
			String gcLogFile = jvmArgMap.get(GMSpl.KEY_GC_LOG);
			if (StringUtils.isValidString(gcLogFile)) {
				gcLogFile = gcLogFile.substring(GMSpl.KEY_GC_LOG.length());
				if (StringUtils.isValidString(gcLogFile)) {
					oldGCLogFile = gcLogFile.trim();
					if (GM.isWindowsOS()) // windowsϵͳ������
						oldGCLogFile = Escape.removeEscAndQuote(oldGCLogFile);
					jTFGCLog.setText(oldGCLogFile);
				}
			}

			// -XX:+PrintGCDetails
			String printGCDetails = jvmArgMap.get(GMSpl.KEY_PRINT_GC_DETAILS);
			if (StringUtils.isValidString(printGCDetails)) {
				oldPrintGCDetails = true;
				jCBPrintGCDetails.setSelected(true);
			}

			// -XX:+PrintGCTimeStamps
			String printGCTimeStamps = jvmArgMap
					.get(GMSpl.KEY_PRINT_GC_TIMESTAMPS);
			if (StringUtils.isValidString(printGCTimeStamps)) {
				oldPrintGCTimeStamps = true;
				jCBPrintGCTimeStamps.setSelected(true);
			}

			// -XX:+PrintGCDateStamps
			String printGCDateStamps = jvmArgMap
					.get(GMSpl.KEY_PRINT_GC_DATESTAMPS);
			if (StringUtils.isValidString(printGCDateStamps)) {
				oldPrintGCDateStamps = true;
				jCBPrintGCDateStamps.setSelected(true);
			}

			// -XX:+PrintHeapAtGC
			String printHeapAtGC = jvmArgMap.get(GMSpl.KEY_PRINT_GC_HEAP);
			if (StringUtils.isValidString(printHeapAtGC)) {
				oldPrintHeapAtGC = true;
				jCBPrintHeapAtGC.setSelected(true);
			}

		}

		jSUndoCount.setValue(ConfigOptions.iUndoCount);
		jCBIdeConsole.setSelected(ConfigOptions.bIdeConsole.booleanValue());
		jCBAutoOpen.setSelected(ConfigOptions.bAutoOpen.booleanValue());
		jCBAutoBackup.setSelected(ConfigOptions.bAutoBackup.booleanValue());
		// jCBLogException.setSelected(ConfigOptions.bLogException.booleanValue());
		jCBAutoConnect.setSelected(ConfigOptions.bAutoConnect.booleanValue());
		jCBAutoSave.setSelected(ConfigOptions.bAutoSave.booleanValue());
		jSAutoSaveInterval.setValue(ConfigOptions.iAutoSaveMinutes.intValue());
		// jTFAutoSaveDir.setText(ConfigOptions.sBackupDirectory);
		jCBAutoTrimChar0.setSelected(ConfigOptions.bAutoTrimChar0
				.booleanValue());
		jCBWindow.setSelected(ConfigOptions.bWindowSize.booleanValue());
		jCBDispOutCell.setSelected(ConfigOptions.bDispOutCell.booleanValue());
		jCBMultiLineExpEditor.setSelected(ConfigOptions.bMultiLineExpEditor
				.booleanValue());
		jCBStepLastLocation.setSelected(ConfigOptions.bStepLastLocation
				.booleanValue());
		jCBAutoSizeRowHeight.setSelected(ConfigOptions.bAutoSizeRowHeight
				.booleanValue());

		jCBShowDBStruct.setSelected(ConfigOptions.bShowDBStruct.booleanValue());
		jCBAdjustNoteCell.setSelected(ConfigOptions.bAdjustNoteCell
				.booleanValue());
		jCBAdjustNoteCell.setSelected(Env.isAdjustNoteCell());

		jCBLevel.x_setSelectedCodeItem(Logger.getLevelName(Logger.getLevel()));
		jCBLogType.x_setSelectedCodeItem(GV.config.getLogType());
		jCBLNF.x_setSelectedCodeItem(LookAndFeelManager
				.getValidLookAndFeel(ConfigOptions.iLookAndFeel));
		jSConnectTimeout.setValue(ConfigOptions.iConnectTimeout);
		jSFontSize.setValue(new Integer(ConfigOptions.iFontSize.intValue()));
		jSPRowCount.setValue(ConfigOptions.iRowCount);
		jSPColCount.setValue(ConfigOptions.iColCount);
		jSPRowHeight.setValue(new Double(ConfigOptions.fRowHeight));
		jSPColWidth.setValue(new Double(ConfigOptions.fColWidth));

		constFColor.setSelectedItem(new Integer(ConfigOptions.iConstFColor
				.getRGB()));
		constBColor.setSelectedItem(new Integer(ConfigOptions.iConstBColor
				.getRGB()));
		noteFColor.setSelectedItem(new Integer(ConfigOptions.iNoteFColor
				.getRGB()));
		noteBColor.setSelectedItem(new Integer(ConfigOptions.iNoteBColor
				.getRGB()));
		valFColor.setSelectedItem(new Integer(ConfigOptions.iValueFColor
				.getRGB()));
		valBColor.setSelectedItem(new Integer(ConfigOptions.iValueBColor
				.getRGB()));
		nValFColor.setSelectedItem(new Integer(ConfigOptions.iNValueFColor
				.getRGB()));
		nValBColor.setSelectedItem(new Integer(ConfigOptions.iNValueBColor
				.getRGB()));

		jCBFontName.setSelectedItem(ConfigOptions.sFontName);
		jCBFontSize.setSelectedItem(ConfigOptions.iFontSize);
		jCBBold.setSelected(ConfigOptions.bBold.booleanValue());
		jCBItalic.setSelected(ConfigOptions.bItalic.booleanValue());
		jCBUnderline.setSelected(ConfigOptions.bUnderline.booleanValue());
		jCBGridline.setSelected(ConfigOptions.bGridline.booleanValue());
		jSPIndent.setValue(ConfigOptions.iIndent);
		jSSeqMembers.setValue(ConfigOptions.iSequenceDispMembers);
		jCBHAlign
				.x_setSelectedCodeItem(compatibleHalign(ConfigOptions.iHAlign));
		jCBVAlign
				.x_setSelectedCodeItem(compatibleValign(ConfigOptions.iVAlign));
		panelEnv.load();

		if (ConfigOptions.iLocale != null) {
			jCBLocale.x_setSelectedCodeItem(ConfigOptions.iLocale.byteValue());
		} else {
			if (GC.LANGUAGE == GC.ASIAN_CHINESE) {
				jCBLocale.x_setSelectedCodeItem(new Byte(GC.ASIAN_CHINESE));
			} else {
				jCBLocale.x_setSelectedCodeItem(new Byte(GC.ENGLISH));
			}
		}
		autoOpenChanged();

	}

	/**
	 * ���ؽڵ������
	 * 
	 * @return
	 */
	private RaqsoftConfig loadUnitServerConfig() {
		InputStream is = null;
		try {
			is = UnitContext.getUnitInputStream("raqsoftConfig.xml");
			GV.config = ConfigUtilIde.loadConfig(is);
		} catch (Exception x) {
			GV.config = new RaqsoftConfig();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}
		return GV.config;
	}

	/**
	 * �Զ����ļ�ѡ��仯��
	 */
	private void autoOpenChanged() {
		boolean isAutoOpen = jCBAutoOpen.isSelected();
		jCBAutoSave.setEnabled(isAutoOpen);
		if (!jCBAutoOpen.isSelected()) {
			if (jCBAutoSave.isSelected()) {
				jCBAutoSave.setSelected(false);
			}
		}
		autoSaveChanged();
	}

	/**
	 * �Զ�����ѡ��仯��
	 */
	private void autoSaveChanged() {
		boolean isAutoSave = jCBAutoSave.isSelected();
		jLAutoSaveInterval.setEnabled(isAutoSave);
		jSAutoSaveInterval.setEnabled(isAutoSave);
		jLAutoSaveMinutes.setEnabled(isAutoSave);
		// jLAutoSaveDir.setEnabled(isAutoSave);
		// jTFAutoSaveDir.setEnabled(isAutoSave);
	}

	public static final Color NOTE_COLOR = new Color(165, 0, 0);

	/**
	 * ��ʼ������
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		jCBAutoBackup.setText("����ʱ�Զ����ݣ����ļ���׺.BAK��");
		jCBAutoConnect.setText("�Զ����ӣ�������ӣ�");
		jCBAutoConnect.setEnabled(true);
		jCBAutoConnect.setForeground(Color.blue);
		labelLocale.setForeground(Color.blue);
		// jCBLogException.setText("���쳣д����־�ļ�");
		jLabelTimeout.setText("���ӵ����ݿ�ʱ��ȴ�");
		jSConnectTimeout.setBorder(BorderFactory.createLoweredBevelBorder());
		jSConnectTimeout.setPreferredSize(new Dimension(80, 25));

		jLXmx.setForeground(Color.BLUE);
		jLGCLog.setForeground(Color.BLUE);
		jCBPrintGCDetails.setForeground(Color.BLUE);
		jCBPrintGCTimeStamps.setForeground(Color.BLUE);
		jCBPrintGCDateStamps.setForeground(Color.BLUE);
		jCBPrintHeapAtGC.setForeground(Color.BLUE);

		jSFontSize.setBorder(BorderFactory.createLoweredBevelBorder());
		jCBIdeConsole.setText("�ӹܿ���̨");
		jCBAutoOpen.setForeground(Color.blue);
		jCBAutoOpen.setText("�Զ��򿪣�����ļ���");

		jLabel9.setText("��");

		jLabelNote.setForeground(NOTE_COLOR);
		jLabelNote.setText("ע�⣺ѡ��������ɫ��ѡ����Ҫ��������IDE������Ч��");
		jLabel22.setForeground(Color.blue);
		jLabel22.setText("Ӧ�ó������");
		jCBWindow.setText("���䴰��λ�ô�С");
		jCBDispOutCell.setText("���ݳ����Ԫ����ʾ");
		jCBMultiLineExpEditor.setText("���б��ʽ�༭");
		jCBStepLastLocation.setText("����ִ��ʱ������");
		jCBAutoSizeRowHeight.setText("�Զ������и�");
		Vector<Byte> lnfCodes = LookAndFeelManager.listLNFCode();
		Vector<String> lnfDisps = LookAndFeelManager.listLNFDisp();
		jCBLNF.x_setData(lnfCodes, lnfDisps);

		// Button
		JPanel jPanelButton = new JPanel();
		VFlowLayout VFlowLayout1 = new VFlowLayout();
		jPanelButton.setLayout(VFlowLayout1);
		jBOK.setActionCommand("");
		jBOK.setText("ȷ��(O)");
		jBOK.addActionListener(new DialogOptions_jBOK_actionAdapter(this));
		jBOK.setMnemonic('O');
		jBCancel.setActionCommand("");
		jBCancel.setText("ȡ��(C)");
		jBCancel.addActionListener(new DialogOptions_jBCancel_actionAdapter(
				this));
		jBCancel.setMnemonic('C');
		jPanelButton.add(jBOK, null);
		jPanelButton.add(jBCancel, null);
		jLabelLevel.setText("��־����");
		jCBLevel.x_setData(ConfigOptions.dispLevels(),
				ConfigOptions.dispLevels());
		jCBLevel.x_setSelectedCodeItem(Logger.DEBUG);

		jLabelLogType.setText("��־����");
		jCBLogType.x_setData(ConfigOptions.codeLogTypes(),
				ConfigOptions.dispLogTypes());
		jCBLogType.x_setSelectedCodeItem(ConfigConsts.LOG_DEFAULT);
		// Normal
		JPanel panelNormal = new JPanel(new VFlowLayout(VFlowLayout.TOP));
		panelNormal.add(jPIdeOpt);
		jPIdeOpt.setLayout(ideOptLayout);
		jPIdeOpt.add(jCBIdeConsole, null);
		// jPanel2.add(jCBAutoOpen, null);
		// jPanel2.add(jCBAutoSave, null);
		jPIdeOpt.add(jCBAutoBackup, null);
		// jPanel2.add(jCBLogException, null);
		jPIdeOpt.add(jCBAutoConnect, null);
		jPIdeOpt.add(jCBWindow, null);
		jPIdeOpt.add(jCBDispOutCell, null);
		jPIdeOpt.add(jCBAutoSizeRowHeight, null);
		jPIdeOpt.add(jCBShowDBStruct, null);
		jPIdeOpt.add(jCBStepLastLocation, null);
		jPIdeOpt.add(jCBAutoTrimChar0, null);
		jPIdeOpt.add(jCBAdjustNoteCell, null);

		// labelFontName.setForeground(Color.blue);

		jCBFontName = new JComboBox(GM.getFontNames());
		FlowLayout fl1 = new FlowLayout(FlowLayout.LEFT);
		fl1.setHgap(0);
		JPanel jPanelTimeout = new JPanel();
		jPanelTimeout.setLayout(fl1);
		jPanelTimeout.add(jSConnectTimeout);
		jPanelTimeout.add(jLabel9); // ��

		boolean isHighVersionJDK = false;
		String javaVersion = System.getProperty("java.version");
		if (javaVersion.compareTo("1.9") > 0) {
			isHighVersionJDK = true;
		}
		JPanel panelMid = new JPanel(new GridBagLayout());
		if (!isHighVersionJDK) { // SubstanceUI��֧�ָ߰汾JDK
			panelMid.add(jLabel22, GM.getGBC(1, 1));
			panelMid.add(jCBLNF, GM.getGBC(1, 2, true));
			panelMid.add(labelLocale, GM.getGBC(1, 3));
			panelMid.add(jCBLocale, GM.getGBC(1, 4, true));
			panelMid.add(jLabelLevel, GM.getGBC(2, 1));
			panelMid.add(jCBLevel, GM.getGBC(2, 2, true));
			panelMid.add(jLabelLogType, GM.getGBC(2, 3));
			panelMid.add(jCBLogType, GM.getGBC(2, 4, true));
			panelMid.add(labelFontName, GM.getGBC(3, 1));
			panelMid.add(jCBFontName, GM.getGBC(3, 2, true));
			panelMid.add(jLUndoCount, GM.getGBC(3, 3));
			panelMid.add(jSUndoCount, GM.getGBC(3, 4, true));
			panelMid.add(jLabelTimeout, GM.getGBC(4, 1));
			panelMid.add(jPanelTimeout, GM.getGBC(4, 2, true));

		} else {
			panelMid.add(labelLocale, GM.getGBC(1, 1));
			panelMid.add(jCBLocale, GM.getGBC(1, 2, true));
			panelMid.add(labelFontName, GM.getGBC(1, 3));
			panelMid.add(jCBFontName, GM.getGBC(1, 4, true));
			panelMid.add(jLabelLevel, GM.getGBC(2, 1));
			panelMid.add(jCBLevel, GM.getGBC(2, 2, true));
			panelMid.add(jLabelLogType, GM.getGBC(2, 3));
			panelMid.add(jCBLogType, GM.getGBC(2, 4, true));
			panelMid.add(jLUndoCount, GM.getGBC(4, 1));
			panelMid.add(jSUndoCount, GM.getGBC(4, 2, true));
			panelMid.add(jLabelTimeout, GM.getGBC(4, 3));
			panelMid.add(jPanelTimeout, GM.getGBC(4, 4, true));
		}
		// ������/����������������ʱ�����ܻ�ռ�ø�����ڴ档
		jLUndoCount.setToolTipText(IdeSplMessage.get().getMessage(
				"dialogoptions.undocountcause"));
		jSUndoCount.setToolTipText(IdeSplMessage.get().getMessage(
				"dialogoptions.undocountcause"));
		jLUndoCount.setForeground(Color.BLUE);

		// jLabel9.setPreferredSize(new Dimension(45, 25));

		JPanel panelVM = new JPanel(new GridLayout(3, 2));
		panelVM.setBorder(BorderFactory.createTitledBorder(mm
				.getMessage("dialogoptions.vmborder")));
		GridBagConstraints gbc;
		gbc = GM.getGBC(5, 1, true, true);
		gbc.gridwidth = 4;
		// panelMid.add(panelVM, gbc);

		JPanel panelXmx = new JPanel(new GridBagLayout());
		panelXmx.add(jLXmx, GM.getGBC(1, 1));
		panelXmx.add(jTFXmx, GM.getGBC(1, 2, true));
		panelVM.add(panelXmx);

		JPanel panelGCLog = new JPanel(new GridBagLayout());
		panelGCLog.add(jLGCLog, GM.getGBC(1, 1));
		panelGCLog.add(jTFGCLog, GM.getGBC(1, 2, true));
		panelGCLog.add(jBGCLog, GM.getGBC(1, 3));
		panelVM.add(panelGCLog);

		panelVM.add(jCBPrintGCDetails);
		panelVM.add(jCBPrintGCTimeStamps);
		panelVM.add(jCBPrintGCDateStamps);
		panelVM.add(jCBPrintHeapAtGC);

		jLAutoSaveMinutes.setPreferredSize(new Dimension(60, 25));

		JPanel panelAutoSave = new JPanel(new GridBagLayout());
		panelAutoSave.add(jCBAutoOpen, GM.getGBC(0, 0, false, false, 0));
		panelAutoSave.add(new JPanel(), GM.getGBC(0, 1));
		panelAutoSave.add(jCBAutoSave, GM.getGBC(0, 2));
		// panelAutoSave.add(new JPanel(), GM.getGBC(0, 3));
		panelAutoSave.add(jLAutoSaveInterval, GM.getGBC(0, 4, false, false, 2));
		panelAutoSave.add(jSAutoSaveInterval, GM.getGBC(0, 5, false, false, 2));
		panelAutoSave.add(jLAutoSaveMinutes, GM.getGBC(0, 6, false, false, 2));
		panelAutoSave.add(new JPanel(), GM.getGBC(0, 7, true, false, 0));

		panelNormal.add(panelAutoSave);

		// gbc = GM.getGBC(6, 1, true, false, 0);
		// gbc.gridwidth = 4;
		// panelMid.add(panelAutoSave, gbc);

		// JPanel panelAutoSaveDir = new JPanel(new GridBagLayout());
		// panelAutoSaveDir.add(new JPanel(), GM.getGBC(0, 0));
		// panelAutoSaveDir.add(jLAutoSaveDir, GM.getGBC(0, 1));
		// panelAutoSaveDir.add(jTFAutoSaveDir, GM.getGBC(0, 2, true));
		// jTFAutoSaveDir.setEditable(false);
		// gbc = GM.getGBC(7, 1, true, false, 0);
		// gbc.gridwidth = 4;
		// panelMid.add(panelAutoSaveDir, gbc);

		jCBAutoOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				autoOpenChanged();
			}

		});

		jCBAutoSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				autoSaveChanged();
			}

		});

		this.jBGCLog.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File f = GM.dialogSelectFile(DialogOptions.this,
						AppConsts.FILE_LOG);
				if (f != null) {
					jTFGCLog.setText(f.getAbsolutePath());
				}
			}

		});

		panelNormal.add(panelMid);
		panelNormal.add(panelVM);
		JPanel jp1 = new JPanel();
		jp1.setLayout(new GridBagLayout());
		jp1.add(jLabelNote, GM.getGBC(1, 1, true));
		panelNormal.add(jp1);

		// Env
		JPanel panelSpl = new JPanel();
		panelSpl.setLayout(new BorderLayout());
		JPanel panelSplGrid = new JPanel();
		panelSpl.add(panelSplGrid, BorderLayout.NORTH);
		panelSplGrid.setLayout(new GridBagLayout());
		JLabel labelRowCount = new JLabel(
				mm.getMessage("dialogoptions.rowcount")); // ����
		JLabel labelColCount = new JLabel(
				mm.getMessage("dialogoptions.colcount")); // ����
		JLabel labelRowHeight = new JLabel(
				mm.getMessage("dialogoptions.rowheight")); // �и�
		JLabel labelColWidth = new JLabel(
				mm.getMessage("dialogoptions.colwidth")); // �п�
		JLabel labelCFColor = new JLabel(mm.getMessage("dialogoptions.cfcolor")); // ����ǰ��ɫ
		JLabel labelCBColor = new JLabel(mm.getMessage("dialogoptions.cbcolor")); // ��������ɫ
		JLabel labelNFColor = new JLabel(mm.getMessage("dialogoptions.nfcolor")); // ע��ǰ��ɫ
		JLabel labelNBColor = new JLabel(mm.getMessage("dialogoptions.nbcolor")); // ע�ͱ���ɫ
		JLabel labelVFColor = new JLabel(mm.getMessage("dialogoptions.vfcolor")); // ��ֵ���ʽǰ��ɫ
		JLabel labelVBColor = new JLabel(mm.getMessage("dialogoptions.vbcolor")); // ��ֵ���ʽ����ɫ
		JLabel labelNVFColor = new JLabel(
				mm.getMessage("dialogoptions.nvfcolor")); // ��ֵ���ʽǰ��ɫ
		JLabel labelNVBColor = new JLabel(
				mm.getMessage("dialogoptions.nvbcolor")); // ��ֵ���ʽ����ɫ
		JLabel labelIndent = new JLabel(mm.getMessage("dialogoptions.indent")); // ����
		JLabel labelSeqMembers = new JLabel(
				mm.getMessage("dialogoptions.seqmembers")); // ������ʾ��Ա����

		JLabel labelHAlign = new JLabel(IdeSplMessage.get().getMessage(
				"dialogoptionsdfx.halign")); // ˮƽ����
		JLabel labelVAlign = new JLabel(IdeSplMessage.get().getMessage(
				"dialogoptionsdfx.valign")); // �������
		jSPRowCount = new JSpinner(new SpinnerNumberModel(20, 1, 100000, 1));
		jSPColCount = new JSpinner(new SpinnerNumberModel(6, 1, 10000, 1));
		jSPRowHeight = new JSpinner(new SpinnerNumberModel(25f, 1f, 100f, 1f));
		jSPColWidth = new JSpinner(new SpinnerNumberModel(150f, 1f, 1000f, 1f));

		constFColor = new ColorComboBox();
		constBColor = new ColorComboBox();
		noteFColor = new ColorComboBox();
		noteBColor = new ColorComboBox();
		valFColor = new ColorComboBox();
		valBColor = new ColorComboBox();
		nValFColor = new ColorComboBox();
		nValBColor = new ColorComboBox();

		jCBFontSize = GM.getFontSizes();
		jCBFontSize.setEditable(true);
		jCBBold = new JCheckBox(mm.getMessage("dialogoptions.bold")); // �Ӵ�
		jCBItalic = new JCheckBox(mm.getMessage("dialogoptions.italic")); // ��б
		jCBUnderline = new JCheckBox(mm.getMessage("dialogoptions.underline")); // �»���
		jCBGridline = new JCheckBox(mm.getMessage("dialogoptions.gridline")); // ������
		jSPIndent = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		jSSeqMembers = new JSpinner(new SpinnerNumberModel(3, 1,
				Integer.MAX_VALUE, 1));
		jCBHAlign = new JComboBoxEx();
		jCBHAlign.x_setData(getHAlignCodes(), getHAlignDisps());
		jCBVAlign = new JComboBoxEx();
		jCBVAlign.x_setData(getVAlignCodes(), getVAlignDisps());

		panelSplGrid.add(labelRowCount, GM.getGBC(1, 1));
		panelSplGrid.add(jSPRowCount, GM.getGBC(1, 2, true));
		panelSplGrid.add(labelColCount, GM.getGBC(1, 3));
		panelSplGrid.add(jSPColCount, GM.getGBC(1, 4, true));
		panelSplGrid.add(labelRowHeight, GM.getGBC(2, 1));
		panelSplGrid.add(jSPRowHeight, GM.getGBC(2, 2, true));
		panelSplGrid.add(labelColWidth, GM.getGBC(2, 3));
		panelSplGrid.add(jSPColWidth, GM.getGBC(2, 4, true));
		panelSplGrid.add(labelCFColor, GM.getGBC(3, 1));
		panelSplGrid.add(constFColor, GM.getGBC(3, 2, true));
		panelSplGrid.add(labelCBColor, GM.getGBC(3, 3));
		panelSplGrid.add(constBColor, GM.getGBC(3, 4, true));
		panelSplGrid.add(labelNFColor, GM.getGBC(4, 1));
		panelSplGrid.add(noteFColor, GM.getGBC(4, 2, true));
		panelSplGrid.add(labelNBColor, GM.getGBC(4, 3));
		panelSplGrid.add(noteBColor, GM.getGBC(4, 4, true));
		panelSplGrid.add(labelVFColor, GM.getGBC(5, 1));
		panelSplGrid.add(valFColor, GM.getGBC(5, 2, true));
		panelSplGrid.add(labelVBColor, GM.getGBC(5, 3));
		panelSplGrid.add(valBColor, GM.getGBC(5, 4, true));
		panelSplGrid.add(labelNVFColor, GM.getGBC(6, 1));
		panelSplGrid.add(nValFColor, GM.getGBC(6, 2, true));
		panelSplGrid.add(labelNVBColor, GM.getGBC(6, 3));
		panelSplGrid.add(nValBColor, GM.getGBC(6, 4, true));

		if (!isUnit) {
			panelSplGrid.add(labelFontSize, GM.getGBC(7, 1));
			panelSplGrid.add(jCBFontSize, GM.getGBC(7, 2, true));
		}
		GridBagConstraints gbc8 = GM.getGBC(8, 1, true);
		gbc8.gridwidth = 4;
		JPanel panel8 = new JPanel();
		GridLayout gl8 = new GridLayout();
		gl8.setColumns(4);
		gl8.setRows(1);
		panel8.setLayout(gl8);
		panel8.add(jCBBold);
		panel8.add(jCBItalic);
		panel8.add(jCBUnderline);
		panel8.add(jCBGridline);
		panelSplGrid.add(panel8, gbc8);
		panelSplGrid.add(labelIndent, GM.getGBC(7, 3));
		panelSplGrid.add(jSPIndent, GM.getGBC(7, 4, true));
		panelSplGrid.add(labelHAlign, GM.getGBC(9, 1));
		panelSplGrid.add(jCBHAlign, GM.getGBC(9, 2, true));
		panelSplGrid.add(labelVAlign, GM.getGBC(9, 3));
		panelSplGrid.add(jCBVAlign, GM.getGBC(9, 4, true));
		panelSplGrid.add(labelSeqMembers, GM.getGBC(10, 1));
		panelSplGrid.add(jSSeqMembers, GM.getGBC(10, 2, true));

		jCBLocale.x_setData(GM.getCodeLocale(), GM.getDispLocale());

		panelEnv = new PanelEnv(this, isUnit ? PanelEnv.TYPE_UNIT
				: PanelEnv.TYPE_ESPROC) {

			private static final long serialVersionUID = 1L;

			protected void addOptComponents(JPanel panelOpt) {
				if (isUnit) {
					panelOpt.add(jLabelLevel, GM.getGBC(7, 1));
					panelOpt.add(jCBLevel, GM.getGBC(7, 2, true));

					panelOpt.add(jLabelLogType, GM.getGBC(7, 3));
					panelOpt.add(jCBLogType, GM.getGBC(7, 4, true));

					panelOpt.add(labelFontSize, GM.getGBC(8, 1));
					panelOpt.add(jCBFontSize, GM.getGBC(8, 2, true));
				}
			}

			protected boolean isExtLibsEnabled() {
				return isExtEnabled();
			}

			public void selectEnvTab() {
				if (!isUnit)
					tabMain.setSelectedIndex(TAB_ENV);
			}
		};
		if (isUnit) {
			JPanel panelRestartMessage = new JPanel(new FlowLayout(
					FlowLayout.LEFT));
			panelRestartMessage.add(jLabelNote);
			panelEnv.add(panelRestartMessage, GM.getGBC(4, 1, true));
		}

		if (isUnit) {
			tabMain.add(panelEnv, mm.getMessage("dialogoptions.panel0"));
		} else {
			tabMain.add(panelNormal, mm.getMessage("dialogoptions.panel0"));
			tabMain.add(panelEnv, mm.getMessage("dialogoptions.panel1"));
			tabMain.add(panelSpl, mm.getMessage("dialogoptions.panel2")); // ������
		}

		this.addWindowListener(new DialogOptions_this_windowAdapter(this));
		this.getContentPane().add(tabMain, BorderLayout.CENTER);
		this.getContentPane().add(jPanelButton, BorderLayout.EAST);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setModal(true);

		jCBAutoSave.setToolTipText(AUTO_SAVE_TIP);
		jLAutoSaveInterval.setToolTipText(AUTO_SAVE_TIP);
		jSAutoSaveInterval.setToolTipText(AUTO_SAVE_TIP);
		jLAutoSaveMinutes.setToolTipText(AUTO_SAVE_TIP);
	}

	protected boolean isExtEnabled() {
		return true;
	}

	/**
	 * ���JVM����ڴ�
	 * 
	 * @return
	 */
	private boolean checkXmx() {
		String sNum = jTFXmx.getText();
		if (!StringUtils.isValidString(sNum)) {
			return true; // ����Ͳ�����
		}
		sNum = sNum.trim();
		try {
			Integer.parseInt(sNum);
			// û��д��λĬ����M
			return true;
		} catch (Exception e) {
		}
		// �п���д�˵�λ������G,M,K��
		int buffer = ConfigUtil.parseBufferSize(sNum);
		if (buffer == -1) {
			return true; // ����Ͳ�������
		} else if (buffer == -2) {
			GM.messageDialog(this, mm.getMessage("dialogoptions.invalidxmx"));
			return false;
		}
		return true;
	}

	/**
	 * ȡ����ť�¼�
	 * 
	 * @param e
	 */
	void jBCancel_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		m_option = JOptionPane.CANCEL_OPTION;
		dispose();
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 * 
	 * @param e
	 */
	void jBOK_actionPerformed(ActionEvent e) {
		try {
			try {
				if (!panelEnv.checkValid())
					return;
			} catch (Exception ex) {
				if (!isUnit)
					tabMain.setSelectedIndex(TAB_ENV);
				GM.messageDialog(DialogOptions.this, ex.getMessage());
			}
			if (save()) {
				GM.setWindowDimension(this);
				m_option = JOptionPane.OK_OPTION;
				dispose();
			}
		} catch (Throwable t) {
			GM.showException(this, t);
		}
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
	 * ȡˮƽ�������ֵ
	 * 
	 * @return
	 */
	public static Vector<Byte> getHAlignCodes() {
		Vector<Byte> hAligns = new Vector<Byte>();
		hAligns.add(new Byte(IStyle.HALIGN_LEFT));
		hAligns.add(new Byte(IStyle.HALIGN_CENTER));
		hAligns.add(new Byte(IStyle.HALIGN_RIGHT));
		return hAligns;
	}

	/**
	 * ȡˮƽ������ʾֵ
	 * 
	 * @return
	 */
	public static Vector<String> getHAlignDisps() {
		MessageManager mm = IdeCommonMessage.get();

		Vector<String> hAligns = new Vector<String>();
		hAligns.add(mm.getMessage("dialogoptions.hleft")); // �����
		hAligns.add(mm.getMessage("dialogoptions.hcenter")); // �ж���
		hAligns.add(mm.getMessage("dialogoptions.hright")); // �Ҷ���
		return hAligns;
	}

	/**
	 * ȡ��ֱ�������ֵ
	 * 
	 * @return
	 */
	public static Vector<Byte> getVAlignCodes() {
		Vector<Byte> vAligns = new Vector<Byte>();
		vAligns.add(new Byte(IStyle.VALIGN_TOP));
		vAligns.add(new Byte(IStyle.VALIGN_MIDDLE));
		vAligns.add(new Byte(IStyle.VALIGN_BOTTOM));
		return vAligns;
	}

	/**
	 * ȡ��ֱ������ʾֵ
	 * 
	 * @return
	 */
	public static Vector<String> getVAlignDisps() {
		MessageManager mm = IdeCommonMessage.get();
		Vector<String> vAligns = new Vector<String>();
		vAligns.add(mm.getMessage("dialogoptions.vtop")); // ����
		vAligns.add(mm.getMessage("dialogoptions.vcenter")); // ����
		vAligns.add(mm.getMessage("dialogoptions.vbottom")); // ����
		return vAligns;
	}

	/**
	 * ��ǰ�������ظ�����ֵ��һ�£����Զ�ˮƽ����ֱ������һ�¼��ݴ�����һ��ʱ��Ϳ���ȥ������ˡ�
	 */
	private Byte compatibleHalign(Byte value) {
		switch (value.byteValue()) {
		case (byte) 0xD0:
			return new Byte(IStyle.HALIGN_LEFT);
		case (byte) 0xD1:
			return new Byte(IStyle.HALIGN_CENTER);
		case (byte) 0xD2:
			return new Byte(IStyle.HALIGN_RIGHT);
		}
		return value;
	}

	/**
	 * ��ΪIStyle���˳���ֵ�����ݴ���һ�£�������޷�����
	 */
	private Byte compatibleValign(Byte value) {
		switch (value.byteValue()) {
		case (byte) 0xE0:
		case (byte) 0:
			return new Byte(IStyle.VALIGN_TOP);
		case (byte) 0xE1:
		case (byte) 1:
			return new Byte(IStyle.VALIGN_MIDDLE);
		case (byte) 2:
		case (byte) 0xE2:
			return new Byte(IStyle.VALIGN_BOTTOM);
		}
		return value;
	}
}

class DialogOptions_jBCancel_actionAdapter implements
		java.awt.event.ActionListener {
	DialogOptions adaptee;

	DialogOptions_jBCancel_actionAdapter(DialogOptions adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBCancel_actionPerformed(e);
	}
}

class DialogOptions_jBOK_actionAdapter implements java.awt.event.ActionListener {
	DialogOptions adaptee;

	DialogOptions_jBOK_actionAdapter(DialogOptions adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOK_actionPerformed(e);
	}
}

class DialogOptions_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogOptions adaptee;

	DialogOptions_this_windowAdapter(DialogOptions adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}