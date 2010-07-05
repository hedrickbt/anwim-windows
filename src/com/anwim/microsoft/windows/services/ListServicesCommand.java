package com.anwim.microsoft.windows.services;

import java.util.HashMap;
import java.util.Map;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;

public class ListServicesCommand extends ServiceCommand {

	public ListServicesCommand(RunCommand command) {
		super(command);
	}

	public Map<String, String> execute(String serverName) {
		Map<String, String> result = new HashMap<String, String>();
		CommandResponse response = new CommandResponse();
		String[] args = { "cmd.exe", "/c",
				"sc \\\\" + serverName + " query state= all" };
		try {
			command.execute(args, response);
			if (response.getStdErr().size() > 0) {
				result.put(ServiceCommand.ERROR, response.getStdErr().toString());
			} else {
				if (response.stdOutContains(" FAILED ")) {
					throw new Exception("Call for SC Failed:" + response.getStdOut());
				} else {
					String serviceName = "";
					String status = "";
					for (String outputLine : response.getStdOut()) {
						if (outputLine.contains("SERVICE_NAME:")) { // ex
																	// line...SERVICE_NAME:
																	// Apache2.2
							serviceName = outputLine.split(":")[1].trim();
						} else {
							if (serviceName.length() > 0) {
								if (outputLine.contains("STATE")) { // ex line...
																	// STATE : 2
																	// START_PENDING
									status = outputLine.split(":")[1].trim().split(
											" ")[0];
									result.put(serviceName, status);
									serverName = "";
									status = "";
								}
							}
	
						}
					}
				}
			}
		} catch (Exception e) {
			result.put(ServiceCommand.ERROR, e.getMessage());
		}

		return result;
	}

}
