package dev.botcity.windows.sdk.execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.botcity.windows.sdk.entity.Application;
import dev.botcity.windows.sdk.entity.Element;
import dev.botcity.windows.sdk.entity.Service;

/**
 * <code>Actions</code> is a class that allows the application to handle with
 * applications and process.
 * 
 * @see dev.botcity.windows.sdk.entity.Service
 * @see dev.botcity.windows.sdk.entity.Application
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
	 * @param app the application to be focus.
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
	
	/**
	 *
	 * Get all applications with the same title.
	 *
	 * @param title a String containing the title of a application in execution.
	 *
	 * @throws IOException              If an I/O error occurs
	 */
	public static List<Application> getApplicationByTitle(String title) throws IOException {
		List<Application> list = getApplications();
		
		return list.stream().filter(o -> o.getWindowTitle().equals(title)).collect(Collectors.toList());
	}
	
	/**
	 *
	 * Get all applications with the same filename.
	 *
	 * @param filename a String containing the file of a application executed.
	 *
	 * @throws IOException              If an I/O error occurs
	 */
	public static List<Application> getApplicationFile(String fileName) throws IOException {
		List<Application> list = getApplications();
		
		return list.stream().filter(o -> o.getWindowName().equals(fileName)).collect(Collectors.toList());
	}
	
	/**
	 *
	 * Get the application with the id.
	 *
	 * @param id a String containing the identifier of a application in execution.
	 *
	 * @throws IOException              If an I/O error occurs
	 */
	public static Application getApplicationById(String id) throws IOException {
		List<Application> list = getApplications();
		
		return list.stream().filter(o -> o.getId().equals(id)).findFirst().get();
	}
	
	/**
	 *
	 * Get all process with the same filename.
	 *
	 * @param filename a String containing the file of a process executed.
	 *
	 * @throws IOException              If an I/O error occurs
	 */
	public static List<Service> getProcessFile(String fileName) throws IOException {
		List<Service> list = getProcesses();
		
		return list.stream().filter(o -> o.getServiceName().equals(fileName)).collect(Collectors.toList());
	}
	
	/**
	 *
	 * Get the process with the id.
	 *
	 * @param id a String containing the identifier of a process in execution.
	 *
	 * @throws IOException              If an I/O error occurs
	 */
	public static Service getProcessById(String id) throws IOException {
		List<Service> list = getProcesses();
		
		return list.stream().filter(o -> o.getId().equals(id)).findFirst().get();
	}
	
	/**
	 *
	 * Get all elements inside of the application.
	 *
	 * @param app the application running to be verified.
	 */
	public List<Element> getApplicationElements(Application app){
		return HWNDHandler.getApplicationElements(app.getWindowTitle());
	}
	
	/**
	 *
	 * Print all elements inside of the application.
	 *
	 * @param app the application running to be verified.
	 */
	public static void printApplicationElements(Application app) {
		for(Element e : HWNDHandler.getApplicationElements(app.getWindowTitle())) {
			System.out.println(" - Found element "+ e.getId() +" / control class: " + e.getClassn() + " / content: "+ e.getContent());
	        System.out.println();
		}
	}
	
	/**
	 *
	 * Get all elements inside of the application, with the same class.
	 *
	 * @param app the application running to be verified.
	 * @param classn the class to be searched.
	 */
	public static List<Element> getApplicationElementsByClassn(Application app, String classn) {
		List<Element> lstElements = new ArrayList<Element>();
		for(Element e : HWNDHandler.getApplicationElements(app.getWindowTitle())) {
			if(e.getClassn().equals(classn)) {
				lstElements.add(e);
			}
		}
		return lstElements;
	}
	
	/**
	 *
	 * Get all elements inside of the application, that contain the text.
	 *
	 * @param app the application running to be verified.
	 * @param content the text inside of the element to be searched.
	 */
	public static List<Element> getApplicationElementsByContent(Application app, String content) {
		List<Element> lstElements = new ArrayList<Element>();
		for(Element e : HWNDHandler.getApplicationElements(app.getWindowTitle())) {
			if(e.getClassn().contains(content)) {
				lstElements.add(e);
			}
		}
		return lstElements;
	}
	
	/**
	 *
	 * Get the element inside of the application, that contain the id.
	 *
	 * @param app the application running to be verified.
	 * @param id the element identifier.
	 */
	public static Element getApplicationElementsById(Application app, int id) {
		for(Element e : HWNDHandler.getApplicationElements(app.getWindowTitle())) {
			if(e.getId() == id) {
				return e;
			}
		}
		return null;
	}	
}
