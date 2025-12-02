package com.example.data.remote.network.feature.diagnosis

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.common.di.network.BASE_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.streams.asInput
import javax.inject.Inject

class DiagnosisApi @Inject constructor(
    private val client: HttpClient,
    @param:ApplicationContext private val context: Context
) {
    private val baseUrl = "${BASE_URL}/ai"

    suspend fun requestDiagnosis(
        images: List<Uri>,
        animalType: String,
        symptom: String,
        onUpload: (bytesSent: Long, totalBytes: Long) -> Unit
    ): HttpResponse {
        return client.post("$baseUrl/diagnosis") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("animalType", animalType)
                        append("symptom", symptom)

                        images.filterNotNull().forEachIndexed { index, uri ->
                            // 1. MIME 타입 및 확장자 결정
                            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                            val ext = if (mimeType.contains("png")) "png" else "jpg"
                            val fileName = "image_$index.$ext"

                            // 2. 스트림을 ByteArray로 변환 (여기서 데이터를 확실히 로드)
                            val imageBytes = context.contentResolver.openInputStream(uri)?.use { stream ->
                                stream.readBytes()
                            } ?: throw IllegalStateException("이미지를 읽을 수 없습니다: $uri")

                            Log.d("siria22", "Image: $fileName, Size: ${imageBytes.size} bytes")

                            // 3. ByteArray로 append (Headers에 파일명과 타입 명시)
                            append(
                                key = "images",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                                }
                            )
                        }
                    }
                )
            )

            // 4. 업로드 진행률 콜백
            onUpload { bytesSent, totalBytes ->
                // 외부에서 전달받은 onUpload 함수 호출
                onUpload(bytesSent, totalBytes ?: -1L)
            }
        }
    }
}
