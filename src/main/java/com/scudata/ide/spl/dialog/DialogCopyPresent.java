package com.scudata.ide.spl.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.ConfigOptions;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.dialog.RQDialog;
import com.scudata.ide.common.swing.JComboBoxEx;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ���ƿɳ��ִ���Ի���
 *
 */
public class DialogCopyPresent extends RQDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * ���캯��
	 */
	public DialogCopyPresent() {
		super("Copy code for presentation", 600, 500);
		try {
			init();
			GM.centerWindow(this);
		} catch (Exception e) {
			GM.showException(this, e);
		}
	}

	/**
	 * ����ѡ��
	 * 
	 * @param showException
	 *            �Ƿ����쳣
	 * @return
	 */
	private boolean saveOption(boolean showException) {
		byte type = ((Number) jCBType.x_getSelectedItem()).byteValue();
		if (type == ConfigOptions.COPY_TEXT) {
			String sep = getSep();
			if (sep == null || "".equals(sep)) {
				if (showException)
					// �붨���зָ�����
					GM.messageDialog(this,
							mm.getMessage("dialogcopypresent.emptycolsep"));
				return false;
			}
			ConfigOptions.sCopyPresentSep = sep;
		}
		ConfigOptions.iCopyPresentType = type;
		ConfigOptions.bCopyPresentHeader = jCBHeader.isSelected();
		return true;
	}

	/**
	 * ȷ�ϰ�ť�¼�
	 */
	protected boolean okAction(ActionEvent e) {
		if (!saveOption(true)) {
			return false;
		}

		return true;
	}

	/**
	 * �رնԻ���
	 */
	protected void closeDialog(int option) {
		super.closeDialog(option);
		GM.setWindowDimension(this);
		ConfigOptions.bTextEditorLineWrap = jCBLineWrap.isSelected();
	}

	/**
	 * ȡ�ָ���
	 * 
	 * @return
	 */
	private String getSep() {
		Object disp = jCBSep.getEditor().getItem();
		if (disp != null) {
			int index = disps.indexOf(disp);
			if (index > -1) {
				return (String) codes.get(index);
			} else {
				return disp.toString();
			}
		}
		return "";
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.add(jLType, GM.getGBC(0, 0));
		panelCenter.add(jCBType, GM.getGBC(0, 1));

		GridBagConstraints gbc = GM.getGBC(1, 0);
		gbc.gridwidth = 2;
		panelCenter.add(jCBHeader, gbc);

		panelCenter.add(jLSep, GM.getGBC(2, 0));
		panelCenter.add(jCBSep, GM.getGBC(2, 1));

		panelCenter.add(jLPreview, GM.getGBC(3, 0));
		gbc = GM.getGBC(4, 0, true, true);
		gbc.gridwidth = 2;
		panelCenter.add(jSPPreview, gbc);

		gbc = GM.getGBC(5, 0);
		gbc.gridwidth = 2;
		panelCenter.add(jCBLineWrap, gbc);

		Vector<Object> codeData = new Vector<Object>();
		codeData.add(ConfigOptions.COPY_HTML);
		codeData.add(ConfigOptions.COPY_TEXT);
		Vector<String> dispData = new Vector<String>();
		dispData.add(LABEL_HTML);
		dispData.add(LABEL_TEXT);
		jCBType.x_setData(codeData, dispData);

		codes.add(TAB);
		codes.add(SPACE);
		codes.add(CSV);
		disps.add(LABEL_TAB);
		disps.add(LABEL_SPACE);
		disps.add(LABEL_CSV);
		jCBSep.x_setData(codes, disps);
		jCBSep.setEditable(true);

		jTAPreview.setEditable(false);
		jTAPreview.setCodeFoldingEnabled(true);
		jTAPreview.setFont(GC.font);
		// ��Ԫ����ʽ
		jTAPreview.setToolTipText(mm.getMessage("toolbarproperty.cellexp"));
		jTAPreview.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		jCBLineWrap.setSelected(ConfigOptions.bTextEditorLineWrap
				.booleanValue());
		jTAPreview.setLineWrap(jCBLineWrap.isSelected());

		jCBType.x_setSelectedCodeItem(ConfigOptions.iCopyPresentType);
		jCBHeader.setSelected(ConfigOptions.bCopyPresentHeader);
		jCBSep.setSelectedItem(ConfigOptions.sCopyPresentSep);
		// ���ƿɳ��ִ���
		setTitle(mm.getMessage("dialogcopypresent.title"));
		this.setResizable(true);
		jCBType.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				typeChanged();
				preview();
			}

		});
		jCBHeader.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				preview();
			}

		});
		Object editor = jCBSep.getEditor().getEditorComponent();
		if (editor instanceof JTextComponent) {
			final JTextComponent textSplitChar = (JTextComponent) editor;
			textSplitChar.setFocusTraversalKeys(
					KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					Collections.EMPTY_SET);
			textSplitChar.setFocusTraversalKeys(
					KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
					Collections.EMPTY_SET);
			textSplitChar.addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_TAB) {
						textSplitChar.setText(LABEL_TAB);
					}
				}

				public void keyPressed(KeyEvent e) {
				}
			});
		}
		jCBSep.getEditor().getEditorComponent()
				.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							e.consume();
							preview();
						}
					}
				});
		jCBSep.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				preview();
			}

		});
		jCBLineWrap.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				jTAPreview.setLineWrap(jCBLineWrap.isSelected());
			}

		});
		typeChanged();
		preview();
	}

	/**
	 * ��ʾ
	 */
	private void preview() {
		if (!saveOption(false)) {
			return;
		}
		String str = GVSpl.splEditor.getCopyPresentString();
		jTAPreview.setText(str);
	}

	/**
	 * ���ͷ����仯
	 */
	private void typeChanged() {
		boolean sepEnabled = ((Number) jCBType.x_getSelectedItem()).byteValue() == ConfigOptions.COPY_TEXT;
		jLSep.setEnabled(sepEnabled);
		jCBSep.setEnabled(sepEnabled);
	}

	/** ����ֵ */
	private Vector<Object> codes = new Vector<Object>();
	/** ��ʾֵ */
	private Vector<String> disps = new Vector<String>();
	/** ��������Դ������ */
	private MessageManager mm = IdeSplMessage.get();
	/**
	 * ��������
	 */
	private JLabel jLType = new JLabel(mm.getMessage("dialogcopypresent.type"));
	/** HTML */
	private final String LABEL_HTML = "HTML";
	/** �ı� */
	private final String LABEL_TEXT = mm.getMessage("dialogcopypresent.text");
	/** ���Ϳؼ� */
	private JComboBoxEx jCBType = new JComboBoxEx();
	/** �Ƿ������кſؼ� */
	private JCheckBox jCBHeader = new JCheckBox(
			mm.getMessage("dialogcopypresent.copyheader"));
	/** �зָ��� */
	private JLabel jLSep = new JLabel(mm.getMessage("dialogcopypresent.colsep"));

	/** ����ֵ */
	private final String TAB = "\t";
	private final String SPACE = "    ";
	private final String CSV = ",";
	/** ��ʾֵ */
	private final String LABEL_TAB = "TAB";
	private final String LABEL_SPACE = mm.getMessage("dialogcopypresent.space");
	private final String LABEL_CSV = ",";

	/**
	 * �ָ����ؼ�
	 */
	private JComboBoxEx jCBSep = new JComboBoxEx();

	/**
	 * Ԥ��
	 */
	private JLabel jLPreview = new JLabel(
			mm.getMessage("dialogcopypresent.preview"));
	/**
	 * Ԥ�����ı��ؼ�
	 */
	protected RSyntaxTextArea jTAPreview = new RSyntaxTextArea() {
		private static final long serialVersionUID = 1L;

		public Rectangle modelToView(int pos) throws BadLocationException {
			try {
				return super.modelToView(pos);
			} catch (Exception ex) {
			}
			return null;
		}
	};
	/**
	 * Ԥ���������
	 */
	protected RTextScrollPane jSPPreview = new RTextScrollPane(jTAPreview);
	/**
	 * �Ƿ��Զ�����
	 */
	private JCheckBox jCBLineWrap = new JCheckBox(
			mm.getMessage("dialogtexteditor.linewrap"));
}
