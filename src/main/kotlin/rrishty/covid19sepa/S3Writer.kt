package rrishty.covid19sepa

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class S3Writer(
    private val bucketName: String,
    private val keyName: String,
    private val regionName: String
) {
    fun writeCsvContentsToS3(contents: String) {
        val s3Client = S3Client.builder()
            .region(Region.of(regionName))
            .build()

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .contentLength(contents.length.toLong())
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build()
        s3Client.putObject(putObjectRequest, contents.toRequestBody())
    }

    private fun String.toRequestBody(): RequestBody = RequestBody.fromString(this)
}