package network.chat.patient;

import jakarta.persistence.*;
import lombok.*;
import network.chat.login.Member;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "LOGIN_ID")
    private Member patient; //환자

    private String diagnosis; // 병명
    private String explanation; // 상세설명

    public PatientChart(Member member, String diagnosis, String explanation) {
        this.patient = member;
        this.diagnosis = diagnosis;
        this.explanation = explanation;
    }
}
