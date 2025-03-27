package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.FieldDefine;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ f.export()
 * ������ǰ׺F��ʾ�ļ�����
 * 
 * @author Joancy
 *
 */
public class FExport extends ObjectElement {
	public String AorCs;
	
	public ArrayList<FieldDefine> fields;
	public String seperator;

	public boolean t;
	public boolean a;
	public boolean b;
	public boolean c;
	public boolean z;
	public boolean w;
	public boolean q;
	public boolean o;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(FExport.class, this);

		paramInfos.add(new ParamInfo("AorCs", EtlConsts.INPUT_CELLAORCS,true));
		paramInfos.add(new ParamInfo("fields", EtlConsts.INPUT_FIELDDEFINE_NORMAL));
		paramInfos.add(new ParamInfo("seperator", EtlConsts.INPUT_SEPERATOR2));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("t", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("a", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("b", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("c", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("z", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("w", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("q", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("o", Consts.INPUT_CHECKBOX));

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
		if(b){
			options.append("b");
		}
		if(c){
			options.append("c");
		}
		if(z){
			options.append("z");
		}
		if(w){
			options.append("w");
		}
		if(q){
			options.append("q");
			if(o){//o������q
				options.append("o");
			}
		}
		return options.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "export";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getExpressionExp(AorCs));
		
		String fieldStr = getFieldDefineExp(fields);
		if(!fieldStr.isEmpty()){
			sb.append(",");
			sb.append( fieldStr );
		}

		if( seperator!=null && !seperator.isEmpty()){
			sb.append(";");
			sb.append(getParamExp(seperator));
		}

		return sb.toString();
	}
	
	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,";");
		String header = st.nextToken();
		int douhao = header.indexOf(",");
		if(douhao>0){
			AorCs= getExpression(header.substring(0,douhao));
			fields = getFieldDefine(header.substring(douhao+1));
		}else{
			AorCs = getExpression(header);
		}
		if(st.hasMoreTokens()){
			String tmp = st.nextToken();
			seperator = getParam(tmp);
		}
		return true;
	}
	
}
