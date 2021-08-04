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

package org.apache.geronimo.daytrader.javaee6.portfolios.controller;

// Java
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

// Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.OrderDataBean;
// DayTrader
import org.apache.geronimo.daytrader.javaee6.portfolios.service.PortfoliosService;
import org.apache.geronimo.daytrader.javaee6.portfolios.utils.Log;

/**
 * API endpoints are documented using Swagger UI.
 *
 * @see https://{portfoliosServiceHost}:{portfoliosServicePort}/swagger-ui.html
 *
 *      HTTP Methods. There is no official and enforced standard for designing
 *      HTTP & RESTful APIs. There are many ways for designing them. These notes
 *      cover the guidelines used for designing the aforementioned HTTP &
 *      RESTful API.
 *
 *      - GET is used for reading objects; no cache headers is used if the
 *      object should not be cached
 *
 *      - POST is used for creating objects or for operations that change the
 *      server side state. In the case where an object is created, the created
 *      object is returned; instead of it'd URI. This is in keeping with the
 *      existing services. A better practice is to return the URI to the created
 *      object, but we elected to keep the REST APIs consistent with the
 *      existing services. New APIs that return the URI can be added during
 *      Stage 04: Microservices if required.
 *
 *      - PUT is used for updates (full)
 *
 *      - PATCH is used for updates (partial)
 *
 *      TODO: 1. Access Control The controller provides a centralized location
 *      for access control. Currently, the application does not check to see is
 *      a user is logged in before invoking a method. So access control checks
 *      should be added in Stage 04: Microservices
 */

@Path("/")
public class PortfoliosController {

    @Inject
    private PortfoliosService portfoliosService;

