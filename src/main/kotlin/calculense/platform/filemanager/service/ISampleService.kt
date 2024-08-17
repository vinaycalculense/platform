package calculense.platform.filemanager.service

import calculense.platform.filemanager.model.Sample

interface ISampleService {
    fun saveSample(sample: Sample): Sample
    fun getSamples(category: String): List<Sample>
}