package net.iubris.facri.persisters.base;

import net.iubris.berkeley_persister.core.Persister;
import net.iubris.berkeley_persister.core.helper.base.BerkeleyDBHelper;
import net.iubris.facri.main.ConsoleMain;
import net.iubris.facri.model.users.User;
import net.iubris.facri.persisters.helpers.base.FacriBerkeleyDBHelper;

public class FacriPersister<U extends User> extends Persister<String, U> {
	
//	@Inject
//	private final HelpersHolder helpersHolder;

	public FacriPersister(FacriBerkeleyDBHelper<U> berkeleyDBHelper, Class<U> valueClass/*, HelpersHolder helpersHolder*/) {
		super(berkeleyDBHelper, String.class, valueClass);
//		System.out.println("Persister!");
//		this.helpersHolder = helpersHolder;
//		this.helpersHolder.addHelper(berkeleyDBHelper);
		
//		helpersHolder.getHelpers().stream().forEach(h-> {
//			preExits.add( h::closeStorage);
			
		ConsoleMain.interactiveConsole.addPreExit( berkeleyDBHelper::closeStorage );
	}
	
	public BerkeleyDBHelper<String, U> getHelper() {
		return berkeleyDBHelper;
	}
	
	/*@Singleton
	@SuppressWarnings("rawtypes")
	public static class HelpersHolder {
		private Set<FacriBerkeleyDBHelper> helpers = new HashSet<>();
		
		public Set<FacriBerkeleyDBHelper> getHelpers() {
			return helpers;
		}
		public void addHelper(FacriBerkeleyDBHelper facriBerkeleyDBHelper) {
			System.out.println("adding ");
			this.helpers.add(facriBerkeleyDBHelper);
		}
	}*/
}
