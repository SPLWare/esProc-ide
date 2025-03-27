package com.scudata.ide.spl;

import java.util.Vector;

import com.scudata.common.LimitedStack;
import com.scudata.ide.common.ConfigOptions;
import com.scudata.ide.common.IAtomicCmd;
import com.scudata.ide.common.control.IEditorListener;
import com.scudata.ide.spl.control.ControlUtils;
import com.scudata.ide.spl.control.EditControl;
import com.scudata.ide.spl.control.SplEditor;

/**
 * �������������Ĺ�����
 *
 */
public class UndoManager {
	/**
	 * ����ԭ����������
	 */
	private LimitedStack undoContainer = new LimitedStack(
			ConfigOptions.iUndoCount);
	/**
	 * ����ԭ����������
	 */
	private LimitedStack redoContainer = new LimitedStack(
			ConfigOptions.iUndoCount);
	/**
	 * ����༭��
	 */
	private SplEditor mEditor;
	/**
	 * ����ؼ�
	 */
	private EditControl editControl;

	/**
	 * ���캯��
	 * 
	 * @param editor ����༭��
	 */
	public UndoManager(SplEditor editor) {
		mEditor = editor;
		editControl = (EditControl) editor.getComponent();
	}

	/**
	 * �Ƿ���Գ���
	 * 
	 * @return
	 */
	public boolean canUndo() {
		return !undoContainer.empty();
	}

	/**
	 * �Ƿ��������
	 * 
	 * @return
	 */
	public boolean canRedo() {
		return !redoContainer.empty();
	}

	/**
	 * ����
	 */
	public void undo() {
		if (undoContainer.empty()) {
			return;
		}
		Vector<IAtomicCmd> cmds = (Vector<IAtomicCmd>) undoContainer.pop();
		executeCommands(cmds, redoContainer);
	}

	/**
	 * ����
	 */
	public void redo() {
		if (redoContainer.empty()) {
			return;
		}
		Vector<IAtomicCmd> cmds = (Vector<IAtomicCmd>) redoContainer.pop();
		executeCommands(cmds, undoContainer);
	}

	/**
	 * ִ��ԭ�����
	 *
	 * @param microCmds ��Ҫִ�е�ԭ�Ӽ�����
	 */
	public void doing(Vector<IAtomicCmd> microCmds) {
		if (microCmds == null || microCmds.size() == 0) {
			return;
		}
		redoContainer.clear();
		executeCommands(microCmds, undoContainer);
	}

	/**
	 * ִ��ԭ������
	 * 
	 * @param cmd
	 */
	public void doing(IAtomicCmd cmd) {
		redoContainer.clear();
		Vector<IAtomicCmd> v = new Vector<IAtomicCmd>();
		v.add(cmd);
		executeCommands(v, undoContainer);
	}

	/**
	 * �ָ�ԭ�����
	 * 
	 * @param v ԭ�����
	 * @return
	 */
	private Vector<IAtomicCmd> reverseVector(Vector<IAtomicCmd> v) {
		Vector<IAtomicCmd> rv = new Vector<IAtomicCmd>();
		for (int i = v.size() - 1; i >= 0; i--) {
			rv.add(v.get(i));
		}
		return rv;
	}

	/**
	 * ִ��ԭ�����
	 * 
	 * @param cmds  ԭ�����
	 * @param stack ��ջ
	 */
	private void executeCommands(Vector<IAtomicCmd> cmds, LimitedStack stack) {
		Vector<IAtomicCmd> vReverseCmds = new Vector<IAtomicCmd>();
		IAtomicCmd cmd, revCmd;

		boolean needRedraw = false;
		int newScale = 0;

		for (int i = 0; i < cmds.size(); i++) {
			cmd = (IAtomicCmd) cmds.get(i);
			revCmd = cmd.execute();
			vReverseCmds.add(revCmd);
		}
		editControl.resetCellSelection(null);
		SplEditor splEditor = ControlUtils.extractSplEditor(editControl);
		if (splEditor != null) {
			splEditor.resetSelectedAreas();
		}
		// Undo�����󣬹�����ڸ���ֵ�п��ܱ仯������װ�ظ�λ���ı�
		try {
			editControl.validate();
		} catch (Exception ex) {
		}
		if (needRedraw) {
			editControl.setDisplayScale(newScale);
		}

		stack.push(reverseVector(vReverseCmds));
		mEditor.setDataChanged(true);

		IEditorListener rel = mEditor.getSplListener();
		if (rel != null) {
			final Runnable delayExecute = new Runnable() {
				public void run() {
					IEditorListener rel1 = mEditor.getSplListener();
					rel1.commandExcuted();
				}
			};

			Thread appThread = new Thread() {
				public void run() {
					try {
						javax.swing.SwingUtilities.invokeLater(delayExecute);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			Thread.yield();
			appThread.start();
		}
	}
}
