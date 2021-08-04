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

package org.apache.geronimo.daytrader.javaee6.accounts.controller;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

// DayTrader
import org.apache.geronimo.daytrader.javaee6.accounts.service.AccountsService;
import org.apache.geronimo.daytrader.javaee6.accounts.utils.Log;
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
// Daytrader
// import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountProfileDataBean;

/**
 * API endpoints are documented using Swagger UI.
 *
 * @see https://{accountsHost}:{accountsPort}/swagger-ui.html
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
public class AccountsController {

    @Inject
    private AccountsService accountsService;

    //
    // Account Related Endpoints
    //

    /**
     * REST call to register the user provided in the request body.
     *
     */
    @POST
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(AccountDataBean accountData) {
        Log.traceEnter("AccountsController.register(" + accountData.getProfileID() + ")");

        // Get the registration data
        String userID = accountData.getProfileID();
        String password = accountData.getProfile().getPassword();
        String fullname = accountData.getProfile().getFullName();
        String address = accountData.getProfile().getAddress();
        String email = accountData.getProfile().getEmail();
        String creditCard = accountData.getProfile().getCreditCard();
        BigDecimal openBalance = accountData.getOpenBalance();

        try {
            // Registers the user and publishes the registered user event
            accountData = accountsService.register(userID, password, fullname, address, email, creditCard, openBalance);
            Log.traceExit("AccountsController.register()");
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
        } catch (Throwable t) {
            Log.error("AccountsController.register()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to update the account profile for given the user id.
     *
     */
    @PUT
    @Path("/accounts/{userId}/profiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccountProfile(@PathParam("userId") String userId, AccountProfileDataBean profileData) {
        Log.traceEnter("AccountsController.updateAccountProfile()");
        try {
            profileData = accountsService.updateAccountProfile(profileData);
            if (profileData != null) {
                Log.traceExit("AccountsController.updateAccountProfile()");
                return Response.status(Status.OK).entity(profileData).build();
            } else {
                Log.traceExit("AccountsController.updateAccountProfile()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("AccountsController.updateAccountProfile()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the user's profile
     *
     */
    @GET
    @Path("/accounts/{userId}/profiles")
    public Response getAccountProfileData(@PathParam("userId") String userId) {
        Log.traceEnter("AccountsController.getAccountProfileData()");
        AccountProfileDataBean profileData = null;
        try {
            profileData = accountsService.getAccountProfileData(userId);
            if (profileData != null) {
                Log.traceExit("AccountsController.getAccountProfileData()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(profileData).build();
            } else {
                Log.traceExit("AccountsController.getAccountProfileData()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("AccountsController.getAccountProfileData()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to get the user's account
     *
     */
    @GET
    @Path("/accounts/{userId}")
    public Response getAccountData(@PathParam("userId") String userId) {
        Log.traceEnter("AccountsController.getAccountData()");
        AccountDataBean accountData = null;
        try {
            accountData = accountsService.getAccountData(userId);
            if (accountData != null) {
                Log.traceExit("AccountsController.getAccountData()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
            } else {
                Log.traceExit("AccountsController.getAccountData()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("AccountsController.getAccountData()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    //
    // Authentication Related Endpoints
    //

    /**
     * REST call to login the user from the authentication data passed in the body.
     *
     * @return The new version of the account
     */

    @PATCH
    @Path("/login/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@PathParam("userId") String userId, String password) {
        Log.traceEnter("AccountsController.login()");
        AccountDataBean accountData = null;
        try {
            accountData = accountsService.login(userId, password);
            Log.traceExit("AccountsController.login()");
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(accountData).build();
        } catch (NotAuthorizedException nae) {
            Log.error("AccountsController.login()", nae);
            return Response.status(Status.UNAUTHORIZED).build();
        } catch (Throwable t) {
            Log.error("AccountsController.login()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * REST call to logout the user with the given userid.
     *
     */
    @PATCH
    @Path("/logout/{userId}")
    public Response logout(@PathParam("userId") String userId) {
        Log.traceEnter("AccountsController.logout()");
        try {
            accountsService.logout(userId);
            Log.traceExit("AccountsController.logout()");
            return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(true).build();
        } catch (Throwable t) {
            Log.error("AccountsController.logout()", t);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //
    // Admin Related Endpoints
    //

    /**
     * REST call to register the specified number of users.
     *
     */
    @POST
    @Path("/admin/tradeBuildDB")
    public Response tradeBuildDB(@QueryParam(value = "limit") Integer limit,
            @QueryParam(value = "offset") Integer offset) {
        Log.traceEnter("AccountsController.tradeBuildDB()");
        Boolean result = false;
        try {
            // Register the sample users
            result = accountsService.tradeBuildDB(limit.intValue(), offset.intValue());
            Log.traceExit("AccountsController.tradeBuildDB()");
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();
        } catch (Throwable t) {
            Log.error("AccountsController.tradeBuildDB()", t);
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
        Log.traceEnter("AccountsController.recreateDBTables()");
        Boolean result = false;
        try {
            result = accountsService.recreateDBTables();
            Log.traceExit("AccountsController.recreateDBTables()");
            return Response.status(Status.CREATED).header("Cache-Control", "no-cache").entity(result).build();
        } catch (Throwable t) {
            Log.error("AccountsController.recreateDBTables()", t);
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
        Log.traceEnter("AccountsController.resetTrade()");
        RunStatsDataBean runStatsData = null;
        try {
            runStatsData = accountsService.resetTrade(deleteAll);
            if (runStatsData != null) {
                Log.traceExit("AccountsController.resetTrade()");
                return Response.status(Status.OK).header("Cache-Control", "no-cache").entity(runStatsData).build();
            } else {
                Log.traceExit("AccountsController.resetTrade()");
                return Response.status(Status.NO_CONTENT).build();
            }
        } catch (Throwable t) {
            Log.error("AccountsController.resetTrade()", t);
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
