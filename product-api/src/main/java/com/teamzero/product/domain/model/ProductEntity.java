package com.teamzero.product.domain.model;

import com.teamzero.product.domain.model.dto.ProductInfo;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "PRODUCT")
public class ProductEntity extends BaseEntity{

    // 제로몰 상품 구분 인덱스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연결된 쇼핑몰 사이트 상품
    @OneToMany
    @JoinColumn
    private List<MallProductInfoEntity> mallProducts;

    // 카테고리
    private String category1; // 대분류
    private String category2; // 중분류
    private String category3; // 소분류

    // 기본 상품 정보(네이버 기준)
    private Long productId;
    private String brand;
    private String name;
    private String imageUrl;
    private int price;

    // 연결된 쇼핑몰 사이트 정보 제외
    public static ProductEntity of(ProductInfo productInfo) {
        return ProductEntity.builder()
            .category1(productInfo.getCategory1())
            .category2(productInfo.getCategory2())
            .category3(productInfo.getCategory3())
            .productId(productInfo.getProductId())
            .brand(productInfo.getBrand())
            .name(productInfo.getName())
            .imageUrl(productInfo.getImageUrl())
            .price(productInfo.getPrice())
            .build();
    }
}
