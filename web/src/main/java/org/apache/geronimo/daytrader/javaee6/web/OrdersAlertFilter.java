/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.daytrader.javaee6.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.geronimo.daytrader.javaee6.core.api.TradeServices;
import org.apache.geronimo.daytrader.javaee6.core.direct.CookieUtils;
import org.apache.geronimo.daytrader.javaee6.utils.Log;

@WebFilter("/app")
public class OrdersAlertFilter implements Filter {

    /**
     * Constructor for CompletedOrdersAlertFilter
     */
    public OrdersAlertFilter() {
        super();
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (filterConfig == null)
            return;

        ServletContext sc = filterConfig.getServletContext();

        try {
            String action = req.getParameter("action");
            if (action != null) {
                action = action.trim();
                if ((action.length() > 0) && (!action.equals("logout"))) {
                    String userID;
                    if (action.equals("login")) {
                        userID = req.getParameter("uid");
                    } else {
                        // Use cookies instead of session attributes
                        userID = CookieUtils.getCookieValue(((HttpServletRequest) req).getCookies(), "uidBean");
                        Log.debug("OrderAlertFilter#doFilter(" + action + ") - getCookieValue(uidBean): " + userID);

                    }
                    if ((userID != null) && (userID.trim().length() > 0)) {
                        TradeServices tAction = new TradeAction();
                        java.util.Collection closedOrders = tAction.getClosedOrders(userID);
                        if ((closedOrders != null) && (closedOrders.size() > 0)) {
                            req.setAttribute("closedOrders", closedOrders);
                        }

                    }
                }
            }
        } catch (

        Exception e) {
            Log.error(e, "OrdersAlertFilter - Error checking for closedOrders");
        }

        // String xyz = (String) sc.getAttribute("hitCounter");
        chain.doFilter(req, resp/* wrapper */);

    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
