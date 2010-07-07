package com.anwim.microsoft.windows;

import java.util.Map;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.processes.ListProcessesCommand;

public class ListProcessesCommandTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExecuteForKnownGoodProcessExplorer() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		ListProcessesCommand lsc = new ListProcessesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("osprey");

			boolean found = findMapValue(result, "explorer.exe");
			Assert.assertTrue("Expecting to find explorer.exe process.", found);

		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	private boolean findMapValue(Map<String, String> result, String searchValue) {
		boolean found = false;
		Iterator<Map.Entry<String,String>> iter = result.entrySet().iterator();
		while ((iter.hasNext()) && (!found)) {
		    Map.Entry<String,String> entry = iter.next();
		    if (entry.getValue().equals(searchValue)) {
		        //String key_you_look_for = entry.getKey();
		    	found = true;
		    }
		}
		return found;
	}

	public void testExecuteForMissingProcessZZTop() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		ListProcessesCommand lsc = new ListProcessesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("osprey");
			boolean found = findMapValue(result, "zztop.exe");
			Assert.assertFalse("Expecting not to find zztop.exe process.", found);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testExecuteForInvalidServerName() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		ListProcessesCommand lsc = new ListProcessesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("ZZTop");
			boolean found = findMapValue(result, "explorer.exe");
			Assert.assertFalse(
					"Expecting not to find explorer.exe process on ZZTop.", found);
			Assert.assertTrue("Expecting to find an error", result
					.containsKey(Command.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
}
