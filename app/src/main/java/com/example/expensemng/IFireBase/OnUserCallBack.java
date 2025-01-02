package com.example.expensemng.IFireBase;

import com.example.expensemng.Models.User;

public interface OnUserCallBack {
    void  onSuccess(User user);
    void onFailure(Exception e);

}
