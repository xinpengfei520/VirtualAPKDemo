package com.xpf.android.virtualapkplugin.manualbinder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

public interface IBookManager extends IInterface {

    String DESCRIPTOR = "com.xpf.android.manualbinder.IBookManager";
    int TRANSACTION_GET_BOOK_LIST = (IBinder.FIRST_CALL_TRANSACTION);
    int TRANSACTION_ADD_BOOK = (IBinder.FIRST_CALL_TRANSACTION + 1);

    List<Book> getBookList() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
