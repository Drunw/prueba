package Processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.LinkedHashMap;

public class MapeoXml implements Processor {
    @Override
    public void process(Exchange exchange) {
        LinkedHashMap<String , Object> jsonMap = exchange.getIn().getBody(LinkedHashMap.class);
        LinkedHashMap<String , Object> body = (LinkedHashMap<String, Object>) jsonMap.get("Response");
        boolean isEmpty = body.isEmpty() || body.values().stream().allMatch(value -> value instanceof String && ((String) value).isEmpty());
        if (!isEmpty){
            exchange.getIn().setBody(body);
            LinkedHashMap<String , Object> customerInfo = (LinkedHashMap<String, Object>) body.get("customerInfo");
            LinkedHashMap<String , Object> primaryContactInfoX2 = (LinkedHashMap<String, Object>) customerInfo.get("primaryContactInfoX2");
            LinkedHashMap<String , Object> subscriberInfo = (LinkedHashMap<String, Object>) body.get("subscriberInfo");
            LinkedHashMap<String , Object> subscriberInfoX2 = (LinkedHashMap<String, Object>) subscriberInfo.get("subscriberInfoX2");
            String birthday = (String) primaryContactInfoX2.get("birthdate");
            String[] partes = birthday.split("T");
            String primaryCondition = (String) primaryContactInfoX2.get("primaryIdTypeCode");
            String primaryId = (String) primaryContactInfoX2.get("primaryId");
            if (primaryCondition.equalsIgnoreCase("RUT")){
                primaryId.trim();
                primaryId.substring(0, primaryId.length() - 1);
            }
            exchange.setProperty("firstName",primaryContactInfoX2.get("firstName"));
            exchange.setProperty("lastName",primaryContactInfoX2.get("lastName"));
            exchange.setProperty("email",primaryContactInfoX2.get("email"));
            exchange.setProperty("genderCode",primaryContactInfoX2.get("genderCode"));
            exchange.setProperty("birthdate",partes[0]);
            exchange.setProperty("nationalID",primaryId);
            exchange.setProperty("primaryIdTypeTitle",primaryContactInfoX2.get("primaryIdTypeTitle"));
            exchange.setProperty("statusCode",subscriberInfoX2.get("statusCode"));
            exchange.setProperty("msisdn",subscriberInfoX2.get("msisdn"));
        }
        else {
            exchange.getIn().setBody(null);
        }

    }
}
