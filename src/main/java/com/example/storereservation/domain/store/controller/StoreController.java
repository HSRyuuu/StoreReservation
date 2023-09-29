package com.example.storereservation.domain.store.controller;

import com.example.storereservation.domain.store.dto.ListQueryInput;
import com.example.storereservation.domain.store.dto.StoreDetail;
import com.example.storereservation.domain.store.service.StoreService;
import com.example.storereservation.global.type.StoreSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 검색
     * @param page : 페이지
     * @param input : storeName, sortType( ALL, ALPHABET, RATING, RATING_COUNT, DISTANCE)
     *              TODO : 거리순(Distance)
     */
    @GetMapping("/store/list")
    public ResponseEntity<?> storeList(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                       @RequestBody ListQueryInput input) {
        Page<StoreDetail> findStores =
                storeService.getStoreListByStoreName(
                        input.getStoreName(),
                        StoreSortType.valueOf(input.getSortType()),
                        page - 1);


        return ResponseEntity.ok(findStores);
    }

    /**
     * 매장 상세
     * @param name : 매장 명
     */
    @GetMapping("/store/detail")
    public ResponseEntity<?> storeDetail(@RequestParam String name) {
        StoreDetail findStore = storeService.findByStoreName(name);
        return ResponseEntity.ok(findStore);
    }

    /**
     * TODO 매장 별 리뷰 목록 확인
     */
    @GetMapping("/store/review/{storeId}")
    public ResponseEntity<?> reviewListByStoreId(@PathVariable Long storeId){

        return ResponseEntity.ok(null);
    }

}
