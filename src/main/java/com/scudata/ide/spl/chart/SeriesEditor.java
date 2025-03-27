package com.scudata.ide.spl.chart;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.scudata.chart.edit.ParamInfo;
import com.scudata.ide.common.*;

/**
 * ��������ֵ�ı��༭��
 * 
 * @author Joancy
 *
 */
public class SeriesEditor extends DefaultCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button = new JButton();
	private TableParamEdit paramTable;
	private int currRow;
	private Dialog owner;
	
	public static ImageIcon enabledIcon = GM.getImageIcon( GC.IMAGES_PATH + "m_pmtredo.gif" );

	/**
	 * ���캯��
	 * @param owner ������
	 */
	public SeriesEditor( Dialog owner ) {
		super( new JCheckBox() );
		this.owner = owner;
		button.setHorizontalAlignment( JButton.CENTER );
		button.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				clicked();
			}
		} );
	}

	protected void clicked() {
		Object icon = button.getIcon();
		if(icon==null){
			return;
		}
		paramTable.seriesTable.addParam2Edit( currRow, owner );
	}

	/**
	 * ʵ�ֱ༭���ĳ��󷽷�
	 */
	public Component getTableCellEditorComponent( JTable table, Object value,
												  boolean isSelected, int row, int column ) {
		paramTable = ( TableParamEdit ) table;
		currRow = row;
		if ( isSelected ) {
			button.setBackground( table.getSelectionBackground() );
		}
		else {
			button.setBackground( table.getBackground() );
		}
		ParamInfo pi = (ParamInfo)paramTable.data.getValueAt(row, TableParamEdit.iOBJCOL);
		if(pi!=null && pi.isAxisEnable()){
			button.setIcon( enabledIcon );
		}else{
			button.setIcon( null );
			button.setBackground( new Color( 240, 240, 240 ) );
		}

		return button;
	}

	/**
	 * ��ȡ�༭ֵ
	 * @return null
	 */
	public Object getCellEditorValue() {
		return null;
	}

	/**
	 * ֹͣ�༭
	 */
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}

