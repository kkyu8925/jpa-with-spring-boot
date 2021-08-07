package com.example.jpaapp1.domain.item;

import com.example.jpaapp1.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 전략
@DiscriminatorColumn(name = "dtype") // 구분 칼럼
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    // @ManyToMany 실무에서는 사용 금지
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
