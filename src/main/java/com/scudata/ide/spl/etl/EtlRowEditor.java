package com.scudata.ide.spl.etl;

import java.awt.*;

import javax.swing.table.*;

import com.scudata.ide.common.swing.*;
import com.scudata.ide.spl.chart.box.EachRowEditor;

/**
 * ���Կ�Ԫ�༭��
 */
public class EtlRowEditor extends EachRowEditor {
	private TableCellEditor fieldDefineEditor;
	private TableCellEditor seperatorEditor, seperatorEditor2;
	private TableCellEditor stringListEditor;
	private TableCellEditor isolationLevelEditor;
	private TableCellEditor aorcsEditor,cellAEditor,cellFileEditor,cellCursorEditor;
	private TableCellEditor cellCtxEditor,dbEditor,cellXlsEditor;

	/**
	 * ���캯��
	 * @param table ����
	 * @param editTypeCol �༭������
	 * @param owner ������
	 */
	public EtlRowEditor(JTableEx table, int editTypeCol, Dialog owner) {
		super(table, editTypeCol, owner);
	}

	/**
	 * ���ݱ༭����ѡ����Ӧ�༭��
	 * @param editType �༭����
	 */
	public TableCellEditor selectEditor(int editType) {
		TableCellEditor editor1 = super.selectEditor(editType);
		if (editor1 != defaultEditor) {
			return editor1;
		}
		switch (editType) {
		case EtlConsts.INPUT_FIELDDEFINE_NORMAL:
		case EtlConsts.INPUT_FIELDDEFINE_EXP_FIELD:
		case EtlConsts.INPUT_FIELDDEFINE_FIELD_EXP:
		case EtlConsts.INPUT_FIELDDEFINE_RENAME_FIELD:
		case EtlConsts.INPUT_FIELDDEFINE_FIELD_DIM:
			if (fieldDefineEditor == null) {
				fieldDefineEditor = new FieldDefineEditor(owner,editType);
			}
			editor1 = fieldDefineEditor;
			break;
		case EtlConsts.INPUT_SEPERATOR:
			if (seperatorEditor == null) {
				JComboBoxEx box = EtlConsts.getSeperatorComboBox(false);
				seperatorEditor = new JComboBoxExEditor(box);
			}
			editor1 = seperatorEditor;
			break;
		case EtlConsts.INPUT_SEPERATOR2:
			if (seperatorEditor2 == null) {
				JComboBoxEx box = EtlConsts.getSeperatorComboBox(true);
				seperatorEditor2 = new JComboBoxExEditor(box);
			}
			editor1 = seperatorEditor2;
			break;
		case EtlConsts.INPUT_STRINGLIST:
			if (stringListEditor == null) {
				stringListEditor = new StringListEditor(owner);
			}
			editor1 = stringListEditor;
			break;
		case EtlConsts.INPUT_ISOLATIONLEVEL:
			if (isolationLevelEditor == null) {
				JComboBoxEx box = EtlConsts.getIsolationLevelBox();
				isolationLevelEditor = new JComboBoxExEditor(box);
			}
			editor1 = isolationLevelEditor;
			break;
		case EtlConsts.INPUT_CELLAORCS:
			if (aorcsEditor == null) {
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_SEQUENCE,EtlConsts.TYPE_CURSOR});
				aorcsEditor = new JComboBoxExEditor(box);
			}
			editor1 = aorcsEditor;
			break;
		case EtlConsts.INPUT_CELLA:
			if (cellAEditor == null) {
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_SEQUENCE});
				cellAEditor = new JComboBoxExEditor(box);
			}
			editor1 = cellAEditor;
			break;
		case EtlConsts.INPUT_CELLXLS:
			if (cellXlsEditor == null) {
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_XLS});
				cellXlsEditor = new JComboBoxExEditor(box);
			}
			editor1 = cellXlsEditor;
			break;
		case EtlConsts.INPUT_CELLFILE:
			if (cellFileEditor == null) {
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_FILE});
				cellFileEditor = new JComboBoxExEditor(box);
			}
			editor1 = cellFileEditor;
			break;
		case EtlConsts.INPUT_CELLBCTX:
			if (cellCtxEditor == null) {
//				ֻ����ͳ��EtlConsts.TYPE_FILE����Ϊû����������ļ�������ɶ���ļ���Ҳ�����Ǳ�����û������չ����ȡ
//				���û��Լ����Ҫѡ��ΪBTX
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_CTX,EtlConsts.TYPE_FILE});
				cellCtxEditor = new JComboBoxExEditor(box);
			}
			editor1 = cellCtxEditor;
			break;
		case EtlConsts.INPUT_CELLCURSOR:
			if (cellCursorEditor == null) {
				JComboBoxEx box = ((DialogFuncEdit)owner).getCellNameDropdownBox(
						new byte[]{EtlConsts.TYPE_CURSOR});
				cellCursorEditor = new JComboBoxExEditor(box);
			}
			editor1 = cellCursorEditor;
			break;
		case EtlConsts.INPUT_DB:
			if (dbEditor == null) {
				JComboBoxEx box = EtlConsts.getDBBox();
				dbEditor = new JComboBoxExEditor(box);
			}
			editor1 = dbEditor;
			break;
		default:
			editor1 = defaultEditor;
			break;
		}
		return editor1;
	}

}
