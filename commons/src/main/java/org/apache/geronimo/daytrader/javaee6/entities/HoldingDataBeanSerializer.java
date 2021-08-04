package org.apache.geronimo.daytrader.javaee6.entities;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.geronimo.daytrader.javaee6.utils.TradeConfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class HoldingDataBeanSerializer extends StdSerializer<HoldingDataBean> {

    /**
     *
     */
    private static final long serialVersionUID = -8677151328711691117L;

    public HoldingDataBeanSerializer() {
        super(HoldingDataBeanSerializer.class, false);
    }

    @Override
    public void serialize(HoldingDataBean holding, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        jgen.writeStartObject();
        jgen.writeNumberField("holdingID", holding.getHoldingID());
        jgen.writeNumberField("quantity", holding.getQuantity());
        jgen.writeNumberField("purchasePrice", holding.getPurchasePrice());
        jgen.writeStringField("purchaseDate", sf.format(holding.getPurchaseDate()));
        jgen.writeStringField("quoteID", holding.getQuoteID());
        jgen.writeEndObject();
    }

}
