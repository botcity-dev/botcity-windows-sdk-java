package dev.botcity.windows.sdk;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public class HWNDHandler {
	
	protected interface User32 extends StdCallLibrary {
		User32 INSTANCE = Native.loadLibrary("user32", User32.class);

        boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer data);
        
        int GetWindowTextW(WinDef.HWND hWnd, char[] lpString, int nMaxCount);

        boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);
	}

	protected static void focus(final String find, final int sw) {
		final HWNDHandler.User32 user32 = HWNDHandler.User32.INSTANCE;
	    user32.EnumWindows(new WinUser.WNDENUMPROC() {

	        @Override
	        public boolean callback(WinDef.HWND hwnd, Pointer pointer) {
	            char[] windowText = new char[512];
	            user32.GetWindowTextW(hwnd, windowText, 512);
	            String windowName = Native.toString(windowText);
	            if(windowName.contains(find)) {
	                user32.ShowWindow(hwnd, sw);
	                return false;
	            }
	            return true;
	        }

	    }, null);
	}
}
