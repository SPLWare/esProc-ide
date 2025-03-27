package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ db.cursor()
 * ������ǰ׺D��ʾ���ݿ�����
 * 
 * @author Joancy
 *
 */
public class DCursorSQL extends ObjectElement {
	public String sql;
	
	public ArrayList<String> args;
	
	public boolean i;
	public boolean d;
	public boolean x;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(DCursorSQL.class, this);

		paramInfos.add(new ParamInfo("sql",true));
		paramInfos.add(new ParamInfo("args", EtlConsts.INPUT_STRINGLIST));
		String group = "options";
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("d", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("x", Consts.INPUT_CHECKBOX));

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
	 * @return EtlConsts.TYPE_CURSOR
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_CURSOR;
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer options = new StringBuffer();
		if(i){
			options.append("i");
		}
		if(d){
			options.append("d");
		}
		if(x){
			options.append("x");
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "cursor";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getParamExp(sql));
		String argStr = getStringListExp(args,",");
		if(!argStr.isEmpty()){
			sb.append(",");
			sb.append(argStr);	
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		int index = funcBody.indexOf(",");
		if(index>0){
			sql = getParam(funcBody.substring(0,index));
			String tmp = funcBody.substring(index+1);
			args = getStringList(tmp,",");
		}else{
			sql = getParam(funcBody);
		}
		return true;
	}

}
