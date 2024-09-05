package com.example.project.repository;

import com.example.project.dto.ExpDataDTO;
import com.example.project.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpDataRepository extends CrudRepository<Member, UUID> {

    // Native Query로 데이터를 Object[] 배열로 반환
    @Query(value = "SELECT m.yyyymm, l.pricemj, l.pricekg, n.pricemj_heat, n.pricemj_ind_max, n.pricemj_ind_min, m.charge, " +
            "f.price AS lpgfm_price, " +
            "g.price AS lngfm_price, " +
            "(m.charge / n.pricemj_heat / 1.1) AS exp_mj_heat, " +
            "(m.charge / n.pricemj_ind_max / 1.1) AS exp_mj_ind_max, " +
            "(m.charge / n.pricemj_ind_min / 1.1) AS exp_mj_ind_min " +
            "FROM members m " +
            "JOIN lpghd l ON m.yyyymm = l.yyyymm " +
            "JOIN lngds n ON m.yyyymm = n.yyyymm " +
            "LEFT JOIN lpgfm f ON TO_DATE(l.yyyymm, 'YYYYMM') = TO_DATE(f.yyyymm, 'YYYYMM') + INTERVAL '2 months' " +
            "LEFT JOIN lngfm g ON TO_DATE(n.yyyymm, 'YYYYMM') = TO_DATE(g.yyyymm, 'YYYYMM') + INTERVAL '4 months' " +
            "WHERE m.phone_number = :phoneNumber", 
            nativeQuery = true)
    List<Object[]> findRawExpDataByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // 결과를 DTO로 변환하는 메서드
    default List<ExpDataDTO> findExpDataByPhoneNumber(String phoneNumber) {
        List<Object[]> results = findRawExpDataByPhoneNumber(phoneNumber);
        return results.stream()
            .map(result -> new ExpDataDTO(
                result[0] != null ? (String) result[0] : null,   // yyyymm
                result[1] != null ? ((Number) result[1]).floatValue() : null,    // priceMJ
                result[2] != null ? ((Number) result[2]).floatValue() : null,    // priceKG
                result[3] != null ? ((Number) result[3]).floatValue() : null,    // priceMJ_heat
                result[4] != null ? ((Number) result[4]).floatValue() : null,    // priceMJ_Ind_Max
                result[5] != null ? ((Number) result[5]).floatValue() : null,    // priceMJ_Ind_Min
                result[6] != null ? ((Number) result[6]).floatValue() : null,    // charge
                result[7] != null ? ((Number) result[7]).floatValue() : null,    // lpgfm_price
                result[8] != null ? ((Number) result[8]).floatValue() : null,    // lngfm_price
                result[9] != null ? ((Number) result[9]).floatValue() : null,    // exp_MJ_heat
                result[10] != null ? ((Number) result[10]).floatValue() : null,   // exp_MJ_Ind_Max
                result[11] != null ? ((Number) result[11]).floatValue() : null    // exp_MJ_Ind_Min
            ))
            .toList();
    }
}
