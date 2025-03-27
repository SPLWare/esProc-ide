package com.scudata.ide.common.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;

import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.IPrjxSheet;
import com.scudata.ide.common.swing.JListEx;

/**
 * �����б������ڰ�ť��ʾ����ʱ��������ര�ڻ���ʾ�˴���
 *
 */
public abstract class JWindowList extends JWindow {
	private static final long serialVersionUID = 1L;
	/**
	 * �����ı���
	 */
	private JTextField textFilter = new JTextField();
	/**
	 * �����б�ؼ�
	 */
	private JListEx listWindow = new JListEx();
	/**
	 * �ļ�·���б�
	 */
	private Vector<String> paths = new Vector<String>();
	/**
	 * �ļ������б�
	 */
	private Vector<String> names = new Vector<String>();
	/**
	 * ���˺���ļ�·���б�
	 */
	private Vector<String> filterPaths = new Vector<String>();
	/**
	 * ���˺���ļ������б�
	 */
	private Vector<String> filterNames = new Vector<String>();

	/**
	 * �Ѿ���ʾ�ڴ��ڹ������ϵ�·������
	 */
	private HashSet<String> existPaths = new HashSet<String>();

	private Map<String, String> typeMap = new HashMap<String, String>();
	private final Color BACK_COLOR = new Color(255, 255, 214);
	/**
	 * ȱʡ�ı���ɫ
	 */
	private final Color DEFAULT_BACK_COLOR = new JList<Object>()
			.getBackground();
	/**
	 * ȱʡ��ѡ��״̬�ı���ɫ
	 */
	private final Color SELECTED_BACK_COLOR = new JList<Object>()
			.getSelectionBackground();

	/**
	 * ���캯��
	 * 
	 * @param buttonSize
	 *            �Ѿ���ʾ�ڴ��ڹ������ϵİ�ť����
	 */
	public JWindowList(int buttonSize) {
		super(GV.appFrame);
		getContentPane().add(textFilter, BorderLayout.NORTH);
		JScrollPane jSPWin = new JScrollPane(listWindow);
		getContentPane().add(jSPWin, BorderLayout.CENTER);
		addWindowFocusListener(new WindowAdapter() {
			public void windowLostFocus(WindowEvent e) {
				if (isClickButton())
					return;
				dispose();
			}
		});
		setFocusable(true);
		GV.appFrame.getAllSheets();
		JInternalFrame[] sheets = GV.appFrame.getAllSheets();
		if (sheets != null) {
			for (int i = 0; i < sheets.length; i++) {
				String filePath = ((IPrjxSheet) sheets[i]).getSheetTitle();
				String fileName = filePath;
				try {
					File f = new File(filePath);
					fileName = f.getName();
				} catch (Exception ex) {
				}
				names.add(fileName);
				paths.add(filePath);
				if (i < buttonSize) {
					existPaths.add(filePath);
				}
				filterNames.add(fileName);
				filterPaths.add(filePath);
				typeMap.put(filePath, getSheetIcon());
			}
		}
		listWindow.x_setData(filterPaths, filterNames);

		ListCellRenderer cellRenderer = new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				final JPanel panel = new JPanel(new BorderLayout());
				String filePath = (String) filterPaths.get(index);
				String iconName = typeMap.get(filePath);
				JLabel label = new JLabel(GM.getImageIcon(GC.IMAGES_PATH
						+ iconName));
				panel.add(label, BorderLayout.WEST);
				final JTextField text = new JTextField(
						(String) filterNames.get(index));
				text.setBackground(BACK_COLOR);
				if (!existPaths.contains(filePath)) {
					Font font = text.getFont();
					text.setFont(new Font(font.getFontName(), Font.BOLD, font
							.getSize() + 2));
				}
				text.setBorder(null);
				panel.add(text, BorderLayout.CENTER);
				panel.setToolTipText(filePath);
				text.setToolTipText(filePath);
				label.setToolTipText(filePath);
				panel.setMinimumSize(new Dimension(0, 20));
				panel.setPreferredSize(new Dimension(0, 20));

				MouseAdapter listener = new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						panel.setBackground(SELECTED_BACK_COLOR);
						text.setBackground(SELECTED_BACK_COLOR);
					}

					public void mouseExited(MouseEvent e) {
						panel.setBackground(DEFAULT_BACK_COLOR);
						text.setBackground(DEFAULT_BACK_COLOR);
					}
				};
				panel.addMouseListener(listener);
				return panel;
			}
		};
		listWindow.setCellRenderer(cellRenderer);
		listWindow.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				int index = listWindow.getSelectedIndex();
				try {
					if (showSheet((String) filterPaths.get(index)))
						dispose();
				} catch (Exception e1) {
					GM.showException(e1);
				}
			}
		});
		textFilter.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filter();
			}
		});
		textFilter.requestFocusInWindow();
		setBackground(BACK_COLOR);
		textFilter.setBackground(BACK_COLOR);
		listWindow.setBackground(BACK_COLOR);
		jSPWin.setBackground(BACK_COLOR);
	}

	/**
	 * ȡҳ���ͼ��
	 * 
	 * @return
	 */
	public abstract String getSheetIcon();

	/**
	 * ��ʾҳ��
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return
	 */
	public abstract boolean showSheet(String filePath);

	/**
	 * �Ƿ����ڵ����ʾ���ര�ڰ�ť
	 * 
	 * @return
	 */
	public abstract boolean isClickButton();

	/**
	 * ����λ��
	 * 
	 * @param x
	 *            X����
	 * @param y
	 *            Y����
	 */
	public void setPos(int x, int y) {
		int height = (paths.size() + 1) * 20 + 10;
		height = Math.min((int) (GV.appFrame.getHeight() * 0.5), height);
		setBounds(x, y, 200, height);
	}

	/**
	 * ����
	 */
	private void filter() {
		String filter = textFilter.getText();
		if (filter != null)
			filter = filter.toLowerCase();
		filterPaths = new Vector<String>();
		filterNames = new Vector<String>();
		for (int i = 0; i < names.size(); i++) {
			String fileName = (String) names.get(i);
			if (filter != null) {
				if (fileName.toLowerCase().startsWith(filter)) {
					filterPaths.add(paths.get(i));
					filterNames.add(names.get(i));
				}
			} else {
				filterPaths.add(paths.get(i));
				filterNames.add(names.get(i));
			}
		}
		listWindow.x_setData(filterPaths, filterNames);
	}
}
