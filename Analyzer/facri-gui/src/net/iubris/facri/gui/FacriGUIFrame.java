package net.iubris.facri.gui;

import java.util.Set;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import net.iubris.ishtaran.gui._di.annotations.GUIButtons;
import net.iubris.ishtaran.gui._di.annotations.JLabelGlobal;
import net.iubris.ishtaran.gui._di.annotations.JLabelRelative;
import net.iubris.ishtaran.gui._di.annotations.JTextAreaGlobal;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobal;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarRelative;
import net.iubris.ishtaran.gui.frame.GUIFrame;

public class FacriGUIFrame extends GUIFrame {

	@Inject
	public FacriGUIFrame(@GUIButtons Set<JButton> buttons, 
			@JTextAreaGlobal JTextArea jTextArea, 
			@JLabelRelative JLabel jLabelRelative,
			@ProgressBarRelative JProgressBar jProgressBarRelative, 
			@JLabelGlobal JLabel jLabelGlobal, 
			@ProgressBarGlobal JProgressBar jProgressBarGlobal) {
		super(buttons, jTextArea, jLabelRelative, jProgressBarRelative, jLabelGlobal, jProgressBarGlobal, "FaCRI");
	}

	private static final long serialVersionUID = 4642194410605183875L;
}
