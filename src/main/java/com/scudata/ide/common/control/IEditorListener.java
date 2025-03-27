package com.scudata.ide.common.control;

import java.awt.Component;

/**
 * �༭�ؼ�������
 *
 */
public interface IEditorListener {
	/**
	 * �༭�ؼ���ѡ��״̬�����˱仯 ��������仯�û�Ӧ�����»�ȡ��ʾ������ˢ�²˵�����״̬����
	 * ���Ե���DMEditor.getDisplayProperty()��
	 * DMEditor.getDisplayExpression()�����ˢ�µ������б�
	 * 
	 * @param newState
	 *            byte���µ�״̬��ֵΪIDMEditor�����״̬
	 */
	public void selectStateChanged(byte newState, boolean keyEvent);

	/**
	 * �û��һ��¼��ķ��������ڵ�����ݲ˵�
	 * 
	 * @param invoker
	 *            JComponent��������ݲ˵��������ؼ�
	 * @param x
	 *            int�� ������
	 * @param y
	 *            int�� ������
	 */
	public void rightClicked(Component invoker, int x, int y);

	/**
	 * �û�������ִ���ˣ�����ˢ��Undo,Redo��Save�˵� ����DMEditor.canUndo()��DMEditor.canRedo()
	 * RepoertEditor.isDataChanged�����ж��Ƿ���Ҫ����
	 */
	public void commandExcuted();
}
