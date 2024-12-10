package network.chat.suggestion.controller;

import network.chat.suggestion.entity.SuggestionEntity;
import network.chat.suggestion.repository.SuggestionRepo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController {
    private final SuggestionRepo suggestionRepo;

    public SuggestionController(SuggestionRepo suggestionRepo) {this.suggestionRepo = suggestionRepo;}

    @PostMapping
    public SuggestionEntity addSuggestion(@RequestBody SuggestionEntity suggestion) {
        suggestion.setTimestamp(LocalDateTime.now());
        return suggestionRepo.save(suggestion);
    }

    @GetMapping
    public List<SuggestionEntity> getAllSuggestions() {return suggestionRepo.findAll();}
}
