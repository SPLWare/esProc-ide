package com.scudata.ide.spl.resources;

import java.util.Locale;

import com.scudata.common.MessageManager;

/**
 * ��������Դ
 *
 */
public class IdeSplMessage {

	/**
	 * ˽�й��캯��
	 */
	private IdeSplMessage() {
	}

	/**
	 * ��ȡ��Դ����������
	 * 
	 * @return
	 */
	public static MessageManager get() {
		return get(Locale.getDefault());
	}

	/**
	 * ��ȡָ���������Դ����������
	 * 
	 * @param locale ����
	 * @return
	 */
	public static MessageManager get(Locale locale) {
		return MessageManager.getManager("com.scudata.ide.spl.resources.ideSplMessage", locale);
	}

}
