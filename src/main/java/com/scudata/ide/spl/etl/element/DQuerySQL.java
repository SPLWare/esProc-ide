package com.scudata.ide.spl.etl.element;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ db.query()
 * ������ǰ׺D��ʾ���ݿ�����
 * 
 * @author Joancy
 *
 */
public class DQuerySQL extends DCursorSQL {
	public boolean one;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = super.getParamInfoList();
		ParamInfo.setCurrent(DQuerySQL.class, this);

		String group = "options";
		paramInfos.add(group, new ParamInfo("one", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_DB
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_DB;
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
		StringBuffer sb = new StringBuffer();
		sb.append(  super.optionString() );
		if(one){
			sb.append("1");
		}
		return sb.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName(){
		return "query";
	}

}
