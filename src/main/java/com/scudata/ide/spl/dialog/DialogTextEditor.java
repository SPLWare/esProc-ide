package com.scudata.ide.spl.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.ConfigOptions;
import com.scudata.ide.common.GM;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.control.SplControl;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * �ı��༭�Ի���
 *
 */
public class DialogTextEditor extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * ȷ�ϰ�ť
	 */
	private JButton okButton = new JButton();
	/**
	 * ȡ����ť
	 */
	private JButton cancelButton = new JButton();

	/**
	 * �ı��༭�ؼ�
	 */
	protected RSyntaxTextArea textEditor = new RSyntaxTextArea() {
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
	 * �������
	 */
	protected RTextScrollPane spEditor = new RTextScrollPane(textEditor);
	/**
	 * ��������Դ������
	 */
	private MessageManager mm = IdeSplMessage.get();
	/**
	 * �Զ����и�ѡ��
	 */
	private JCheckBox jCBLineWrap = new JCheckBox(
			mm.getMessage("dialogtexteditor.linewrap"));

	/**
	 * �˳�ѡ��
	 */
	private int option = JOptionPane.CLOSED_OPTION;

	/**
	 * ���캯��
	 */
	public DialogTextEditor(JFrame parent) {
		this(parent, true);
	}

	/**
	 * ���캯��
	 */
	public DialogTextEditor(JDialog parent) {
		this(parent, true);
	}

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            �����
	 * @param isEditable
	 *            �Ƿ���Ա༭
	 */
	public DialogTextEditor(JFrame parent, boolean isEditable) {
		super(parent, "", true);
		rqInit(isEditable);
	}

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            �����
	 * @param isEditable
	 *            �Ƿ���Ա༭
	 */
	public DialogTextEditor(JDialog parent, boolean isEditable) {
		super(parent, "", true);
		rqInit(isEditable);
	}

	private void rqInit(boolean isEditable) {
		init();
		GM.setWindowToolSize(this);
		GM.setDialogDefaultButton(this, okButton, cancelButton);
		this.setResizable(true);
		jCBLineWrap.setSelected(ConfigOptions.bTextEditorLineWrap
				.booleanValue());
		textEditor.setLineWrap(jCBLineWrap.isSelected());
		if (!isEditable) {
			textEditor.setEditable(false);
			okButton.setVisible(false);
			setTitle(mm.getMessage("dialogtexteditor.title1"));
		} else {
			setTitle(mm.getMessage("dialogtexteditor.title"));
		}
	}

	/**
	 * ȡ�˳�ѡ��
	 * 
	 * @return
	 */
	public int getOption() {
		return option;
	}

	// ̫�����ı�ͨ�������ݾͲ������༭�ˣ�Ҫ��Ȼ̫����
	private static final int STYLE_MAX_LENGTH = 100000;

	/**
	 * �����ı�
	 * 
	 * @param text
	 */
	public void setText(String text) {
		textEditor.setText(text);
		if (text == null || text.length() <= STYLE_MAX_LENGTH) {
			textEditor.setCodeFoldingEnabled(true);
			textEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		}
	}

	/**
	 * ȡ�ı�
	 * 
	 * @return
	 */
	public String getText() {
		return textEditor.getText();
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		this.getContentPane().setLayout(new BorderLayout());
		okButton.addActionListener(this);
		okButton.setMnemonic('O');
		cancelButton.addActionListener(this);
		cancelButton.setMnemonic('C');
		okButton.setText(mm.getMessage("button.ok"));
		cancelButton.setText(mm.getMessage("button.cancel"));
		JPanel panelSouth = new JPanel(new GridBagLayout());
		panelSouth.add(jCBLineWrap, GM.getGBC(0, 0));
		panelSouth.add(new JLabel(""), GM.getGBC(0, 1, true));
		panelSouth.add(okButton, GM.getGBC(0, 2, false, false, 0));
		panelSouth.add(cancelButton, GM.getGBC(0, 3));
		this.getContentPane().add(panelSouth, BorderLayout.SOUTH);
		JPanel panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(spEditor, BorderLayout.CENTER);
		this.getContentPane().add(panelCenter, BorderLayout.CENTER);
		textEditor.setEditable(true);
		try {
			if (GVSpl.splEditor != null) {
				SplControl control = GVSpl.splEditor.getComponent();
				if (control != null) {
					Font font = GM.getScaleFont(control.scale);
					textEditor.setFont(new Font("Dialog", font.getStyle(), font
							.getSize()));
				}
			}
		} catch (Exception e) {
		}

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		jCBLineWrap.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textEditor.setLineWrap(jCBLineWrap.isSelected());
			}

		});
	}

	/**
	 * �رմ���
	 */
	private void closeWindow() {
		ConfigOptions.bTextEditorLineWrap = jCBLineWrap.isSelected();
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * �ؼ��¼�
	 */
	public void actionPerformed(ActionEvent e) {
		Object c = e.getSource();
		if (c == okButton) {
			option = JOptionPane.OK_OPTION;
			closeWindow();
		} else if (c == cancelButton) {
			closeWindow();
		}
	}
}
