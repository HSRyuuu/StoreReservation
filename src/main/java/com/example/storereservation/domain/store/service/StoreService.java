package com.example.storereservation.domain.store.service;

import com.example.storereservation.domain.review.dto.ReviewDto;
import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import com.example.storereservation.global.type.PageConst;
import com.example.storereservation.domain.store.dto.StoreDetail;
import com.example.storereservation.domain.store.persist.StoreEntity;
import com.example.storereservation.domain.store.persist.StoreRepository;
import com.example.storereservation.global.type.StoreSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * 상점 명으로 상점 정보 찾기
     * - STORE_NOT_FOUND : 해당 상점명을 가진 상점이 없을 때
     */
    public StoreDetail findByStoreName(String name){
        StoreEntity findStore = storeRepository.findByStoreName(name)
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));
        return StoreDetail.fromEntity(findStore);
    }

    /**
     *  상점 명으로 상점 리스트 찾기
     *  SortType : ALL / ALPHABET / RATING / REVIEW_COUNTS / DISTANCE
     *  page : constant로 저장
     */
    public Page<StoreDetail> getStoreListByStoreName(String storeName, StoreSortType storeSortType, Integer page){
        PageRequest pageRequest = getPageRequestBySortTypeAndPage(storeSortType, page);
        Page<StoreEntity> findStores = storeRepository.findByStoreNameContaining(storeName, pageRequest);

        if(findStores.getSize() == 0){
            throw new MyException(ErrorCode.STORE_NO_SEARCH_RESULT);
        }

        return findStores.map(store -> StoreDetail.fromEntity(store));
    }

    private PageRequest getPageRequestBySortTypeAndPage(StoreSortType storeSortType, Integer page){
        PageRequest pageRequest = PageRequest.of(page, PageConst.STORE_LIST_PAGE_SIZE);
        if(storeSortType == StoreSortType.ALPHABET){
            return PageRequest.of(page, PageConst.STORE_LIST_PAGE_SIZE, Sort.by("storeName"));
        }else if(storeSortType == StoreSortType.RATING){
            return PageRequest.of(page, PageConst.STORE_LIST_PAGE_SIZE, Sort.by("rating").descending());
        }else if(storeSortType == StoreSortType.RATING_COUNT){
            return PageRequest.of(page, PageConst.STORE_LIST_PAGE_SIZE, Sort.by("ratingCount").descending());
        } else if(storeSortType == StoreSortType.DISTANCE){
            //TODO 거리 ???
        }
        return pageRequest;
    }

    /**
     * 리뷰 추가됬을 때, 매장의 리뷰 업데이트
     * @param review : 추가된 리뷰
     */
    public void updateRatingForAddReview(ReviewDto review){
        StoreEntity store = storeRepository.findByStoreName(review.getStoreName())
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));
        log.info("Update Store Rating, before update => rating : {}, count : {}", store.getRating(), store.getRatingCount());
        Long ratingCount = store.getRatingCount();
        double rating = getNewRatingForAddReview(store.getRating(), ratingCount, review.getRating());

        store.setRating(rating);
        store.setRatingCount(ratingCount + 1);

        StoreEntity saved = storeRepository.save(store);
        log.info("update complete => rating : {}, count : {}", saved.getRating(),saved.getRatingCount());
    }

    /**
     * 리뷰 추가 시 업데이트된 별점 반환
     * @param rating : 매장의 기존 별점
     * @param ratingCount : 매장의 기존 리뷰 수
     * @param reviewRating : 새로운 리뷰의 별점
     * @return : 업데이트 될 매장의 별점
     */
    private double getNewRatingForAddReview(double rating, Long ratingCount, double reviewRating){
        double updatedRating = rating * ((double)ratingCount / (ratingCount + 1)) + reviewRating / (ratingCount + 1);
        log.info("updated : {}", updatedRating);
        return updatedRating;
    }

    /**
     * 리뷰 수정 시 매장 별점 정보 업데이트
     */
    public void updateRatingForEditReview(ReviewDto review, double oldRating){
        StoreEntity store = storeRepository.findByStoreName(review.getStoreName())
                .orElseThrow(() -> new MyException(ErrorCode.STORE_NOT_FOUND));

        double newRating =
                getNewRatingForEditReview(
                        store.getRating(),
                        store.getRatingCount(),
                        oldRating,
                        review.getRating());

        store.setRating(newRating);

        storeRepository.save(store);
    }

    /**
     * 리뷰 수정 시 업데이트 된 별점 반환
     * @param rating : 기존의 별점
     * @param ratingCount : 기존의 리뷰 수
     * @param reviewOldRating : 기존 리뷰의 별점
     * @param reviewNewRating : 수정된 리뷰의 별점
     * @return 업데이트 될 매장의 별점
     */
    private double getNewRatingForEditReview(double rating, Long ratingCount, double reviewOldRating, double reviewNewRating){
        return rating - (reviewOldRating - reviewNewRating) / ratingCount;
    }
}
