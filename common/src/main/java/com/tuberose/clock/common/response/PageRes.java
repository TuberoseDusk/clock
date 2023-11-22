package com.tuberose.clock.common.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PageRes<T> {
    private final Integer pageNum;
    private final Integer pageSize;
    private final Long total;
    private final List<T> data;

    public PageRes(Integer pageNum, Integer pageSize, Long total, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public static <T> PageRes<T> of(Integer pageNum, Integer pageSize, Long total, List<T> data) {
        return new PageRes<>(pageNum, pageSize, total, data);
    }
}
