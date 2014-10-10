package net.iubris.facri.main;


public class Main {

	public static void main(String[] args) {
		String dataRootDir;
		if (!args[1].isEmpty())
			dataRootDir = args[1];
		else
			dataRootDir = "";//+Config.DATA_ROOT_DIR;
	}
}
