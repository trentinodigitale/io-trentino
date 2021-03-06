package it.tndigit.iot.service;

import it.tndigit.iot.domain.message.NotificationPO;
import it.tndigit.iot.exception.IotException;
import it.tndigit.iot.service.dto.message.MessageDTO;
import it.tndigit.iot.service.dto.message.NotificationDTO;

public interface MessageServiceReceive {



    /**
     *
     * Invia il messaggio
     *
     * @return MessageDTO
     * @throws IotException
     */

    MessageDTO sendMessage(MessageDTO messageDTO) throws IotException;
    MessageDTO getMessage(MessageDTO messageDTO) throws IotException;
    void receiveSendMessage(MessageDTO messageDTO) throws IotException;


}
