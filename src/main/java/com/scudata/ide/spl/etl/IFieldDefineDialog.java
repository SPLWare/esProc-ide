package com.scudata.ide.spl.etl;

import java.util.ArrayList;

/**
 * �ֶζ���Ի����ͨ�ýӿں���
 * 
 * @author Joancy
 *
 */
public interface IFieldDefineDialog{
	/**
	 * �����ֶζ����б�
	 * @param fields �ֶζ����б�
	 */
	public void setFieldDefines(ArrayList<FieldDefine> fields);
	
	/**
	 * ��ȡ�༭�õ��ֶζ����б�
	 * @return �ֶζ����б�
	 */
	public ArrayList<FieldDefine> getFieldDefines();
	
	/**
	 * ���ش��ڵĶ���ѡ��
	 * @return ѡ��
	 */
	public int getOption();
}