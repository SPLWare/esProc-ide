package com.scudata.ide.spl.etl.element;

import com.scudata.chart.Consts;
import com.scudata.common.ArgumentTokenizer;
import com.scudata.common.StringUtils;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ file()
 * 
 * @author Joancy
 *
 */
public class File extends ObjectElement {
	public String fn;

	public String cs;

	public boolean s;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(File.class, this);

		paramInfos.add(new ParamInfo("fn", Consts.INPUT_FILE,true));
		paramInfos.add(new ParamInfo("cs", Consts.INPUT_CHARSET));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("s", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_EMPTY
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_EMPTY;
	}

	/**
	 * ��ȡ�ú����ķ�������
	 * @return EtlConsts.TYPE_FILE
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_FILE;
	}


	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		if(s){
			return "s";
		}
		return "";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "file";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getParamExp(fn));
		if(StringUtils.isValidString(cs)){
			sb.append(":");
			sb.append(getParamExp(cs));
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		ArgumentTokenizer at = new ArgumentTokenizer(funcBody,':');
		fn = getParam(at.nextToken());
		if(at.hasMoreTokens()){
			cs = getParam(at.nextToken());
		}
		return true;
	}

}
