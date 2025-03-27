package com.scudata.ide.common.swing;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.scudata.common.ArgumentTokenizer;
import com.scudata.common.Logger;
import com.scudata.util.Variant;

/**
 * ���ݹ�����ʾֵҪ�����ΪString���ͣ�������ʾ���� ����ֵ�������������ͣ���Ҫ���JListEx��ͬ
 *
 */
public class JComboBoxEx extends JComboBox {
	private static final long serialVersionUID = 1L;

	/**
	 * �Ƿ�����޸�ֵ
	 */
	private boolean isItemChangeable = true;
	/**
	 * �����б�ģ��
	 */
	public DefaultComboBoxModel data = new DefaultComboBoxModel();
	/**
	 * ����ֵ
	 */
	public Vector<Object> codeData = new Vector<Object>();

	/**
	 * ���캯��
	 */
	public JComboBoxEx() {
		super.setModel(data);
	}

	/**
	 * ���캯��
	 * 
	 * @param items
	 *            �ɶ��ŷָ����������ַ���
	 */
	public JComboBoxEx(String items) {
		this();
		setListData(items, ',');
	}

	/**
	 * ���캯��
	 * 
	 * @param items
	 *            ��delim�ָ����������ַ���
	 * @param delim
	 *            �ָ���
	 */
	public JComboBoxEx(String items, char delim) {
		this();
		setListData(items, delim);
	}

	/**
	 * ���캯��
	 * 
	 * @param items
	 *            ����������
	 */
	public JComboBoxEx(Object[] items) {
		this();
		setListData(items);
	}

	/**
	 * ���캯��
	 * 
	 * @param items
	 *            �������
	 */
	public JComboBoxEx(Vector items) {
		this();
		setListData(items);
	}

	/**
	 * ���캯��
	 * 
	 * @param model
	 *            ������ģ��
	 */
	public JComboBoxEx(DefaultComboBoxModel model) {
		this();
		if (model == null) {
			return;
		}
		setModel(model);
	}

	/**
	 * ȡ��Ա�Ƿ���޸�
	 * 
	 * @return
	 */
	public boolean isItemChangeable() {
		return isItemChangeable;
	}

	/**
	 * ���ó�Ա�Ƿ���޸�
	 * 
	 * @param changeable
	 */
	public void setItemChageable(boolean changeable) {
		isItemChangeable = changeable;
	}

	/**
	 * ���õ�ǰ������Ϊitems
	 *
	 * @param items
	 *            delim�ָ����������ַ���
	 * @param delim
	 *            ���ڷֿ�items���ݵķָ����
	 */
	public void setListData(String items, char delim) {
		if (items == null) {
			return;
		}
		isItemChangeable = false;
		data.removeAllElements();
		ArgumentTokenizer at = new ArgumentTokenizer(items, delim);
		while (at.hasMoreTokens()) {
			data.addElement(at.nextToken());
		}
		isItemChangeable = true;
	}

	/**
	 * ���õ�ǰ������Ϊitems
	 *
	 * @param items
	 *            ��','�ֿ��������б�
	 */
	public void setListData(String items) {
		setListData(items, ',');
	}

	/**
	 * ���õ�ǰ������ΪlistData
	 *
	 * @param listData
	 *            �������ݵĶ�������
	 */
	public void setListData(Object[] listData) {
		if (listData == null) {
			return;
		}
		isItemChangeable = false;
		data.removeAllElements();
		for (int i = 0; i < listData.length; i++) {
			data.addElement(listData[i]);
		}
		isItemChangeable = true;
	}

	/**
	 * ���õ�ǰ������ΪlistData
	 *
	 * @param listData
	 *            �������ݵ�Vector����
	 */
	public void setListData(Vector listData) {
		if (listData == null) {
			return;
		}
		setListData(listData.toArray());
	}

	/**
	 * ���õ�ǰ������Ϊmodel
	 *
	 * @param model
	 *            �������ݵ�ListModel����
	 */
	public void setModel(DefaultComboBoxModel model) {
		if (model == null) {
			return;
		}
		data = model;
		super.setModel(model);
	}

	/**
	 * ���õ�ǰѡ�������
	 */
	public void setSelectedItem(Object anObject) {
		if (anObject == null) {
			return;
		}
		if (codeData.size() > 0) {
			x_setSelectedCodeItem(anObject);
		} else {
			x_setSelectedDispItem(anObject.toString());
		}
	}

