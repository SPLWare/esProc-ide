package com.scudata.ide.spl;

import com.scudata.ide.common.GC;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ����������
 *
 */
public class GCSpl extends GC {
	/**
	 * �������½���������ǰ׺
	 */
	public final static String PRE_NEWPGM = "p";
	/**
	 * ETL�½���������ǰ׺
	 */
	public final static String PRE_NEWETL = "s";

	/**
	 * ȱʡ���и�
	 */
	public final static int DEFAULT_ROW_HEIGHT = 20;

	/**
	 * �˵�����
	 */
	/**
	 * �ļ�
	 */
	/** �½�SPL(T) */
	public static final String NEW_SPL = "file.newspl";
	/** ���浽FTP(P) */
	public static final String SAVE_FTP = "file.saveftp";
	/** ����SPL�ļ�(I) */
	// public static final String FILE_LOADTXT = "file.loadtxt";
	/** ������SPL�ļ�(E) */
	// public static final String FILE_EXPORTTXT = "file.exporttxt";
	/** ���´��ļ�(R) */
	public static final String FILE_REOPEN = "file.reopen";

	/** �½�SPL(T) */
	public static final short iNEW_SPL = MENU_SPL + 3;
	/** ���浽FTP(P) */
	public static final short iSAVE_FTP = MENU_SPL + 5;
	/** ����SPL�ļ�(I) */
	// public static final short iSPL_IMPORT_TXT = MENU_SPL + 11;
	/** ������SPL�ļ�(E) */
	// public static final short iFILE_EXPORTTXT = MENU_SPL + 21;
	/** ���´��ļ�(R) */
	public static final short iFILE_REOPEN = MENU_SPL + 31;

