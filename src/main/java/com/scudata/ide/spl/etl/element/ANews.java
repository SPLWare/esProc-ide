package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.FieldDefine;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ A.news()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class ANews extends ObjectElement {
	public String bigX;
	public ArrayList<FieldDefine> newFields;//��ʹ�õ����У� ���ʽ���ֶ���
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(ANews.class, this);

		paramInfos.add(new ParamInfo("bigX"));
		paramInfos.add(new ParamInfo("newFields",EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		
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
	public String optionString() {
		return null;
	}
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "news";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append( getExpressionExp(bigX) );
		sb.append(";");
		sb.append( getFieldDefineExp(newFields));
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,";");
		String tmp = st.nextToken();
		bigX = getExpression(tmp);
		
		tmp = st.nextToken();
		newFields = getFieldDefine(tmp);
		return true;
	}

}
