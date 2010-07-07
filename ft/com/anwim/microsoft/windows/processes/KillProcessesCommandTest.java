package com.anwim.microsoft.windows.processes;

import java.util.Map;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.anwim.microsoft.windows.Command;
import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.processes.ListProcessesCommand;
import com.anwim.microsoft.windows.processes.KillProcessesCommand;

public class KillProcessesCommandTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExecuteForKnownGoodProcessHelp() {
		try {
			// find the pid for help (hh.exe)
			RunCommand cmd = new RunCommand();
			CommandResponse response = new CommandResponse();
			ListProcessesCommand lsc = new ListProcessesCommand(cmd);
			Map<String, String> result = lsc.execute("osprey");
			String processId = findMapKeyByValue(result, "hh.exe");
			Assert
					.assertNotNull("Expecting to find hh.exe process.",
							processId);

			// Kill the hh.exe process by pid
			cmd = new RunCommand();
			response = new CommandResponse();
			KillProcessesCommand kpc = new KillProcessesCommand(cmd);
			result = kpc.execute("osprey", processId);
			Assert.assertNull("Expecting not to find an error.", result
					.get(Command.ERROR));

			// Check to make sure the hh.exe process id no longer exists
			cmd = new RunCommand();
			response = new CommandResponse();
			lsc = new ListProcessesCommand(cmd);
			result = lsc.execute("osprey");
			Assert.assertNull("Expecting not to find hh.exe process id: "
					+ processId + ".", result.get(processId));

		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	private boolean findMapValue(Map<String, String> map, String searchValue) {
		// This assumes a null key is illegal for a Map
		return (findMapKeyByValue(map, searchValue) != null);
	}

	private String findMapKeyByValue(Map<String, String> map, String searchValue) {
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			if (entry.getValue().equals(searchValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public void testExecuteForMissingProcessZZTop() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		KillProcessesCommand kpc = new KillProcessesCommand(cmd);
		try {
			Map<String, String> result = kpc.execute("osprey", "-1");
			Assert.assertTrue("Expecting to find an error", result
					.containsKey(Command.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testExecuteForInvalidServerName() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		KillProcessesCommand kpc = new KillProcessesCommand(cmd);
		try {
			Map<String, String> result = kpc.execute("ZZTop", "0");
			Assert.assertTrue("Expecting to find an error", result
					.containsKey(Command.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}	}
}
