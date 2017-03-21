package by.sleipnirim.messageServer.socket;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sleipnir on 20.3.17.
 */
@Service("serverSocket")
public class ServerSocketConnection {

    private final static Log LOG = LogFactory.getLog(ServerSocketConnection.class);

    ObjectMapper mapper;

    {
        try {
            serverSocket = new ServerSocket(3000); //TODO read port from properties
        } catch (IOException e) {
            LOG.error("Cannot create socket : " + e.getMessage());

        }
    }

    private ServerSocket serverSocket;

    private Map<Long, PrintWriter> connections = new HashMap<>();

    public void connect (Long userId) {
        Socket client = null;
        long waitTime = System.nanoTime();
        while (client == null && (System.nanoTime() - waitTime) < 60 * 1000 * 1000) {
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                LOG.error("Error while accepting socket from client : " +
                        e.getMessage());
            }
        }

        if (client != null) {
            try {
                OutputStream outputStream = client.getOutputStream();
                connections.put(userId, new PrintWriter(outputStream, true));

            } catch (IOException e) {
                LOG.error("Cannot get input stream from socket : " + e.getMessage());
            }


        }
    }

    public void sendMessage(Message message) {
        PrintWriter writer = connections.get(message.getTo().getId());
        try {
            String json = mapper.writeValueAsString(message);
            writer.println(json);
        } catch (JsonProcessingException e) {
            LOG.error("Error processing message" + e.getMessage());
        }
    }

    public Map<Long, PrintWriter> getConnections() {
        return connections;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}