package com.scudata.ide.starter;

import java.awt.event.ActionEvent;

import com.scudata.ide.common.ConfigMenuAction;
import com.scudata.ide.common.dialog.DialogDemoFiles;

/**
 * ����������
 * 
 * @author wunan
 *
 */
public class SampleProgramListener extends ConfigMenuAction {

	public void actionPerformed(ActionEvent arg0) {
		new DialogDemoFiles().setVisible(true);
	}
}