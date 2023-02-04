package mpti.domain.trainer.application;

import lombok.RequiredArgsConstructor;
import mpti.common.exception.EmailDuplicateException;
import mpti.common.exception.ResourceNotFoundException;
import mpti.domain.trainer.api.request.SignUpRequest;
import mpti.domain.trainer.api.request.UpdateRequest;
import mpti.domain.trainer.dao.TrainerRepository;
import mpti.domain.trainer.dto.TrainerDto;
import mpti.domain.trainer.entity.Trainer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                trainer.setAwards(signUpRequest.getAwards());
                trainer.setLicense(signUpRequest.getLicense());
                trainer.setCareer(signUpRequest.getCareer());
        trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public TrainerDto getInfo(String email) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email", email)
                );

        TrainerDto trainerDto = TrainerDto.builder()
                .name(trainer.getName())
                .email(trainer.getEmail())
                .birthday(trainer.getBirthday())
                .gender(trainer.getGender())
                .phone(trainer.getPhone())
                .awards(trainer.getAwards())
                .license(trainer.getLicense())
                .career(trainer.getCareer())
                .imageUrl(trainer.getImageUrl())
                .build();
        return trainerDto;
    }

    @Transactional
    public TrainerDto updateInfo(String email, UpdateRequest updateRequest) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email", email)
                );

        String name = updateRequest.getName();
        String phone = updateRequest.getPhone();
        String gender = updateRequest.getGender();
        String imageUrl = updateRequest.getImageUrl();

        if(name != null) trainer.setName(name);
        if(phone != null) trainer.setPhone(phone);
        if(gender != null) trainer.setGender(gender);
        if(imageUrl != null) trainer.setImageUrl(imageUrl);

        return getInfo(email);
    }

    @Transactional
    public void deleteInfo(String email) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email", email)
                );

        trainerRepository.delete(trainer);
    }

    @Transactional(readOnly = true)
    public String getName(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Id", id)
                );
        return trainer.getName();
    }

    @Transactional
    public void setAprroved(String email) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email", email)
                );
        trainer.setApproved(true);
    }

    public Slice<TrainerDto> getAllTrainers(int page, int size, String orderType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, orderType));
        Slice<Trainer> slice = trainerRepository.findSliceByApproved(true, pageRequest);
        Slice<TrainerDto> toMap = slice.map(m
                -> TrainerDto.builder()
                .name(m.getName())
                .email(m.getEmail())
                .birthday(m.getBirthday())
                .gender(m.getGender())
                .phone(m.getPhone())
                .awards(m.getAwards())
                .license(m.getLicense())
                .career(m.getCareer())
                .imageUrl(m.getImageUrl())
                .stars(m.getStars())
                .createAt(m.getCreateAt())
                .build()
        );

        return toMap;
    }

    public Slice<TrainerDto> getAllNotApprovedTrainers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createAt"));
        Slice<Trainer> slice = trainerRepository.findSliceByApproved(false, pageRequest);
        Slice<TrainerDto> toMap = slice.map(m
                -> TrainerDto.builder()
                .name(m.getName())
                .email(m.getEmail())
                .birthday(m.getBirthday())
                .gender(m.getGender())
                .phone(m.getPhone())
                .awards(m.getAwards())
                .license(m.getLicense())
                .career(m.getCareer())
                .imageUrl(m.getImageUrl())
                .stars(m.getStars())
                .createAt(m.getCreateAt())
                .build()
        );
        return toMap;
    }
}
