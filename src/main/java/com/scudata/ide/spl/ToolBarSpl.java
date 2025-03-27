package com.scudata.ide.spl;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import com.scudata.common.StringUtils;
import com.scudata.ide.common.AppToolBar;
import com.scudata.ide.common.GC;
import com.scudata.ide.common.GM;
import com.scudata.ide.common.GV;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ��������������ҳ��򿪺�
 *
 */
public class ToolBarSpl extends AppToolBar {
	private static final long serialVersionUID = 1L;

	/**
	 * ��ͣ��ť
	 */
	private JButton pauseButton;

	/**
	 * ���캯��
	 */
	public ToolBarSpl() {
		super();
		add(getCommonButton(GC.iNEW, GC.NEW));
		add(getCommonButton(GC.iOPEN, GC.OPEN));
		add(getCommonButton(GCSpl.iSAVE, GCSpl.SAVE));
		addSeparator(seperator);
		add(getSplButton(GCSpl.iEXEC, GCSpl.EXEC));
		JButton b;
		b = getSplButton(GCSpl.iEXE_DEBUG, GCSpl.EXE_DEBUG);
		add(b);
		addSeparator(seperator);
		b = getSplButton(GCSpl.iSTEP_CURSOR, GCSpl.STEP_CURSOR);
		add(b);
		b = getSplButton(GCSpl.iSTEP_NEXT, GCSpl.STEP_NEXT);
		add(b);
		b = getSplButton(GCSpl.iSTEP_INTO, GCSpl.STEP_INTO);
		add(b);
		b = getSplButton(GCSpl.iSTEP_RETURN, GCSpl.STEP_RETURN);
		add(b);
		b = getSplButton(GCSpl.iSTEP_STOP, GCSpl.STEP_STOP);
		add(b);
		pauseButton = getSplButton(GCSpl.iPAUSE, GCSpl.PAUSE);
		add(pauseButton);
		b = getSplButton(GCSpl.iSTOP, GCSpl.STOP);
		add(b);
		b = getSplButton(GCSpl.iBREAKPOINTS, GCSpl.BREAKPOINTS);
		add(b);
		addSeparator(seperator);
		add(getSplButton(GCSpl.iCALC_AREA, GCSpl.CALC_AREA));
		add(getSplButton(GCSpl.iCLEAR, GCSpl.CLEAR));
		add(getSplButton(GCSpl.iUNDO, GCSpl.UNDO));
		add(getSplButton(GCSpl.iREDO, GCSpl.REDO));
		setBarEnabled(false);
		this.setFocusable(false);
	}

	/**
	 * ���ù������Ƿ����
	 */
	public void setBarEnabled(boolean enabled) {
		setButtonEnabled(GCSpl.iSAVE, enabled);
		setButtonEnabled(GCSpl.iEXEC, enabled);
		setButtonEnabled(GCSpl.iEXE_DEBUG, enabled);
		setButtonEnabled(GCSpl.iSTEP_NEXT, enabled);
		setButtonEnabled(GCSpl.iSTEP_CURSOR, enabled);
		setButtonEnabled(GCSpl.iPAUSE, enabled);
		setButtonEnabled(GCSpl.iSTOP, enabled);
		setButtonEnabled(GCSpl.iBREAKPOINTS, enabled);
		setButtonEnabled(GCSpl.iCALC_AREA, enabled);
		setButtonEnabled(GCSpl.iCLEAR, enabled);
		setButtonEnabled(GCSpl.iREDO, enabled);
		setButtonEnabled(GCSpl.iUNDO, enabled);
	}

	/**
	 * ��ť���������
	 */
	private ActionListener actionNormal = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String menuId = "";
			try {
				menuId = e.getActionCommand();
				short cmdId = Short.parseShort(menuId);
				GMSpl.executeCmd(cmdId);
			} catch (Exception ex) {
				GM.showException(GV.appFrame, ex);
			}
		}
	};

	/**
	 * �½���ť
	 * 
	 * @param cmdId  ��GCSpl�ж��������
	 * @param menuId ��GCSpl�ж���Ĳ˵���
	 * @return
	 */
	private JButton getSplButton(short cmdId, String menuId) {
		JButton b = getButton(cmdId, menuId,
				IdeSplMessage.get().getMessage(GC.MENU + menuId), actionNormal);
		buttonHolder.put(cmdId, b);
		b.setFocusable(false);
		return b;
	}

	/**
	 * �½���̬����
	 * 
	 * @param cmdId   ��GCSpl�ж��������
	 * @param menuId  ��GCSpl�ж���Ĳ˵���
	 * @param toolTip ��ʾ��Ϣ
	 * @return
	 */
	public JToggleButton getToggleButton(short cmdId, String menuId,
			String toolTip) {
		ImageIcon img = GM.getMenuImageIcon(menuId);
		JToggleButton b = new JToggleButton(img);
		b.setOpaque(false);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setContentAreaFilled(false);
		if (StringUtils.isValidString(toolTip)) {
			int index = toolTip.indexOf("(");
			if (index > 0) {
				toolTip = toolTip.substring(0, index);
			}
		}
		b.setToolTipText(toolTip);
		b.setActionCommand(Short.toString(cmdId));
		b.addActionListener(actionNormal);
		Dimension d = new Dimension(28, 28);
		b.setPreferredSize(d);
		b.setMaximumSize(d);
		b.setMinimumSize(d);
		buttonHolder.put(cmdId, b);
		b.setFocusable(false);
		return b;
	}

	/**
	 * ִ�а�������
	 */
	public void executeCmd(short cmdId) {
		try {
			GMSpl.executeCmd(cmdId);
		} catch (Exception e) {
			GM.showException(GV.appFrame, e);
		}
	}

	/** ����ִ�� */
	private final String S_CONTINUE = IdeSplMessage.get().getMessage(
			"menu.program.continue");
	/** ��ͣ */
	private final String S_PAUSE = IdeSplMessage.get().getMessage(
			"menu.program.pause");
	/** ����ִ��ͼ�� */
	private final ImageIcon I_CONTINUE = GM.getMenuImageIcon(GCSpl.CONTINUE);
	/** ��ͣͼ�� */
	private final ImageIcon I_PAUSE = GM.getMenuImageIcon(GCSpl.PAUSE);

	/**
	 * ������ͣ�������ı���ͼ��
	 * 
	 * @param isPause
	 */
	public void resetPauseButton(boolean isPause) {
		if (isPause) {
			pauseButton.setIcon(I_CONTINUE);
			pauseButton.setToolTipText(S_CONTINUE);
		} else {
			pauseButton.setIcon(I_PAUSE);
			pauseButton.setToolTipText(S_PAUSE);
		}
	}
}
