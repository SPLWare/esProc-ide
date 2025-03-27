package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;

import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.FieldDefine;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ A.rename()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class ARename extends ObjectElement {
	public ArrayList<FieldDefine> fieldNames;//���������ֶ��б�
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(ARename.class, this);

		paramInfos.add(new ParamInfo("fieldNames",EtlConsts.INPUT_FIELDDEFINE_RENAME_FIELD));
		
		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return ǰ׺A��ͷ�ĺ�����������EtlConsts.TYPE_SEQUENCE
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_SEQUENCE;
	}

	/**
	 * ��ȡ�ú����ķ�������
	 * @return EtlConsts.TYPE_SEQUENCE
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_SEQUENCE;
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer options = new StringBuffer();
		return options.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "rename";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		String buf = getFieldDefineExp2(fieldNames);
		return buf;
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		fieldNames = getFieldDefine2(funcBody);
		return true;
	}

}
