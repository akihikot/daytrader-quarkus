package org.apache.geronimo.daytrader.javaee6.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.geronimo.daytrader.javaee6.core.beans.MarketSummaryDataBean;
import org.apache.geronimo.daytrader.javaee6.utils.TradeConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        HoldingDataBean holding = new HoldingDataBean(1, 111.0, new BigDecimal(222), new Date(), "IBM");
        MarketSummaryDataBean summary = new MarketSummaryDataBean(new BigDecimal(222), new BigDecimal(222), 3223.1,
                Collections.emptyList(), Collections.emptyList());

        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);

        List<Object> res = new ArrayList<>();
        res.add(holding);
        res.add(summary);

        System.out.println(TradeConfig.JSON_SERIALIZER.writeValueAsString(res));
    }

}
