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

package org.apache.geronimo.daytrader.javaee6.gateway.controller;

import java.math.BigDecimal;
// Java
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
// Javax
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.geronimo.daytrader.javaee6.core.beans.MarketSummaryDataBean;
// Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountProfileDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.OrderDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;
// DayTrader
import org.apache.geronimo.daytrader.javaee6.gateway.service.GatewayService;
import org.apache.geronimo.daytrader.javaee6.gateway.utils.Log;

/**
 * API endpoints are documented using Swagger UI.
 *
 * @see https://{serviceHost}:{servicePort}/swagger-ui.html
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
 *      - PUT is used for full updates
 *
 *      - PATCH is used for partial updates
 *
 *      TODO: 1. Access Control The controller provides a centralized location
 *      for access control. Currently, the application does not check to see is
 *      a user is logged in before invoking a method. So access control checks
 *      should be added in Stage 04: Microservices
 */

@Path("/")
public class GatewayController {
    @Inject
    private GatewayService gatewayService;

    //
    // Account Related Endpoints
    //

    /**
     * REST call to register an user provided in the request body.
     *
     */
    @Path("/accounts")
    @POST
    public Response register(AccountDataBean accountData) {
        Log.traceEnter("GatewayController.register()");
        // Get the registration data
        String userID = accountData.getProfileID();
        String password = accountData.getProfile().getPassword();
        String fullname = accountData.getProfile().getFullName();
        String address = accountData.getProfile().getAddress();
        String email = accountData.getProfile().getEmail();
        String creditCard = accountData.getProfile().getCreditCard();
        BigDecimal openBalance = accountData.getOpenBalance();

        try {
            // Register the user
            accountData = gatewayService.register(userID, password, fullname, address, email, creditCard, openBalance);
            Log.traceExit("GatewayController.register()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(accountData).build();
        } catch (Throwable t) {
            Log.error("GatewayController.register()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to update the account profile for given the user id.
     *
     */
    @Path("/accounts/{userId}/profiles")
    @PUT
    public Response updateAccountProfile(@PathParam("userId") String userId, AccountProfileDataBean profileData) {
        Log.traceEnter("GatewayController.updateAccountProfile()");

        try {
            profileData = gatewayService.updateAccountProfile(profileData);
            if (profileData != null) {
                Log.traceExit("GatewayController.updateAccountProfile()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(profileData).build();
            } else {
                Log.traceExit("GatewayController.updateAccountProfile()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(profileData)
                        .build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.updateAccountProfile()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * REST call to get the user's profile
     *
     */
    @Path("/accounts/{userId}/profiles")
    @GET
    public Response getAccountProfileData(@PathParam("userId") String userId) {
        Log.traceEnter("GatewayController.getAccountProfileData()");
        AccountProfileDataBean profileData = null;
        try {
            profileData = gatewayService.getAccountProfileData(userId);
            if (profileData != null) {
                Log.traceExit("GatewayController.getAccountProfileData()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(profileData).build();
            } else {
                Log.traceExit("GatewayController.getAccountProfileData()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(profileData)
                        .build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getAccountProfileData()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the user's account
     *
     */
    @Path("/accounts/{userId}")
    @GET
    public Response getAccountData(@PathParam("userId") String userId) {
        Log.traceEnter("GatewayController.getAccountData()");
        AccountDataBean accountData = null;
        try {
            accountData = gatewayService.getAccountData(userId);
            if (accountData != null) {
                Log.traceExit("GatewayController.getAccountData()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
            } else {
                Log.traceExit("GatewayController.getAccountData()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(accountData)
                        .build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getAccountData()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    //
    // Authentication Related Endpoints
    //

    /**
     * REST call to login the user from the authentication data passed in the body.
     *
     */
    @Path("/login/{userId}")
    @PATCH
    public Response login(@PathParam("userId") String userId, String password) {
        Log.traceEnter("GatewayController.login()");
        AccountDataBean accountData = null;
        try {
            accountData = gatewayService.login(userId, password);
            Log.traceExit("GatewayController.login()");
            // TODO Auto-generated method stub
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
        } catch (NotAuthorizedException nae) {
            Log.error("AccountsController.login()", nae);
            // TODO Auto-generated method stub
            return Response.status(Status.UNAUTHORIZED).build();
        } catch (Throwable t) {
            Log.error("AccountsController.login()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to logout the user with the given userid.
     *
     */
    @Path("/logout/{userId}")
    @PATCH
    public Response logout(@PathParam("userId") String userId) {
        Log.traceEnter("GatewayController.logout()");
        try {
            gatewayService.logout(userId);
            Log.traceExit("GatewayController.logout()");
            // TODO Auto-generated method stub
            return Response.status(Status.OK).entity(true).build();
        } catch (Throwable t) {
            Log.error("GatewayController.logout()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Admin Related Endpoints
    //

    /**
     * REST call to populate the database with the specified users and quotes
     *
     */
    @Path("/admin/tradeBuildDB")
    @POST
    public Response tradeBuildDB(@QueryParam(value = "limit") int limit, @QueryParam(value = "offset") int offset) {
        Log.traceEnter("GatewayController.tradeBuildDB()");

        Boolean success = false;

        try {
            // Register the user
            success = gatewayService.tradeBuildDB(limit, offset);
            Log.traceExit("GatewayController.tradeBuildDB()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(success).build();
        } catch (Throwable t) {
            Log.error("GatewayController.tradeBuildDB()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to populate the database with the specified users and quotes
     *
     */
    @Path("/admin/quotesBuildDB")
    @POST
    public Response quotesBuildDB(@QueryParam(value = "limit") int limit, @QueryParam(value = "offset") int offset) {
        Log.traceEnter("GatewayController.quotesBuildDB()");

        Boolean success = false;

        try {
            // Create the quotes
            success = gatewayService.quotesBuildDB(limit, offset);
            Log.traceExit("GatewayController.quotesBuildDB()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(success).build();
        } catch (Throwable t) {
            Log.error("GatewayController.quotesBuildDB()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to recreate dbtables
     *
     */
    @Path("/admin/recreateDBTables")
    @POST
    public Response recreateDBTables() {
        Log.traceEnter("GatewayController.recreateDBTables()");

        Boolean result = false;

        try {
            result = gatewayService.recreateDBTables();
            Log.traceExit("GatewayController.recreateDBTables()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();
        } catch (Throwable t) {
            Log.error("GatewayContoller.recreateDBTables()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to reset trades and return the usage statistics
     *
     */
    @Path("/admin/resetTrade")
    @GET
    public Response resetTrade(@QueryParam(value = "deleteAll") Boolean deleteAll) {
        Log.traceEnter("GatewayController.resetData()");

        RunStatsDataBean runStatsData = null;

        try {
            runStatsData = gatewayService.resetTrade(deleteAll);
            if (runStatsData != null) {
                Log.traceExit("GatewayController.resetTrade()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(runStatsData).build();
            } else {
                Log.traceExit("GatewayController.resetTrade()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(runStatsData)
                        .build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.resetTrade()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Portfolio related endpoints
    //

    /**
     * REST call to get the portfolio's holdings.
     *
     */
    @Path("/portfolios/{userId}/holdings")
    @GET
    public Response getHoldings(@PathParam("userId") String userId) {
        Log.traceEnter("GatewayController.getHoldings()");

        Collection<HoldingDataBean> holdings = null;

        try {
            holdings = gatewayService.getHoldings(userId);
            if (holdings != null) {
                Log.traceExit("GatewayController.getHoldings()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(holdings).build();
            } else {
                Log.traceExit("GatewayController.getHoldings()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(holdings).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getHoldings()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the portfolio's orders.
     *
     */
    @Path("/portfolios/{userId}/orders")
    @GET
    public Response getOrders(@PathParam("userId") String userId) {
        Log.traceEnter("GatewayController.getOrders()");

        Collection<OrderDataBean> orders = null;

        try {
            orders = gatewayService.getOrders(userId);
            if (orders != null) {
                Log.traceExit("GatewayController.getOrders()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(orders).build();
            } else {
                Log.traceExit("GatewayController.getOrders()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(orders).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getOrders()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get orders by their status.
     *
     * At present, the only status that is recognizes is closed. It transitions the
     * closed orders to the completed state and returns them. If it doesn't receive
     * the closed status then it returns Status.BAD_REQUEST
     *
     */
    @Path("/portfolios/{userId}/orders")
    @PATCH
    public Response getOrdersByStatus(@PathParam("userId") String userId, @QueryParam(value = "status") String status) {
        Log.traceEnter("GatewayController.getOrdersByStatus()");

        Collection<OrderDataBean> orders = new ArrayList<OrderDataBean>();

        try {
            if (status.equals("closed")) {
                orders = gatewayService.getClosedOrders(userId);
                if (orders != null) {
                    Log.traceExit("GatewayController.getOrdersByStatus()");
                    // TODO Auto-generated method stub
                    return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(orders).build();
                } else {
                    Log.traceExit("GatewayController.getOrdersByStatus()");
                    // TODO Auto-generated method stub
                    return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(orders)
                            .build();
                }
            } else {
                Throwable t = new Throwable("GatewayController.getOrdersByStatus: invalid status=" + status
                        + " valid statuses are 'closed'");
                Log.error("GatewayController.getOrdersByStatus()", t);
                // TODO Auto-generated method stub
                return Response.status(Status.BAD_REQUEST).header("Cache-Control", "no-cache").entity(orders).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getOrdersByStatus()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(orders).build();
        }
    }

    /**
     * REST call to buy/sell a stock and add it to the user's portfolio.
     *
     * TODO: - Improved choreography and error handling. The group of actions to
     * purchase a stock must be considered as a single entity even though it crosses
     * multiple microservices.
     */
    @Path("/portfolios/{userId}/orders")
    @POST
    public Response processOrder(@PathParam("userId") String userId, OrderDataBean orderData,
            @QueryParam(value = "mode") Integer mode) {
        Log.traceEnter("GatewayController.processOrder()");
        try // buy()
        {
            if (orderData.getOrderType().equals("buy")) {
                // Buy the specified quantity of stock and add a holding to the portfolio
                orderData = gatewayService.buy(userId, orderData.getSymbol(), orderData.getQuantity(), mode);
                Log.traceExit("GatewayController.processOrder()");
                // TODO Auto-generated method stub
                return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(orderData).build();
            }
        } catch (NotFoundException nfe) {
            Log.error("PortfoliosController.processOrder()", nfe);
            // TODO Auto-generated method stub
            return Response.status(Status.NOT_FOUND).build();
        } catch (ClientErrorException cee) {
            Log.error("PortfoliosController.processOrder()", cee);
            // TODO Auto-generated method stub
            return Response.status(Status.CONFLICT).build();
        } catch (Throwable t) {
            Log.error("GatewayController.processOrder()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        try // sell()
        {
            if (orderData.getOrderType().equals("sell")) {
                // Sell the specified holding and remove it from the portfolio
                orderData = gatewayService.sell(userId, orderData.getHoldingID(), mode);
                Log.traceExit("GatewayController.processOrder()");
                // TODO Auto-generated method stub
                return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(orderData).build();
            }
        } catch (NotFoundException nfe) {
            Log.error("PortfoliosController.processOrder()", nfe);
            // TODO Auto-generated method stub
            return Response.status(Status.NOT_FOUND).build();
        } catch (ClientErrorException cee) {
            Log.error("PortfoliosController.processOrder()", cee);
            // TODO Auto-generated method stub
            return Response.status(Status.CONFLICT).build();
        } catch (Throwable t) {
            Log.error("GatewayController.processOrder()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        // other
        Throwable t = new BadRequestException(
                "Invalid order type=" + orderData.getOrderType() + " valid types are 'buy' and 'sell'");
        Log.error("GatewayController.processOrder()", t);
        // TODO Auto-generated method stub
        return Response.status(Status.BAD_REQUEST).header("Cache-Control", "no-cache").entity(orderData).build();
    }

    /**
     * REST call to get the quote given the symbol.
     *
     */
    @Path("/quotes/{symbol}")
    @GET
    public Response getQuote(@PathParam("symbol") String symbol) {
        Log.traceEnter("GatewayController.getQuote()");

        QuoteDataBean quoteData = null;

        try {
            quoteData = gatewayService.getQuote(symbol);
            if (quoteData != null) {
                Log.traceExit("GatewayController.getQuote()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quoteData).build();
            } else {
                Log.traceExit("GatewayController.getQuote()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quoteData).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getQuote()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get all quotes.
     *
     */
    @Path("/quotes")
    @GET
    public Response getAllQuotes(@QueryParam(value = "limit") Integer limit,
            @QueryParam(value = "offset") Integer offset) {
        Log.traceEnter("GatewayController.getAllQuotes()");

        Collection<QuoteDataBean> quotes = null;

        try {
            //
            // - TODO: Currently get all quotes returns all resources. It is never a good
            // idea to
            // return all resources. So, refactor this method to take in the parameters
            // limit &
            // offset change the SQL query to use those values, e.g.
            //
            // SELECT * FROM quoteejb
            // ORDER BY symbol
            // LIMIT limit OFFSET offset;
            //
            // Note this offset pagination mechanism uses well known keywords from SQL
            // databases
            quotes = gatewayService.getAllQuotes(limit, offset);
            if (quotes != null) {
                Log.traceExit("GatewayController.getAllQuotes()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quotes).build();
            } else {
                Log.traceExit("GatewayController.getAllQuotes()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quotes).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.getAllQuotes()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to create a quote provided in the request body.
     *
     */
    @Path("/quotes")
    @POST
    public Response createQuote(QuoteDataBean quoteData) {
        Log.traceEnter("GatewayController.createQuote()");

        // Get the quote data
        String symbol = quoteData.getSymbol();
        String companyName = quoteData.getCompanyName();
        BigDecimal price = quoteData.getPrice();

        try {
            // Create the quote
            quoteData = gatewayService.createQuote(symbol, companyName, price);
            Log.traceExit("GatewayController.createQuote()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(quoteData).build();
        } catch (Throwable t) {
            Log.error("GatewayController.createQuote()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to update the quote price and volume using data provided in the
     * request body.
     *
     */
    @Path("/quotes/{symbol}")
    @PATCH
    public Response updateQuotePriceVolume(@PathParam("symbol") String symbol,
            @QueryParam(value = "price") BigDecimal price, @QueryParam(value = "volume") double volume) {
        Log.trace("GatewayController.updateQuotePriceVolume()");

        QuoteDataBean quoteData = null;

        try {
            // Update the quote price volume
            quoteData = gatewayService.updateQuotePriceVolume(symbol, price, volume);
            if (quoteData != null) {
                Log.traceExit("GatewayController.updateQuotePriceVolume()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quoteData).build();
            } else {
                Log.traceExit("GatewayController.updateQuotePriceVolume()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quoteData).build();
            }
        } catch (Throwable t) {
            Log.error("GatewayController.updateQuotePriceVolume()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Markets Related Endpoints
    //

    @Path("/markets/{exchange}")
    @GET
    public Response getMarketSummary(@PathParam("exchange") String exchange) {
        Log.traceEnter("GatewayController.getMarketSummary()");

        MarketSummaryDataBean marketSummary = new MarketSummaryDataBean();

        try {
            marketSummary = gatewayService.getMarketSummary();
            Log.traceExit("GatewayController.getMarketSummary()");
            // TODO Auto-generated method stub
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(marketSummary).build();
        } catch (NotFoundException nfe) {
            Log.error("QuotesController.getMarketSummary()", nfe);
            // TODO Auto-generated method stub
            return Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            Log.error("GatewayController.getMarketSummary()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Private helper functions
    //

//	private HttpHeaders getNoCacheHeaders()
//	{
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Cache-Control", "no-cache");
//		return responseHeaders;
//	}

}
