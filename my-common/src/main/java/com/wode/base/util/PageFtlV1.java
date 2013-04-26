package com.wode.base.util;

import com.godtips.common.UtilString;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-6-26 上午9:52:08
 */
public class PageFtlV1 {
	
	private static final String ellipse_text = "...";
	private static final int num_edge_entries = 2;

	public String doPage(int totalPages, int pageNo, String href) {
		String prev_text = "Prev";
		String next_text = "Next";
		boolean prev_show_always = true;
		boolean next_show_always = true;
		int current_page = pageNo - 1;
		int[] interval = getInterval(current_page, totalPages, pageNo);
		StringBuffer sb = new StringBuffer();
		generatePreviousLink(sb, current_page, totalPages, href, prev_text, prev_show_always);
		generateStartingPoint(sb, interval, current_page, totalPages, href);
		generateIntervalLink(sb, interval, current_page, totalPages, href);
		generateEndingPoint(sb, interval, current_page, totalPages, href);
		generateNextLink(sb, current_page, totalPages, href, next_text, next_show_always);
		return sb.toString();
	}

	// Generate "Previous"-Link
	private String generatePreviousLink(StringBuffer sb, int current_page, int np, String href, String prev_text, boolean prev_show_always) {
		if (null != prev_text && (current_page > 0 || prev_show_always)) {
			appendItem(sb, current_page - 1, np, current_page, prev_text, "prev", href);
		}
		return "";
	}

	// Generate starting points
	private String generateStartingPoint(StringBuffer sb, int[] interval, int current_page, int np, String href) {
		if (interval[0] > 0 && num_edge_entries > 0) {
			int end = Math.min(num_edge_entries, interval[0]);
			for (int i = 0; i < end; i++) {
				appendItem(sb, i, np, current_page, "", "", href);
			}
			if (num_edge_entries < interval[0] && null != ellipse_text) {
				sb.append("<li><span>" + ellipse_text + "</span></li>");
			}
		}
		return "";
	}

	// Generate interval links
	private String generateIntervalLink(StringBuffer sb, int[] interval, int current_page, int np, String href) {
		for (int i = interval[0]; i < interval[1]; i++) {
			appendItem(sb, i, np, current_page, "", "", href);
		}
		return "";
	}

	// Generate ending points
	private String generateEndingPoint(StringBuffer sb, int[] interval, int current_page, int np, String href) {
		if (interval[1] < np && num_edge_entries > 0) {
			if (np - num_edge_entries > interval[1] && null != ellipse_text) {
				sb.append("<li><span>" + ellipse_text + "</span></li>");
			}
			int begin = (int) Math.max(np - num_edge_entries, interval[1]);
			for (int i = begin; i < np; i++) {
				appendItem(sb, i, np, current_page, "", "", href);
			}
		}
		return "";
	}

	// Generate "Next"-Link
	private String generateNextLink(StringBuffer sb, int current_page, int np, String href, String next_text, boolean next_show_always) {
		if (null != next_text && (current_page < np - 1 || next_show_always)) {
			appendItem(sb, current_page + 1, np, current_page, next_text, "next", href);
		}
		return "";
	}

	/**
	 * 
	 * @param page_id
	 * @param totalPages
	 * @param current_page
	 * @param text_p
	 * @param classes_p
	 * @return
	 */
	private String appendItem(StringBuffer sb, int page_id, int np, int current_page, String text_p, String classes_p, String href) {
		int page_id_n = page_id < 0 ? 0 : (page_id < np ? page_id : np - 1);
		String text = UtilString.isEmptyOrNullByTrim(text_p) ? (page_id_n + 1) + "" : text_p;
		String classes = UtilString.isEmptyOrNullByTrim(classes_p) ? "" : classes_p;
		if (page_id_n == current_page) {
			if (UtilString.isEmptyOrNullByTrim(classes)) {
				sb.append("<li><span class=\"current\">" + text + "</span></li>");
			} else {
				sb.append("<li><span class=\"current " + classes + "\">" + text + "</span></li>");
			}
		} else {
			if (UtilString.isEmptyOrNullByTrim(classes)) {
				sb.append("<li><a href=\"" + href + (page_id_n + 1) + "\">" + text + "</a></li>");
			} else {
				sb.append("<li><a class=\"" + classes + "\" href=\"" + href + (page_id_n + 1) + "\">" + text + "</a></li>");
			}
		}
		return "";
	}

	private int[] getInterval(int current_page, int totalPages, int pageNo) {
		int num_display_entries = 6;// 显示多少个链接
		int np = totalPages;// 总页数
		int ne_half = (int) Math.ceil(num_display_entries / 2);
		int upper_limit = np - num_display_entries;
		int start = current_page > ne_half ? Math.max(Math.min(current_page - ne_half, upper_limit), 0) : 0;
		int end = current_page > ne_half ? Math.min(current_page + ne_half, np) : Math.min(num_display_entries, np);
		return new int[] { start, end };
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int totalPages = 3;
		int pageNo = 1;
		int start = 1;
		int end = 2;
		String href = "admin.htm?pageNo=";
		// dd(totalPages, pageNo, start, end, href);

	}
}