	/**
	 * �༭(E)
	 */
	public static final String EDIT = "edit";
	/** ����(Z) */
	public static final String UNDO = "edit.undo";
	/** ����(Y) */
	public static final String REDO = "edit.redo";
	/** ���Ʋ˵� */
	public static final String COPY_MENU = "edit.copymenu";
	/** ����ctrl-C */
	public static final String COPY = "edit.copy";
	/** ֵ����ctrl-alt-C */
	public static final String COPYVALUE = "edit.copyvalue";
	/** ���븴��ctrl-shift-C */
	public static final String CODE_COPY = "edit.codecopy";
	/** ���ƿɳ��ִ���(C)�޴���html */
	public static final String COPY_HTML = "edit.copyhtml";
	/** ���ƿɳ��ִ���(P) */
	public static final String COPY_HTML_DIALOG = "edit.copyhtmldialog";
	/** ����ctrl-X */
	public static final String CUT = "edit.cut";
	/** ճ���˵� */
	public static final String PASTE_MENU = "edit.pastemenu";
	/** ճ��ctrl-V */
	public static final String PASTE = "edit.paste";
	/** ��Ǩճ��ctrl-alt-V */
	public static final String PASTE_ADJUST = "edit.pasteadjust";
	/** Ctrl-Shift-V */
	public static final String PASTE_SPECIAL = "edit.pastespecial";
	/** ����ճ��ctrl-B */
	public static final String PASTE_INSERT = "edit.pasteinsert";
	/** �����Ǩճ��ctrl-alt-B */
	public static final String PASTE_ADJUST_INSERT = "edit.pasteadjustinsert";
	/** ���� */
	public static final String INSERT = "edit.insert";
	/** ������(R) */
	public static final String INSERT_ROW = "edit.insertrow";
	/** ׷����(A) */
	public static final String ADD_ROW = "edit.addrow";
	/** ������ */
	public static final String DUP_ROW = "edit.duprow";
	/** ������ */
	public static final String DUP_ROW_ADJUST = "edit.duprowadjust";
	/** ������(C) */
	public static final String INSERT_COL = "edit.insertcol";
	/** ׷����(D) */
	public static final String ADD_COL = "edit.addcol";
	/** �س�����(E)ctrl-enter */
	public static final String CTRL_ENTER = "edit.ctrlenter";
	/** ���Ƶ�Ԫ��(I)ctrl-insert */
	public static final String CTRL_INSERT = "edit.ctrlinsert";
	/** ���Ƶ�Ԫ��(S)alt-insert */
	public static final String ALT_INSERT = "edit.altinsert";
	/** ɾ��(D) */
	public static final String DELETE = "edit.delete";
	/** ��� */
	public static final String CLEAR = "edit.clear";
	/** ȫ����� */
	public static final String FULL_CLEAR = "edit.fullclear";
	/** ɾ����(R) */
	public static final String DELETE_ROW = "edit.deleterow";
	/** ɾ����(C) */
	public static final String DELETE_COL = "edit.deletecol";
	/** �˸�(B)ctrl-backspace */
	public static final String CTRL_BACK = "edit.ctrlback";
	/** ����ɾ��(D)ctrl-delete */
	public static final String CTRL_DELETE = "edit.ctrldelete";
	/** �ƶ�����(M) */
	public static final String MOVE_COPY = "edit.movecopy";
	/** �����ƶ����� */
	public static final String MOVE_COPY_UP = "edit.movecopyup";
	/** �����ƶ����� */
	public static final String MOVE_COPY_DOWN = "edit.movecopydown";
	/** �����ƶ����� */
	public static final String MOVE_COPY_LEFT = "edit.movecopyleft";
	/** �����ƶ����� */
	public static final String MOVE_COPY_RIGHT = "edit.movecopyright";
	/** �ı��༭�� */
	public static final String TEXT_EDITOR = "edit.texteditor";
	/** ע�� ctrl-/ */
	public static final String NOTE = "edit.note";
	/** ��ʽ(O) */
	public static final String FORMAT = "edit.format";
	/** �и�(H) */
	public static final String ROW_HEIGHT = "edit.rowheight";
	/** �����и�(R) */
	public static final String ROW_ADJUST = "edit.rowadjust";
	/** ������(I) */
	public static final String ROW_HIDE = "edit.rowhide";
	/** ȡ��������(V) */
	public static final String ROW_VISIBLE = "edit.rowvisible";
	/** �п�(W) */
	public static final String COL_WIDTH = "edit.colwidth";
	/** �����п�(C) */
	public static final String COL_ADJUST = "edit.coladjust";
	/** ������(D) */
	public static final String COL_HIDE = "edit.colhide";
	/** ȡ��������(S) */
	public static final String COL_VISIBLE = "edit.colvisible";
	/** ͳ��ͼ(G) */
	public static final String CHART = "edit.chart";
	/** ���񻭲�(G) */
	public static final String CANVAS = "edit.canvas";
	/** ����(L) */
	public static final String ZOOM = "edit.zoom";
	/** ����(F) */
	public static final String SEARCH = "edit.search";
	/** �滻(R) */
	public static final String REPLACE = "edit.replace";

