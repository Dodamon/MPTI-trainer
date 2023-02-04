package mpti.domain.trainer.application;

import lombok.RequiredArgsConstructor;
import mpti.common.exception.EmailDuplicateException;
import mpti.domain.trainer.dao.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerAuthService {
    private final TrainerRepository trainerRepository;
}