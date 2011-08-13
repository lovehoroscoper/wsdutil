/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 *
 * Created on 07/12/2006 21:01:17
 * The JForum Project
 * http://www.jforum.net
 */
package com.godtips.dao.impl.mysql;

import java.util.List;

import net.jforum.dao.BanlistDAO;
import net.jforum.entities.Banlist;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * @author Rafael Steil
 * @version $Id: GenericBanlistDAO.java,v 1.2 2007/03/24 23:26:50 rafaelsteil
 *          Exp $
 */
public class BanlistDaoImpl extends BaseDaoImpl implements BanlistDAO {
	/**
	 * @see net.jforum.dao.BanlistDAO#delete(int)
	 */
	public void delete(int banlistId) {
		this.deleteObject("BanlistModel.delete", banlistId);
	}

	/**
	 * @see net.jforum.dao.BanlistDAO#insert(net.jforum.entities.Banlist)
	 */
	public void insert(Banlist b) {
		this.addObject("BanlistModel.insert", b);
	}

	/**
	 * @see net.jforum.dao.BanlistDAO#selectAll()
	 */
	public List selectAll() {
		return this.queryList("BanlistModel.selectAll", null);
	}
}
