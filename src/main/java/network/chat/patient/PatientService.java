package network.chat.patient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.chat.exception.CustomException;
import network.chat.exception.ErrorCode;
import network.chat.login.Member;
import network.chat.login.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PatientService {
    private final MemberRepository memberRepository;
    private final PatientChartRepository patientChartRepository;

    public List<PatientListResponseDto> getPatients() {
        List<Member> memberList = memberRepository.findAllByRole(false);
        List<PatientListResponseDto> patientList = memberList.stream()
                .map(member -> new PatientListResponseDto(member.getLoginId(), member.getUsername()))
                .collect(Collectors.toList());
        return patientList;
    }

    public PatientResponseDto getPatientInfo(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            Member memberValue = member.get(); // 유저정보
            PatientChart patientChart = patientChartRepository.findByPatient(memberValue);
            PatientResponseDto patient = new PatientResponseDto(memberValue.getLoginId(), memberValue.getUsername(), patientChart.getDiagnosis(), patientChart.getExplanation());
            return patient;
        } else {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void createChart(String loginId, ChartRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        PatientChart patientChart = new PatientChart(member, requestDto.getDiagnosis(),requestDto.getExplanation());
        patientChartRepository.save(patientChart);
    }

}
