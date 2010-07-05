package com.anwim.microsoft.windows;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;
import com.anwim.microsoft.windows.services.ListServicesCommand;
import com.anwim.microsoft.windows.services.ServiceCommand;

public class ListServicesCommandTest extends TestCase {
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
		ListServicesCommand lsc = new ListServicesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("hedrickbt-r61");
			Assert.assertNull("Expecting to find Apache2.2 service.", result
					.get("Apache2.2"));
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
		ListServicesCommand lsc = new ListServicesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("hedrickbt-r61");
			Assert.assertNull("Expecting not to find ZZTop service.", result
					.get("ZZTop"));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testExecuteForInvalidServerName() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		ListServicesCommand lsc = new ListServicesCommand(cmd);
		try {
			Map<String, String> result = lsc.execute("ZZTop");
			Assert.assertNull(
					"Expecting not to find Apache2.2 service on ZZTop.", result
							.get("Apache2.2"));
			Assert.assertTrue("Expecting to find an error", result
					.containsKey(ServiceCommand.ERROR));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
}
