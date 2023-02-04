package mpti.domain.trainer.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@Table(name = "trainer")
public class Trainer {

//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Column(name = "trainer_id", columnDefinition = "BINARY(16)")
//    private UUID id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    private String imageUrl;
    @Column(nullable = false)
    private Boolean emailVerified = false;
    private String password;
    private String provider;
    private String providerId;
    private String gender;
    private String phone;
    private String awards;
    private String license;
    private String career;
    private boolean approved;
    private LocalDate birthday;

    private String title;
    public Trainer() {
    }

    public void setTitle(String title) {
        if(title != null) this.title = title;
    }


    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
