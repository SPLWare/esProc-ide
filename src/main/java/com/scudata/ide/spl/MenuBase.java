package com.scudata.ide.spl;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.scudata.common.MessageManager;
import com.scudata.ide.common.AppMenu;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * �����˵������ļ���ʱ��
 *
 */
public class MenuBase extends AppMenu {
	private static final long serialVersionUID = 1L;

	/**
	 * ��������Դ������
	 */
	protected MessageManager mm = IdeSplMessage.get();

	/**
	 * ���캯��
	 */
	public MenuBase() {
		init();
	}

	/**
	 * ��ʼ���˵�
	 */
	protected void init() {
		// �ļ��˵�
		add(getFileMenu());

		// ���߲˵�
		add(getToolMenu());

		// ���ڲ˵���
		tmpLiveMenu = getWindowMenu();
		add(tmpLiveMenu);

		// �����˵���
		add(getHelpMenu());

		setEnable(getMenuItems(), false);
		resetLiveMenu();
	}

	/**
	 * ȡ�ļ��˵�
	 * 
	 * @return JMenu
	 */
	protected JMenu getFileMenu() {
		JMenu menu;
		JMenuItem menuTemp;
		menu = getCommonMenuItem(GC.FILE, 'F', true);
		menu.add(newCommonMenuItem(GC.iNEW, GC.NEW, 'N', ActionEvent.CTRL_MASK,
				true));
		menu.add(newCommonMenuItem(GC.iOPEN, GC.OPEN, 'O',
				ActionEvent.CTRL_MASK, true));
		menu.addSeparator();
		menu.add(getRecentMainPaths());
		menu.add(getRecentFile());
		menuTemp = getRecentConn();
		menu.add(menuTemp);
		menu.addSeparator();
		menu.add(newCommonMenuItem(GC.iQUIT, GC.QUIT, 'X', GC.NO_MASK, true));
		return menu;
	}

	/**
	 * ȡ���߲˵�
	 * 
	 * @return JMenu
	 */
	protected JMenu getToolMenu() {
		JMenu menu = getCommonMenuItem(GC.TOOL, 'T', true);
		JMenuItem menuTemp;
		menuTemp = newCommonMenuItem(GC.iDATA_SOURCE, GC.DATA_SOURCE, 'S',
				GC.NO_MASK, true);
		menu.add(menuTemp);
		JMenuItem miRep = newSplMenuItem(GCSpl.iFILE_REPLACE,
				GCSpl.FILE_REPLACE, 'R', GC.NO_MASK);
		menu.add(miRep);
		menu.addSeparator();
		menu.add(newCommonMenuItem(GC.iOPTIONS, GC.OPTIONS, 'O', GC.NO_MASK,
				true));
		return menu;
	}

	/**
	 * ȡ���пɱ�״̬�Ĳ˵���
	 */
	public short[] getMenuItems() {
		short[] menus = new short[] {};
		return menus;
	}

	/**
	 * ����Դ���Ӻ�
	 */
	public void dataSourceConnected() {
		if (GVSpl.tabParam != null)
			GVSpl.tabParam.resetEnv();
	}
}
