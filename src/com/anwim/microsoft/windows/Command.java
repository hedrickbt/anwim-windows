package com.anwim.microsoft.windows;


public class Command {
	public static final String ERROR = "Error:";
	public static final String STD_OUT = "StdOut:";

	
	protected RunCommand command;

	public Command(RunCommand command) {
		this.command = command;
	}
	
	public RunCommand getCommand() {
		return command;
	}

}
