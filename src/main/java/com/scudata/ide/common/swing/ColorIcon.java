package com.scudata.ide.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * ��ɫͼ��
 *
 */
public class ColorIcon implements Icon {
	/**
	 * ��ɫ
	 */
	private Object color;
	/**
	 * ��͸�
	 */
	private final int w = 37, h = 15;

	/**
	 * ���캯��
	 */
	public ColorIcon() {
		this(Color.gray, 0, 0);
	}

	/**
	 * ���캯��
	 * 
	 * @param color
	 *            ��ɫ
	 * @param w
	 *            ��
	 * @param h
	 *            ��
	 */
	public ColorIcon(Color color, int w, int h) {
		this.color = color;
	}

	/**
	 * ���͸��ɫ
	 * 
	 * @param g
	 * @param w
	 * @param h
	 * @param L
	 */
	public static void fillTransparent(Graphics g, int w, int h, int L) {
		g.setColor(Color.gray);
		g.fillRect(1, 1, w - 2, h - 2);

		g.setColor(Color.white);
		int x = 1, y = 1;
		int r = 0;
		while (y < h) {
			if (r % 2 == 0) {
				x = 1;
			} else {
				x = 1 + L;
			}
			while (x < w) {
				g.fillRect(x, y, L, L);
				x += L * 2;
			}
			r++;
			y += L;
		}
	}

	/**
	 * ��ͼ��
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (color instanceof String) {
			g.setColor(Color.white);
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(Color.black);
			g.drawString((String) color, 2, 14);
		} else {
			g.translate(x, y);
			Color cc = null;// ��colorΪnullʱ����ʾ͸��ɫ
			if (color instanceof Color) {
				cc = (Color) color;
			} else if (color instanceof Integer) {
				cc = new Color(((Integer) color).intValue(), true);
			}

			if (cc == null || cc.getAlpha() == 0) {
				fillTransparent(g, w, h, 4);
			} else {
				g.setColor(cc);
				g.fillRect(1, 1, w - 2, h - 2);
			}

			g.setColor(Color.black);
			g.drawRect(0, 0, w - 1, h - 1);
			g.translate(-x, -y);
		}
	}

	/**
	 * ȡ��ɫ
	 * 
	 * @return
	 */
	public Color getColor() {
		if (color instanceof Color) {
			return (Color) color;
		}
		return null;
	}

	/**
	 * ������ɫ
	 * 
	 * @param color
	 */
	public void setColor(Object color) {
		this.color = color;
	}

	/**
	 * ȡ��
	 */
	public int getIconWidth() {
		return w;
	}

	/**
	 * ȡ��
	 */
	public int getIconHeight() {
		return h;
	}
}
