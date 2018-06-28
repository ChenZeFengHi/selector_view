package com.selector.view.selectorview;

import java.util.List;

/**
 * @author Ambit
 * @date 2018/6/22
 */
public interface DataProvider {
    void provideData(int position, DataReceiver receiver);


    interface DataReceiver {
        void send(List<ISelectAble> data);
    }
}
