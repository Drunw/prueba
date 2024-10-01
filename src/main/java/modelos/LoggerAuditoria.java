package modelos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RegisterForReflection
@ApplicationScoped
public class LoggerAuditoria {

    public static final String LOG_SIMPLE = "imprimirLogSimple(${exchangeProperty.serviceCode},${exchangeProperty.serviceName},${exchangeProperty.operationCode},${exchangeProperty.esbMessageID},${exchangeProperty.type},${exchangeProperty.status},${body},${exchangeProperty.message},${exchangeProperty.operationName})";
    public static final String PROPIEDAD_MENSAJE = "message";
    private static final Logger logger = LoggerFactory.getLogger(LoggerAuditoria.class);

    public void imprimirLogSimple(String serviceCode, String serviceName, String operationCode, String childTrx, String type, String status, String body, String message, String operationName){
        String bodySinEspaciosNiSaltosdeLinea = body.replaceAll("\\s+","");
        String log = String.format("Telefonica::{\"serviceCode\":\"%s\",\"serviceName\":\"%s\",\"operationCode\":\"%s\",\"operationName\":\"%s\",\"childTrxId\":\"%s\",\"type\":\"%s\",\"status\":\"%s\",\"message\":\"%s\",\"data\":\"%s\"",serviceCode,serviceName,operationCode,operationName,childTrx,type,status,message,bodySinEspaciosNiSaltosdeLinea);
        logger.info(log);
    }
    public AuditRelic requestToNewRelic(Exchange exchange, String environment) {
        String serviceCode = (String) exchange.getProperty("serviceCode");
        String serviceName = (String) exchange.getProperty("serviceName");
        String operationCode = (String) exchange.getProperty("operationCode");
        String operationName = (String) exchange.getProperty("operationName");
        String childTrx = (String) exchange.getProperty("esbMessageID");
        String type = (String) exchange.getProperty("type");
        String body = (String) exchange.getIn().getBody();
        String status = (String) exchange.getProperty("status");
        String message = (String) exchange.getProperty("message");
        String bodySinEspaciosNiSaltosdeLinea = body.replaceAll("\\s+","");
        String log = String.format("Telefonica::{\"serviceCode\":\"%s\",\"serviceName\":\"%s\",\"operationCode\":\"%s\",\"operationName\":\"%s\",\"childTrxId\":\"%s\",\"type\":\"%s\",\"status\":\"%s\",\"message\":\"%s\",\"data\":\"%s\"",serviceCode,serviceName,operationCode,operationName,childTrx,type,status,message,bodySinEspaciosNiSaltosdeLinea);
        String logSinEspaciosNiSaltosdeLinea = log.replaceAll("\\\\", "");
        String message2 = logSinEspaciosNiSaltosdeLinea;
        return AuditRelic.builder()
                .message(message2)
                .attributes(mappingAttributes(exchange, environment))
                .build();
    }

    private Attributes mappingAttributes(Exchange exchange, String environment) {
        String catalog = (String) exchange.getProperty("catalogId");
        String eventType = (String) exchange.getProperty("eventType");
        String level = eventType.equalsIgnoreCase("error")
                || eventType.equalsIgnoreCase("backEndError") ? "ERROR" : "INFO";
        String parentTrxId = (String) exchange.getProperty("esbMessageID");
        return Attributes.builder()
                .parentId(parentTrxId)
                .level(level)
                .serviceName(String.valueOf(exchange.getProperty("serviceName")))
                .operationName(String.valueOf(exchange.getProperty("operationName")))
                .hostname("ms-ocq-qkus-integracion-openIdRetrieveUser")
                .environment(environment)
                .catalogId(catalog)
                .tel(mappingTel(exchange))
                .build();
    }

    private Map<String, String> mappingTel(Exchange exchange) {
        Map<String, String> attributesTel = new HashMap<>();
        String eventType = (String) exchange.getProperty("eventType");
        Map<String, Object> headers = exchange.getIn().getHeaders();
        attributesTel.put("eventType", eventType);
        if (!eventType.equalsIgnoreCase("request") && !eventType.equalsIgnoreCase("response")
                && !eventType.equalsIgnoreCase("error")) {
            String childTrxId = (String) exchange.getProperty("esbMessageID");
            attributesTel.put("transactionId", childTrxId);
            attributesTel.put("operation", (String) exchange.getProperty("operationName"));
            attributesTel.put("serviceName", (String) exchange.getProperty("serviceName"));
        }
        Object application = headers.get("UNICA-Application");
        if (application != null) {
            attributesTel.put("application", application.toString());
        }

        Map<String, String> telEntries = exchange.getProperties().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("tel_"))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replaceFirst("tel_", ""),
                        entry -> (String) entry.getValue()
                ));

        if (telEntries != null && !telEntries.isEmpty()) {
            attributesTel.putAll(telEntries);
        }
        return attributesTel;
    }

}