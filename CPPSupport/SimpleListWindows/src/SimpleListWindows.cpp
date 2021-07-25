//============================================================================
// Name        : SimpleListWindows.cpp
// Author      : Don Trummell
// Version     : 0.1
// Copyright   : (c) 2021, Don Trummell
// Description : List Key Windows Information
//
// See: https://www.whitebyte.info/programming/how-to-get-main-window-handle-of-the-last-active-window
// See: https://www.devegou.com/1180.html
// See: https://www.dreamincode.net/forums/topic/70823-enumchildwindows/
//============================================================================

#include <windows.h>
#include <iostream>
#include <IOmanip>
using namespace std;
#include "SimpleListWindows.h"

static const int N = 256;

int win_count = 0;
int win_hidden_count = 0;
int win_no_title = 0;

struct Windows_Info {
	HWND active_window;
	HWND focus_window;
	HWND forground_window;
	HWND desktop_window;
	HWND console_window;
	HWND zorder1;
};

Windows_Info winfo = Windows_Info();

void Setup_Known_WIndows() {
	// One window has the focus -- the one that's first in line to get keyboard events.
	// The outer window (that the user can drag around the screen) is "active"
	// if one of its sub-windows has the focus, but it might or might not have focus itself.

	winfo.active_window = GetActiveWindow(); // top level window associated with focus
	cout << "   Active Window     : " << std::setw(10) << std::hex
			<< winfo.active_window << endl;
	winfo.focus_window = GetFocus();
	cout << "   Focus Window      : " << std::setw(10) << std::hex
			<< winfo.focus_window << endl;
	winfo.forground_window = GetForegroundWindow();
	cout << "   Foreground Window : " << std::setw(10) << std::hex
			<< winfo.forground_window << endl;
	winfo.desktop_window = GetDesktopWindow();
	cout << "   Desk Top Window   : " << std::setw(10) << std::hex
			<< winfo.forground_window << endl;
	winfo.console_window = GetConsoleWindow();
	cout << "       Console Window: " << std::setw(10) << std::hex
			<< winfo.console_window << endl;
}

void List_Codes() {
	cout << "  Window Type Codes" << endl;
	cout << "  ----------------------" << endl;
	cout << "      V : Visible" << endl;
	cout << "      A : Active" << endl;
	cout << "      F : Focus" << endl;
	cout << "      G : Foreground" << endl;
	cout << "      C : Console" << endl;
	cout << "      E : Enabled" << endl;
	cout << "  ----------------------" << endl;
}

BOOL CALLBACK EnumChildProc(HWND hWnd, LPARAM lParam);	// forward declaration

BOOL List_Window_Info(const char header[], const BOOL do_counts,
		const BOOL do_enabled_check, const HWND hWnd, const LPARAM lParam) {
	BOOL listable_window = FALSE;
	BOOL enabled = TRUE;

	if (IsWindowVisible(hWnd)) {
		char buff[N];
		GetWindowText(hWnd, (LPSTR) buff, sizeof(buff) - 1);
		if (strlen(buff) > 0) {
			listable_window = TRUE;
			if (do_counts) {
				win_count++;
			}

			char marks[] = { '-', '-', '-', '-', '-', '-', '\0' };
			if (IsWindowVisible(hWnd)) {
				marks[visible_window_idx ] = visible_window_mark;
			}
			if (hWnd == winfo.active_window) {
				marks[active_window_idx ] = active_window_mark;
			}
			if (hWnd == winfo.focus_window) {
				marks[focus_window_idx ] = focus_window_mark;
			}
			if (hWnd == winfo.forground_window) {
				marks[foreground_window_idx ] = foreground_window_mark;
			}
			if (hWnd == winfo.console_window) {
				marks[console_idx ] = console_mark;
			}
			enabled = IsWindowEnabled(hWnd);
			if (enabled) {
				marks[enabled_idx ] = enabled_mark;
			}

			if (strlen(header) > 0) {
				cout << header;
			}

			if (do_counts) {
				cout << std::setw(4) << std::dec << win_count << ".";
			}
			cout << " " << marks << setw(10) << std::hex << hWnd << " --> "
					<< buff;

			if (GetParent(hWnd) != (HWND) 0) {
				cout << " *** NOT TOP LEVEL WINDOW ***";
			}

			if (lParam != 0) {
				cout << " lParam(" << std::hex << lParam << ")";
			}
			cout << endl;
		} else {
			if (do_counts) {
				win_no_title++;
			}
		}
	} else {
		if (do_counts) {
			win_hidden_count++;
		}
	}

	if (!enabled && do_enabled_check) {
		EnumChildWindows(hWnd, EnumChildProc, lParam);
		HWND popup = GetLastActivePopup(hWnd);
		if (popup != (HWND) 0) {
			cout << "            --- Popup Window ---" << endl;
			List_Window_Info("            = = =   ", FALSE, FALSE, hWnd, lParam);
			cout << "            --------------------" << endl;
		}
	}

	return listable_window;
}

BOOL CALLBACK EnumChildProc(HWND hWnd, LPARAM lParam) {
	List_Window_Info("      . . .   ", FALSE, TRUE, hWnd, lParam);
	return TRUE;
}

BOOL CALLBACK EnumWindowsProc(HWND hWnd, long lParam) {
	List_Window_Info("", TRUE, TRUE, hWnd, lParam);
	return TRUE;
}

int main() {
	cout << "***List Top Level Windows***" << endl;

	Setup_Known_WIndows();
	cout << endl;
	List_Codes();
	cout << endl;

	BOOL result = EnumWindows(EnumWindowsProc, 0);
	cout << endl << "Done with " << std::dec << win_count
			<< " visible windows and " << win_hidden_count
			<< " hidden windows, with " << win_no_title
			<< " visible windows having no title." << endl;
	system("pause");
	cout << "***" << endl;

	return result ? 1 : 0;
}
