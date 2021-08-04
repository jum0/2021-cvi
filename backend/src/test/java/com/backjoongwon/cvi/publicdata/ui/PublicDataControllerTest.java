package com.backjoongwon.cvi.publicdata.ui;

import com.backjoongwon.cvi.ApiDocument;
import com.backjoongwon.cvi.publicdata.application.PublicDataService;
import com.backjoongwon.cvi.publicdata.dto.VaccinationRateResponse;
import com.backjoongwon.cvi.user.domain.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.backjoongwon.cvi.publicdata.PublicDataFacotry.toVaccinationRateResponse;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("공공데이터 컨트롤러 Mock 테스트")
@WebMvcTest(controllers = PublicDataController.class)
class PublicDataControllerTest extends ApiDocument {

    private static final LocalDateTime targetDateTime = LocalDateTime.now();

    @MockBean
    public PublicDataService publicDataService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @DisplayName("백신 접종률 조회 - 성공")
    @Test
    void findVaccinationRate() throws Exception {
        //given
        List<VaccinationRateResponse> vaccinationRateResponses = toVaccinationRateResponse(targetDateTime);
        //when
        willReturn(vaccinationRateResponses).given(publicDataService).findVaccinationRates(targetDateTime.toLocalDate());
        ResultActions response = 백신_정보률_조회_요청(targetDateTime.toLocalDate());
        //then
        백신_접종률_조회_성공함(response);
    }

    @DisplayName("백신 접종률 조회 - 성공 - 요청 후 api 데이터가 없을 때")
    @Test
    void findVaccinationRateEmtpy() throws Exception {
        //given
        //when
        willReturn(Collections.emptyList()).given(publicDataService).findVaccinationRates(targetDateTime.toLocalDate());
        ResultActions response = 백신_정보률_조회_요청(targetDateTime.toLocalDate());
        //then
        백신_접종률_없데이트전_조회_성공함(response);
    }

    private ResultActions 백신_정보률_조회_요청(LocalDate targetDate) throws Exception {
        return mockMvc.perform(get("/api/v1/publicdatas/vaccinations")
                .param("targetDate", LocalDate.now().toString()));
    }

    private void 백신_접종률_조회_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("publicdata-vaccination-find"));
    }

    private void 백신_접종률_없데이트전_조회_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("publicdata-vaccination-nodata-find"));
    }
}