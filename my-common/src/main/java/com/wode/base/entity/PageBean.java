package com.wode.base.entity;

import java.util.List;

public class PageBean {

	/**
	 * 结果集
	 */
	@SuppressWarnings("rawtypes")
	private List resultList;

	/**
	 * 当前页数。
	 */
	private int pageNo = 0;

	/**
	 * 总页数。
	 */
	private int totalPages = 0;

	/**
	 * 每页显示条数,默认每页显示20条。
	 */
	private int pageSize = 20;

	/**
	 * 总记录数。
	 */
	private int totalRows = 0;

	/**
	 * 显示记录的开始行数。
	 */
	private int pageStartRow = 0;

	/**
	 * 显示记录的结束行数。
	 */
	private int pageEndRow = 0;

	/**
	 * 是否有下一页。
	 */
	private boolean hasNextPage = false;

	/**
	 * 是否有上一页。
	 */
	private boolean hasPreviousPage = false;

	/**
	 * 下一页页数。
	 */
	private int nextPage = 0;

	/**
	 * 上一页页数。
	 */
	private int previousPage = 0;

	private int start;

	private int end;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public PageBean() {
	};

	@SuppressWarnings("rawtypes")
	public PageBean(int pageNo, int totalRows, List resultList) {
		this(20, pageNo, totalRows, resultList);
	}

	@SuppressWarnings("rawtypes")
	public PageBean(int pageNo, int pageSize, int totalRows, List resultList) {
		this.resultList = resultList;
		this.pageNo = pageNo;
		this.totalRows = totalRows;
		this.pageSize = pageSize;
		// 计算总页数
		if ((totalRows % pageSize) == 0) {
			this.totalPages = totalRows / pageSize;
			/*
			if (totalRows != 0) {
				this.totalPages = totalRows / pageSize;
			} else {
				this.totalPages = 1;
			}*/
		} else {
			this.totalPages = totalRows / pageSize + 1;
		}

		// 处理当前页数
		if (pageNo < 0) {
			this.pageNo = 1;
		}
		if (pageNo > this.totalPages) {
			this.pageNo = this.totalPages;
		}

		if ((this.pageNo - 1) > 0) {
			this.hasPreviousPage = true;
			this.previousPage = this.pageNo - 1;
		} else {
			this.hasPreviousPage = false;
		}

		if (this.pageNo >= totalPages) {
			this.hasNextPage = false;
			this.nextPage = this.pageNo;
		} else {
			this.hasNextPage = true;
			this.nextPage = this.pageNo + 1;
		}

		if (this.pageNo * pageSize <= totalRows) {
			this.pageEndRow = pageNo * pageSize;
			this.pageStartRow = pageEndRow - pageSize;
		} else {
			this.pageEndRow = totalRows;
			if (totalPages == 0) {
				this.pageStartRow = 0;
			} else {
				this.pageStartRow = pageSize * (totalPages - 1);
			}
		}
		int num_display_entries = 6;// 显示多少个链接
		int np = totalPages;// 总页数
		int ne_half = (int) Math.ceil(num_display_entries / 2);
		int upper_limit = np - num_display_entries;
		int current_page = pageNo - 1;
		this.start = current_page > ne_half ? Math.max(Math.min(current_page - ne_half, upper_limit), 0) : 0;
		this.end = current_page > ne_half ? Math.min(current_page + ne_half, np) : Math.min(num_display_entries, np);
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageStartRow() {
		return pageStartRow;
	}

	public void setPageStartRow(int pageStartRow) {
		this.pageStartRow = pageStartRow;
	}

	public int getPageEndRow() {
		return pageEndRow;
	}

	public void setPageEndRow(int pageEndRow) {
		this.pageEndRow = pageEndRow;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
