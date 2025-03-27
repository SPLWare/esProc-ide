package com.scudata.ide.vdb.menu;

public class GCMenu {
//	1
	public static final String CONNECTION = "connection"; // ����(C)

	public static final String CONN_NEW = "conn.new";//�½�
	public static final String CONN_OPEN = "conn.open"; // ��
	public static final String CONN_SAVE = "conn.save"; // ����
	public static final String CONN_CLOSE = "conn.close"; // �ر�
	public static final String CONN_CONFIG = "conn.config"; // ����
	public static final String CONN_DELETE = "conn.delete"; // ɾ��
	public static final String CONN_ACHEIVE = "conn.acheive"; // �鵵
	public static final String CONN_PURGE = "conn.purge"; // ����
	
	public static final String CONN_EXIT = "conn.exit";

	public static final short iCONNECTION = 0;
	public static final short iCONN_NEW = 5; // �½�
	public static final short iCONN_OPEN = 10; // ������
	public static final short iCONN_SAVE = 15; // ����
	public static final short iCONN_CLOSE = 20; // �ر�����
	public static final short iCONN_CONFIG = 30;
	public static final short iCONN_DELETE = 40;
	public static final short iCONN_ACHEIVE = 50;
	public static final short iCONN_PURGE = 60; 
	
	public static final short iCONN_EXIT = 99;// �˳�(X)

//2
	public static final String NODE = "node"; // �ڵ�(N)
	public static final String NODE_COPY = "node.copy"; // ����
	public static final String NODE_PASTE = "node.paste"; //ճ��
	public static final String NODE_DELETE = "node.delete"; // ɾ��
	public static final String NODE_CREATE = "node.create"; //����
	
	public static final short iNODE = 200;//�ڵ�
	public static final short iNODE_COPY = 210; // 
	public static final short iNODE_PASTE = 220; 
	public static final short iNODE_DELETE = 230; 
	public static final short iNODE_CREATE = 240; 
	
//3
	public static final String DATA = "data"; // ����(D)
	public static final String DATA_SAVE = "data.save"; // ����(D)
	public static final String DATA_COPY = "data.copy";
	public static final String DATA_PASTE = "data.paste";
	public static final String DATA_IMPORT = "data.import";
	
	public static final short iDATA = 300; 
	public static final short iDATA_SAVE = 310; 
	public static final short iDATA_COPY = 320; 
	public static final short iDATA_PASTE = 330; 
	public static final short iDATA_IMPORT = 340; 
//	public static final short iDATA_SAVE = 310; 

//4
	public static final String TOOLS = "tools"; // ����(T)
	public static final String TOOLS_BINBROWSER = "tools.binbrowser";
	public static final String TOOLS_OPTION = "tools.option";
	
	public static final short iTOOLS = 400;
	public static final short iTOOLS_BINBROWSER = 440; // ���ļ������
	public static final short iTOOLS_OPTION = 450; // ѡ��(O)
	

//5
	public static final String WINDOW = "window"; // ����(W)
	protected static final String CASCADE = "window.cascade";
	protected static final String TILE_HORIZONTAL = "window.tilehorizontal";
	protected static final String TILE_VERTICAL = "window.tilevertical";
	protected static final String LAYER = "window.layer";
	
	public static final short iWINDOW = 501;
	public static final short iCASCADE = 511;
	public static final short iTILE_HORIZONTAL = 512;
	public static final short iTILE_VERTICAL = 513;
	public static final short iLAYER = 514;
	
	
		// �հ�ͼ������
	public final static String BLANK_ICON = "blank";
	public final static String PRE_MENU = "menu.";

}
