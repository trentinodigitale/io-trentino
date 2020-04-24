package it.tndigit.iot.service.dto.message;

import it.tndigit.iot.service.dto.CommonDTO;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Data
public class PaymentDTO extends CommonDTO {

    private static final long serialVersionUID = 8754912410969106985L;

    private Integer importo;
    private String numeroAvviso;
    private Boolean invalid_after_due_date;


}
