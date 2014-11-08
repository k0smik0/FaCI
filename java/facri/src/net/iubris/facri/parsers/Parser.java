package net.iubris.facri.parsers;

import java.io.File;
import java.util.Map;

import net.iubris.facri.model.User;

public interface Parser {

	void parse(File userDir, String userId, Map<String,User> useridToUserMap) throws Exception;
}
