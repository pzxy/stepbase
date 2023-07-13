package com.base.core.head.ao;

import java.io.Serializable;

import com.gitee.magic.core.valid.annotation.number.IntegerValid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

/**
 * 分页请求参数
 *
 * @author Start
 */
@ToString
public class PageAO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Schema(title="当前页数(默认:1)",example = "1")
    @IntegerValid(min=1)
    private int pageIndex = 1;

	@Schema(title="分页大小(默认:8)",example = "8")
    @IntegerValid(min=1)
    private int pageSize = 8;

    public int getPageIndex() {
    	return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int index() {
    	return (getPageIndex() - 1) * getPageSize();
    }

}
