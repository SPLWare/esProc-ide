package com.scudata.ide.common.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.scudata.cellset.datamodel.PgmNormalCell;
import com.scudata.ide.common.EditListener;

/**
 * ֻ�����ı���
 *
 */
public class JTextFieldReadOnly extends JTextField {
	private static final long serialVersionUID = 1L;

	/**
	 * ���캯��
	 */
	public JTextFieldReadOnly() {
		this(0);
	}

	/**
	 * ���캯��
	 * 
	 * @param columns
	 *            ����
	 */
	public JTextFieldReadOnly(int columns) {
		this(new String(), columns);
	}

	/**
	 * ���캯��
	 * 
	 * @param s
	 *            �ַ���
	 */
	public JTextFieldReadOnly(String s) {
		this(s, 0);
	}

	/**
	 * ���캯��
	 * 
	 * @param s
	 *            �ַ���
	 * @param columns
	 *            ����
	 */
	public JTextFieldReadOnly(String s, int columns) {
		this(s, columns, null);
	}

	/**
	 * ���캯��
	 * 
	 * @param s
	 *            �ַ���
	 * @param columns
	 *            ����
	 * @param el
	 *            ����������Ϊnullʱ���Ա༭
	 */
	public JTextFieldReadOnly(String s, int columns, final EditListener el) {
		super(s, columns);
		setEditable(el != null);
		if (el != null) {
			KeyListener kl = new KeyListener() {
				public void keyTyped(KeyEvent e) {
				}

				public void keyPressed(KeyEvent e) {
				}

				public void keyReleased(KeyEvent e) {
					if (e != null) {
						Object src = e.getSource();
						if (src instanceof JTextField) {
							// ����editable��TextField��Ȼ�ܽ��յ������¼�
							JTextField txt = (JTextField) src;
							if (!txt.isEditable())
								return;
							if (e.isControlDown() || e.isAltDown()
									|| e.isShiftDown()) {
								return;
							}
							if (e.isActionKey()) {
								return;
							}
							if (e.getKeyCode() < 32) {
								return;
							}

							String newTxt = txt.getText();
							Object newVal = PgmNormalCell
									.parseConstValue(newTxt);
							el.editChanged(newVal);
						}
					}
				}
			};
			this.addKeyListener(kl);
		}

		addFocusListener(new FocusListener() {
			int caretPosition = 0;

			public void focusGained(FocusEvent e) {
				if (!getCaret().isVisible()) {
					getCaret().setVisible(true);
					setCaretPosition(caretPosition);
				}
			}

			public void focusLost(FocusEvent e) {
				if (getCaretPosition() > 0)
					caretPosition = getCaretPosition();
			}
		});

	}

}
