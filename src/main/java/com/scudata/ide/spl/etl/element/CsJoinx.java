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
 * ���������༭ CS.joinx()
 * ������ǰ׺Cs��ʾ�α�
 * 
 * @author Joancy
 *
 */
public class CsJoinx extends ObjectElement {
	public ArrayList<String> foreignKeys;
	
	public String joinFile;//���ӵļ��ļ���������ļ�
	
	public ArrayList<String> joinKeys;//joinFile�������������foreignKeysһһ��Ӧ
	public ArrayList<FieldDefine> attachFields;//���Ӻ󸽼��ڵ�ǰA��ı��ʽ�Լ�����ֶ�������ʹ��FieldDefine�ĵ�����
	public String bufferN="1024";//����������
	
	public boolean i;
	public boolean d;
	public boolean q;
	public boolean c;
	public boolean u;
//	public boolean o;//�Ȳ�ʵ��oѡ��
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(CsJoinx.class, this);

		paramInfos.add(new ParamInfo("foreignKeys",EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("joinFile",EtlConsts.INPUT_CELLBCTX,true));
		paramInfos.add(new ParamInfo("joinKeys",EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("attachFields",EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD));
		paramInfos.add(new ParamInfo("bufferN"));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("d", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("q", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("c", Consts.INPUT_CHECKBOX));
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
		if(i){
			options.append("i");
		}
		if(d){
			options.append("d");
		}
		if(q){
			options.append("q");
		}
		if(c){
			options.append("c");
		}
		if(u){
			options.append("u");
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "joinx";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append( getStringListExp(foreignKeys,":") );
		sb.append(",");
		sb.append(getExpressionExp(joinFile));
		sb.append(":");
		sb.append( getStringListExp(joinKeys,":") );
		
		String buf = getFieldDefineExp2(attachFields);
		if(!buf.isEmpty()){
			sb.append(",");
			sb.append( buf );
		}
		
		sb.append(";");
		sb.append(getNumberExp(bufferN));
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer fenhao = new StringTokenizer(funcBody,";");
		String tmp =  fenhao.nextToken();
		StringTokenizer st = new StringTokenizer(tmp,",");
		foreignKeys = getStringList(st.nextToken(),":");
		String joins = st.nextToken();
		int first = joins.indexOf(":");
		String header = joins.substring(0,first);
		joinFile = getExpression(header);
		String tailer = joins.substring(first+1);
		joinKeys = getStringList(tailer,":");
		if(st.hasMoreTokens()){
			attachFields = getFieldDefine2(st.nextToken());
		}
		if( fenhao.hasMoreTokens() ){
			bufferN = getNumber(fenhao.nextToken());
		}
		return true;
	}
}
