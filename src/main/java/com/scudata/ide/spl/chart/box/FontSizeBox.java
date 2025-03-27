package com.scudata.ide.spl.chart.box;

import java.util.*;

import com.scudata.app.common.Section;
import com.scudata.ide.common.*;
import com.scudata.ide.common.swing.*;

/**
 * �ֺ������б�
 * 
 * @author Joancy
 *
 */
public class FontSizeBox extends JComboBoxEx {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624791389366744866L;

	/**
	 * ȱʡ���캯��
	 */
	public FontSizeBox() {
		Vector<Integer> code = new Vector<Integer>();
		for ( int i = 0; i < GC.FONTSIZECODE.length; i++ ) {
			code.add( new Integer( GC.FONTSIZECODE[i].intValue() ) );
		}
		Section ss = new Section( GC.FONTSIZEDISP );
		Vector disp = ss.toVector();
		x_setData( code, disp );
	}

	/**
	 * ������ʾֵ��ȡ����ֵ
	 * @param dispItem ��ʾֵ
	 * @return ����ֵ
	 */
	public Object x_getCodeItem( Object dispItem ) {
		Object o = super.x_getCodeItem( dispItem );
		if ( o instanceof String ) {
			try {
				o = new Integer( o.toString() );
			}
			catch ( Exception e ) {
				o = new Integer( 12 );
			}
		}
		return o;
	}

}
