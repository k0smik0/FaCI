package net.iubris.facri.parsers;

import java.io.File;

public interface Parser {

	void parse(File... userDirs) throws Exception;
}
