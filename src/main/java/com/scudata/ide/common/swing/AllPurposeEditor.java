package com.scudata.ide.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.scudata.ide.common.GM;

/**
 * JTable�ĵ�Ԫ��༭��
 *
 */
public class AllPurposeEditor extends DefaultCellEditor implements KeyListener,
		MouseListener, FocusListener {

	private static final long serialVersionUID = 1L;

	/**
	 * ���ؼ�
	 */
	private JTableEx parent;
	/**
	 * �ı���ؼ�
	 */
	private JTextField textField;
	/**
	 * ֵ
	 */
	private Object value;

	/**
	 * ���캯��
	 * 
	 * @param tf
	 * @param parent
	 */
	public AllPurposeEditor(JTextField tf, JTableEx parent) {
		super(tf);
		textField = tf;
		tf.addKeyListener(this);
		tf.addFocusListener(this);
		tf.addMouseListener(this);
		textField.setFont(parent.getFont());
		tf.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		this.parent = parent;
		this.setClickCountToStart(1);
	}

	/**
	 * ������¼�
	 */
	public void mouseClicked(final MouseEvent e) {
		JComponent editor = (JComponent) e.getSource();

		java.awt.Container p;
		java.awt.Container ct = editor.getTopLevelAncestor();
		int absoluteX = e.getX() + editor.getX(), absoluteY = e.getY()
				+ editor.getY();
		p = editor.getParent();
		while (p != ct) {
			absoluteX += p.getX();
			absoluteY += p.getY();
			p = p.getParent();
		}
		parent.fireClicked(absoluteX, absoluteY, e);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	/**
	 * focusGained
	 * 
	 * @param e
	 *            FocusEvent
	 */
	public void focusGained(FocusEvent e) {
		parent.focusGained(e);
	}

	/**
	 * focusLost
	 * 
	 * @param e
	 *            FocusEvent
	 */
	public void focusLost(FocusEvent e) {
	}

	/**
	 * keyPressed
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * keyReleased
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * keyTyped
	 * 
	 * @param e
	 *            KeyEvent
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		// ����༭�����ı�������ֵ����ʾ�ı�һ�£�����Ϊû�б༭�����ض�����
		String editorText = textField.getText();
		if (editorText.equals(GM.renderValueText(value))) {
			return value;
		}
		// �����û��༭�������ı�������Ϊ�鼰�����ı�����
		return editorText;
	}

	/**
	 * �Ƿ���Ա༭
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return textField.isEditable();
	}

	/**
	 * ��Ҫʵ�ַ��������ر༭�ؼ�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.value = value;
		textField.setText(GM.renderValueText(value));
		Color bc = table.getBackground();
		textField.setBackground(bc);
		return textField;
	}

}
