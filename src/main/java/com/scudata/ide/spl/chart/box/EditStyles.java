package com.scudata.ide.spl.chart.box;

import java.util.*;

import com.scudata.app.common.*;
import com.scudata.cellset.graph.config.GraphTypes;
import com.scudata.cellset.graph.config.IGraphProperty;
import com.scudata.chart.*;
import com.scudata.common.MessageManager;
import com.scudata.ide.common.*;
import com.scudata.ide.common.swing.*;
import com.scudata.ide.spl.resources.*;

/**
 * �༭���ĸ����������󴴽�������
 * 
 * @author Joancy
 *
 */
public class EditStyles {
	static MessageManager mm = ChartMessage.get();
	/**
	 * �������������
	 * @return JComboBoxEx ���������б�
	 */
	public static JComboBoxEx getFontBox() {
		Vector<String> fontV = new Section( GM.getFontNames()).toVector();
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData(fontV, fontV);
		return box;
	}

	/**
	 * ��ÿ̶���������
	 * @return JComboBoxEx �̶��������б�
	 */
	public static JComboBoxEx getTicksBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.TICK_RIGHTUP ) );
		code.add( new Integer( Consts.TICK_LEFTDOWN ) );
		code.add( new Integer( Consts.TICK_CROSS ) );
		code.add( new Integer( Consts.TICK_NONE ) );
		disp.add( mm.getMessage( "options.ticks1" ) );  //"���һ���" );
		disp.add( mm.getMessage( "options.ticks2" ) );  //"�������" );
		disp.add( mm.getMessage( "options.ticks3" ) );  //"ѹ��" );
		disp.add( mm.getMessage( "options.ticks4" ) );  //"�޿̶���" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ������굥λ�����б�
	 * @return JComboBoxEx ���굥λ�����б�
	 */
	public static JComboBoxEx getUnitBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.NUNIT_NONE ) );
		code.add( new Integer( Consts.NUNIT_HUNDREDS ) );
		code.add( new Integer( Consts.NUNIT_THOUSANDS ) );
		code.add( new Integer( Consts.NUNIT_TEN_THOUSANDS ) );
		code.add( new Integer( Consts.NUNIT_HUNDRED_THOUSANDS ) );
		code.add( new Integer( Consts.NUNIT_MILLIONS ) );
		code.add( new Integer( Consts.NUNIT_TEN_MILLIONS ) );
		code.add( new Integer( Consts.NUNIT_HUNDRED_MILLIONS ) );
		code.add( new Integer( Consts.NUNIT_THOUSAND_MILLIONS ) );
		code.add( new Integer( Consts.NUNIT_BILLIONS ) );
		disp.add( mm.getMessage( "options.unit1" ) );  //"��" );
		disp.add( mm.getMessage( "options.unit2" ) );  //"��" );
		disp.add( mm.getMessage( "options.unit3" ) );  //"ǧ" );
		disp.add( mm.getMessage( "options.unit4" ) );  //"��" );
		disp.add( mm.getMessage( "options.unit5" ) );  //"ʮ��" );
		disp.add( mm.getMessage( "options.unit6" ) );  //"����" );
		disp.add( mm.getMessage( "options.unit7" ) );  //"ǧ��" );
		disp.add( mm.getMessage( "options.unit8" ) );  //"��" );
		disp.add( mm.getMessage( "options.unit9" ) );  //"ʮ��" );
		disp.add( mm.getMessage( "options.unit10" ) );  //"����" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * �������ϵ�����б�
	 * @return JComboBoxEx ����ϵ�����б�
	 */
	public static JComboBoxEx getCoordinateBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.COORDINATES_CARTESIAN ) );
		code.add( new Integer( Consts.COORDINATES_POLAR ) );
		code.add( new Integer( Consts.COORDINATES_CARTE_3D ) );
		code.add( new Integer( Consts.COORDINATES_CARTE_VIRTUAL_3D ) );
		code.add( new Integer( Consts.COORDINATES_POLAR_3D ) );
		code.add( new Integer( Consts.COORDINATES_POLAR_VIRTUAL_3D ) );
		code.add( new Integer( Consts.COORDINATES_LEGEND ) );
		code.add( new Integer( Consts.COORDINATES_FREE ) );
		disp.add( mm.getMessage( "options.coord1" ) );  //"ֱ������ϵ" );
		disp.add( mm.getMessage( "options.coord2" ) );  //"������ϵ" );
		disp.add( mm.getMessage( "options.coord3" ) );  //"����չ��ֱ������ϵ" );
		disp.add( mm.getMessage( "options.coord4" ) );  //"����Ч����ƽ��ֱ������ϵ" );
		disp.add( mm.getMessage( "options.coord5" ) );  //"����չ�ּ�����ϵ" );
		disp.add( mm.getMessage( "options.coord6" ) );  //"����Ч����ƽ�漫����ϵ" );
		disp.add( mm.getMessage( "options.coord7" ) );  //"ͼ������ϵ" );
		disp.add( mm.getMessage( "options.coord8" ) );  //"��������ϵ" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ���������λ�������б�
	 * @return JComboBoxEx ������λ�������б�
	 */
	public static JComboBoxEx getAxisBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.AXIS_LOC_H ) );
		code.add( new Integer( Consts.AXIS_LOC_V ) );
		code.add( new Integer( Consts.AXIS_LOC_POLAR ) );
		code.add( new Integer( Consts.AXIS_LOC_ANGLE ) );
		disp.add( mm.getMessage( "options.axis1" ) );  //"����" );
		disp.add( mm.getMessage( "options.axis2" ) );  //"����" );
		disp.add( mm.getMessage( "options.axis3" ) );  //"����" );
		disp.add( mm.getMessage( "options.axis4" ) );  //"����" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ������������б�
	 * @return JComboBoxEx ���������б�
	 */
	public static JComboBoxEx getColumnStyleBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.COL_COBOID ) );
		code.add( new Integer( Consts.COL_CUBE ) );
		code.add( new Integer( Consts.COL_CYLINDER ) );
		disp.add( mm.getMessage( "options.bar1" ) );  //"����" );
		disp.add( mm.getMessage( "options.bar2" ) );  //"���巽��" );
		disp.add( mm.getMessage( "options.bar3" ) );  //"Բ��" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ���ˮƽ���������б�
	 * @return JComboBoxEx ˮƽ���������б�
	 */
	public static JComboBoxEx getHAlignBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.HALIGN_LEFT ) );
		code.add( new Integer( Consts.HALIGN_CENTER ) );
		code.add( new Integer( Consts.HALIGN_RIGHT ) );
		disp.add( mm.getMessage( "options.align1" ) );  //"�����" );
		disp.add( mm.getMessage( "options.align2" ) );  //"�ж���" );
		disp.add( mm.getMessage( "options.align3" ) );  //"�Ҷ���" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ô�ֱ���������б�
	 * @return JComboBoxEx ��ֱ���������б�
	 */
	public static JComboBoxEx getVAlignBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.VALIGN_TOP ) );
		code.add( new Integer( Consts.VALIGN_MIDDLE ) );
		code.add( new Integer( Consts.VALIGN_BOTTOM ) );
		disp.add( mm.getMessage( "options.valign1" ) );  //"����" );
		disp.add( mm.getMessage( "options.valign2" ) );  //"����" );
		disp.add( mm.getMessage( "options.valign3" ) );  //"����" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ����ͼ���ģʽ�����б�
	 * @return ����ͼ���ģʽ�����б�
	 */
	public static JComboBoxEx getImageMode() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.MODE_NONE ) );
		code.add( new Integer( Consts.MODE_FILL ) );
		code.add( new Integer( Consts.MODE_TILE ) );
		disp.add( mm.getMessage( "options.modenone" ) );  //"����" );
		disp.add( mm.getMessage( "options.modefill" ) );  //"���" );
		disp.add( mm.getMessage( "options.modetile" ) );  //"ƽ��" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}
	
	/**
	 * ��ȡͼ����ͼ����״�����б�
	 * @return ͼ����ͼ����״�����б�
	 */
	public static JComboBoxEx getLegendIconBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.LEGEND_RECT ) );
		code.add( new Integer( Consts.LEGEND_POINT ) );
		code.add( new Integer( Consts.LEGEND_LINE ) );
		code.add( new Integer( Consts.LEGEND_LINEPOINT ) );
		code.add( new Integer( Consts.LEGEND_NONE ) );
		disp.add( mm.getMessage( "options.legendicon1" ) );  //"����" );
		disp.add( mm.getMessage( "options.legendicon2" ) );  //"����" );
		disp.add( mm.getMessage( "options.legendicon3" ) );  //"����" );
		disp.add( mm.getMessage( "options.legendicon4" ) );  //"����" );
		disp.add( mm.getMessage( "options.legendicon5" ) );  //"��" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ���ڵ�λ�����б�
	 * @return ���ڵ�λ�����б�
	 */
	public static JComboBoxEx getDateUnitBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.DATEUNIT_YEAR ) );
		code.add( new Integer( Consts.DATEUNIT_MONTH ) );
		code.add( new Integer( Consts.DATEUNIT_DAY ) );
		code.add( new Integer( Consts.DATEUNIT_HOUR ) );
		code.add( new Integer( Consts.DATEUNIT_MINUTE ) );
		code.add( new Integer( Consts.DATEUNIT_SECOND ) );
		code.add( new Integer( Consts.DATEUNIT_MILLISECOND ) );
		disp.add( mm.getMessage( "options.dateunit1" ) );  //"��" );
		disp.add( mm.getMessage( "options.dateunit2" ) );  //"��" );
		disp.add( mm.getMessage( "options.dateunit3" ) );  //"��" );
		disp.add( mm.getMessage( "options.dateunit4" ) );  //"ʱ" );
		disp.add( mm.getMessage( "options.dateunit5" ) );  //"��" );
		disp.add( mm.getMessage( "options.dateunit6" ) );  //"��" );
		disp.add( mm.getMessage( "options.dateunit7" ) );  //"����" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡURLĿ��ֵ�����б�
	 * @return URLĿ��ֵ�����б�
	 */
	public static JComboBoxEx getUrlTargetBox() {
		Vector<String> code = new Vector<String>();
		Vector<String> disp = new Vector<String>();
		code.add( "_self" );
		code.add( "_blank" );
		code.add( "_parent" );
		code.add( "_top" );
		disp.add( mm.getMessage( "options.urltarget1" ) );  //"������" );
		disp.add( mm.getMessage( "options.urltarget2" ) );  //"�´���" );
		disp.add( mm.getMessage( "options.urltarget3" ) );  //"������" );
		disp.add( mm.getMessage( "options.urltarget4" ) );  //"���㴰��" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		box.setEditable( true );
		return box;
	}

	/**
	 * �����ֵ�任���������б�
	 * @return JComboBoxEx ��ֵ�任���������б�
	 */
	public static JComboBoxEx getTransformBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.TRANSFORM_NONE ) );
		code.add( new Integer( Consts.TRANSFORM_SCALE ) );
		code.add( new Integer( Consts.TRANSFORM_LOG ) );
		code.add( new Integer( Consts.TRANSFORM_EXP ) );
		disp.add( mm.getMessage( "options.transform1" ) );  //"���任" );
		disp.add( mm.getMessage( "options.transform2" ) );  //"����" );
		disp.add( mm.getMessage( "options.transform3" ) );  //"����" );
		disp.add( mm.getMessage( "options.transform4" ) );  //"ָ��" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ�ѻ�ͼ���������б�
	 * @return �ѻ�ͼ���������б�
	 */
	public static JComboBoxEx getStackTypeBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.STACK_NONE ) );
		code.add( new Integer( Consts.STACK_PERCENT ) );
		code.add( new Integer( Consts.STACK_VALUE ) );
		disp.add( mm.getMessage( "options.stackNone" ) );  //"���ѻ�" );
		disp.add( mm.getMessage( "options.stackPercent" ) );  //"�ٷֱȶѻ�" );
		disp.add( mm.getMessage( "options.stackValue" ) );  //"ԭֵ�ѻ�" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ��ֵ��ʾ���������б�
	 * @return ��ֵ��ʾ���������б�
	 */
	public static JComboBoxEx getDisplayDataBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( IGraphProperty.DISPDATA_NONE) );
		code.add( new Integer( IGraphProperty.DISPDATA_PERCENTAGE) );
		code.add( new Integer( IGraphProperty.DISPDATA_VALUE) );
		code.add( new Integer( IGraphProperty.DISPDATA_NAME_PERCENTAGE) );
		code.add( new Integer( IGraphProperty.DISPDATA_NAME_VALUE) );
		disp.add( mm.getMessage( "options.dispDataNone" ) );  //"����ʾ
		disp.add( mm.getMessage( "options.dispDataPercent" ) );  //"�ٷֱ���ʾ
		disp.add( mm.getMessage( "options.dispDataValue" ) );  //"ԭֵ��ʾ
		disp.add( mm.getMessage( "options.dispDataNamePercent" ) );  //���ƺͰٷֱ�
		disp.add( mm.getMessage( "options.dispDataNameValue" ) );  //���ƺ�ֵ
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡͼ����λ�����б�
	 * @return ͼ����λ�����б�
	 */
	public static JComboBoxEx getLegendLocationBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( IGraphProperty.LEGEND_NONE) );
		code.add( new Integer( IGraphProperty.LEGEND_TOP) );
		code.add( new Integer( IGraphProperty.LEGEND_BOTTOM) );
		code.add( new Integer( IGraphProperty.LEGEND_LEFT) );
		code.add( new Integer( IGraphProperty.LEGEND_RIGHT) );
		disp.add( mm.getMessage( "options.dispDataNone" ) );  //"����ʾ
		disp.add( mm.getMessage( "options.top" ) );
		disp.add( mm.getMessage( "options.bottom" ) );
		disp.add( mm.getMessage( "options.left" ) );
		disp.add( mm.getMessage( "options.right" ) );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}
	
	/**
	 * ��ȡ���������������б�
	 * @return ���������������б�
	 */
	public static JComboBoxEx getBarcodeType() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( Consts.TYPE_NONE) );
		code.add( new Integer( Consts.TYPE_CODABAR) );
		code.add( new Integer( Consts.TYPE_CODE128) );
		code.add( new Integer( Consts.TYPE_CODE128A) );
		code.add( new Integer( Consts.TYPE_CODE128B) );
		code.add( new Integer( Consts.TYPE_CODE128C) );
		code.add( new Integer( Consts.TYPE_CODE39) );
		code.add( new Integer( Consts.TYPE_EAN13) );
		code.add( new Integer( Consts.TYPE_EAN8) );
		code.add( new Integer( Consts.TYPE_ITF) );
		code.add( new Integer( Consts.TYPE_PDF417) );
		code.add( new Integer( Consts.TYPE_UPCA) );
		code.add( new Integer( Consts.TYPE_QRCODE) );
		
		disp.add( mm.getMessage( "options.dispDataNone" ) );  //"����ʾ
		disp.add( "Codabar" );
		disp.add( "Code128Auto" );
		disp.add( "Code128A" );
		disp.add( "Code128B" );
		disp.add( "Code128C" );
		disp.add( "Code39" );
		disp.add( "Ean13" );
		disp.add( "Ean8" );
		disp.add( "ITF" );
		disp.add( "PDF417" );
		disp.add( "UPCA" );
		disp.add( "QRCode" );
		
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}
	
	/**
	 * ��ȡ�ַ��������б�
	 * @return �ַ��������б�
	 */
	public static JComboBoxEx getCharSet() {
		Vector<String> code = new Vector<String>();
		code.add( "" );
		code.add( "UTF-8" );
		code.add( "GBK" );
		code.add( "iso-8859-1" );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, code );
		return box;
	}

	/**
	 * ��ȡ��ά���ݴ��������б�
	 * @return ��ά���ݴ��������б�
	 */
	public static JComboBoxEx getRecError() {
		Vector<String> code = new Vector<String>();//L(7%)��M(15%)��Q(25%)��H(30%)
	    Vector<String> disp = new Vector<String>();
	    code.add("L");
	    code.add("M");
	    code.add("Q");
	    code.add("H");

	    disp.add("L(7%)");
	    disp.add("M(15%)");
	    disp.add("Q(25%)");
	    disp.add("H(30%)");
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ��״ͼ�����б�
	 * @return ��״ͼ�����б�
	 */
	public static JComboBoxEx getInputColumnTypeBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( GraphTypes.GT_COL) );
		code.add( new Integer( GraphTypes.GT_COL3D) );
		code.add( new Integer( GraphTypes.GT_COL3DOBJ) );
		code.add( new Integer( GraphTypes.GT_COLSTACKED) );
		code.add( new Integer( GraphTypes.GT_COLSTACKED3DOBJ) );
		code.add( new Integer( GraphTypes.GT_BAR) );
		code.add( new Integer( GraphTypes.GT_BAR3DOBJ) );
		code.add( new Integer( GraphTypes.GT_BARSTACKED) );
		code.add( new Integer( GraphTypes.GT_BARSTACKED3DOBJ) );
		disp.add( mm.getMessage("graphType.1") );
		disp.add( mm.getMessage("graphType.2") );
		disp.add( mm.getMessage("graphType.3") );
		disp.add( mm.getMessage("graphType.4") );
		disp.add( mm.getMessage("graphType.5") );
		disp.add( mm.getMessage("graphType.12") );
		disp.add( mm.getMessage("graphType.14") );
		disp.add( mm.getMessage("graphType.15") );
		disp.add( mm.getMessage("graphType.16") );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ����ͼ�����б�
	 * @return ����ͼ�����б�
	 */
	public static JComboBoxEx getInputLineTypeBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( GraphTypes.GT_LINE) );
		code.add( new Integer( GraphTypes.GT_LINE3DOBJ) );
		code.add( new Integer( GraphTypes.GT_CURVE) );
		code.add( new Integer( GraphTypes.GT_RADAR) );
		disp.add( mm.getMessage("graphType.8") );
		disp.add( mm.getMessage("graphType.9") );
		disp.add( mm.getMessage("graphType.29") );
		disp.add( mm.getMessage("graphType.22") );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ��״ͼ�����б�
	 * @return ��״ͼ�����б�
	 */
	public static JComboBoxEx getInputPieTypeBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( GraphTypes.GT_PIE) );
		code.add( new Integer( GraphTypes.GT_PIE3DOBJ) );
		disp.add( mm.getMessage("graphType.6") );
		disp.add( mm.getMessage("graphType.7") );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}

	/**
	 * ��ȡ˫��ͼ�������б�
	 * @return ˫��ͼ�������б�
	 */
	public static JComboBoxEx getInput2AxisTypeBox() {
		Vector<Integer> code = new Vector<Integer>();
		Vector<String> disp = new Vector<String>();
		code.add( new Integer( GraphTypes.GT_2YCOLLINE) );
		code.add( new Integer( GraphTypes.GT_2Y2LINE) );
		disp.add( mm.getMessage("graphType.20") );
		disp.add( mm.getMessage("graphType.21") );
		JComboBoxEx box = new JComboBoxEx();
		box.x_setData( code, disp );
		return box;
	}
}

