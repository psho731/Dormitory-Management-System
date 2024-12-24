package view;

public class ResignationApply_View
{
    // 성공여부 (1: 성공, 0: 실패) 를 인자로 받아서 각각 띄워줄 문자열을 반환.
    public String checkInsert(int check)
    {
        if (0 < check)
            return "퇴사 신청 성공\n";

        return "퇴사 신청 실패\n";
    }
}