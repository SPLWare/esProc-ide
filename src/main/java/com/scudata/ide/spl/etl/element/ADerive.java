package com.scudata.ide.spl.etl.element;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ A.derive()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class ADerive extends ANew {
	public boolean x;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = super.getParamInfoList();
		ParamInfo.setCurrent(ADerive.class, this);

		String group = "options";
		paramInfos.add(group, new ParamInfo("x", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName(){
		return "derive";
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer options = new StringBuffer();
		options.append(super.optionString());
		if(x){
			options.append("x");
		}
		return options.toString();
	}
}
