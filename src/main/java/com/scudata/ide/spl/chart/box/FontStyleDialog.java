package com.scudata.ide.spl.chart.box;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.scudata.chart.*;
import com.scudata.ide.common.*;
import com.scudata.ide.spl.resources.*;

/**
 * ������ʽ�Ի���
 * 
 * @author Joancy
 *
 */
public class FontStyleDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3611045277241487646L;
	private int m_option = JOptionPane.CLOSED_OPTION;
	private int style;
	JCheckBox bold = new JCheckBox();
	JCheckBox italic = new JCheckBox();
	JCheckBox underLine = new JCheckBox();
	JCheckBox vertical = new JCheckBox();
	JButton okbtn = new JButton();
	JButton cancelbtn = new JButton();
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	/**
	 * ���캯��
	 * @param owner ������
	 */
	public FontStyleDialog( Dialog owner ) {
		super( owner );
		this.setTitle( ChartMessage.get().getMessage( "fontdialog.title" ) );
		this.setModal( true );
		this.setSize( 245, 150 );
		GM.setDialogDefaultButton( this, okbtn, cancelbtn );
		try {
			jbInit();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout( gridBagLayout1 );
		bold.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		bold.setText( ChartMessage.get().getMessage( "fontdialog.bold" ) );  //"����" );
		italic.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		italic.setText( ChartMessage.get().getMessage( "fontdialog.italic" ) );  //"б��" );
		underLine.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		underLine.setActionCommand( "" );
		underLine.setText( ChartMessage.get().getMessage( "fontdialog.ul" ) );  //"�»���" );
		vertical.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		vertical.setActionCommand( ChartMessage.get().getMessage( "fontdialog.vert" ) );  //"��������" );
		vertical.setText( ChartMessage.get().getMessage( "fontdialog.vert" ) );  //"��������" );
		okbtn.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		okbtn.setMargin( new Insets( 2, 10, 2, 10 ) );
		okbtn.setText( ChartMessage.get().getMessage( "button.ok" ) );  //"ȷ��(O)" );
		okbtn.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				okbtn_actionPerformed( e );
			}
		} );
		cancelbtn.setText( ChartMessage.get().getMessage( "button.cancel" ) );  //"ȡ��(C)" );
		cancelbtn.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				cancelbtn_actionPerformed( e );
			}
		} );
		cancelbtn.setFont( new java.awt.Font( "Dialog", 0, 12 ) );
		cancelbtn.setMargin( new Insets( 2, 10, 2, 10 ) );
		this.getContentPane().add( underLine, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0
			, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 11, 40, 0, 0 ), 0, 0 ) );
		this.getContentPane().add( bold, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0
			, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 20, 40, 0, 12 ), 0, 0 ) );
		this.getContentPane().add( vertical, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0
			, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 11, 49, 0, 23 ), 0, 0 ) );
		this.getContentPane().add( italic, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0
			, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 20, 49, 0, 47 ), 0, 0 ) );
		this.getContentPane().add( cancelbtn, new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0
			, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 23, 52, 20, 43 ), 0, -4 ) );
		this.getContentPane().add( okbtn, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0
			, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 23, 47, 20, 0 ), 0, -4 ) );
		okbtn.setMnemonic( 'o' );
		cancelbtn.setMnemonic( 'c' );
	}

	/**
	 * ����������ʽ
	 * @param style ��ʽ
	 */
	public void setFontStyle( int style ) {
		this.style = style;
		if ( ( style & Consts.FONT_BOLD ) != 0 ) {
			bold.setSelected( true );
		}
		else {
			bold.setSelected( false );
		}
		if ( ( style & Consts.FONT_ITALIC ) != 0 ) {
			italic.setSelected( true );
		}
		else {
			italic.setSelected( false );
		}
		if ( ( style & Consts.FONT_UNDERLINE ) != 0 ) {
			underLine.setSelected( true );
		}
		else {
			underLine.setSelected( false );
		}
		if ( ( style & Consts.FONT_VERTICAL ) != 0 ) {
			vertical.setSelected( true );
		}
		else {
			vertical.setSelected( false );
		}
	}

	/**
	 * ��ȡ���ڶ���ѡ��
	 * @return ѡ��
	 */
	public int getOption() {
		return m_option;
	}

	/**
	 * ��ȡ������ʽ
	 * @return ������ʽ
	 */
	public int getFontStyle() {
		return style;
	}

	void okbtn_actionPerformed( ActionEvent e ) {
		style = 0;
		if ( bold.isSelected() ) {
			style += Consts.FONT_BOLD;
		}
		if ( italic.isSelected() ) {
			style += Consts.FONT_ITALIC;
		}
		if ( underLine.isSelected() ) {
			style += Consts.FONT_UNDERLINE;
		}
		if ( vertical.isSelected() ) {
			style += Consts.FONT_VERTICAL;
		}
		m_option = JOptionPane.OK_OPTION;
		dispose();
	}

	void cancelbtn_actionPerformed( ActionEvent e ) {
		m_option = JOptionPane.CANCEL_OPTION;
		dispose();
	}

}
