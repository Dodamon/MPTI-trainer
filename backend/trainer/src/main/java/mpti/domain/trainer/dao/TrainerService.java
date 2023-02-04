package mpti.domain.trainer.dao;

import lombok.RequiredArgsConstructor;
import mpti.common.exception.EmailDuplicateException;
import mpti.domain.trainer.api.request.SignUpRequest;
import mpti.domain.trainer.dto.TrainerDto;
import mpti.domain.trainer.entity.Trainer;
import net.minidev.json.JSONArray;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.UnknownHostException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final PasswordEncoder passwordEncoder;

    private final TrainerRepository trainerRepository;
    @Transactional(readOnly = true)
    public void checkDuplicateEmail(String email) {
        if(trainerRepository.existsByEmail(email)) {
            throw new EmailDuplicateException(email);
        }
    }

    @Transactional
    public void join(final SignUpRequest signUpRequest) {
        if(trainerRepository.findByEmail(signUpRequest.getEmail()).orElse(null) != null) {
            throw new EmailDuplicateException(signUpRequest.getEmail());
        }

        Trainer trainer = new Trainer();
                trainer.setName(signUpRequest.getName());
                trainer.setEmail(signUpRequest.getEmail());
                trainer.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
                trainer.setBirthday(signUpRequest.getBirthday());
                trainer.setGender(signUpRequest.getGender());
                trainer.setPhone(signUpRequest.getPhone());
                trainer.setAwards(JSONArray.toJSONString(signUpRequest.getAwards()));
                trainer.setLicense(JSONArray.toJSONString(signUpRequest.getLicense()));
                trainer.setCareer(JSONArray.toJSONString(signUpRequest.getCareer()));
        trainerRepository.save(trainer);
    }

//    public TrainerDto getInfo(String email) {
//
//    }


}
