package sk.fejero.emconnect.messages;

import java.util.Date;

/**
 * Created by fejero on 11.11.2014.
 */
public class ConceptMessage extends Message {

    public ConceptMessage(Date date, String address, String subject, String cc, String content) {
        super(date, address, subject, cc, content);
    }
}