package network.chat.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    private String loginId;
    private String username;
    private String diagnosis;
    private String explanation;
}
