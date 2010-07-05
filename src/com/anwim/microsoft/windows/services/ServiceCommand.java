package com.anwim.microsoft.windows.services;

import com.anwim.microsoft.windows.RunCommand;

public class ServiceCommand {
	public static final String ERROR = "Error:";
	public static final String STD_OUT = "StdOut:";

	
	RunCommand command;

	public ServiceCommand(RunCommand command) {
		this.command = command;
	}
	
	public RunCommand getCommand() {
		return command;
	}

}
