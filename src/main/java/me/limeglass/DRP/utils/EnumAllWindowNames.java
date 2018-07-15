package me.limeglass.DRP.utils;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public class EnumAllWindowNames {
	
	static interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		interface WNDENUMPROC extends StdCallCallback {
			boolean callback(Pointer hWnd, Pointer arg);
		}

		boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);
		int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
		Pointer GetWindow(Pointer hWnd, int uCmd);
	}

	public static List<String> getAllWindowNames() {
		final List<String> windowNames = new ArrayList<String>();
		final User32 user32 = User32.INSTANCE;
		user32 .EnumWindows(new User32.WNDENUMPROC() {

			@Override
			public boolean callback(Pointer hWnd, Pointer arg) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText).trim();
				if (!wText.isEmpty()) {
					windowNames.add(wText);
				}
				return true;
			}
		}, null);

		return windowNames;
	}
}
