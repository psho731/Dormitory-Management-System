package view;

public class PaymentHistory_View
{
    public String checkPayment(int check)
    { // 사용자에게 납부 여부를 출력
        if (0 < check)
            return "납부 성공\n";

        return "납부 실패\n";
    }

    public String checkRefund(int check)
    { // 사용자에게 환불 여부를 출력
        if (0 < check)
            return "환불 성공\n";

        return "환불 실패\n";
    }
}