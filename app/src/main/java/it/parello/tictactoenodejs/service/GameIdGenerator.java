package it.parello.tictactoenodejs.service;
import java.security.SecureRandom;
import java.math.BigInteger;
/**
 * Created by edoar on 22/04/2017.
 */

public final class GameIdGenerator {

    public String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}