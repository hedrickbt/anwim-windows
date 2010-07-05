package com.anwim.microsoft.windows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class RunCommand {
	public static final int DEFAULT_TIMEOUT = 60000;

	public CommandResponse execute(String[] cmdLine, CommandResponse response)
			throws Exception {
		return execute(cmdLine, response, DEFAULT_TIMEOUT, null);
	}

	public CommandResponse execute(String[] cmdLine, CommandResponse response,
			String inputResponse) throws Exception {
		return execute(cmdLine, response, DEFAULT_TIMEOUT, inputResponse);
	}

	public CommandResponse execute(String[] cmdLine, CommandResponse response,
			int timeout) throws Exception {
		return execute(cmdLine, response, timeout, null);
	}

	public CommandResponse execute(String[] cmdLine, CommandResponse response,
			int timeout, String inputResponse) throws Exception {
		Process process = Runtime.getRuntime().exec(cmdLine);
		
		if ((inputResponse != null) && (inputResponse.trim().length() > 0)) {
			BufferedWriter stdIn = new BufferedWriter(new OutputStreamWriter(
					process.getOutputStream()));
			stdIn.write(inputResponse);
			stdIn.flush();
		}

		StreamPumper outPumper = new StreamPumper(process.getInputStream(),
				System.out, response.getStdOut());
		StreamPumper errPumper = new StreamPumper(process.getErrorStream(),
				System.err, response.getStdErr());

		// outPumper.start();
		// errPumper.start();
		// process.waitFor();
		// outPumper.join();
		// errPumper.join();

		outPumper.start();
		errPumper.start();
		Worker worker = new Worker(process);
		worker.start();
		try {
			worker.join(timeout);
			if (worker.exit != null) {
				response.setExitValue(worker.exit);
				// return worker.exit;
			} else {
				outPumper.setStopped(true);
				errPumper.setStopped(true);
				worker.interrupt();
				outPumper.interrupt();
				errPumper.interrupt();
				Thread.currentThread().interrupt();
				process.destroy();
				throw new TimeoutException();
			}
		} catch (InterruptedException ex) {
			outPumper.setStopped(true);
			errPumper.setStopped(true);
			worker.interrupt();
			outPumper.interrupt();
			errPumper.interrupt();
			Thread.currentThread().interrupt();
			process.destroy();
			throw ex;
		} finally {
			//while (!outPumper.isFinished()) {
			//	Thread.currentThread().sleep(10);
			//}
			outPumper.join();  // NOT THE ISSUE - you cannot call join successfully if the thread hasn't entered the run() method
			//while (!outPumper.isFinished()) {
			//	Thread.currentThread().sleep(10);
			//}
			errPumper.join();   // NOT THE ISSUE - you cannot call join successfully if the thread hasn't entered the run() method
			process.destroy();
		}

		// response.setExitValue(process.exitValue());
		return response;
	}

	private class StreamPumper extends Thread {
		private InputStream is;
		private PrintStream os;
		private ArrayList<String> output;
		private boolean stopped = false;
		//private boolean finished = false;

		public StreamPumper(InputStream is, PrintStream os,
				ArrayList<String> output) {
			this.is = is;
			this.os = os;
			this.output = output;
		}
		
		//public boolean isFinished() {
		//	return finished;
		//}

		public void setStopped(boolean stopped) {
			this.stopped = stopped;
		}
		
		public void run() {
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;

				while (((line = br.readLine()) != null) && (!stopped)) {
				//while ((line = br.readLine()) != null) {
					output.add(line);
					os.println(line);  // debugging only
				}
				//finished = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static class Worker extends Thread {
		private final Process process;
		private Integer exit;

		private Worker(Process process) {
			this.process = process;
		}

		public void run() {
			try {
				exit = process.waitFor();
			} catch (InterruptedException ignore) {
				return;
			}
		}
	}

	

}
