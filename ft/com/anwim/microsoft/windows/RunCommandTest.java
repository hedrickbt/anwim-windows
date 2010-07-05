package com.anwim.microsoft.windows;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.anwim.microsoft.windows.CommandResponse;
import com.anwim.microsoft.windows.RunCommand;

public class RunCommandTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testExecuteDirectoryListing() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "dir", "c:\\"};
			cmd.execute(args, response);
			Assert.assertEquals("Invalid response code", 0, response.getExitValue());
			Assert.assertEquals("WINDOWS folder not found ", true, response.stdOutContainsIgnoreCase("WINDOWS"));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
	
	public void testListServices() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc query state= all|grep -i \"DISPLAY_NAME: \"|sed \"s/DISPLAY_NAME: //\""};
			cmd.execute(args, response);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testStopService() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc stop Apache2.2"};
			cmd.execute(args, response);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
	
	public void testStartService() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc start Apache2.2"};
			cmd.execute(args, response);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	public void testServiceStatus() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc queryex Apache2.2"};
			cmd.execute(args, response);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

	
	public void testRemoteServiceStatus() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc \\\\hedrickbt-r61 queryex Apache2.2"};
			cmd.execute(args, response);
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}

// This approach doesn't work
//	public void testRemoteServiceStatusWithRunAs() {
//		RunCommand cmd = new RunCommand();
//		CommandResponse response = new CommandResponse();
//		try {
//			String[] args = {"cmd.exe", "/c", "runas /user:user@domain.com \"sc \\\\hedrickbt-r61 queryex Apache2.2\""};
//			cmd.execute(args, response, "passwordhere\r\n");
//			Assert.assertEquals("The word failed should not be found.",false, response.contains("FAILED"));
//		} catch (Exception e) {
//			fail("This should not throw an exception: " + e.getMessage());
//		}
//	}

	public void testRemoteServiceStatusWithSpaceInDisplayName() {
		RunCommand cmd = new RunCommand();
		CommandResponse response = new CommandResponse();
		try {
			String[] args = {"cmd.exe", "/c", "sc \\\\hedrickbt-r61 queryex \"Windows Search\""};
			cmd.execute(args, response);
			Assert.assertEquals("Did not find the value FAILED for an unknown service name.",true, response.contains("FAILED"));
		} catch (Exception e) {
			fail("This should not throw an exception: " + e.getMessage());
		}
	}
}
