package com.scudata.ide.spl.base;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.scudata.common.MessageManager;
import com.scudata.dm.Param;
import com.scudata.dm.ParamList;
import com.scudata.ide.common.DataSource;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.common.control.PanelConsole;
import com.scudata.ide.common.resources.IdeCommonMessage;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * IDE���½ǵĶ��ǩҳ��塣�б��������ʽ�ȱ�ǩҳ
 *
 */
public abstract class JTabbedParam extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	/**
	 * Common��Դ������
	 */
	private MessageManager mm = IdeCommonMessage.get();

	/**
	 * �������
	 */
	private final String STR_CS_VAR = mm.getMessage("jtabbedparam.csvar");

	/**
	 * ����ռ����
	 */
	private final String STR_SPACE_VAR = mm.getMessage("jtabbedparam.spacevar");

	/**
	 * ȫ�ֱ���
	 */
	private final String STR_GB_VAR = mm.getMessage("jtabbedparam.globalvar");

	/**
	 * �鿴���ʽ
	 */
	private final String STR_WATCH = mm.getMessage("jtabbedparam.watch");

	/**
	 * ����Դ
	 */
	private final String STR_DB = mm.getMessage("jtabbedparam.db");

	/**
	 * ���
	 */
	private final String STR_CONSOLE = IdeSplMessage.get().getMessage(
			"dfx.tabconsole");

	private JPanel jPCsVar = new JPanel(new GridBagLayout());
	private JPanel jPSpaceVar = new JPanel(new GridBagLayout());
	private JPanel jPGbVar = new JPanel(new GridBagLayout());

	private JButton jBCsRefresh = new JButton(IdeSplMessage.get().getMessage(
			"public.refresh"));
	private JButton jBSpaceRefresh = new JButton(IdeSplMessage.get()
			.getMessage("public.refresh"));
	private JButton jBGbRefresh = new JButton(IdeSplMessage.get().getMessage(
			"public.refresh"));

	/**
	 * ������ؼ�
	 */
	private TableVar tableCsVar = new TableVar() {
		private static final long serialVersionUID = 1L;

		public void select(Object val, String varName) {
			selectVar(val, varName, null);
		}
	};

	/**
	 * ����ռ������ؼ�
	 */
	private JTableJobSpace tableSpaceVar = new JTableJobSpace() {
		private static final long serialVersionUID = 1L;

		public void select(Object val, String varName, String spaceName) {
			selectVar(val, varName, spaceName);
		}
	};

	/**
	 * ȫ�ֱ�����ؼ�
	 */
	private TableVar tableGbVar = new TableVar() {
		private static final long serialVersionUID = 1L;

		public void select(Object val, String varName) {
			selectVar(val, varName, null);
		}
	};

	/**
	 * ѡ���ֶ����
	 */
	private PanelSelectField psf = new PanelSelectField();
	/**
	 * �������
	 */
	private PanelConsole panelConsole;

	/**
	 * ���캯��
	 */
	public JTabbedParam() {
		try {
			initUI();
			resetEnv();
		} catch (Exception e) {
			GM.showException(GV.appFrame, e);
		}
	}

	/**
	 * ���û���
	 */
	public void resetEnv() {
		boolean allClosed = true;
		if (GV.dsModel != null) {
			DataSource ds;
			for (int i = 0; i < GV.dsModel.getSize(); i++) {
				ds = (DataSource) GV.dsModel.get(i);
				if (ds != null && !ds.isClosed()) {
					allClosed = false;
					break;
				}
			}
		}
		int index = getTabIndex(STR_DB);
		if (allClosed) {
			if (index > -1) {
				this.remove(index);
			}
		} else {
			if (index < 0) {
				addTab(STR_DB, psf);
			}
			psf.resetEnv();
		}
	}

	/**
	 * ȡ������
	 * 
	 * @return
	 */
	public PanelConsole getPanelConsole() {
		return panelConsole;
	}

	/**
	 * �����������Ƿ����
	 * 
	 * @param isVisible
	 */
	public void consoleVisible(boolean isVisible) {
		int index = getTabIndex(STR_CONSOLE);
		if (isVisible) {
			if (index < 0) {
				if (panelConsole == null) {
					panelConsole = new PanelConsole(GV.console, true);
				} else {
					GV.console.clear();
				}
				addTab(STR_CONSOLE, panelConsole);
			}
			showConsoleTab();
		} else {
			if (index > -1) {
				this.remove(index);
			}
		}
	}

	/**
	 * ��ʾ���ҳ
	 */
	public void showConsoleTab() {
		int index = getTabIndex(STR_CONSOLE);
		if (index > -1)
			this.setSelectedIndex(index);
	}

	/**
	 * ȡָ�����Ʊ�ǩ�����
	 * 
	 * @param tabName
	 *            ��ǩ����
	 * @return
	 */
	private int getTabIndex(String tabName) {
		int count = getTabCount();
		for (int i = 0; i < count; i++) {
			if (getTitleAt(i).equals(tabName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ѡ�����
	 * 
	 * @param val
	 * @param varName
	 */
	public abstract void selectVar(Object val, String varName, String spaceName);

	public abstract ParamList getCellSetParamList();

	public abstract HashMap<String, Param[]> getSpaceParams();

	public abstract ParamList getEnvParamList();

	/**
	 * ���ò����б�
	 * 
	 * @param pl �����б�
	 */
	public void resetParamList(ParamList paramList,
			HashMap<String, Param[]> hm, ParamList envParamList) {
		tableCsVar.setParamList(paramList);
		tableSpaceVar.setJobSpaces(hm);
		tableGbVar.setParamList(envParamList);
	}

	private void initUI() {
		this.setMinimumSize(new Dimension(1, 1));
		GridBagConstraints gbc;
		jPCsVar.add(new JPanel(), GM.getGBC(0, 0, true));
		jPCsVar.add(jBCsRefresh, GM.getGBC(0, 1));
		gbc = GM.getGBC(1, 0, true, true);
		gbc.gridwidth = 2;
		jPCsVar.add(tableCsVar, gbc);

		jPSpaceVar.add(new JPanel(), GM.getGBC(0, 0, true));
		jPSpaceVar.add(jBSpaceRefresh, GM.getGBC(0, 1));
		gbc = GM.getGBC(1, 0, true, true);
		gbc.gridwidth = 2;
		jPSpaceVar.add(tableSpaceVar, gbc);

		jPGbVar.add(new JPanel(), GM.getGBC(0, 0, true));
		jPGbVar.add(jBGbRefresh, GM.getGBC(0, 1));
		gbc = GM.getGBC(1, 0, true, true);
		gbc.gridwidth = 2;
		jPGbVar.add(tableGbVar, gbc);

		addTab(STR_CS_VAR, jPCsVar);
		addTab(STR_SPACE_VAR, jPSpaceVar);
		addTab(STR_GB_VAR, jPGbVar);
		addTab(STR_WATCH, GVSpl.panelSplWatch);

		jBCsRefresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ParamList pl = getCellSetParamList();
				tableCsVar.setParamList(pl);
			}
		});

		jBSpaceRefresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				HashMap<String, Param[]> hm = getSpaceParams();
				tableSpaceVar.setJobSpaces(hm);
			}
		});

		jBGbRefresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ParamList pl = getEnvParamList();
				tableGbVar.setParamList(pl);
			}
		});
	}
}
