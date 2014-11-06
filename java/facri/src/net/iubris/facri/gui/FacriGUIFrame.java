package net.iubris.facri.gui;

import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import net.iubris.ishtaran.gui.GUIFrame;

public class FacriGUIFrame extends GUIFrame {

	public FacriGUIFrame(Set<JButton> buttons, JTextArea jTextArea, JLabel jLabelRelative,
			JProgressBar jProgressBarRelative, JLabel jLabelGlobal, JProgressBar jProgressBarGlobal) {
		super(buttons, jTextArea, jLabelRelative, jProgressBarRelative, jLabelGlobal, jProgressBarGlobal);
	}

	private static final long serialVersionUID = 4642194410605183875L;

}
