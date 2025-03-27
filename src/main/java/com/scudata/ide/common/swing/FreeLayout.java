package com.scudata.ide.common.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * ���ɣ����꣩����
 *
 */
public class FreeLayout implements LayoutManager2, Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * ���
	 */
	private int width;
	/**
	 * �߶�
	 */
	private int height;
	/**
	 * ��������
	 */
	private Hashtable<Component, FreeConstraints> hashtable;
	/**
	 * ȱʡ����
	 */
	private static final FreeConstraints DEFAULT_CONSTRAINTS = new FreeConstraints();

	/**
	 * ���캯��
	 */
	public FreeLayout() {
		hashtable = new Hashtable<Component, FreeConstraints>();
	}

	/**
	 * ���캯��
	 * 
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public FreeLayout(int width, int height) {
		hashtable = new Hashtable<Component, FreeConstraints>();
		this.width = width;
		this.height = height;
	}

	/**
	 * ȡ���
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ���ÿ��
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ȡ�߶�
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ���ø߶�
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * ת�ַ���
	 */
	public String toString() {
		return "[width=" + width + ",height=" + height + "]";
	}

	/**
	 * ���Ӳ������
	 */
	public void addLayoutComponent(String s, Component component1) {
	}

	/**
	 * ɾ���������
	 */
	public void removeLayoutComponent(Component c) {
		hashtable.remove(c);
	}

	/**
	 * ƫ�ò��ֳߴ�
	 */
	public Dimension preferredLayoutSize(Container container) {
		return getLayoutSize(container, true);
	}

	/**
	 * ��С���ֳߴ�
	 */
	public Dimension minimumLayoutSize(Container container) {
		return getLayoutSize(container, false);
	}

	/**
	 * �ڷ�����
	 */
	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component c = container.getComponent(i);
			if (c.isVisible()) {
				Rectangle r = getComponentBounds(c, true);
				c.setBounds(insets.left + r.x, insets.top + r.y, r.width,
						r.height);
			}
		}

	}

	/**
	 * ���Ӳ������
	 */
	public void addLayoutComponent(Component c, Object constraints) {
		if (constraints instanceof FreeConstraints)
			hashtable.put(c, (FreeConstraints) constraints);
	}

	/**
	 * ��󲼾ֳߴ�
	 */
	public Dimension maximumLayoutSize(Container container) {
		return new Dimension(100000, 100000);
	}

	/**
	 * ȡ���򲼾ֶ���
	 */
	public float getLayoutAlignmentX(Container container) {
		return 0.5f;
	}

	/**
	 * ȡ���򲼾ֶ���
	 */
	public float getLayoutAlignmentY(Container container) {
		return 0.5f;
	}

	/**
	 * ���ò���
	 */
	public void invalidateLayout(Container container) {
	}

	/**
	 * ȡ����ĳߴ�
	 * 
	 * @param c
	 * @param doPreferred
	 * @return
	 */
	Rectangle getComponentBounds(Component c, boolean doPreferred) {
		FreeConstraints constraints = hashtable.get(c);
		if (constraints == null)
			constraints = DEFAULT_CONSTRAINTS;
		Rectangle r = new Rectangle(constraints.x, constraints.y,
				constraints.w, constraints.h);
		if (r.width <= 0 || r.height <= 0) {
			Dimension d = doPreferred ? c.getPreferredSize() : c
					.getMinimumSize();
			if (r.width <= 0)
				r.width = d.width;
			if (r.height <= 0)
				r.height = d.height;
		}
		return r;
	}

	/**
	 * ȡ���ֵĳߴ�
	 * 
	 * @param container
	 * @param doPreferred
	 * @return
	 */
	Dimension getLayoutSize(Container container, boolean doPreferred) {
		Dimension dim = new Dimension(1, 1);
		if (width <= 0 || height <= 0) {
			int count = container.getComponentCount();
			for (int i = 0; i < count; i++) {
				Component c = container.getComponent(i);
				if (c.isVisible()) {
					Rectangle r = getComponentBounds(c, doPreferred);
					dim.width = Math.max(dim.width, r.x + r.width);
					dim.height = Math.max(dim.height, r.y + r.height);
				}
			}

		}
		if (width > 0)
			dim.width = width;
		if (height > 0)
			dim.height = height;
		Insets insets = container.getInsets();
		dim.width += insets.left + insets.right;
		dim.height += insets.top + insets.bottom;
		return dim;
	}

}
