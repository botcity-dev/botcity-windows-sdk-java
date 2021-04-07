package dev.botcity.windows.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>Actions</code> is a class that allows the application to handle with
 * applications and process.
 * 
 * @see dev.botcity.windows.sdk.Service
 * @see dev.botcity.windows.sdk.Application
 * 
 * @author <a href="https://github.com/gvom">Gabriel Meneses</a>
 */
public class WindowsSDK {

	/**
	 *
	 * Print all applications runing.
	 *
	 */
	public static void printApplications() {

		List<Application> appsOpeneds;
		try {
			appsOpeneds = getApplications();
			for (Application i : appsOpeneds) {
				System.out.println("Id: " + i.getId() + ", WindowName: " + i.getWindowName() + ", WindowTitle: "
						+ i.getWindowTitle());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Gets all applications runing.
	 *
	 * @return the applications.
	 * @throws SecurityException If a security manager exists and its
	 *                           {@link SecurityManager#checkExec checkExec} method
	 *                           doesn't allow creation of the subprocess
	 *
	 * @throws IOException       If an I/O error occurs
	 */
	public static List<Application> getApplications() throws IOException {

		List<Application> appsOpeneds = new ArrayList<Application>();
		String code = "$Count = 0; $OpenWindows = Get-Process |where {$_.mainWindowTItle} | ` ForEach-Object ProcessName; $Titles = Get-Process |where {$_.mainWindowTItle} | ` ForEach-Object mainwindowtitle; $Id = Get-Process |where {$_.mainWindowTItle} | ` ForEach-Object Id; Foreach ($WindowName in $OpenWindows) { Write-Host $Id[$Count], '::', $WindowName, '::', $Titles[$Count]; $Count = $Count + 1; }";
		String command = "powershell.exe  " + code;
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		powerShellProcess.getOutputStream().close();

		String line;
		BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
		while ((line = stdout.readLine()) != null) {
			String[] i = line.split("::");
			Application app = new Application();
			app.setId(i[0].replaceFirst(" +", ""));
			app.setWindowName(i[1].replaceFirst(" +", ""));
			app.setWindowTitle(i[2].replaceFirst(" +", ""));
			appsOpeneds.add(app);
		}
		stdout.close();

		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		int error = 0;
		while ((line = stderr.readLine()) != null) {
			if (error == 0) {
				System.out.println("Standard Error:");
			}
			System.out.println(line);
			error++;
		}
		stderr.close();

		return appsOpeneds;

	}

	/**
	 *
	 * Kill all applications runing.
	 *
	 * @throws SecurityException If a security manager exists and its
	 *                           {@link SecurityManager#checkExec checkExec} method
	 *                           doesn't allow creation of the subprocess
	 *
	 * @throws IOException       If an I/O error occurs
	 */
	public static void killAllApplications() throws IOException {

		for (Application app : getApplications()) {
			killApplication(app);
		}
	}

	/**
	 *
	 * Focus in a specific application.
	 *
	 * @param app the application to be interrupted.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>app</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>app</code> is empty
	 */
	public static void focusApplication(Application app) throws IOException {

		HWNDHandler.focus(app.getWindowTitle(), app.getSW());
	}

	/**
	 *
	 * Kill a specific application.
	 *
	 * @param app the application to be interrupted.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>app</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>app</code> is empty
	 */
	public static void killApplication(Application app) throws IOException {

		String code = "Stop-Process -ID " + app.getId() + " -Force";
		String command = "powershell.exe " + code;
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		powerShellProcess.getOutputStream().close();
		String line;

		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		int error = 0;
		while ((line = stderr.readLine()) != null) {
			if (error == 0) {
				System.out.println("Standard Error:");
			}
			System.out.println(line);
			error++;
		}
		stderr.close();
	}

	/**
	 *
	 * Print all processes runing.
	 *
	 */
	public static void printProcesses() {

		List<Service> processes;
		try {
			processes = getProcesses();
			for (Service i : processes) {
				System.out.println("Id: " + i.getId() + ", ServiceName: " + i.getServiceName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Gets all processes runing.
	 *
	 * @return the processes
	 * @throws SecurityException If a security manager exists and its
	 *                           {@link SecurityManager#checkExec checkExec} method
	 *                           doesn't allow creation of the subprocess
	 *
	 * @throws IOException       If an I/O error occurs
	 */
	public static List<Service> getProcesses() throws IOException {

		List<Service> processes = new ArrayList<Service>();
		String code = "$Count = 0; $OpenWindows = Get-Process | ` ForEach-Object ProcessName; $Id = Get-Process | ` ForEach-Object Id; Foreach ($WindowName in $OpenWindows) { Write-Host $Id[$Count], '::', $WindowName; $Count = $Count + 1; }";
		String command = "powershell.exe  " + code;
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		powerShellProcess.getOutputStream().close();

		String line;
		BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
		while ((line = stdout.readLine()) != null) {
			String[] i = line.split("::");
			Service app = new Service();
			app.setId(i[0].replaceFirst(" +", ""));
			app.setServiceName(i[1].replaceFirst(" +", ""));
			processes.add(app);
		}
		stdout.close();

		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		int error = 0;
		while ((line = stderr.readLine()) != null) {
			if (error == 0) {
				System.out.println("Standard Error:");
			}
			System.out.println(line);
			error++;
		}
		stderr.close();

		return processes;

	}

	/**
	 *
	 * Kill a specific process.
	 *
	 * @param service the service to be interrupted.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>service</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>service</code> is empty
	 */
	public static void killProcess(Service service) throws IOException {

		String code = "Stop-Process -ID " + service.getId() + " -Force";
		String command = "powershell.exe " + code;
		Process powerShellProcess = Runtime.getRuntime().exec(command);
		powerShellProcess.getOutputStream().close();
		String line;

		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		int error = 0;
		while ((line = stderr.readLine()) != null) {
			if (error == 0) {
				System.out.println("Standard Error:");
			}
			System.out.println(line);
			error++;
		}
		stderr.close();
	}

	/**
	 *
	 * Kill a list of processes.
	 *
	 * @param processes the processes to be interrupted.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>processes</code> is
	 *                                  <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>processes</code> is empty
	 */
	public static void killProcesses(List<Service> processes) throws IOException {

		for (Service process : processes) {
			killProcess(process);
		}
	}

	/**
	 *
	 * Executes the specified string command in a separate process.
	 *
	 * @param command a String containing command executed.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>command</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>path</code> is empty
	 */
	public static void executeCommand(String command) throws IOException {

		execute(command, "");
	}

	/**
	 *
	 * Executes the specified file in string path in a separate process.
	 *
	 * @param path        a String containing the path of a file to executed.
	 * @param extraParams the extra params
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>path</code> is <code>null</code>,
	 *                                  or one of the elements of
	 *                                  <code>extraParams</code> is
	 *                                  <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>path</code> is empty
	 */
	public static void execute(String path, String extraParams) throws IOException {

		Runtime.getRuntime().exec(path + " " + extraParams);
	}

	/**
	 *
	 * Executes the specified file in string path in a separate process.
	 *
	 * @param path a String containing the path of a file to executed.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>path</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>path</code> is empty
	 */
	public static void execute(String path) throws IOException {

		execute(path, "");
	}

	/**
	 *
	 * Executes a jar specified in a separate process.
	 *
	 * @param path        a String containing the path of a jar to executed.
	 * @param extraParams the extra params
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>path</code> is <code>null</code>,
	 *                                  or one of the elements of
	 *                                  <code>extraParams</code> is
	 *                                  <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>path</code> is empty
	 */
	public static void executeJar(String path, String extraParams) throws IOException {

		execute("java -jar " + path, extraParams);
	}

	/**
	 *
	 * Executes a jar specified in a separate process.
	 *
	 * @param path a String containing the path of a jar to executed.
	 * @throws SecurityException        If a security manager exists and its
	 *                                  {@link SecurityManager#checkExec checkExec}
	 *                                  method doesn't allow creation of the
	 *                                  subprocess
	 *
	 * @throws IOException              If an I/O error occurs
	 *
	 * @throws NullPointerException     If <code>path</code> is <code>null</code>
	 *
	 * @throws IllegalArgumentException If <code>path</code> is empty
	 */
	public static void executeJar(String path) throws IOException {

		execute("java -jar " + path, "");
	}
}
