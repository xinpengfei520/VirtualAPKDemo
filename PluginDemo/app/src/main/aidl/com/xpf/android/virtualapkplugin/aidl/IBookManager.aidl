package com.xpf.android.virtualapkplugin.aidl;

import com.xpf.android.virtualapkplugin.aidl.Book;
import com.xpf.android.virtualapkplugin.aidl.IOnNewBookArrivedListener;

interface IBookManager {
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}