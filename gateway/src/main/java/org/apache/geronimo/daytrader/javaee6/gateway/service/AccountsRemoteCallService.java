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

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

// Daytrader
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountProfileDataBean;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * The remote call service to the accounts microservice.
 *
 * @author
 *
 */
@Path("/")
@RegisterRestClient
public interface AccountsRemoteCallService {

    /**
     *
     * @see AccountsServices#tradeBuildDB(int,int)
     *
     */
    @POST
    @Path("/admin/tradeBuildDB")
    public boolean tradeBuildDB(@QueryParam("limit") int limit, @QueryParam("offset") int offset);

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
     * REST call to recreate dbtables
     *
     */
    @POST
    @Path("/admin/recreateDBTables")
    public boolean recreateDBTables();

    /**
     *
     * @see TradeServices#getAccountData(String)
     *
     */
    @GET
    @Path("/accounts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDataBean getAccountData(@PathParam("userId") String userID);

    /**
     *
     * @see TradeServices#getAccountProfileData(String)
     *
     */
    @GET
    @Path("/accounts/{userId}/profiles")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountProfileDataBean getAccountProfileData(@PathParam("userId") String userId);

    /**
     *
     * @see TradeServices#updateAccountProfile(AccountProfileDataBean)
     *
     */
    @PUT
    @Path("/accounts/{userId}/profiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountProfileDataBean updateAccountProfile(@PathParam("userId") String userId,
            AccountProfileDataBean profileData);

    /**
     *
     * @see TradeServices#login(String,String)
     *
     */
    @PATCH
    @Path("/login/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDataBean login(@PathParam("userId") String userId, String password);

    /**
     *
     * @see TradeServices#logout(String)
     *
     */
    @PATCH
    @Path("/logout/{userId}")
    public void logout(@PathParam("userId") String userId);

    /**
     *
     * @see TradeServices#register(String,String,String,String,String,String,BigDecimal)
     *
     */
    @POST
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountDataBean register(AccountDataBean accountData);

}