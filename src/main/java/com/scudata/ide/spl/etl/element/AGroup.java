package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;


/**
 * ���������༭ A.group()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class AGroup extends ObjectElement {
	public ArrayList<String> groupFields;
	
	public boolean o;
	public boolean one;
	public boolean n;
	public boolean u;
	public boolean i;
	public boolean zero;
	public boolean s;
	public boolean p;
	public boolean h;
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(AGroup.class, this);

		paramInfos.add(new ParamInfo("groupFields",EtlConsts.INPUT_STRINGLIST));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("o", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("one", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("n", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("u", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("zero", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("s", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("p", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("h", Consts.INPUT_CHECKBOX));

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
		if(o){
			options.append("o");
		}
		if(one){
			options.append("1");
		}
		if(n && !o){//o n����
			options.append("n");
		}
		if(u && !(o || n)){//��o,n����
			options.append("u");
		}
		if(i){
			options.append("i");
		}
		if(zero){
			options.append("0");
		}
		if(s){
			options.append("s");
		}
		if(p){
			options.append("p");
		}
		if(h){
			options.append("h");
		}
		return options.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "group";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		return getStringListExp(groupFields,",");
	}


	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		if(funcBody.indexOf(";")>0){
			return false;
		}
		groupFields = getStringList(funcBody,",");
		return true;
	}

}
