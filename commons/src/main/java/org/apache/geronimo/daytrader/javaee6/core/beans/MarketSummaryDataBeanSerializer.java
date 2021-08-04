package org.apache.geronimo.daytrader.javaee6.core.beans;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBean;
import org.apache.geronimo.daytrader.javaee6.entities.HoldingDataBeanSerializer;
import org.apache.geronimo.daytrader.javaee6.entities.QuoteDataBean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MarketSummaryDataBeanSerializer extends StdSerializer<MarketSummaryDataBean> {

    /**
     *
     */
    private static final long serialVersionUID = -8677151328711691117L;

    public MarketSummaryDataBeanSerializer() {
        super(MarketSummaryDataBean.class, false);
    }

    @Override
    public void serialize(MarketSummaryDataBean summary, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        jgen.writeStartObject();

        // {"openTSIA":222,"volume":3223.1,"topGainers":[],"topLosers":[],"summaryDate":"2021-06-11","gainPercent":0.0000,"tsia":222}]

        jgen.writeNumberField("tsia", summary.getTSIA());
        jgen.writeNumberField("openTSIA", summary.getOpenTSIA());
        jgen.writeNumberField("volume", summary.getVolume());
        jgen.writeArrayFieldStart("topGainers");
        for (QuoteDataBean quote: summary.getTopGainers()) {
            jgen.writeObject(quote);
        }
        jgen.writeEndArray();
        jgen.writeArrayFieldStart("topLosers");
        for (QuoteDataBean quote: summary.getTopLosers()) {
            jgen.writeObject(quote);
        }
        jgen.writeEndArray();
        jgen.writeStringField("summaryDate", sf.format(summary.getSummaryDate()));
        jgen.writeNumberField("gainPercent", summary.getGainPercent());
        jgen.writeEndObject();
    }

}
