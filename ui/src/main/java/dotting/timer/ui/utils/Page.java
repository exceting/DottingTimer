package dotting.timer.ui.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable{
	private List<T> records = new ArrayList<T>();
	private int pageNo;
	private int pageSize;
	private int totalCount; // default 0
	
	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public Page(int totalCount){
		this.totalCount = totalCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<T> getRecords() {
		return records;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public boolean isHasNextPage() {
		if(totalCount < pageSize) {
			return false;
		} else if( pageNo * pageSize < totalCount) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取一共有多少页
	 * 
	 * @return
	 */
	public int getPageCount() {
		if(totalCount != 0) {
			return (totalCount % pageSize == 0) ? totalCount / pageSize : (totalCount / pageSize + 1);
		}
		return 0;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	

}
