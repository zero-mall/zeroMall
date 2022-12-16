package com.teamzero.product.service;

import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.repository.MallRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MallService {

  private final MallRepository mallRepository;

  @Transactional(readOnly = true)
  public Optional<MallEntity> getMallByName(String name) {
    return mallRepository.findByName(name);
  }

}
