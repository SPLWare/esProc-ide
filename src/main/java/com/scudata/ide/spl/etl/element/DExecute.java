package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;

import com.scudata.chart.Consts;
import com.scudata.common.StringUtils;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ db.execute()
 * ������ǰ׺D��ʾ���ݿ�����
 * 
 * @author Joancy
 *
 */
public class DExecute extends ObjectElement {
	public String aOrCs;
	public String sql;
	
	public ArrayList<String> args;
	
	public boolean k;
	public boolean s;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(DExecute.class, this);

		paramInfos.add(new ParamInfo("aOrCs", EtlConsts.INPUT_CELLAORCS));
		paramInfos.add(new ParamInfo("sql",true));
		paramInfos.add(new ParamInfo("args", EtlConsts.INPUT_STRINGLIST));
		String group = "options";
		paramInfos.add(group, new ParamInfo("k", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("s", Consts.INPUT_CHECKBOX));

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
	 * @return EtlConsts.TYPE_EMPTY
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_EMPTY;
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer options = new StringBuffer();
		if(k){
			options.append("k");
		}
		if(s){
			options.append("s");
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "execute";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isValidString(aOrCs)){
			sb.append(aOrCs);	
			sb.append(",");	
		}
		sb.append(getParamExp(sql));
		String argStr = getStringListExp(args,",");
		if(!argStr.isEmpty()){
			sb.append(",");
			sb.append(argStr);	
		}
		return sb.toString();
	}

	private boolean isSql(String var){
		var = var.toUpperCase();
		if(var.startsWith("SELECT ")){
			return true;
		}
		if(var.startsWith("UPDATE ")){
			return true;
		}
		if(var.startsWith("INSERT ")){
			return true;
		}
		if(var.startsWith("DELETE ")){
			return true;
		}
		return false;
	}
	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		int index = funcBody.indexOf(",");
		if(index<0){
			sql = getParam(funcBody);
			return true;
		}
		
		String buf = funcBody.substring(0,index);
		if( isSql(buf) ){
			sql = getParam( buf );
		}else{
			aOrCs = buf;
		}
		buf = funcBody.substring(index+1);
		
		if(StringUtils.isValidString(buf)){
			args = getStringList(buf,",");
		}
		return true;
	}
}
