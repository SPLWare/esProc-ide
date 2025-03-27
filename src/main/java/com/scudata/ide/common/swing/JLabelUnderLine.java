package com.scudata.ide.common.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.scudata.cellset.IStyle;
import com.scudata.ide.common.control.ControlUtilsBase;
import com.scudata.ide.spl.control.ControlUtils;

/**
 * ֧���»��ߵı�ǩ�ؼ�
 *
 */
public class JLabelUnderLine extends JLabel {
	private static final long serialVersionUID = 1L;

	/**
	 * �»�����ɫ
	 */
	private Color underLineColor = Color.BLUE;

	/**
	 * ֵ
	 */
	Object value = null;
	/**
	 * ��ʾ��
	 */
	String dispText = null;

	/**
	 * ���캯��
	 */
	public JLabelUnderLine() {
		super("");
		setBackground(new JTextField().getBackground());
		setBorder(null);
	}

	/**
	 * ����ֵ
	 * 
	 * @param value
	 *            ֵ
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	public void setDispText(String dispText) {
		this.dispText = dispText;
	}

	/**
	 * ȡ�»�����ɫ
	 * 
	 * @return
	 */
	public Color getUnderLineColor() {
		return underLineColor;
	}

	/**
	 * �����»�����ɫ
	 * 
	 * @param pUnderLineColor
	 */
	public void setUnderLineColor(Color pUnderLineColor) {
		underLineColor = pUnderLineColor;
	}

	/**
	 * ����
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // ���Ƴ����ı����»��������
		int width = getWidth();
		if (width <= 0)
			return;
		ControlUtils.setGraphicsRenderingHints(g);
		// �����»���ʹ�õ�
		float underLineSize = 0.75f;
		((Graphics2D) g).setStroke(new BasicStroke(underLineSize));
		boolean underLine = value != null && value instanceof String;
		byte halign;
		switch (getHorizontalAlignment()) {
		case JLabel.LEFT:
			halign = IStyle.HALIGN_LEFT;
			break;
		case JLabel.CENTER:
			halign = IStyle.HALIGN_CENTER;
			break;
		default:
			halign = IStyle.HALIGN_RIGHT;
			break;
		}
		Font font = getFont();
		FontMetrics fm = getFontMetrics(font);
		int indent = fm.stringWidth(" ");
		ControlUtilsBase.drawText(g, dispText, 0, 0, getWidth(), getHeight(),
				underLine, halign, IStyle.VALIGN_MIDDLE, font, getForeground(),
				indent, false);
	}
}