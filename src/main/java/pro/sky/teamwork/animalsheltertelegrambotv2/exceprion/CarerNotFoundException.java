package pro.sky.teamwork.animalsheltertelegrambotv2.exceprion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CarerNotFoundException extends RuntimeException {
    public CarerNotFoundException(String message) {
        super(message);
    }
}