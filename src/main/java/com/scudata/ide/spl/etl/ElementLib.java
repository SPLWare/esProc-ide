package com.scudata.ide.spl.etl;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.scudata.common.MessageManager;

/**
 * �Ѿ�ʵ���˸����༭�ĺ���
 * 
 * ����Ҫ��̬�Ǽǵ�Ԫ�ؿ�
 * 
 * @author Joancy
 *
 */
public class ElementLib {
	private static ArrayList<ElementInfo> elements = new ArrayList<ElementInfo>(
			20);
	private static MessageManager mm = FuncMessage.get();

	static {
		loadSystemElements();
	}

	private static int indexof(String name) {
		int size = elements.size();
		for (int i = 0; i < size; i++) {
			ElementInfo ei = elements.get(i);
			if (ei.getName().equalsIgnoreCase(name))
				return i;
		}
		return -1;
	}

	/**
	 * ���������ҵ���Ӧ��Ԫ����Ϣ
	 * 
	 * @param name
	 *            ������
	 * @return Ԫ����Ϣ
	 */
	public static ElementInfo getElementInfo(String name) {
		int i = indexof(name);
		if (i >= 0)
			return elements.get(i);
		return null;
	}

	/**
	 * ׷��һ��Ԫ��
	 * 
	 * @param name
	 *            Ԫ����
	 */
	public static void addElement(String name) {
		try {
			String packageName = "com.scudata.ide.spl.etl.element.";
			String className = packageName + name;
			Class elemClass = Class.forName(className);
			String title = mm.getMessage(name);

			ElementInfo ei = new ElementInfo(name, title, elemClass);
			int i = indexof(name);
			if (i >= 0) {
				elements.add(i, ei);
			} else {
				elements.add(ei);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * ���ݸ������г�������غ�����Ϣ���б�
	 * 
	 * @param parentType
	 *            ������
	 * @return ��Ӧ��Ԫ����Ϣ�б�
	 */
	public static ArrayList<ElementInfo> getElementInfos(byte parentType) {
		ArrayList<ElementInfo> eis = new ArrayList<ElementInfo>();
		for (ElementInfo ei : elements) {
			if (ei.getParentType() == parentType) {
				eis.add(ei);
			}
		}
		return eis;
	}

	/**
	 * ���ݺ��������г�ͬ����Ԫ����Ϣ������group���α��Լ�������Ͷ��иú���
	 * 
	 * @param funcName
	 *            ������
	 * @return Ԫ����Ϣ
	 */
	public static ArrayList<ElementInfo> getElementInfos(String funcName) {
		ArrayList<ElementInfo> eis = new ArrayList<ElementInfo>();
		for (ElementInfo ei : elements) {
			if (ei.getFuncName().equals(funcName)) {
				eis.add(ei);
			}
		}
		return eis;
	}

	/**
	 * װ��ϵͳĿ¼��ȫ��ʵ�ָ����༭�ĺ���Ԫ�� ·����/com/scudata/ide/spl/etl/element
	 */
	public static void loadSystemElements() {
		String names = "ACreate,ADelete,ADerive,AGroup,AGroup2,AGroups,AInsert,"
				+ "AJoin,AKeys,ANew,ANews,ARename,ARun,ASelect,"
				+ "ConnectDB,ConnectDriver,Create,CsDerive,CsFetch,CsGroup,CsGroups,"
				+ "CsGroupx,CsJoin,CsJoinx,CsNew,CsNews,CsRename,CsRun,CsSortx,DCursorSQL,DExecute,"
				+ "DQuerySQL,DUpdate,FCreate,FCursor,FExport,File,FImport,FOpen,FXlsExport,"
				+ "FXlsImport,FXlsOpen,FXlsWrite,Joinx,TAppend,TAttach,TClose,TCursor,TUpdate,"
				+ "XXlsClose,XXlsExport,XXlsImport";
		StringTokenizer st = new StringTokenizer(names,",");
		while (st.hasMoreTokens()) {
			String name = st.nextToken();
			addElement(name);
		}
	}

}
