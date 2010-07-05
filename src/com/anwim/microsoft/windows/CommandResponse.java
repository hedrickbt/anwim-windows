package com.anwim.microsoft.windows;

import java.util.ArrayList;

public class CommandResponse {
	int exitValue = 0;
	ArrayList<String> standardOutput = new ArrayList<String>();
	ArrayList<String> standardError = new ArrayList<String>();
	
	public boolean stdOutContains(String substring) {
		for (String item : standardOutput) {
			if (item.contains(substring)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean stdOutContainsIgnoreCase(String substring) {
		for (String item : standardOutput) {
			if (item.toUpperCase().contains(substring.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public boolean stdErrContains(String substring) {
		for (String item : standardError) {
			if (item.contains(substring)) {
				return true;
			}
		}
		return false;
	}

	public boolean stdErrContainsIgnoreCase(String substring) {
		for (String item : standardError) {
			if (item.toUpperCase().contains(substring.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(String substring) {
		return (stdOutContains(substring) || stdErrContains(substring));
	}

	public boolean containsIgnoreCase(String substring) {
		return (stdOutContainsIgnoreCase(substring) || stdErrContainsIgnoreCase(substring));
	}

	public void addStdOut(String newData) {
		standardOutput.add(newData);
	}
	
	public void addStdErr(String newData) {
		standardError.add(newData);
	}
	
	public ArrayList<String> getStdOut() {
		return standardOutput;
	}

	public ArrayList<String> getStdErr() {
		return standardError;
	}
	
	public int getExitValue() {
		return exitValue;
	}
	
	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

}
