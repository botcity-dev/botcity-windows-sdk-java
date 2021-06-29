package dev.botcity.windows.sdk.execute;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.win32.StdCallLibrary;

import dev.botcity.windows.sdk.entity.Element;

public class HWNDHandler {
	
	protected interface User32 extends StdCallLibrary {
		User32 INSTANCE = Native.loadLibrary("user32", User32.class);

		int WM_GETTEXT = 0x000D;
		
        boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);
        
        HWND FindWindowA(String lpClassName, String lpWindowName);
        
        HWND FindWindowExA(HWND hwndParent, HWND hwndChildAfter, String lpClassName, String lpWindowName);      
        
        LRESULT SendMessageA(HWND editHwnd, int wmGettext, long l, byte[] lParamStr);
        
        HWND FindWindow(String lpClassName, String lpWindowName);
        int GetWindowRect(HWND handle, int[] rect);
        int SendMessage(HWND hWnd, int msg, int wParam, byte[] lParam); 
        HWND FindWindowEx(HWND parent, HWND child, String className, String window);

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
        boolean EnumChildWindows(HWND parent, WNDENUMPROC callback, LPARAM info);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(HWND hWnd, Pointer arg);
        }

        int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
        long GetWindowLong(HWND hWnd, int index);
        boolean SetForegroundWindow(HWND in);
        int GetClassNameA(HWND in, byte[] lpString, int size);
		
	}
	
	final static HWNDHandler.User32 user32 = HWNDHandler.User32.INSTANCE;

	protected static void focus(final String title, final int sw) {
		user32.ShowWindow(com.sun.jna.platform.win32.User32.INSTANCE.FindWindow(null, title), sw);
	}
	
	
	protected static String getApplicationInfo(HWND editHwnd, String element) {
		byte[] lParamStr = new byte[9999999];
		user32.SendMessageA(editHwnd, User32.WM_GETTEXT, 9999999, lParamStr);
	    return Native.toString(lParamStr);
	}	
	
	protected static List<Element> getApplicationElements(String title) {
		List<Element> lstElements = new ArrayList<Element>();
		HWND hWnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow(null, title);
		byte[] textBuffer = new byte[512];
        User32.INSTANCE.GetWindowTextA(hWnd, textBuffer, 512);
        String wText = Native.toString(textBuffer);
		System.out.println("Window found: " + wText);
    	User32.INSTANCE.EnumChildWindows(hWnd, new User32.WNDENUMPROC() {
            public boolean callback(HWND hWnd, Pointer userData) { 
                byte[] textBuffer = new byte[512];
                User32.INSTANCE.GetClassNameA(hWnd, textBuffer, 512);
                String classn = new String(textBuffer).trim();
                if(!classn.equals("")) {
                	int id = hexadecimalToDecimal((""+hWnd).replace("native@", ""));
                	String content = getApplicationInfo(hWnd,  classn);
                	Element el = new Element(content, classn, id);
                	lstElements.add(el);
                }
                return true;
            }
        }, null);
		return lstElements;
	}
	
	protected static int hexadecimalToDecimal(String hexVal)
    {
        int len = hexVal.length();
        int base = 1; 
        int dec_val = 0;
        
        for (int i = len - 1; i >= 0; i--) {
            if (hexVal.charAt(i) >= '0' && hexVal.charAt(i) <= '9') {
                dec_val += (hexVal.charAt(i) - 48) * base;
                base = base * 16;
            }else if (hexVal.charAt(i) >= 'A' && hexVal.charAt(i) <= 'F') {
                dec_val += (hexVal.charAt(i) - 55) * base;
                base = base * 16;
            }
        }
        return dec_val;
    }
}
