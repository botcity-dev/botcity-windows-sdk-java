package dev.botcity.windows.sdk.entity;

import com.sun.jna.platform.win32.WinUser;

/**
 * <code>Application</code> is a class that contains the info about the application.
 * 
 * @see dev.botcity.windows.sdk.execute.WindowsSDK
 * @see dev.botcity.windows.sdk.entity.Service
 * 
 * @author <a href="https://github.com/gvom">Gabriel Meneses</a>
 */
public class Application {

	private String Id;
	private String WindowName;
	private String WindowTitle;
	private int SW = WinUser.SW_SHOWMAXIMIZED;

	/**
	 *
	 * Gets the identifier
	 *
	 * @return the identifier
	 */
	public String getId() {

		return Id;
	}

	/**
	 *
	 * Sets the identifier
	 *
	 * @param id the identifier
	 */
	public void setId(String id) {

		Id = id;
	}

	/**
	 *
	 * Gets the window name
	 *
	 * @return the window name
	 */
	public String getWindowName() {

		return WindowName;
	}

	/**
	 *
	 * Sets the window name
	 *
	 * @param windowName the window name
	 */
	public void setWindowName(String windowName) {

		WindowName = windowName;
	}

	/**
	 *
	 * Gets the window title
	 *
	 * @return the window title
	 */
	public String getWindowTitle() {

		return WindowTitle;
	}

	/**
	 *
	 * Sets the window title
	 *
	 * @param windowTitle the window title
	 */
	public void setWindowTitle(String windowTitle) {

		WindowTitle = windowTitle;
	}

	/**
	 *
	 * Gets the SW
	 *
	 * @return the SW
	 */
	public int getSW() {

		return SW;
	}

	/**
	 *
	 * Show the application maximized
	 *
	 */
	public void SW_SHOWMAXIMIZED() {

		SW = WinUser.SW_SHOWMAXIMIZED;
	}

	/**
	 *
	 * Maximize the application
	 *
	 */
	public void SW_MAXIMIZE() {

		SW = WinUser.SW_MAXIMIZE;
	}

	/**
	 *
	 * Minimize the application
	 *
	 */
	public void SW_MINIMIZE() {

		SW = WinUser.SW_MINIMIZE;
	}

	/**
	 *
	 * Show the application minimized
	 *
	 */
	public void SW_SHOWMINIMIZED() {

		SW = WinUser.SW_SHOWMINIMIZED;
	}

	/**
	 *
	 * Force the application to minimize
	 *
	 */
	public void SW_FORCEMINIMIZE() {

		SW = WinUser.SW_FORCEMINIMIZE;
	}

	/**
	 *
	 * Restore the application to previous size
	 *
	 */
	public void SW_RESTORE() {

		SW = WinUser.SW_RESTORE;
	}

	/**
	 *
	 * Set the application to normal size
	 *
	 */
	public void SW_NORMAL() {

		SW = WinUser.SW_NORMAL;
	}

	/**
	 *
	 * Show the application in normal size
	 *
	 */
	public void SW_SHOWNORMAL() {

		SW = WinUser.SW_SHOWNORMAL;
	}
}
