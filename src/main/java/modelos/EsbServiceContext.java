package modelos;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "EsbServiceContext", namespace = "http://telefonica.com/peru/esb/context")
public class EsbServiceContext {

    @JacksonXmlProperty(localName = "serviceInformation", namespace = "http://telefonica.com/peru/esb/context")
    private ServiceInformation serviceInformation;

    @JacksonXmlProperty(localName = "requestHeader", namespace = "http://telefonica.com/peru/esb/context")
    private String requestHeader;

    @JacksonXmlProperty(localName = "responseHeader", namespace = "http://telefonica.com/peru/esb/context")
    private ResponseHeader responseHeader;

    // Getters y setters

    public ServiceInformation getServiceInformation() {
        return serviceInformation;
    }

    public void setServiceInformation(ServiceInformation serviceInformation) {
        this.serviceInformation = serviceInformation;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public static class ServiceInformation {
        @JacksonXmlProperty(localName = "serviceInfo", namespace = "http://telefonica.com/peru/esb/context")
        private ServiceInfo serviceInfo;

        public ServiceInfo getServiceInfo() {
            return serviceInfo;
        }

        public void setServiceInfo(ServiceInfo serviceInfo) {
            this.serviceInfo = serviceInfo;
        }

        public static class ServiceInfo {
            @JacksonXmlProperty(localName = "ServiceId", namespace = "http://telefonica.com/peru")
            private String serviceId;

            @JacksonXmlProperty(localName = "InternalId", namespace = "http://telefonica.com/peru")
            private String internalId;

            @JacksonXmlProperty(localName = "ServiceName", namespace = "http://telefonica.com/peru")
            private String serviceName;

            @JacksonXmlProperty(localName = "OperationId", namespace = "http://telefonica.com/peru")
            private String operationId;

            @JacksonXmlProperty(localName = "OperationName", namespace = "http://telefonica.com/peru")
            private String operationName;

            @JacksonXmlProperty(localName = "Version", namespace = "http://telefonica.com/peru")
            private String version;

            @JacksonXmlProperty(localName = "TraceLevel", namespace = "http://telefonica.com/peru")
            private String traceLevel;

            @JacksonXmlProperty(localName = "Status", namespace = "http://telefonica.com/peru")
            private String status;

            @JacksonXmlProperty(localName = "InputValidation", namespace = "http://telefonica.com/peru")
            private String inputValidation;

            @JacksonXmlProperty(localName = "OutputValidation", namespace = "http://telefonica.com/peru")
            private String outputValidation;

            @JacksonXmlProperty(localName = "InternalSimulator", namespace = "http://telefonica.com/peru")
            private String internalSimulator;

            @JacksonXmlProperty(localName = "ValidateExitCode", namespace = "http://telefonica.com/peru")
            private String validateExitCode;

            @JacksonXmlProperty(localName = "ExitCode", namespace = "http://telefonica.com/peru")
            private String exitCode;

            @JacksonXmlProperty(localName = "CurrentAPI", namespace = "http://telefonica.com/peru")
            private String currentAPI;

            @JacksonXmlProperty(localName = "UserReference", namespace = "http://telefonica.com/peru")
            private String userReference;

            public void setServiceId(String serviceId) {
                this.serviceId = serviceId;
            }

            public void setInternalId(String internalId) {
                this.internalId = internalId;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public void setOperationId(String operationId) {
                this.operationId = operationId;
            }

            public void setOperationName(String operationName) {
                this.operationName = operationName;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public void setTraceLevel(String traceLevel) {
                this.traceLevel = traceLevel;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setInputValidation(String inputValidation) {
                this.inputValidation = inputValidation;
            }

            public void setOutputValidation(String outputValidation) {
                this.outputValidation = outputValidation;
            }

            public void setInternalSimulator(String internalSimulator) {
                this.internalSimulator = internalSimulator;
            }

            public void setValidateExitCode(String validateExitCode) {
                this.validateExitCode = validateExitCode;
            }

            public void setExitCode(String exitCode) {
                this.exitCode = exitCode;
            }

            public void setCurrentAPI(String currentAPI) {
                this.currentAPI = currentAPI;
            }

            public void setUserReference(String userReference) {
                this.userReference = userReference;
            }
        }
    }

    public static class ResponseHeader {
        @JacksonXmlProperty(localName = "TefHeaderRes", namespace = "http://telefonica.pe/TefResponseHeader/V1")
        private TefHeaderRes tefHeaderRes;

        public TefHeaderRes getTefHeaderRes() {
            return tefHeaderRes;
        }

        public void setTefHeaderRes(TefHeaderRes tefHeaderRes) {
            this.tefHeaderRes = tefHeaderRes;
        }

        public static class TefHeaderRes {
            @JacksonXmlProperty(localName = "idMessage", namespace = "http://telefonica.pe/TefResponseHeader/V1")
            private String idMessage;

            @JacksonXmlProperty(localName = "serviceName", namespace = "http://telefonica.pe/TefResponseHeader/V1")
            private String serviceName;

            @JacksonXmlProperty(localName = "responseDateTime", namespace = "http://telefonica.pe/TefResponseHeader/V1")
            private String responseDateTime;

            public void setIdMessage(String idMessage) {                this.idMessage = idMessage;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public void setResponseDateTime(String responseDateTime) {
                this.responseDateTime = responseDateTime;
            }

            public void setTransactionID(String transactionID) {
                this.transactionID = transactionID;
            }

            @JacksonXmlProperty(localName = "transactionID", namespace = "http://telefonica.pe/TefResponseHeader/V1")
            private String transactionID;

            // Getters y setters

            // ... (agrega los getters y setters para cada campo aquí)
        }
    }
}
