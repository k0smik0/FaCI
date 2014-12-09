package net.iubris.facri.console.actions;

import java.io.Console;
import java.util.List;

import net.iubris.facri.console.actions.QuitAction.QuitCommand;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class HelpAction implements CommandAction {
	
	private final String helpString;
	
	public HelpAction() {
		String newLine = "\n";
		helpString = new StringBuilder()
		.append("'Facri' available commands:").append(newLine)
//		.append(tab(1)).append("'" + quitCommandChar + "': exit").append(newLine)
//		.append(tab(1)).append("'" + helpCommandChar + "': display this help").append(newLine)
//		.append(tab(1)).append("'" + analyzeCommandChar + "': analyze [world] [analysis type]").append(newLine)
//		.append(tab(1)).append("'"+ Command.q +"': exit").append(newLine)
		.append( QuitCommand.q.getHelpMessage() )
//		.append(tab(1)).append("'"+ Command.h +"': display this help").append(newLine)
		.append( HelpCommand.h.getHelpMessage() )
		.append(tab(1)).append("'"+ "a" +"': analyze [world] [analysis type]").append(newLine)
		.append(tab(2)).append("analyze command needs two arguments:").append(newLine)
		.append(tab(2)).append("first argument select 'world' to analyze:").append(newLine)
//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.f +"': analyze friendships 'world'").append(newLine)
//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.i+"': analyze interactions 'world'").append(newLine)
		.append(tab(3)).append(AnalyzerLocator.WorldTargetChar.f.getHelpMessage()).append(newLine)
		.append(tab(3)).append(AnalyzerLocator.WorldTargetChar.i.getHelpMessage()).append(newLine)
		.append(tab(2)).append("second argument select analysis type:").append(newLine)
		.append(tab(3)).append("'"+ AnalyzerLocator.AnalysisTypeChar.mf +"': me and my friends").append(newLine)
		.append(tab(3)).append("'"+ AnalyzerLocator.AnalysisTypeChar.ft +"': my friends and their friends (friends of friends)").append(newLine)
		.append(tab(3)).append("'"+ AnalyzerLocator.AnalysisTypeChar.t +"': friends of my friends").append(newLine)
		.append(tab(3)).append("'"+ AnalyzerLocator.AnalysisTypeChar.mft +"': me, my friends, their friends").append(newLine)
		.append(tab(1)).append("example: 'a i mf' -> analyze interactions between me and my friends").append(newLine)
		.append(newLine)
		.toString();
		
//		Map<Enum<? extends ConsoleCommand> ,CommandAction> actionsMap = new HashMap<>();
//		actionsMap.put(WorldTargetChar.f, null	);
//		
//		ConsoleCommand consoleCommand;
//		for(Enum<? extends ConsoleCommand> consolecommandEnum: actionsMap.keySet()) {
//			try {
//				consoleCommand = Enum.valueOf(consolecommandEnum.getDeclaringClass(), "asd");
//			break;
//			} catch(IllegalArgumentException e) {
//				
//			}
//		}
		
		
	}

	@Override
	public void exec(Console console, List<String> params) {
		console.printf(helpString);
	}

	public static String tab(int many) {
		String tab = "";
		for (int i = 1; i <= many; i++) {
			tab += "\t";
		}
		
		return tab;
	}
	
//	private String firstLetter(Command command) {
//		return command.name().substring(0, 1).toLowerCase();
//	}
	
//	@Override
//	public Command getCommand() {
//		return HelpCommand.h;
//	}
	
	public enum HelpCommand implements ConsoleCommand {
		h;
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private String helpMessage = HelpAction.tab(1)+"'"+name()+"': display this help\n";
	}


}
