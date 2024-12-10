package network.chat.notice.controller;

import network.chat.notice.entity.NoticeEntity;
import network.chat.notice.repository.NoticeRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeRepository repo;

    public NoticeController(NoticeRepository repo) {this.repo = repo;}

    @PostMapping
    public NoticeEntity sendNotice(@RequestBody NoticeEntity notice){
        notice.setTimeStamp(LocalDateTime.now());
        return repo.save(notice);
    }

    @GetMapping
    public List<NoticeEntity> getAllNotices(){return repo.findAll();}
}
