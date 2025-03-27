package com.scudata.ide.spl.control;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import com.scudata.ide.common.GV;
import com.scudata.ide.spl.SPL;

/**
 * ���񰴼�������������ʵ��CTRL-TAB���л�����һ������
 *
 */
public class SplKeyListener implements AWTEventListener {

	/**
	 * CTRL���Ƿ�����
	 */
	private boolean isCtrlDown = false;

	/**
	 * �����¼�
	 */
	public void eventDispatched(AWTEvent event) {
		if (event.getClass() == KeyEvent.class) {
			KeyEvent keyEvent = (KeyEvent) event;
			if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
				if (keyEvent.isControlDown()
						&& keyEvent.getKeyCode() == KeyEvent.VK_TAB) {
					((SPL) GV.appFrame).showNextSheet(isCtrlDown);
					isCtrlDown = true;
				}
			} else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_CONTROL) {
					isCtrlDown = false;
				}
			}
		}
	}

}
