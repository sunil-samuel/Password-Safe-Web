/**
 * SecureInterceptor.java (Sep 17, 2014 - 11:30:10 PM)
 *
 * Sunil Samuel CONFIDENTIAL
 *
 *  [2017] Sunil Samuel
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */

package com.sunilsamuel.passwordsafe.interceptor;

import java.security.AccessControlException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecureInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws AccessControlException, Exception {

		super.preHandle(request, response, handler);
		if (request.getSession().getAttribute("loggedinUserInfo") == null) {
			logger.warn("Required credential is missing");
			String url = request.getContextPath() + "/timedout";
			response.sendRedirect(url);
			return false;
		}
		return true;
	}

	/**
	 * Process after the handler has decided what to do.
	 */
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler, ModelAndView mav) throws Exception {
		logger.debug("In SecureInterceptor postHandle");
		super.postHandle(req, res, handler, mav);
	}

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
			Object handler, Exception ex) throws Exception {
		logger.debug("In SecureInterceptor afterCompletion");
		super.afterCompletion(req, res, handler, ex);
	}

}
