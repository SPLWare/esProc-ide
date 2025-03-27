package com.scudata.ide.spl.etl;

import java.util.Locale;

import com.scudata.common.*;

/**
 * ������������Դ�༭��
 * @author Joancy
 *
 */
public class FuncMessage {

	private FuncMessage() {
	}

	public static MessageManager get() {
	  return get(Locale.getDefault());
	}

	public static MessageManager get(Locale locale) {
		return MessageManager.getManager(
				"com.scudata.ide.spl.etl.funcMessage", locale);
	}

}
