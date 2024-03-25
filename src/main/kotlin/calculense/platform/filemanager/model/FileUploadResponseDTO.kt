package calculense.platform.filemanager.model

import java.util.UUID

data class FileUploadResponseDTO (
    val requestId:UUID,
    val urls:List<URL>
)