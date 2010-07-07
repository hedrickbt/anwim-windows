package com.anwim.microsoft.windows.services;

import java.util.HashMap;
import java.util.Map;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.Command;

public class StopServicesCommand extends Command {


	public StopServicesCommand(RunCommand command) {
		super(command);
	}

	public Map<String, String> execute(String serverName, String serviceName) {
		Map<String, String> result = new HashMap<String, String>();
		CommandResponse response = new CommandResponse();
		String[] args = { "cmd.exe", "/c",
				"stop-service.bat " + serverName + " " + serviceName };
		try {
			command.execute(args, response);
			if (response.getStdErr().size() > 0) {
				result.put(Command.ERROR, response.getStdErr().toString());
			} else {
				if (response.stdOutContains(" FAILED ")) {
					throw new Exception("Call for SC Failed:" + response.getStdOut());
				} else {
					boolean success = false;
					for (String outputLine : response.getStdOut()) {
						if (outputLine.contains("Stopped")) {
							success = true;
							break;
						}
					}

					result.put(Command.STD_OUT,response.getStdOut().toString());
					if (success) {
					} else {
						result.put(Command.ERROR, "Output does not indicate the service was stopped.  The word Stopped was not found.");
					}
				}
			}
		} catch (Exception e) {
			result.put(Command.ERROR, e.getMessage());
		}

		return result;
	}

}
