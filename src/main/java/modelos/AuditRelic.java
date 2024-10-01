package modelos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class AuditRelic {

    private String message;
    private Attributes attributes;
}
