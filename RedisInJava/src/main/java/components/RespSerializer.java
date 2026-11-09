package components;

import org.springframework.stereotype.Component;

@Component
public class RespSerializer {
    public void printWorking() {
        System.out.println("!------- Started -------!");
    }

    public String returnString() {
        return "hi";
    }
}
