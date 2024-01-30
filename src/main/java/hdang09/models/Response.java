package hdang09.models;

import hdang09.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
