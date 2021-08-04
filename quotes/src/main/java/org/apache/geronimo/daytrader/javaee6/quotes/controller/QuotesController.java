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

package org.apache.geronimo.daytrader.javaee6.quotes.controller;

import java.math.BigDecimal;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

// Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.MarketSummaryDataBean;
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;
// DayTrader
import org.apache.geronimo.daytrader.javaee6.quotes.service.QuotesService;
import org.apache.geronimo.daytrader.javaee6.quotes.utils.Log;

/**
 * API endpoints are documented using Swagger UI.
 *
 * @see https://{quotesServiceHost}:{quotesServicePort}/swagger-ui.html
 *
 *      HTTP Methods. There is no official and enforced standard for designing
 *      HTTP & RESTful APIs. There are many ways for designing them. These notes
 *      cover the guidelines used for designing the aforementioned HTTP &
 *      RESTful API.
 *
 *      - GET is used for reading objects and for partial updates. - POST is
 *      used for creating objects or for operations that change the server side
 *      state. In the case where an object is created, the created object is
 *      returned; instead of it'd URI. This is in keeping with the existing
 *      services. A better practice is to return the URI to the created object,
 *      but we elected to keep the REST APIs consistent with the existing
 *      services. New APIs that return the URI can be added during Stage 04:
 *      Microservices if required. - PUT is used for updates (full and partial)
 *
 *      TODO: 1. Access Control The controller provides a centralized location
 *      for access control. Currently, the application does not check to see is
 *      a user is logged in before invoking a method. So access control checks
 *      should be added in Stage 04: Microservices
 */
@Path("/")
public class QuotesController {

    @Inject
    private QuotesService quotesService;

    //
    // Quotes Related Endpoints
    //

    /**
     * REST call to get the quote given the symbol.
     *
     */
    @GET
    @Path("/quotes/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuote(@PathParam("symbol") String symbol) {
        Log.traceEnter("QuotesController.getQuote()");

        QuoteDataBean quoteData = null;

        try {
            quoteData = quotesService.getQuote(symbol);
            if (quoteData != null) {
                Log.traceExit("QuotesController.getQuote()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quoteData).build();
            } else {
                Log.traceExit("QuotesController.getQuote()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quoteData).build();
            }
        } catch (Throwable t) {
            Log.error("QuotesController.getQuote()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get all quotes.
     *
     * Notes: added params attribute to request mapping to resolve ambigous mapping
     *
     */
    @GET
    @Path("/quotes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllQuotes(@QueryParam(value = "limit") Integer limit,
            @QueryParam(value = "offset") Integer offset) {
        Log.traceEnter("QuotesController.getAllQuotes()");

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
            quotes = quotesService.getAllQuotes();
            if (quotes != null) {
                Log.traceExit("QuotesController.getAllQuotes()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quotes).build();
            } else {
                Log.traceExit("QuotesController.getAllQuotes()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quotes).build();
            }
        } catch (Throwable t) {
            Log.error("QuotesController.getAllQuotes()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to create a quote provided in the request body.
     *
     */
    @POST
    @Path("/quotes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuote(QuoteDataBean quoteData) {
        Log.traceEnter("QuotesController.createQuote()");

        // Get the quote data
        String symbol = quoteData.getSymbol();
        String companyName = quoteData.getCompanyName();
        BigDecimal price = quoteData.getPrice();

        try {
            // Create the quote
            quoteData = quotesService.createQuote(symbol, companyName, price);
            Log.traceExit("QuotesController.createQuote()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(quoteData).build();
        } catch (Throwable t) {
            Log.error("QuotesController.createQuote()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to update the quote price volume using data provided in the request
     * body.
     *
     */
    @PATCH
    @Path("/quotes/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response updateQuotePriceVolume(@PathParam("symbol") String symbol,
            @QueryParam(value = "price") BigDecimal price, @QueryParam(value = "volume") double volume) {
        Log.traceEnter("QuotesController.updateQuotePriceVolume()");

        QuoteDataBean quoteData = null;

        try {
            // Update the quote price volume
            quoteData = quotesService.updateQuotePriceVolumeInt(symbol, price, volume);
            if (quoteData != null) {
                Log.traceExit("QuotesController.updateQuotePriceVolume()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(quoteData).build();
            } else {
                Log.traceExit("QuotesController.updateQuotePriceVolume()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(quoteData).build();
            }
        } catch (Throwable t) {
            Log.error("QuotesController.updateQuotePriceVolume()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Markets Related Endpoints
    //
    @GET
    @Path("/markets/{exchange}")
    @Produces(MediaType.APPLICATION_JSON)    
    public Response getMarketSummary(@PathParam("exchange") String exchange) {
        Log.traceEnter("QuotesController.getMarketSummary()");

        MarketSummaryDataBean marketSummary = new MarketSummaryDataBean();

        try {
            marketSummary = quotesService.getMarketSummary(exchange);
            Log.traceExit("QuotesController.getMarketSummary()");
            // TODO Auto-generated method stub
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(marketSummary).build();
        } catch (NotFoundException nfe) {
            Log.error("QuotesController.getMarketSummary()", nfe);
            // TODO Auto-generated method stub
            return Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            Log.error("QuotesController.getMarketSummary()", t);
            // TODO Auto-generated method stub
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Admin Related Endpoints
    //

    /**
     * REST call to create the specified number of quotes
     *
     */
    @POST
    @Path("/admin/tradeBuildDB")
    public Response tradeBuildDB(@QueryParam(value = "limit") Integer limit,
            @QueryParam(value = "offset") Integer offset) {
        Log.traceEnter("QuotesController.tradeBuildDB()");

        Boolean success = false;

        try {
            // Create the sample quotes
            success = quotesService.tradeBuildDB(limit, offset);
            Log.traceExit("QuotesController.tradeBuildDB()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(success).build();
        } catch (Throwable t) {
            Log.error("QuotesController.tradeBuildDB()", t);
            // TODO Auto-generated method stub
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
        Log.traceEnter("QuotesController.recreateDBTables()");

        Boolean result = false;

        try {
            result = quotesService.recreateDBTables();
            Log.traceExit("QuotesController.recreateDBTables()");
            // TODO Auto-generated method stub
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();
        } catch (Throwable t) {
            Log.error("QuotesController.recreateDBTables()", t);
            // TODO Auto-generated method stub
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
        Log.traceEnter("QuotesController.resetTrade()");

        RunStatsDataBean runStatsData = new RunStatsDataBean();
        try {
            runStatsData = quotesService.resetTrade(deleteAll);
            if (runStatsData != null) {
                Log.traceExit("QuotesController.resetTrade()");
                // TODO Auto-generated method stub
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(runStatsData).build();
            } else {
                Log.traceExit("QuotesController.resetTrade()");
                // TODO Auto-generated method stub
                return Response.status(Status.NO_CONTENT).header("Cache-Control", "no-cache").entity(runStatsData).build();
            }
        } catch (Throwable t) {
            Log.error("QuotesController.resetTrade()", t);
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
