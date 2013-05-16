package com.wode.base.util;

import java.util.ArrayList;
import java.util.List;

import com.godtips.common.StringUtils;
import com.wode.base.entity.BootstrapVo;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-6-26 上午9:52:08
 */
public class PageBootstrap {
	
	private static final String ellipse_text = "...";
	private static final int num_edge_entries = 2;
	private static final String bootstrap_disabled = "disabled";
	private static final String bootstrap_active = "active";

	public static List<BootstrapVo> doPage(int totalPages, int pageNo, String href) {
		String prev_text = "&laquo;";
		String next_text = "&raquo;";
		boolean prev_show_always = true;
		boolean next_show_always = true;
		int current_page = pageNo - 1;
		int[] interval = getInterval(current_page, totalPages, pageNo);
		List<BootstrapVo> list = new ArrayList<BootstrapVo>();
		generatePreviousLink(list, current_page, totalPages, href, prev_text, prev_show_always);
		generateStartingPoint(list, interval, current_page, totalPages, href);
		generateIntervalLink(list, interval, current_page, totalPages, href);
		generateEndingPoint(list, interval, current_page, totalPages, href);
		generateNextLink(list, current_page, totalPages, href, next_text, next_show_always);
		return list;
	}

	// Generate "Previous"-Link
	private static String generatePreviousLink(List<BootstrapVo> list, int current_page, int np, String href, String prev_text, boolean prev_show_always) {
		if (null != prev_text && (current_page > 0 || prev_show_always)) {
			appendItem(list, current_page - 1, np, current_page, prev_text, "", href);
		}
		return "";
	}

	// Generate starting points
	private static String generateStartingPoint(List<BootstrapVo> list, int[] interval, int current_page, int np, String href) {
		if (interval[0] > 0 && num_edge_entries > 0) {
			int end = Math.min(num_edge_entries, interval[0]);
			for (int i = 0; i < end; i++) {
				appendItem(list, i, np, current_page, "", "", href);
			}
			if (num_edge_entries < interval[0] && null != ellipse_text) {
				BootstrapVo vo = new BootstrapVo();
				vo.setNameInfo(ellipse_text);
				vo.setCssInfo(bootstrap_disabled);
				vo.setPageNo("");
				vo.setIsCurNo("true");
				list.add(vo);
			}
		}
		return "";
	}

	// Generate interval links
	private static String generateIntervalLink(List<BootstrapVo> list, int[] interval, int current_page, int np, String href) {
		for (int i = interval[0]; i < interval[1]; i++) {
			appendItem(list, i, np, current_page, "", "", href);
		}
		return "";
	}

	// Generate ending points
	private static String generateEndingPoint(List<BootstrapVo> list, int[] interval, int current_page, int np, String href) {
		if (interval[1] < np && num_edge_entries > 0) {
			if (np - num_edge_entries > interval[1] && null != ellipse_text) {
				BootstrapVo vo = new BootstrapVo();
				vo.setNameInfo(ellipse_text);
				vo.setCssInfo(bootstrap_disabled);
				vo.setPageNo("");
				vo.setIsCurNo("true");
				list.add(vo);
			}
			int begin = (int) Math.max(np - num_edge_entries, interval[1]);
			for (int i = begin; i < np; i++) {
				appendItem(list, i, np, current_page, "", "", href);
			}
		}
		return "";
	}

	// Generate "Next"-Link
	private static String generateNextLink(List<BootstrapVo> list, int current_page, int np, String href, String next_text, boolean next_show_always) {
		if (null != next_text && (current_page < np - 1 || next_show_always)) {
			appendItem(list, current_page + 1, np, current_page, next_text, "", href);
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
	private static String appendItem(List<BootstrapVo> list, int page_id, int np, int current_page, String text_p, String classes_p, String href) {
		int page_id_n = page_id < 0 ? 0 : np > 0 ? (page_id < np ? page_id : np - 1) : 0;
		String text = StringUtils.isEmptyOrNullByTrim(text_p) ? (page_id_n + 1) + "" : text_p;
		String classes = StringUtils.isEmptyOrNullByTrim(classes_p) ? "" : classes_p;
		BootstrapVo vo = new BootstrapVo();
		vo.setNameInfo("");
		vo.setCssInfo("");
		vo.setPageNo("");
		if (page_id_n == current_page) {
			vo.setIsCurNo("true");
			if (StringUtils.isEmptyOrNullByTrim(classes)) {
				vo.setNameInfo(text);
				vo.setCssInfo(bootstrap_active);
			} else {
				vo.setNameInfo(text);
				vo.setCssInfo(bootstrap_active + " " + classes);
			}
		} else {
			if (StringUtils.isEmptyOrNullByTrim(classes)) {
				vo.setNameInfo(text);
				vo.setPageNo(String.valueOf(page_id_n + 1));
			} else {
				vo.setNameInfo(text);
				vo.setCssInfo(classes);
				vo.setPageNo(String.valueOf(page_id_n + 1));
			}
		}
		list.add(vo);
		return "";
	}

	private static int[] getInterval(int current_page, int totalPages, int pageNo) {
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
		
		PageBootstrap t = new PageBootstrap();
		List<BootstrapVo> list = t.doPage(50, 5, "");
		for (int i = 0; i < list.size(); i++) {
			BootstrapVo vo = list.get(i);
			
			//System.out.println(vo.getPageNo() +"_" + vo.getNameInfo() +"_" + vo.getCssInfo());
			System.out.print(vo.getNameInfo() +"   " );
		}

	}
}
