package network.chat.suggestion.repository;


import network.chat.suggestion.entity.SuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepo extends JpaRepository<SuggestionEntity, Long> {
}
