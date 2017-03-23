package by.sleipnirim.messageServer.socket;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;
import by.sleipnirim.messageServer.service.MessageService;
import by.sleipnirim.messageServer.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sleipnir on 20.3.17.
 */
@Service("serverSocket")
@Scope(value = "singleton")
public class ServerSocketConnection {

    private final static Log LOG = LogFactory.getLog(ServerSocketConnection.class);

    ObjectMapper mapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private ServerSocket serverSocket;

    private Map<Long, PrintWriter> connections = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            serverSocket = new ServerSocket(3000); //TODO read port from properties
        } catch (IOException e) {
            LOG.error("Cannot create socket : " + e.getMessage());
        }
    }

    public void connect(Long userId) throws ServerSocketConnectionException {
        Socket client = null;
        long waitTime = System.nanoTime();
        while (client == null && (System.nanoTime() - waitTime) < 60 * 1000 * 1000) {
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                LOG.error("Error while accepting socket from client : " +
                        e.getMessage());
                throw new ServerSocketConnectionException();
            }
        }

        if (client != null) {
            try {
                OutputStream outputStream = client.getOutputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                connections.put(userId, new PrintWriter(outputStream, true));
                new SocketThread(userId, bufferedReader).start();

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


    class SocketThread extends Thread {

        private BufferedReader bufferedReader;
        private Long clientId;
        private int outPing;
        private int inPing;
        private volatile boolean accessible = true;

        public SocketThread(Long clientId, BufferedReader bufferedReader) {
            this.clientId = clientId;
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {

            Timer pingTaskExecution = new Timer();
            TimerTask pingTask = new PingTask(clientId);
            pingTaskExecution.schedule(pingTask, 0, 60 * 1000);

            while (accessible) {
                try {
                    String inputData = bufferedReader.readLine();
                    if (inputData.equals("STOP")) {
                        break;
                    }
                    if (inputData.equals("PING")) inPing++;
                    Message message = mapper.readValue(inputData, Message.class);
                    messageService.save(message);
                    if (userService.findById(message.getTo().getId()).isOnline()) {
                        sendMessage(message);
                    }
                } catch (IOException e) {
                    LOG.error("Error while reading data from client socket : " + e.getMessage());
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        LOG.error("Trying to close bufferder reader from client" +
                                "socket, but get an exception" + e.getMessage());
                    }
                }
            }
            LOG.debug("User disconnected");
            connections.remove(clientId);
        }

        class PingTask extends TimerTask {

            private Long clientId;

            public PingTask(Long clientId) {
                this.clientId = clientId;
            }

            @Override
            public void run() {
                if (outPing > inPing) {
                    accessible = false;
                    return;
                }
                PrintWriter printWriter = connections.get(clientId);
                printWriter.println("PING");
                outPing++;
            }
        }
    }

}