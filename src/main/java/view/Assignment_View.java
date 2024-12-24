package view;

public class Assignment_View
{
    public String printSwap(boolean pass)
    { // 교체 여부 출력 (관리자가 수동 배정할 때 사용)
        if (pass)
            return "교체 성공\n";
        else
            return "교체 실패\n";
    }
}
