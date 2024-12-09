package network.chat.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import network.chat.login.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChartRequestDto {
    private String diagnosis; // 병명
    private String explanation; // 상세설명
}
