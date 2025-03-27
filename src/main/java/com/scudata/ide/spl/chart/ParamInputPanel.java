package com.scudata.ide.spl.chart;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.scudata.chart.edit.*;
import com.scudata.ide.common.GM;
import com.scudata.ide.spl.resources.*;

/**
 * �����������
 * 
 * @author Joancy
 *
 */
public class ParamInputPanel extends JSplitPane implements ActionListener {
	private static final long serialVersionUID = 1L;
	private TableParamEdit table;
	private TableInputSeries seriesTable;
	private JButton addRowBtn, delRowBtn, insertBtn;
	ParamInfoList infoList;
	private Dialog owner;

	/**
	 * ����ͼԪ��Ϣ
	 * @param info ͼԪ��Ϣ����
	 */
	public void setElementInfo( ElementInfo info ) {
		String label = ChartMessage.get().getMessage( "label.propedit", info.getTitle() );  //"���Ա༭";
		init( owner, label, info.getParamInfoList() );
		boolean hasLegendCol = info.getName().equals("Dot") ||
				info.getName().equals("Line") ||
				info.getName().equals("Column") ||
				info.getName().equals("Sector");
		table.setColumnVisible(table.AXISCOL, hasLegendCol );
	}

	/**
	 * ���ò�����Ϣ�б�
	 * @param owner ������
	 * @param list ������Ϣ�б�
	 */
	public void setParamInfoList( Dialog owner, ParamInfoList list ) {
		String label = ChartMessage.get().getMessage( "label.paramedit" );  //"��ͼ�����༭";
		init( owner, label, list );
	}

	/**
	 * ʹ�ø����ڶ���Ĺ��캯��
	 * @param owner ������
	 */
	public ParamInputPanel( Dialog owner ) {
		this.owner = owner;
	}
	/**
	 * չ��ȫ������
	 */
	public void expandAll(){
		table.expandAll();
	}
	
	/**
	 * ����ȫ������
	 */
	public void collapseAll(){
		table.collapseAll();
	}
	private void init( Dialog owner, String label, ParamInfoList list ) {
		this.infoList = list;
		JPanel paramPanel = new JPanel();
		paramPanel.setLayout( new BorderLayout() );
		paramPanel.add( new JLabel( "  "+label ), BorderLayout.NORTH );
		table = new TableParamEdit( owner, list );
		paramPanel.add( new JScrollPane( table ) );
		this.setLeftComponent( paramPanel );

		JPanel seriesPanel = new JPanel(new BorderLayout());
		JPanel top = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = GM.getGBC(1, 1,true);
		gbc.gridwidth = 4;
		top.add( new JLabel( ChartMessage.get().getMessage( "label.seriesinput" ) ), gbc );  //"��������ֵ¼��"
		
		seriesTable = new TableInputSeries( table );
		seriesTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		insertBtn = new JButton( ChartMessage.get().getMessage( "button.insertrow" ) );  //"������(I)" );
		insertBtn.addActionListener( this );
		insertBtn.setMnemonic( 'I' );
		addRowBtn = new JButton( ChartMessage.get().getMessage( "button.addrow" ) );  //"�����(A)" );
		addRowBtn.addActionListener( this );
		addRowBtn.setMnemonic( 'A' );
		delRowBtn = new JButton( ChartMessage.get().getMessage( "button.delrow" ) );  //"ɾ����(D)" );
		delRowBtn.addActionListener( this );
		delRowBtn.setMnemonic( 'D' );
		
		top.add( new JLabel(" "),GM.getGBC(2, 1, true) );
		top.add( insertBtn,GM.getGBC(2, 2) );
		top.add( addRowBtn,GM.getGBC(2, 3) );
		top.add( delRowBtn,GM.getGBC(2, 4) );
		seriesPanel.add( top,BorderLayout.NORTH );
		JScrollPane jsp =new JScrollPane( seriesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); 
		seriesPanel.add( jsp, BorderLayout.CENTER );

		this.setRightComponent( seriesPanel );
		this.setDividerLocation( 480 );
		this.setDividerSize( 8 );
		this.setOneTouchExpandable( true );
		table.seriesTable = seriesTable;
	}

	/**
	 * ��ȡ������༭��
	 * @return
	 */
	public TableParamEdit getParamTable() {
		return table;
	}

	/**
	 * ��ȡ���б༭��
	 * @return
	 */
	public TableInputSeries getSeriesTable() {
		return seriesTable;
	}

	/**
	 * �¼�������
	 * 
	 * @param e ActionEvent �¼�����
	 */
	public void actionPerformed( ActionEvent e ) {
		Object o = e.getSource();
		if( o.equals( addRowBtn ) ) {
			seriesTable.myAddRow();
		}
		else if( o.equals( insertBtn ) ) {
			seriesTable.myInsertRow();
		}
		else if( o.equals( delRowBtn ) ) {
			seriesTable.myDelRow();
		}
	}

	/**
	 * ��ȡ������Ϣ�б�
	 * @return ������Ϣ�б�
	 */
	public ParamInfoList getParamInfoList() {
		table.acceptText();
		return infoList;
	}

}
