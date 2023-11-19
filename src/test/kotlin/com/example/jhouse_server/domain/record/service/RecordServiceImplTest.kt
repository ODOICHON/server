package com.example.jhouse_server.domain.record.service

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.repository.RecordRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class RecordServiceImplTest @Autowired constructor(
    private val recordService: RecordService,
    private val recordRepository: RecordRepository,
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val odoriReqDto = MockEntity.odoriReqDto()
    private val retroReqDto = MockEntity.retroReqDto()
    private val techReqDtoNewTech = MockEntity.techReqDtoNewTech()
    private val techReqDtoIssue = MockEntity.techReqDtoIssue()
    private val recordUpdateDto = MockEntity.recordUpdateDto()

    private val recordPageConditionAll = MockEntity.recordPageConditionAll()
    private val recordPageConditionOdori = MockEntity.recordPageConditionOdori()
    private val recordPageConditionRetro = MockEntity.recordPageConditionRetro()
    private val recordPageConditionTech = MockEntity.recordPageConditionTech()

    private val recordIds: MutableList<Long> = mutableListOf()
    private val ip: String = "ipAddress"

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        user.updateAuthority(Authority.ADMIN)
        user.updateUserType(UserType.SERVER)

        //record setting
        val recordId1 = recordService.saveRecord(odoriReqDto, user)
        val recordId2 = recordService.saveRecord(retroReqDto, user)
        val recordId3 = recordService.saveRecord(techReqDtoNewTech, user)
        val recordId4 = recordService.saveRecord(techReqDtoNewTech, user)
        val recordId5 = recordService.saveRecord(techReqDtoIssue, user)
        val record1 = recordRepository.findById(recordId1).get()
        val record2 = recordRepository.findById(recordId2).get()
        val record3 = recordRepository.findById(recordId3).get()
        val record4 = recordRepository.findById(recordId4).get()
        val record5 = recordRepository.findById(recordId5).get()
        record1.updateRecordStatus(RecordStatus.APPROVE)
        record2.updateRecordStatus(RecordStatus.APPROVE)
        record3.updateRecordStatus(RecordStatus.APPROVE)
        record4.updateRecordStatus(RecordStatus.APPROVE)
        record5.updateRecordStatus(RecordStatus.APPROVE)

        recordIds.add(recordId1)
        recordIds.add(recordId2)
        recordIds.add(recordId3)
        recordIds.add(recordId4)
        recordIds.add(recordId5)
    }

    @Test
    @DisplayName("레코드 저장 테스트")
    fun saveRecordTest() {
        //given
        val user = userRepository.findByUserName(userSignUpDto.userName).get()

        //when
        val recordId = recordService.saveRecord(odoriReqDto, user)
        val record = recordRepository.findById(recordId).get()

        //then
        assertThat(record.title).isEqualTo(odoriReqDto.title)
        assertThat(record.content).isEqualTo(odoriReqDto.content)
        assertThat(record.part).isEqualTo(Part.SERVER)
        assertThat(record.hits).isEqualTo(0)
        assertThat(record.user).isEqualTo(user)
    }

    @Test
    @DisplayName("레코드 수정 테스트")
    fun updateRecordTest() {
        //given
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val recordId = recordService.saveRecord(odoriReqDto, user)

        //when
        recordService.updateRecord(recordUpdateDto, user, recordId)
        val record = recordRepository.findById(recordId).get()

        //then
        assertThat(record.title).isEqualTo(recordUpdateDto.title)
        assertThat(record.content).isEqualTo(recordUpdateDto.content)
        assertThat(record.part).isEqualTo(Part.SERVER)
        assertThat(record.hits).isEqualTo(0)
        assertThat(record.user).isEqualTo(user)
    }

    @Test
    @DisplayName("레코드 삭제 테스트")
    fun deleteRecordTest() {
        //given
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val recordId = recordService.saveRecord(odoriReqDto, user)

        //when
        recordService.deleteRecord(user, recordId)
        val recordOptional = recordRepository.findById(recordId)

        //then
        assertThat(recordOptional).isEmpty
    }

    @Test
    @DisplayName("핫 레코드 조회")
    fun getHotRecordsTest() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]
        val recordId3 = recordIds[2]
        val record1 = recordRepository.findById(recordId1).get()
        val record2 = recordRepository.findById(recordId2).get()
        val record3 = recordRepository.findById(recordId3).get()
        record1.updateHits()
        record2.updateHits()
        record3.updateHits()

        //when
        val recordHotResDto = recordService.getHotRecords()
        val hotThumbnailResDtos = recordHotResDto.records

        //then
        assertThat(hotThumbnailResDtos.size).isEqualTo(3)
    }

    @Test
    @DisplayName("레코드 목록 조회")
    fun getRecordsTest() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]
        val recordId3 = recordIds[2]
        val recordId4 = recordIds[3]

        val pageRequest1 = PageRequest.of(0, 4)
        val pageRequest2 = PageRequest.of(1, 4)

        //when
        val all1 = recordService.getRecords(recordPageConditionAll, pageRequest1).records.content
        val all2 = recordService.getRecords(recordPageConditionAll, pageRequest2).records.content
        val odori = recordService.getRecords(recordPageConditionOdori, pageRequest1).records.content
        val retro = recordService.getRecords(recordPageConditionRetro, pageRequest1).records.content
        val tech = recordService.getRecords(recordPageConditionTech, pageRequest1).records.content

        val allWeb = recordService.getRecords(MockEntity.recordPageCondition("web", "all", ""), pageRequest1).records.content
        val allServer = recordService.getRecords(MockEntity.recordPageCondition("server", "all", ""), pageRequest1).records.content
        val allInfra = recordService.getRecords(MockEntity.recordPageCondition("infra", "all", ""), pageRequest1).records.content
        val odoriWeb = recordService.getRecords(MockEntity.recordPageCondition("web", "odori", "culture"), pageRequest1).records.content
        val odoriServer = recordService.getRecords(MockEntity.recordPageCondition("server", "odori", "culture"), pageRequest1).records.content
        val odoriInfra = recordService.getRecords(MockEntity.recordPageCondition("infra", "odori", "culture"), pageRequest1).records.content
        val retroWeb = recordService.getRecords(MockEntity.recordPageCondition("web", "retro", "retrospection"), pageRequest1).records.content
        val retroServer = recordService.getRecords(MockEntity.recordPageCondition("server", "retro", "retrospection"), pageRequest1).records.content
        val retroInfra = recordService.getRecords(MockEntity.recordPageCondition("infra", "retro", "retrospection"), pageRequest1).records.content
        val techWeb = recordService.getRecords(MockEntity.recordPageCondition("web", "tech", "new_tech"), pageRequest1).records.content
        val techServer = recordService.getRecords(MockEntity.recordPageCondition("server", "tech", "new_tech"), pageRequest1).records.content
        val techInfra = recordService.getRecords(MockEntity.recordPageCondition("infra", "tech", "new_tech"), pageRequest1).records.content

        //then
        assertThat(all1.size).isEqualTo(4)
        assertThat(all2.size).isEqualTo(1)
        assertThat(odori.size).isEqualTo(1)
        assertThat(retro.size).isEqualTo(1)
        assertThat(tech.size).isEqualTo(2)
        assertThat(odori.map { it.recordId }.toList()).contains(recordId1)
        assertThat(retro.map { it.recordId }.toList()).contains(recordId2)
        assertThat(tech.map { it.recordId }.toList()).containsAll(listOf(recordId3, recordId4))

        assertThat(allWeb.size).isEqualTo(0)
        assertThat(allServer.size).isEqualTo(4)
        assertThat(allInfra.size).isEqualTo(0)
        assertThat(odoriWeb.size).isEqualTo(0)
        assertThat(odoriServer.size).isEqualTo(1)
        assertThat(odoriInfra.size).isEqualTo(0)
        assertThat(retroWeb.size).isEqualTo(0)
        assertThat(retroServer.size).isEqualTo(1)
        assertThat(retroInfra.size).isEqualTo(0)
        assertThat(techWeb.size).isEqualTo(0)
        assertThat(techServer.size).isEqualTo(2)
        assertThat(techInfra.size).isEqualTo(0)
    }

    @Test
    @DisplayName("레코드 조회")
    fun getRecordTest() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]
        val recordId3 = recordIds[2]
        val record3 = recordRepository.findById(recordId3).get()

        val pageRequest = PageRequest.of(0, 10)

        //when
        val odori = recordService.getRecord(recordId1, ip, pageRequest)
        recordService.getRecord(recordId1, ip, pageRequest) //hits check
        val retro = recordService.getRecord(recordId2, ip, pageRequest)
        val tech = recordService.getRecord(recordId3, ip, pageRequest)

        //then
        assertThat(odori.hits).isEqualTo(1)
        assertThat(odori.recordId).isEqualTo(recordId1)

        assertThat(retro.hits).isEqualTo(1)
        assertThat(retro.recordId).isEqualTo(recordId2)

        assertThat(tech.hits).isEqualTo(1)
        assertThat(tech.recordId).isEqualTo(recordId3)
        assertThat(tech.title).isEqualTo(record3.title)
        assertThat(tech.content).isEqualTo(record3.content)
        assertThat(tech.part).isEqualTo("server")
        assertThat(tech.type).isEqualTo("tech")
        assertThat(tech.category).isEqualTo("new_tech")
    }
}