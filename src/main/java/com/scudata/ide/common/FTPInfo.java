package com.scudata.ide.common;

/**
 * FTP��Ϣ
 *
 */
public class FTPInfo {
	/**
	 * ������
	 */
	private String host;
	/**
	 * �˿�
	 */
	private int port = 21;
	/**
	 * �û���
	 */
	private String user;
	/**
	 * ����
	 */
	private String password;
	/**
	 * Ŀ¼
	 */
	private String directory;
	/**
	 * �Ƿ�ѡ��
	 */
	private boolean selected;

	/**
	 * ȡ������
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * ����������
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * ȡ�˿�
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * ���ö˿�
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * ȡ�û���
	 * 
	 * @return
	 */
	public String getUser() {
		return user;
	}

	/**
	 * �����û���
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * ȡ����
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * ��������
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * ȡĿ¼
	 * 
	 * @return
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param directory
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * �Ƿ�ѡ��
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * �����Ƿ�ѡ��
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
