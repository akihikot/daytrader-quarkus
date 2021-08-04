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

package org.apache.geronimo.daytrader.javaee6.portfolios.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Daytrader
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * The remote call service to the quotes microservice.
 *
 * @author
 *
 */

@Path("/")
@RegisterRestClient
public interface QuotesRemoteCallService {

    /**
     * 
     * @see TradeServices#getQuote(String)
     *
     */
    @GET
    @Path("/quotes/{symbol}")
    @Produces(MediaType.APPLICATION_JSON)
    public QuoteDataBean getQuote(@PathParam("symbol") String symbol);

}
