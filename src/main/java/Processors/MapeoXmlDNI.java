package Processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapeoXmlDNI implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        LinkedHashMap<String , Object> jsonMap = exchange.getIn().getBody(LinkedHashMap.class);
        ArrayList<Map<String ,Object>> responses = (ArrayList<Map<String ,Object>>) jsonMap.get("Response");
        ArrayList customerIdList = new ArrayList();
        ArrayList suscriberInfoList = new ArrayList();
        for (Map<String ,Object> response : responses){
            LinkedHashMap<String , Object> customerInfo = (LinkedHashMap<String, Object>) response.get("customerInfo");
            LinkedHashMap<String , Object> customerInfoX2 = (LinkedHashMap<String, Object>) customerInfo.get("customerInfoX2");
            LinkedHashMap<String , Object> primaryContactInfoX2 = (LinkedHashMap<String, Object>) customerInfo.get("primaryContactInfoX2");
            ArrayList<Map<String ,Object>> subscriberInfo = (ArrayList<Map<String ,Object>>) response.get("subscriberInfo");
            LinkedHashMap<String , Object> subscriberInfoX2 = (LinkedHashMap<String, Object>) subscriberInfo.get(0).get("subscriberInfoX2");
            for (Map<String ,Object> suscriberInfos : subscriberInfo){
                LinkedHashMap<String , Object> subscriberInfoMini = (LinkedHashMap<String, Object>) suscriberInfos.get("subscriberInfoX2");
                suscriberInfoList.add(subscriberInfoMini);
            }
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
            customerIdList.add(customerInfoX2.get("customerId"));
        }
        exchange.setProperty("customerIdList",customerIdList);
        exchange.setProperty("suscribeInfoList",suscriberInfoList);
    }
}
