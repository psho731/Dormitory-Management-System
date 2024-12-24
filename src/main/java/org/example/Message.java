package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message
{
    private byte type;
    private byte code;
    private int L1;
    private short L2;
    private short L3;
    private short L4;
    private short L5;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;

    public Message()
    {
        this.type = 0;
        this.code = 0;
        this.L1 = 0;
        this.L2 = 0;
        this.L3 = 0;
        this.L4 = 0;
        this.L5 = 0;
    }

    public int getTotalLength()
    {
        return L1 + L2 + L3 + L4 + L5;
    }

    public static Message makeMessage(byte type, byte code, int l1, int l2, int l3, int l4, int l5, String data1, String data2, String data3, String data4, String data5)
    {
        Message msg = new Message();

        msg.type = type;
        msg.code = code;
        msg.L1 = l1;
        msg.L2 = (short)l2;
        msg.L3 = (short)l3;
        msg.L4 = (short)l4;
        msg.L5 = (short)l5;

        msg.data1 = data1;
        msg.data2 = data2;
        msg.data3 = data3;
        msg.data4 = data4;
        msg.data5 = data5;

        return msg;
    }

    public static Message makeMessage(byte type, byte code, int l1, int l2, int l3, int l4, String data1, String data2, String data3, String data4)
    {
        return Message.makeMessage(type, code, l1, l2, l3, l4, 0, data1, data2, data3, data4, null);
    }

    public static Message makeMessage(byte type, byte code, int l1, int l2, int l3, String data1, String data2, String data3)
    {
        return Message.makeMessage(type, code, l1, l2, l3, 0, 0, data1, data2, data3, null, null);
    }

    public static Message makeMessage(byte type, byte code, int l1, int l2, String data1, String data2)
    {
        return Message.makeMessage(type, code, l1, l2, 0, 0, 0, data1, data2, null, null, null);
    }

    public static Message makeMessage(byte type, byte code, int l1, String data1)
    {
        return Message.makeMessage(type, code, l1, 0, 0, 0, 0, data1, null, null, null, null);
    }

    public static Message makeMessage(byte type, byte code)
    {
        return Message.makeMessage(type, code, 0, 0, 0, 0, 0, null, null, null, null, null);
    }

    public static void makeMessageHeader(Message msg, byte[] header)
    {
        int index = 0;
        byte[] bytes;

        msg.type = header[index++];
        msg.code = header[index++];

        bytes = new byte[Packet.LEN_L1];
        for(int i = 0; i < Packet.LEN_L1; i++)
            bytes[i] = header[index++];
        msg.L1 = Packet.bytesToInt(bytes);

        bytes = new byte[Packet.LEN_L2];
        for(int i = 0; i < Packet.LEN_L2; i++)
            bytes[i] = header[index++];
        msg.L2 = Packet.bytesToShort(bytes);

        bytes = new byte[Packet.LEN_L3];
        for(int i = 0; i < Packet.LEN_L3; i++)
            bytes[i] = header[index++];
        msg.L3 = Packet.bytesToShort(bytes);

        bytes = new byte[Packet.LEN_L4];
        for(int i = 0; i < Packet.LEN_L4; i++)
            bytes[i] = header[index++];
        msg.L4 = Packet.bytesToShort(bytes);

        bytes = new byte[Packet.LEN_L5];
        for(int i = 0; i < Packet.LEN_L5; i++)
            bytes[i] = header[index++];
        msg.L5 = Packet.bytesToShort(bytes);
    }

    public static void makeMessageBody(Message msg, byte[] body)
    {
        int index = 0;
        byte[] bytes;

        bytes = new byte[msg.L1];
        for(int i = 0; i < msg.L1; i++)
            bytes[i] = body[index++];
        msg.data1 = new String(bytes);

        bytes = new byte[msg.L2];
        for(int i = 0; i < msg.L2; i++)
            bytes[i] = body[index++];
        msg.data2 = new String(bytes);

        bytes = new byte[msg.L3];
        for(int i = 0; i < msg.L3; i++)
            bytes[i] = body[index++];
        msg.data3 = new String(bytes);

        bytes = new byte[msg.L4];
        for(int i = 0; i < msg.L4; i++)
            bytes[i] = body[index++];
        msg.data4 = new String(bytes);

        bytes = new byte[msg.L5];
        for(int i = 0; i < msg.L5; i++)
            bytes[i] = body[index++];
        msg.data5 = new String(bytes);
    }
}