package message;

import lombok.Getter;

import java.io.Serializable;

public class Message<T> implements Serializable {
    @Getter
    private final T data;

    public Message(T data) {
        this.data = data;
    }
}
