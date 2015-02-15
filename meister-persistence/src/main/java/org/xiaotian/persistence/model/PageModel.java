package org.xiaotian.persistence.model;

import java.util.List;


public class PageModel{
	
	@SuppressWarnings("rawtypes")
	private  List dataList; // 要返回的某一页的记录列表
	
	private int recordCount=0; // 总记录数
	
	private int pageCount=0; // 总页数
	
	private int currentPage=1; // 当前页数
	
	private int pageSize=15; // 每页记录数
	
	private int startPos=0;  //当前页的起始位置，默认为0，以0开始    
	//页码显示
	private int pageNum=6;    //显示的页码数
	
	/**
	 * 判断页的信息，只需GETTER方法（is方法）即可
	 * @return
	 */
	public boolean isFirstPage(){
		
		return currentPage == 1;
	}
	
	public boolean isLastPage(){
		
		return currentPage == pageCount;
	}
	
	public boolean isHasPreviousPage(){
		
		return currentPage != 1;
	}
	
	public boolean isHasNextPage(){
		
		return currentPage != pageCount;
	}
	
	/**
	 * 计算总页数，静态方法，供外部直接通过类名调用
	 * @return
	 */
	public static int countTotalPage(final int pageSize, final int allRow){
		
		int totalPage = allRow % pageSize == 0 ? allRow/pageSize : allRow/pageSize+1;
		return totalPage;
	}
	
	/**
	 * 计算当前页开始记录
	 * @return
	 */
	public static int countOffset(final int pageSize, final int currentPage){
		
		final int offset = pageSize*(currentPage-1);
		return offset;
	}
	
	/**
	 * 计算当前页，若为0或者请求URL中没有page?=,则用1代替
	 * @return
	 */
	public static int countCurrentPage(int page){		
		final int curPage = (page==0 ? 1 : page);
		return curPage;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageCount() {
		pageCount = recordCount % pageSize == 0 ? recordCount/pageSize : recordCount/pageSize+1;
		
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	
	public int getStartPos() {
		startPos = pageSize*(currentPage-1);
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@SuppressWarnings("rawtypes")
	public List getDataList() {
		return dataList;
	}

	@SuppressWarnings("rawtypes")
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
	/**
	 * 设置界面上显示的号码页数
	 * 
	 * @param pageCurrent
	 */
	public void setPageCurrent(int pageCurrent){
		this.pageNum=pageCurrent;
	}

	public int getPageLast() {
		if(pageCount<=pageNum)
			return pageCount;
		if(currentPage-pageNum/2<=1)
			return pageNum;
		if(currentPage+pageNum/2-1>=pageCount)
			return pageCount;
		return currentPage+pageNum/2-1;
	}

	public int getPageStart() {
		if(pageCount<=pageNum)
			return 1;
		if(currentPage-pageNum/2<=1)
			return 1;
		if(currentPage+pageNum/2-1>=pageCount)
			return pageCount-pageNum+1;
		return currentPage-pageNum/2;
	}
}