package cc.kocho.humancannonandroid;

import cc.kocho.humancannonandroid.config.Config;
import cc.kocho.humancannonandroid.util.HCRandom;
import cc.kocho.humancannonandroid.util.HttpRequest;
import okhttp3.*;

import java.io.IOException;

public class Cannon {

    public static Cannonball fire(String qName){
        Cannonball cannonball = new Cannonball();
        cannonball.Q = qName;
        cannonball.name = qName + HCRandom.rString(10);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                Config.requestMessage.replace("(username)", cannonball.name));

        try {
            if (HttpRequest.post(Config.serveUrl + "/hk4e_global/mdk/shield/api/login", body).contains("\"message\":\"OK\"")){
                cannonball.result = "OK";
            }else {
                cannonball.result = "NO";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cannonball;
    }

}
