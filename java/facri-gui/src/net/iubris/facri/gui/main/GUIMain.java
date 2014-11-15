package net.iubris.facri.gui.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.iubris.facri.gui.FacriGUIFrame;
import net.iubris.facri.gui._di.guice.module.FacriGUIModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GUIMain {

	public static void main(String[] args) {
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("swing.boldMetal", Boolean.FALSE);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Injector injector = Guice.createInjector( new FacriGUIModule() );
		final FacriGUIFrame guiFrame = injector.getInstance(FacriGUIFrame.class);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				guiFrame.setVisible(true);
			}
		});
	}
}
