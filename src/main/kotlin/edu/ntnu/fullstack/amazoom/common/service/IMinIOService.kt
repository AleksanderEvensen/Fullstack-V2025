package edu.ntnu.fullstack.amazoom.common.service

import org.springframework.web.multipart.MultipartFile

interface IMinIOService {
    fun uploadImage(file: MultipartFile, fileNameBase: String): String
    fun getImageData(fileName: String): ByteArray
    fun getImageContentType(fileName: String): String
}