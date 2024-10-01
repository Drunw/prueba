package Processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapeoXmlDNI2 implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        LinkedHashMap<String , Object> jsonMap = exchange.getIn().getBody(LinkedHashMap.class);
        LinkedHashMap<String , Object> getAccountListResponse = (LinkedHashMap<String, Object>) jsonMap.get("getAccountListResponse");
        ArrayList<Map<String ,Object>> responses = (ArrayList<Map<String ,Object>>) getAccountListResponse.get("return");
        ArrayList customerAccount = new ArrayList();
        for (Map<String ,Object> response : responses){
            LinkedHashMap<String , Object> customerInfo = (LinkedHashMap<String, Object>) response.get("accountIdInfo");
            customerAccount.add(customerInfo.get("accountNo"));
        }
        exchange.setProperty("accountNoList",customerAccount);
    }
}
