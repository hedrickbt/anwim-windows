package com.anwim.microsoft.windows.processes;

import java.util.HashMap;
import java.util.Map;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.Command;

public class ListProcessesCommand extends Command {

	public ListProcessesCommand(RunCommand command) {
		super(command);
	}

	public Map<String, String> execute(String serverName) {
		Map<String, String> result = new HashMap<String, String>();
		CommandResponse response = new CommandResponse();
		String[] args = { "cmd.exe", "/c", "tasklist /V /S " + serverName };
		try {
			command.execute(args, response);
			if (response.getStdErr().size() > 0) {
				result.put(Command.ERROR, response.getStdErr().toString());
			} else {
				// if (response.stdOutContains(" FAILED ")) {
				// throw new Exception("Call for SC Failed:" +
				// response.getStdOut());
				// } else {
				long readLines = 0;
				String[] columns = null;
				for (String outputLine : response.getStdOut()) {
					readLines++;
					if (readLines == 3) { // get the column sizes
						columns = outputLine.split(" ");
						//System.out.println(columns);
					} else {
						if (readLines > 3) {
							// Now that I know the format
							String processName = outputLine.substring(0,(columns[0].length()-1)).trim();
							//System.out.println("substr begin:" + (columns[0].length()+1));
							//System.out.println("substr end:" + (columns[0].length()+1+columns[1].length()));
							String processId = outputLine.substring(columns[0].length()+1,(columns[0].length()+1+columns[1].length())).trim();
							//System.out.println("processName:" + processName +":");
							//System.out.println("processId:" + processId +":");
							result.put(processId,processName);
						}
					}
					
				}
				// }
			}
		} catch (Exception e) {
			result.put(Command.ERROR, e.getMessage());
		}

		return result;
	}

}
