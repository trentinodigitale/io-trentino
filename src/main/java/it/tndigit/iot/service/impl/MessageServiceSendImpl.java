package it.tndigit.iot.service.impl;

import it.tndigit.iot.common.UtilityIot;
import it.tndigit.iot.domain.ServizioPO;
import it.tndigit.iot.domain.message.MessagePO;
import it.tndigit.iot.exception.IotException;
import it.tndigit.iot.repository.MessageRepository;
import it.tndigit.iot.repository.ServizioRepository;
import it.tndigit.iot.service.MessageServiceReceive;
import it.tndigit.iot.service.MessageServiceSend;
import it.tndigit.iot.service.dto.ServizioDTO;
import it.tndigit.iot.service.dto.message.MessageDTO;
import it.tndigit.iot.service.mapper.MessageMapper;
import it.tndigit.iot.service.mapper.ServizioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class MessageServiceSendImpl implements MessageServiceSend {
    private final Logger log = LoggerFactory.getLogger(MessageServiceSendImpl.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    protected ServizioRepository servizioRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ServizioMapper enteMapper;

    @Autowired
    private JmsTemplate jmsMessagingTemplate;


    protected Optional<ServizioDTO> getServizio(){

        String utente  = UtilityIot.getUserName();

        if (utente==null || utente.isEmpty()){
            return Optional.empty();
        }else {
            Optional<ServizioPO> entePOOptional = servizioRepository.findByEmailPec(utente);
            if(entePOOptional.isPresent()){
                return Optional.of(enteMapper.toDto(entePOOptional.get()));
            }
            return  Optional.empty();
        }
    }



    @Override
    public MessageDTO sendMessageInCode(MessageDTO messageDTO) throws IotException {
        Optional<ServizioDTO> servizioDTOOptional = getServizio();
        if (!servizioDTOOptional.isPresent()){
            throw  new IotException("Impossibile inviare il messaggio, Servizio NON presente");
        }
        messageDTO.setServizioDTO(servizioDTOOptional.get());

        //Save the new message on the database
        MessagePO messagePO = messageMapper.toEntity(messageDTO);
        messagePO = messageRepository.saveAndFlush(messagePO);
        messageDTO = messageMapper.toDto(messagePO);

        jmsMessagingTemplate.convertAndSend(messageDTO.getTipoMessage().name() + "_QUEUE", messageDTO );

        return messageDTO;
    }





    @Override
    public Optional<MessageDTO> checkMessage(Long idObj, String codiceFiscale) throws IotException {


        log.info("INIZIO elaborazione check per id " + idObj + ", codice fiscale "+ codiceFiscale);


        //Cerco il messaggio nella base dati
        Optional<MessagePO> messagePO = messageRepository.findByIdObjAndAndCodiceFiscale(idObj,codiceFiscale);

        if (messagePO.isPresent()){
            //Converto il messaggio
            MessageDTO messageDTO = messageMapper.toDto(messagePO.get());
            MessageServiceReceive messageService = (MessageServiceReceive) applicationContext.getBean(messagePO.get().getTipoMessage().getMessageService());
            messageDTO = messageService.getMessage(messageDTO);

            log.info("FINE elaborazione check per id " + idObj + ", codice fiscale "+ codiceFiscale);
            return Optional.of(messageDTO);
        }


        log.info("IMPOSSIBILE elaborare check per id " + idObj + ", codice fiscale "+ codiceFiscale);
        return Optional.empty();

    }


    @Override
    public Optional<MessageDTO> getMessage(Long idObj, String codiceFiscale) throws IotException {

        Optional<MessagePO> messagePO = messageRepository.findByIdObjAndAndCodiceFiscale(idObj,codiceFiscale);

        if (messagePO.isPresent()){
            MessageDTO messageDTO = messageMapper.toDto(messagePO.get());
            return Optional.of(messageDTO);
        }

        return Optional.empty();

    }
}

