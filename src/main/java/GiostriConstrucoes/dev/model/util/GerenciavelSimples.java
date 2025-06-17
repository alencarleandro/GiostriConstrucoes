package GiostriConstrucoes.dev.model.util;

import GiostriConstrucoes.dev.util.IDataUtil;
import GiostriConstrucoes.dev.util.IValidadorUtil;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@MappedSuperclass
public abstract class GerenciavelSimples implements IValidadorUtil, IDataUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
}
