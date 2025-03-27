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
 * ���������༭ db.update()
 * ������ǰ׺D��ʾ���ݿ�����
 * 
 * @author Joancy
 *
 */
public class DUpdate extends ObjectElement {
	public String aOrCs;
	public String originalA;
	public String tableName;
	
	public ArrayList<FieldDefine> updateFields;
	public ArrayList<String> keys;
	
	public boolean u;
	public boolean i;
	public boolean a;
	public boolean k;
	public boolean one;
	public boolean d;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(DUpdate.class, this);

		paramInfos.add(new ParamInfo("aOrCs", EtlConsts.INPUT_CELLAORCS,true));
		paramInfos.add(new ParamInfo("originalA", EtlConsts.INPUT_CELLA));
		paramInfos.add(new ParamInfo("tableName",true));
		paramInfos.add(new ParamInfo("updateFields", EtlConsts.INPUT_FIELDDEFINE_FIELD_EXP));
		paramInfos.add(new ParamInfo("keys", EtlConsts.INPUT_STRINGLIST));
		String group = "options";
		paramInfos.add(group, new ParamInfo("u", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("a", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("k", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("one", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("d", Consts.INPUT_CHECKBOX));

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
		if(u){
			options.append("u");
		}
		if(i){
			options.append("i");
		}
		if(a){
			options.append("a");
		}
		if(k){
			options.append("k");
		}
		if(one){
			options.append("1");
		}
		if(d){
			options.append("d");
		}
		return options.toString();
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
		sb.append(aOrCs);
		if(StringUtils.isValidString(originalA)){
			sb.append(":");
			sb.append(originalA);
		}
		
		sb.append(",");
		sb.append(tableName);
		String buf = getFieldDefineExp2(updateFields);
		if(StringUtils.isValidString(buf)){
			sb.append(",");
			sb.append(buf);	
		}
		String keysStr = getStringListExp(keys,",");
		if(StringUtils.isValidString(keysStr)){
			sb.append(";");
			sb.append(keysStr);	
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		int fenhao = funcBody.indexOf(";");
		String header;
		if(fenhao>0){
			header = funcBody.substring(0,fenhao);
			String tmp = funcBody.substring(fenhao+1);
			keys = getStringList(tmp,",");
		}else{
			header = funcBody;
		}
		StringTokenizer st = new StringTokenizer(header,",");
		String buf = st.nextToken();
		int maohao = buf.indexOf(":");
		if(maohao>0){
			aOrCs = buf.substring(0,maohao);
			originalA = buf.substring(maohao+1);
		}else{
			aOrCs = buf;
		}
		tableName = st.nextToken();
		buf = st.nextToken(";");//������������ֶ�ȫ���س���
		if(StringUtils.isValidString(buf)){
			buf = buf.substring(1);//ȥ��ȫ�غ󣬶�������ַ�����
			updateFields = getFieldDefine2(buf);
		}
		return true;
	}

}
