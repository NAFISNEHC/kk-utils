package com.kk.common.vo; /**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: HttpResult
 * Author:   allahbin
 * Date:     2019/8/6 10:43
 * Description: 包含的内容
 */

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈包含的内容〉
 *
 * @author allahbin
 * @create 2019/8/6
 * @since 1.0.0
 */
public class HttpResult<T> {

    private int total;
    private int pages;
    private String size;
    private List<T> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

}