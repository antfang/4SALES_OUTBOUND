package com.sufang.scanner.callback;

import android.graphics.Color;

import com.sufang.scanner.PrintHistory;

public interface MiddlewareListener {
    /**
     * 显示结果
     * @param msg
     */
    void onShowMSG(String msg);    /**
     * 打印
     * @param history
     */
    void onMiddlewarePrint(PrintHistory history);
    /**
     * 查询成功
     * @param history
     */
    void onMiddlewareHistory(PrintHistory history);
    /**
     * 成功显示
     * */
    void onMiddlewareSuccessShow();
    /**
     * 显示颜色
     * */
    void onMiddlewareChangeColor(int code , String msg, int color,Boolean alarm);
    /**
     * 查询失败
     * @param msg
     */
    void onMiddlewareFail(String msg);

    /**
     * 连接成功
     */
    void onConnectSuccess();

    /**
     * 连接失败
     * @param msg
     */
    void onConnectFail(String msg);
}
