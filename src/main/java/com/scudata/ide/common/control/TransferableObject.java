package com.scudata.ide.common.control;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * IDE����ʱ���ݵĶ���
 */
public class TransferableObject implements Transferable {
	/**
	 * ���ƵĶ���
	 */
	private Object object;
	/**
	 * DataFlavor����
	 */
	public static final DataFlavor objectFlavor = new DataFlavor(
			TransferableObject.class, "object");

	/**
	 * DataFlavor����
	 */
	static DataFlavor[] flavors = { objectFlavor };

	/**
	 * ���캯��
	 * 
	 * @param object
	 *            ���ƵĶ���
	 */
	public TransferableObject(Object object) {
		this.object = object;
	}

	/**
	 * ȡ���ݵ�DataFlavor����
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	/**
	 * DataFlavor�Ƿ�֧��
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(objectFlavor);
	}

	/**
	 * ��DataFlavorȡ���ݵĶ���
	 */
	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {
		if (flavor.equals(objectFlavor)) {
			return object;
		} else {
			return null;
		}
	}
}
