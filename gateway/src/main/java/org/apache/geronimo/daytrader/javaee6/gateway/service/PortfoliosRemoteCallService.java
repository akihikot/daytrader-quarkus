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

package org.apache.geronimo.daytrader.javaee6.gateway.service;

// Java Runtime
import java.io.PrintWriter;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.OrderDataBean;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * The remote call service to the accounts microservice.
 *
 * @author
 *
 */
@Path("/")
@RegisterRestClient
public interface PortfoliosRemoteCallService {

    /**
     *
     * @see PortfoliosServices#tradeBuildDB(int,int)
     *
     */
    @POST
    @Path("/admin/tradeBuildDB")
    public boolean tradeBuildDB(@QueryParam(value = "limit") int limit, @QueryParam(value = "offset") int offset);

    /**
     *
     * @see TradeServices#resetTrade(boolean)
     *
     */
    @GET
    @Path("/admin/resetTrade")
    @Produces(MediaType.APPLICATION_JSON)
    public RunStatsDataBean resetTrade(@QueryParam(value = "deleteAll") Boolean deleteAll);

    /**
     *
     * @see TradeDBServices#recreateDBTables(Object[],PrintWriter)
     *
     */
    @POST
    @Path("/admin/recreateDBTables")
    public boolean recreateDBTables();

    /**
     *
     * - Endpoint: POST /portfolios/{userId}/orders?mode
     */
    @POST
    @Path("/portfolios/{userId}/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderDataBean processOrder(@PathParam("userId") String userId, OrderDataBean orderData);

    /**
     *
     * @see TradeServices#getOrders(String)
     *
     */
    @GET
    @Path("/portfolios/{userId}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<OrderDataBean> getOrders(@PathParam("userId") String userId);

    @PATCH
    @Path("/portfolios/{userId}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<OrderDataBean> getOrdersByStatus(@PathParam("userId") String userId,
            @QueryParam(value = "status") String status);

    /**
     *
     * @see TradeServices#getHoldings(String)
     *
     */
    @GET
    @Path("/portfolios/{userId}/holdings")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<HoldingDataBean> getHoldings(@PathParam("userId") String userId);

}