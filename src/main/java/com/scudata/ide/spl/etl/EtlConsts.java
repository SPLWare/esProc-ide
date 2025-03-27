package com.scudata.ide.spl.etl;

import java.util.Vector;

import com.scudata.ide.common.swing.JComboBoxEx;
import com.scudata.ide.spl.GVSpl;

/**
 * ��ʵ���˲��ָ�ETL��صĺ��������༭
 * ���ඨ�帨���༭�������õ��ĳ�������
 * �Լ����ֹ��ߺ���
 * 
 * @author Joancy
 *
 */
public class EtlConsts {
	public final static byte TYPE_EMPTY = 0;
	public final static byte TYPE_DB = 1;
	public final static byte TYPE_FILE = 2;
	public final static byte TYPE_SEQUENCE = 3;
	public final static byte TYPE_CURSOR = 4;
	public final static byte TYPE_CTX = 5;//���
	public final static byte TYPE_XLS = 6;//XLS����
	public final static byte TYPE_STRING = 7;

	// ���������������
	public final static int INPUT_SEPERATOR = 1002;
	public final static int INPUT_SEPERATOR2 = 1003;//����ѡ��յķָ���������ɾ������
	public final static int INPUT_STRINGLIST = 1004;
	public final static int INPUT_ISOLATIONLEVEL = 1005;//�����������
	public final static int INPUT_DB = 1006;//��������Դ����
	public final static int INPUT_ONLYPROPERTY = 1007;//���������ִ����ԣ�����������ʽ
	
	public final static int INPUT_CELLA = 1011;//�������͵ĸ��������б� A
	public final static int INPUT_CELLAORCS = 1012;//���к��α�ĸ��������б� A  or  Cursor
	public final static int INPUT_CELLFILE = 1013;//�ļ����� �����б� File
	public final static int INPUT_CELLCURSOR = 1014;//�����б� Cursor
	public final static int INPUT_CELLBCTX = 1015;//BTX and CTX
	public final static int INPUT_CELLXLS = 1016;//XLS
	
	public final static int INPUT_FIELDDEFINE_NORMAL = 1021;//�ֶ���������(����),format
	public final static int INPUT_FIELDDEFINE_EXP_FIELD = 1022;//���ʽ���ֶ���
	public final static int INPUT_FIELDDEFINE_FIELD_EXP = 1023;//�ֶ��������ʽ
	public final static int INPUT_FIELDDEFINE_RENAME_FIELD = 1024;//ԭ������������
	
	public final static int INPUT_FIELDDEFINE_FIELD_DIM = 1030;//�ֶ���,ά(Checkbox)


	/**
	 * ����ֵ���б���߸��ӵĶ���ʱ����Ҫ��ֹ���ʽ�༭
	 * @param type �༭����
	 * @return ��Ҫ��ֹ���ʽ��ʱ����true�����򷵻�false
	 */
	public static boolean isDisableExpEditType(int type){
		if(type==INPUT_STRINGLIST){
			return true;
		}
		if(type==INPUT_FIELDDEFINE_NORMAL){
			return true;
		}
		if(type==INPUT_FIELDDEFINE_EXP_FIELD){
			return true;
		}
		if(type==INPUT_FIELDDEFINE_FIELD_EXP){
			return true;
		}
		if(type==INPUT_FIELDDEFINE_RENAME_FIELD){
			return true;
		}
		if(type==INPUT_FIELDDEFINE_FIELD_DIM){
			return true;
		}
		if(type==INPUT_ONLYPROPERTY){
			return true;
		}
		
		return false;
	}
	
	/**
	 * ��ȡ������Դtype��������Ϣ
	 * @param type ������Դ����
	 * @return �ı�������Ϣ
	 */
	public static String getTypeDesc(byte type){
		switch(type){
		case TYPE_DB:
			return "DB";
		case TYPE_FILE:
			return "File";
		case TYPE_SEQUENCE:
			return "Sequence";
		case TYPE_CURSOR:
			return "Cursor";
		case TYPE_CTX:
			return "CTX";
		case TYPE_XLS:
			return "XLS";
		}
		return "";
	}

	/**
	 * ��ȡ���ݿ�ĸ��뼶�������б�
	 * @return �����б�
	 */
	public static JComboBoxEx getIsolationLevelBox(){
		Vector<String> scodes = new Vector<String>(),sdisps = new Vector<String>();
	    scodes.add("");
	    scodes.add("n");
	    scodes.add("c");
	    scodes.add("u");
	    scodes.add("r");
	    scodes.add("s");

	    sdisps.add("");
	    sdisps.add("None");
	    sdisps.add("Commit");
	    sdisps.add("Uncommit");
	    sdisps.add("Repeatable");
	    sdisps.add("Serializable");
	    JComboBoxEx combo = new JComboBoxEx();
	    combo.x_setData(scodes, sdisps);
		return combo;
	}
	
	/**
	 * ��ȡ��ǰ�����µ����ж�������ݿ�����������б�
	 * @return �����б�
	 */
	public static JComboBoxEx getDBBox(){
		Vector<String> dbNames = GVSpl.dsModel.listNames();
		dbNames.insertElementAt("", 0);
	    JComboBoxEx combo = new JComboBoxEx();
	    combo.x_setData(dbNames, dbNames);
		return combo;
	}

	/**
	 * ��ȡ�ı�����Դʱ���ָ������͵������б�
	 * @param allowEmpty �Ƿ����һ����ֵʹ���û�����ѡ��ձ�ʾûѡ�κ�ֵ
	 * @return �����б�
	 */
	public static JComboBoxEx getSeperatorComboBox(boolean allowEmpty) {
		String msg = "SPACE";
		String SEP_SPACE = msg;
		String SEP_TAB = "TAB";
		Vector<String> scodes = new Vector<String>(), sdisps = new Vector<String>();
		if (allowEmpty) {
			scodes.add("");
			sdisps.add("");
		}
		// �ָ���
		scodes.add("\t");
		scodes.add(",");
		scodes.add(" ");
		scodes.add("|");
		scodes.add("-");
		scodes.add("_");
		sdisps.add(SEP_TAB);
		sdisps.add(",");
		sdisps.add(SEP_SPACE);
		sdisps.add("|");
		sdisps.add("-");
		sdisps.add("_");
		JComboBoxEx combo = new JComboBoxEx();
		combo.x_setData(scodes, sdisps);
		return combo;
	}
	
}
