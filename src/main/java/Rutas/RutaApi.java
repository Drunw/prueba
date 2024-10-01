package Rutas;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import modelos.LoggerAuditoria;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@RegisterForReflection
@ApplicationScoped
public class RutaApi extends RouteBuilder {

    @ConfigProperty(name = "application.audit.new-relic.environment")
    String newRelicEnv;
    @ConfigProperty(name = "application.audit.new-relic.url")
    String newRelicUrl;
    @ConfigProperty(name = "application.audit.new-relic.api-key")
    String newRelicApiKey;
    @ConfigProperty(name = "application.backend.newCaller")
    String findCallerUrl;

    @Inject
    LoggerAuditoria loggerAuditoria;

    protected String fixHttpPath(String path, boolean secure) {

        if (path.startsWith("http")) {
            return path;
        }

        if (secure) {
            return "https://" + path;
        } else {
            return "http://" + path;
        }

    }

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true)
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Ha ocurrido un error al consumir el BackEnd ${exchangeProperty.backend}: ${exception.stacktrace}"))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .setProperty("eventType",simple("30"))
                .setProperty("catalogId",simple("2"))
                .setProperty("errorCode",simple("ERR001"))
                .setProperty("exceptionMessage",simple("${exception}"))
                .setProperty("reason",simple("${exception}"))
                .wireTap("direct:logsEntradaQueue")
                .setProperty("eventType",simple("50"))
                .to("velocity:/ResponseError.vm?allowContextMapAll=true&encoding=UTF-8")
                .convertBodyTo(String.class)
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Body de salida del api."))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .wireTap("direct:logsEntradaQueue")
                .end();


        from("direct:logsEntradaQueue").routeId("LogginEntradaInput")
                .process(exchange -> {
                    exchange.getIn().setBody(loggerAuditoria.requestToNewRelic(exchange, newRelicEnv));
                })
                .marshal().json()
                .removeHeader("*")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Api-Key", simple(newRelicApiKey))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .doTry()
                .toD(fixHttpPath( newRelicUrl+ "?bridgeEndpoint=true", false))
                .doCatch(Exception.class)
                .log("Ocurrió un error al enviar el mensaje al "
                        + "servicio de auditoria: ${exception.message}")
                .end();

        from("direct:consumoBackEndFindCaller").routeId("Consumo BackEnd FindCaller")
                .setProperty("routeId",simple("Consumo BackEnd FindCaller"))
                .setProperty("backend",constant("findCaller"))
                .setProperty("bodyPeticionFindCaller",simple("${body}"))
                .setProperty("type",simple("SERVICE"))
                .setProperty("status",simple("OK"))
                .setProperty("eventType",simple("20"))
                .setProperty("catalogId",simple("109"))
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Peticion al BackEnd FindCaller."))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .convertBodyTo(String.class)
                .wireTap("direct:logsEntradaQueue")
                .setBody(simple("${exchangeProperty.bodyPeticionFindCaller}"))
                .removeHeader("*")
                .setHeader("Content-Type", constant("text/xml"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .toD(fixHttpPath(findCallerUrl +"?bridgeEndpoint=true",false))
                //.to("velocity:/DummyBackend.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty("eventType",simple("30"))
                .setProperty("BodyRespuestaFindCaller",simple("${body}"))
                .convertBodyTo(String.class)
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Respuesta BackEnd FindCaller."))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .wireTap("direct:logsEntradaQueue")
                .setBody(simple("${exchangeProperty.BodyRespuestaFindCaller}"))
                .to("direct:MapeoFindCallerResponse")
                .end();

        from("direct:consumoBackEndSearchByCustomer").routeId("consumoBackEndSearchByCustomer")
                .setProperty("routeId",simple("Consumo BackEnd Search By Customer"))
                .setProperty("backend",constant("Search By Customer"))
                .setProperty("bodyPeticionFindCaller",simple("${body}"))
                .setProperty("type",simple("SERVICE"))
                .setProperty("status",simple("OK"))
                .setProperty("eventType",simple("20"))
                .setProperty("catalogId",simple("552"))
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Peticion al BackEnd Search By Customer."))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .convertBodyTo(String.class)
                .wireTap("direct:logsEntradaQueue")
                .to("velocity:/DummyBackEnd2.vm?allowContextMapAll=true&encoding=UTF-8")
                .setProperty(LoggerAuditoria.PROPIEDAD_MENSAJE,simple("Respuesta BackEnd Search by customer"))
                .bean(loggerAuditoria,LoggerAuditoria.LOG_SIMPLE)
                .setProperty("eventType",simple("30"))
                .setProperty("BodyRespuestaSearchByCustomer",simple("${body}"))
                .convertBodyTo(String.class)
                 .wireTap("direct:logsEntradaQueue")
                .setBody(simple("${exchangeProperty.BodyRespuestaSearchByCustomer}"))
                .end();
    }
}
