package view;

public class Apply_View
{
    public String printPass(byte pass)
    { // 합격 여부 출력 (학생이 합격 여부 조회할 때 사용)
        if (pass == 1)
            return "지원 결과: 합격\n";
        else if (pass == 0)
            return "지원 결과: 불합격\n";

        return "지원자가 아닙니다.\n";
    }

    public String printInsert(boolean check)
    { // 지원 결과 출력
        if(check)
            return "지원 성공\n";

        return "지원 실패\n";
    }
}