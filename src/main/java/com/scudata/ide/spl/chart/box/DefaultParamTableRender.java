package com.scudata.ide.spl.chart.box;

import javax.swing.table.*;

import com.scudata.ide.common.swing.*;

import java.awt.*;
import javax.swing.*;

/**
 * ȱʡ��������Ⱦ��
 * 
 * @author Joancy
 *
 */
public class DefaultParamTableRender implements TableCellRenderer {
	private DefaultTableCellRenderer expRender, groupRender;
	private TableCellRenderer renderer;
	private int align = JLabel.LEFT;

	/**
	 * ����һ��ȱʡ��������Ⱦ��
	 */
	public DefaultParamTableRender() {
		this( JLabel.LEFT );
	}

	/**
	 * ����һ��ȱʡ��������Ⱦ��
	 * @param align �ı����뷽ʽ
	 */
	public DefaultParamTableRender( int align ) {
		this.align = align;
		expRender = new DefaultTableCellRenderer();
		groupRender = new DefaultTableCellRenderer();
		groupRender.setForeground( Color.blue.darker().darker() );
		groupRender.setBackground( new Color( 240, 240, 240 ) );
	}

	/**
	 * ʵ�ָ�����󷽷�
	 */
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
		renderer = expRender;
		try {
			if ( ( ( JTableEx ) table ).data.getValueAt( row, 6 ) == null ) {
				renderer = groupRender;
			}
			else {
				renderer = expRender;
			}
		}catch( Throwable t ) {}
		JLabel label = (JLabel) renderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		label.setHorizontalAlignment( align );
		return label;
	}
}
