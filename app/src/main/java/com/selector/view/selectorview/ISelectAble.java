package com.selector.view.selectorview;

/**
 * @author Ambit
 * @date 2018/6/22
 */
public interface ISelectAble {
    /**
     * 显示在栏目上的名字
     */
    String getName();

    /**
     * 用户设定的id，根据这个id，可以获取级栏目或者指定为最终栏目的id
     */
    long getId();

    /**
     * 自定义类型对象。
     */
    Object getArg();

}
