package mpti.domain.trainer.dao;

import lombok.RequiredArgsConstructor;
import mpti.common.exception.EmailDuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerAuthService {
    private final TrainerRepository trainerRepository;
}