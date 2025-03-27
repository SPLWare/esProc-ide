package com.scudata.ide.spl.etl.element;

import com.scudata.chart.Consts;
import com.scudata.ide.spl.etl.EtlConsts;
import com.scudata.ide.spl.etl.ParamInfo;
import com.scudata.ide.spl.etl.ParamInfoList;

/**
 * ���������༭ xls.xlsimport()
 * ������ǰ׺X��ʾ xls�ļ�����
 * 
 * @author Joancy
 *
 */
public class XXlsImport extends FXlsImport {
	
	/**
	 * ��ȡ���ڽ���༭�Ĳ�����Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		ParamInfoList paramInfos = new ParamInfoList();
		ParamInfo.setCurrent(FXlsImport.class, this);

		paramInfos.add(new ParamInfo("fields", EtlConsts.INPUT_STRINGLIST));
		paramInfos.add(new ParamInfo("sheetName"));
		paramInfos.add(new ParamInfo("argb"));
		paramInfos.add(new ParamInfo("arge"));
		
		String group = "options";
		paramInfos.add(group, new ParamInfo("t", Consts.INPUT_CHECKBOX));
		paramInfos.add(group, new ParamInfo("c", Consts.INPUT_CHECKBOX));

		return paramInfos;
	}

	/**
	 * ��ȡ������
	 * ���͵ĳ�������Ϊ
	 * EtlConsts.TYPE_XXX
	 * @return EtlConsts.TYPE_XLS
	 */
	public byte getParentType() {
		return EtlConsts.TYPE_XLS;
	}

	/**
	 * ��ȡ�ú����ķ�������
	 * @return EtlConsts.TYPE_CURSOR
	 */
	public byte getReturnType() {
		if(c){
			return EtlConsts.TYPE_CURSOR;
		}
		return EtlConsts.TYPE_SEQUENCE;
	}
}
