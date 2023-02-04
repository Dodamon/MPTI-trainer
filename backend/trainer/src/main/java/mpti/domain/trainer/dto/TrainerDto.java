package mpti.domain.trainer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.time.LocalDate;


@Getter
@Setter
@Builder
public class TrainerDto {
    @Column(nullable = false)
    private String name;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    private String imageUrl;
    private String provider;
    private String gender;
    private String phone;
    private String awards;
    private String license;
    private String career;
    private LocalDate birthday;
    private final String role = "trainer";
    private String title;
    private String s3Url;


    public void setTitle(String title) {
       if(title != null) this.title = title;
    }

    public void setS3Url(String s3Url) {
        if(s3Url != null) this.s3Url = s3Url;
    }
}
