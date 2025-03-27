package com.scudata.ide.spl.etl;

import java.util.*;

import com.scudata.common.*;

/**
 * ������Ϣ�б�
 * 
 * �����Ķ���Ԫ�ص����в����ļ���
 * @author Joancy
 *
 */
public class ParamInfoList {
	ArrayList<ArrayList<ParamInfo>> paramGroups = new ArrayList<ArrayList<ParamInfo>>();
	ArrayList<String> groupNames = new ArrayList<String>();

	ArrayList<ParamInfo> rootList = new ArrayList<ParamInfo>();
	private final static String ROOTGROUP = "RootGroup";
	private MessageManager mm = FuncMessage.get();

	/**
	 * ���캯��
	 */
	public ParamInfoList() {
	}

	/**
	 * ��ȡ��������Ϣ�б�
	 * ����Ԫ�صĲ���ͨ���Ƚ϶࣬���ԻὫ���������Է���༭
	 * ����ͨ�õĻ�ֵ�����
	 * @return ������Ϣ�б�
	 */
	private ArrayList<ParamInfo> getrootList() {
		return rootList;
	}

	/**
	 * ׷�����в�����Ϣ
	 * @param pil ��һ��������Ϣ�б�
	 */
	public void addAll(ParamInfoList pil) {
		//pil�з�������Ҫ��������������ͬ���ƺϲ�
		ArrayList<String> groupNames = pil.getGroupNames();
		for( int i=0; i<groupNames.size(); i++){
			String grpName = groupNames.get(i);
			ArrayList<ParamInfo> grpParams = pil.getParams(grpName);
			for( int n=0;n<grpParams.size(); n++){
				add(grpName,grpParams.get(n));
			}
		}
		rootList.addAll(pil.getrootList());
	}

	/**
	 * ��������Ϣpi׷�ӵ�����group����
	 * @param group ������
	 * @param pi ������Ϣ
	 */
	public void add(String group, ParamInfo pi) {
		ArrayList<ParamInfo> pis = null;
		if (group == null || ROOTGROUP.equalsIgnoreCase(group)) {
			pis = rootList;
		} else {
			group = mm.getMessage(group);
			int index = groupNames.indexOf(group);
			if (index < 0) {
				groupNames.add(group);
				pis = new ArrayList<ParamInfo>();
				paramGroups.add(pis);
			} else {
				pis = paramGroups.get(index);
			}
		}
		if (pis == null) {
			pis = rootList;
		}
		pis.add(pi);
	}

	/**
	 * ɾ����group�µ���Ϊname�Ĳ���
	 * @param group ������
	 * @param name ��������
	 */
	public void delete(String group, String name){
		group = mm.getMessage(group);
		delete(getParams(group),name);
	}
	
	/**
	 * ����������һ��������Ϣ
	 * @param pi ������Ϣ
	 */
	public void add(ParamInfo pi) {
		rootList.add(pi);
	}
	/**
	 * ɾ�������µ���Ϊname�Ĳ���
	 * @param name ��������
	 */
	public void delete(String name){
		delete(rootList,name);
	}
	
	/**
	 * ɾ��������Ϣ�б�list�е���Ϊname�Ĳ���
	 * @param list ������Ϣ�б�
	 * @param name ��������
	 */
	public void delete(ArrayList<ParamInfo> list,String name){
		for(int i=0; i<list.size();i++){
			ParamInfo pi = list.get(i);
			if(pi.getName().equalsIgnoreCase(name)){
				list.remove(i);
				return;
			}
		}
	}

	/**
	 * ��ȡ�������������
	 * @return �������б�
	 */
	public ArrayList<String> getGroupNames() {
		return groupNames;
	}

	/**
	 * ��ȡ��ΪgroupName��������в�����Ϣ
	 * @param groupName ������
	 * @return ������Ϣ�б�
	 */
	public ArrayList<ParamInfo> getParams(String groupName) {
		ArrayList<ParamInfo> pis = null;
		int index = groupNames.indexOf(groupName);
		if (index >= 0) {
			pis = paramGroups.get(index);
		}
		if (pis != null)
			return pis;
		return rootList;
	}

	/**
	 * ��ȡ����Ĳ�����Ϣ
	 * @return ������Ϣ�б�
	 */
	public ArrayList<ParamInfo> getRootParams() {
		return rootList;
	}

	/**
	 * �����в������ҵ���Ϊname�Ĳ�����Ϣ
	 * @param name ��������
	 * @return ������Ϣ���Ҳ���ʱ����null
	 */
	public ParamInfo getParamInfoByName(String name) {
		ArrayList<ParamInfo> aps = getAllParams();
		int infoSize = aps.size();
		for (int i = 0; i < infoSize; i++) {
			ParamInfo pi = (ParamInfo) aps.get(i);
			if (pi.getName().equalsIgnoreCase(name)) {
				return pi;
			}
		}
		return null;
	}

	/**
	 * ��ȡ�������ȫ��������Ϣ
	 * @return ������Ϣ�б�
	 */
	public ArrayList<ParamInfo> getAllParams() {
		ArrayList<ParamInfo> aps = new ArrayList<ParamInfo>();
		int size = rootList == null ? 0 : rootList.size();
		for (int i = 0; i < size; i++) {
			aps.add(rootList.get(i));
		}
		size = paramGroups == null ? 0 : paramGroups.size();
		for (int i = 0; i < size; i++) {
			ArrayList<ParamInfo> pis = paramGroups.get(i);
			int ps = pis == null ? 0 : pis.size();
			for (int j = 0; j < ps; j++) {
				aps.add(pis.get(j));
			}
		}
		return aps;
	}
	
	/**
	 * �����в���ִ�п�ֵ���
	 */
	public void check(){
		ArrayList<ParamInfo> all = getAllParams();
		for(ParamInfo pi:all){
			pi.check();
		}
	}

}
