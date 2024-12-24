package org.example;

public class Packet
{
    // 헤더 길이
    public static final int LEN_HEADER = 14;                        // 헤더 길이
    public static final int LEN_TYPE = 1;                           // 타입 길이
    public static final int LEN_CODE = 1;                           // 코드 길이
    public static final int LEN_L1 = 4;                             // L1 길이
    public static final int LEN_L2 = 2;                             // L2 길이
    public static final int LEN_L3 = 2;                             // L3 길이
    public static final int LEN_L4 = 2;                             // L4 길이
    public static final int LEN_L5 = 2;                             // L5 길이

    // 타입
    public static final byte REQUEST = 1;                           // 요청
    public static final byte RESPONSE = 2;                          // 응답
    public static final byte SUBMIT = 3;                            // 제출
    public static final byte RESULT = 4;                            // 결과

    // 코드
    public static final byte LOGIN = 1;                             // 로그인
    public static final byte SCHEDULE = 2;                          // 선발일정
    public static final byte DELETE_SCHEDULE = 3;                   // 선발일정 삭제
    public static final byte DORMITORY_COST = 4;                    // 기숙사비
    public static final byte MEAL_COST = 5;                         // 식사비
    public static final byte TOTAL_COST = 6;                       // 기숙사비,식사비,모집인원 통합
    public static final byte APPLY = 7;                             // 입사신청
    public static final byte SELECTION = 8;                         // 선발
    public static final byte SELECTION_CHECK = 9;                  // 선발결과 확인(관리자)
    public static final byte MANUAL_ASSIGNMENT = 10;                 // 수동배정
    public static final byte AUTO_ASSIGNMENT = 11;                   // 자동배정
    public static final byte ASSIGNMENT_CHECK = 12;                 // 배정결과 확인(관리자)
    public static final byte PASS_CHECK = 13;                       // 합격 확인
    public static final byte ROOM_CHECK = 14;                       // 호실 확인
    public static final byte DORMITORY_COST_PAYMENT = 15;           // 생활관 비용
    public static final byte PAYMENT = 16;                          // 납부
    public static final byte UNPAID_USER_CHECK = 17;                // 미납부자 조회
    public static final byte CERTIFICATE = 18;                      // 결핵진단서
    public static final byte CERTIFICATE_FILE = 19;                 // 결핵진단서 파일
    public static final byte RESIGNATION_APPLY = 20;                // 퇴사신청
    public static final byte REFUND = 21;                           // 환불
    public static final byte REWARD_POINT = 22;                     // 상벌점
    public static final byte LOGOUT = 23;                           // 로그아웃
    public static final byte RECRUIT_NUMBER = 24;                   // 모집인원
    public static final byte TERMINATE = 25;                        // 통신 종료
    public static final byte SCHEDULE_CHECK = 26;                   // 선발일정학인(관리자)


    public static final byte SUCCESS = 1;


    public static byte[] shortToBytes(short data)
    {
        return new byte[] {
                (byte)((data >> 8) & 0xff),
                (byte)((data >> 0) & 0xff),
        };
    }

    public static short bytesToShort(byte[] data)
    {
        return (short)(
                (0xff & data[0]) << 8 |
                (0xff & data[1]) << 0
        );
    }

    public static byte[] intToBytes(int data)
    {
        return new byte[] {
                (byte)((data >> 24) & 0xff),
                (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff),
                (byte)((data >> 0) & 0xff),
        };
    }

    public static int bytesToInt(byte[] data)
    {
        return (int)(
                (0xff & data[0]) << 24 |
                (0xff & data[1]) << 16 |
                (0xff & data[2]) << 8  |
                (0xff & data[3]) << 0
        );
    }


    public static byte[] makePacket(Message msg)
    { // 헤더 패킷화
        byte type = msg.getType();
        byte code = msg.getCode();
        int L1 = msg.getL1();
        short L2 = msg.getL2();
        short L3 = msg.getL3();
        short L4 = msg.getL4();
        short L5 = msg.getL5();
        String data1 = msg.getData1();
        String data2 = msg.getData2();
        String data3 = msg.getData3();
        String data4 = msg.getData4();
        String data5 = msg.getData5();

        byte[] packet = new byte[LEN_HEADER + (int)L1 + (int)L2 + (int)L3 + (int)L4 + (int)L5];
        int index = 0;

        packet[index++] = type;
        packet[index++] = code;

        System.arraycopy(intToBytes(L1), 0, packet, index, LEN_L1);
        index += LEN_L1;
        System.arraycopy(shortToBytes(L2), 0, packet, index, LEN_L2);
        index += LEN_L2;
        System.arraycopy(shortToBytes(L3), 0, packet, index, LEN_L3);
        index += LEN_L3;
        System.arraycopy(shortToBytes(L4), 0, packet, index, LEN_L4);
        index += LEN_L4;
        System.arraycopy(shortToBytes(L5), 0, packet, index, LEN_L5);
        index += LEN_L5;

        // 데이터 패킷화
        byte[] dataByte;

        if(0 < L1)
        {
            dataByte = data1.getBytes();
            System.arraycopy(dataByte, 0, packet, index, dataByte.length);
            index += dataByte.length;

            if (0 < L2)
            {
                dataByte = data2.getBytes();
                System.arraycopy(dataByte, 0, packet, index, dataByte.length);
                index += dataByte.length;

                if (0 < L3)
                {
                    dataByte = data3.getBytes();
                    System.arraycopy(dataByte, 0, packet, index, dataByte.length);
                    index += dataByte.length;

                    if (0 < L4)
                    {
                        dataByte = data4.getBytes();
                        System.arraycopy(dataByte, 0, packet, index, dataByte.length);
                        index += dataByte.length;

                        if (0 < L5)
                        {
                            dataByte = data5.getBytes();
                            System.arraycopy(dataByte, 0, packet, index, dataByte.length);
                        }
                    }
                }
            }
        }
        System.out.println("통신중");
        return packet;
    }
}