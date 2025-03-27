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
 * ���������༭ A.insert()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class AInsert extends ObjectElement {
	public int k=0;
	public String originalTable;//����Դ����룬������Ҳ���������������Ȳ��룬û����ϱ༭�����Ը�����д�����Լ�д���ʽȥ
	public ArrayList<FieldDefine> expFields;//�����ֵ�Լ���Ӧ�ֶ�
	
	public boolean n;
	public boolean r;
	public boolean f;
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(AInsert.class, this);

		paramInfos.add(new ParamInfo("k",Consts.INPUT_INTEGER));
		paramInfos.add(new ParamInfo("originalTable",EtlConsts.INPUT_CELLA));
		paramInfos.add(new ParamInfo("expFields",EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("n", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("r", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("f", Consts.INPUT_CHECKBOX));

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
		if(n){
			options.append("n");
		}
		if(r){
			options.append("r");
		}
		if(f){
			options.append("f");
		}
		return options.toString();
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "insert";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append( k );
		if(StringUtils.isValidString(originalTable)){
			sb.append(":");
			sb.append(originalTable);
		}
		
		String buf = getFieldDefineExp2(expFields);
		if(!buf.isEmpty()){
			sb.append(",");
			sb.append( buf );
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,",");
		String tmp = st.nextToken();
		StringTokenizer header = new StringTokenizer(tmp,":");
		k = Integer.parseInt(header.nextToken());
		if(header.hasMoreTokens()){
			tmp = header.nextToken();
			originalTable = tmp;
		}
		
		if(st.hasMoreTokens()){
			tmp = st.nextToken(";");
			expFields = getFieldDefine2(tmp);
		}
		return true;
	}
}
