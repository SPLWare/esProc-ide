package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.scudata.common.StringUtils;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.FieldDefine;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ t.attach()
 * ������ǰ׺T��ʾCTXʵ��
 * 
 * @author Joancy
 *
 */
public class TAttach extends ObjectElement {
	public String tableName;	
	public ArrayList<FieldDefine> fields;
	

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(TAttach.class, this);

		paramInfos.add(new ParamInfo("tableName",true));
		paramInfos.add(new ParamInfo("fields",EtlConsts.INPUT_FIELDDEFINE_FIELD_DIM));
		
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
	 * @return EtlConsts.TYPE_SEQUENCE
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_SEQUENCE;
	}

	
	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		return "";
	}

	private String getFieldsExp(){
		if(fields==null || fields.isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if(fields!=null){
			for(FieldDefine fd:fields){
				sb.append(",");
				
				if(StringUtils.isValidString(fd.getTwo())){
					if(Boolean.parseBoolean(fd.getTwo())){
						sb.append("#");
					}
				}
				sb.append(fd.getOne());
			}
		}
		return sb.toString();
		
	}
	
	private void getFields(String fieldstr){
		fields = new ArrayList<FieldDefine>();
		StringTokenizer st = new StringTokenizer(fieldstr,",");
		while( st.hasMoreTokens()){
			FieldDefine fd = new FieldDefine();
			String buf = st.nextToken();
			if(buf.startsWith("#")){
				fd.setTwo("true");
				buf = buf.substring(1);
			}
			fd.setOne(buf);
			fields.add(fd);
		}
	}
	
	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "attach";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getExpressionExp(tableName));
		sb.append(getFieldsExp());
		
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,",");
		tableName = getExpression(st.nextToken());
		if(st.hasMoreTokens()){
			String buf = st.nextToken(";");//ʹ�ò�ͬ�ķ��ţ�ȡ���������ж��Ŵ�
			getFields(buf);
		}
		return true;
	}


}
