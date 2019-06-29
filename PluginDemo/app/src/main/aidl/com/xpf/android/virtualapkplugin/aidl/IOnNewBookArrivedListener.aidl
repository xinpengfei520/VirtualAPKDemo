package com.xpf.android.virtualapkplugin.aidl;

import com.xpf.android.virtualapkplugin.aidl.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
