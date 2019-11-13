package cn.geekhalo.common.ddd.support;

import lombok.Data;

@Data
public class PageRequestWrapper<T> {
    private Integer page = 1;
    private Integer size = 10;
    private T bean;
    private String sort;
}
