package calculense.platform.filemanager.service

import calculense.platform.filemanager.model.FileLabel

interface IFileLabelService {
    fun createFileLabel(fileLabel: FileLabel):FileLabel
}