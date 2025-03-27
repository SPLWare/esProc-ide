package com.scudata.ide.common.dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;

/**
 * ��󻯴��ڻ���
 *
 */
public class DialogMaxmizable extends JDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * �ɳߴ�
	 */
	public Dimension oldSize = null;
	/**
	 * �Ƿ����
	 */
	public boolean isMaxized = false;

	/**
	 * ���캯��
	 * 
	 * @param frame
	 * @param title
	 * @param model
	 */
	public DialogMaxmizable(Frame frame, String title, boolean model) {
		super(frame, title, model);
	}

	/**
	 * ���캯��
	 * 
	 * @param parent
	 * @param title
	 * @param model
	 */
	public DialogMaxmizable(Dialog parent, String title, boolean model) {
		super(parent, title, model);
	}
}