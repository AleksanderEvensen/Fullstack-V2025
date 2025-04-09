package edu.ntnu.fullstack.amazoom.common.repository

import edu.ntnu.fullstack.amazoom.common.entity.ImageMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageMetadataRepository : JpaRepository<ImageMetadata, Long>
