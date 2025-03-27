package com.scudata.ide.spl.dialog;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JWindow;

import com.scudata.common.StringUtils;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.swing.FreeLayout;

/**
 * ��������ʾ����ͼƬ����
 * 
 */
public class DialogSplash extends JWindow {
	private static final long serialVersionUID = 1L;

	/**
	 * ���캯��
	 * 
	 * @param splashImage
	 *            splashͼƬ·��
	 */
	public DialogSplash(String splashImage) {
		try {
			// this.setUndecorated(true);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // ���ù��
			this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			this.getRootPane().setBorder(null);
			this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			// this.setResizable(false);
			initUI(splashImage);
			GM.centerWindow(this);
			// this.setModal(false);
		} catch (Exception e) {
			GM.writeLog(e);
		}
	}

	/**
	 * ��ʼ���ؼ�
	 * 
	 * @throws Exception
	 */
	private void initUI(String splashImage) throws Exception {
		ImageIcon ii = getImageIcon(splashImage);
		Image image = null;
		if (ii != null) {
			this.setSize(ii.getIconWidth(), ii.getIconHeight());
			image = ii.getImage();
		}
		panelImage = new ImagePanel(image);
		panelImage.setOpaque(false);
		panelImage.setLayout(new FreeLayout());
		getContentPane().add(panelImage);
	}

	/**
	 * �رմ���
	 */
	public void closeWindow() {
		dispose();
	}

	/**
	 * ȡͼƬ����
	 * 
	 * @return
	 */
	private ImageIcon getImageIcon(String splashImage) {
		ImageIcon ii = null;
		if (StringUtils.isValidString(splashImage)) {
			String path = GM.getAbsolutePath(splashImage);
			File f = new File(path);
			if (f.exists()) {
				ii = new ImageIcon(path);
			} else {
				ii = GM.getImageIcon(this, splashImage);
			}
		}
		if (ii == null) {
			String imgPath = GC.IMAGES_PATH + getDefaultImageName()
					+ GM.getLanguageSuffix() + ".png";
			ii = GM.getImageIcon(this, imgPath);
		}
		return ii;
	}

	/**
	 * Ĭ�ϵ�ͼ���ļ���
	 * 
	 * @return
	 */
	protected String getDefaultImageName() {
		return "esproc";
	}

	/**
	 * ��ʾͼƬ�����
	 *
	 */
	class ImagePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image image = null;

		public ImagePanel(Image image) {
			this.image = image;
		}

		public void paint(Graphics g) {
			if (image != null)
				g.drawImage(image, 0, 0, null);
			super.paint(g);
		}
	}

	protected ImagePanel panelImage;

	public static final int WINDOW_WIDTH = 600;
	public static final int WINDOW_HEIGHT = 370;
}
