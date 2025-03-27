package com.scudata.ide.spl.etl.element;

import java.util.StringTokenizer;

import com.scudata.chart.Consts;
import com.scudata.common.StringUtils;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ CS.groupx()
 * ������ǰ׺Cs��ʾ�α�
 * 
 * @author Joancy
 *
 */
public class CsGroupx extends CsGroups {
	public String bufferN;
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(AGroups.class, this);
		paramInfos.add(new ParamInfo("groupExps",EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		paramInfos.add(new ParamInfo("aggregateExps",EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		ParamInfo.setCurrent(CsGroupx.class, this);
		paramInfos.add(new ParamInfo("bufferN"));
		
		ParamInfo.setCurrent(AGroups.class, this);
		String group = "options";
		paramInfos.add(group, new ParamInfo("n", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("u", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_CURSOR
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_CURSOR;
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
		if(n){
			options.append("n");
		}
		if(u && !n){//u,n����
			options.append("u");
		}
		return options.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append( getFieldDefineExp(groupExps) );
		String aggregates = getFieldDefineExp(aggregateExps);
		if(!aggregates.isEmpty()){
			sb.append(";");
			sb.append(aggregates);
		}
		if(StringUtils.isValidString(bufferN)){
			sb.append(";");
			sb.append(getExpressionExp(bufferN));
		}
		
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,";");
		String tmp = st.nextToken();
		groupExps = getFieldDefine(tmp);
		if(st.hasMoreTokens()){
			tmp = st.nextToken();
			aggregateExps = getFieldDefine(tmp);
		}
		if(st.hasMoreTokens()){
			tmp = st.nextToken();
			bufferN = getExpression(tmp);
		}
		return true;
	}

}
