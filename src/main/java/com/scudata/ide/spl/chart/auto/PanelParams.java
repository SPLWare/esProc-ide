package com.scudata.ide.spl.chart.auto;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import com.scudata.chart.edit.*;
import com.scudata.ide.spl.chart.TableParamEdit;

import java.awt.*;

/**
 * �����༭���
 * 
 * @author Joancy
 *
 */
public abstract class PanelParams extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private TableParamEdit table;
	ParamInfoList infoList;
	private Dialog owner;

	/**
	 * ���ò�����Ϣ
	 * @param info ������Ϣ
	 */
	public void setElementInfo( ElementInfo info ) {
		init( owner, info.getParamInfoList() );
	}

	/**
	 * ���ò�����Ϣ�б�
	 * @param owner ������
	 * @param list ������Ϣ�б�
	 */
	public void setParamInfoList( Dialog owner, ParamInfoList list ) {
		init( owner, list );
	}

	/**
	 * ���캯��
	 * @param owner ������
	 */
	public PanelParams( Dialog owner ) {
		this.owner = owner;
	}
	
	public abstract void refresh();

	/**
	 * չ�����в���
	 */
	public void expandAll(){
		table.expandAll();
	}
	
	/**
	 * �������в���
	 */
	public void collapseAll(){
		table.collapseAll();
	}

	private void init( Dialog owner, ParamInfoList list ) {
		this.infoList = list;
		setLayout( new BorderLayout() );
//		�����ɽ���ƴ��
		list.delete("categories");
		list.delete("values");
		table = new TableParamEdit( owner, list ){
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JTextField){
					JTextField tf = (JTextField)e.getSource();
					String txt = tf.getText();
					table.setValueAt(txt, table.getEditingRow(), table.getEditingColumn());
				}else if(e.getSource() instanceof JSpinner){
					JSpinner sp = (JSpinner)e.getSource();
					Object obj = sp.getValue();
					table.setValueAt(obj, table.getEditingRow(), table.getEditingColumn());
				}
				Thread t = new Thread(){
					public void run(){
						refresh();
					}
				};
				t.start();
			}
		};
		table.autoHide();
		add( new JScrollPane( table ),BorderLayout.CENTER);
	}

	/**
	 * ��ȡ�����༭��
	 * @return ������
	 */
	public TableParamEdit getParamTable() {
		return table;
	}

	/**
	 * ��ȡ������Ϣ�б�
	 * @return ������Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		table.acceptText();
		return infoList;
	}

}
