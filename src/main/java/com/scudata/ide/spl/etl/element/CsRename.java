package com.scudata.ide.spl.etl.element;

import com.scudata.ide.spl.etl.EtlConsts;

/**
 * ���������༭ CS.rename()
 * ������ǰ׺Cs��ʾ�α�
 * 
 * @author Joancy
 *
 */
public class CsRename extends ARename {
	
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

}
