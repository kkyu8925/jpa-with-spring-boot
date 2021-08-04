package session7;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// 이 클래스를 상속받아 사용
// 상속 관계 매핑X
// 엔티티 X, 테이블과 매핑X
// 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
@MappedSuperclass
public abstract class BaseEntity {

    private String createBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
