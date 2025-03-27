package com.scudata.ide.common.control;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;

/**
 * ��ʾͼ��������ı༭��
 *
 */
public class IconTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	/** �ļ��� */
	public static final byte TYPE_FOLDER = 0;
	/** �� */
	public static final byte TYPE_TABLE = 1;
	/** �ֶ� */
	public static final byte TYPE_COLUMN = 2;

	/**
	 * ������͡�TYPE_FOLDER,TYPE_KEYVAL,TYPE_TABLE,TYPE_COLUMN
	 */
	private byte type = TYPE_FOLDER;

	/**
	 * �������
	 */
	private String name;

	/**
	 * ���캯��
	 * 
	 * @param name
	 *            �������
	 */
	public IconTreeNode(String name) {
		this(name, TYPE_FOLDER);
	}

	/**
	 * ���캯��
	 * 
	 * @param name
	 *            �������
	 * @param type
	 *            ������͡�TYPE_FOLDER,TYPE_KEYVAL,TYPE_TABLE,TYPE_COLUMN
	 */
	public IconTreeNode(String name, byte type) {
		this.type = type;
		this.name = name;
	}

	/**
	 * ȡ�������
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���ý������
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ȡ������͡�TYPE_FOLDER,TYPE_KEYVAL,TYPE_TABLE,TYPE_COLUMN
	 * 
	 * @return
	 */
	public byte getType() {
		return type;
	}

	/**
	 * ���ý�����͡�TYPE_FOLDER,TYPE_KEYVAL,TYPE_TABLE,TYPE_COLUMN
	 * 
	 * @param type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * ת��Ϊ�ַ���
	 */
	public String toString() {
		return name;
	}

	/**
	 * ��¡
	 * 
	 * @return
	 */
	public IconTreeNode deepClone() {
		return new IconTreeNode(name, type);
	}

	/**
	 * ��ȡ��ʾ��ͼ��
	 * 
	 * @return
	 */
	public Icon getDispIcon() {
		String url = GC.IMAGES_PATH;
		if (this.isRoot()) {
			url += "tree0.gif";
		} else if (type == TYPE_FOLDER) {
			url += "treefolder.gif";
		} else if (type == TYPE_TABLE) {
			url += "treetable.gif";
		} else if (type == TYPE_COLUMN) {
			url += "treecolumn.gif";
		}
		return GM.getImageIcon(url);
	}
}
