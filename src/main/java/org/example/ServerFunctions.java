package org.example;

import persistence.dao.*;
import service.*;
import view.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ServerFunctions 
{
    public static Message insertCertificate(Message msg, String studentID)
    {
        try 
        {
            // Base64로 인코딩된 이미지 문자열 (예: 위의 출력값)
            String encodedImage = msg.getData1(); // 실제 Base64 문자열로 교체

            // Base64 디코딩
            byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
            String imagePath = "C:\\Certificate\\" + studentID + ".jpg";
            // 이미지 파일로 저장
            Path path = Paths.get(imagePath);
            Files.write(path, decodedBytes);
            String insertPath = "C:\\\\Certificate\\\\" + studentID + ".jpg";
            Certificate_DAO certificateDAO = new Certificate_DAO();
            Certificate_Service certificateService = new Certificate_Service(certificateDAO);
            certificateService.insertCertificate(studentID, LocalDateTime.now(), insertPath);

            String result = "저장 완료";
            msg = Message.makeMessage(Packet.RESULT, Packet.CERTIFICATE, result.getBytes().length, result);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return msg;
    }

    public static Message insertResignation(Message msg, String studentID)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // 기본 ISO 형식
        LocalDateTime dateTime = LocalDateTime.parse(msg.getData1(), formatter);


        ResignationApply_DAO resignationApplyDAO = new ResignationApply_DAO();
        ResignationApply_Service resignationApplyService = new ResignationApply_Service(resignationApplyDAO);
        ResignationApply_View resignationApply_View = new ResignationApply_View();
        String result = resignationApply_View.checkInsert(resignationApplyService.insertResignation(studentID,dateTime,msg.getData2(),msg.getData3()));
        
        return Message.makeMessage(Packet.RESULT, Packet.RESIGNATION_APPLY, result.getBytes().length, result);
    }

    public static boolean checkDate(int id)
    {
        CheckDate_DAO checkDateDAO = new CheckDate_DAO();
        CheckDate_Service checkDateService = new CheckDate_Service(checkDateDAO);

        return checkDateService.checkDate(id);
    }

    public static Message executeManualAssignment(Message msg)
    {
        Assignment_DAO assignmentDAO = new Assignment_DAO();
        Assignment_Service assignmentService = new Assignment_Service(assignmentDAO);
        Assignment_View assignment_view = new Assignment_View();
        String result = assignment_view.printSwap(assignmentService.manualAssignmentSwap(msg.getData1(), msg.getData2()));

        return Message.makeMessage(Packet.RESULT, Packet.MANUAL_ASSIGNMENT, result.getBytes().length, result);
    }

    public static Message findStudentPoint_admin()
    {
        StudentPoint_DAO studentPointDAO = new StudentPoint_DAO();
        StudentPoint_Service assignmentService = new StudentPoint_Service(studentPointDAO);
        StudentPoint_View studentPoint_View = new StudentPoint_View();

        String result = studentPoint_View.printPointByStudentAdmin(assignmentService.findAll());
        
        return Message.makeMessage(Packet.RESPONSE, Packet.REWARD_POINT, result.getBytes().length, result);
    }

    public static Message findStudentPoint_student(String studentID)
    {
        StudentPoint_DAO studentPointDAO = new StudentPoint_DAO();
        StudentPoint_Service assignmentService = new StudentPoint_Service(studentPointDAO);
        StudentPoint_View studentPoint_View = new StudentPoint_View();

        String result = studentPoint_View.printPointByStudent(assignmentService.findStudent(studentID));
        
        return Message.makeMessage(Packet.RESPONSE, Packet.REWARD_POINT, result.getBytes().length, result);
    }

    public static Message insertStudentPoint(Message msg)
    {
        StudentPoint_DAO studentPointDAO = new StudentPoint_DAO();
        StudentPoint_Service assignmentService = new StudentPoint_Service(studentPointDAO);
        int score = Integer.parseInt(msg.getData2());

        assignmentService.insert(msg.getData1(),score,
                LocalDateTime.now(), msg.getData3());
        String result = "입력 완료";
        
        return Message.makeMessage(Packet.RESULT, Packet.REWARD_POINT, result.getBytes().length, result);
    }

    public static String findAllSchedules()
    {
        Schedule_DAO scheduleDAO = new Schedule_DAO();
        Schedule_Service scheduleService = new Schedule_Service(scheduleDAO);
        Schedule_View schedule_View = new Schedule_View();

        return schedule_View.printAll(scheduleService.findAll());
    }

    public static void insertSchedule(String name, String startDate, String endDate)
    {
        Schedule_DAO scheduleDAO = new Schedule_DAO();
        Schedule_Service scheduleService = new Schedule_Service(scheduleDAO);

        scheduleService.insert(name, startDate, endDate);
    }

    public static void deleteSchedule(String scheduleId)
    {
        Schedule_DAO scheduleDAO = new Schedule_DAO();
        Schedule_Service scheduleService = new Schedule_Service(scheduleDAO);

        scheduleService.delete(scheduleId);
    }

    public static String findAllDormitoryCost()
    {
        DormitoryCost_DAO dormitoryCostDAO = new DormitoryCost_DAO();
        DormitoryCost_Service dormitoryCostService = new DormitoryCost_Service(dormitoryCostDAO);
        DormitoryCost_View dormitory_cost_View = new DormitoryCost_View();

        return dormitory_cost_View.printAll(dormitoryCostService.findAll());
    }

    public static void updateDormitoryCost(int dormitoryCostId, int cost)
    {
        DormitoryCost_DAO dormitoryCostDAO = new DormitoryCost_DAO();
        DormitoryCost_Service dormitoryCostService = new DormitoryCost_Service(dormitoryCostDAO);

        dormitoryCostService.updateCost(dormitoryCostId, cost);
    }

    public static String findAllMealCost()
    {
        MealCost_DAO mealCostDAO = new MealCost_DAO();
        MealCost_Service mealCostService = new MealCost_Service(mealCostDAO);
        MealCost_View meal_cost_View = new MealCost_View();

        return meal_cost_View.printAll(mealCostService.findAll());
    }

    public static void updateMealCost(int dormitoryCostId, int cost)
    {
        MealCost_DAO mealCostDAO = new MealCost_DAO();
        MealCost_Service mealCostService = new MealCost_Service(mealCostDAO);

        mealCostService.updateCost(dormitoryCostId, cost);
    }

    public static String ApplyPassCheck(String clientId)
    {
        Apply_DAO applyDAO = new Apply_DAO();
        Apply_Service applyService = new Apply_Service(applyDAO);
        Apply_View apply_View = new Apply_View();

        return apply_View.printPass(applyService.findPassByStudentID(clientId));
    }

    public static String dormitoryApply(String clientId, String buildingName1,
                                         String mealOption1, String buildingName2, String mealOption2)
    {
        Apply_DAO applyDAO = new Apply_DAO();
        Apply_Service applyService = new Apply_Service(applyDAO);
        Apply_View applyView = new Apply_View();

        boolean isResult = applyService.insert(clientId, buildingName1, mealOption1, buildingName2, mealOption2);

        return applyView.printInsert(isResult);
    }

    public static void executeSelection(int totalDormitoryNum)
    {
        Selection_DAO selectionDAO = new Selection_DAO();
        Selection_Service selectionService = new Selection_Service(selectionDAO);

        selectionService.executeAutoSelection(totalDormitoryNum);

        Assignment_DAO assignmentDAO = new Assignment_DAO();
        Assignment_Service assignmentService = new Assignment_Service(assignmentDAO);
        assignmentService.autoAssignment();
    }

    public static String findRefund(String studentID)
    {
        VwResignationApply_DAO vwResignationApplyDAO = new VwResignationApply_DAO();
        VwResignationApply_Service vwResignationApplyService = new VwResignationApply_Service(vwResignationApplyDAO);
        VwResignationApply_View vwResignationApplyView = new VwResignationApply_View();
        return vwResignationApplyView.printRefund(vwResignationApplyService.findRefund(studentID));
    }

    public static String findAllSelection()
    {
        VwAssignment_DAO vw_assignment_DAO = new VwAssignment_DAO();
        VwAssignment_Service vw_assignment_Service = new VwAssignment_Service(vw_assignment_DAO);
        VwAssignment_View vw_assignment_View = new VwAssignment_View();

        return vw_assignment_View.printAll(vw_assignment_Service.findOrderByBuilding());
    }

    public static String findAllAssignment()
    {
        VwAssignment_DAO vw_assignment_DAO = new VwAssignment_DAO();
        VwAssignment_Service vw_assignment_Service = new VwAssignment_Service(vw_assignment_DAO);
        VwAssignment_View vw_assignment_View = new VwAssignment_View();

        return vw_assignment_View.printOrderByBuilding(vw_assignment_Service.findOrderByBuilding());
    }

    public static String Room_check(String studentID)
    {
        VwAssignment_DAO vw_assignment_DAO = new VwAssignment_DAO();
        VwAssignment_Service vw_assignment_Service = new VwAssignment_Service(vw_assignment_DAO);
        VwAssignment_View vw_assignment_View = new VwAssignment_View();

        return vw_assignment_View.printStudentID(vw_assignment_Service.findStudentID(studentID));
    }

    public static String findApplyStudents()
    {
        VwApply_DAO vw_apply_dao = new VwApply_DAO();
        VwApply_Service vw_applyService = new VwApply_Service(vw_apply_dao);
        VwApply_View vw_applyView = new VwApply_View();

        return vw_applyView.printOrderByBuilding(vw_applyService.findOrderByBuilding());
    }

    public static String findTotalCost()
    {
        VwCost_DAO vw_cost_DAO = new VwCost_DAO();
        VwCost_Service vw_cost_Service = new VwCost_Service(vw_cost_DAO);
        VwCost_View vw_cost_View = new VwCost_View();

        return vw_cost_View.printFormat(vw_cost_Service.findAll());
    }

    public static String findTotalCost_std(String studentID)
    {
        VwAssignmentCost_DAO vwAssignmentCost_DAO = new VwAssignmentCost_DAO();
        VwAssignmentCost_Service vwAssignmentCost_Service = new VwAssignmentCost_Service(vwAssignmentCost_DAO);
        VwAssignmentCost_View vwAssignmentCost_View = new VwAssignmentCost_View();

        return vwAssignmentCost_View.print_cost(vwAssignmentCost_Service.getTotalCost(studentID));
    }

    public static String findPaymentStudents()
    {
        VwAssignment_DAO vwAssignment_DAO = new VwAssignment_DAO();
        VwAssignment_Service vwAssignment_Service = new VwAssignment_Service(vwAssignment_DAO);
        VwAssignment_View vwAssignment_View = new VwAssignment_View();
        return vwAssignment_View.printPaymentStudent(vwAssignment_Service.findPaymentStudent());

    }

    public static String find_Non_PaymentStudents()
    {
        VwAssignment_DAO vwAssignment_DAO = new VwAssignment_DAO();
        VwAssignment_Service vwAssignment_Service = new VwAssignment_Service(vwAssignment_DAO);
        VwAssignment_View vwAssignment_View = new VwAssignment_View();
        return vwAssignment_View.printPaymentStudent(vwAssignment_Service.findNonPaymentStudent());

    }

    public static String findSubmittedStudents()
    {
        Certificate_DAO certificateDAO = new Certificate_DAO();
        Certificate_Service certificateService = new Certificate_Service(certificateDAO);
        Certificate_View certificateView = new Certificate_View();
        return certificateView.print_orderByBuilding(certificateService.findOrderByBuilding());
    }

    public static String updateRefundStatus(String studentId)
    {
        PaymentHistory_DAO paymentHistoryDAO = new PaymentHistory_DAO();
        PaymentHistory_Service paymentHistoryService = new PaymentHistory_Service(paymentHistoryDAO);
        PaymentHistory_View paymentHistory_View = new PaymentHistory_View();
        return paymentHistory_View.checkRefund(paymentHistoryService.processRefund(studentId));
    }

    public static String updatePayment(String studentId, String bank, String account)
    {
        PaymentHistory_DAO paymentHistoryDAO = new PaymentHistory_DAO();
        PaymentHistory_Service paymentHistoryService = new PaymentHistory_Service(paymentHistoryDAO);
        PaymentHistory_View paymentHistory_View = new PaymentHistory_View();

        return paymentHistory_View.checkPayment(paymentHistoryService.processPayment(studentId, bank, account));
    }

    public static String findResignationStudents()
    {
        VwResignationApply_DAO vw_resignationApply_DAO = new VwResignationApply_DAO();
        VwResignationApply_Service vw_resignationApply_Service = new VwResignationApply_Service(vw_resignationApply_DAO);
        VwResignationApply_View vw_resignationApply_View = new VwResignationApply_View();

        return vw_resignationApply_View.printOrderByBuilding(vw_resignationApply_Service.findOrderByBuilding());
    }

    public static Message download(String studentID, Message msg)
    {
        String encodedImage;
        try
        {
            Certificate_DAO certificateDAO = new Certificate_DAO();
            Certificate_Service certificateService = new Certificate_Service(certificateDAO);
            Certificate_View certificateView = new Certificate_View();
            String imagePath = certificateView.printCertificateFilePath(certificateService.findFilePath(studentID));

            // 이미지 파일을 읽어서 바이트 배열로 변환
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            // 이미지를 Base64로 인코딩하여 String으로 변환
            encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            msg = Message.makeMessage(Packet.RESPONSE, Packet.CERTIFICATE_FILE, encodedImage.getBytes().length, studentID.getBytes().length, encodedImage, studentID);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }

        return msg;
    }

    public static String info_Manual_assignment()
    {
        VwManualAssignment_DAO vwManualAssignment_DAO = new VwManualAssignment_DAO();
        VwManualAssignment_Service vwManualAssignment_Service = new VwManualAssignment_Service(vwManualAssignment_DAO);
        VwManualAssignment_View vwManualAssignment_View = new VwManualAssignment_View();

        return vwManualAssignment_View.printAssignmentStd(vwManualAssignment_Service.findAssignmentStd());
    }
}