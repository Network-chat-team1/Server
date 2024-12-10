package network.chat.websocket;
import network.chat.doctor.entity.DoctorEntity;
import network.chat.doctor.repository.DoctorRepository;
import network.chat.notice.entity.NoticeEntity;
import network.chat.patient.entity.PatientEntity;
import network.chat.patient.repository.PatientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class ChatNotificationHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final DoctorRepository doctorRepository;
    private final Map<String, CopyOnWriteArrayList<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final Map<String, String> sessionRoles = new ConcurrentHashMap<>();
    private final PatientRepository patientRepository;

    public ChatNotificationHandler(DoctorRepository doctorRepository,PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        //방 ID 추출
        String roomId=extractRoomIdFromSession(session);
        rooms.putIfAbsent(roomId,new CopyOnWriteArrayList<>());
        rooms.get(roomId).add(session);

        sessions.add(session);

        // DB에서 이름 조회 및 설정
        String sessionId = session.getId();
        DoctorEntity doctor = doctorRepository.findBySessionId(sessionId).orElse(null);
        PatientEntity patient = patientRepository.findBySessionId(sessionId).orElse(null);
        if (doctor != null) {
            sessionRoles.put(sessionId,"doctor");
            System.out.println("의료진 연결됨: " + doctor.getName() + " (" + sessionId + ")");
        } else if (patient != null) {
            sessionRoles.put(sessionId,"paient");
            System.out.println("환자 연결됨: " + patient.getName() + " (" + sessionId + ")");
        } else {
            System.out.println("등록되지 않은 세션: " + sessionId);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        String roomId = extractRoomIdFromSession(session);


        // DB에서 이름 조회 및 설정
        DoctorEntity doctor = doctorRepository.findBySessionId(sessionId).orElse(null);
        PatientEntity patient = patientRepository.findBySessionId(sessionId).orElse(null);
        if (doctor != null) {
            sessionRoles.put(sessionId, "doctor");
            System.out.println("의료진 연결됨: " + doctor.getName() + " (" + sessionId + ")");
        } else if (patient != null) {
            sessionRoles.put(sessionId, "paient");
            System.out.println("환자 연결됨: " + patient.getName() + " (" + sessionId + ")");
        } else {
            System.out.println("등록되지 않은 세션: " + sessionId);
        }

        //유저 이름 받아오기
        String senderName;
        if (doctor != null) {
            senderName = doctor.getName(); // 의사인 경우 의사의 이름
        } else if (patient != null) {
            senderName = patient.getName(); // 환자인 경우 환자의 이름
        } else {
            senderName = "알 수 없는 사용자"; // 의사와 환자가 아닌 경우
        }


        // 이름을 포함한 메시지 생성
        String senderRole = sessionRoles.get(sessionId);
        String formattedMessage = "[" + senderRole + "]" + senderName + ": " + message.getPayload();
        System.out.println("브로드캐스트 메시지: " + formattedMessage);
        System.out.println("메시지 (방 " + roomId + "): " + formattedMessage);

        // 모든 클라이언트에게 브로드캐스트 //기존 코드
        for (WebSocketSession s : sessions) {
            try {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(formattedMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (roomId.equals("1")) {
            // 모든 클라이언트에게 브로드캐스트 //기존 코드
            for (WebSocketSession s : sessions) {
                try {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(formattedMessage));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //공지방
        if (roomId.equals("2")) {
            if ("patient".equals(senderRole)) {//역할이 doctor인 경우만 메시지 브로드캐스트
                for (WebSocketSession s : sessions) {
                    try {
                        if (s.isOpen()) {
                            s.sendMessage(new TextMessage(formattedMessage));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("권한없음");
                try {
                    session.sendMessage(new TextMessage("권한이 없음. 메시지 전송 불가"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //건의방
        if ("3".equals(roomId)) { // 3번 방 (환자와 의료진 자유 대화)
            if ("patient".equals(senderRole)) {
                // 환자가 보낸 메시지를 해당 의료진에게만 전송
                sendMessageToDoctor(formattedMessage);
            } else if ("doctor".equals(senderRole)) {
                // 의사가 보낸 메시지를 해당 환자에게만 전송
                sendMessageToPatient(formattedMessage, session);
            } else {
                try {
                    session.sendMessage(new TextMessage("권한이 없습니다. 메시지 전송 불가"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }







//        //해당 방에만 브로드캐스트
//        CopyOnWriteArrayList<WebSocketSession> roomSessions = rooms.getOrDefault(roomId, new CopyOnWriteArrayList<>());
//        for (WebSocketSession s : roomSessions) {
//            try {
//                if (s.isOpen()) {
//                    s.sendMessage(new TextMessage(formattedMessage));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }





    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("클라이언트 연결 종료: " + session.getId());
    }

    //긴급메시지?
    public void sendNotification(String notificationMessage) {
        System.out.println("알림 메시지 전송: " + notificationMessage);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(notificationMessage)); // 알림 메시지를 모든 클라이언트로 전송
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //url에서 방 id 추출
    private String extractRoomIdFromSession(WebSocketSession session) {
        String uri=session.getUri().toString();
        return uri.substring(uri.lastIndexOf("/")+1); // URL의 마지막 부분을 방 ID로 사용
    }

    // 환자가 보낸 메시지를 해당 의료진에게만 전송
    private void sendMessageToDoctor(String message) {
        for (WebSocketSession s : sessions) {
            if ("doctor".equals(sessionRoles.get(s.getId())) && s.isOpen()) {
                try {
                    // 의료진에게 메시지 전송
                    s.sendMessage(new TextMessage("[환자]: " + message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 의사가 보낸 메시지를 해당 환자에게만 전송
    private void sendMessageToPatient(String message, WebSocketSession doctorSession) {
        // 의사가 보낸 메시지를 해당 환자에게만 전송
        String doctorSessionId = doctorSession.getId(); // 의료진 세션 ID
        for (WebSocketSession s : sessions) {
            if ("patient".equals(sessionRoles.get(s.getId())) && s.isOpen()) {
                try {
                    // 환자에게 메시지 전송
                    s.sendMessage(new TextMessage("[의료진]: " + message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
