package com.scudata.ide.common.swing;

import java.io.Serializable;

/**
 * ����(����)��������
 *
 */
public class FreeConstraints implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * X���꣬Y���꣬����
	 */
	int x, y, w, h;

	/**
	 * ���캯��
	 */
	public FreeConstraints() {
		this(0, 0, 0, 0);
	}

	/**
	 * ���캯��
	 * 
	 * @param x
	 *            X����
	 * @param y
	 *            Y����
	 * @param w
	 *            ��
	 * @param h
	 *            ��
	 */
	public FreeConstraints(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * ȡX����
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * �� X����
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * ȡY����
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * ����Y����
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * ȡ���
	 * 
	 * @return
	 */
	public int getWidth() {
		return w;
	}

	/**
	 * ���ÿ��
	 * 
	 * @param w
	 */
	public void setWidth(int w) {
		this.w = w;
	}

	/**
	 * ȡ�߶�
	 * 
	 * @return
	 */
	public int getHeight() {
		return h;
	}

	/**
	 * ���ø߶�
	 * 
	 * @param h
	 */
	public void setHeight(int h) {
		this.h = h;
	}

	/**
	 * ��ϣֵ
	 */
	public int hashCode() {
		return Integer.parseInt("" + x + y + w + h);
	}

	/**
	 * �Ƚ�
	 */
	public boolean equals(Object o) {
		if (o instanceof FreeConstraints) {
			FreeConstraints other = (FreeConstraints) o;
			return other.x == x && other.y == y && other.w == w && other.h == h;
		} else {
			return false;
		}
	}

	/**
	 * ��¡
	 */
	public Object clone() {
		return new FreeConstraints(x, y, w, h);
	}

	/**
	 * תΪ�ַ���
	 */
	public String toString() {
		return "[" + x + "," + "y" + "," + w + "," + h + "]";
	}

}
