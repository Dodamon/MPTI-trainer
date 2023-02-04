package mpti.domain.trainer.api.controller;

import lombok.RequiredArgsConstructor;
import mpti.domain.trainer.api.request.ApprovedRequest;
import mpti.domain.trainer.api.request.SignUpRequest;
import mpti.domain.trainer.api.request.UpdateRequest;
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

//    @GetMapping("/list")
//    public ResponseEntity getTrainerList(@PathVariable String email) {
//        TrainerDto[] trainerDtoList =
//        return ResponseEntity.ok("");
//    }

    @GetMapping("/application/list")
    public ResponseEntity getTrainerApplicationList(@Valid @RequestBody ApprovedRequest approvedRequest) {
        Boolean approved = approvedRequest.getApproved();
        System.out.println(approved);
        if(approved) {
            trainerService.setAprroved(approvedRequest.getEmail());
            return ResponseEntity.ok("approve success");
        } else {
            trainerService.deleteInfo(approvedRequest.getEmail());
            return ResponseEntity.ok("delete success");
        }
    }

    @PostMapping("/application/process")
    public ResponseEntity processTrainerApplicationList(@Valid @RequestBody ApprovedRequest approvedRequest) {
        Boolean approved = approvedRequest.getApproved();
        System.out.println(approved);
        if(approved) {
            trainerService.setAprroved(approvedRequest.getEmail());
            return ResponseEntity.ok("approve success");
        } else {
            trainerService.deleteInfo(approvedRequest.getEmail());
            return ResponseEntity.ok("delete success");
        }
    }

    @GetMapping("/chat/info/{id}")
    public ResponseEntity getTrainerName(@PathVariable Long id) {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("name", trainerService.getName(id));
        return ResponseEntity.ok(userInfo);
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