	/** ����(Z) */
	public static final short iUNDO = MENU_SPL + 101;
	/** ����(Y) */
	public static final short iREDO = MENU_SPL + 103;
	/** ����ctrl-C */
	public static final short iCOPY = MENU_SPL + 111;
	/** ֵ����ctrl-alt-C */
	public static final short iCOPYVALUE = MENU_SPL + 113;
	/** ���븴��ctrl-shift-C */
	public static final short iCODE_COPY = MENU_SPL + 115;
	/** ���ƿɳ��ִ���(C)�޴���html */
	public static final short iCOPY_HTML = MENU_SPL + 117;
	/** ���ƿɳ��ִ���(P) */
	public static final short iCOPY_HTML_DIALOG = MENU_SPL + 119;
	/** ����ctrl-X */
	public static final short iCUT = MENU_SPL + 121;
	/** ճ��ctrl-V */
	public static final short iPASTE = MENU_SPL + 123;
	/** ��Ǩճ��ctrl-alt-V */
	public static final short iPASTE_ADJUST = MENU_SPL + 125;
	/** Ctrl-Shift-V */
	public static final short iPASTE_SPECIAL = MENU_SPL + 127;
	/** ������(C) */
	public static final short iINSERT_COL = MENU_SPL + 131;
	/** ׷����(D) */
	public static final short iADD_COL = MENU_SPL + 133;
	/** ������ */
	public static final short iDUP_ROW = MENU_SPL + 141;
	/** ������ */
	public static final short iDUP_ROW_ADJUST = MENU_SPL + 143;
	/** �س�����(E)ctrl-enter */
	public static final short iCTRL_ENTER = MENU_SPL + 145;
	/** ���Ƶ�Ԫ��(I)ctrl-insert */
	public static final short iCTRL_INSERT = MENU_SPL + 151;
	/** ���Ƶ�Ԫ��(S)alt-insert */
	public static final short iALT_INSERT = MENU_SPL + 153;
	/** ��� */
	public static final short iCLEAR = MENU_SPL + 160;
	/** ȫ����� */
	public static final short iFULL_CLEAR = MENU_SPL + 162;
	/** ɾ����(R) */
	public static final short iDELETE_ROW = MENU_SPL + 164;
	/** ɾ����(C) */
	public static final short iDELETE_COL = MENU_SPL + 165;
	/** �˸�(B)ctrl-backspace */
	public static final short iCTRL_BACK = MENU_SPL + 167;
	/** ����ɾ��(D)ctrl-delete */
	public static final short iCTRL_DELETE = MENU_SPL + 169;
	/** �ı��༭�� */
	public static final short iTEXT_EDITOR = MENU_SPL + 171;
	/** �����ƶ����� */
	public static final short iMOVE_COPY_UP = MENU_SPL + 175;
	/** �����ƶ����� */
	public static final short iMOVE_COPY_DOWN = MENU_SPL + 176;
	/** �����ƶ����� */
	public static final short iMOVE_COPY_LEFT = MENU_SPL + 177;
	/** �����ƶ����� */
	public static final short iMOVE_COPY_RIGHT = MENU_SPL + 178;
	/** ע�� ctrl-/ */
	public static final short iNOTE = MENU_SPL + 180;
	/** �и�(H) */
	public static final short iROW_HEIGHT = MENU_SPL + 182;
	/** �����и�(R) */
	public static final short iROW_ADJUST = MENU_SPL + 183;
	/** ������(I) */
	public static final short iROW_HIDE = MENU_SPL + 184;
	/** ȡ��������(V) */
	public static final short iROW_VISIBLE = MENU_SPL + 185;
	/** �п�(W) */
	public static final short iCOL_WIDTH = MENU_SPL + 186;
	/** �����п�(C) */
	public static final short iCOL_ADJUST = MENU_SPL + 187;
	/** ������(D) */
	public static final short iCOL_HIDE = MENU_SPL + 188;
	/** ȡ��������(S) */
	public static final short iCOL_VISIBLE = MENU_SPL + 189;
	/** ͳ��ͼ(G) */
	public static final short iEDIT_CHART = MENU_SPL + 191;
	/** ����(L) */
	public static final short iZOOM = MENU_SPL + 194;
	/** ����(F) */
	public static final short iSEARCH = MENU_SPL + 195;
	/** �滻(R) */
	public static final short iREPLACE = MENU_SPL + 197;

	/**
	 * ����(P)
	 */
	public static final String PROGRAM = "program";
	/** �������(P) */
	public static final String PARAM = "program.param";
	/** �������񻷾� */
	public static final String RESET_CELLSET = "program.resetcellset";
	/** ����ȫ�ֻ��� */
	public static final String RESET_GLOBAL = "program.resetglobal";
	/** ִ�� */
	public static final String EXEC = "program.exec";
	/** ����ִ�� */
	public static final String EXE_DEBUG = "program.exe_debug";
	/** ִ�е���� */
	public static final String STEP_CURSOR = "program.stepcursor";
	/** ����ִ�� */
	public static final String STEP_NEXT = "program.stepnext";
	/** �������� */
	public static final String STEP_INTO = "program.stepinto";
	/** �������� */
	public static final String STEP_RETURN = "program.stepreturn";
	/** �жϵ�������(T) */
	public static final String STEP_STOP = "program.stepstop";
	/** ��ִͣ�� */
	public static final String PAUSE = "program.pause";
	/** ����ִ�� */
	public static final String CONTINUE = "program.continue";
	/** ִֹͣ�� */
	public static final String STOP = "program.stop";
	/** ����/ȡ���ϵ� */
	public static final String BREAKPOINTS = "program.breakpoints";
	/** ���㵱ǰ������ */
	public static final String CALC_LOCK = "program.execlock";
	/** �����ֵ */
	public static final String CALC_AREA = "program.execarea";
	/** ��ʾ��ֵ */
	public static final String SHOW_VALUE = "programe.showcellvalue";
	/** �����ֵ(C) */
	public static final String CLEAR_VALUE = "program.clearvalue";
	/** ͼ�λ���(A) */
	public static final String DRAW_CHART = "program.drawchart";

