package com.scudata.ide.spl.base;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.scudata.cellset.datamodel.PgmCellSet;
import com.scudata.ide.common.EditListener;
import com.scudata.ide.common.GM;
import com.scudata.ide.spl.GVSpl;
import com.scudata.ide.spl.resources.IdeSplMessage;

/**
 * ֵ���
 */
public class PanelValue extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * �������
	 */
	public JScrollPane spValue;

	/**
	 * ���ؼ�
	 */
	public JTableValue tableValue;

	/**
	 * ֵ���������
	 */
	public PanelValueBar valueBar;

	/**
	 * ������
	 */
	public JScrollBar sbValue;

	/**
	 * ��ֹ�仯
	 */
	public boolean preventChange = false;
	/**
	 * ��Ԫ�����ִ��ʱ��
	 */
	private JLabel jLCellTime = new JLabel();

	/**
	 * ��Ԫ�����ִ��ʱ����
	 */
	private JLabel jLInterval = new JLabel();

	private JLabel jLDispRows1 = new JLabel(IdeSplMessage.get().getMessage(
			"panelvalue.disprows1"));
	private JLabel jLDispRows2 = new JLabel(IdeSplMessage.get().getMessage(
			"panelvalue.disprows2"));

	/**
	 * ��ʾ������������
	 */
	private JSpinner jSDispRows = new JSpinner(new SpinnerNumberModel(100, 1,
			Integer.MAX_VALUE, 1));

	/**
	 * �α�������ݰ�ť
	 */
	private JButton jBCursorFetch = new JButton(IdeSplMessage.get().getMessage(
			"panelvalue.cursorfetch")); // ��������

	/**
	 * ���캯��
	 */
	public PanelValue() {
		GVSpl.panelValue = this;
		this.setLayout(new BorderLayout());
		setMinimumSize(new Dimension(1, 1));
		valueBar = new PanelValueBar();
		add(valueBar, BorderLayout.NORTH);
		tableValue = new JTableValue(this);
		GM.loadWindowSize(this);
		spValue = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spValue.getViewport().add(tableValue);
		spValue.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3)
					return;
				tableValue.rightClicked(e.getX(), e.getY(), -1, -1, e);
			}
		});
		tableValue.addMWListener(spValue);
		tableValue.getTableHeader().addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3)
					return;
				int col = tableValue.columnAtPoint(e.getPoint());
				tableValue.selectCol(col);
				tableValue.rightClicked(e.getX(), e.getY(),
						tableValue.getSelectedRow(), col, e);
			}
		});
		add(spValue, BorderLayout.CENTER);
		sbValue = new JScrollBar();
		sbValue.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (preventChange)
					return;
				int index = e.getValue();
				if (index > 0) {
					tableValue.resetData(index);
				}
			}
		});
		this.add(sbValue, BorderLayout.EAST);
		tableValue.setValue(null);

		panelDebug.add(jLCellTime, GM.getGBC(0, 0, true, false, 2));
		panelDebug.add(jLInterval, GM.getGBC(0, 1, true));

		panelCursor.add(jLDispRows1, GM.getGBC(0, 0, false, false, 2));
		panelCursor.add(jSDispRows, GM.getGBC(0, 1, false, false, 0));
		panelCursor.add(jLDispRows2, GM.getGBC(0, 2, false, false, 2));
		panelCursor.add(jBCursorFetch, GM.getGBC(0, 3));
		panelCursor.add(new JPanel(), GM.getGBC(0, 4, true));

		JPanel panelAction = new JPanel(new GridBagLayout());
		panelAction.add(panelCursor, GM.getGBC(0, 0, true));
		panelAction.add(panelDebug, GM.getGBC(1, 0, true));

		panelSouth.setMinimumSize(new Dimension(1, 1));

		panelSouth.add(CARD_DEBUG, panelAction);
		JPanel panelEmpty = new JPanel();
		panelEmpty.setMinimumSize(new Dimension(1, 1));
		panelSouth.add(CARD_EMPTY, panelEmpty);
		cl.show(panelSouth, CARD_EMPTY);
		panelSouth.setVisible(false);
		this.add(panelSouth, BorderLayout.SOUTH);
		jBCursorFetch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object value = jSDispRows.getValue();
				if (!(value instanceof Number))
					return;
				int dispRows = ((Number) value).intValue();
				tableValue.cursorFetch(dispRows);
			}
		});
		Dimension dim = new Dimension(100, 25);
		jSDispRows.setPreferredSize(dim);
	}

	public void setCursorValue(boolean isCursor) {
		jLDispRows1.setVisible(isCursor);
		jSDispRows.setVisible(isCursor);
		jLDispRows2.setVisible(isCursor);
		jBCursorFetch.setVisible(isCursor);
		panelCursor.setVisible(isCursor);
	}

	/** ������� */
	private static final String CARD_DEBUG = "CARD_DEBUG";
	/** ����� */
	private static final String CARD_EMPTY = "CARD_EMPTY";
	/**
	 * �����л��Ƿ���ʾǩ���Ŀ�Ƭ����
	 */
	private CardLayout cl = new CardLayout();
	/**
	 * ������塣������ʱ�л�Ϊ�����
	 */
	private JPanel panelSouth = new JPanel(cl);

	private JPanel panelDebug = new JPanel(new GridBagLayout());
	private JPanel panelCursor = new JPanel(new GridBagLayout());

	/**
	 * ��������
	 * 
	 * @param cs
	 *            �������
	 */
	public void setCellSet(PgmCellSet cs) {
		if (cs == null) {
			panelSouth.setVisible(false);
			cl.show(panelSouth, CARD_EMPTY);
		} else {
			cl.show(panelSouth, CARD_DEBUG);
			panelSouth.setVisible(true);
		}
	}

	/**
	 * ���õ���ִ��ʱ�䣬��λ����
	 * 
	 * @param time
	 */
	public void setDebugTime(String cellId, Long time) {
		if (cellId == null || time == null) {
			jLCellTime.setText(null);
		} else {
			jLCellTime.setText(IdeSplMessage.get().getMessage(
					"panelvalue.debugtime", cellId, time));
		}
	}

	public void setInterval(String cellId1, String cellId2, Long interval) {
		if (cellId1 == null || cellId2 == null) {
			jLInterval.setText(null);
		} else {
			jLInterval.setText(IdeSplMessage.get().getMessage(
					"panelvalue.cellinterval", cellId1, cellId2, interval));
		}
	}

	/**
	 * ���ñ༭������
	 * 
	 * @param el
	 */
	public void setEditListener(EditListener el) {
		tableValue.setEditListener(el);
	}
}
