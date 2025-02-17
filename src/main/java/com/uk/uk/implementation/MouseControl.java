package com.uk.uk.implementation;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import org.springframework.stereotype.Service;

@Service
public class MouseControl {

    public interface User32Ex extends Library {
        User32Ex INSTANCE = Native.load("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);
        void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
    }

    private static final int MOUSEEVENTF_MOVE = 0x0001;
    private static final int MOUSEEVENTF_LEFTDOWN = 0x0002;
    private static final int MOUSEEVENTF_LEFTUP = 0x0004;

    public String moveMouseToCenterAndClick() {
        try {
            WinDef.RECT desktopRect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(User32.INSTANCE.GetDesktopWindow(), desktopRect);

            int centerX = (desktopRect.right - desktopRect.left) / 2;
            int centerY = (desktopRect.bottom - desktopRect.top) / 2;

            boolean success = User32.INSTANCE.SetCursorPos(centerX, centerY);
            if (!success) {
                return "Failed to set cursor position.";
            }

            User32Ex.INSTANCE.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
            User32Ex.INSTANCE.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);

            return "Mouse moved to center and clicked.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
