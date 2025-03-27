package com.scudata.ide.common.dialog;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.scudata.app.common.AppConsts;
import com.scudata.common.ByteMap;
import com.scudata.common.MessageManager;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.common.swing.VFlowLayout;
import com.scudata.util.CellSetUtil;

/**
 * �����ļ�
 *
 */
public class DialogDemoFiles extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * �򿪰�ť
	 */
	private JButton jBOpen = new JButton();

	/**
	 * �رհ�ť
	 */
	private JButton jBClose = new JButton();

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * ���ؼ�
	 */
	private JTree m_Tree = new JTree();

	/**
	 * �༭��
	 */
	private JEditorPane tpDesc = new JEditorPane();

	/**
	 * �ļ���׺
	 */
	private String[] fileExts;

	/**
	 * ���캯��
	 */
	public DialogDemoFiles() {
		this(AppConsts.SPL_FILE_EXTS.split(","));
	}

	/**
	 * ���캯��
	 * 
	 * @param fileExts �ļ���׺
	 */
	public DialogDemoFiles(String[] fileExts) {
		super(GV.appFrame, "�����ļ�", true);
		try {
			this.fileExts = fileExts;
			initUI();
			resetLangText();
			setSize(800, 560);
			load();
			GM.setDialogDefaultButton(this, jBOpen, jBClose);
		} catch (Exception e) {
			GM.showException(this, e);
		}
	}

	/**
	 * ���ļ��б�
	 * 
	 * @param filePath ���ļ�·��
	 * @param fileExts �ļ���׺
	 * @return
	 */
	private static File[] listSubFiles(String filePath, final String[] fileExts) {
		File f = new File(filePath);
		FileFilter ff = new FileFilter() {
			public boolean accept(File file) {
				String extName = file.getName().toLowerCase();
				if (file.isDirectory())
					return true;
				for (String fileExt : fileExts) {
					if (extName.endsWith(fileExt))
						return true;
				}
				return false;
			}
		};
		return f.listFiles(ff);
	}

	/**
	 * �������ļ�
	 * 
	 * @param parent   �����
	 * @param filePath �ļ�·��
	 * @return
	 */
	private boolean loadSubFiles(DefaultMutableTreeNode parent, String filePath) {
		File[] list = listSubFiles(filePath, fileExts);
		if (list == null) {
			return false;
		}
		boolean r = false;
		for (int i = 0; i < list.length; i++) {
			String text = list[i].getName();
			if (text.indexOf(".") > 0) {
				text = text.substring(0, text.length() - 4);
			}
			if (list[i].isDirectory()) {
				DefaultMutableTreeNode p = new DefaultMutableTreeNode(text);
				boolean subr = loadSubFiles(p, list[i].getAbsolutePath());
				if (subr) {
					parent.add(p);
				}
				r = r || subr;
			} else {
				String absolutePath = list[i].getAbsolutePath();
				String title = text;
				String desc = "";

				ByteMap bm = CellSetUtil.readCsCustomPropMap(absolutePath);
				if (bm != null) {
					String val = (String) bm.get(GC.CELLSET_TITLE);
					if (val != null) {
						title = val;
					}
					val = (String) bm.get(GC.CELLSET_DESC);
					if (val != null) {
						desc = val;
					}
				}
				FileNode fileObject = new FileNode(absolutePath, title, desc);
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fileObject);
				parent.add(fileNode);
				r = true;
			}
		}
		return r;
	}

	/**
	 * �����ļ��Ƿ����
	 * 
	 * @param fileExts
	 * @return
	 */
	public static boolean isExistDemoFiles(String[] fileExts) {
		String filePath = GM.getAbsolutePath("demo/" + GM.getLanguageSuffix().substring(1));
		File[] list = listSubFiles(filePath, fileExts);
		if (list == null || list.length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * ����
	 */
	private void load() {
		String filePath = GM.getAbsolutePath("demo/" + GM.getLanguageSuffix().substring(1));
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(filePath);
		DefaultTreeModel m_TreeModel = new DefaultTreeModel(root);
		m_Tree.setModel(m_TreeModel);
		File[] list = listSubFiles(filePath, fileExts);
		if (list == null || list.length == 0) {
			return;
		}
		loadSubFiles(root, filePath);
		TreeNode childNode = root.getFirstChild();
		if (childNode != null) {
			TreePath path = new TreePath(new Object[] { root, childNode });
			m_Tree.setSelectionPath(path);
			m_Tree_mouseClicked(null);
		}
	}

	/**
	 * ����������Դ
	 */
	private void resetLangText() {
		this.setTitle(new StringTokenizer(mm.getMessage("menu.help.demofiles"), "(").nextToken());
		jBOpen.setText(mm.getMessage("menu.file.open"));
		jBClose.setText(mm.getMessage("button.close"));
	}

	/**
	 * ��ʼ��UI
	 * 
	 * @throws Exception
	 */
	private void initUI() throws Exception {
		jBOpen.setMnemonic('O');
		jBOpen.setText("��(O)");
		jBOpen.addActionListener(new DialogDemoFiles_jBOpen_actionAdapter(this));
		jBClose.setMnemonic('C');
		jBClose.setText("�ر�(C)");
		jBClose.addActionListener(new DialogDemoFiles_jBClose_actionAdapter(this));
		JPanel jPanel2 = new JPanel();
		VFlowLayout vFlowLayout1 = new VFlowLayout();
		JScrollPane jScrollPane1 = new JScrollPane();
		JPanel jPanel1 = new JPanel();
		jPanel2.setLayout(vFlowLayout1);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DialogDemoFiles_this_windowAdapter(this));
		m_Tree.addMouseListener(new DialogDemoFiles_m_Tree_mouseAdapter(this));
		m_Tree.addTreeSelectionListener(new DialogDemoFiles_m_Tree_treeSelectionAdapter(this));
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		jPanel2.add(jBOpen, null);
		jPanel2.add(jBClose, null);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.add(jScrollPane1, JSplitPane.LEFT);
		jScrollPane1.getViewport().add(m_Tree, null);
		splitPane.add(jPanel1, JSplitPane.RIGHT);
		jPanel1.setLayout(new GridBagLayout());
		tpDesc.setEditable(false);
		tpDesc.setContentType("text/html");

		JScrollPane spDesc = new JScrollPane(tpDesc);
		spDesc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		jPanel1.add(spDesc, GM.getGBC(1, 1, true, true, 0, 0));
		splitPane.setDividerLocation(300);
	}

	/**
	 * ���ڹر��¼�
	 * 
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * �򿪰�ť�¼�
	 * 
	 * @param e
	 */
	void jBOpen_actionPerformed(ActionEvent e) {
		Object selectedNode = getSelectedNode();
		if (selectedNode == null) {
			return;
		}
		if (selectedNode instanceof FileNode) {
			try {
				JInternalFrame sheet = GV.appFrame.openSheetFile(((FileNode) selectedNode).getAbsolutePath());
				if (sheet != null) {
					GM.setWindowDimension(this);
					dispose();
				}
			} catch (Throwable ex) {
			}
		} else {
			if (GM.getOperationSytem() == GC.OS_WINDOWS) {
				try {
					String directory = (String) selectedNode;
					Runtime.getRuntime().exec("cmd /C start explorer.exe " + directory);
				} catch (Exception x) {
					GM.showException(this, x);
				}
			}
		}
	}

	/**
	 * �رհ�ť�¼�
	 * 
	 * @param e
	 */
	void jBClose_actionPerformed(ActionEvent e) {
		GM.setWindowDimension(this);
		dispose();
	}

	/**
	 * ȡ��ǰѡ��Ľ��
	 * 
	 * @return
	 */
	private Object getSelectedNode() {
		TreePath[] paths = m_Tree.getSelectionPaths();
		if (paths == null || paths.length == 0) {
			return null;
		}

		DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) paths[paths.length - 1].getLastPathComponent();
		Object lastNode = dmt.getUserObject();
		if (lastNode instanceof FileNode) {
			return lastNode;
		} else {
			TreeNode[] tns = dmt.getPath();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < tns.length; i++) {
				if (i > 0) {
					sb.append("\\");
				}
				sb.append(tns[i]);
			}
			return sb.toString();
		}
	}

	/**
	 * ����������
	 * 
	 * @param e
	 */
	void m_Tree_mouseClicked(MouseEvent e) {
		Object selectedNode = getSelectedNode();
		if (selectedNode == null) {
			return;
		}

		boolean isFileNode = selectedNode instanceof FileNode;
		if (!isFileNode) {
			tpDesc.setText("");
		} else {
			FileNode fn = (FileNode) selectedNode;
			StringBuffer buf = new StringBuffer();
			buf.append("<font size=\"6\">");
			buf.append(fn.getTitle());
			buf.append("</font > <br> <br> <font size=\"5\">");
			buf.append(fn.getDesc());
			buf.append("</font> <br> <br> <font color=#0000C6 size=\"4\">");//
			buf.append(fn.getAbsolutePath());
			buf.append("</font>");
			tpDesc.setText(buf.toString());
			tpDesc.setCaretPosition(0);

			if (e != null && e.getClickCount() == 2) {
				jBOpen_actionPerformed(null);
			}
		}

	}

	/**
	 * ��ѡ����仯
	 * 
	 * @param e
	 */
	void m_Tree_valueChanged(TreeSelectionEvent e) {
		m_Tree_mouseClicked(null);
	}

}

