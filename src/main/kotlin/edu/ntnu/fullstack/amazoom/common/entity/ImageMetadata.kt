package edu.ntnu.fullstack.amazoom.common.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "image_metadata")
data class ImageMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val originalName: String,

    @Column(nullable = false)
    val contentType: String,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val uploadDate: LocalDateTime
)