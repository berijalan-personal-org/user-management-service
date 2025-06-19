package com.batch14.usermanagementservice.controller

import com.batch14.usermanagementservice.domain.dto.response.BaseResponse
import com.batch14.usermanagementservice.domain.dto.response.ResGetAllUserDto
import com.batch14.usermanagementservice.service.MasterUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//API HELPER CONTROLLER
@RestController
@RequestMapping("/v1/datasource")
class DataSourceController(
    private val masterUserService: MasterUserService
){
    @GetMapping("/users/by-ids")
    fun getAllUsersByIds(
        @RequestParam(required = true) ids: List<Int>
    ): ResponseEntity<BaseResponse<List<ResGetAllUserDto>>> {
        return ResponseEntity.ok(
            BaseResponse(
                data = masterUserService.findAllUsersByIds(ids)
            )
        )
    }
}
