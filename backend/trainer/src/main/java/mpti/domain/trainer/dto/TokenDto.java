package mpti.domain.trainer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String refreshToken;
    private Boolean state;
}
