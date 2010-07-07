package com.anwim.microsoft.windows;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.services.StartServicesCommand;

public class StartServicesCommandTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExecuteForKnownGoodServiceApache() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		StartServicesCommand ssc = new StartServicesCommand(cmd);
		try {
			Map<String, String> result = ssc.execute("osprey", "Apache2.2");
			Assert.assertNull("Expecting not to find an error.", result
					.get(Command.ERROR));
			// Set<String> temp = result.keySet();
			// System.out.println(temp.size());
			// Iterator<String> iter = temp.iterator();
			// int i = 0;
			// while (iter.hasNext()) {
			// String item = (String)iter.next();
			// System.out.println( item + "=" + result.get(item));
			// }
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testExecuteForMissingServiceZZTop() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		StartServicesCommand ssc = new StartServicesCommand(cmd);
		try {
			Map<String, String> result = ssc.execute("osprey", "ZZTop");
			Assert.assertNotNull("Expecting not to start ZZTop service.", result
					.containsKey(Command.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testExecuteForInvalidServerName() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		StartServicesCommand ssc = new StartServicesCommand(cmd);
		try {
			Map<String, String> result = ssc.execute("ZZTop", "DoesNotMatter");
			Assert.assertNotNull(
					"Expecting not to start DoesNotMatter service on ZZTop.", result
							.containsKey(Command.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
}
