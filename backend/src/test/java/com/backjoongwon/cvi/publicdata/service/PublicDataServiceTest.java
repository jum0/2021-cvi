package com.backjoongwon.cvi.publicdata.service;

import com.backjoongwon.cvi.aws.s3.AwsS3Uploader;
import com.backjoongwon.cvi.parser.VaccinationParser;
import com.backjoongwon.cvi.publicdata.domain.PublicDataProperties;
import com.backjoongwon.cvi.publicdata.domain.RegionPopulation;
import com.backjoongwon.cvi.publicdata.domain.VaccinationStatistic;
import com.backjoongwon.cvi.publicdata.domain.VaccinationStatisticRepository;
import com.backjoongwon.cvi.publicdata.dto.VaccinationStatisticResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.backjoongwon.cvi.publicdata.PublicDataFacotry.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
@DisplayName("공공데이터 요청 서비스 흐름 테스트")
@Transactional
class PublicDataServiceTest {

    @MockBean
    private AwsS3Uploader awsS3Uploader;
    @Autowired
    private VaccinationStatisticRepository vaccinationStatisticRepository;
    @Autowired
    private PublicDataProperties publicDataProperties;
    @Autowired
    private PublicDataService publicDataService;

    private VaccinationParser vaccinationParser;

    public static Stream<Arguments> targetDate() {
        return Stream.of(
                Arguments.of(LocalDate.now()),
                Arguments.of(LocalDate.now().minusDays(1)),
                Arguments.of(LocalDate.of(2021, 3, 11))
        );
    }

    @BeforeEach
    void init() {
        vaccinationParser = mock(VaccinationParser.class);
        publicDataService = new PublicDataService(vaccinationParser, vaccinationStatisticRepository, publicDataProperties);
    }

    @AfterEach
    void clear() {
        vaccinationStatisticRepository.deleteAll();
    }

    @DisplayName("백신 정종률 데이터 저장 - 성공")
    @ParameterizedTest
    @MethodSource("targetDate")
    void saveVaccinationStatistics(LocalDate targetDate) {
        //given
        //when
        백신_접종률_저장되어_있음(targetDate);
        List<VaccinationStatistic> publicData = vaccinationStatisticRepository.findByBaseDate(targetDate);
        //then
        assertThat(publicData).extracting(VaccinationStatistic::getBaseDate)
                .contains(targetDate);
    }

    @DisplayName("백신 정종률 데이터 조회 - 성공")
    @ParameterizedTest
    @MethodSource("targetDate")
    void findVaccinationStatistics(LocalDate targetDate) {
        //given
        //when
        백신_접종률_저장되어_있음(targetDate);
        List<VaccinationStatisticResponse> vaccinationStatistics = publicDataService.findVaccinationStatistics(targetDate);
        //then
        assertThat(vaccinationStatistics).isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getAccumulatedFirstCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getAccumulatedSecondCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getBaseDate)
                .contains(targetDate);
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getSido)
                .containsAll(REGIONS);
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getFirstCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getSecondCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getTotalFirstCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getTotalSecondCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getTotalSecondCnt)
                .isNotEmpty();
        assertThat(vaccinationStatistics).extracting(VaccinationStatisticResponse::getTotalFirstRate)
                .isNotEmpty();
    }

    @DisplayName("세계 백신 정좁률 데이터 - 저장 - 성공 ")
    @Test
    void saveWorldVaccinationStatistics() {
        //given
        LocalDate targetDate = LocalDate.now();
        //when
        세계_백신_접종률_저장되어_있음(targetDate);
        List<VaccinationStatistic> vaccinationStatistics =
                vaccinationStatisticRepository.findByRegionPopulation(RegionPopulation.WORLD);
        //then
        assertThat(vaccinationStatistics).extracting(VaccinationStatistic::getBaseDate)
                .contains(targetDate);
        assertThat(vaccinationStatistics).extracting(VaccinationStatistic::getRegionPopulation)
                .contains(RegionPopulation.WORLD);
    }

    private void 백신_접종률_저장되어_있음(LocalDate targetDate) {
        willReturn(toVaccineParserResponse(targetDate))
                .given(vaccinationParser).parseToKoreaPublicData(any(LocalDate.class), anyString());
        publicDataService.saveVaccinationStatistics(targetDate);
    }

    private void 세계_백신_접종률_저장되어_있음(LocalDate targetDate) {
        willReturn(toWorldVaccinationParserResponse(targetDate)).given(vaccinationParser).parseToWorldPublicData();
        publicDataService.saveWorldVaccinationStatistics(targetDate);
    }
}
