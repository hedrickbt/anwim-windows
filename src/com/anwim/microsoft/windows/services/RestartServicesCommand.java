package com.anwim.microsoft.windows.services;

import java.util.HashMap;
import java.util.Map;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;

public class RestartServicesCommand extends ServiceCommand {


	public RestartServicesCommand(RunCommand command) {
		super(command);
	}

	public Map<String, String> execute(String serverName, String serviceName) {
		Map<String, String> result = new HashMap<String, String>();
		CommandResponse response = new CommandResponse();
		String[] args = { "cmd.exe", "/c",
				"restart-service.bat " + serverName + " " + serviceName };
		try {
			command.execute(args, response);
			if (response.getStdErr().size() > 0) {
				result.put(ServiceCommand.ERROR, response.getStdErr().toString());
			} else {
				if (response.stdOutContains(" FAILED ")) {
					throw new Exception("Call for SC Failed:" + response.getStdOut());
				} else {
					boolean success = false;
					for (String outputLine : response.getStdOut()) {
						if (outputLine.contains("Running")) {
							success = true;
							break;
						}
					}

					result.put(ServiceCommand.STD_OUT,response.getStdOut().toString());
					if (success) {
					} else {
						result.put(ServiceCommand.ERROR, "Output does not indicate the service was restarted.  The word Running was not found.");
					}
				}
			}
		} catch (Exception e) {
			result.put(ServiceCommand.ERROR, e.getMessage());
		}

		return result;
	}

}
