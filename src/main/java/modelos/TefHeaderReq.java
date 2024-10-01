package modelos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.ToString;

@ToString
@JacksonXmlRootElement(localName = "TefHeaderReq", namespace = "http://telefonica.pe/TefRequestHeader/V1")
public class TefHeaderReq {

    @JacksonXmlProperty(localName = "userLogin", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String userLogin;

    @JacksonXmlProperty(localName = "serviceChannel", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String serviceChannel;

    @JacksonXmlProperty(localName = "application", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String application;

    @JacksonXmlProperty(localName = "idMessage", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String idMessage;

    @JacksonXmlProperty(localName = "ipAddress", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String ipAddress;

    @JacksonXmlProperty(localName = "transactionTimestamp", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String transactionTimestamp;

    @JacksonXmlProperty(localName = "serviceName", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String serviceName;

    @JacksonXmlProperty(localName = "version", namespace = "http://telefonica.pe/TefRequestHeader/V1")
    private String version = "V1"; // Valor fijo en el XML

    // Getters y Setters
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getServiceChannel() {
        return serviceChannel;
    }

    public void setServiceChannel(String serviceChannel) {
        this.serviceChannel = serviceChannel;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(String transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

