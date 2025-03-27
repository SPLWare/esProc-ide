package com.scudata.ide.common.control;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * ��ʾͼ�����������ʾ��
 *
 */
public class IconTreeRender extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * ���캯��
	 */
	public IconTreeRender() {
	}

	/**
	 * ʵ�ֺ�������DefaultTreeCellRenderer�Ļ����ϼ���ͼ��
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		IconTreeNode node = (IconTreeNode) value;
		setIcon(node.getDispIcon());
		return this;
	}
}
