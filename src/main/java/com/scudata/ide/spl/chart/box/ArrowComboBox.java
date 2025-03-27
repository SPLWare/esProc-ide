package com.scudata.ide.spl.chart.box;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.scudata.chart.*;
import com.scudata.ide.common.swing.*;

/**
 * ֱ�߼�ͷ�����б�
 * 
 * @author Joancy
 *
 */
public class ArrowComboBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * ����һ�������б�
	 * @param isSimpleArrow �Ƿ���༭�򵥼�ͷ
	 * �򵥼�ͷʱ�����������֣���������������ͼ��ָ��
	 * �����ж��ּ�ͷ����
	 */
	public ArrowComboBox(boolean isSimpleArrow) {
		Object[] vals;
		if(isSimpleArrow){
			vals = new Object[] {
					   new Integer( Consts.LINE_ARROW ),
					   new Integer( Consts.LINE_ARROW_L ),
					   new Integer( Consts.LINE_ARROW_NONE )
				};	
		}else{
			vals = new Object[] {
					   new Integer( Consts.LINE_ARROW ),
					   new Integer( Consts.LINE_ARROW_L ),
					   new Integer( Consts.LINE_ARROW_BOTH ),
					   new Integer( Consts.LINE_ARROW_HEART ),
					   new Integer( Consts.LINE_ARROW_CIRCEL ),
					   new Integer( Consts.LINE_ARROW_DIAMOND ),
					   new Integer( Consts.LINE_ARROW_NONE )
				};	
		}
		for(Object item:vals){
			addItem( item );
		}
		this.setPreferredSize( new Dimension( 80, 25 ) );
		setEditor( new ArrowComboBoxEditor() );
		setRenderer( new ArrowRender() );
	}

	/**
	 * ��ȡ����ֵ
	 * @return ��ͷ������ֵ
	 */
	public int getValue() {
		return ( ( Integer ) getEditor().getItem() ).intValue();
	}

	class ArrowComboBoxEditor extends AbstractComboBoxEditor {
		ArrowIcon editorIcon = new ArrowIcon();
		JLabel editorLabel = new JLabel( editorIcon );
		Object item = new Object();

		public ArrowComboBoxEditor() {
			editorLabel.setBorder( BorderFactory.createEtchedBorder() );
		}

		public Component getEditorComponent() {
			return editorLabel;
		}

		public Object getItem() {
			return item;
		}

		public void setItem( Object itemToSet ) {
			item = itemToSet;
			editorIcon.setIconType( ( ( Integer ) itemToSet ).intValue() );
			editorLabel.setText( " " );
		}

		public void selectAll() {
			// from ComboBoxModel interface:  nothing to select
		}
	}

}

class ArrowRender extends JLabel implements ListCellRenderer, TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrowIcon icon = new ArrowIcon();

	public ArrowRender() {
		setOpaque( true );
	}

	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus ) {

		setText( " " );

		if ( isSelected ) {
			setForeground( list.getSelectionForeground() );
			setBackground( list.getSelectionBackground() );
		}
		else {
			setForeground( list.getForeground() );
			setBackground( list.getBackground() );
		}
		int w = list.getWidth();
		if( w < 30 ) w = 80;
		this.setPreferredSize( new Dimension( w - 20, 25 ) );
		icon.setWidth( w - 25 );
		icon.setIconType( ( ( Integer ) value ).intValue() );
		setIcon( icon );
		return this;
	}

	/**
	 * ʵ�ָ�����󷽷�
	 */
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
		setText( " " );

		if ( isSelected ) {
			setForeground( table.getSelectionForeground() );
			setBackground( table.getSelectionBackground() );
		}
		else {
			setForeground( table.getForeground() );
			setBackground( table.getBackground() );
		}
		int w = ((JTableEx)table).getColumn( column ).getWidth();
		this.setPreferredSize( new Dimension( w, 25 ) );
		icon.setWidth( w - 5 );
		icon.setIconType( ( ( Integer ) value ).intValue() );
		setIcon( icon );
		return this;
	}
}

/**
 * ��ͷͼ�������
 * 
 * @author Joancy
 *
 */
class ArrowIcon implements Icon {
	private int width;
	private int type;

	public ArrowIcon() {
		this( Consts.LINE_ARROW );
	}

	public ArrowIcon( int type ) {
		this.type = type;
	}

	public void paintIcon( Component c, Graphics g, int x, int y ) {
		g.setColor( Color.black );
		if( type != Consts.LINE_ARROW_NONE ) {
			int x1 = x+15;
			int y1 = y+5;
			int x2 = x+width-15;
			int y2 = y1;
			g.drawLine( x1, y1, x2, y2 );
			if(type==Consts.LINE_ARROW_L){//���ͷʱ��������˵�
				Utils.drawLineArrow( ( Graphics2D ) g, x1, y1, 0, type );
				return;
			}
			Utils.drawLineArrow( ( Graphics2D ) g, x2, y2, 0, type );
		}
	}

	public int getIconWidth() {
		return width + 5;
	}

	public int getIconHeight() {
		return 10;
	}

	public void setIconType( int type ) {
		this.type = type;
	}

	public void setWidth( int w ) {
		this.width = w;
	}

}

