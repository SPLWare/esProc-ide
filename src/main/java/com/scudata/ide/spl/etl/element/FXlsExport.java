package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.scudata.chart.Consts;
import com.scudata.common.StringUtils;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.FieldDefine;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ f.xlsexport()
 * ������ǰ׺F��ʾ�ļ�����
 * 
 * @author Joancy
 *
 */
public class FXlsExport extends ObjectElement {
	public String aOrCs;
	
	public ArrayList<FieldDefine> exportFields;
	public String sheet;
	public String password;

	public boolean t;
	public boolean a;
	public boolean c;
	public boolean w;
	public boolean p;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(FXlsExport.class, this);

		paramInfos.add(new ParamInfo("aOrCs",EtlConsts.INPUT_CELLAORCS,true));
		paramInfos.add(new ParamInfo("exportFields", EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		paramInfos.add(new ParamInfo("sheet"));
		paramInfos.add(new ParamInfo("password"));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("t", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("a", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("c", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("w", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("p", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_FILE
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_FILE;
	}

	/**
	 * ��ȡ�ú����ķ�������
	 * @return EtlConsts.TYPE_EMPTY
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_EMPTY;
	}


	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName(){
		return "xlsexport";
	}
	
	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer options = new StringBuffer();
		if(t){
			options.append("t");
		}
		if(a){
			options.append("a");
		}
		if(c){
			options.append("c");
		}
		boolean existTC = t|c;
		if(!existTC & w){
			options.append("w");
			if(p){
				options.append("p");
			}
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getExpressionExp(aOrCs));
		String buf = getFieldDefineExp2(exportFields);
		if(StringUtils.isValidString(buf)){
			sb.append(",");
			sb.append(getNumberExp(buf));
		}
		
		String pwd=null;
		if(StringUtils.isValidString(password)){
			pwd=";"+getParamExp(password);
		}
		String suffix = null;
		if(StringUtils.isValidString(sheet)){
			suffix = ";"+getParamExp(sheet);
			if(pwd!=null){
				suffix += pwd;
			}
		}else{
			if(pwd!=null){
				suffix = ";"+pwd;
			}
		}
		if(suffix!=null){
			sb.append(suffix);
		}
		
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody( String funcBody ) {
		StringTokenizer st = new StringTokenizer( funcBody,";");
		String buf = st.nextToken();
		int douhao = buf.indexOf(",");
		if(douhao>0){
			aOrCs = getExpression(buf.substring(0,douhao));
			exportFields = getFieldDefine2(buf.substring(douhao+1));
		}else{
			aOrCs = getExpression(buf);
		}
		if(st.hasMoreTokens()){
			sheet = getParam(st.nextToken());
		}
		if(st.hasMoreTokens()){
			password = getParam(st.nextToken());
		}
		return true;
	}

}