    /**
     * REST call to create the user's portfolio.
     * 
     */
    @POST
    @Path("/portfolios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register (AccountDataBean accountData) 
    {
        Log.traceEnter("PortfolioController.register()");       

        try
        {
            // Create the user's portfolio
            accountData = portfoliosService.register(accountData);
            Log.traceExit("PortfoliosController.register()");
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(accountData).build();
        }
        catch (Throwable t)
        {
            Log.error("PortfoliosController.register()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the user's portfolio.
     * 
     */
    @GET
    @Path("/portfolios/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountData (@PathParam("userId") String userId) 
    {
        Log.traceEnter("PortfolioController.getAccountData()");     

        AccountDataBean accountData = null;
    
    
        try
        {
            // Get the account
            accountData = portfoliosService.getAccountData(userId);
            if (accountData != null)
            {
                Log.traceExit("PortfoliosController.getAccountData()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
            }
            else
            {
                Log.traceExit("PortfoliosController.getAccountData()");
                return Response.status(Status.NO_CONTENT).build();
            }
        }
        catch (Throwable t)
        {
            Log.error("PortfoliosController.getAccountData()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * REST call to get the portfolio's holdings.
     *
     */
    @GET
    @Path("/portfolios/{userId}/holdings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHoldings(@PathParam("userId") String userId) {
        Log.traceEnter("PortfoliosController.getHoldings()");

        Collection<HoldingDataBean> holdings = null;

        try {
            holdings = portfoliosService.getHoldings(userId);
            if (holdings != null) {
                Log.traceExit("PortfoliosController.getHoldings()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(holdings).build();
            } else {
                Log.traceExit("PortfoliosController.getHoldings()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("PortfoliosController.getHoldings()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the portfolio's orders.
     *
     */
    @GET
    @Path("/portfolios/{userId}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders(@PathParam("userId") String userId) {
        Log.traceEnter("PortfoliosController.getOrders()");

        Collection<OrderDataBean> orders = null;

        try {
            orders = portfoliosService.getOrders(userId);
            if (orders != null) {
                Log.traceExit("PortfoliosController.getOrders()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(orders).build();
            } else {
                Log.traceExit("PortfoliosController.getOrders()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("PortfoliosController.getOrders()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get orders by their status.
     *
     * At present, the only status that is recognizes is closed. It transitions the
     * closed orders to the completed state and returns them. If it doesn't receive
     * the closed status then it returns HttpStatus.NO_CONTENT
     *
     */
    @PATCH
    @Path("/portfolios/{userId}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersByStatus(@PathParam("userId") String userId, @QueryParam(value = "status") String status) {
        Log.traceEnter("PortfoliosController.getOrdersByStatus()");

        Collection<OrderDataBean> orders = new ArrayList<OrderDataBean>();

        try {
            if (status.equals("closed")) {
                orders = portfoliosService.getClosedOrders(userId);
                if (orders != null) {
                    Log.traceExit("PortfoliosController.getOrdersByStatus()");
                    return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(orders).build();
                } else {
                    Log.traceExit("PortfoliosController.getOrdersByStatus()");
                    return Response.status(Status.NO_CONTENT).build();
                }
            } else {
                Log.error("PortfoliosController.getOrdersByStatus: invalid status=" + status
                        + " valid statuses are 'closed'");
                return Response.status(Status.BAD_REQUEST).header("Cache-Control", "no-cache").entity(orders).build();
            }
        } catch (Throwable t) {
            Log.error("PortfoliosController.getOrdersByStatus()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to buy/sell a stock and add it to the user's portfolio.
     *
     * TODO: - Improved choreography and error handling. The group of actions to
     * purchase a stock must be considered as a single entity even though it crosses
     * multiple microservices.
     */
    @POST
    @Path("/portfolios/{userId}/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processOrder(@PathParam("userId") String userId, OrderDataBean orderData,
            @QueryParam(value = "mode") Integer mode) {
        Log.traceEnter("PortfoliosController.processOrder()");

        try // buy
        {
            if (orderData.getOrderType().equals("buy")) {
                // Buy the specified quantity of stock and add a holding to the portfolio
                orderData = portfoliosService.buy(userId, orderData.getSymbol(), orderData.getQuantity(), mode);
                Log.traceExit("PortfoliosController.processOrder()");
                return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(orderData).build();
            }
        } catch (NotFoundException nfe) {
            Log.error("PortfoliosController.processOrder()", nfe);
            return Response.status(Status.BAD_REQUEST).build();
        } catch (ClientErrorException cee) {
            Log.error("PortfoliosController.processOrder()", cee);
            return Response.status(Status.CONFLICT).build();
        } catch (Throwable t) {
            Log.error("PortfoliosController.processOrder()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        try // sell
        {
            if (orderData.getOrderType().equals("sell")) {
                // Sell the specified holding and remove it from the portfolio
                orderData = portfoliosService.sell(userId, orderData.getHoldingID(), mode);
                Log.traceExit("PortfoliosController.processOrder()");
                return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(orderData).build();
            }
        } catch (NotFoundException nfe) {
            Log.error("PortfoliosController.processOrder()", nfe);
            return Response.status(Status.NOT_FOUND).build();
        } catch (ClientErrorException cee) {
            Log.error("PortfoliosController.processOrder()", cee);
            return Response.status(Status.CONFLICT).build();
        } catch (Throwable t) {
            Log.error("PortfoliosController.processOrder()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        // other
        Throwable t = new BadRequestException(
                "Invalid order type=" + orderData.getOrderType() + " valid types are 'buy' and 'sell'");
        Log.error("PortfoliosController.processOrder()", t);
        return Response.status(Status.BAD_REQUEST).build();
    }

    //
    // Admin Related Endpoints
    //

    /**
     * REST call to register the specified number of users in the query param.
     *
     */
    @POST
    @Path("/admin/tradeBuildDB")
    public Response tradeBuildDB(@QueryParam(value = "limit") int limit, @QueryParam(value = "offset") int offset) {
        Log.traceEnter("PortfolioController.tradeBuildDB()");

        Boolean result = false;

        try {
            // Register max users
            result = portfoliosService.tradeBuildDB(limit, offset);
            Log.traceExit("PortfoliosController.tradeBuildDB()");
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();

        } catch (Throwable t) {
            Log.error("PortfoliosController.tradeBuildDB()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to recreate dbtables
     *
     */
    @POST
    @Path("/admin/recreateDBTables")
    public Response recreateDBTables() {
        Log.traceEnter("PortfoliosController.recreateDBTables()");

        Boolean result = false;

        try {
            result = portfoliosService.recreateDBTables();
            Log.traceExit("PortfoliosController.recreateDBTables()");
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();
        } catch (Throwable t) {
            Log.error("PortfoliosController.recreateDBTables()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to reset trades and return the usage statistics
     *
     */
    @GET
    @Path("/admin/resetTrade")
    public Response resetTrade(@QueryParam(value = "deleteAll") Boolean deleteAll) {
        Log.traceEnter("PortfoliosController.resetTrade()");

        RunStatsDataBean runStatsData = null;

        try {
            runStatsData = portfoliosService.resetTrade(deleteAll);
            if (runStatsData != null) {
                Log.traceExit("PortfoliosController.resetTrade()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(runStatsData).build();
            } else {
                Log.traceExit("PortfoliosController.resetTrade()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("PortfoliosController.resetTrade()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Private helper functions
    //

//    private HttpHeaders getNoCacheHeaders() {
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Cache-Control", "no-cache");
//        return responseHeaders;
//    }

}