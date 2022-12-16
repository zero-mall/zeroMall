package com.teamzero.product.domain.model;

import com.teamzero.product.domain.dto.NaverSearch.Response.NaverProduct;
import com.teamzero.product.domain.dto.ProductDetail;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "PRODUCT")
public class ProductEntity extends BaseEntity{

    // 제로몰 상품 구분 인덱스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    // 카테고리
    private String catId;

    // 네이버 상품 정보
    private Long naverId;
    private String brand;
    private String name;
    private String imageUrl;
    private int price;

    // 연결된 쇼핑몰 상품 목록
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<MallProductEntity> mallProducts;

    // 별점
    private int avgStar;

    // 좋아요, 조회수
    private long viewCount;
    private long likeCount;

    /**
     * 네이버 상품
     * 연결된 쇼핑몰 상품들을 제외한 정보만 저장
     */
    public static ProductEntity from(ProductDetail.Request request, String catId) {
        return ProductEntity.builder()
            .catId(catId)
            .naverId(request.getNaverId())
            .brand(request.getBrand())
            .name(request.getTitle())
            .imageUrl(request.getImageUrl())
            .price(request.getLPrice())
            .build();
    }
}
