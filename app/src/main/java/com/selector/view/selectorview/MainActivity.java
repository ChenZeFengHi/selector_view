package com.selector.view.selectorview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SelectorView selectorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectorView = findViewById(R.id.selectorView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectorView.setDataProvider(new DataProvider() {
                    @Override
                    public void provideData(int position, DataReceiver receiver) {
                        List<ISelectAble> list = new ArrayList<>();
                        list.add(new SelectModel("北京" + (position + 1)));
                        list.add(new SelectModel("上海" + (position + 1)));
                        list.add(new SelectModel("深圳" + (position + 1)));
                        list.add(new SelectModel("河北" + (position + 1)));
                        list.add(new SelectModel("河南" + (position + 1)));
                        list.add(new SelectModel("张家口" + (position + 1)));
                        list.add(new SelectModel("内蒙古" + (position + 1)));
                        list.add(new SelectModel("湖北" + (position + 1)));
                        list.add(new SelectModel("湖南" + (position + 1)));
                        if (position == 5) {
                            receiver.send(null);
                            return;
                        }
                        receiver.send(list);
                    }
                });
            }
        }, 0);

        selectorView.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                Log.e("ambit", selectAbles.toString());
            }
        });

    }
}
