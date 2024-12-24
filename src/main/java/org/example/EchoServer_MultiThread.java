package org.example;

import java.io.*;
import java.net.*;

public class EchoServer_MultiThread
{
    public static final int PORT = 5000;

    public static void main(String[] args)
    {
        ServerSocket listenSocket = null;
        Socket commSocket;
        int count = 0;

        try
        {
            listenSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");
            
            while(true)
            {
                commSocket = listenSocket.accept();
                count++;

                System.out.println("Connection received from " + commSocket.getInetAddress().getHostName() + " : " + commSocket.getPort());

                ClientHandler cliHandler = new ClientHandler(commSocket, count);
                cliHandler.start();
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        finally
        {
            if(listenSocket != null)
            {
                try
                {
                    listenSocket.close();
                }
                catch(IOException e)
                {
                    System.out.println(e);
                }
            }
        }
    }
}

class ClientHandler extends Thread
{
    private Socket commSocket;
    private int clientID;
    private byte[] packet;
    private Message msg;

    ClientHandler(Socket commSocket, int clientID)
    {
        this.commSocket = commSocket;
        this.clientID = clientID;
    }

    public void run()
    {
        InputStream is;
        OutputStream os;
        DataInputStream dis;
        DataOutputStream dos;
        byte[] header;
        byte[] body;
        String studentID = null;
        String authority = "n";
        boolean isLoop = true;

        try
        {
            is = commSocket.getInputStream();
            os = commSocket.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            msg = Message.makeMessage(Packet.REQUEST, Packet.LOGIN);
            packet = Packet.makePacket(msg);

            dos.write(packet);
            dos.flush();

            while(isLoop)
            {
                msg = new Message();
                header = new byte[Packet.LEN_HEADER];
                dis.readFully(header);

                // Make Message Header
                Message.makeMessageHeader(msg, header);

                // Packet body received
                body = new byte[msg.getTotalLength()];
                dis.readFully(body);

                // Make Message Body
                Message.makeMessageBody(msg, body);

                if(msg.getCode() == Packet.TERMINATE)
                    isLoop = false;
                else
                {
                    if((msg.getCode() == Packet.LOGIN) && (msg.getType() == Packet.RESPONSE))
                        studentID = msg.getData1();

                    // 메세지 읽어서 각 기능별로 전송
                    msg = ServerApp.msgExecuteServer(msg, studentID, authority);

                    if((msg.getCode() == Packet.LOGIN) && (msg.getType() == Packet.RESULT))
                        authority = msg.getData1();
                    
                    packet = Packet.makePacket(msg);

                    dos.write(packet);
                    dos.flush();
                }
            }

            System.out.println("Client #" + clientID + ": " + "Socket closing");
            commSocket.close();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}