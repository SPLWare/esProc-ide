package com.scudata.ide.spl.etl.element;

import java.util.StringTokenizer;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ObjectElement;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ connect(driver,url)
 * 
 * @author Joancy
 *
 */
public class ConnectDriver extends ObjectElement {
	public String driver;
	public String URL;
	public String level;
	
	public boolean l;
	public boolean e;

	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(ConnectDriver.class, this);

		paramInfos.add(new ParamInfo("driver",true));
		paramInfos.add(new ParamInfo("URL",true));
		paramInfos.add(new ParamInfo("level",EtlConsts.INPUT_ISOLATIONLEVEL));
		String group = "options";
		paramInfos.add(group, new ParamInfo("l", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("e", Consts.INPUT_CHECKBOX));

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
	 * @return EtlConsts.TYPE_DB
	 */
	public byte getReturnType() {
		return EtlConsts.TYPE_DB;
	}

	/**
	 * ��ȡ��������SPL���ʽ��ѡ�
	 */
	public String optionString(){
		StringBuffer sb = new StringBuffer();
		if(e){
			sb.append("e");
		}
		if(l){
			sb.append("l");
		}
		if(level!=null){
			sb.append(level);
		}
		return sb.toString();
	}
	
	/**
	 * ���Ǹ�������ú�����ѡ��
	 * �����е�level�����ַ������ĸ��뼶�𣬸�booleanѡ��ֵ��ͨ�ã�������Ҫ���⴦��
	 * @param options ѡ��
	 */
	public void setOptions(String options){
		if(options==null){
			return;
		}
		boolean isE = options.indexOf("e")>-1;
		if(isE){
			e = true;
			options = options.replace('e', ' ');
		}
		boolean isL = options.indexOf("l")>-1;
		if(isL){
			l = true;
			options = options.replace('l', ' ');
		}
		level = options.trim();
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 */
	public String getFuncName() {
		return "connect";
	}

	/**
	 * ��ȡ��������SPL���ʽ�ĺ�����
	 * ��setFuncBody���溯����Ȼ����ʽ�ĸ�ֵҲ���ǻ����
	 */
	public String getFuncBody() {
		StringBuffer sb = new StringBuffer();
		sb.append(getParamExp(driver));
		sb.append(",");
		sb.append(getParamExp(URL));
		return sb.toString();
	}

	/**
	 * ���ú�����
	 * @param funcBody ������
	 */
	public boolean setFuncBody(String funcBody) {
		StringTokenizer st = new StringTokenizer(funcBody,",");
		driver = getParam( st.nextToken() );
		URL = getParam( st.nextToken() );
		return true;
	}
}
