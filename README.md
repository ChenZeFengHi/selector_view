# 仿京东（升级版）多级选择器

#### 项目介绍
仿京东（升级版）多级选择器
支持动态添加RecycleView、Tab标签
深度无限扩深
#### 使用说明

```
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
            if (position == 5) {//根据业务需要 send传入null会携带选中的数据回调onAddressSelected方法
                receiver.send(null);
                return;
            }
            receiver.send(list);
        }
    });

selectorView.setSelectedListener(new SelectedListener() {
        @Override
        public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
            Log.e("ambit", selectAbles.toString());
        }
    });
```
