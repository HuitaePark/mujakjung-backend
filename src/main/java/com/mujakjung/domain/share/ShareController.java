package com.mujakjung.domain.share;


import com.mujakjung.domain.share.dto.ShareDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/share")
public class ShareController {

    private final ShareService shareService;

    @PostMapping//공유 데이터 저장
    public ResponseEntity<?> saveShare(
            @RequestParam String type,
            @RequestParam Long id){
        ShareDto dto = new ShareDto(type,id);
        shareService.saveAttraction(dto);
        return ResponseEntity.status(HttpStatus.OK).body("공유데이터 저장 성공");
    }
    @GetMapping("/hot")
    public ResponseEntity<?> getPopular(){
        //레디스에 올라가 있을경우 레디스에서 가져옴
        //레디스에 없거나 올라간게 ttl되면 새로 조회
        return ResponseEntity.ok(response);
    }
}
