package Processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.LinkedHashMap;

public class MapeoXmlMSISDN implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        LinkedHashMap<String , Object> jsonMap = exchange.getIn().getBody(LinkedHashMap.class);
        LinkedHashMap<String , Object> body = (LinkedHashMap<String, Object>) jsonMap.get("return");
        boolean isEmpty = body.isEmpty() || body.values().stream().allMatch(value -> value instanceof String && ((String) value).isEmpty());
        if (!isEmpty){
            exchange.setProperty("accountNo",body.get("accountNo"));
            exchange.setProperty("customerNo",body.get("customerNo"));
        }
        else {
            exchange.getIn().setBody(null);
        }
    }
}
