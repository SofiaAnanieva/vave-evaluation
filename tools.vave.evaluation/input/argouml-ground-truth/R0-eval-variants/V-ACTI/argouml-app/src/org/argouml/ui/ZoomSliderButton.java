package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.PopupButton;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;


public class ZoomSliderButton extends PopupButton {
	private static final String RESOURCE_NAME = "Zoom Reset";
	private static final Font LABEL_FONT = new Font("Dialog",Font.PLAIN,10);
	public static final int MINIMUM_ZOOM = 25;
	public static final int MAXIMUM_ZOOM = 300;
	private static final int SLIDER_HEIGHT = 250;
	private JSlider slider = null;
	private JTextField currentValue = null;
	private boolean popupButtonIsActive = true;
	private boolean popupMenuIsShowing = false;
	private boolean mouseIsOverPopupButton = false;
	public ZoomSliderButton() {
		super();
		setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (e.getModifiers() == 0) {
					showPopup();
				}
			}
		});
		Icon icon = ResourceLoaderWrapper.lookupIcon(RESOURCE_NAME);
		MyMouseListener myMouseListener = new MyMouseListener();
		addMouseMotionListener(myMouseListener);
		addMouseListener(myMouseListener);
		setIcon(icon);
		setToolTipText(Translator.localize("button.zoom"));
	}
	private void createPopupComponent() {
		slider = new JSlider(JSlider.VERTICAL,MINIMUM_ZOOM,MAXIMUM_ZOOM,MINIMUM_ZOOM);
		slider.setInverted(true);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		int sliderBaseWidth = slider.getPreferredSize().width;
		slider.setPaintLabels(true);
		for (Enumeration components = slider.getLabelTable().elements();components.hasMoreElements();) {
			((Component) components.nextElement()).setFont(LABEL_FONT);
		}
		slider.setToolTipText(Translator.localize("button.zoom.slider-tooltip"));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				handleSliderValueChange();
			}
		});
		int labelWidth = slider.getFontMetrics(LABEL_FONT).stringWidth(String.valueOf(MAXIMUM_ZOOM)) + 6;
		slider.setPreferredSize(new Dimension(sliderBaseWidth + labelWidth,SLIDER_HEIGHT));
		currentValue = new JTextField(5);
		currentValue.setHorizontalAlignment(JLabel.CENTER);
		currentValue.setFont(LABEL_FONT);
		currentValue.setToolTipText(Translator.localize("button.zoom.current-zoom-magnification"));
		updateCurrentValueLabel();
		currentValue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleTextEntry();
			}
		});
		currentValue.addFocusListener(new FocusAdapter() {
			@Override public void focusLost(FocusEvent e) {
				handleTextEntry();
			}
		});
		JPanel currentValuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		currentValuePanel.add(currentValue);
		JPanel zoomPanel = new JPanel(new BorderLayout(0,0));
		zoomPanel.add(slider,BorderLayout.CENTER);
		zoomPanel.add(currentValuePanel,BorderLayout.NORTH);
		setPopupComponent(zoomPanel);
	}
	@Override protected void showPopup() {
		if (slider == null) {
			createPopupComponent();
		}
		Editor ed = Globals.curEditor();
		if (ed != null) {
			slider.setValue((int) (ed.getScale() * 100.0));
		}
		if (popupButtonIsActive) {
			super.showPopup();
			JPopupMenu pm = (JPopupMenu) this.getPopupComponent().getParent();
			PopupMenuListener pml = new MyPopupMenuListener();
			pm.addPopupMenuListener(pml);
			popupMenuIsShowing = true;
		}
		slider.requestFocus();
	}
	private void handleSliderValueChange() {
		updateCurrentValueLabel();
		double zoomPercentage = slider.getValue() / 100.0;
		Editor ed = Globals.curEditor();
		if (ed == null||zoomPercentage <= 0.0) {
			return;
		}
		if (zoomPercentage != ed.getScale()) {
			ed.setScale(zoomPercentage);
			ed.damageAll();
		}
	}
	private void handleTextEntry() {
		String value = currentValue.getText();
		if (value.endsWith("%")) {
			value = value.substring(0,value.length() - 1);
		}
		try {
			int newZoom = Integer.parseInt(value);
			if (newZoom < MINIMUM_ZOOM||newZoom > MAXIMUM_ZOOM) {
				throw new NumberFormatException();
			}
			slider.setValue(newZoom);
		}catch (NumberFormatException ex) {
			updateCurrentValueLabel();
		}
	}
	private void updateCurrentValueLabel() {
		currentValue.setText(String.valueOf(slider.getValue()) + '%');
	}
	private class MyPopupMenuListener extends AbstractAction implements PopupMenuListener {
	public void actionPerformed(ActionEvent e) {
	}
	public void popupMenuCanceled(PopupMenuEvent e) {
		if (mouseIsOverPopupButton) {
			popupButtonIsActive = false;
		}else {
			popupButtonIsActive = true;
		}
		popupMenuIsShowing = false;
	}
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	}
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}
}
	private class MyMouseListener extends MouseInputAdapter {
	@Override public void mouseEntered(MouseEvent me) {
		mouseIsOverPopupButton = true;
	}
	@Override public void mouseExited(MouseEvent me) {
		mouseIsOverPopupButton = false;
		if (!popupButtonIsActive&&!popupMenuIsShowing) {
			popupButtonIsActive = true;
		}
	}
	@Override public void mousePressed(MouseEvent me) {
		if (popupButtonIsActive) {
			showPopup();
		}else if (!popupMenuIsShowing) {
			popupButtonIsActive = true;
		}
	}
}
}



