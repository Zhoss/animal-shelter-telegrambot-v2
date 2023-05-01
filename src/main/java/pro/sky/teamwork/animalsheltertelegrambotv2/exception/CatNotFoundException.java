package pro.sky.teamwork.animalsheltertelegrambotv2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CatNotFoundException extends RuntimeException {
    public CatNotFoundException() {
    }
}
