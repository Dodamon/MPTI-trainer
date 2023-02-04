package mpti.domain.trainer.api.controller;

import lombok.RequiredArgsConstructor;
import mpti.domain.trainer.api.request.SignUpRequest;
import mpti.domain.trainer.api.response.ApiResponse;
import mpti.domain.trainer.application.FileService;
import mpti.domain.trainer.dao.TrainerService;
import mpti.domain.trainer.dto.FileDto;
import mpti.domain.trainer.dto.TrainerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mpti.domain.trainer.application.S3Service;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final S3Service s3Service;
    private final FileService fileService;
    private final TrainerService trainerService;

    @GetMapping("/test")
    public String checkDuplicateId() {
        return "<h1>Hello Trainer Server Main Page</h1>";
    }

    @GetMapping("/duplicate/{email}")
    public ResponseEntity checkDuplicateId(@PathVariable String email) {
        trainerService.checkDuplicateEmail(email);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody SignUpRequest signupRequest) {
        trainerService.join(signupRequest);
        return ResponseEntity.ok("success");
    }

//    @GetMapping("/info/{email}")
//    public ResponseEntity getTrainerInfo(@PathVariable String email) {
//        TrainerDto trainerDto = trainerService.getInfo(email);
//        return ResponseEntity.ok(trainerDto);
//    }

//    @PostMapping("/info/update/{email}")
//    public ResponseEntity updateTrainerInfo(@PathVariable String email) {
//        TrainerDto trainerDto = trainerService.updateInfo(email);
//        return ResponseEntity.ok(trainerDto);
//    }
//
//    @GetMapping("/info/delete/{email}")
//    public ResponseEntity deleteTrainer(@PathVariable String email) {
//        TrainerDto trainerDto = trainerService.deleteInfo(email);
//        return ResponseEntity.ok("success");
//    }
//
//
//
//
//    @GetMapping("/list")
//    public ResponseEntity getTrainerList(@Valid @RequestBody SignUpRequest signupRequest) {
//        TrainerDto[] trainerDtoList =
//        return ResponseEntity.ok("");
//    }

    @GetMapping("/application/list")
    public ResponseEntity getTrainerApplicationList(@Valid @RequestBody SignUpRequest signupRequest) {
        return ResponseEntity.ok("");
    }

    @PostMapping("/application/process")
    public ResponseEntity processTrainerApplicationList(@Valid @RequestBody SignUpRequest signupRequest) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/upload")
    public String goToUpload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(FileDto fileDto) throws IOException {
        String url = s3Service.uploadFile(fileDto.getFile());
        fileDto.setUrl(url);
        fileService.save(fileDto);

        return "success";
    }


}
