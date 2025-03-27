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
 * ���������༭ t.close()
 * ������ǰ׺T��ʾCTXʵ��
 * 
 * @author Joancy
 *
 */
public class TCursor extends ObjectElement {
	public ArrayList<FieldDefine> fields;
	public String where;
	public String argk;
	public String argn;
	public String cursorN;
	
	public boolean m;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(TCursor.class, this);

		paramInfos.add(new ParamInfo("fields", EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		paramInfos.add(new ParamInfo("where",EtlConsts.INPUT_ONLYPROPERTY));
		paramInfos.add(new ParamInfo("argk"));
		paramInfos.add(new ParamInfo("argn"));
		paramInfos.add(new ParamInfo("cursorN"));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("m", Consts.INPUT_CHECKBOX));

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
		if(m){
			options.append("m");
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
		sb.append(getFieldDefineExp(fields));
		
		StringBuffer bufKN = new StringBuffer();
		if(m){
			if(StringUtils.isValidString(cursorN)){
				bufKN.append(getNumberExp(cursorN));	
			}
		}else{
			if(StringUtils.isValidString(argk)){
				bufKN.append(getNumberExp(argk));
				bufKN.append(":");
				bufKN.append(getNumberExp(argn));
			}
		}
		if(bufKN.length()>0){
			sb.append(";");
			if(StringUtils.isValidString(where)){
				sb.append(where);
			}
			sb.append(";");
			sb.append(bufKN.toString());
		}else{
			if(StringUtils.isValidString(where)){
				sb.append(";");
				sb.append(where);
			}
		}
		
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,";");
		fields = getFieldDefine(st.nextToken());
		String buf;
		if(st.hasMoreTokens()){
			buf = st.nextToken();
			if(isValidString(buf)){
				where = buf;
			}
		}
		if(st.hasMoreTokens()){
			buf = st.nextToken();
			int maohao = buf.indexOf(":");
			if(maohao>0){
				m = false;
				argk = getNumber(buf.substring(0,maohao));
				argn = getNumber(buf.substring(maohao+1));
			}else{
				m = true;
				cursorN = getNumber(buf);
			}
		}
		return true;
	}


}
