package com.scudata.ide.spl;

import java.util.List;

import com.scudata.cellset.datamodel.PgmCellSet;
import com.scudata.common.CellLocation;
import com.scudata.dm.Context;

/**
 * �������Ե���Ϣ��
 * 
 * ���������֣�һ���Ǵ򿪵ĸ�������һ���Ǳ�call��spl�� ��call��spl��û�д򿪵ģ�������Ҫ�ȴ򿪣��༭���ٱ��档
 *
 */
public class StepInfo {

	public static final byte TYPE_CALL = 0; // call
	public static final byte TYPE_FUNC = 1; // func

	/**
	 * ���͡�TYPE_CALL,TYPE_FUNC
	 */
	public byte type;
	/**
	 * �ļ�·��
	 */
	public String filePath;
	/**
	 * ��˳���ҳ�б�
	 */
	public List<SheetSpl> sheets;
	/**
	 * ������
	 */
	public Context splCtx;
	/**
	 * ������call��func�������ڵ�����
	 */
	public CellLocation parentLocation;
	/**
	 * func������
	 */
	public CellLocation funcLocation;
	/**
	 * ������ʼ���������
	 */
	public CellLocation exeLocation;
	/**
	 * �����Ľ�����
	 */
	public int endRow;

	/**
	 * callʹ��
	 */
	// public Call parentCall;

	/**
	 * �������
	 */
	public PgmCellSet cellSet;

	/**
	 * ���캯��
	 */
	public StepInfo() {
	}

	/**
	 * ���캯��
	 * 
	 * @param sheets ��˳���ҳ�б�
	 */
	public StepInfo(List<SheetSpl> sheets, byte type) {
		this.sheets = sheets;
		this.type = type;
	}

	/**
	 * �Ƿ�func��������
	 * 
	 * @return
	 */
	public boolean isFunc() {
		return type == TYPE_FUNC;
	}

	/**
	 * �Ƿ�call��������
	 * 
	 * @return
	 */
	public boolean isCall() {
		return type == TYPE_CALL;
	}

}
