package com.scudata.ide.spl.base;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.scudata.app.common.AppUtil;
import com.scudata.app.config.ConfigUtil;
import com.scudata.common.Logger;
import com.scudata.common.MessageManager;
import com.scudata.common.StringUtils;
import com.scudata.ide.common.ConfigOptions;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ��Դ���ؼ�
 *
 */
public class FileTree extends JTree {
	private static final long serialVersionUID = 1L;

	public static MessageManager mm = IdeSplMessage.get();

	/**
	 * �����
	 */
	protected FileTreeNode root;
	/**
	 * ������Դ
	 */
	protected FileTreeNode localRoot;
	/**
	 * δ����Ӧ����Դ·��
	 */
	private static final String NO_MAIN_PATH = mm
			.getMessage("filetree.nomainpath");
	/**
	 * DEMOĿ¼������
	 */
	private static final String NO_DEMO_DIR = mm
			.getMessage("filetree.nodemodir");
	/**
	 * Ӧ����Դ
	 */
	public static final String ROOT_TITLE = mm.getMessage("filetree.roottitle");

	/**
	 * ���캯��
	 */
	public FileTree() {
		super();
		this.setMinimumSize(new Dimension(1, 1));
		this.root = new FileTreeNode("", FileTreeNode.TYPE_ROOT);
		this.localRoot = new FileTreeNode("", FileTreeNode.TYPE_LOCAL);
		this.root.setDir(true);
		this.root.setTitle(ROOT_TITLE);
		this.root.setExpanded(true);
		this.localRoot.setDir(true);
		if (ConfigOptions.bFileTreeDemo) {
			this.localRoot.setTitle(NO_DEMO_DIR);
		} else {
			this.localRoot.setTitle(NO_MAIN_PATH);
		}
		this.root.add(this.localRoot);

		setModel(new DefaultTreeModel(root));
		setCellRenderer(new FileTreeRender());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel();
		dtsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setSelectionModel(dtsm);
		addMouseListener(new mTree_mouseAdapter());
		this.addTreeWillExpandListener(new TreeWillExpandListener() {

			public void treeWillExpand(TreeExpansionEvent event)
					throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path == null)
					return;
				FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
				treeNodeWillExpand(node);
			}

			public void treeWillCollapse(TreeExpansionEvent event)
					throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path == null)
					return;
				FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
				if (node != null) {
					node.setExpanded(false);
				}
			}

		});
	}

	/**
	 * δչ��
	 */
	private final String NOT_EXPAND = "NOT_EXPAND";

	/**
	 * ����չ��״̬
	 * 
	 * @param dl �ļ�����λ��
	 */
	public void saveExpandState(int dl) {
		try {
			ConfigOptions.iConsoleLocation = new Integer(dl); // �����ļ���������̨���
			if (!this.isExpanded(0)) { // ���ڵ�ûչ��
				ConfigOptions.sFileTreeExpand = NOT_EXPAND;
				ConfigOptions.save(false, true);
				return;
			}
			StringBuffer buf = new StringBuffer();
			buf.append(localRoot.getName());
			Enumeration em = localRoot.depthFirstEnumeration();

			while (em.hasMoreElements()) {
				FileTreeNode node = (FileTreeNode) em.nextElement();
				if (node.isExpanded()) {
					buf.append(",");
					buf.append(node.getFullPath());
				}
			}
			ConfigOptions.sFileTreeExpand = buf.toString();
			ConfigOptions.save(false, true);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �Ƿ��ʼ��
	 */
	private boolean isInit = true;

	/**
	 * ˢ�±����ļ�
	 */
	public synchronized void refreshLocal() {
		localRoot.removeAllChildren();
		String home = System.getProperty("start.home");
		String mainPath = null;
		if (ConfigOptions.bFileTreeDemo) {
			File demoDir = new File(home, "demo");
			if (demoDir.exists()) {
				if (GC.LANGUAGE == GC.ASIAN_CHINESE
						|| GC.LANGUAGE == GC.ASIAN_CHINESE_TRADITIONAL) {
					demoDir = new File(demoDir, "zh");
				} else {
					demoDir = new File(demoDir, "en");
				}
				if (demoDir.exists())
					mainPath = demoDir.getAbsolutePath();
			}
			if (mainPath == null) {
				Logger.info(mm.getMessage("filetree.nodemodir"));
			}
		} else {
			mainPath = ConfigUtil.getPath(home, ConfigOptions.sMainPath);
		}
		if (StringUtils.isValidString(mainPath)) {
			if (!mainPath.equals(localRoot.getUserObject())) {
				localRoot.setDir(true);
				localRoot.setUserObject(mainPath);
				localRoot.setTitle(null);
			}
		} else {
			// if (StringUtils.isValidString(localRoot.getUserObject())) {
			localRoot.setDir(true);
			localRoot.setUserObject("");
			if (ConfigOptions.bFileTreeDemo) {
				localRoot.setTitle(NO_DEMO_DIR);
			} else {
				localRoot.setTitle(NO_MAIN_PATH);
			}
			nodeStructureChanged(localRoot);
			return;
			// }
		}
		localRoot.setExpanded(true);
		loadSubNode(localRoot);
		nodeStructureChanged(localRoot);
		if (isInit) { // �����ϴ�������չ�ṹ
			isInit = false;
			try {
				String sExpand = ConfigOptions.sFileTreeExpand;
				if (!StringUtils.isValidString(sExpand)) { // ��һ�δ�
					this.collapsePath(new TreePath(localRoot.getPath()));
					loadSubNode(localRoot);
					nodeStructureChanged(localRoot);
					return;
				}
				if (NOT_EXPAND.equals(sExpand)) { // ���ڵ�ûչ��
					this.collapsePath(new TreePath(localRoot.getPath()));
					localRoot.setExpanded(false);
					return;
				}
				String[] expands = sExpand.split(",");
				if (expands != null && expands.length > 0) {
					List<String> expandList = Arrays.asList(expands);
					int count = localRoot.getChildCount();
					for (int i = 0; i < count; i++) {
						expandTree((FileTreeNode) localRoot.getChildAt(i),
								expandList);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void treeNodeWillExpand(FileTreeNode node) {
		if (node != null && !node.isLoaded()) {
			// ���ݽڵ����ͣ�����ˢ�¼��ؽڵ�ķ���
			if (node.getType() == FileTreeNode.TYPE_LOCAL) {
				loadSubNode(node);
			} else if (node.getType() == FileTreeNode.TYPE_ROOT) {
				// ˢ�¸��ڵ�
				refreshLocal();
			}
			node.setExpanded(true);
			nodeStructureChanged(node);
		}
	}

	/**
	 * չ�������
	 * 
	 * @param pNode
	 * @param expandList
	 */
	private void expandTree(FileTreeNode pNode, List<String> expandList) {
		if (expandList.contains(pNode.getFullPath())) {
			loadSubNode(pNode);
			int count = pNode.getChildCount();
			for (int i = 0; i < count; i++) {
				expandTree((FileTreeNode) pNode.getChildAt(i), expandList);
			}
			pNode.setExpanded(true);
			nodeStructureChanged(pNode);
			this.expandPath(new TreePath(pNode.getPath()));
		}
	}

	/**
	 * �����ӽ��
	 * 
	 * @param pNode �����
	 */
	protected void loadSubNode(FileTreeNode pNode) {
		try {
			String pDir = (String) pNode.getUserObject();
			File dir = new File(pDir);
			if (!dir.isDirectory() || !dir.exists())
				return;
			pNode.removeAllChildren();
			File[] files = dir.listFiles();
			files = GM.sortFiles(files);
			if (files == null || files.length == 0)
				return;
			for (File f : files) {
				String fileName = f.getName();
				if (!StringUtils.isValidString(fileName)) {
					continue;
				}
				FileTreeNode node = new FileTreeNode(f.getAbsolutePath(),
						FileTreeNode.TYPE_LOCAL);
				node.setTitle(fileName);
				boolean isDir = f.isDirectory();
				if (isDir) {
					node.setDir(isDir);
					pNode.add(node);
					File[] subFiles = f.listFiles();
					subFiles = GM.sortFiles(subFiles);
					if (subFiles != null && subFiles.length > 0) {
						for (File subFile : subFiles) {
							String subName = subFile.getName();
							if (subFile.isDirectory()
									|| (subFile.isFile() && isValidFile(
											FileTreeNode.TYPE_LOCAL, subName))) {
								FileTreeNode subNode = new FileTreeNode(
										subFile.getAbsolutePath(),
										FileTreeNode.TYPE_LOCAL);
								subNode.setTitle(subName);
								subNode.setDir(subFile.isDirectory());
								node.add(subNode);
								break;
							}
						}
					}
				}
			}
			Set<String> existNames = new HashSet<String>();
			for (int i = 0, cc = pNode.getChildCount(); i < cc; i++) {
				FileTreeNode subNode = (FileTreeNode) pNode.getChildAt(i);
				existNames.add(subNode.getTitle());
			}
			for (File f : files) {
				String fileName = f.getName();
				if (!StringUtils.isValidString(fileName)) {
					continue;
				}
				if (existNames.contains(fileName)) { // �Ѿ����ع����ӽ��
					continue;
				}
				FileTreeNode node = new FileTreeNode(f.getAbsolutePath(),
						FileTreeNode.TYPE_LOCAL);
				node.setTitle(fileName);
				boolean isDir = f.isDirectory();
				if (!isDir) {
					if (isValidFile(FileTreeNode.TYPE_LOCAL, fileName)) {
						node.setDir(isDir);
						pNode.add(node);
					}
				}
			}
			pNode.setLoaded(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �Ƿ�Ϸ��ļ�
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isValidFile(byte type, String fileName) {
		return AppUtil.isSPLFile(fileName);
	}

	/**
	 * ��ʾ���
	 * 
	 * @param node
	 */
	public void showNode(FileTreeNode node) {
	}

	/**
	 * ȡ�Ҽ������˵�
	 * 
	 * @param node ѡ��Ľ��
	 * @return
	 */
	protected JPopupMenu getPopupMenu(final FileTreeNode node) {
		MenuListener menuListener = new MenuListener(node);
		JPopupMenu popMenu = new JPopupMenu();
		byte type = node.getType();
		if (type == FileTreeNode.TYPE_LOCAL) {
			if (node.isDir()) {
				popMenu.add(getMenuItem(OPEN_DIR, menuListener));
				popMenu.add(getMenuItem(REFRESH, menuListener));
			} else {
				popMenu.add(getMenuItem(OPEN_FILE, menuListener));
				popMenu.add(getMenuItem(OPEN_FILE_DIR, menuListener));
			}
			popMenu.add(getMenuItem(SWITCH_PATH, menuListener));

		}
		return popMenu;
	}

	/** ���ļ� */
	public static final byte OPEN_FILE = (byte) 1;
	/** ��Ŀ¼ */
	public static final byte OPEN_DIR = (byte) 2;
	/** ���ļ�����Ŀ¼ */
	public static final byte OPEN_FILE_DIR = (byte) 3;
	/** ˢ�� */
	public static final byte REFRESH = (byte) 4;
	/** �л���ʾ��Ŀ¼/DEMO */
	public static final byte SWITCH_PATH = (byte) 5;

	/**
	 * �½��˵���
	 * 
	 * @param action
	 * @param al
	 * @return
	 */
	protected JMenuItem getMenuItem(byte action, ActionListener al) {
		String title;
		String imgPath;
		switch (action) {
		case OPEN_FILE:
			title = mm.getMessage("filetree.open"); // ��
			imgPath = "m_open.gif";
			break;
		case OPEN_DIR:
			title = mm.getMessage("filetree.opendir"); // ��Ŀ¼
			imgPath = "m_load.gif";
			break;
		case OPEN_FILE_DIR:
			title = mm.getMessage("filetree.openfiledir"); // ���ļ�����Ŀ¼
			imgPath = "m_load.gif";
			break;
		case REFRESH:
			title = mm.getMessage("filetree.refresh"); // ˢ��
			imgPath = "m_refresh.gif";
			break;
		case SWITCH_PATH:
			if (ConfigOptions.bFileTreeDemo)
				title = mm.getMessage("filetree.switchmain"); // �л���ʾ��Ŀ¼
			else
				title = mm.getMessage("filetree.switchdemo"); // �л���ʾDEMO
			imgPath = "switchpath.gif";
			break;
		default:
			return null;
		}
		JMenuItem mi = new JMenuItem(title);
		mi.setName(action + "");
		if (imgPath != null)
			mi.setIcon(GM.getImageIcon(GC.IMAGES_PATH + imgPath));
		mi.addActionListener(al);
		return mi;
	}

	/**
	 * �˵����������
	 *
	 */
	protected class MenuListener implements ActionListener {
		FileTreeNode node;

		public MenuListener(FileTreeNode node) {
			this.node = node;
		}

		public void setFileTreeNode(FileTreeNode node) {
			this.node = node;
		}

		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
			menuAction(node, mi);
		}
	}

	protected void menuAction(FileTreeNode node, JMenuItem mi) {
		String sAction = mi.getName();
		switch (Byte.parseByte(sAction)) {
		case OPEN_FILE:
			openFile(node);
			break;
		case OPEN_DIR:
			try {
				Desktop.getDesktop().open(
						new File((String) node.getUserObject()));
			} catch (Exception ex) {
				GM.showException(GV.appFrame, ex);
			}
			break;
		case OPEN_FILE_DIR:
			try {
				Desktop.getDesktop()
						.open(new File((String) node.getUserObject())
								.getParentFile());
			} catch (Exception ex) {
				GM.showException(GV.appFrame, ex);
			}
			break;
		case REFRESH:
			refreshNode(node);
			break;
		case SWITCH_PATH:
			ConfigOptions.bFileTreeDemo = !ConfigOptions.bFileTreeDemo
					.booleanValue();
			refreshLocal();
			closeActiveNode();
			break;
		}
	}

	/**
	 * ��ǰ���ر�
	 */
	protected void closeActiveNode() {
	}

	/**
	 * ˢ�����ڵ�
	 * @param node
	 */
	public void refreshNode(FileTreeNode node) {
		if (node == null)
			return;
		node.setLoaded(false);
		if (node.getType() == FileTreeNode.TYPE_LOCAL) {
			loadSubNode(node);
		} else if (node.getType() == FileTreeNode.TYPE_ROOT) {
			// ˢ�¸��ڵ�
			refreshLocal();
		}
		nodeStructureChanged(node);
	}

	/**
	 * ���ļ�
	 * 
	 * @param node
	 */
	public void openFile(FileTreeNode node) {
		Object o = node.getUserObject();
		if (o == null)
			return;
		if (o instanceof String) {
			try {
				GV.appFrame.openSheetFile((String) node.getUserObject());
			} catch (Exception e) {
				GM.showException(GV.appFrame, e);
			}
		}
	}

	/**
	 * ����¼�������
	 *
	 */
	class mTree_mouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JTree mTree = (JTree) e.getSource();
			TreePath path = mTree.getPathForLocation(e.getX(), e.getY());
			if (path == null)
				return;
			FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
			if (node.isCheckNode()) {
				showNode(node);
				int x = e.getX();
				int level = node.getLevel();
				if (x < 22 + level * 20 && x > level * 20) {
					byte oldState = node.getSelectedState();
					byte newState;
					switch (oldState) {
					case FileTreeNode.NOT_SELECTED:
						newState = FileTreeNode.SELECTED;
						break;
					case FileTreeNode.DONT_CARE:
						newState = FileTreeNode.NOT_SELECTED;
						break;
					default:
						newState = FileTreeNode.NOT_SELECTED;
					}
					node.setSelectedState(newState);
					setSubNodesSelected(node, newState);
					FileTreeNode tempNode = node;
					setParentNodesSelected(tempNode);
					nodeChanged(node);
				}
			} else {
				showNode(node);
			}
			nodeSelected(node);
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON3) {
				return;
			}
			TreePath path = getClosestPathForLocation(e.getX(), e.getY());
			if (path == null)
				return;
			setSelectionPath(path);
			FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
			JPopupMenu pop = getPopupMenu(node);
			if (pop != null)
				pop.show(e.getComponent(), e.getX(), e.getY());
			nodeSelected(node);
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 2) {
				return;
			}
			TreePath path = getClosestPathForLocation(e.getX(), e.getY());
			if (path == null)
				return;
			FileTreeNode node = (FileTreeNode) path.getLastPathComponent();
			nodeSelected(node);
			if (!node.isDir())
				openFile(node);
		}
	}

	/**
	 * ���ڵ�ĵ��
	 * @param node
	 */
	protected void nodeSelected(FileTreeNode node) {
	}

	/**
	 * �����ӽڵ��ѡ��״̬
	 * 
	 * @param pNode
	 * @param state
	 */
	private void setSubNodesSelected(FileTreeNode pNode, byte state) {
		for (int i = 0; i < pNode.getChildCount(); i++) {
			FileTreeNode subNode = (FileTreeNode) pNode.getChildAt(i);
			subNode.setSelectedState(state);
			setSubNodesSelected(subNode, state);
		}
	}

	/**
	 * ���ø��ڵ��ѡ��״̬
	 * 
	 * @param node
	 */
	private void setParentNodesSelected(FileTreeNode node) {
		while (node.getParent() instanceof FileTreeNode) {
			FileTreeNode pNode = (FileTreeNode) node.getParent();
			byte tempState = FileTreeNode.NOT_SELECTED;
			boolean allSelected = true;
			for (int i = 0; i < pNode.getChildCount(); i++) {
				FileTreeNode subNode = (FileTreeNode) pNode.getChildAt(i);
				if (subNode.getSelectedState() != FileTreeNode.SELECTED) {
					allSelected = false;
				}
				if (subNode.getSelectedState() > tempState) {
					tempState = subNode.getSelectedState();
				}
			}
			if (tempState == FileTreeNode.SELECTED && !allSelected) {
				tempState = FileTreeNode.DONT_CARE;
			}
			pNode.setSelectedState(tempState);
			node = pNode;
		}
	}

	/**
	 * �Ƿ�ѡ����
	 */
	protected boolean isCheckNode = false;

	/**
	 * ����Ϊ��ѡ����
	 * 
	 * @param isCheckNode
	 */
	public void setCheckNodeModel(boolean isCheckNode) {
		this.isCheckNode = isCheckNode;
	}

	/**
	 * �Ƿ�ѡ����
	 * 
	 * @return
	 */
	public boolean isCheckNodeModel() {
		return isCheckNode;
	}

	/**
	 * ȡ��ǰ���
	 * 
	 * @return
	 */
	public FileTreeNode getActiveNode() {
		TreePath path = getSelectionPath();
		if (path == null)
			return null;
		return (FileTreeNode) path.getLastPathComponent();
	}

	/**
	 * �����ļ����
	 * 
	 * @param pNode
	 * @param path
	 * @param data
	 * @return
	 */
	protected FileTreeNode addFileNode(FileTreeNode pNode, String path,
			Object data) {
		if (path == null)
			return null;
		StringTokenizer st = new StringTokenizer(path, File.separator);
		List<String> paths = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			paths.add(st.nextToken());
		}
		if (paths.isEmpty())
			return null;
		for (int i = 0; i < paths.size() - 1; i++) {
			pNode = getChildByName(pNode, paths.get(i));
			if (pNode == null)
				return null;
		}
		String lastName = paths.get(paths.size() - 1);
		FileTreeNode fNode = getChildByName(pNode, lastName);
		if (fNode != null) // ����ͬ���ļ�
			return null;
		fNode = new FileTreeNode(data, FileTreeNode.TYPE_LOCAL);
		fNode.setTitle(lastName);
		pNode.add(fNode);
		return fNode;
	}

	/**
	 * ��λ�ļ����
	 * 
	 * @param pNode
	 * @param path
	 * @return
	 */
	protected FileTreeNode locateFileNode(FileTreeNode pNode, String path) {
		if (path == null)
			return null;
		StringTokenizer st = new StringTokenizer(path, File.separator);
		List<String> paths = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			paths.add(st.nextToken());
		}
		if (paths.isEmpty())
			return null;
		for (int i = 0; i < paths.size(); i++) {
			pNode = getChildByName(pNode, paths.get(i));
			if (pNode == null)
				return null;
		}
		return pNode;
	}

	/**
	 * ��λ�ļ����
	 * 
	 * @param pNode
	 * @param paths
	 * @return
	 */
	protected FileTreeNode locateFileNode(FileTreeNode pNode, List<String> paths) {
		if (paths == null || paths.isEmpty())
			return pNode;
		int size = paths.size();
		for (int i = size - 1; i >= 0; i--) {
			pNode = getChildByName(pNode, paths.get(i));
			if (pNode == null)
				return null;
		}
		return pNode;
	}

	/**
	 * ������ȡ�ӽڵ�
	 * 
	 * @param pNode
	 * @param nodeName
	 * @return
	 */
	protected FileTreeNode getChildByName(FileTreeNode pNode, String nodeName) {
		if (nodeName == null)
			return null;
		int count = pNode.getChildCount();
		FileTreeNode childNode;
		for (int i = 0; i < count; i++) {
			childNode = (FileTreeNode) pNode.getChildAt(i);
			if (nodeName.equals(childNode.getTitle()))
				return childNode;
		}
		return null;
	}

	/**
	 * ѡ����
	 * 
	 * @param node
	 */
	protected void selectNode(FileTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		expandPath(path);
		setSelectionPath(path);
		nodeSelected(node);
	}

	/**
	 * ���仯��
	 * 
	 * @param node
	 */
	protected void nodeChanged(FileTreeNode node) {
		if (node != null)
			((DefaultTreeModel) getModel()).nodeChanged(node);
	}

	/**
	 * ���ṹ�仯��
	 * 
	 * @param node
	 */
	protected void nodeStructureChanged(FileTreeNode node) {
		if (node != null)
			((DefaultTreeModel) getModel()).nodeStructureChanged(node);
	}

	/**
	 * ȡ���ڵ�
	 * 
	 * @return
	 */
	public FileTreeNode getRoot() {
		return root;
	}

	/**
	 * ������Ŀ¼
	 */
	public void changeMainPath(String mainPath) {
		ConfigOptions.sMainPath = mainPath;
		refreshLocal();
	}

}
