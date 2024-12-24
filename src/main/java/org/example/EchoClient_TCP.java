package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient_TCP
{
    public static final String HOST = "LocalHost";  // 서버 ip
    public static final int PORT = 5000;            // 서버 port

    public static void main(String[] args)
    {
        Socket cliSocket = null;
        InputStream is;
        OutputStream os;
        DataInputStream dis;
        DataOutputStream dos;
        Menu menu = new Menu();
        Message msg;
        byte[] header;
        byte[] body;
        byte[] packet;
        boolean isLoop = true;

        try
        {
            cliSocket = new Socket(HOST, PORT);
            System.out.println("Connection successful");

            is = cliSocket.getInputStream();
            os = cliSocket.getOutputStream();
            dis = new DataInputStream(is);
            dos = new DataOutputStream(os);

            while(isLoop)
            {
                msg = new Message();
                header = new byte[Packet.LEN_HEADER];
                dis.readFully(header);

                // Make Message Header
                Message.makeMessageHeader(msg, header);

                // Packet body received
                body = new byte[msg.getL1()+msg.getL2()+msg.getL3()+msg.getL4()+msg.getL5()];
                dis.readFully(body);

                // Make Message Body
                Message.makeMessageBody(msg, body);

                msg = menu.msgExecuteClient(msg);
                packet = Packet.makePacket(msg);

                dos.write(packet);
                dos.flush();

                if(msg.getCode() == Packet.TERMINATE)
                    isLoop = false;
            }
        }
        catch(UnknownHostException e)
        {
            System.err.println("Server not found");
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        finally
        {
            try
            {
                cliSocket.close();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }
    }
}