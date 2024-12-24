package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu
{
    private String authority;

    public Message msgExecuteClient(Message msg)
    {
        byte type = msg.getType();
        byte code = msg.getCode();
        BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));

        switch(code)
        {
            case Packet.LOGIN:
                if(type == Packet.REQUEST)
                {
                    try
                    {
                        System.out.println("id (q 입력시 종료)");
                        String id = keyInput.readLine();

                        if(id.equals("q"))
                        {
                            msg = Message.makeMessage(Packet.RESPONSE, Packet.TERMINATE);
                            break;
                        }

                        System.out.println("pw (q 입력시 종료)");
                        String pw = keyInput.readLine();

                        if(pw.equals("q"))
                        {
                            msg = Message.makeMessage(Packet.RESPONSE, Packet.TERMINATE);
                            break;
                        }

                        msg = Message.makeMessage(Packet.RESPONSE, Packet.LOGIN,id.getBytes().length,
                                pw.getBytes().length, id, pw);
                    }
                    catch(IOException e)
                    {
                        System.err.println(e);
                    }

                    break;
                }
                else if(type == Packet.RESULT)
                {
                    String result = msg.getData1();

                    if(result.equals("s"))
                    {
                        authority = "s";
                        msg = studentMenu(msg, keyInput); // 사용자에게 메뉴 보여주고 입력받아서 기능 실행
                    }
                    else if(result.equals("a"))
                    {
                        authority = "a";
                        msg = adminMenu(msg, keyInput);
                    }
                    else
                    {
                        System.out.println("로그인 실패");
                        try
                        {
                            System.out.println("id");
                            String id = keyInput.readLine();
                            System.out.println("pw");
                            String pw = keyInput.readLine();
                            msg = Message.makeMessage(Packet.RESPONSE, Packet.LOGIN,id.getBytes().length,
                                    pw.getBytes().length, id, pw);
                        }
                        catch(IOException e)
                        {
                            System.err.println(e);
                        }
                    }
                }
                break;

            case Packet.SCHEDULE:
                if(type == Packet.RESPONSE)
                {
                    if(authority.equals("s"))
                    {
                        System.out.println(msg.getData1()); // 선발일정 정보 출력
                        msg = studentMenu(msg, keyInput); // 학생 기능 선택
                    }
                    else if(authority.equals("a"))
                        msg = Client.insertSchedule(msg, keyInput); // 선발일정등록 제출
                }
                else if(type == Packet.RESULT)
                { // 일정 등록에 대한 결과
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.DELETE_SCHEDULE:
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1()); // 선발일정 정보 출력
                    msg = Client.updateSchedule(msg, keyInput); // 선발 일정 수정 제출
                }
                else if(type == Packet.RESULT)
                {
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.SCHEDULE_CHECK:
                if(type == Packet.RESPONSE)
                { // 선발일정 정보 응답
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput); // 추가 동작 없이 다음 관리자 기능 선택
                }
                break;

            case Packet.DORMITORY_COST:
                if(type == Packet.RESPONSE)
                { // 응답 정보 출력
                    System.out.println(msg.getData1());
                    msg = Client.updateDormitory_cost(msg, keyInput); // 식사비 수정 제출
                }
                else if(type == Packet.RESULT)
                {
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput); // 관리자 기능 선택
                }
                break;

            case Packet.MEAL_COST:
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1()); // 응답 정보 출력
                    msg = Client.updateMeal_cost(msg, keyInput); // 식사비 수정 제출
                }
                else if(type == Packet.RESULT){
                    System.out.println(msg.getData1()); // 식사비 수정 제출 결과
                    msg = adminMenu(msg, keyInput); // 관리자 기능 선택
                }
                break;

            case Packet.TOTAL_COST:
                if(type == Packet.RESPONSE)
                { // 기숙사비, 식사비, 모집인원 등 총 정보 출력
                    if(authority.equals("s"))
                    {
                        System.out.println(msg.getData1());
                        msg = studentMenu(msg, keyInput); // 학생 기능 선택
                    }
                    else if(authority.equals("a"))
                    {
                        System.out.println(msg.getData1());
                        msg = adminMenu(msg, keyInput); // 관리자 기능 선택
                    }
                }
                break;

            case Packet.APPLY:
                if(type == Packet.RESPONSE)
                {
                    if(authority.equals("s"))
                        msg = Client.insertApply(msg, keyInput); // 입사 신청 제출
                    else if(authority.equals("a"))
                    {
                        System.out.println(msg.getData1());
                        msg = adminMenu(msg, keyInput); // 관리자는 입사신청자 조회 응답 출력하고 끝
                    }
                }
                else if(type == Packet.RESULT)
                { // 학생 입사신청 제출 성공여부 출력
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                break;

            case Packet.SELECTION:
                if(type == Packet.RESPONSE)
                { // 선발 결과 출력
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.MANUAL_ASSIGNMENT:
                if(type == Packet.RESPONSE)
                { // 현재 배정 정보 출력
                    System.out.println(msg.getData1());
                    msg = Client.manualAssignment(msg, keyInput);
                }
                else if(type == Packet.RESULT)
                {
                    try
                    {
                        System.out.println(msg.getData1()); // 배정결과 출력
                        System.out.println("q : 배정 종료 | 계속 진행시 아무 키 입력");
                        String command = keyInput.readLine();
                        if(command.equals("q"))
                            msg = adminMenu(msg, keyInput); // 배정 종료 다음 기능 선택
                        else
                            msg = Message.makeMessage(Packet.REQUEST, Packet.MANUAL_ASSIGNMENT); // 관리자 메뉴에 있는 수동배정요청이랑 동일
                        // 수동배정 다시 시작
                    }
                    catch(IOException e)
                    {
                        System.err.println(e);
                    }
                }
                break;

            case Packet.SELECTION_CHECK: // 관리자 전체 선발결과 확인
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.ASSIGNMENT_CHECK: // 관리자 전체 배정결과 확인
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.PASS_CHECK: // 학생 본인 선발결과 확인
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                break;

            case Packet.ROOM_CHECK: // 학생 본인 배정 방 확인
                if(type == Packet.RESPONSE)
                {
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg,keyInput);
                }
                break;

            case Packet.DORMITORY_COST_PAYMENT:
                if(type == Packet.RESPONSE)
                {
                    try
                    {
                        System.out.println(msg.getData1()); // 납부액 출력
                        System.out.println("q : 미납부 / 납부 : 납부"); // 납부여부 선택
                        String command = keyInput.readLine();
                        if(command.equals("q"))
                            msg = studentMenu(msg, keyInput); // 납부 X 다음 기능 선택
                        else if(command.equals("납부"))
                            msg = Client.updatePayment(msg, keyInput); // 납부
                    }
                    catch(IOException e)
                    {
                        System.err.println(e);
                    }
                }
                break;

            case Packet.PAYMENT:
                if(type == Packet.RESPONSE)
                { // 납부자 조회 응답
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                else if(type == Packet.RESULT)
                { // 학생 납부 결과
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                break;

            case Packet.UNPAID_USER_CHECK:
                if(type == Packet.RESPONSE)
                { // 미납부자 조회 요청 응답
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.CERTIFICATE:
                if(type == Packet.RESPONSE)
                { // 결핵진단서 조회 요청애 대한 응답
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                else if(type == Packet.RESULT)
                { // 결핵진단서 제출에 대한 결과
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                break;

            case Packet.CERTIFICATE_FILE:
                if(type == Packet.RESPONSE)
                { // 파일 다운로드
                    Client.downloadCertificate(msg, keyInput);
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.RESIGNATION_APPLY:
                if(type == Packet.RESPONSE)
                { // 퇴사자 조회 요청에 대한 응답
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                else if(type == Packet.RESULT)
                { // 퇴사 신청 결과
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                break;

            case Packet.REFUND:
                if(type == Packet.RESPONSE)
                { // 횐불 확인 요청 응답
                    System.out.println(msg.getData1());
                    msg = studentMenu(msg, keyInput);
                }
                else if(type == Packet.RESULT)
                { // 환불 제출 결과
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;

            case Packet.REWARD_POINT:
                if(type == Packet.RESPONSE)
                {
                    if(authority.equals("s"))
                    {
                        System.out.println(msg.getData1());
                        msg = studentMenu(msg, keyInput);
                    }
                    else if(authority.equals("a")){
                        System.out.println(msg.getData1());
                        msg = adminMenu(msg, keyInput);
                    }
                }
                else if(type == Packet.RESULT)
                { // 상벌점 제출 결과
                    System.out.println(msg.getData1());
                    msg = adminMenu(msg, keyInput);
                }
                break;
        }
        return msg;
    }


    public Message studentMenu(Message msg, BufferedReader keyInput)
    {
        String command;
        boolean isOn = true;

        try
        {
            while (isOn)
            {
                System.out.println("1.선발 일정 및 비용 확인");
                System.out.println("2.입사 신청");
                System.out.println("3.합격 여부 및 호실 확인");
                System.out.println("4.생활관 비용 확인 및 납부");
                System.out.println("5.결핵진단서 제출");
                System.out.println("6.퇴사 신청");
                System.out.println("7.환불 확인");
                System.out.println("8.상벌점 확인");
                System.out.println("9.로그아웃");
                System.out.println("10.프로그램 종료");
                command = keyInput.readLine();

                switch(command)
                {
                    case "1":
                        while(true)
                        {
                            System.out.println("1.선발 일정 ");
                            System.out.println("2.기숙사비 및 식사비 확인");
                            System.out.println("3.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.SCHEDULE);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.TOTAL_COST);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "2":
                        while(true)
                        {
                            System.out.println("1.입사 신청 ");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.APPLY);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "3":
                        while(true)
                        {
                            System.out.println("1.합격 여부 확인");
                            System.out.println("2.호실 확인");
                            System.out.println("3.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.PASS_CHECK);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.ROOM_CHECK);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "4":
                        while(true)
                        {
                            System.out.println("1.생활관 비용 확인");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.DORMITORY_COST_PAYMENT);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "5":
                        while(true)
                        {
                            System.out.println("1.결핵 진단서");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Client.insertCertificate(msg, keyInput);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "6":
                        while(true)
                        {
                            System.out.println("1.퇴사 신청");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Client.resignationApply(msg, keyInput);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "7":
                        while(true)
                        {
                            System.out.println("1.환불 확인 ");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.REFUND);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "8":
                        while(true)
                        {
                            System.out.println("1.상벌점 확인 ");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.REWARD_POINT);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "9":
                        msg = Message.makeMessage(Packet.REQUEST, Packet.LOGOUT);
                        isOn = false;
                        break;

                    case "10":
                        msg = Message.makeMessage(Packet.REQUEST, Packet.TERMINATE);
                        isOn =false;
                        break;

                    default:
                        System.out.println("입력 오류");
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }


    public Message adminMenu(Message msg, BufferedReader keyInput)
    {
        String command;
        boolean isOn = true;

        try
        {
            while (isOn)
            {
                System.out.println("1.선발 일정");
                System.out.println("2.기숙사비 및 식사비");
                System.out.println("3.신청자 조회");
                System.out.println("4.입사자 선발 및 호실 배정");
                System.out.println("5.생활관 비용 납부자 조회");
                System.out.println("6.생활관 비용 미납부자 조회");
                System.out.println("7.결핵진단서");
                System.out.println("8.퇴사신청자 조회 및 환불");
                System.out.println("9.상벌점");
                System.out.println("10.로그아웃");
                System.out.println("11.프로그램 종료");
                command = keyInput.readLine();

                switch(command)
                {
                    case "1":
                        while(true)
                        {
                            System.out.println("1.선발 일정 등록");
                            System.out.println("2.선발 일정 삭제");
                            System.out.println("3.선발 일정 확인");
                            System.out.println("4.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.SCHEDULE);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.DELETE_SCHEDULE);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.SCHEDULE_CHECK);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("4"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "2":
                        while(true)
                        {
                            System.out.println("1.기숙사비 등록");
                            System.out.println("2.급식비 등록");
                            System.out.println("3.비용 정보 확인");
                            System.out.println("4.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.DORMITORY_COST);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.MEAL_COST);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.TOTAL_COST);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("4"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "3":
                        while(true)
                        {
                            System.out.println("1.신청자 조회");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.APPLY);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "4":
                        while(true)
                        {
                            System.out.println("1.선발 및 배정");
                            System.out.println("2.수동 배정");
                            System.out.println("3.선발 결과 확인");
                            System.out.println("4.배정 결과 확인");
                            System.out.println("5.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.SELECTION);
                                isOn = false;
                                break;
                            }
                            if(command.equals("2"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.MANUAL_ASSIGNMENT);
                                isOn = false;
                                break;
                            }
                            if(command.equals("3"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.SELECTION_CHECK);
                                isOn = false;
                                break;
                            }
                            if(command.equals("4"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.ASSIGNMENT_CHECK);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("5"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "5":
                        while(true)
                        {
                            System.out.println("1.생활관 비용 납부자 조회 ");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.PAYMENT);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "6":
                        while(true)
                        {
                            System.out.println("1.생활관 비용 미납부자 조회 ");
                            System.out.println("2.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.UNPAID_USER_CHECK);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("2"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "7":
                        while(true)
                        {
                            System.out.println("1.결핵진단서 제출자 조회 ");
                            System.out.println("2.결핵진단서 다운로드");
                            System.out.println("3.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.CERTIFICATE);
                                isOn = false;
                                break;
                            }
                            if(command.equals("2"))
                            {
                                msg = Client.requestCertificate_file(msg, keyInput);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "8":
                        while(true)
                        {
                            System.out.println("1.퇴사신청자 조회");
                            System.out.println("2.환불");
                            System.out.println("3.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Message.makeMessage(Packet.REQUEST, Packet.RESIGNATION_APPLY);
                                isOn = false;
                                break;
                            }
                            if(command.equals("2"))
                            {
                                msg = Client.updateRefund(msg, keyInput);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "9":
                        while(true)
                        {
                            System.out.println("1.상벌점 조회");
                            System.out.println("2.상벌점 부여");
                            System.out.println("3.이전 메뉴");
                            command = keyInput.readLine();

                            if(command.equals("1"))
                            {
                                msg = Client.findStudentPoint();
                                isOn = false;
                                break;
                            }
                            if(command.equals("2"))
                            {
                                msg = Client.insertStudentPoint(msg, keyInput);
                                isOn = false;
                                break;
                            }
                            else if(command.equals("3"))
                                break;
                            else
                                System.out.println("입력 오류");
                        }
                        break;

                    case "10":
                        msg = Message.makeMessage(Packet.REQUEST, Packet.LOGOUT);
                        isOn = false;
                        break;

                    case "11":
                        msg = Message.makeMessage(Packet.REQUEST, Packet.TERMINATE);
                        isOn =false;
                        break;

                    default:
                        System.out.println("입력 오류");
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return msg;
    }
}