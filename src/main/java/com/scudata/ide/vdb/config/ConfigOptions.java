package com.scudata.ide.vdb.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.scudata.common.StringUtils;
import com.scudata.ide.vdb.commonvdb.*;

/**
 * ѡ������
 * 
 * @author wunan
 *
 */
public class ConfigOptions {
	// �Զ����ļ�
	public static Boolean bAutoOpen = Boolean.TRUE;
	// ������ʽ
	public static Byte iLookAndFeel = new Byte(LNFManager.LNF_NIMBUS);
	// ����쳣����־
	public static Boolean bLogException = Boolean.TRUE;
	// ���䴰��λ�ô�С
	public static Boolean bWindowSize = Boolean.TRUE;
	// �ӹܿ���̨
	public static Boolean bHoldConsole = Boolean.FALSE;
	// ��־�ļ�
	public static String sLogFileName = "/log/vdb.log";
	// �����Ŀ¼
	public static String sLastDirectory = System.getProperty("user.home");
	// ��������Ȩ
	public static String sEsprocLic = GC.PATH_CONFIG + "/�������ڲ����԰�.lic";

	// ѡ��
	public static Map<String, Object> options = new HashMap<String, Object>();
	// �����洢����λ�ô�С���ַ���
	public static Map<String, String> dimensions = new HashMap<String, String>();

	// ������Ϣ
	public static Map<String, String> connections = new HashMap<String, String>();

	static {
		putOptions();
	}

	private static void putOptions() {
		options.put("bAutoOpen", bAutoOpen);
		options.put("bLogException", bLogException);
		options.put("bWindowSize", bWindowSize);
		options.put("bHoldConsole", bHoldConsole);
		options.put("iLookAndFeel", iLookAndFeel);
		options.put("sLogFileName", sLogFileName);
		options.put("sLastDirectory", sLastDirectory);
		options.put("sEsprocLic", sEsprocLic);
	}

	public static void load() throws Exception {
		try {
			ConfigFile.load();
			Iterator<String> it = options.keySet().iterator();
			String key;
			Object value;
			while (it.hasNext()) {
				key = it.next();
				value = options.get(key);
				loadOption(key, value);
			}
			applyOptions();
		} catch (Exception ex) {
//			GM.showException(ex);
			ConfigFile.newInstance();
		}
	}

	private static void loadOption(String option, Object value) {
		if (!StringUtils.isValidString(value)) {
			return;
		}
		String val = (String) value;
		String type = option.substring(0, 1);
		if (type.equalsIgnoreCase("i")) {
			Integer i = Integer.valueOf(val);
			if (option.equalsIgnoreCase("iLookAndFeel")) {
				iLookAndFeel = new Byte(i.byteValue());
			}
		} else if (type.equalsIgnoreCase("b")) {
			Boolean b = Boolean.valueOf(val);
			if (option.equalsIgnoreCase("bLogException")) {
				bLogException = b;
			} else if (option.equalsIgnoreCase("bWindowSize")) {
				bWindowSize = b;
			} else if (option.equalsIgnoreCase("bAutoOpen")) {
				bAutoOpen = b;
			} else if (option.equalsIgnoreCase("bHoldConsole")) {
				bHoldConsole = b;
			}
		} else if (StringUtils.isValidString(val)) {
			if (option.equalsIgnoreCase("sLogFileName")) {
				sLogFileName = val;
			} else if (option.equalsIgnoreCase("sLastDirectory")) {
				sLastDirectory = val;
			} else if (option.equalsIgnoreCase("sEsprocLic")) {
				sEsprocLic = val;
			}
		}
	}

	public static boolean save() throws Exception {
		putOptions();
		ConfigFile.save();
		return applyOptions();
	}

	public static boolean applyOptions() {
		return true;
	}
}
