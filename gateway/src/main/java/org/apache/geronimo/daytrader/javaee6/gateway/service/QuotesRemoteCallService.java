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

import java.io.PrintWriter;
import java.math.BigDecimal;
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

// Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.MarketSummaryDataBean;
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * The remote call service to the accounts microservice.
 *
 * @author
 *
 */

@Path("/")
@RegisterRestClient
public interface QuotesRemoteCallService {

    /**
     *
     * @see QuotesServices#tradeBuildDB(int,int)
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

    @GET
    @Path("/markets/{exchange}")
    @Produces(MediaType.APPLICATION_JSON)
    public MarketSummaryDataBean getMarketSummary(@PathParam("exchange") String exchange);

    /**
     *
     * @see TradeServices#createQuote(String, String, BigDecimal)
     *
     */
    @POST
    @Path("/quotes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public QuoteDataBean createQuote(QuoteDataBean quoteData);

    /**
     *
     * @see TradeServices#getQuote(String)
     *
     */
    @GET
    @Path("/quotes/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    public QuoteDataBean getQuote(@PathParam("symbol") String symbol);

    /**
     *
     * @see TradeServices#getAllQuotes()
     *
     */
    @GET
    @Path("/quotes")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<QuoteDataBean> getAllQuotes(@QueryParam(value = "limit") Integer limit,
            @QueryParam(value = "offset") Integer offset);

    /**
     *
     * @see TradeServices#updateQuotePriceVolume(String,BigDecimal,double)
     *
     */
    @PATCH
    @Path("/quotes/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    public QuoteDataBean updateQuotePriceVolume(@PathParam("symbol") String symbol,
            @QueryParam(value = "price") BigDecimal price, @QueryParam(value = "volume") double volume);

}