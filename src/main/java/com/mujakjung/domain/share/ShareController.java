package com.mujakjung.domain.share;


import com.mujakjung.domain.share.dto.HotAttractionDto;
import com.mujakjung.domain.share.dto.ShareDto;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private static final String[] ALLOWED_TYPES ={"COURSE","RESTAURANT","ACCOMMODATION"};
    private final RedisTemplate<String, Object> redisTemplate;

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
        List<HotAttractionDto> results = new ArrayList<>();
        for(String type : ALLOWED_TYPES){
            String key = "hot:"+type.toUpperCase();
            HotAttractionDto cached = (HotAttractionDto) redisTemplate.opsForValue().get(key);

            if(cached != null){
                results.add(cached);
            }else{
                // TTL이 끝났거나 캐시에 없음 → 서비스 호출 후 캐싱
                HotAttractionDto feched = shareService.findHotAttraction(type);
                redisTemplate.opsForValue().set(key,feched, Duration.ofMinutes(10));
                results.add(feched);
            }
        }
        return ResponseEntity.ok(results);
    }
}
