package com.scudata.ide.spl.chart;

import javax.swing.table.*;

import com.scudata.ide.common.*;

import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import javax.swing.*;
import java.awt.*;

/**
 * �������༭ʱ��ʹ�ø�ͼ��༭����ʾ�������շ�״̬
 * 
 * @author Joancy
 *
 */
public class ImageEditor implements TableCellEditor {
	/**
	 * ����һ��ͼ��༭��
	 */
	public ImageEditor() {
	}

	/**
	 * ʵ�ֱ��༭�ĳ��󷽷�
	 */
	public Component getTableCellEditorComponent( JTable table, Object value,
												  boolean isSelected, int row,
												  int column ) {
		JLabel label = new JLabel();
		if ( isSelected ) {
			label.setForeground( table.getSelectionForeground() );
			label.setBackground( table.getSelectionBackground() );
		}
		else {
			label.setForeground( table.getForeground() );
			label.setBackground( table.getBackground() );
		}
		label.setBorder( BorderFactory.createEmptyBorder() );
		if ( value != null && value instanceof Byte ) {
			String path = GC.IMAGES_PATH;
			switch ( ( ( Byte ) value ).byteValue() ) {
				case GC.TYPE_EMPTY:
					return label;
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
			label.setIcon( icon );
		}
		return label;
	}

	/**
	 * ȡ���༭��������
	 */
	public void cancelCellEditing() {
	}

	/**
	 * ֹͣ�༭
	 * @return false
	 */
	public boolean stopCellEditing() {
		return false;
	}

	/**
	 * ��ȡ�༭ֵ��������
	 */
	public Object getCellEditorValue() {
		return "";
	}

	/**
	 * �����Ƿ�ɱ༭
	 * @return false
	 */
	public boolean isCellEditable( EventObject anEvent ) {
		return false;
	}

	/**
	 * ѡ�и���
	 * @return false
	 */
	public boolean shouldSelectCell( EventObject anEvent ) {
		return false;
	}

	/**
	 * ���ӱ༭������
	 */
	public void addCellEditorListener( CellEditorListener l ) {
	}

	/**
	 * ɾ���༭������
	 */
	public void removeCellEditorListener( CellEditorListener l ) {
	}
}
