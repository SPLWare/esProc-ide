package com.scudata.ide.common.swing;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;

import com.scudata.util.Variant;

/**
 * JTable��Ԫ����ı��༭��
 *
 */
public class JTextAreaEditor extends DefaultCellEditor implements MouseListener {
	private static final long serialVersionUID = 1L;

	/** ��ͨ�ı��� */
	public static final int TYPE_TEXT_SIMPLE = 1;
	/** �����ı��� */
	public static final int TYPE_TEXT_WRAP = 2;
	/** ��Ȼ���༭�� */
	public static final int TYPE_UNSIGNED_INTEGER = 3;
	/** �����༭�� */
	public static final int TYPE_SIGNED_INTEGER = 4;
	/** �޷��Ÿ������༭�� */
	public static final int TYPE_UNSIGNED_DOUBLE = 5;
	/** �������༭�� */
	public static final int TYPE_SIGNED_DOUBLE = 6;
	/** ֻ���ı��� */
	public static final int TYPE_TEXT_READONLY = 7;

	/**
	 * �༭����
	 */
	private int editorType;
	/**
	 * �������ؼ�
	 */
	private JScrollPane jsp;
	/**
	 * ���ؼ�
	 */
	private JTableExListener parent;

	/**
	 * ��ͨ�ı���ؼ�
	 */
	private JTextField textSimple = null;

	/**
	 * �����ı���ؼ�
	 */
	private JTextPane textWrap = null;

	/**
	 * Spinner�ؼ����������ֵ��Ŀǰû����ȡ��ʧȥ���㣬Ȼ����ܵ�ǰ�༭ֵ������JTextField 2020��12��30��
	 */
	/**
	 * ��Ȼ���༭��
	 */
	private JTextField uInteger = null;
	/**
	 * �����༭��
	 */
	private JTextField sInteger = null;
	/**
	 * �������༭��
	 */
	private JTextField sNumber = null;
	/**
	 * �༭ֵ
	 */
	private Object oldValue = null;

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            ���ؼ�
	 */
	public JTextAreaEditor(JTableExListener parent) {
		this(parent, TYPE_TEXT_SIMPLE);
	}

	/**
	 * ���캯��
	 * 
	 * @param parent
	 *            ���ؼ�
	 * @param editorType
	 *            �༭������
	 */
	public JTextAreaEditor(final JTableExListener parent, int editorType) {
		super(new JTextField(""));
		this.editorType = editorType;
		this.parent = parent;

		KeyAdapter kl = new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e != null) {
					Object src = e.getSource();
					if (src instanceof JTextField) {
						// ����editable��TextField��Ȼ�ܽ��յ������¼�
						JTextField txt = (JTextField) src;
						if (!txt.isEditable())
							return;
					}
					parent.stateChanged(new ChangeEvent(src));
				}
			}
		};

		switch (editorType) {
		case TYPE_TEXT_WRAP:
			textWrap = new JTextPane();
			textWrap.addMouseListener(this);
			textWrap.addKeyListener(kl);
			textWrap.setBorder(BorderFactory.createEmptyBorder());
			jsp = new JScrollPane(textWrap);
			break;
		case TYPE_SIGNED_INTEGER:
			sInteger = new JTextField();
			sInteger.addKeyListener(kl);
			sInteger.addMouseListener(this);
			sInteger.setBorder(BorderFactory.createEmptyBorder());
			break;
		case TYPE_UNSIGNED_INTEGER:
			uInteger = new JTextField();
			uInteger.addMouseListener(this);
			uInteger.addKeyListener(kl);
			uInteger.setBorder(BorderFactory.createEmptyBorder());
			break;
		case TYPE_UNSIGNED_DOUBLE:
			textSimple = new JTextField("");
			textSimple.addKeyListener(kl);
			textSimple.setBorder(BorderFactory.createEmptyBorder());
			break;
		case TYPE_SIGNED_DOUBLE:
			sNumber = new JTextField();
			sNumber.addMouseListener(this);
			sNumber.addKeyListener(kl);
			sNumber.setBorder(BorderFactory.createEmptyBorder());
			break;
		case TYPE_TEXT_READONLY:
			textSimple = new JTextFieldReadOnly("");
			textSimple.addMouseListener(this);
			textSimple.setBorder(BorderFactory.createEmptyBorder());
			break;
		default:
			textSimple = new JTextField("");
			textSimple.addKeyListener(kl);
			textSimple.addMouseListener(this);
			textSimple.setBorder(BorderFactory.createEmptyBorder());
			break;
		}
		this.setClickCountToStart(1);
	}

	/**
	 * ����ֵ
	 * 
	 * @param value
	 *            ֵ
	 * @return
	 */
	public Component setValue(Object value) {
		Component o;
		oldValue = value;

		switch (editorType) {
		case TYPE_TEXT_WRAP:
			if (value == null) {
				textWrap.setText("");
			} else {
				textWrap.setText(value.toString());
			}
			o = jsp;
			break;
		case TYPE_SIGNED_INTEGER:
			if (value == null || !(value instanceof Integer)) {
				sInteger.setText("0");
			} else {
				sInteger.setText(value.toString());
			}
			o = sInteger;
			break;
		case TYPE_UNSIGNED_INTEGER:
			if (value == null || !(value instanceof Integer)) {
				uInteger.setText("0");
			} else {
				uInteger.setText(value.toString());
			}
			o = uInteger;
			break;
		case TYPE_UNSIGNED_DOUBLE:
			if (value == null) {
				textSimple.setText("");
			} else {
				textSimple.setText(value.toString());
			}
			o = textSimple;
			break;
		case TYPE_SIGNED_DOUBLE:
			if (value == null) {
				sNumber.setText("0");
			} else {
				sNumber.setText(value.toString());
			}
			o = sNumber;
			break;
		default:
			if (value == null) {
				textSimple.setText("");
			} else {
				textSimple.setText(value.toString());
			}
			o = textSimple;
			break;
		}
		return o;
	}

	/**
	 * ȡ�༭ֵ
	 */
	public Object getCellEditorValue() {
		Object value = null;
		String tmp;
		switch (editorType) {
		case TYPE_TEXT_WRAP:
			value = textWrap.getText();
			break;
		case TYPE_SIGNED_INTEGER:
			tmp = sInteger.getText();
			value = Variant.parse(tmp);
			if (!(value instanceof Integer)) {
				value = oldValue;
			}
			break;
		case TYPE_UNSIGNED_INTEGER:
			tmp = uInteger.getText();
			value = Variant.parse(tmp);
			if (!(value instanceof Integer)) {
				value = oldValue;
			}
			break;
		case TYPE_UNSIGNED_DOUBLE:
			value = textSimple.getText();
			break;
		case TYPE_SIGNED_DOUBLE:
			tmp = sNumber.getText();
			value = Variant.parse(tmp);
			if (!(value instanceof Number)) {
				value = oldValue;
			}

			break;
		default:
			value = textSimple.getText();
			break;
		}
		return value;
	}

	/**
	 * ȡ��Ԫ��༭�ؼ�
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		return setValue(value);
	}

	/**
	 * ������¼�
	 */
	public void mouseClicked(MouseEvent e) {
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
		if (parent != null) {
			parent.fireClicked(absoluteX, absoluteY, e);
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void setArrange(int min, int max, int step) {
	}

	public void setArrange(double min, double max, double step) {
	}

}
