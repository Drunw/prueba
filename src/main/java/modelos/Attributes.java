package modelos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Data
@Builder
@RegisterForReflection
public class Attributes {
    Map<String, String> tel;
    private String level;
    private String serviceName;
    private String hostname;
    private String environment;
    private String parentId;
    private String operationName;
    private String catalogId;
}
