package com.scudata.ide.spl.control;

import java.awt.dnd.DropTarget;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import com.scudata.cellset.datamodel.CellSet;
import com.scudata.ide.spl.GCSpl;
import com.scudata.ide.spl.GVSpl;

/**
 * ����ؼ�
 *
 */
public class EditControl extends SplControl {

	private static final long serialVersionUID = 1L;

	/**
	 * �����Ƿ���Ա༭
	 */
	private boolean editable = true;

	/**
	 * ���캯��
	 *
	 * @param rows int
	 * @param cols int
	 */
	public EditControl(int rows, int cols) {
		super(rows, cols);
	}

	/**
	 * ���ɽǲ����
	 *
	 * @return �ǲ����
	 */
	JPanel createCorner() {
		JPanel panel = new CornerPanel(this, editable);
		CornerListener listener = new CornerListener(this, editable);
		panel.addMouseListener(listener);
		panel.addMouseWheelListener(getMouseWheelListener());
		return panel;
	}

	/**
	 * �������׸����
	 *
	 * @return ���׸����
	 */
	JPanel createColHeaderView() {
		headerPanel = new ColHeaderPanel(this, editable);
		ColHeaderListener listener = new ColHeaderListener(this, editable);
		headerPanel.addMouseListener(listener);
		headerPanel.addMouseMotionListener(listener);
		headerPanel.addKeyListener(listener);
		headerPanel.addMouseWheelListener(getMouseWheelListener());
		return headerPanel;
	}

	/**
	 * �������׸����
	 *
	 * @return ���׸����
	 */
	JPanel createRowHeaderView() {
		JPanel panel = new RowHeaderPanel(this, editable);
		RowHeaderListener listener = new RowHeaderListener(this, editable);
		panel.addMouseListener(listener);
		panel.addMouseMotionListener(listener);
		panel.addKeyListener(listener);
		panel.addMouseWheelListener(getMouseWheelListener());
		return panel;
	}

	protected ContentPanel contentPanel;

	/**
	 * �����������
	 *
	 * @return �������
	 */
	ContentPanel createContentView() {
		contentPanel = newContentPanel(cellSet);
		CellSelectListener listener = new CellSelectListener(this,
				contentPanel, editable);
		contentPanel.addMouseListener(listener);
		contentPanel.addMouseMotionListener(listener);
		contentPanel.addKeyListener(listener);
		// mouseWheelListener.setMouseWheelListeners(getMouseWheelListeners());
		contentPanel.addMouseWheelListener(getMouseWheelListener());
		DropTarget target = new DropTarget(contentPanel, new EditDropListener());
		contentPanel.setDropTarget(target);
		contentPanel.setFocusTraversalKeysEnabled(false);
		return contentPanel;
	}

	private ScaleListener mouseWheelListener = null;

	protected synchronized ScaleListener getMouseWheelListener() {
		if (mouseWheelListener == null) {
			mouseWheelListener = new ScaleListener();
			mouseWheelListener.setMouseWheelListeners(getMouseWheelListeners());
		}
		return mouseWheelListener;
	}

	// private ScaleListener mouseWheelListener = new ScaleListener();

	class ScaleListener implements MouseWheelListener {
		MouseWheelListener[] listeners;

		public ScaleListener() {
		}

		public void setMouseWheelListeners(MouseWheelListener[] listeners) {
			this.listeners = listeners;
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if (!e.isControlDown()) {
				if (listeners != null) {
					for (MouseWheelListener mwl : listeners) {
						mwl.mouseWheelMoved(e);
					}
				}
				return;
			}
			int percent = (int) (scale * 100);
			int wr = e.getWheelRotation();
			int newPercent;
			if (wr < 0) { // �������ϣ��Ŵ�
				newPercent = GCSpl.DEFAULT_SCALES[GCSpl.DEFAULT_SCALES.length - 1];
				for (int i = 0; i < GCSpl.DEFAULT_SCALES.length; i++) {
					if (percent < GCSpl.DEFAULT_SCALES[i] - 7) {
						newPercent = GCSpl.DEFAULT_SCALES[i];
						break;
					}
				}
			} else { // ��С
				newPercent = GCSpl.DEFAULT_SCALES[0];
				for (int i = GCSpl.DEFAULT_SCALES.length - 1; i >= 0; i--) {
					if (percent > GCSpl.DEFAULT_SCALES[i] + 7) {
						newPercent = GCSpl.DEFAULT_SCALES[i];
						break;
					}
				}
			}
			if (newPercent != percent) {
				setScale(new Float(newPercent) / 100f);
				if (GVSpl.splEditor != null)
					GVSpl.splEditor.setDataChanged(true);
			}
			e.consume();
		}
	}

	/**
	 * ����SPL�������
	 * @param cellSet
	 * @return ContentPanel
	 */
	protected ContentPanel newContentPanel(CellSet cellSet) {
		return new ContentPanel(cellSet, 1, cellSet.getRowCount(), 1,
				cellSet.getColCount(), true, true, this);
	}

	/**
	 * �ύ�ı��༭
	 */
	public void acceptText() {
		this.contentView.submitEditor();
	}

}
