package com.scudata.ide.spl.etl;

/**
 * ����������ͨ�ýӿ�
 * 
 * @author Joancy
 *
 */
public interface IFuncObject{
	/**
	 * �ú����������ڵĸ�����
	 * @return ����
	 */
	public byte getParentType();
	
	/**
	 * �ú������ص�����
	 * @return ����
	 */
	public byte getReturnType();
}