package network.chat.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)         // null 값을 가지는 필드는 Json 응답에 미포함
@AllArgsConstructor
@Getter
public class Response<T> {
    private boolean success;
    private String message;
    private T data;

    public Response(boolean success, String message) {
        this(success, message,null);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(true, message, data);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(false, message);
    }
}
