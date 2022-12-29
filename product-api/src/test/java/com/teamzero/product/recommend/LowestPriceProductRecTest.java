package com.teamzero.product.recommend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallProductRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.recommend.LowestPriceProductRec;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LowestPriceProductRecTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MallProductRepository mallProductRepository;
    @InjectMocks
    private LowestPriceProductRec lowestPriceProductRec;

    @Test
    void lowestPriceProductRecTest() {
        ProductEntity product = ProductEntity.builder()
            .productId(211L)
            .productName("테스트 상품 1")
            .price(70000)
            .build();
        Optional<MallProductEntity> mallProduct = Optional.ofNullable(
            MallProductEntity.builder()
                .id(1L)
                .productId(1L)
                .price(70000)
                .maxPrice(100000)
                .build());
        // given

        given(mallProductRepository.findById(anyLong())).willReturn(
            mallProduct);
        given(mallProductRepository.findAllByProductId(anyLong())).willReturn(
            getMallProductList());
        given(
            productRepository.findAllByRegisteredAtAfterOrModifiedAtAfter(any(),
                any())).willReturn(getProductList());
        System.out.println(lowestPriceProductRec.recommendProducts());


    }

    private List<MallProductEntity> getMallProductList() {

        List<MallProductEntity> mallProductEntities = new ArrayList();

        for (int i = 0; i < 3; i++) {
            MallProductEntity product = MallProductEntity.builder()
                .id(i + 1L)
                .productId(i + 1L)
                .price(i + 70000)
                .maxPrice(100000)
                .priceUpdateDt(LocalDate.now())
                .build();
            mallProductEntities.add(product);
        }
        for (int i = 0; i < 3; i++) {
            MallProductEntity product = MallProductEntity.builder()
                .id(i + 1L)
                .productId(i + 1L)
                .price(i + 80000)
                .maxPrice(100000)
                .priceUpdateDt(LocalDate.now())
                .build();
            mallProductEntities.add(product);
        }
        for (int i = 0; i < 1; i++) {
            MallProductEntity product = MallProductEntity.builder()
                .id(i + 1L)
                .productId(i + 1L)
                .price(i + 60000)
                .maxPrice(100000)
                .priceUpdateDt(LocalDate.now())
                .build();
            mallProductEntities.add(product);
        }
        return mallProductEntities;
    }

    private List<ProductEntity> getProductList() {

        List<ProductEntity> productEntities = new ArrayList();
        for (int i = 0; i < 5; i++) {
            ProductEntity product = ProductEntity.builder()
                .productId(i + 211L)
                .price(i - 70000)

                .build();
            productEntities.add(product);
        }
//        for (int i = 0; i < 10; i++) {
//            ProductEntity product = ProductEntity.builder()
//                .productId(i + 622L)
//                .price(100000)
//                .build();
//            productEntities.add(product);
//        }
        return productEntities;
    }
}

