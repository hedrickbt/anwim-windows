package com.anwim.microsoft.windows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class CopyOfRunCommand {

	static final String CMD_START = "cmd /c net start \"";
	static final String CMD_STOP = "cmd /c net stop \"";

	public static int startService(String serviceName) throws Exception {
		return execCmd(CMD_START + serviceName + "\"");
	}

	public static int stopService(String serviceName) throws Exception {
		return execCmd(CMD_STOP + serviceName + "\"");
	}

	static int execCmd(String cmdLine) throws Exception {
		Process process = Runtime.getRuntime().exec(cmdLine);
		StreamPumper outPumper = new StreamPumper(process.getInputStream(),
				System.out);
		StreamPumper errPumper = new StreamPumper(process.getErrorStream(),
				System.err);

		outPumper.start();
		errPumper.start();
		process.waitFor();
		outPumper.join();
		errPumper.join();

		return process.exitValue();
	}

	static class StreamPumper extends Thread {
		private InputStream is;
		private PrintStream os;

		public StreamPumper(InputStream is, PrintStream os) {
			this.is = is;
			this.os = os;
		}

		public void run() {
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;

				while ((line = br.readLine()) != null)
					os.println(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
