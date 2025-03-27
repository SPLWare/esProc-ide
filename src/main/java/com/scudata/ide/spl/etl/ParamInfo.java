package com.scudata.ide.spl.etl;

import java.lang.reflect.*;
import java.util.ArrayList;

import com.scudata.chart.Consts;
import com.scudata.common.*;

import java.awt.*;

/**
 * ������Ϣ
 * 
 * @author Joancy
 *
 */
public class ParamInfo extends FuncParam {
	private String title;
	private int inputType;
	private Object defValue;// ֻ���༭�õ�ȱʡֵ������ɾ�����ʽ���ø�ֵ��Ⱦ����ֵ�����ȱʡֵһ��

	// ��ǰ���ڻ�ȡ�������࣬��Ҫ�Ӹ����и��ݲ�������ȡ��Ӧ�����Ķ���
	private static transient Class currentClass;
	private static transient Object currentObj;
	private static transient Object defaultObj;

	private MessageManager mm = FuncMessage.get();

	private boolean needCheckEmpty = false;//�Ƿ���Ҫ�������ֵΪ��
	
	/**
	 * ���õ�ǰ����ʵ����ȡ��Դʱ��Ҫ����ʵ�������������
	 * @param objClass Class
	 * @param obj ����
	 */
	public static void setCurrent(Class objClass, Object obj) {
		currentClass = objClass;
		currentObj = obj;
		try {
			defaultObj = objClass.newInstance();
		} catch (Exception e) {
		}
	}

	/**
	 * ���캯��
	 * @param name ������
	 * @param needCheck �Ƿ���Ҫ����ֵ
	 * �������ֵ�����ڱ༭����ʱ��������ֵ�ᱨ������ֹ�����˳�
	 */
	public ParamInfo(String name,boolean needCheck) {
		this(name, Consts.INPUT_NORMAL,needCheck);
	}
	
	/**
	 * ���캯��
	 * @param name ������
	 */
	public ParamInfo(String name) {
		this(name, Consts.INPUT_NORMAL);
	}

	/**
	 * ���캯��
	 * @param name ������
	 * @param inputType ��������
	 */
	public ParamInfo(String name, int inputType) {
		this(name,inputType,false);
	}
	
	/**
	 * ���캯��
	 * @param name ������
	 * @param inputType ��������
	 * @param needCheck �Ƿ����ֵ
	 */
	public ParamInfo(String name, int inputType, boolean needCheck) {
		this.name = name;
		this.needCheckEmpty = needCheck;
		try {
			Field f = currentClass.getDeclaredField(name);
			Object paraValue = f.get(currentObj);
			if (paraValue instanceof Color) {
				this.value = new Integer(((Color) paraValue).getRGB());
			} else {
				this.value = paraValue;
			}
			String className = currentClass.getName();
			int last = className.lastIndexOf('.');
			String prefix = className.substring(last+1);
			this.title = mm.getMessage(prefix+"."+name);
			this.inputType = inputType;
			this.defValue = f.get(defaultObj);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * ��ȡ������ȱʡֵ
	 * @return ȱʡֵ
	 */
	public Object getDefValue() {
		return defValue;
	}

	/**
	 * ���ú����������󣬽�ֵ������ǰʵ��
	 * @param fp ��������
	 */
	public void setFuncParam(FuncParam fp) {
		Object tmp = fp.getValue();
		value = tmp;
	}

	/**
	 * �������⣬������ʾ�ڱ༭����
	 * @return ����
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ��ȡ��������
	 * �����ı༭����������������ṩ��ͬ�ı༭��
	 * @return ��������
	 */
	public int getInputType() {
		return inputType;
	}
	
	/**
	 * ���ղ�������ֵʱ�׳���Ӧ�쳣
	 */
	public void check(){
		if(needCheckEmpty){
			String sValue=null;
			if(value instanceof ArrayList){
				ArrayList al = (ArrayList)value;
				if(al.size()>0){
					Object element = al.get(0);
					if(element instanceof FieldDefine){
						ArrayList<FieldDefine> tmp = (ArrayList<FieldDefine>)value;
						sValue = ObjectElement.getFieldDefineExp2(tmp);
					}else if(element instanceof String){
						ArrayList<String> tmp = (ArrayList<String>)value;
						sValue = ObjectElement.getStringListExp(tmp, ",");
					}
				}
			}else if(value instanceof String){
				sValue = (String)value;
			}
			if(!StringUtils.isValidString( sValue )){
				throw new RuntimeException(mm.getMessage("EmptyWarning",title));
			}
		}
	}

}
