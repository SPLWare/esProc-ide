package com.scudata.ide.spl.etl.element;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ joinx()
 * 
 * @author Joancy
 *
 */
public class Joinx extends ObjectElement {
	public String srcCursor;//���ӵ�Դ�α�
	public ArrayList<String> srcKeys;//���ӵ�Դ�ֶ�
	public String dstCursor;//���ӵ�Ŀ���α�
	public ArrayList<String> dstKeys;//���ӵ�Ŀ���ֶ�
	
	public boolean f;
	public boolean one;
	public boolean p;
	public boolean u;
	public boolean i;
	public boolean d;
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(Joinx.class, this);

		paramInfos.add(new ParamInfo("srcCursor",EtlConsts.INPUT_CELLCURSOR,true));
		paramInfos.add(new ParamInfo("srcKeys",EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("dstCursor",EtlConsts.INPUT_CELLAORCS,true));//ѡ��uʱ����Ϊ���
		paramInfos.add(new ParamInfo("dstKeys",EtlConsts.INPUT_STRINGLIST));

		String group = "options";
		paramInfos.add(group, new ParamInfo("f", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("one", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("p", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("u", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("d", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_EMPTY
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_EMPTY;
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
		if(f){
			options.append("f");
		}
		if(one){
			options.append("1");
		}
		if(p){
			options.append("p");
		}
		if(u){
			options.append("u");
		}
		if(i){
			options.append("i");
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
		return "joinx";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getExpressionExp(srcCursor));
		sb.append(",");
		sb.append( getStringListExp(srcKeys,",") );
		sb.append(";");
		sb.append(getExpressionExp(dstCursor));
		sb.append(",");
		sb.append( getStringListExp(dstKeys,",") );
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer( funcBody,";");
		String buf = st.nextToken();
		StringTokenizer tmpST = new StringTokenizer( buf, ",");
		srcCursor = getExpression( tmpST.nextToken() );
		srcKeys = getStringList( tmpST.nextToken(";"),",");
		
		buf = st.nextToken();
		tmpST = new StringTokenizer( buf, ",");
		dstCursor = getExpression( tmpST.nextToken() );
		dstKeys = getStringList( tmpST.nextToken(";"),",");
		
		return true;
	}

}
