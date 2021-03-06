package com.ek.cab.prototype.broker.jcsmp;

import com.ek.cab.prototype.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionMessageListener implements XMLMessageListener {

    // private static final Logger log = LogManager.getLogger(TransactionMessageListener.class);

    @Override
    public void onReceive(BytesXMLMessage msg) {
        if (msg instanceof TextMessage) {
            String jsonObject = ((TextMessage) msg).getText();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Transaction transaction = mapper.readValue(jsonObject, Transaction.class);
                log.info("TextMessage received:[" + transaction.toString() + "]");
                //ACK at the end after successfully processing required logic, in case of failure don't ACK !!!
                msg.ackMessage();
            } catch (JsonProcessingException e) {
              log.error(e.getMessage());
            }

        } else {
            log.info("Message received.");
        }
        log.debug("Message Dump:%n%s%n " + msg.dump());

    }

    @Override
    public void onException(JCSMPException e) {
        log.error("### MessageListener's onException(): %s%n", e);

    }
}
