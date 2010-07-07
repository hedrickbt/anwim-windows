package com.anwim.microsoft.windows.processes;

import java.util.HashMap;
import java.util.Map;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.Command;

public class KillProcessesCommand extends Command {

	public KillProcessesCommand(RunCommand command) {
		super(command);
	}

	public Map<String, String> execute(String serverName, String processId) {
		Map<String, String> result = new HashMap<String, String>();
		CommandResponse response = new CommandResponse();
		String[] args = { "cmd.exe", "/c", "taskkill /F /PID " + processId + " /S " + serverName };
		try {
			command.execute(args, response);
			if (response.getStdErr().size() > 0) {
				result.put(Command.ERROR, response.getStdErr().toString());
			} else {
				boolean success = false;
				for (String outputLine : response.getStdOut()) {
					if (outputLine.contains("SUCCESS")) {
						success = true;
						break;
					}
				}

				result.put(Command.STD_OUT,response.getStdOut().toString());
				if (success) {
				} else {
					result.put(Command.ERROR, "Output does not indicate the process was killed.  The word SUCCESS was not found.");
				}
			}
		} catch (Exception e) {
			result.put(Command.ERROR, e.getMessage());
		}

		return result;
	}

}
