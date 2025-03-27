package com.scudata.ide.spl.chart.auto;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.scudata.chart.edit.ElementInfo;
import com.scudata.common.MessageManager;
import com.scudata.ide.spl.dialog.DialogDisplayChart;
import com.scudata.ide.spl.resources.ChartMessage;

/**
 * ͼ�����Ա༭��
 * 
 * Ӧ�ó����������ֱ��ʹ�ý������ͼʱ ʹ�øô�����ʱ�༭ͼ������
 * 
 * @author Joancy
 *
 */
public class DialogGraphEdit extends JDialog {
	private static final long serialVersionUID = 1L;

	private PanelParams propPanel;
	private ElementInfo elmInfo;
	JButton btExpandAll = new JButton();
	JButton btCollapseAll = new JButton();

	MessageManager mm = ChartMessage.get();
	private DialogDisplayChart ddc;

	/**
	 * ����ͼ�����Ա༭��
	 * 
	 * @param ddc
	 *            ��ʾͼ�θ�����
	 */
	public DialogGraphEdit(DialogDisplayChart ddc) {
		super(ddc);
		this.ddc = ddc;

		this.setModal(true);
		this.setSize(300, ddc.getHeight());
		this.setResizable(true);
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());

		propPanel = new PanelParams(this) {
			public void refresh() {
				refreshDDC();
			}
		};
		pane.add(propPanel, BorderLayout.CENTER);
		btExpandAll.setText(mm.getMessage("button.expandAll"));
		btExpandAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				propPanel.expandAll();
			}
		});

		btCollapseAll.setText(mm.getMessage("button.collapseAll"));
		btCollapseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				propPanel.collapseAll();
			}
		});
		JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmp.add(btExpandAll);
		tmp.add(btCollapseAll);
		pane.add(tmp, BorderLayout.SOUTH);

		elmInfo = new ElementInfo();
		elmInfo.setProperties(ddc.getGraphName(), ddc.getProperties());
		dispDetail();

		String label = mm.getMessage("label.propedit", elmInfo.getTitle());
		this.setTitle(label);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeDialog();
			}
		});
		setLocation(ddc.getX() + ddc.getWidth(), ddc.getY());
	}

	/**
	 * ���ݵ�ǰ��������ˢ�¸����ڵ���ʾͼ��
	 */
	public void refreshDDC() {
		HashMap<String, Object> p = elmInfo.getProperties(propPanel.infoList);
		ddc.setProperties(p);
	}

	/**
	 * ��ʾ��������ϸ��Ϣ
	 */
	public void dispDetail() {
		propPanel.setElementInfo(elmInfo);
	}

	private void closeDialog() {
		dispose();
	}

}
