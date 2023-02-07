package mpti.domain.trainer.api.controller;

import lombok.RequiredArgsConstructor;
import mpti.domain.trainer.api.request.StopRequest;
import mpti.domain.trainer.api.response.UserInfoResponse;
import mpti.domain.trainer.application.TrainerService;
import mpti.domain.trainer.dao.TrainerRepository;
import mpti.domain.trainer.entity.Trainer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class ServerController {
    private final TrainerService trainerService;
    @PostMapping("/admin/stop")
    public ResponseEntity setStopUntil(@RequestBody StopRequest stopRequest) {
        trainerService.setStopUntil(stopRequest);
        return ResponseEntity.ok("stop trainer success");
    }
}
