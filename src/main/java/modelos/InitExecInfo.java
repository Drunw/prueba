package modelos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.ToString;

@ToString
@XmlRootElement(name = "initExecInfo")
public class InitExecInfo {
    private String service;
    private String operation;
    private String flowId;
    private long execStartTime;
    private String reqHeader;

    public InitExecInfo() {}

    public InitExecInfo(String service, String operation, String flowId, long execStartTime, String reqHeader) {
        this.service = service;
        this.operation = operation;
        this.flowId = flowId;
        this.execStartTime = execStartTime;
        this.reqHeader = reqHeader;
    }

    @XmlElement
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @XmlElement
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @XmlElement
    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @XmlElement
    public long getExecStartTime() {
        return execStartTime;
    }

    public void setExecStartTime(long execStartTime) {
        this.execStartTime = execStartTime;
    }

    @XmlElement
    public String getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(String reqHeader) {
        this.reqHeader = reqHeader;
    }
}
