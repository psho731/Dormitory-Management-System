package org.example;

import persistence.dao.Login_DAO;
import service.Login_Service;

public class ServerApp
{
    public static Message msgExecuteServer(Message msg, String studentID, String authority)
    {
        byte type = msg.getType();
        byte code = msg.getCode();

        switch(code)
        {
            case Packet.LOGIN:
                if(type == Packet.RESPONSE)
                {
                    Login_DAO dao = new Login_DAO();
                    Login_Service login = new Login_Service(dao);
                    String result = login.accountPrint(msg.getData1(), msg.getData2()); //권환 반환 로그인 실패시 n반환//result = "s";
                    msg = Message.makeMessage(Packet.RESULT, Packet.LOGIN, result.getBytes().length, result);
                }
                break;

            case Packet.LOGOUT:
                if(type == Packet.REQUEST)
                    msg = Message.makeMessage(Packet.REQUEST, Packet.LOGIN);
                break;

            case Packet.SCHEDULE:
                if(type == Packet.REQUEST)
                {
                    String result = ServerFunctions.findAllSchedules();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.SCHEDULE, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    String name = msg.getData1(), startDate = msg.getData2(), endDate = msg.getData3();
                    ServerFunctions.insertSchedule(name, startDate, endDate);
                    String result = "등록 완료";
                    msg = Message.makeMessage(Packet.RESULT, Packet.SCHEDULE, result.getBytes().length, result);
                }
                break;

            case Packet.DELETE_SCHEDULE:
                if(type == Packet.REQUEST)
                {
                    String result = ServerFunctions.findAllSchedules();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.DELETE_SCHEDULE,
                            result.getBytes().length, result);
                }
                if(type == Packet.SUBMIT)
                {
                    String id = msg.getData1();
                    ServerFunctions.deleteSchedule(id);
                    String result = "수정 완료";
                    msg =  Message.makeMessage(Packet.RESULT, Packet.DELETE_SCHEDULE, result.getBytes().length, result);
                }
                break;

            case Packet.SCHEDULE_CHECK:
                if(type == Packet.REQUEST)
                {
                    String result = ServerFunctions.findAllSchedules();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.SCHEDULE_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.DORMITORY_COST:
                if(type == Packet.REQUEST)
                {
                    //server.관리자기숙사비응답(msg, is, os);
                    String result = ServerFunctions.findAllDormitoryCost();
                    msg =  Message.makeMessage(Packet.RESPONSE, Packet.DORMITORY_COST, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    //msg = server.기숙사비등록(msg, is, os);
                    ServerFunctions.updateDormitoryCost(Integer.parseInt(msg.getData1()), Integer.parseInt(msg.getData2()));
                    String result = "수정 완료";
                    msg = Message.makeMessage(Packet.RESULT, Packet.DORMITORY_COST,result.getBytes().length, result);
                }
                break;

            case Packet.MEAL_COST:
                if(type == Packet.REQUEST)
                {
                    //msg = server.관리자식사비응답(msg, is, os);
                    String result = ServerFunctions.findAllMealCost();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.MEAL_COST, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    //msg = server.식사비등록(msg, is, os);
                    ServerFunctions.updateMealCost(Integer.parseInt(msg.getData1()), Integer.parseInt(msg.getData2()));
                    String result = "등록 완료";
                    msg = Message.makeMessage(Packet.RESULT, Packet.MEAL_COST,result.getBytes().length, result);
                }
                break;

            case Packet.TOTAL_COST:
                if(type == Packet.REQUEST)
                {
                    //msg = server.통합정보응답(msg, is, os);
                    String result = ServerFunctions.findTotalCost();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.TOTAL_COST,result.getBytes().length, result);
                }
                break;

            case Packet.APPLY:
                if(type == Packet.REQUEST)
                {
                    if(authority.equals("s")) // 신청정보응답
                        msg = Message.makeMessage(Packet.RESPONSE, Packet.APPLY);
                    else if(authority.equals("a"))
                    {
                        // 신청자조회응답
                        String result = ServerFunctions.findApplyStudents();
                        msg = Message.makeMessage(Packet.RESPONSE, Packet.APPLY,result.getBytes().length, result);
                    }
                }
                else if(type == Packet.SUBMIT)
                {
                    if(!(ServerFunctions.checkDate(2) || ServerFunctions.checkDate(7)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESULT, Packet.APPLY, result.getBytes().length, result);
                        break;
                    }
                    //msg = server.입사신청등록(msg);
                    String result = ServerFunctions.dormitoryApply(studentID, msg.getData1(), msg.getData2(), msg.getData3(), msg.getData4());
                    msg = Message.makeMessage(Packet.RESULT, Packet.APPLY, result.getBytes().length, result);
                }
                break;

            case Packet.SELECTION:
                if(type == Packet.REQUEST)
                {
                    //msg = server.자동선발(msg);
                    if((ServerFunctions.checkDate(2)||ServerFunctions.checkDate(7)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESPONSE, Packet.SELECTION, result.getBytes().length, result);
                        break;
                    }
                    final int totalDormitoryNum = 6;
                    ServerFunctions.executeSelection(totalDormitoryNum);
                    String result = "선발 완료";
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.SELECTION, result.getBytes().length, result);
                }
                break;

            case Packet.MANUAL_ASSIGNMENT:
                if(type == Packet.REQUEST)
                {
                    //msg = server.수동배정요청_응답(msg);
                    String result = ServerFunctions.info_Manual_assignment();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.MANUAL_ASSIGNMENT, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                    msg = ServerFunctions.executeManualAssignment(msg); // 수동배정 실행
                break;

            case Packet.SELECTION_CHECK:
                if(type == Packet.REQUEST)
                {
                    //msg = server.선발결과확인(관리자)_응답(msg);
                    String result = ServerFunctions.findAllSelection();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.SELECTION_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.ASSIGNMENT_CHECK:
                if(type == Packet.REQUEST)
                {
                    //msg = server.배정결과확인(관리자)_응답(msg);
                    String result = ServerFunctions.findAllAssignment();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.ASSIGNMENT_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.PASS_CHECK:
                if(type == Packet.REQUEST)
                {
                    if(!(ServerFunctions.checkDate(3)||ServerFunctions.checkDate(8)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESPONSE, Packet.PASS_CHECK, result.getBytes().length, result);
                        break;
                    }
                    //msg = server.합격확인요청_응답(msg);//패킷 보내기까지
                    String result = ServerFunctions.ApplyPassCheck(studentID);
                    msg =  Message.makeMessage(Packet.RESPONSE, Packet.PASS_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.ROOM_CHECK:
                if(type == Packet.REQUEST)
                {
                    if(!(ServerFunctions.checkDate(3)||ServerFunctions.checkDate(7)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESPONSE, Packet.ROOM_CHECK, result.getBytes().length, result);
                        break;
                    }
                    //msg = server.호실확인요청_응답(msg, is, os);//패킷 보내기까지
                    String result = ServerFunctions.Room_check(studentID);
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.ROOM_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.DORMITORY_COST_PAYMENT:
                if(type == Packet.REQUEST)
                {
                    String result = ServerFunctions.findTotalCost_std(studentID);
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.DORMITORY_COST_PAYMENT, result.getBytes().length, result);
                }
                break;

            case Packet.PAYMENT:
                if(type == Packet.REQUEST)
                {
                    //msg = server.납부자조회요청_응답(msg, is, os);//패킷 보내기까지
                    String result = ServerFunctions.findPaymentStudents();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.PAYMENT, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    if(!(ServerFunctions.checkDate(4)||ServerFunctions.checkDate(9)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESULT, Packet.PAYMENT, result.getBytes().length, result);
                        break;
                    }
                    //msg = server.납부등록(msg, is, os);//납부하고 결과 패킷 보내기까지
                    String result = ServerFunctions.updatePayment(studentID,msg.getData1(),msg.getData2());
                    msg = Message.makeMessage(Packet.RESULT, Packet.PAYMENT, result.getBytes().length, result);
                }
                break;

            case Packet.UNPAID_USER_CHECK:
                if(type == Packet.REQUEST)
                {
                    //msg = server.미납부자조회요청_응답(msg, is, os);//패킷 보내기까지
                    String result = ServerFunctions.find_Non_PaymentStudents();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.UNPAID_USER_CHECK, result.getBytes().length, result);
                }
                break;

            case Packet.CERTIFICATE:
                if(type == Packet.REQUEST)
                {
                    //msg = server.결핵진단서조회요청_응답(msg, is, os);
                    String result = ServerFunctions.findSubmittedStudents();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.CERTIFICATE, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    if(!(ServerFunctions.checkDate(5)||ServerFunctions.checkDate(10)))
                    {
                        String result = "일정기간 아님";
                        msg = Message.makeMessage(Packet.RESULT, Packet.CERTIFICATE, result.getBytes().length, result);
                        break;
                    }
                    msg = ServerFunctions.insertCertificate(msg,studentID);
                }
                break;

            case Packet.CERTIFICATE_FILE:
                if(type == Packet.REQUEST)
                {
                    //msg = server.결핵진단서_다운로드요청_응답(msg, is, os);//패킷 보내기까지
                    msg = ServerFunctions.download(msg.getData1(),msg);
                }
                break;

            case Packet.RESIGNATION_APPLY:
                if(type == Packet.REQUEST)
                {
                    //msg = server.퇴사신청자조회요청_응답(msg, is, os);//패킷 보내기까지
                    String result = ServerFunctions.findResignationStudents();
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.RESIGNATION_APPLY, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                    msg = ServerFunctions.insertResignation(msg, studentID);
                break;

            case Packet.REFUND:
                if(type == Packet.REQUEST)
                {
                    //msg = server.환불확인조회요청_응답
                    String result = ServerFunctions.findRefund(studentID);
                    msg = Message.makeMessage(Packet.RESPONSE, Packet.REFUND, result.getBytes().length, result);
                }
                else if(type == Packet.SUBMIT)
                {
                    //msg = server.환불등록(msg, is, os);//등록하고 결과 패킷 보내기까지
                    String result = ServerFunctions.updateRefundStatus(msg.getData1());
                    msg = Message.makeMessage(Packet.RESULT, Packet.REFUND, result.getBytes().length, result);
                }
                break;

            case Packet.REWARD_POINT:
                if(type == Packet.REQUEST)
                {
                    if(authority.equals("s"))
                        msg = ServerFunctions.findStudentPoint_student(studentID);
                    else if(authority.equals("a"))
                        msg = ServerFunctions.findStudentPoint_admin();
                }
                else if(type == Packet.SUBMIT)
                    msg = ServerFunctions.insertStudentPoint(msg);
                break;
        }
        return msg;
    }
}