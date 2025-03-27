package com.scudata.ide.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.control.JWindowList;
import com.scudata.ide.common.resources.IdeCommonMessage;

/**
 * IDE toolbar window
 *
 */
public abstract class ToolBarWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Sheet buttons
	 */
	private SheetButton[] buttons = null;

	/**
	 * ��ť�Ŀ��
	 */
	private final int BUTTON_WIDTH = 180;
	/**
	 * ��ʾ���ര�ڵİ�ť
	 */
	private JLabel bMore = new JLabel();

	/**
	 * ��ʾ���ര�ڵ��б�ؼ�
	 */
	private JWindowList winList;

	/**
	 * ��ֹ�¼�ִ��
	 */
	private boolean preventChange = false;
	/**
	 * ��С����ť
	 */
	private JButton bMin = new JButton();
	/**
	 * ��ԭ��ť
	 */
	private JButton bResume = new JButton();
	/**
	 * �رհ�ť
	 */
	private JButton bClose = new JButton();
	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * ���캯��
	 */
	public ToolBarWindow() {
		super();
		setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(0, 30));
		this.setPreferredSize(new Dimension(1, 30));
		this.setVisible(false);
		bMin.setToolTipText(mm.getMessage("toolbarwindow.min")); // ��С��
		bResume.setToolTipText(mm.getMessage("toolbarwindow.resume")); // ���»�ԭ
		bClose.setToolTipText(mm.getMessage("toolbarwindow.close")); // �ر�
		bMin.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_min1.gif"));
		bResume.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_resume1.gif"));
		bClose.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_close1.gif"));
		bMin.setBorder(null);
		bResume.setBorder(null);
		bClose.setBorder(null);
		Dimension d = new Dimension(25, 25);
		bMin.setMaximumSize(d);
		bMin.setPreferredSize(d);
		bResume.setMaximumSize(d);
		bResume.setPreferredSize(d);
		bClose.setMaximumSize(d);
		bClose.setPreferredSize(d);
		bMore.setBorder(null);
		bMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GV.appSheet.setIcon(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
				refresh();
			}
		});
		bMin.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bMin.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_min2.gif"));
			}

			public void mouseExited(MouseEvent e) {
				bMin.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_min1.gif"));
			}
		});
		bResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GV.appSheet.setMaximum(false);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
				refreshTitleButton();
			}
		});
		bResume.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bResume.setIcon(GM.getImageIcon(GC.IMAGES_PATH
						+ "b_resume2.gif"));
			}

			public void mouseExited(MouseEvent e) {
				bResume.setIcon(GM.getImageIcon(GC.IMAGES_PATH
						+ "b_resume1.gif"));
			}
		});
		bClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeSheet(GV.appSheet);
			}
		});
		bClose.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				bClose.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_close2.gif"));
			}

			public void mouseExited(MouseEvent e) {
				bClose.setIcon(GM.getImageIcon(GC.IMAGES_PATH + "b_close1.gif"));
			}
		});
		bMore.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				try {
					if (e.getButton() != MouseEvent.BUTTON1)
						return;
					preventChange = true;
					if (winList != null && winList.isVisible()) {
						winList.dispose();
						return;
					}
					winList = new JWindowList(buttons.length) {
						private static final long serialVersionUID = 1L;

						public boolean showSheet(String filePath) {
							JInternalFrame[] sheets = GV.appFrame
									.getAllSheets();
							if (sheets != null) {
								for (int i = 0; i < sheets.length; i++) {
									IPrjxSheet sheet = (IPrjxSheet) sheets[i];
									if (sheet.getSheetTitle().equals(filePath)) {
										try {
											dispSheet(sheet);
											refreshSheet(sheet);
										} catch (Exception e1) {
											GM.showException(GV.appFrame, e1);
										}
										return true;
									}
								}
							}
							return false;
						}

						public boolean isClickButton() {
							return preventChange;
						}

						public String getSheetIcon() {
							return getSheetIconName();
						}
					};
					Rectangle rect = bMore.getBounds();
					int x = GM.getAbsolutePos(bMore, true);
					int y = GM.getAbsolutePos(bMore, false);
					winList.setPos(x, y + rect.height);
					winList.setVisible(true);
				} finally {
					preventChange = false;
				}
			}

			public void mouseEntered(MouseEvent e) {
				bMore.setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseExited(MouseEvent e) {
				bMore.setBorder(null);
			}
		});
	}

	/**
	 * ȡҳ���ͼ������
	 * 
	 * @return
	 */
	public abstract String getSheetIconName();

	/**
	 * �ر�ҳ��
	 * 
	 * @param sheet
	 */
	public abstract void closeSheet(IPrjxSheet sheet);

	/**
	 * ��ʾҳ��
	 * 
	 * @param sheet
	 * @throws Exception
	 */
	public abstract void dispSheet(IPrjxSheet sheet) throws Exception;

	/**
	 * ȡ��ť�ϵ�ͼ�����
	 * 
	 * @return
	 */
	public abstract ImageIcon getLogoImage();

	/**
	 * �Ƿ����ڵ����ʾ���ര�ڰ�ť
	 * 
	 * @return
	 */
	public boolean isButtonClicking() {
		return preventChange;
	}

	/**
	 * ˢ��
	 */
	public void refresh() {
		JInternalFrame[] sheets = GV.appFrame.getAllSheets();
		this.removeAll();
		if (sheets == null || sheets.length == 0) {
			buttons = null;
			return;
		}
		int width = GV.appFrame.getWidth();
		int size;
		if (width - 85 > BUTTON_WIDTH * sheets.length) { // ȫ���ܰ���
			size = sheets.length;
		} else {
			size = (width - 210) / BUTTON_WIDTH;
			if (size < 0)
				size = 0;
		}
		JInternalFrame appSheet = GV.appFrame.getActiveSheet();
		size = Math.min(size, sheets.length);
		buttons = new SheetButton[size];
		GridBagConstraints gbc;
		Insets bInsets = new Insets(0, 0, 0, 0);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new SheetButton(sheets[i]);
			gbc = GM.getGBC(0, i);
			gbc.insets = bInsets;
			if (i == 0)
				gbc.insets.left = 2;
			add(buttons[i], gbc);
			buttons[i].setSelected(sheets[i] == appSheet);
		}
		int otherSize = sheets.length - size;
		if (otherSize > 0) {
			bMore.setText(">>" + otherSize);
			bMore.setMaximumSize(new Dimension(100, 24));
			bMore.setPreferredSize(new Dimension(40, 24));
			gbc = GM.getGBC(0, buttons.length);
			gbc.insets = new Insets(3, 2, 3, 0);
			add(bMore, gbc);
		}
		add(new JPanel(), GM.getGBC(0, buttons.length + 1, true));
		gbc = GM.getGBC(0, buttons.length + 2);
		gbc.insets = bInsets;
		add(bMin, gbc);
		gbc = GM.getGBC(0, buttons.length + 3);
		gbc.insets = bInsets;
		add(bResume, gbc);
		gbc = GM.getGBC(0, buttons.length + 4);
		gbc.insets = bInsets;
		gbc.insets.right = 2;
		add(bClose, gbc);
		if (winList != null && winList.isVisible()) {
			if (otherSize == 0) {
				winList.dispose();
			} else {
				Rectangle rect = bMore.getBounds();
				int x = GM.getAbsolutePos(bMore, true);
				int y = GM.getAbsolutePos(bMore, false);
				winList.setBounds(x, y + rect.height, 200, 300);
			}
		}
		refreshTitleButton();
		this.revalidate();
		this.repaint();
	}

	/**
	 * ˢ�±������İ�ť(��С��,��ԭ,�ر�)
	 */
	private void refreshTitleButton() {
		boolean vis = false;
		if (GV.appSheet != null) {
			vis = GV.appSheet.isMaximum() && !GV.appSheet.isIcon();
		}
		bMin.setVisible(vis);
		bResume.setVisible(vis);
		bClose.setVisible(vis);
	}

	/**
	 * ˢ��ҳ��
	 * 
	 * @param sheet
	 */
	public void refreshSheet(JInternalFrame sheet) {
		if (sheet != null && buttons != null) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i] != null && buttons[i].getSheet() == sheet) {
					for (int j = 0; j < buttons.length; j++)
						buttons[j].setSelected(false);
					buttons[i].setSelected(true);
					refreshTitleButton();
					return;
				}
			}
		}
		refresh();
	}

	/**
	 * �������ļ�
	 * 
	 * @param sheet
	 * @param newFile
	 */
	public void changeFileName(IPrjxSheet sheet, String newFile) {
		if (buttons != null) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].getSheet() == sheet) {
					buttons[i].rename(newFile);
					break;
				}
			}
		}
	}

	/**
	 * ѡ��״̬�ı���ɫ
	 */
	private static Color SELECTED_BACK_COLOR = Color.WHITE;
	/**
	 * ȱʡ�ı���ɫ
	 */
	private static Color DEFAULT_BACK_COLOR = new JPanel().getBackground()
			.darker();

	/**
	 * ҳ�水ť��
	 */
	class SheetButton extends JPanel {
		private static final long serialVersionUID = 1L;
		/**
		 * ҳ�����
		 */
		private JInternalFrame sheet;
		/**
		 * ҳ������
		 */
		private JLabel labelText = new JLabel();
		/**
		 * ҳ��ͼ��
		 */
		private JLabel labelIcon = new JLabel();
		/**
		 * �ر�ͼ��
		 */
		private JLabel labelClose = new JLabel();
		/**
		 * ͼ�����
		 */
		private JPanel panelIcon;

		/**
		 * ���캯��
		 * 
		 * @param sheet
		 */
		public SheetButton(final JInternalFrame sheet) {
			this.sheet = sheet;
			setLayout(new BorderLayout());
			ImageIcon image = ((IPrjxSheet)sheet).getSheetImageIcon();
			if(image==null){
				image = getLogoImage();
			}
			
			if (image != null)
				image.setImage(image.getImage().getScaledInstance(20, 20,
						Image.SCALE_SMOOTH));
			labelIcon.setIcon(image);

			String filePath = ((IPrjxSheet) sheet).getSheetTitle();
			String fileName = filePath;
			try {
				File f = new File(filePath);
				fileName = f.getName();
			} catch (Exception ex) {
			}
			labelText.setText(fileName);
			setTip(filePath);
			add(labelText, BorderLayout.CENTER);
			panelIcon = new JPanel(new BorderLayout(2, 2));
			panelIcon.add(labelIcon);
			Dimension di = new Dimension(25, 25);
			panelIcon.setPreferredSize(di);
			add(panelIcon, BorderLayout.WEST);
			add(labelClose, BorderLayout.EAST);

			MouseAdapter showSheetListener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					try {
						if (!GV.appFrame.showSheet(sheet))
							return;
					} catch (Exception e1) {
						GM.showException(GV.appFrame, e1);
					}
					refreshSheet(sheet);
				}

				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1
							&& e.getClickCount() == 2) {
						try {
							GV.appSheet.setMaximum(!GV.appSheet.isMaximum());
						} catch (PropertyVetoException e1) {
							e1.printStackTrace();
						}
						refreshTitleButton();
					}
				}
			};
			labelIcon.addMouseListener(showSheetListener);
			labelText.addMouseListener(showSheetListener);

			MouseAdapter closeSheetListener = new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					labelClose.setIcon(GM.getImageIcon(GC.IMAGES_PATH
							+ "b_closesheet.gif"));
				}

				public void mouseExited(MouseEvent e) {
					labelClose.setIcon(null);
				}
			};

			labelIcon.addMouseListener(closeSheetListener);
			labelText.addMouseListener(closeSheetListener);

			labelClose.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					labelClose.setIcon(GM.getImageIcon(GC.IMAGES_PATH
							+ "m_delete.gif"));
				}

				public void mouseExited(MouseEvent e) {
					labelClose.setIcon(null);
				}
			});

			labelClose.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON1)
						return;
					closeSheet((IPrjxSheet) sheet);
				}
			});
			Dimension d = new Dimension(BUTTON_WIDTH, 25);
			this.setMaximumSize(d);
			this.setMinimumSize(d);
			this.setPreferredSize(d);
		}

		/**
		 * ����ѡ��״̬
		 * 
		 * @param isSelected
		 */
		public void setSelected(boolean isSelected) {
			Color bc;
			if (isSelected) {
				this.setBorder(BorderFactory.createRaisedBevelBorder());
				bc = SELECTED_BACK_COLOR;
			} else {
				this.setBorder(BorderFactory.createEtchedBorder());
				bc = DEFAULT_BACK_COLOR;
			}
			this.setBackground(bc);
			panelIcon.setBackground(bc);

		}

		/**
		 * ����ҳ�����
		 * 
		 * @return
		 */
		public JInternalFrame getSheet() {
			return sheet;
		}

		/**
		 * ������
		 * 
		 * @param newName
		 */
		public void rename(String newName) {
			String fileName = newName;
			try {
				File f = new File(newName);
				fileName = f.getName();
			} catch (Exception ex) {
			}
			labelText.setText(fileName);
			setTip(newName);
			repaint();
		}

		/**
		 * ������ʾ
		 * 
		 * @param filePath
		 */
		private void setTip(String filePath) {
			labelText.setToolTipText(filePath);
			this.setToolTipText(filePath);
			labelIcon.setToolTipText(filePath);
			labelClose.setToolTipText(filePath);
		}
	}
}