package com.selector.view.selectorview;

public class SelectModel implements ISelectAble {
    private String name;

    public SelectModel() {
    }

    public SelectModel(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Object getArg() {
        return null;
    }
}
