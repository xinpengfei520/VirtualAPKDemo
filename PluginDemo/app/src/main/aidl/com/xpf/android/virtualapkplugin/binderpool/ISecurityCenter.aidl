package com.xpf.android.virtualapkplugin.binderpool;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}