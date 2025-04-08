package com.gijun.withusglobal.store.controller

import com.gijun.withusglobal.common.dto.CommonResponse
import com.gijun.withusglobal.common.dto.PagedResponse
import com.gijun.withusglobal.store.dto.ApplicationDTO
import com.gijun.withusglobal.store.service.StoreApplicationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/applications")
@Tag(name = "스토어 지원", description = "스토어 소유자를 위한 지원서 관리")
class StoreApplicationController(
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )private val storeApplicationService: StoreApplicationService
) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@GetMapping("/campaign/{campaignId}")
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )fun getApplicationsForCampaign(
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@PathVariable campaignId: Long,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(defaultValue = "0") page: Int,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(defaultValue = "10") size: Int,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(required = false) status: String?
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )try {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val applications = storeApplicationService.getApplicationsForCampaign(campaignId, page, size, status)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.ok(CommonResponse.success(applications))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )} catch (e: Exception) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@GetMapping
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )fun getStoreApplications(
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(defaultValue = "0") page: Int,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(defaultValue = "10") size: Int,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestParam(required = false) status: String?
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )): ResponseEntity<CommonResponse<PagedResponse<ApplicationDTO.Response>>> {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )try {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val applications = storeApplicationService.getStoreApplications(page, size, status)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.ok(CommonResponse.success(applications))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )} catch (e: Exception) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get applications"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@GetMapping("/{applicationId}")
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )fun getApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.DetailedResponse>> {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )try {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val application = storeApplicationService.getApplication(applicationId)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.ok(CommonResponse.success(application))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )} catch (e: Exception) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to get application"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@PostMapping("/{applicationId}/approve")
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )fun approveApplication(@PathVariable applicationId: Long): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )try {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val application = storeApplicationService.approveApplication(applicationId)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val response = ApplicationDTO.Response.fromEntity(application)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.ok(CommonResponse.success(response, "Application approved successfully"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )} catch (e: Exception) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to approve application"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@PostMapping("/{applicationId}/reject")
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )fun rejectApplication(
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@PathVariable applicationId: Long,
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )@RequestBody request: ApplicationDTO.RejectRequest
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )): ResponseEntity<CommonResponse<ApplicationDTO.Response>> {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )try {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val application = storeApplicationService.rejectApplication(applicationId, request.reason)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )val response = ApplicationDTO.Response.fromEntity(application)
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.ok(CommonResponse.success(response, "Application rejected successfully"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )} catch (e: Exception) {
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )return ResponseEntity.badRequest().body(CommonResponse.error(e.message ?: "Failed to reject application"))
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
    @Operation(
        summary = "캠페인별 지원 목록 조회",
        description = "특정 캠페인에 대한 모든 지원 목록을 페이지네이션으로 조회합니다",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "지원 목록 조회 성공",
                content = [Content(schema = Schema(implementation = PagedResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "지원 목록 조회 실패"
            )
        ]
    )}
}
