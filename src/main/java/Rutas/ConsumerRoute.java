package Rutas;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

@RegisterForReflection
@ApplicationScoped
public class ConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .bindingMode(RestBindingMode.off);


        rest("/OpenId-RetrieveUserProfile/OpenId-RetrieveUserProfile/ProxyServices")
                .post("/OpenId-RetrieveUserProfileSOAP_v1").type(String.class)
                .enableCORS(true)
                .consumes("application/xml")
                .produces("application/xml")
                .to("direct:expInitServiceParam");

    }
}