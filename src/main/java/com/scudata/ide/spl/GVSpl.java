package com.scudata.ide.spl;

import com.scudata.ide.common.AppMenu;
import com.scudata.ide.common.AppToolBar;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.ToolBarPropertyBase;
import com.scudata.ide.spl.base.JTabbedParam;
import com.scudata.ide.spl.base.PanelSplWatch;
import com.scudata.ide.spl.base.PanelValue;
import com.scudata.ide.spl.control.SplEditor;
import com.scudata.ide.spl.dialog.DialogSearch;

/**
 * ������IDE�еĳ���
 *
 */
public class GVSpl extends GV {
	/**
	 * ����༭��
	 */
	public static SplEditor splEditor = null;

	/**
	 * IDE���½ǵĶ��ǩ�ؼ�,��������������ʽ�ȱ�ǩҳ
	 */
	public static JTabbedParam tabParam = null;

	/**
	 * ��Ԫ��ֵ���
	 */
	public static PanelValue panelValue = null;

	/**
	 * ������ʽ�������
	 */
	public static PanelSplWatch panelSplWatch = null;

	/**
	 * �����Ի���
	 */
	public static DialogSearch searchDialog = null;

	/**
	 * ȡ�������˵�
	 * 
	 * @return
	 */
	public static AppMenu newSplMenu() {
		appMenu = new MenuSpl();
		return appMenu;
	}

	/**
	 * ȡ������������
	 * 
	 * @return
	 */
	public static ToolBarSpl newSplTool() {
		appTool = new ToolBarSpl();
		return (ToolBarSpl) appTool;
	}

	/**
	 * ȡ���Թ�����
	 * 
	 * @return
	 */
	public static ToolBarPropertyBase newSplProperty() {
		toolBarProperty = new ToolBarProperty();
		return toolBarProperty;
	}

	/**
	 * ȡ�����˵������ļ���ʱ��
	 * 
	 * @return
	 */
	public static AppMenu newBaseMenu() {
		appMenu = new MenuBase();
		return appMenu;
	}

	/**
	 * ȡ���������������ļ���ʱ��
	 * 
	 * @return
	 */
	public static AppToolBar newBaseTool() {
		appTool = new ToolBarBase();
		return appTool;
	}

}
