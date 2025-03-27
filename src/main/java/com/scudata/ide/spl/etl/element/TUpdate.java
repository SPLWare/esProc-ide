package com.scudata.ide.spl.etl.element;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ t.update()
 * ������ǰ׺T��ʾCTXʵ��
 * 
 * @author Joancy
 *
 */
public class TUpdate extends ObjectElement {
	public String tableName;

	public boolean i;
	public boolean u;
	public boolean n;
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(TUpdate.class, this);

		paramInfos.add(new ParamInfo("tableName",EtlConsts.INPUT_CELLA,true));
		String group = "options";
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("u", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("n", Consts.INPUT_CHECKBOX));
		
		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_CTX
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_CTX;
	}

	/**
	 * ��ȡ�ú����ķ�������
	 * @return EtlConsts.TYPE_CTX
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_CTX;
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer sb = new StringBuffer();
		if(i){
			sb.append("i");
		}
		if(u){
			sb.append("u");
		}
		if(n){
			sb.append("n");
		}
		return sb.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "update";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getExpressionExp(tableName));
		
		return sb.toString();
	}


	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody( String funcBody ) {
		tableName = getExpression( funcBody );
		return true;
	}


}
