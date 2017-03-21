package by.sleipnirim.messageServer.socket;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.service.MessageService;
import by.sleipnirim.messageServer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by sleipnir on 21.3.17.
 */
public class SocketThread extends Thread {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Autowired
    ServerSocketConnection serverSocketConnection;

    private BufferedReader bufferedReader;
    private ObjectMapper mapper;

    @Override
    public void run() {

        while (true) {
            try {
                String inputData = bufferedReader.readLine();
                if (inputData.equals("STOP")) break;
                Message message = mapper.readValue(inputData, Message.class);
                messageService.save(message);
                if(userService.findById(message.getTo().getId()).isOnline()){
                    serverSocketConnection.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

}
