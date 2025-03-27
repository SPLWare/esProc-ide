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
 * ���������༭ A.join()
 * ������ǰ׺A��ʾ���
 * 
 * @author Joancy
 *
 */
public class AJoin extends ObjectElement {
	public ArrayList<String> foreignKeys;
	public String joinTable;
	public ArrayList<String> joinKeys;// ���ӵ�������ú�������A.keys(keys)ʱ���������ʡ��

	public ArrayList<FieldDefine> attachFields;// ���Ӻ󸽼��ڵ�ǰA��ı��ʽ�Լ�����ֶ�������ʹ��FieldDefine�ĵ�����

	public boolean i;

	// public boolean o;//�Ȳ�ʵ��oѡ��

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(AJoin.class, this);

		paramInfos
				.add(new ParamInfo("foreignKeys", EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("joinTable", EtlConsts.INPUT_CELLA, true));
		paramInfos.add(new ParamInfo("joinKeys", EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("attachFields",
				EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));

		String group = "options";
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));

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
		StringBuffer options = new StringBuffer();
		if (i) {
			options.append("i");
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "join";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getStringListExp(foreignKeys, ":"));
		sb.append(",");
		sb.append(getExpressionExp(joinTable));

		String buf = getStringListExp(joinKeys, ":");
		if (StringUtils.isValidString(buf)) {
			sb.append(":");
			sb.append(buf);
		}

		buf = getFieldDefineExp(attachFields);
		if (!buf.isEmpty()) {
			sb.append(",");
			sb.append(buf);
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody, ",");
		String tmp = st.nextToken();
		foreignKeys = getStringList(tmp, ":");
		tmp = st.nextToken();
		int index = tmp.indexOf(":");
		if (index < 0) {
			return false;
		}
		joinTable = getExpression(tmp.substring(0, index));
		joinKeys = getStringList(tmp.substring(index + 1), ":");

		if (st.hasMoreTokens()) {
			tmp = st.nextToken();
			attachFields = getFieldDefine(tmp);
		}
		return true;
	}

}
