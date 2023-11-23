package com.cs407.barhop;

public interface LoginResultListener {
    void onLoginSuccess();
    void onLoginFailed(String errorMessage);
}
