#include <Windows.h>
#include <functional>

std::function<void(bool)> g_touchStateCallback;

HHOOK g_hook = nullptr;

LRESULT CALLBACK HookProcedure(int nCode, WPARAM wParam, LPARAM lParam) {
    if (nCode == HC_ACTION) {
        PMSLLHOOKSTRUCT p = (PMSLLHOOKSTRUCT)lParam;
        if ((p->flags & LLMHF_INJECTED) == 0) {
            if (GetSystemMetrics(SM_MAXIMUMTOUCHES) > 0) {
                g_touchStateCallback(true);
            } else {
                g_touchStateCallback(false);
            }
        }
    }
    return CallNextHookEx(g_hook, nCode, wParam, lParam);
}

void SetHook(std::function<void(bool)> callback) {
    g_touchStateCallback = callback;
    g_hook = SetWindowsHookExW(WH_MOUSE_LL, HookProcedure, NULL, 0);
}

void RemoveHook() {
    if (g_hook) {
        UnhookWindowsHookEx(g_hook);
        g_hook = nullptr;
    }
}