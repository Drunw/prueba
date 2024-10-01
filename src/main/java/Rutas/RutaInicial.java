package Rutas;

import Processors.MapeoXml;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;

import modelos.InitExecInfo;
import modelos.TefHeaderReq;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@RegisterForReflection
@ApplicationScoped
public class RutaInicial extends RouteBuilder {

    @ConfigProperty(name = "userNameCaller")
    String nameCaller;
    @ConfigProperty(name = "passwordCaller")
    String passCaller;
    @ConfigProperty(name = "application.audit.serviceCode")
    String serviceCode;
    @ConfigProperty(name = "application.audit.serviceName")
    String serviceName;
    @ConfigProperty(name = "application.audit.operationCode")
    String operationCode;
    @ConfigProperty(name = "application.audit.operationName")
    String operationName;

    @Override
    public void configure() throws Exception {
        JacksonXMLDataFormat xmlFormat = new JacksonXMLDataFormat();

        onException(Exception.class)
                .log("HOLA")
                .end();


        from("direct:expInitServiceParam").routeId("expInitServiceParam")
                .setProperty("serviceCode",simple(serviceCode))
                .setProperty("operationCode",simple(operationCode))
                .setProperty("operationName",simple(operationName))
                .setProperty("serviceName",simple(serviceName))
                .setProperty("bodyEntrada",simple("${body}"))
                .to("direct:logsEntradaQueue")
                .setBody(simple("${exchangeProperty.bodyEntrada}"))
                .unmarshal().jacksonXml()
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .setProperty("Header",simple("${body[Header]}"))
                .setProperty("inputBody",simple("${body[Body]}"))
                .setBody(simple("${exchangeProperty.inputBody}"))
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .marshal().jacksonXml()
                .convertBodyTo(String.class)
                .process(exchange -> {
                    String xml = (String) exchange.getIn().getBody();
                    String modifiedXml = xml.replaceAll("<LinkedHashMap>|</LinkedHashMap>", "");
                    exchange.setProperty("inputBodyXml",modifiedXml);
                })
                .setProperty("x-correlator",simple(""))
                .setBody(simple("${exchangeProperty.Header}"))
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .marshal().jacksonXml()
                .convertBodyTo(String.class)
                .process(exchange -> {
                        String xml = (String) exchange.getIn().getBody();
                        String modifiedXml = xml.replaceAll("<LinkedHashMap>|</LinkedHashMap>", "");
                        exchange.setProperty("inputHeader",modifiedXml);
                        exchange.setProperty("esbMessageID",UUID.randomUUID().toString());
                })
                .to("velocity:/serviceInfoValues.vm?allowContextMapAll=true&encoding=UTF-8")
              //  .log("resultado velocity1: ${body}")
                .unmarshal().jacksonXml()
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .setProperty("errorMappingConfig",simple("${body[errorMappingConfig]}"))
                .to("velocity:/ErrorMappingConfig.vm?allowContextMapAll=true&encoding=UTF-8")
             //   .log("resultado velocity 2: ${body}")
                .to("direct:expInitExecutionInfo")
                .to("direct:personalizacionPreInvocacion")
                .end();

        from("direct:expInitExecutionInfo").routeId("expInitExecutionInfo")
                .process(exchange -> {
                    String headerIn = (String) exchange.getProperty("inputHeader");
                    String uudi = (String) exchange.getProperty("esbMessageID");
                    InitExecInfo execInfo = new InitExecInfo("openIdRetrieveUserProfile", "retrieveUserProfile", uudi, System.currentTimeMillis(), headerIn);
                    exchange.setProperty("initExecInfo",execInfo);
                })
                .marshal(xmlFormat)
              //  .log("body initExecInfo: ${exchangeProperty.initExecInfo}")
                .end();

        from("direct:personalizacionPreInvocacion").routeId("personalizacionPreInvocacion")
                .setBody(simple("${exchangeProperty.inputHeader}"))
                .unmarshal().jacksonXml()
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .setProperty("userLogin",simple("${body[userId]}"))
                .setProperty("serviceChannel",simple("${body[subsystem]}"))
                .setProperty("serviceName", simple("${body[operation]}"))
                .setProperty("application",simple("${body[system]}"))
                .setProperty("idMessage",simple("${body[idMessage]}"))
                .setProperty("functionalityCode",simple("${body[functionalityCode]}"))
                .setProperty("ipAddress",simple("${body[ipAddress]}"))
                .process(exchange -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timestamp = LocalDateTime.now().format(formatter);
                    TefHeaderReq tefHeaderReq = new TefHeaderReq();
                    tefHeaderReq.setUserLogin((String) exchange.getProperty("userLogin"));
                    tefHeaderReq.setServiceChannel((String) exchange.getProperty("serviceChannel"));
                    tefHeaderReq.setApplication((String) exchange.getProperty("serviceChannel"));
                    tefHeaderReq.setIdMessage("");
                    tefHeaderReq.setIpAddress("");
                    tefHeaderReq.setTransactionTimestamp(timestamp);
                    tefHeaderReq.setServiceName((String) exchange.getProperty("serviceName"));
                    tefHeaderReq.setVersion("V1");
                    exchange.setProperty("timeStamp",(timestamp));
                    exchange.getIn().setBody(tefHeaderReq);
                })
                .marshal(xmlFormat)
                .convertBodyTo(String.class)
                //.log("TefHeaderReq: ${body}")
                .setProperty("TefHeaderReq",simple("${body}"))
                .setProperty("traceLevel",simple("DEBUG"))
                .to("velocity:/ObtainServiceInfo.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("esbContext",simple("${body}"))
                //.log("ObteinServiceInfo: ${body}")
                .to("velocity:/CreateRequestLog.vm?allowContextMapAll=true&encoding=UTF-8")
               // .log("CreateTraceMessageNovum: ${body}")
                .to("direct:findCallerTransformation")
                .end();


        from("direct:findCallerTransformation")
                .setProperty("nameCaller",simple(nameCaller))
                .setProperty("passCaller",simple(passCaller))
                .choice()
                .when().simple("${exchangeProperty.userLogin} contains 'MSISDN'")
                .process(exchange -> {
                    String entrada = (String) exchange.getProperty("userLogin");
                    String[] partes = entrada.split("-");
                    exchange.setProperty("userId",partes[1]);
                })
                .to("velocity:/PrepareRequestFindCallerMSISDN.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("bodyCaller",simple("${body}"))
                .otherwise()
                .process(exchange -> {
                    String entrada = (String) exchange.getProperty("userLogin");
                    String[] partes = entrada.split("-");
                    exchange.setProperty("userId",partes[1]);
                    exchange.setProperty("info2",partes[0]);
                })
                .to("velocity:/PrepareRequestFindCaller.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("bodyCaller",simple("${body}"))
                .end()
                .to("velocity:/PrepeareHeaderFindCaller.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("headerCaller",simple("${body}"))
                .to("velocity:/FindCallerRequest.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("requestFindCaller",simple("${body}"))
                .to("direct:consumoBackEndFindCaller")
                .end();

        from("direct:MapeoFindCallerResponse").routeId("MapeoFindCallerResponse")
                .unmarshal().jacksonXml()
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().json(JsonLibrary.Jackson)
                .process(new MapeoXml())
                .choice().when(simple("${body} != null"))
                .log("SI")
                .to("direct:MapeoSearchByCustomerRequest")
                .otherwise()
                .log("SALTAR EXCEPCION DE RESPUESTA VACIA.")
                .end()
                .end();

        from("direct:MapeoSearchByCustomerRequest").routeId("MapeoSearchByCustomerRequest")
                .choice().when().simple("${exchangeProperty.userLogin} contains 'MSISDN'")
                .to("velocity:/RequestSearchAccountByMSIDN.vm?allowContextMapAll=true&encoding=UTF-8")
                .log("BODY A ENVIAR A SEARCH BY CUSTOMER: ${body}")
                .otherwise()
                .end()
                .end();

    }
}
