package calculense.platform.filemanager.service

import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.dao.FileLabelRepository
import calculense.platform.filemanager.model.FileLabel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileLabelService:IFileLabelService {

    @Autowired
    lateinit var fileLabelRepository: FileLabelRepository
    override fun createFileLabel(fileLabel: FileLabel): FileLabel {
        if (fileLabelRepository.findFileLabelByName(fileLabel.name.uppercase())!=null){
             throw CalculenseException(errorCode = 400, errorMessage = "File Label already exists")

        }
        fileLabel.name=fileLabel.name.uppercase()
        return fileLabelRepository.save(fileLabel)
    }

    override fun getFileLabelByIDs(fileLabelIds: List<Long>): List<FileLabel> {
        return fileLabelRepository.findFileLabelByIdIn(fileLabelIds)
    }
}