	/** �������(P) */
	public static final short iPARAM = MENU_SPL + 201;
	/** �������񻷾� */
	public static final short iRESET_CELLSET = MENU_SPL + 210;
	/** ����ȫ�ֻ��� */
	public static final short iRESET_GLOBAL = MENU_SPL + 213;
	/** ִ�� */
	public static final short iEXEC = MENU_SPL + 221;
	/** ����ִ�� */
	public static final short iEXE_DEBUG = MENU_SPL + 223;
	/** ִ�е���� */
	public static final short iSTEP_CURSOR = MENU_SPL + 225;
	/** ����ִ�� */
	public static final short iSTEP_NEXT = MENU_SPL + 231;
	/** �������� */
	public static final short iSTEP_INTO = MENU_SPL + 233;
	/** �������� */
	public static final short iSTEP_RETURN = MENU_SPL + 235;
	/** �жϵ�������(T) */
	public static final short iSTEP_STOP = MENU_SPL + 237;
	/** ��ͣ�ͼ���ִ�� */
	public static final short iPAUSE = MENU_SPL + 241;
	/** ִֹͣ�� */
	public static final short iSTOP = MENU_SPL + 243;
	/** ����/ȡ���ϵ� */
	public static final short iBREAKPOINTS = MENU_SPL + 251;
	/** ���㵱ǰ������ */
	public static final short iCALC_LOCK = MENU_SPL + 261;
	/** �����ֵ */
	public static final short iCALC_AREA = MENU_SPL + 263;
	/** ��ʾ��ֵ */
	public static final short iSHOW_VALUE = MENU_SPL + 265;
	/** �����ֵ(C) */
	public static final short iCLEAR_VALUE = MENU_SPL + 267;
	/** ͼ�λ���(A) */
	public static final short iDRAW_CHART = MENU_SPL + 271;

	/** ����(T) */
	public static final String TOOL = "tool";
	/** ������(N) */
	public static final String CONST = "program.const";
	/** ���ļ��в����滻 */
	public static final String FILE_REPLACE = "tool.filereplace";

	/** ������(N) */
	public static final short iCONST = MENU_SPL + 301;
	/** ���ļ��в����滻 */
	public static final short iFILE_REPLACE = MENU_SPL + 341;

	/** ������ */
	public static final String TITLE_NAME = IdeSplMessage.get().getMessage(
			"jtablevalue.name");
	/** ����ֵ */
	public static final String TITLE_PROP = IdeSplMessage.get().getMessage(
			"jtablevalue.property");
	/** ����Դ���� */
	public static final String DB_NAME = IdeSplMessage.get().getMessage(
			"jtablevalue.dbname");
	/** �û��� */
	public static final String USER = IdeSplMessage.get().getMessage(
			"jtablevalue.user");
	/** ���� */
	public static final String PASSWORD = IdeSplMessage.get().getMessage(
			"jtablevalue.password");
	/** ���ݿ����� */
	public static final String DB_TYPE = IdeSplMessage.get().getMessage(
			"jtablevalue.dbtype");
	/** �������� */
	public static final String DRIVER = IdeSplMessage.get().getMessage(
			"jtablevalue.driver");
	/** ����ԴURL */
	public static final String URL = IdeSplMessage.get().getMessage(
			"jtablevalue.url");
	/** ��������ģʽ */
	public static final String USE_SCHEMA = IdeSplMessage.get().getMessage(
			"jtablevalue.useschema");
	/** ���������޶��� */
	public static final String ADD_TILDE = IdeSplMessage.get().getMessage(
			"jtablevalue.addtilde");

	public static final int[] DEFAULT_SCALES = new int[] { 10, 25, 40, 55, 70,
			85, 100, 115, 130, 145, 160, 175, 190, 205, 220, 235, 250, 265,
			280, 295, 310, 325, 340, 355, 370, 385, 400 };

}
