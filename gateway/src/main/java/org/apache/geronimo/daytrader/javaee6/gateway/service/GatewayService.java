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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.geronimo.daytrader.javaee6.core.beans.MarketSummaryDataBean;
import org.apache.geronimo.daytrader.javaee6.core.beans.RunStatsDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.AccountProfileDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.OrderDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * The remote call service to the trades services.
 *
 * @author
 *
 */

@ApplicationScoped
public class GatewayService {

    @Inject
    @RestClient
    private AccountsRemoteCallService accountsService;

    @Inject
    @RestClient
    private PortfoliosRemoteCallService portfoliosService;

    @Inject
    @RestClient
    private QuotesRemoteCallService quotesService;

    /**
     * @see TradeBuildDB#TradeBuildDB(PrintWriter, String)
     */
    public boolean tradeBuildDB(int limit, int offset) throws Exception {
        if (accountsService.tradeBuildDB(limit, offset)) {
            return portfoliosService.tradeBuildDB(limit, offset);
        }
        return false;
    }

    /**
     * @see TradeBuildDB#TradeBuildDB(PrintWriter, String)
     */
    public boolean quotesBuildDB(int limit, int offset) throws Exception {
        return quotesService.tradeBuildDB(limit, offset);
    }

    /**
     * @see TradeServices#resetTrade(boolean)
     */
    public RunStatsDataBean resetTrade(boolean deleteAll) throws Exception {
        // Note elected to put the orchestration across these microservices in the
        // gateway
        // instead of picking one of the microservices to do so. If we were to pick one
        // of
        // the microservicecs it would be Accounts simply because Accounts is dependent
        // on
        // (calls) Portfolios and Portfolios is dependent on (calls) Quotes
        //
        // Note that the only orchestration logic is for the admin services which create
        // the database tables, generate sample data in the databases, and retrieves run
        // stats from the databases. Although they are unusual services, they are formal
        // business operations in the trade application so we mapped them into the micro
        // services architecture

        // Ask the microservices to reset their trades and return their usage data
        RunStatsDataBean quoteStatsData = quotesService.resetTrade(deleteAll);
        RunStatsDataBean portfolioStatsData = portfoliosService.resetTrade(deleteAll);
        RunStatsDataBean accountStatsData = accountsService.resetTrade(deleteAll);

        // Aggregate the results form the microservices
        RunStatsDataBean runStatsData = accountStatsData;
        runStatsData.setTradeStockCount(quoteStatsData.getTradeStockCount());
        runStatsData.setHoldingCount(portfolioStatsData.getHoldingCount());
        runStatsData.setOrderCount(portfolioStatsData.getOrderCount());
        runStatsData.setBuyOrderCount(portfolioStatsData.getBuyOrderCount());
        runStatsData.setSellOrderCount(portfolioStatsData.getSellOrderCount());
        runStatsData.setCancelledOrderCount(portfolioStatsData.getCancelledOrderCount());
        runStatsData.setOpenOrderCount(portfolioStatsData.getOpenOrderCount());
        runStatsData.setDeletedOrderCount(portfolioStatsData.getDeletedOrderCount());

        return runStatsData;
    }

    /**
     * @see TradeDBServices#recreateDBTables(Object[],PrintWriter)
     */
    public boolean recreateDBTables() throws Exception {
        // create the trade db
        if (accountsService.recreateDBTables()) {
            if (portfoliosService.recreateDBTables()) {
                return quotesService.recreateDBTables();
            }
        }
        return false;
    }

    /**
     * @see TradeServices#buy(String,String,double,int)
     */

    public OrderDataBean buy(String userID, String symbol, double quantity, int orderProcessingMode) throws Exception {
        // Construct the order data from the given params
        OrderDataBean orderData = new OrderDataBean();
        orderData.setSymbol(symbol);
        orderData.setQuantity(quantity);
        orderData.setOrderType("buy");
        orderData.setOrderStatus("open");
        return portfoliosService.processOrder(userID, orderData);
    }

    /**
     * @see TradeServices#sell(String,Integer,int)
     */
    public OrderDataBean sell(String userID, Integer holdingID, int orderProcessingMode) throws Exception {
        // Construct the order data from the given params
        OrderDataBean orderData = new OrderDataBean();
        orderData.setHoldingID(holdingID);
        orderData.setOrderType("sell");
        orderData.setOrderStatus("open");
        return portfoliosService.processOrder(userID, orderData);
    }

