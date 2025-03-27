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
 * ���������༭ f.import()
 * ������ǰ׺F��ʾ�ļ�����
 * 
 * @author Joancy
 *
 */
public class FImport extends ObjectElement {
	public ArrayList<FieldDefine> fields;
	public String argk;
	public String argn;
	public String seperator="\t";

	//ѡ�tcsiqokmbednv
	public boolean t;
	public boolean c;
	public boolean s;
	public boolean i;
	public boolean q;
	public boolean o;
	public boolean k;
	public boolean m;
	public boolean b;
	public boolean e;
	public boolean d;
	public boolean n;
	public boolean v;
	public boolean a;
	public boolean f;
	public boolean one;
	public boolean p;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(FImport.class, this);

		paramInfos.add(new ParamInfo("fields", EtlConsts.INPUT_FIELDDEFINE_NORMAL));
		paramInfos.add(new ParamInfo("argk"));
		paramInfos.add(new ParamInfo("argn"));
		paramInfos.add(new ParamInfo("seperator", EtlConsts.INPUT_SEPERATOR));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("t", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("c", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("s", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("i", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("q", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("o", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("k", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("m", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("b", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("e", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("d", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("n", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("v", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("a", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("f", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("one", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("p", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_FILE
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_FILE;
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
//		tcsiqokmbednv
		if(t){
			options.append("t");
		}
		if(c){
			options.append("c");
		}
		if(s){
			options.append("s");
		}
		if(i){
			options.append("i");
		}
		if(q){
			options.append("q");
		}
		if(o){
			options.append("o");
		}
		if(k){
			options.append("k");
		}
		if(m){
			options.append("m");
		}
		if(b){
			options.append("b");
		}
		if(e){
			options.append("e");
		}
		if(d){
			options.append("d");
		}
		if(n){
			options.append("n");
		}
		if(v){
			options.append("v");
		}
		if(a){
			options.append("a");
		}
		if(f){
			options.append("f");
		}
		if(one){
			options.append("1");
		}
		if(p){
			options.append("p");
		}
		return options.toString();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "import";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getFieldDefineExp(fields));
		
		StringBuffer suffix = new StringBuffer();
		if(StringUtils.isValidString(argk)){
			suffix.append(getNumberExp(argk));
			suffix.append(":");
			suffix.append(getNumberExp(argn));
		}
		suffix.append(",");
		suffix.append(getParamExp(seperator));
		if(suffix.length()>0){
			sb.append(";");
			sb.append(suffix.toString());
		}
		if(sb.length()==2){
			return null;
		}
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,";");
		fields = getFieldDefine( st.nextToken() );
		if(st.hasMoreTokens()){
			String buf = st.nextToken();
			st = new StringTokenizer( buf, ",");
			buf = st.nextToken();
			if(isValidString(buf)){
				int maohao=buf.indexOf(":");
				if(maohao>0){
					argk = getNumber(buf.substring(0,maohao));
					argn = getNumber(buf.substring(maohao+1));
				}else{
					argk = getNumber( buf );
				}
			}
			if(st.hasMoreTokens()){
				seperator = getParam( st.nextToken() );
			}
		}
		return true;
	}

}
