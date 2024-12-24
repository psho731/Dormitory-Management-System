package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Client 
{
    public static Message resignationApply(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("퇴사일(yyyy-MM-dd)");
            String date = keyInput.readLine();
            date += "T23:59:59";
            System.out.println("은행");
            String bank = keyInput.readLine();
            System.out.println("계좌번호");
            String accountNumber = keyInput.readLine();
            msg = Message.makeMessage(Packet.SUBMIT, Packet.RESIGNATION_APPLY, date.getBytes().length, bank.getBytes().length,
                    accountNumber.getBytes().length,date, bank, accountNumber);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }

    public static Message insertCertificate(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("이미지 경로 입력");
            String imagePath = keyInput.readLine();

            // 이미지 파일을 읽어서 바이트 배열로 변환
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            // 이미지를 Base64로 인코딩하여 String으로 변환
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            msg = Message.makeMessage(Packet.SUBMIT, Packet.CERTIFICATE, encodedImage.getBytes().length, encodedImage);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }

        return msg;
    }

    public static Message findStudentPoint() 
    {
        return Message.makeMessage(Packet.REQUEST, Packet.REWARD_POINT);
    }

    public static Message insertStudentPoint(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("학번 입력");
            String studentID = keyInput.readLine();
            System.out.println("점수 입력(벌점은 음수로)");
            String score = keyInput.readLine();
            System.out.println("사유 입력");
            String reason = keyInput.readLine();
            
            msg = Message.makeMessage(Packet.SUBMIT, Packet.REWARD_POINT, studentID.getBytes().length,score.getBytes().length,
                    reason.getBytes().length,studentID,score,reason);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }

    public static Message insertSchedule(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("일정명 입력");
            String name = keyInput.readLine();
            System.out.println("시작시간 입력(yyyy-MM-ddThh:mm:ss)");
            String start = keyInput.readLine();
            System.out.println("종료시간 입력(yyyy-MM-ddThh:mm:ss)");
            String end = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.SCHEDULE, name.getBytes().length, start.getBytes().length,
                    end.getBytes().length, name, start ,end);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }

    public static Message updateSchedule(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("삭제할 일정id 입력");
            String id = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.DELETE_SCHEDULE, id.getBytes().length, id);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }

    public static Message updateDormitory_cost(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("수정할 기숙사비id 입력");
            String id = keyInput.readLine();
            System.out.println("금액 입력");
            String cost = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.DORMITORY_COST, id.getBytes().length, cost.getBytes().length, id, cost);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }

    public static Message updateMeal_cost(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("수정할 식사비id 입력");
            String id = keyInput.readLine();
            System.out.println("금액 입력");
            String cost = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.MEAL_COST, id.getBytes().length,
                    cost.getBytes().length, id, cost);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }

    public static Message insertApply(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("건물 선택 : 남 {오름1동, 푸름1동, 푸름3동 } / " +
                    "여 {오름2동 , 푸름2동, 푸름4동 }");
            System.out.println("식사 선택 : 오름7일식, 오름5일식  / 푸름7일식 , 푸름5일식, 푸름0일식 ");
            System.out.println("1지망 입력");
            String apply1 = keyInput.readLine();
            System.out.println("식사 입력");
            String meal1 = keyInput.readLine();
            System.out.println("2지망 입력");
            String apply2 = keyInput.readLine();
            System.out.println("식사 입력");
            String meal2 = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.APPLY, apply1.getBytes().length,
                    meal1.getBytes().length,apply2.getBytes().length, meal2.getBytes().length, apply1, meal1, apply2 ,meal2);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }

    public static Message manualAssignment(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("교체할 학번1 입력");
            String id1 = keyInput.readLine();
            System.out.println("교체할 학번2 입력");
            String id2 = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.MANUAL_ASSIGNMENT, id1.getBytes().length,
                    id2.getBytes().length, id1, id2);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }

    public static Message updatePayment(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("은행 입력");
            String bank = keyInput.readLine();
            System.out.println("계좌번호 입력");
            String accountNum = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.PAYMENT, bank.getBytes().length,
                    accountNum.getBytes().length, bank, accountNum);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }

    public static void downloadCertificate(Message msg, BufferedReader keyInput) 
    {
        try 
        {
            // Base64로 인코딩된 이미지 문자열 (예: 위의 출력값)
            String encodedImage = msg.getData1(); // 실제 Base64 문자열로 교체

            // Base64 디코딩
            byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
            System.out.println("다운로드 받을 파일 경로 입력");
            String mypath = keyInput.readLine();
            // 이미지 파일로 저장
            mypath = mypath + "\\" + msg.getData2() + ".jpg";
            Path path = Paths.get(mypath);
            Files.write(path, decodedBytes);

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public static Message requestCertificate_file(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("학번 입력");
            String studentID = keyInput.readLine();

            msg = Message.makeMessage(Packet.REQUEST, Packet.CERTIFICATE_FILE, studentID.getBytes().length, studentID);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }

    public static Message updateRefund(Message msg, BufferedReader keyInput) 
    {
        try
        {
            System.out.println("학번 입력");
            String studentID = keyInput.readLine();

            msg = Message.makeMessage(Packet.SUBMIT, Packet.REFUND, studentID.getBytes().length, studentID);

        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        
        return msg;
    }
}