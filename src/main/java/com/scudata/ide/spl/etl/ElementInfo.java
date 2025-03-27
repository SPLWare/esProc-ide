package com.scudata.ide.spl.etl;

/**
 * Ԫ����Ϣ��
 * 
 * @author Joancy
 *
 */
public class ElementInfo {
	private String name;
	private String title;
	private Class elementClass;

	transient ObjectElement instance = null;
	
	/**
	 * ���캯��
	 * @param name ����
	 */
	public ElementInfo(String name) {
		this.name = name;
		ElementInfo ei = ElementLib.getElementInfo(name);
		this.title = ei.getTitle();
		this.elementClass = ei.getElementClass();
	}

	/**
	 * ���캯��
	 * @param name ����
	 * @param title ����
	 * @param elementClass ��ӦClass
	 */
	public ElementInfo(String name, String title, Class elementClass) {
		this.name = name;
		this.title = title;
		this.elementClass = elementClass;
	}

	/**
	 * �����ĸ�����
	 * @return ����
	 */
	public byte getParentType(){
		return getInstance().getParentType();
	}
	
	/**
	 * ������ʵ������
	 * @return ��������
	 */
	public String getFuncName(){
		return getInstance().getFuncName();
	}

	/**
	 * �õ�һ����ǰ��Ϣ��Ψһ����Ԫ��ʵ��
	 * @return ʵ��
	 */
	public ObjectElement getInstance(){
		if(instance==null){
			instance = newInstance();
		}
		return instance;
	}
	
	/**
	 * �½�һ��Ԫ��ʵ��
	 * @return ʵ��
	 */
	public ObjectElement newInstance() {
		try {
			ObjectElement oe = (ObjectElement) elementClass.newInstance();
			oe.setElementName( name );
			return oe;
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ����
	 * @return ����
	 */
	public String getName() {
		return name;
	}

	/**
	 * ��ȡ����
	 * @return ����
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ��ȡԪ�ص���Class
	 * @return Class
	 */
	public Class getElementClass() {
		return elementClass;
	}

}
