package com.example.storereservation.domain.store.controller;

import com.example.storereservation.domain.store.dto.ListQueryInput;
import com.example.storereservation.domain.store.dto.StoreDetail;
import com.example.storereservation.domain.store.service.StoreService;
import com.example.storereservation.domain.store.type.SortType;
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
     * 매장 상세
     */
    @GetMapping("/store/detail")
    public ResponseEntity<?> storeDetail(@RequestParam String name) {
        StoreDetail findStore = storeService.findByStoreName(name);
        return ResponseEntity.ok(findStore);
    }

    /**
     * 매장 검색
     *
     * @param page
     * @param input : storeName, sortType
     */
    @GetMapping("/store/list")
    public ResponseEntity<?> storeList(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                       @RequestBody ListQueryInput input) {
        Page<StoreDetail> findStores =
                storeService.getStoreListByStoreName(
                        input.getStoreName(),
                        SortType.valueOf(input.getSortType()),
                        page - 1);


        return ResponseEntity.ok(findStores);
    }


}