/**
 * �ļ����
 *
 */
class FileNode {
	/**
	 * ·��
	 */
	String absolutePath;

	/**
	 * ����
	 */
	String title;

	/**
	 * ����
	 */
	String desc;

	/**
	 * ���캯��
	 * 
	 * @param absolutePath ·��
	 * @param title        ����
	 * @param desc         ����
	 */
	public FileNode(String absolutePath, String title, String desc) {
		this.absolutePath = absolutePath;
		this.title = title;
		this.desc = desc;
	}

	/**
	 * ȡ·��
	 * 
	 * @return
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

	/**
	 * ȡ����
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ȡ����
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * ת�����ַ���
	 */
	public String toString() {
		return title;
	}
}

class DialogDemoFiles_jBOpen_actionAdapter implements java.awt.event.ActionListener {
	DialogDemoFiles adaptee;

	DialogDemoFiles_jBOpen_actionAdapter(DialogDemoFiles adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBOpen_actionPerformed(e);
	}
}

class DialogDemoFiles_jBClose_actionAdapter implements java.awt.event.ActionListener {
	DialogDemoFiles adaptee;

	DialogDemoFiles_jBClose_actionAdapter(DialogDemoFiles adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jBClose_actionPerformed(e);
	}
}

class DialogDemoFiles_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogDemoFiles adaptee;

	DialogDemoFiles_this_windowAdapter(DialogDemoFiles adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogDemoFiles_m_Tree_mouseAdapter extends java.awt.event.MouseAdapter {
	DialogDemoFiles adaptee;

	DialogDemoFiles_m_Tree_mouseAdapter(DialogDemoFiles adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.m_Tree_mouseClicked(e);
	}
}

class DialogDemoFiles_m_Tree_treeSelectionAdapter implements javax.swing.event.TreeSelectionListener {
	DialogDemoFiles adaptee;

	DialogDemoFiles_m_Tree_treeSelectionAdapter(DialogDemoFiles adaptee) {
		this.adaptee = adaptee;
	}

	public void valueChanged(TreeSelectionEvent e) {
		adaptee.m_Tree_valueChanged(e);
	}
}
