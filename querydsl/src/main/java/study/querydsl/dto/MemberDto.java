package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberDto {

    private String username;
    private int age;

    public MemberDto() {
    }

    @QueryProjection // 컴파일로 q파일 생성
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}