package net.iubris.facri.parsers;

import java.io.File;

public interface Parser {

	void parse(File... userDir/*, Map<String,User> useridToUserMap*/) throws Exception;
}
