package session9;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
// 임베디드 타입은 불변 객체로 만들자
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // 기본 생성자 필수
    public Period() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(startDate, period.startDate) && Objects.equals(endDate, period.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    // 불변
    private void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // 불변
    private void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
