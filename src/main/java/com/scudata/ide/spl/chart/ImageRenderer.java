package com.scudata.ide.spl.chart;

import javax.swing.*;
import javax.swing.table.*;

import com.scudata.ide.common.*;

import java.awt.*;

/**
 * ��ӦImageEditor����Ⱦ��
 * 
 * @author Joancy
 *
 */
public class ImageRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7297185433633526409L;

	/**
	 * ȱʡ���캯��
	 */
	public ImageRenderer() {
		setHorizontalAlignment( JLabel.CENTER );
	}

	/**
	 * ʵ�ָ�����󷽷�������״̬������ѡ�ò�ͬͼƬ��ʾ
	 */
	public Component getTableCellRendererComponent( JTable table, Object value,
		boolean isSelected,
		boolean hasFocus,
		int row, int column ) {
		if ( isSelected ) {
			setForeground( table.getSelectionForeground() );
			setBackground( table.getSelectionBackground() );
		}
		else {
			setForeground( table.getForeground() );
			setBackground( table.getBackground() );
		}
		setBorder( BorderFactory.createEmptyBorder() );
		if ( value != null && value instanceof Byte ) {
			String path = GC.IMAGES_PATH;
			switch ( ( ( Byte ) value ).byteValue() ) {
				case GC.TYPE_EMPTY:
					return this;
				case GC.TYPE_PLUS:
					path += "plus.gif";
					break;
				case GC.TYPE_MINUS:
					path += "minus.gif";
					break;
				case GC.TYPE_NODE:
					path += "node.gif";
					break;
				case GC.TYPE_LASTPLUS:
					path += "lastplus.gif";
					break;
				case GC.TYPE_LASTMINUS:
					path += "lastminus.gif";
					break;
				case GC.TYPE_LASTNODE:
					path += "lastnode.gif";
					break;
			}
			ImageIcon icon = GM.getImageIcon( path );
			setIcon( icon );
		}
		else setIcon( null );
		return this;
	}
}
