package com.scudata.ide.spl.base;

import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;

/**
 * ��Դ�����
 *
 */
public class FileTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	/** ���ڵ����� */
	public static final byte TYPE_ROOT = 0;
	/** ������Դ���� */
	public static final byte TYPE_LOCAL = 1;

	/** δѡ��״̬ */
	public static final byte NOT_SELECTED = 0;
	/** ѡ��״̬ */
	public static final byte SELECTED = 1;
	/** ����̬ */
	public static final byte DONT_CARE = 2;

	/**
	 * ѡ��״̬
	 */
	private transient byte selectedState = SELECTED;

	/**
	 * �Ƿ�ѡ����
	 */
	private boolean isCheckNode = false;
	/**
	 * �Ƿ��Ѿ��������ӽ��
	 */
	private boolean isLoaded = false;

	/**
	 * �������
	 */
	protected byte type = TYPE_LOCAL;
	/**
	 * ������
	 */
	private String title = null;
	/**
	 * �Ƿ�Ŀ¼��true��Ŀ¼��false���ļ���
	 */
	protected boolean isDir = false;
	/**
	 * ��������
	 */
	private String filter = null;
	/**
	 * �ӽ���б�
	 */
	private ArrayList<FileTreeNode> childBuffer = null;
	/**
	 * �Ƿ�ƥ����
	 */
	private boolean isMatched = false;
	/**
	 * �Ƿ�չ����
	 */
	private boolean isExpanded = false;

	/**
	 * ���캯��
	 * 
	 * @param data
	 * @param type
	 */
	public FileTreeNode(Object data, byte type) {
		this.setUserObject(data);
		this.type = type;
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param isDir
	 */
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	/**
	 * ȡĿ¼
	 * 
	 * @return
	 */
	public boolean isDir() {
		return isDir;
	}

	/**
	 * �����Ƿ�ƥ��
	 * 
	 * @param isMatched
	 */
	public void setMatched(boolean isMatched) {
		this.isMatched = isMatched;
	}

	/**
	 * ȡ�Ƿ�ƥ��
	 * 
	 * @return
	 */
	public boolean isMatched() {
		return isMatched;
	}

	/**
	 * ȡ��ʾ��ͼ��
	 * 
	 * @return
	 */
	public ImageIcon getDispIcon() {
		String imgPath = GC.IMAGES_PATH;
		if (type == TYPE_ROOT) {
			imgPath += "tree0.gif";
		} else if (this.getLevel() == 1) { // ������Դ�ͷ�������Դ��Ŀ¼��ͼ��
			imgPath += "treelocal.gif";
		} else {
			if (isDir)
				imgPath += "treefolder.gif";
			else {
				imgPath += "file_spl.png";
			}
		}
		ImageIcon img = GM.getImageIcon(imgPath);
		return img;
	}

	/**
	 * �����Ƿ�������ӽ��
	 * 
	 * @param isLoaded
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * ȡ�Ƿ�������ӽ��
	 * 
	 * @return
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * ȡ�������
	 * 
	 * @return
	 */
	public byte getType() {
		return type;
	}

	/**
	 * ���ý������
	 * 
	 * @param type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * ȡ�������
	 * 
	 * @return
	 */
	public String getName() {
		return toString();
	}

	/**
	 * ȡȫ·��
	 * 
	 * @return
	 */
	public String getFullPath() {
		String path = getName();
		FileTreeNode pNode = (FileTreeNode) getParent();
		while (pNode != null) {
			if (pNode.getName().equals(FileTree.ROOT_TITLE))
				break;
			path = pNode.getName() + File.separator + path;
			pNode = (FileTreeNode) pNode.getParent();
		}
		return path;
	}

	/**
	 * ȡ����
	 * 
	 * @return
	 */
	public String getTitle() {
		return title == null ? getName() : title;
	}

	/**
	 * ���ñ���
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * ѡ��״̬
	 * 
	 * @return NOT_SELECTED,SELECTED,DONT_CARE
	 */
	public byte getSelectedState() {
		return selectedState;
	}

	/**
	 * ����ѡ��״̬
	 * 
	 * @param selectedState
	 *            NOT_SELECTED,SELECTED,DONT_CARE
	 */
	public void setSelectedState(byte selectedState) {
		this.selectedState = selectedState;
	}

	/**
	 * ���ù�������
	 * 
	 * @param filter
	 *            ��������
	 * 
	 */
	public void setFilter(String filter) {
		this.filter = filter.toLowerCase();
		filter();
	}

	/**
	 * ����
	 */
	private void filter() {
		if (childBuffer == null) {
			childBuffer = new ArrayList<FileTreeNode>();
			for (int i = 0; i < getChildCount(); i++) {
				childBuffer.add((FileTreeNode) getChildAt(i));
			}
		}
		removeAllChildren();
		for (int c = 0; c < childBuffer.size(); c++) {
			FileTreeNode childNode = childBuffer.get(c);
			String lowerTitle = childNode.getTitle().toLowerCase();
			if (lowerTitle.indexOf(filter) >= 0) {
				add(childNode);
			}
		}
	}

	/**
	 * ȡ��������
	 * 
	 * @return ��������
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * �Ƿ�ѡ����
	 * 
	 * @return
	 */
	public boolean isCheckNode() {
		return isCheckNode;
	}

	/**
	 * ���ø�ѡ����
	 * 
	 * @param isCheckNode
	 */
	public void setCheckNode(boolean isCheckNode) {
		this.isCheckNode = isCheckNode;
	}

	/**
	 * ����Ƿ�չ��
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * ���ý���Ƿ�չ��
	 * 
	 * @param isExpanded
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	/**
	 * ��¡
	 * 
	 * @return
	 */
	public FileTreeNode deepClone() {
		FileTreeNode newNode = new FileTreeNode(getUserObject(), type);
		newNode.setTitle(title);
		newNode.setMatched(isMatched);
		newNode.setSelectedState(selectedState);
		newNode.setCheckNode(isCheckNode);
		return newNode;
	}

	/**
	 * ת��Ϊ�ַ���
	 */
	public String toString() {
		if (title != null)
			return title;
		Object data = this.getUserObject();
		if (type == TYPE_LOCAL) {
			return data == null ? null : data.toString();
		}
		return null;
	}

}
