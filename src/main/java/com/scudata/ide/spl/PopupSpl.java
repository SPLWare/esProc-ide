package com.scudata.ide.spl;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuListener;

import com.scudata.ide.common.GV;

/**
 * �Ҽ������˵�
 *
 */
public class PopupSpl {
	/**
	 * �����˵�������
	 */
	PopupMenuListener listener = null;

	/**
	 * ���캯��
	 */
	public PopupSpl() {
	}

	/**
	 * ���ӵ����˵�������
	 * 
	 * @param listener
	 */
	public void addPopupMenuListener(PopupMenuListener listener) {
		this.listener = listener;
	}

	/**
	 * ȡ�Ҽ������˵�
	 * 
	 * @param selectStatus
	 *            ����ѡ���״̬��GCSpl�ж���ĳ���
	 * @return
	 */
	public JPopupMenu getSplPop(byte selectStatus) {
		MenuSpl mSpl = (MenuSpl) GV.appMenu;
		JPopupMenu pm = new JPopupMenu();
		pm.add(mSpl.cloneMenuItem(GCSpl.iCUT));
		pm.add(mSpl.cloneMenuItem(GCSpl.iCOPY));
		pm.add(mSpl.cloneMenuItem(GCSpl.iPASTE));
		pm.addSeparator();

		switch (selectStatus) {
		case GCSpl.SELECT_STATE_CELL:
			pm.add(mSpl.cloneMenuItem(GCSpl.iCTRL_ENTER));
			pm.add(mSpl.cloneMenuItem(GCSpl.iDUP_ROW));
			pm.add(mSpl.cloneMenuItem(GCSpl.iDUP_ROW_ADJUST));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iFULL_CLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR_VALUE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTEXT_EDITOR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iNOTE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTIPS));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iEDIT_CHART));

			pm.add(mSpl.cloneMenuItem(GCSpl.iDRAW_CHART));
			JMenuItem calcArea = mSpl.cloneMenuItem(GCSpl.iCALC_AREA);
			calcArea.setVisible(true);
			pm.add(calcArea);
			break;
		case GCSpl.SELECT_STATE_COL:
			pm.add(mSpl.cloneMenuItem(GCSpl.iINSERT_COL));
			pm.add(mSpl.cloneMenuItem(GCSpl.iADD_COL));
			pm.add(mSpl.cloneMenuItem(GCSpl.iDELETE_COL));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iFULL_CLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR_VALUE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTEXT_EDITOR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iNOTE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTIPS));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iCOL_WIDTH));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCOL_HIDE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCOL_VISIBLE));
			break;
		case GCSpl.SELECT_STATE_ROW:
			pm.add(mSpl.cloneMenuItem(GCSpl.iCTRL_ENTER));
			pm.add(mSpl.cloneMenuItem(GCSpl.iDELETE_ROW));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iFULL_CLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR_VALUE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTEXT_EDITOR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iNOTE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTIPS));
			pm.addSeparator();
			pm.add(mSpl.cloneMenuItem(GCSpl.iROW_HEIGHT));
			pm.add(mSpl.cloneMenuItem(GCSpl.iROW_HIDE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iROW_VISIBLE));
			break;
		default:
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iFULL_CLEAR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iCLEAR_VALUE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTEXT_EDITOR));
			pm.add(mSpl.cloneMenuItem(GCSpl.iNOTE));
			pm.add(mSpl.cloneMenuItem(GCSpl.iTIPS));
			break;
		}
		if (listener != null) {
			pm.addPopupMenuListener(listener);
		}
		return pm;
	}
}
