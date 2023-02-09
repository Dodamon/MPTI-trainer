package mpti.domain.trainer.api.controller;

import lombok.RequiredArgsConstructor;
import mpti.domain.trainer.api.request.ApprovedRequest;
import mpti.domain.trainer.api.request.SignUpRequest;
import mpti.domain.trainer.api.request.UpdateRequest;
import mpti.domain.trainer.api.response.UserInfoResponse;
import mpti.domain.trainer.application.FileService;
import mpti.domain.trainer.application.TrainerService;
import mpti.domain.trainer.dto.FileDto;
import mpti.domain.trainer.dto.TrainerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mpti.domain.trainer.application.S3Service;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        return ResponseEntity.ok("check success");
    }

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody SignUpRequest signupRequest) {
        trainerService.join(signupRequest);
        return ResponseEntity.ok("join success");
    }

    @GetMapping("/info/{email}")
    public ResponseEntity getTrainerInfo(@PathVariable String email) {
        TrainerDto trainerDto = trainerService.getInfo(email);
        return ResponseEntity.ok(trainerDto);
    }

    @PostMapping("/info/update/{email}")
    public ResponseEntity updateTrainerInfo(@PathVariable String email, @RequestBody UpdateRequest updateRequest) {
        TrainerDto trainerDto = trainerService.updateInfo(email, updateRequest);
        return ResponseEntity.ok(trainerDto);
    }

    @GetMapping("/info/delete/{email}")
    public ResponseEntity deleteTrainer(@PathVariable String email) {
        trainerService.deleteInfo(email);
        return ResponseEntity.ok("delete success");
    }

    @GetMapping("/list/{page}")
    public ResponseEntity getTrainerListByDate(@PathVariable int page) {
        Page<TrainerDto> pages = trainerService.getAllTrainers(page, 5, "createAt");
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/listbystar/{page}")
    public ResponseEntity getTrainerListByStar(@PathVariable int page) {
        Page<TrainerDto> pages = trainerService.getAllTrainers(page, 5, "stars");
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/application/list/{page}")
    public ResponseEntity getTrainerApplicationList(@PathVariable int page) {
        Page<TrainerDto> pages = trainerService.getAllNotApprovedTrainers(page, 4);
        return ResponseEntity.ok(pages);
    }

    @PostMapping("/application/process")
    public ResponseEntity processTrainerApplicationList(@Valid @RequestBody ApprovedRequest approvedRequest) {
        Boolean approved = approvedRequest.getApproved();
        System.out.println(approved);
        if (approved) {
            trainerService.setAprroved(approvedRequest.getEmail());
            return ResponseEntity.ok("approve success");
        } else {
            trainerService.deleteInfo(approvedRequest.getEmail());
            return ResponseEntity.ok("delete success");
        }
    }

    @GetMapping("/info/name/{id}")
    public ResponseEntity getTrainerName(@PathVariable Long id) {
        UserInfoResponse userInfoResponse = UserInfoResponse
                .builder()
                .name(trainerService.getName(id))
                .build();
        return ResponseEntity.ok(userInfoResponse);
    }


    @GetMapping("/upload")
    public String goToUpload() {
        return "upload";
    }

    @PostMapping("info/imageurl")
    @ResponseBody
    public ResponseEntity uploadFile(FileDto fileDto) throws IOException {
        String url = s3Service.uploadFile(fileDto.getFile());
        fileDto.setUrl(url);
        fileService.save(fileDto);

        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", url);

        return ResponseEntity.ok(map);
    }


}
