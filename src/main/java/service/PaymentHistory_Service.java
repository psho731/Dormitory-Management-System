package service;

import persistence.dao.PaymentHistory_DAO;

public class PaymentHistory_Service 
{
    private final PaymentHistory_DAO dao;

    public PaymentHistory_Service(PaymentHistory_DAO dao)
    {
        this.dao = dao;
    }

    public int processPayment(String student_id, String bank, String accountNum)
    {   // 학번과 은행, 계좌번호를 받아 납부처리
        // 납부 성공 시 1 이상 반환(뷰에서 사용)
        return dao.processPayment(student_id, bank, accountNum);
    }

    public int processRefund(String student_id)
    {   // // 학번과 은행, 계좌번호를 받아 환불처리
        // 환불 성공 시 1 이상 반환(뷰에서 사용)
        return dao.processRefund(student_id);
    }
}
