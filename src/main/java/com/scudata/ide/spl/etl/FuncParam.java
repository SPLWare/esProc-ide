package com.scudata.ide.spl.etl;

import com.scudata.common.*;
import com.scudata.dm.*;
import com.scudata.util.*;

/**
 * �����Ĳ�������ֵ��
 * 
 * valueΪ�ַ�����ʱ��A1��ʾ���봮��=A1��ʾ������ʽ 
 * �洢��plot�ַ�������ʱ���ֱ�Ϊ�� "A1"�� A1
 */
public class FuncParam {
	// �������ƣ��ò�������ֱ��Ϊ�༭Ԫ����������
	protected String name;
	protected Object value;

	/**
	 * ���캯��
	 */
	public FuncParam() {
	}

	/**
	 * ���캯��
	 * @param name ������
	 * @param value ����ֵ
	 */
	public FuncParam(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * ���ò�������
	 * @param name ����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ������
	 * @return ����
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���ò���ֵ
	 * @param value ����ֵ
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * ��ȡ����ֵ
	 * @return ����ֵ
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * ���ñ��ʽ�еı༭����ת��Ϊ��Ӧ�Ĳ���ֵ
	 * @param editString �༭��
	 */
	public void setEditString(String editString) {
		String tmp = editString;

		boolean removeEscape = !tmp.startsWith("["); // ��������У������ʱ����ȥ����
		value = Variant.parse(tmp, removeEscape);
		if (value instanceof String && Variant.isEquals(tmp, value)) {
			value = "=" + value;
		}
	}

	private String stringValueToEdit(String val) {
		if (val.startsWith("=")) {
			return val.substring(1);
		} else {
			return Escape.addEscAndQuote(val);
		}
	}

	private String seriesValueToEdit(Sequence seq) {
		StringBuffer sb = new StringBuffer("[");
		int size = seq.length();
		for (int i = 1; i <= size; i++) {
			if (i > 1) {
				sb.append(",");
			}
			Object o = seq.get(i);
			if (o instanceof Sequence) {
				sb.append(seriesValueToEdit((Sequence) o));
			} else if (o instanceof String) {
				sb.append(stringValueToEdit((String) o));
			} else {
				sb.append(Variant.toString(o));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * ������ֵת��Ϊ�༭��
	 * @param defValue��ȱʡֵ������ֵΪȱʡֵʱ����Ҫת����ȥ
	 * @return
	 */
	public String toEditString(Object defValue) {
		if (value == null) {
			return null;
		}
		if (StringUtils.isSpaceString(value.toString())) {
			return null;
		}
		if (Variant.isEquals(value, defValue)) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		if (value instanceof String) {
			String tmp = (String) value;
			sb.append(stringValueToEdit(tmp));
		} else if (value instanceof Sequence) {
			sb.append(seriesValueToEdit((Sequence) value));
		} else {
			sb.append(Variant.toString(value));
		}
		return sb.toString();
	}

	/**
	 * ʵ�ָ����toString�ӿ�
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (value != null) {
			sb.append(value);
		}
		return sb.toString();
	}

}