    /**
     * @see TradeServices#getMarketSummary()
     */
    public MarketSummaryDataBean getMarketSummary() {
        return quotesService.getMarketSummary("TSIA"); /* Trade Stock Index Average */
    }

    /**
     * @see TradeServices#getOrders(Integer)
     */
    public Collection<OrderDataBean> getOrders(String userID) throws Exception {
        return portfoliosService.getOrders(userID);
    }

    /**
     * @see TradeServices#getClosedOrders(String)
     */
    public Collection<OrderDataBean> getClosedOrders(String userID) throws Exception {
        // REST call transitions closed orders to completed state and returns them.
        return portfoliosService.getOrdersByStatus(userID, "closed");
    }

    /**
     * @see TradeServices#createQuote(String, String, BigDecimal)
     */

    public QuoteDataBean createQuote(String symbol, String companyName, BigDecimal price) throws Exception {
        // Consruct the quote data from the given params
        QuoteDataBean quoteData = new QuoteDataBean();
        quoteData.setSymbol(symbol);
        quoteData.setCompanyName(companyName);
        quoteData.setPrice(price);
        return quotesService.createQuote(quoteData);
    }

    /**
     * @see TradeServices#getQuote(String)
     */
    public QuoteDataBean getQuote(String symbol) throws Exception {
        return quotesService.getQuote(symbol);
    }

    /**
     * @see TradeServices#getAllQuotes(String)
     */
    public Collection<QuoteDataBean> getAllQuotes(int limit, int offset) throws Exception {
        return quotesService.getAllQuotes(limit, offset);
    }

    /**
     * @see TradeServices#getHoldings(String)
     */
    public Collection<HoldingDataBean> getHoldings(String userID) throws Exception {
        return portfoliosService.getHoldings(userID);
    }

    /**
     * @see TradeServices#getAccountData(String)
     */
    public AccountDataBean getAccountData(String userID) throws Exception {
        return accountsService.getAccountData(userID);
    }

    /**
     * @see TradeServices#getProfileData(String)
     */
    public AccountProfileDataBean getAccountProfileData(String userID) throws Exception {
        return accountsService.getAccountProfileData(userID);
    }

    /**
     * @see TradeServices#updateAccountProfileData(AccountProfileDataBean)
     */
    public AccountProfileDataBean updateAccountProfile(AccountProfileDataBean profileData) {
        return accountsService.updateAccountProfile(profileData.getUserID(), profileData);
    }

    /**
     * @see TradeServices#updateQuotePriceVolume(String,BigDecimal,double)
     */
    public QuoteDataBean updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded)
            throws Exception {
        return quotesService.updateQuotePriceVolume(symbol, changeFactor, sharesTraded);
    }

    /**
     * @see TradeServices#login(String, String)
     */
    public AccountDataBean login(String userID, String password) throws Exception {
        return accountsService.login(userID, password);
    }

    /**
     * @see TradeServices#logout(String)
     */
    public void logout(String userID) throws Exception {
        accountsService.logout(userID);
    }

    /**
     * @see TradeServices#register(String, String, String, String, String, String,
     *      BigDecimal, boolean)
     */
    public AccountDataBean register(String userID, String password, String fullname, String address, String email,
            String creditCard, BigDecimal openBalance) {
        // Consruct the account data from that given params
        AccountDataBean accountData = new AccountDataBean();
        accountData.setProfileID(userID);
        accountData.setOpenBalance(openBalance);
        accountData.setProfile(new AccountProfileDataBean());
        accountData.getProfile().setUserID(userID);
        accountData.getProfile().setPassword(password);
        accountData.getProfile().setFullName(fullname);
        accountData.getProfile().setAddress(address);
        accountData.getProfile().setEmail(email);
        accountData.getProfile().setCreditCard(creditCard);
        return accountsService.register(accountData);
    }

    /**
     * @see TradeServices#updateQuotePriceVolume(String,BigDecimal,double,boolean)
     */
    public QuoteDataBean updateQuotePriceVolumeInt(String symbol, BigDecimal changeFactor, double sharesTraded,
            boolean publishQuotePriceChange) throws Exception {
        // The parameter publishQuotePriceChange isn't required; it was always false
        return updateQuotePriceVolume(symbol, changeFactor, sharesTraded);
    }

}