	/**
	 * ��õ�ǰ�б���е�������Ŀ��
	 *
	 * @return �ַ�����ʽ����Ŀ�б��б�֮���á������ֿ�
	 */
	public String totalItems() {
		if (data.getSize() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.getSize(); i++) {
			sb.append(data.getElementAt(i) + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * x_ Ϊǰ׺�ĺ���������ʾֵ����ʵֵ��ʾ�����еĺ�����������Ӧ�� x_... ��ʾֵ�������ַ�����ʽ�� ��ʵֵ�������κ�ֵ
	 *
	 * @param codeData
	 * @param dispData
	 */
	public void x_setData(Vector codeData, Vector dispData) {
		isItemChangeable = false;
		// �����������codeData,����ÿؼ���ActionPerformed�¼��еõ���x_getSelectedItem()�������д�

		this.codeData.removeAllElements();
		this.codeData.addAll(codeData);

		data.removeAllElements();
		setListData(dispData);
		isItemChangeable = true;
	}

	/**
	 * ����
	 * 
	 * @param sortByDisp
	 *            �Ƿ�����ʾֵ����
	 * @param ascend
	 *            ����
	 * @return
	 */
	public boolean x_sort(boolean sortByDisp, boolean ascend) {
		int i, j;
		Comparable ci, cj;
		boolean lb_exchange;
		if (sortByDisp) {
			for (i = 0; i < data.getSize(); i++) {
				if (!(data.getElementAt(i) instanceof Comparable)) {
					return false;
				}
			}
		} else {
			for (i = 0; i < codeData.size(); i++) {
				if (!(codeData.get(i) instanceof Comparable)) {
					return false;
				}
			}
		}

		for (i = 0; i < data.getSize() - 1; i++) {
			for (j = i + 1; j < data.getSize(); j++) {
				if (sortByDisp) {
					ci = (Comparable) data.getElementAt(i);
					cj = (Comparable) data.getElementAt(j);
				} else {
					ci = (Comparable) codeData.get(i);
					cj = (Comparable) codeData.get(j);
				}
				if (ascend) {
					lb_exchange = ci.compareTo(cj) > 0;
				} else {
					lb_exchange = ci.compareTo(cj) < 0;
				}
				if (lb_exchange) {
					Object o, o2;
					o = data.getElementAt(i);
					o2 = data.getElementAt(j);
					data.removeElementAt(j);
					data.insertElementAt(o, j);

					data.removeElementAt(i);
					data.insertElementAt(o2, i);

					o = codeData.get(i);
					o2 = codeData.get(j);
					codeData.setElementAt(o2, i);
					codeData.setElementAt(o, j);
				}
			}
		}
		return true;
	}

	/**
	 * ��ӡ
	 * 
	 * @param v
	 */
	public void prints(Object v) {
		Vector vc = null;
		DefaultComboBoxModel vd = null;
		if (v instanceof Vector) {
			vc = (Vector) v;
		} else {
			vd = (DefaultComboBoxModel) v;
		}
		if (vc != null) {
			if (vc.size() < 50) {
				return;
			}
			for (int i = 0; i < vc.size(); i++) {
				Logger.debug(i + "-" + vc.get(i).toString());
			}
		}

		if (vd != null) {
			for (int i = 0; i < vd.getSize(); i++) {
				Logger.debug(vd.getElementAt(i).toString());
			}

		}
	}

	/**
	 * ������ʾֵȡ����ֵ
	 * 
	 * @param dispItem
	 *            ��ʾֵ
	 * @return
	 */
	public Object x_getCodeItem(Object dispItem) {
		if (codeData == null || data == null) {
			return dispItem;
		}
		Object disp;
		int i = 0;
		for (i = 0; i < data.getSize(); i++) {
			disp = data.getElementAt(i);
			if (disp == null) {
				continue;
			}
			if (disp.equals(dispItem)) {
				break;
			}
		}
		if (i >= codeData.size()) {
			return dispItem;
		}
		return codeData.get(i);
	}

	/**
	 * ���ݴ���ֵȡ��ʾֵ
	 * 
	 * @param codeItem
	 *            ����ֵ
	 * @return
	 */
	public String x_getDispItem(Object codeItem) {
		if (codeItem == null) {
			return null;
		}
		if (codeData == null || data == null) {
			return codeItem.toString();
		}
		Object code;
		int i = 0;
		for (i = 0; i < codeData.size(); i++) {
			code = codeData.get(i);
			if (Variant.isEquals(code, codeItem)) { // code.equals(codeItem)) {
				break;
			}
		}
		if (i >= data.getSize()) {
			return codeItem.toString();
		}
		return String.valueOf(data.getElementAt(i));
	}

	/**
	 * ���ô���ֵ����ʾģ��
	 * 
	 * @param codeData
	 *            ����ֵ
	 * @param dispModel
	 *            ��ʾģ��
	 */
	public void x_setModel(Vector codeData, DefaultComboBoxModel dispModel) {
		data = dispModel;
		super.setModel(dispModel);
		this.codeData = codeData;
	}

	/**
	 * ɾ��ȫ����Ա
	 */
	public void x_removeAllElements() {
		data.removeAllElements();
		codeData.removeAllElements();
	}

	/**
	 * �����ɾ����Ա
	 * 
	 * @param i
	 *            ���
	 */
	public void x_removeElement(int i) {
		data.removeElementAt(i);
		codeData.removeElementAt(i);
	}

	/**
	 * ���ӳ�Ա
	 * 
	 * @param code
	 *            ����ֵ
	 * @param disp
	 *            ��ʾֵ
	 */
	public void x_addElement(Object code, String disp) {
		codeData.addElement(code);
		data.addElement(disp);
	}

	/**
	 * ȡ��ǰѡ��Ĵ���ֵ
	 * 
	 * @return
	 */
	public Object x_getSelectedItem() {
		return x_getCodeItem(getSelectedItem());
	}

	/**
	 * ����ѡ�����ʾֵ
	 * 
	 * @param dispItem
	 *            ��ʾֵ
	 */
	public void x_setSelectedDispItem(String dispItem) {
		data.setSelectedItem(dispItem);
	}

	/**
	 * ����ѡ��Ĵ���ֵ
	 * 
	 * @param codeItem
	 *            ����ֵ
	 */
	public void x_setSelectedCodeItem(Object codeItem) {
		x_setSelectedDispItem(x_getDispItem(codeItem));
	}
}
